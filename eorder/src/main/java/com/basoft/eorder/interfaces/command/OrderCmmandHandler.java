package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.WxSession;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.application.framework.Event;
import com.basoft.eorder.application.framework.EventProducer;
import com.basoft.eorder.application.wx.api.WechatAPI;
import com.basoft.eorder.application.wx.model.BaseResult;
import com.basoft.eorder.application.wx.model.DataItem;
import com.basoft.eorder.application.wx.model.TemplateMessageReturn;
import com.basoft.eorder.application.wx.model.WxPayJsResp;
import com.basoft.eorder.application.wx.model.WxPayRefundResult;
import com.basoft.eorder.application.wx.model.WxPayResult;
import com.basoft.eorder.batch.job.model.retail.RetailToDoRecoverTempOrder;
//import com.basoft.eorder.batch.job.threads.HotelOrderRecoverThread;
//import com.basoft.eorder.batch.job.threads.retail.RetailOrderRecoverThread;
//import com.basoft.eorder.batch.lock.RedissonUtil;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.InventoryHotelRepository;
import com.basoft.eorder.domain.OrderRepository;
import com.basoft.eorder.domain.OrderService;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.OrderItem;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.StoreTable;
import com.basoft.eorder.domain.retail.InventoryRetailRepository;
import com.basoft.eorder.interfaces.query.ExchangeRateDTO;
import com.basoft.eorder.interfaces.query.OrderQuery;
import com.basoft.eorder.interfaces.query.ProductQuery;
import com.basoft.eorder.interfaces.query.ProductSkuDTO;
import com.basoft.eorder.interfaces.query.activity.discount.DiscountDisplayDTO;
import com.basoft.eorder.interfaces.query.activity.discount.DiscountQuery;
import com.basoft.eorder.interfaces.query.hotel.HotelProductSkuDatePriceDTO;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelDTO;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelQuery;
import com.basoft.eorder.interfaces.query.order.HotelOrderDTO;
import com.basoft.eorder.util.DateUtil;
import com.basoft.eorder.util.RedisUtil;
import com.basoft.eorder.util.UidGenerator;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@CommandHandler.AutoCommandHandler("OrderCommandHandler")
@Transactional
public class OrderCmmandHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private UidGenerator uidGenerator;

    private OrderService orderService;

    private StoreRepository storeRepository;

    private OrderRepository orderRepository;

    @Autowired
    private OrderQuery orderQuery;

    private AppConfigure appConfigure;

    private ProductQuery productQuery;

    // 极光推送
    private EventProducer eventProducer;

    // 折扣详情列表-用于二次价格核查
    private DiscountQuery discountQuery;

//    @Autowired
//    private RedissonUtil redissonUtil;

    @Autowired
    private InventoryHotelQuery inventoryHotelQuery;

    @Autowired
    private InventoryHotelRepository inventoryHotelRepository;

    @Autowired
    private InventoryRetailRepository inventoryRetailRepository;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    public OrderCmmandHandler(UidGenerator uidGenerator,
                              StoreRepository storeRepository,
                              OrderService orderService,
                              OrderRepository orderRepository,
                              AppConfigure appConfigure,
                              ProductQuery productQuery,
                              EventProducer eventProducer,
                              DiscountQuery discountQuery) {
        this.orderService = orderService;
        this.uidGenerator = uidGenerator;
        this.storeRepository = storeRepository;
        this.orderRepository = orderRepository;
        this.appConfigure = appConfigure;
        this.productQuery = productQuery;
        this.eventProducer = eventProducer;
        this.discountQuery = discountQuery;
    }

    /**
     * 【Wechat H5】微信支付下单
     *
     * 电子点餐、酒店、医美和购物
     *
     * @param saveOrder
     * @param context
     * @return
     *
     * @Desc 说明：20200518零售业务订单退款迁移到RetailOrderCommandHandler中。这儿的零售业务订单原有退款保留
     */
    @CommandHandler.AutoCommandHandler(SaveOrder.NAME)
    public WxPayJsResp saveOrder(SaveOrder saveOrder, CommandHandlerContext context) {
        logger.info("Start saveProduct -------");

        // 安全性验证：验证IP地址
        String ip = Objects.toString(context.props().get("realIp"), null);
        if (StringUtils.isBlank(ip)) {
            throw new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND IP");
        }

        WxSession session = (WxSession) context.props().get(AppConfigure.BASOFT_WX_SESSION_PROP);
        // session.getSceneStr()获取的是qrcode_id。对应store_table表中的qrcode_id字段
        logger.info("=========▶ session.getSceneStr() : " + session.getSceneStr());

        // 下单不支付门店不允许调用该接口
        String isPayStore = session.getIsPayStore();
        if ("0".equals(isPayStore)){
            throw new BizException(ErrorCode.PARAM_INVALID, "INVALID INVOKE, IS NOT PAY STORE!");
        }

        Store store = storeRepository.getStore(session.getStoreId());
        if (store == null) {
            logger.error("▶▶▶▶▶▶▶▶▶▶ not found store, session.getStoreId() : " + session.getStoreId());
            throw new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND STORE");
        }

        //验证店铺支付信息填写是否完整，不完整不允许发起支付
        checkStorePay(store);

        // 从门店的桌子列表中过滤出QRCode不为空且等于订单中QrCode的桌子。
        StoreTable table = storeRepository.getStoreTableList(store)
            .stream()
            .filter(dd -> StringUtils.isNotBlank(dd.getQrCodeId()))
            .filter((st) -> st.getQrCodeId().equals(session.getSceneStr()))
            .findFirst()
            .orElseThrow(() -> new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND TABLE"));

        Long orderId = uidGenerator.generate(BusinessTypeEnum.ORDER);
        ExchangeRateDTO erd = productQuery.getNowExchangeRate();

        BigDecimal payAmtUsd = saveOrder.getAmount().multiply(erd.getKrwusdRate()).setScale(2, BigDecimal.ROUND_UP);

        // totalAmount
        BigDecimal totalAmount = saveOrder.getTotalAmount();
        if (totalAmount == null) {
            totalAmount = saveOrder.getAmount();
        }

        // 处理订单备注中的特殊字符
        String cmt = saveOrder.getCmt();
        if(StringUtils.isNotBlank(cmt)) {
            if (hasEmoji(cmt)) {
                cmt = replaceEmoji(cmt);
            }
        }

        // 构造订单信息
        Order order = saveOrder.build(
            orderId,
            session.getStoreId(),
            table.id(),
            session.getOpenId(),
            ip,
            DateUtil.getWxPayNowStr(),
            DateUtil.getFormatStr(DateUtil.plusWxPayDateTime(30, ChronoUnit.MINUTES)),
            payAmtUsd,
            erd.getKrwusdRate(),
            totalAmount,
            cmt,
            // 20190905-新增押金产品的支持
            store.storeType()
        );
        // 补充订单的即时费率
        if(store.chargeType() == 1 || store.chargeType() == 2){
            order.setRateSettle(store.chargeRate());
        } else {
            order.setRateSettle(-1);
        }
        // 设置订单结算金额为-1
        order.setAmountSettle(new BigDecimal(-1));
        logger.info("=========▶ order full info::::" + order);

        // 处理订单中的订单内容列表-start
        // 酒店房间订单业务（房间且不是押金产品）
        //List<Long> skuIdList = order.getItemList().stream().map(OrderItem::getSkuId).collect(Collectors.toList());
        List<Long> skuIdList = new ArrayList<>();
        if (CommonConstants.BIZ_HOTEL_INT == store.storeType() && saveOrder.getOrderType() == CommonConstants.ORDER_TYPE_NORMAL) {
            // 酒店房间下单多天会在item里存放每天的价格，导致sku重复，所以先去重。当然该类去重不影响其他业务类型
            List<OrderItem> noDuplicateItemList = order.getItemList().stream().collect(
                    Collectors.collectingAndThen(Collectors.toCollection(() ->
                            new TreeSet<>(Comparator.comparing(OrderItem::getSkuId))), ArrayList::new));
            skuIdList = noDuplicateItemList.stream().map(OrderItem::getSkuId).collect(Collectors.toList());
        } else {
            skuIdList = order.getItemList().stream().map(OrderItem::getSkuId).collect(Collectors.toList());
        }

        // 订单中订购产品SKU_ID列表
        if (skuIdList.size() > 0) {
            // 根据订购产品的SKU_ID列表查询详细的产品SKU信息
            Map<String, Object> param = Maps.newHashMap();
            param.put("skuIds", skuIdList);
            List<ProductSkuDTO> skuList = productQuery.getProductSkuListByMap(param);

            // 查询到的订购产品SKU详情信息列表不为空
            if (skuList != null && skuList.size() > 0) {

                // 判断订单中的sku_id列表和查询到的产品sku信息列表的数量是否一致。一致则订单中的产品sku全部合法，否则存在不合法的sku_id
                if (skuIdList.size() == skuList.size()) {
                    // 订单二次核价-start
                    // 酒店房间业务核价
                    if (CommonConstants.BIZ_HOTEL_INT == store.storeType() && saveOrder.getOrderType() == CommonConstants.ORDER_TYPE_NORMAL) {
                        try{
                            Long prodSkuId = skuIdList.get(0);
                            logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-prodSkuId>>>>>>" + prodSkuId);

                            // 0、准备sku信息
                            // 将skuList转换为Map<skuId,ProductSkuDTO>格式，循环设置或检查订单时便于获取ProductSkuDTO对象
                            Map<Long, ProductSkuDTO> maps = skuList.stream().collect(Collectors.toMap(ProductSkuDTO::getId, sku -> sku));
                            ProductSkuDTO productSkuDTO = maps.get(prodSkuId);
                            BigDecimal productSkuPrice = productSkuDTO.getPriceKor();
                            logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-productSkuPrice>>>>>>" + productSkuPrice);

                            // 1、数据库中的sku按日期价格信息
                            // 根据预定日期和预定的prod sku id获取该sku每天的有效价格（韩币单位）
                            List<HotelProductSkuDatePriceDTO> productSkuDatePriceList = inventoryHotelQuery
                                    .getProductSkuDatePriceBySkuIdList(prodSkuId, saveOrder.getReseveDtfrom(), saveOrder.getReseveDtto());
                            Map<String, HotelProductSkuDatePriceDTO> hotelProductSkuDatePriceMap = productSkuDatePriceList
                                    .stream().collect(Collectors.toMap(HotelProductSkuDatePriceDTO::getPriceDate, sku -> sku));
                            logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-库存价格列表>>>>>>" + hotelProductSkuDatePriceMap);

                            // 2、订单列表内容
                            Map<String, OrderItem> dateOrderItemMap = order.getItemList().stream()
                                    .collect(Collectors.toMap(OrderItem::getSkuNmChn, orderItem -> orderItem));
                            logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-订单item列表>>>>>>" + dateOrderItemMap);

                            // 3、日期列表
                            List<String> dateAllList = DateUtil.findDataAll(saveOrder.getReseveDtfrom(),
                                    saveOrder.getReseveDtto(), 1);
                            dateAllList.remove(saveOrder.getReseveDtto());
                            logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查日期列表：" + dateAllList);

                            // 4、循环验证每日价格
                            BigDecimal checkTotalAmount = BigDecimal.ZERO;
                            for(String date : dateAllList){
                                logger.info("\n\n");
                                logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-日期>>>>>>" + date + "开始");
                                // 按日期去订单信息
                                OrderItem orderItem = dateOrderItemMap.get(date);
                                if(orderItem == null){
                                    logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-orderItem为空，异常抛出！！！");
                                    throw new BizException(ErrorCode.PARAM_INVALID,"Order Item Error(orderItem)!");
                                } else {
                                    // 按日期取库存日期价格
                                    HotelProductSkuDatePriceDTO hotelProductSkuDatePriceDTO = hotelProductSkuDatePriceMap.get(date);
                                    if(hotelProductSkuDatePriceDTO == null){
                                        logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-hotelProductSkuDatePriceDTO为空！！！");
                                        // 不存在库存日期价格，比较产品sku价格
                                        if(orderItem.getPrice().compareTo(productSkuPrice) != 0){
                                            logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-订单价格和产品sku价格不一致，抛出异常！！！");
                                            throw new BizException(ErrorCode.PARAM_INVALID,"Order Price Error(original)!");
                                        } else {
                                            checkTotalAmount = checkTotalAmount.add(productSkuPrice);
                                        }
                                    } else {
                                        logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-hotelProductSkuDatePriceDTO>>>>>>" + hotelProductSkuDatePriceDTO);
                                        // 存在库存日期价格，比较库存价格中的有效价格
                                        BigDecimal inventEffectPrice = new BigDecimal(hotelProductSkuDatePriceDTO.getEffectivePriceKor());
                                        if(orderItem.getPrice().compareTo(inventEffectPrice) != 0){
                                            logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-订单价格和库存有效价格不一致，抛出异常！！！");
                                            throw new BizException(ErrorCode.PARAM_INVALID,"Order Price Error(inventory)!");
                                        } else {
                                            checkTotalAmount = checkTotalAmount.add(inventEffectPrice);
                                        }
                                    }
                                }
                                logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-日期>>>>>>" + date + "结束");
                                logger.info("\n\n");
                            }

                            // 5、总价
                            if(totalAmount.compareTo(checkTotalAmount) != 0){
                                throw new BizException(ErrorCode.PARAM_INVALID,"Order Amount Error!");
                            }
                        } catch (Exception e){
                            throw new BizException(ErrorCode.PARAM_INVALID,e.getMessage());
                        }
                    }
                    // 非酒店房间业务核价
                    else {
                        // 将skuList转换为Map<skuId,ProductSkuDTO>格式，循环设置或检查订单时便于获取ProductSkuDTO对象
                        Map<Long, ProductSkuDTO> maps = skuList.stream().collect(Collectors.toMap(ProductSkuDTO::getId, sku -> sku));

                        // 查询获取当前店铺的折扣产品列表（含折扣价格），用于价格的二次核查
                        Map<String, Object> discountDisplayParam = Maps.newHashMap();
                        discountDisplayParam.put("storeId", String.valueOf(session.getStoreId()));
                        List<DiscountDisplayDTO> discountDisplayList = discountQuery.getDiscountDisplayList(discountDisplayParam);
                        final Map<Long, DiscountDisplayDTO> discountDisplayMap = discountDisplayList.stream()
                            .collect(Collectors.toMap(DiscountDisplayDTO::getProdSkuId, discountDisplayDTO -> discountDisplayDTO));

                        // 订单二次核价和订单信息补充（补充韩文名称和中文名称）
                        order.getItemList().forEach(item -> {
                            Long skuId = item.getSkuId();
                            ProductSkuDTO sku = maps.get(skuId);

                            // 订单提交价格核对，防止价格被黑--start
                            logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-START◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                            // 根据折扣价进行核价
                            if (discountDisplayMap.containsKey(skuId)) {
                                DiscountDisplayDTO discount = discountDisplayMap.get(skuId);
                                // 折扣价
                                BigDecimal discountPrice = new BigDecimal(discount.getDiscPrice());
                                // 订单价
                                BigDecimal orderPrice = item.getPrice();
                                logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-折扣-折扣价格【" + discountPrice.toString()
                                    +"】，订单价格【" + orderPrice.toString() +"】◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                                // 价格核查
                                if (discountPrice.compareTo(orderPrice) != 0) {
                                    throw new BizException(ErrorCode.PARAM_INVALID,"Order Price Error!");
                                }
                            }
                            // 根据sku product价格核价
                            else {
                                BigDecimal dbPrice = sku.getPriceKor();
                                BigDecimal orderPrice = item.getPrice();
                                logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-SKU价格【" + dbPrice.toString()
                                    +"】，订单价格【" + orderPrice.toString() +"】◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                                if (dbPrice.compareTo(orderPrice) != 0) {
                                    throw new BizException(ErrorCode.PARAM_INVALID);
                                }
                            }
                            logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-END◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                            // 订单提交价格核对，防止价格被黑--end

                            item.setSkuNmKor(sku.getNameKor());
                            item.setSkuNmChn(sku.getNameChn());
                        });
                    }
                    // 订单二次核价-end


                    // 20190903-酒店下单库存管理-start
                    int storeType = store.storeType();
                    int orderType = saveOrder.getOrderType();
                    logger.info("<><><><><><><><><>><><><><>商店类型为{}，订单类型为{}<><><><><><><><><>><><><><>", storeType, orderType);
                    // 酒店业务且是正常商品则处理库存
                    if (CommonConstants.BIZ_HOTEL_INT == storeType && orderType == CommonConstants.ORDER_TYPE_NORMAL) {
                        logger.info("<><><><><><><><><>><><><><>【酒店下单】支付前库存验证和库存削减<><><><><><><><><>><><><><>");

                        // 具体到酒店商户锁
                        StringBuilder hotelLockKey = new StringBuilder(CommonConstants.HOTEL_INVENTORY_LOCK).append(store.id());
                        // RLock lock = redissonUtil.getRLock(hotelLockKey.toString());
//                        RLock lock = redissonUtil.getFairLock(hotelLockKey.toString());

                        // 获取锁，并且防止死锁。60秒后自动释放。
//                        lock.lock(60, TimeUnit.SECONDS);

                        try {
                            logger.info("<><><><><><><><><>><><><><>【酒店下单】获取到酒店下单验证削减锁<><><><><><><><><>><><><><>");
                            // 验证房间库存。
                            Map<String, Object> hiParam = Maps.newHashMap();
                            hiParam.put("storeId", store.id());
                            // !!!特别说明酒店房间下单预约目前规则：一次只能预约一种房间，即一个sku，并且预约数量为1
                            hiParam.put("skuId", String.valueOf(skuIdList.get(0)));
                            hiParam.put("startDate", saveOrder.getReseveDtfrom());
                            hiParam.put("endDate", saveOrder.getReseveDtto());
                            List<InventoryHotelDTO> inventoryHotelList = inventoryHotelQuery.getHotelInventoryListByConditions(hiParam);
                            // 过滤库存不足的日期库存信息
                            List<InventoryHotelDTO> filteredInventoryHotelList = inventoryHotelList.stream().filter(inventoryHotelDTO -> {
                                logger.info("【库存验证】库存信息>>>>>>" + inventoryHotelDTO);
                                // 存在房间关闭则直接认为库存不足
                                if("0".equals(inventoryHotelDTO.getIsOpening())){
                                    return true;
                                }
                                int invTotal = Integer.valueOf(inventoryHotelDTO.getInvTotal());
                                int invUsed = Integer.valueOf(inventoryHotelDTO.getInvUsed());
                                int invBalance = invTotal - invUsed;
                                logger.info("【库存验证】验证日期，库存余量分别为>>>>>>" + inventoryHotelDTO.getInvDate() + "," + invBalance);
                                // 库存不足
                                if (invBalance <= 0) {
                                    return true;
                                }
                                // 库存大于1
                                else {
                                    return false;
                                }
                            }).collect(Collectors.toList());

                            boolean canOrder = filteredInventoryHotelList == null || filteredInventoryHotelList.size() <= 0 ? true : false;
                            // 可以继续预订
                            if (canOrder) {
                                // insert Order to Temp
                                orderRepository.saveOrderTempWithInventory(order);
                            }
                            // 库存不足，无法继续预订。释放酒店下单验证削减锁，并抛出提示异常
                            else {
                                throw new BizException(ErrorCode.INVENTHOTEL_INSUFFICIENT);
                            }
                        } finally {
                            // lock.unlock();
//                            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
//                                lock.unlock();
//                                logger.info("【酒店下单】酒店下单核心逻辑已处理，库存锁释放，进入支付......");
//                            }
                        }
                    }
                    // 非酒店业务和酒店业务押金产品
                    else {
                        // insert Order to Temp
                        orderRepository.saveOrderTemp(order);
                    }
                    // 20190903-酒店下单库存管理-start



                    Long transId = orderRepository.getOrderTransId(order.getId());
                    logger.info("=========▶ transId::::" + transId);
                    // http://domain/eorder/wechat/api/v1/order_pay
                    String notifyUrl = appConfigure.get(AppConfigure.BASOFT_WECHAT_NOTIFY_URL);

                    //to Wechat
                    WxPayJsResp resp = orderService.sendWechatOrder(store, order, ip, notifyUrl, getCertStream(), transId);
                    logger.info("=========▶ orderService sendWechatOrder result::::" + resp);
                    return resp;
                }
                // 订单中的存在不合法的订购内容
                else {
                    logger.error("=======>>>> order details are not valid data <<<<======");
                }
            }
            // 查询到的订购产品SKU详情信息列表为空，即订单中的SKU_ID全部不合法
            else {
                logger.error("=======>>>> order details are not valid data <<<<======");
            }
        }
        // 订单中无订购内容
        else {
            logger.error("========>>>> product is empty !!!! <<<<======");
        }
        // 处理订单中的订单内容列表-end

        return failResp(order, "FAIL", "payment is fail");
    }

    /**
     * 验证店铺支付信息是否正确
     * 验证字段:merchant_id,merchant_nm,gateway_pw,transid_type,currency
     *
     * @Param store
     * @return void
     * @author Dong Xifu
     * @date 2019/9/11 3:29 下午
     */
    private void checkStorePay(Store store) {
        if(StringUtils.isBlank(store.merchantId())
        ||StringUtils.isBlank(store.merchantNm())
        ||StringUtils.isBlank(store.gatewayPw())
        ||StringUtils.isBlank(store.currency())
        ){
            logger.error("▶▶▶▶▶▶▶▶▶▶店铺支付信息参数不全");
            throw new BizException(ErrorCode.STORE_PAYINFO_WRONG);
        }

        if (store.merchantId().length() < 10
            || store.merchantNm().length() < 2
            || store.gatewayPw().length() < 10
            || store.currency().length() < 2) {
            logger.error("▶▶▶▶▶▶▶▶▶▶店铺支付信息错误");
            throw new BizException(ErrorCode.STORE_PAYINFO_WRONG);
        }
        if (!"BT".equals(store.transidType()) && !"BA".equals(store.transidType())) {
            logger.error("▶▶▶▶▶▶▶▶▶transidType信息错误");
            throw new BizException(ErrorCode.STORE_PAYINFO_WRONG);
        }
    }


    /**
     * @Title 判断是否存在特殊字符串
     * @Param content
     * @return boolean
     * @author Dong Xifu
     * @date 2019/5/10 下午1:59
     */
    private static boolean hasEmoji(String content){
        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher = pattern.matcher(content);
        if(matcher .find()){
            return true;
        }
        return false;
    }

    /**
     * @Title 替换字符串中的emoji字符
     * @Param str
     * @return java.lang.String
     * @author Dong Xifu
     * @date 2019/5/10 下午1:59
     */
    private static String replaceEmoji(String str) {
        if (!hasEmoji(str)) {
            return str;
        } else {
            str = str.replaceAll("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", " ");
            return str;
        }


    }

    private WxPayJsResp failResp(Order order, String returnCode, String ReturnMsg) {
        WxPayJsResp resp = new WxPayJsResp.Builder()
            .orderId(order.getId() + "")
            .build();
        resp.setReturn_code(returnCode);    //"FAIL"
        resp.setReturn_msg(ReturnMsg);        //"payment is fail"
        return resp;
    }

    private String getCertStream() {
        String certPath = appConfigure.get(AppConfigure.BASOFT_WECHAT_CERT_PATH);
        /*byte[] certData;
        InputStream certStream = null;
        File file;
		try {
			file = new ClassPathResource(certPath).getFile();
	        certStream = new FileInputStream(file);
	        certData = new byte[(int) file.length()];
	        certStream.read(certData);
	        certStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
        return certPath;
    }

    /**
     * 餐厅的订单确认，酒店，医美和购物的订单确认方法是confirmOrderReserve
     * 餐厅服务员接单
     *
     * @param acceptOrder
     * @return
     * @author Dong Xifu
     * @date 2019/4/11 下午5:24
     */
    @CommandHandler.AutoCommandHandler(AcceptOrder.NAME)
    @Transactional
    public int acceptOrder(AcceptOrder acceptOrder) {
        acceptOrder.setStatus(AcceptOrder.Status.ACEEPT.getStatusCode());
        return orderService.acceptOrder(acceptOrder);
    }

    /**
     * 修改订单状态
     *
     * @param acceptOrder
     * @return
     * @author Dong Xifu
     * @date 2019/4/11 下午5:32
     */
    @CommandHandler.AutoCommandHandler(AcceptOrder.UPORDERSTATUS)
    @Transactional
    public int upOrderStatus(AcceptOrder acceptOrder) {
        Order order = orderRepository.getOrder(acceptOrder.getOrderId());
        // 订单不为空
        if (order != null) {
            // 订单状态为已退款
            if (order.getStatus() == Order.Status.CANCELED.getStatusCode()) {
                // 已退款订单不允许修改为完成状态
                if (acceptOrder.getStatus() == Order.Status.COMPLETE.getStatusCode()) {
                    throw new BizException(ErrorCode.ORDER_COMPLETE_CANCELD);
                }
            }
        }
        return orderService.acceptOrder(acceptOrder);
    }

    /**
     * 服务员接单
     *
     * @param saveOrder
     * @return
     * @author Dong Xifu
     * @date 2019/4/11 下午5:24
     */
    @CommandHandler.AutoCommandHandler(SaveOrder.SAVE_STORE_CUSTNO)
    @Transactional
    public int saveStoreCustNo(SaveOrder saveOrder) {
        return orderRepository.saveStoreCustNo(saveOrder);
    }

    /**
     * 修改订单备注
     *
     * @Param saveOrder
     * @return int
     * @author Dong Xifu
     * @date 2019/8/21 下午1:54
     */
    @CommandHandler.AutoCommandHandler(SaveOrder.UP_SHIPPING_CMT)
    @Transactional
    public int upShippingCmt(SaveOrder saveOrder) {
        return orderRepository.upShippingCmt(saveOrder);
    }



    /**
     * 订单取消[点餐退款]
     *
     * @param saveCancelOrder
     * @param context
     * @return
     */
    @CommandHandler.AutoCommandHandler(SaveCancelOrder.NAME)
    public WxPayRefundResult saveCancelOrder(SaveCancelOrder saveCancelOrder, CommandHandlerContext context) {
        // 20190712验证退款权限------start
        logger.debug("=========▶ 点餐退款权限验证=========▶START" );
        UserSession userSession = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        if(userSession == null){
            throw new BizException(ErrorCode.SYS_TIMEOUT);
        }
        boolean permitRefund = false;
        int accountType = userSession.getAccountType();
        int accountRole = userSession.getAccountRole();
        logger.info("=========▶ 点餐退款权限信息【accountType||accountRole】=" + accountType + "||" + accountRole);
        // 门店超级管理员：如果账号类型为1
        if (accountType == 1) {
            permitRefund = true;
        }
        // 门店操作员
        else if(accountType == 2){
            // 21-店长 22-退款管理员（退款、接单权限等等）
            if(accountRole == 21 || accountRole == 22){
                permitRefund = true;
            } else {
                permitRefund = false;
            }
        }
        /*// 为了防止退款阻塞，此处对于其他值（其他值或0，0代表数据无值或其他情况）设置为可退款
        else {
            permitRefund = true;
        }*/
        // 退款阻塞，此处对于其他值（其他值或0，0代表数据无值或其他情况）设置为不可退款
        else {
            permitRefund = false;
        }

        // 越权退款进行异常终止
        if(!permitRefund){
            logger.debug("=========▶ 点餐退款权限验证结束=========▶不▶允许退款" );
            // 抛出业务异常信息
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }
        logger.debug("=========▶ 点餐退款权限验证结束=========▶▶允许退款" );
        // 20190712验证退款权限------end



        logger.debug("Start saveCancelOrder -------");
        // WxSession session = (WxSession) context.props().get(AppConfigure.BASOFT_WX_SESSION_PROP);
        String ip = Objects.toString(context.props().get("realIp"), null);
        Long orderId = saveCancelOrder.getOrderId();
        logger.info("=========▶ Cancel Order orderId : " + orderId);

        Order orderCheck = orderRepository.getOrder(orderId);
        if ((!Order.Status.get(orderCheck.getStatus()).equals(Order.Status.PAYMENY)) &&            //오더 지불 상태
            (!Order.Status.get(orderCheck.getStatus()).equals(Order.Status.ACEEPT)) &&            //오더 접수 상태
            (!Order.Status.get(orderCheck.getStatus()).equals(Order.Status.CANCEL_FAIL))) {        //Fail 상태
            WxPayRefundResult resp = null;
            resp = new WxPayRefundResult();
            resp.setOut_trade_no(orderId.toString());
            resp.setReturn_code("FAIL");    //"FAIL"
            resp.setReturn_msg("환불이 가능한 주문이 아닙니다.");        //"payment is fail"
            return resp;
        }

        /*String orderDt = orderCheck.getTimeStart().substring(0, 8);
        SimpleDateFormat dtformat = new SimpleDateFormat("yyyyMMdd");
        Date time = new Date();
        String today = dtformat.format(time);
        // 테스트로 인하여 전일 오더 삭제 못하게 하던 것을 임시로 주석 처리 함 by dikim on 20190410
        if (!(today.equals(orderDt))) {		//Fail 상태
        	WxPayRefundResult resp = null;
        	resp = new WxPayRefundResult();
        	resp.setOut_trade_no(orderId.toString());
            resp.setReturn_code("FAIL");	//"FAIL"
            resp.setReturn_msg("당일 주문건에 한하여 환불이 가능합니다.");		//"payment is fail"
            return resp;
        }*/

        // 验证退款操作者所属门店ID和订单的门店ID是否一致（安全防范）
        Long sessionStoreId = userSession.getStoreId();
        Long orderStoreId = orderCheck.getStoreId();
        logger.info("=========▶ userSessionStoreId : " + sessionStoreId);
        logger.info("=========▶ orderStoreId : " + orderStoreId);
        if(sessionStoreId != null && orderStoreId != null){
            if(!(sessionStoreId.longValue() == orderStoreId.longValue())){
                // 抛出业务异常信息
                throw new BizException(ErrorCode.BIZ_EXCEPTION);
            }
        } else {
            // 抛出业务异常信息
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }

        Store store = storeRepository.getStore(orderCheck.getStoreId());
        if (store == null) {
            WxPayRefundResult resp = null;
            resp = new WxPayRefundResult();
            resp.setOut_trade_no(orderId + "");
            resp.setReturn_code("FAIL");    //"FAIL"
            resp.setReturn_msg("Order ID or Store is null!");
            return resp;
        }

        WxPayRefundResult resp = null;
        resp = new WxPayRefundResult();

        ConfirmOrder confirmOrder = null;
        confirmOrder = new ConfirmOrder();

        confirmOrder.setOrderId(saveCancelOrder.getOrderId());
        confirmOrder.setCancelReason(saveCancelOrder.getCancelReason());

        if (store.isPaySet() == 1) {

            orderCheck.setStatus(Order.Status.CANCEL_REQ);
            // Cancel order status update (Cancel Request)
            orderRepository.updateOrder(orderCheck);

            resp = orderService.sendWechatCancelOrder(store, orderCheck, ip, getCertStream());
        }
        else if (store.isPaySet() == 0) {

            orderCheck.setStatus(Order.Status.CANCELED);
            // Cancel order status update (Cancel success)
            orderRepository.updateOrder(orderCheck);
            orderRepository.cancelOrderReserve(confirmOrder);

            resp.setOut_trade_no(orderId + "");
            resp.setReturn_code("SUCCESS");    //"FAIL"
            resp.setResult_code("SUCCESS");
            resp.setReturn_msg("Cancel Success");
        }

        Order order = orderRepository.getOrder(orderId);
        sendTemplateMsg(order, store, confirmOrder, true);

        return resp;
    }

    /**
     * 订单取消
     *
     * @param saveCancelOrder
     * @param context
     * @return
     */
    @CommandHandler.AutoCommandHandler("getAccessToken")
    public WxPayRefundResult getAccessToken(SaveCancelOrder saveCancelOrder, CommandHandlerContext context) {
        logger.debug("Start getAccessToken -------");

        String token = Objects.toString(redisUtil.get("wx_token"), null);

        WxPayRefundResult resp = null;
        resp = new WxPayRefundResult();
        resp.setReturn_code("FAIL");    //"FAIL"
        resp.setReturn_msg(token);
        return resp;
    }

    /**
     * 根据订单编号查询取消的订单信息
     *
     * @param saveCancelOrder
     * @param context
     * @return
     */
    @CommandHandler.AutoCommandHandler("queryCancelOrder")
    public WxPayRefundResult queryCancelOrder(SaveCancelOrder saveCancelOrder, CommandHandlerContext context) {
        logger.debug("Start queryCancelOrder -------");
        // WxSession session = (WxSession) context.props().get(AppConfigure.BASOFT_WX_SESSION_PROP);
        String ip = Objects.toString(context.props().get("realIp"), null);

        Long orderId = saveCancelOrder.getOrderId();
        Order orderCheck = orderRepository.getOrder(orderId);

        Store store = storeRepository.getStore(orderCheck.getStoreId());
        if (store == null) {
            WxPayRefundResult resp = null;
            resp = new WxPayRefundResult();
            resp.setOut_trade_no(orderId + "");
            resp.setReturn_code("FAIL");    //"FAIL"
            resp.setReturn_msg("Order ID or Store is null !");        //"payment is fail"
            return resp;
        }

        WxPayRefundResult resp = orderService.queryWechatCancelOrder(store, orderCheck, ip, getCertStream());
        return resp;
    }

    /**
     * 根据订单编号查询订单信息
     *
     * @param saveCancelOrder
     * @param context
     * @return
     */
    @CommandHandler.AutoCommandHandler("queryOrder")
    public WxPayResult queryOrder(SaveCancelOrder saveCancelOrder, CommandHandlerContext context) {
        logger.debug("Start queryOrder -------");

        // WxSession session = (WxSession) context.props().get(AppConfigure.BASOFT_WX_SESSION_PROP);
        String ip = Objects.toString(context.props().get("realIp"), null);

        // 根据订单编号查询订单
        Long orderId = saveCancelOrder.getOrderId();
        Order orderCheck = orderRepository.getOrder(orderId);

        // 获取门店信息
        Store store = storeRepository.getStore(orderCheck.getStoreId());
        if (store == null) {
            WxPayResult resp = null;
            resp = new WxPayResult();
            resp.setOut_trade_no(orderId + "");
            //"FAIL"
            resp.setReturn_code("FAIL");
            resp.setReturn_msg("Order ID or Store is null !");
        }

        // 调用微信平台接口查询订单信息
        WxPayResult resp = orderService.queryWechatOrderManual(store, orderCheck, ip, saveCancelOrder, getCertStream());
        return resp;
    }

    /**
     * 逻辑完全同queryOrder方法，不知道该方法何用！！！
     * @param order
     * @param context
     * @return
     */
    @CommandHandler.AutoCommandHandler("queryOrderTemp")
    public WxPayResult queryOrderTemp(SaveCancelOrder order, CommandHandlerContext context) {
        logger.debug("Start queryOrderWechat -------");
        String ip = Objects.toString(context.props().get("realIp"), null);

        // 根据订单编号查询订单
        Long orderId = order.getOrderId();
        Order orderCheck = orderRepository.getOrderTemp(orderId);

        // 查询门店信息
        Store store = storeRepository.getStore(orderCheck.getStoreId());
        if (store == null) {
            WxPayResult resp = null;
            resp = new WxPayResult();
            resp.setOut_trade_no(orderId + "");
            resp.setReturn_code("FAIL");
            resp.setReturn_msg("Order ID or Store is null !");
        }

        // 调用微信平台接口查询订单信息
        WxPayResult resp = orderService.queryWechatOrder(store, orderCheck, ip, getCertStream());
        return resp;
    }

    /**
     * 宾馆订单确认
     * 20200108：酒店、医美、购物的订单确认。餐厅的订单确认是方法acceptOrder
     *
     * 店铺类型(1:餐厅 2:医院 3:购物 4:酒店)
     *
     * @param confirmOrder
     * @param context
     * @return
     */
    @CommandHandler.AutoCommandHandler(ConfirmOrder.NAME_CONFIRM_RESERVE)
    @Transactional
    public BaseResult confirmOrderReserve(ConfirmOrder confirmOrder, CommandHandlerContext context) {
        BaseResult baseResult = new BaseResult();

        // query order info
        Order order = orderRepository.getOrder(confirmOrder.getOrderId());
        // if order is null,directly return
        if (order == null) {
            baseResult.setReturn_code("FAIL");
            baseResult.setReturn_msg("Order information is wrong!");
            return baseResult;
        }

        // query store info
        Store store = storeRepository.getStore(order.getStoreId());
        //医院,酒店
        if (store.storeType() == 2 || store.storeType() == 4) {
            //医院,酒店订单，且订单状态为4或5状态的时候可以预约确定
            if ((!Order.Status.get(order.getStatus()).equals(Order.Status.PAYMENY)) &&       //오더 지불 상태  订单支付成功状态
                    (!Order.Status.get(order.getStatus()).equals(Order.Status.ACEEPT))) {    //예약 확정 상태 订单确定状态
                baseResult.setReturn_code("FAIL");
                baseResult.setReturn_msg("Order status is wrong !");
                return baseResult;
            }
        }

        // 酒店
        if (store.storeType() == 4) {
            if(order.getOrderType() == 1) {
                logger.info("正式订单，接单校验库存-------");

                // 酒店接单时检查库存
                boolean canAccept = checkHotelInventory(order, store);
                if (!canAccept) {
                    baseResult.setReturn_code("FAIL");
                    baseResult.setReturn_msg(" Sold out!방이 매진되다!");
                    return baseResult;
                }
            }
            confirmOrder.setStatus(AcceptOrder.Status.ACEEPT.getStatusCode());
            // 接单时填写备注备忘信息
            int id_info = orderService.confirmOrderHotel(confirmOrder);
        }

        // 医院
        else if (store.storeType() == 2) {
            /**
             * 接单流程：用户预约，选定日期和上下午（上午值为1 下午值为2）；接单员接单确认用户预约日期和上下午，
             * 接单员可以修改上下午（电话沟通完再改），同时用户那边也可以显示。
             */
            // 接单时预约日期和时间会从用户填写的订单里带出来。接单时检查预约订单的预约日期和时间是否为空，如果有一项为空则不允许接单。
            if (confirmOrder.getReseveDtfrom() == null || confirmOrder.getReseveConfirmtime() == null ||
                    confirmOrder.getReseveDtfrom().trim() == "" || confirmOrder.getReseveConfirmtime().trim() == "") {
                baseResult.setReturn_code("FAIL");
                baseResult.setReturn_msg("Input confirm date and time !");
                return baseResult;
            }
            // 예약 확정 상태
            confirmOrder.setStatus(AcceptOrder.Status.ACEEPT.getStatusCode());
            // 更新接单备注备忘信息，以及沟通后的新的预约时间（上下午，日期无法修改）
            int id_info = orderService.confirmOrderClinic(confirmOrder);
        }

        // 购物
        else if (store.storeType() == 3) {
            baseResult.setReturn_code("FAIL");
            baseResult.setReturn_msg("Order status is wrong!");

            if (confirmOrder.getStatus() == 5) {    //접수
                if ((!Order.Status.get(order.getStatus()).equals(Order.Status.PAYMENY)) &&            //오더 지불 상태
                        (!Order.Status.get(order.getStatus()).equals(Order.Status.ACEEPT))) {        //예약 확정 상태
                    return baseResult;
                }
                confirmOrder.setStatus(AcceptOrder.Status.ACEEPT.getStatusCode());
            } else if (confirmOrder.getStatus() == 10) {    //상품준비
                if ((!Order.Status.get(order.getStatus()).equals(Order.Status.PAYMENY)) &&            //오더 지불 상태
                        (!Order.Status.get(order.getStatus()).equals(Order.Status.ACEEPT))) {        //예약 확정 상태
                    return baseResult;
                }
                confirmOrder.setStatus(AcceptOrder.Status.SHIPPING_REDY.getStatusCode());
            } else if (confirmOrder.getStatus() == 11) {    //배송처리
                if ((!Order.Status.get(order.getStatus()).equals(Order.Status.PAYMENY)) &&            //오더 지불 상태
                        (!Order.Status.get(order.getStatus()).equals(Order.Status.ACEEPT)) &&            //오더 지불 상태
                        (!Order.Status.get(order.getStatus()).equals(Order.Status.SHIPPING_REDY))) {        //상품 준비 상태
                    return baseResult;
                }
                confirmOrder.setStatus(AcceptOrder.Status.SHIPPING_COMP.getStatusCode());
                if (!(confirmOrder.getShippingNo() == null)) {
                    int id_info = orderService.confirmOrderShopping(confirmOrder);
                }
            } else {
                baseResult.setReturn_code("FAIL");
                baseResult.setReturn_msg("Check order status !");
                return baseResult;
            }
        }

        // 更新订单状态
        int id = orderService.acceptOrderHotel(confirmOrder);
        baseResult.setReturn_code("SUCCESS");
        baseResult.setReturn_msg("OK");

        // send template msg
        sendTemplateMsg(order, store, confirmOrder, false);

        return baseResult;
    }

    /**
     * 酒店订单确认时，检查库存是否充足。
     * 防止用户长时间停滞在支付页面最终选择支付导致的超卖情况发生
     * @param order
     * @param store
     * @return
     */
    private boolean checkHotelInventory(Order order, Store store) {
        boolean canAccept = true;
        // 1、根据订单ID查询订单对应的预定期间及对应的prodSKU等
        HotelOrderDTO hotelOrderInfo = orderRepository.getHotelOrderInfo(order.getId());

        // 2、 查询订单中预定的酒店房间的库存信息-start
        Map<String, Object> hiParam = Maps.newHashMap();
        hiParam.put("storeId", store.id());
        // !!!特别说明酒店房间下单预约目前规则：一次只能预约一种房间，即一个sku，并且预约数量为1
        hiParam.put("skuId", String.valueOf(hotelOrderInfo.getProdSkuId()));
        hiParam.put("startDate", hotelOrderInfo.getReseveDtfrom());
        hiParam.put("endDate", hotelOrderInfo.getReseveDtto());

        List<InventoryHotelDTO> inventoryHotelList = inventoryHotelQuery.getHotelInventoryListByConditions(hiParam);

        if(inventoryHotelList == null || inventoryHotelList.size() == 0){
            // true 无库存设置，放行即可
            return canAccept;
        }
        // 将期间库存转换为map方式
        Map<String, InventoryHotelDTO> inventoryHotelMap = inventoryHotelList.stream().collect(Collectors.toMap(InventoryHotelDTO::getInvDate, sku -> sku));
        // 2、 查询订单中预定的酒店房间的库存信息-end

        // 3、查询预订且已经被订单确认或订单完成的明细
        List<HotelOrderDTO> hotelOrderDetailList = orderRepository.getHotelOrderDetailList(hiParam);
        if(hotelOrderDetailList ==  null || hotelOrderDetailList.size() == 0){
            // true 无库存设置，放行即可
            return canAccept;
        }
        Map<String, List<HotelOrderDTO>> hotelOrderDetailMap = hotelOrderDetailList.stream().collect(Collectors.groupingBy(HotelOrderDTO::getReserveDate));

        // 4、明细汇总和库存相比
        for(String reserveDate : hotelOrderDetailMap.keySet()){
            // 已经订单确认或完成的所有效占用库存数量
            int rdDSize = hotelOrderDetailMap.get(reserveDate).size();

            InventoryHotelDTO ihdto = inventoryHotelMap.get(reserveDate);
            // 不存在对应日期库存，即库存无限制
            if(ihdto == null){
                logger.info("【接单确认】日期【{}】无库存限制！",reserveDate);
                continue;
            }
            // 存在对应日期库存
            else {
                // 酒店商户设置的日期库存数量
                int rdISize = Integer.valueOf(inventoryHotelMap.get(reserveDate).getInvTotal());
                if(rdISize > 0){
                    // 已经订单确认或完成的所有效占用库存数量 大于等于 酒店商户设置的日期库存数量
                    if(rdDSize >= rdISize){
                        canAccept = false;
                        logger.info("【接单确认】日期【{}】库存不足！已用库存：{}，设置库存：{}",reserveDate,rdDSize,rdISize);
                        break;
                    }
                }
                // 库存等于0，无库存，无法接单。既然已经下单成功，就不需要判断该日期是否对外开放该房间
                else if(rdISize == 0){
                    canAccept = false;
                    break;
                }
                // 库存小于等于0，库存无限制
                else if(rdISize < 0){
                    continue;
                }
            }
        }

        return canAccept;
    }

    /**
     * 宾馆订单，医美订单和购物订单取消
     *
     * @param confirmOrder
     * @param context
     * @return
     */
    @CommandHandler.AutoCommandHandler(ConfirmOrder.NAME_CANCEL_RESERVE)
    public WxPayRefundResult cancelOrderReserve(ConfirmOrder confirmOrder, CommandHandlerContext context) {
        // 20190712验证退款权限------start
        logger.debug("=========▶ 非点餐业务退款权限验证=========▶START" );
        UserSession userSession = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        if(userSession == null){
            throw new BizException(ErrorCode.SYS_TIMEOUT);
        }
        boolean permitRefund = false;
        int accountType = userSession.getAccountType();
        int accountRole = userSession.getAccountRole();
        logger.info("=========▶ 非点餐业务退款权限信息【accountType||accountRole】=" + accountType + "||" + accountRole);
        // 门店超级管理员：如果账号类型为1
        if (accountType == 1) {
            permitRefund = true;
        }
        // 门店操作员
        else if(accountType == 2){
            // 21-店长 22-退款管理员（退款、接单权限等等）
            if(accountRole == 21 || accountRole == 22){
                permitRefund = true;
            } else {
                permitRefund = false;
            }
        }
        /*// 为了防止退款阻塞，此处对于其他值（其他值或0，0代表数据无值或其他情况）设置为可退款
        else {
            permitRefund = true;
        }*/
        // 退款阻塞，此处对于其他值（其他值或0，0代表数据无值或其他情况）设置为不可退款
        else {
            permitRefund = false;
        }

        // 越权退款进行异常终止
        if(!permitRefund){
            logger.debug("=========▶ 非点餐退款权限验证结束=========▶不▶允许退款" );
            // 抛出业务异常信息
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }
        logger.debug("=========▶ 非点餐业务退款权限验证结束=========▶▶允许退款" );
        // 20190712验证退款权限------end



        logger.debug("Start cancelOrderReserve -------");
        Long orderId = confirmOrder.getOrderId();
        Order order = orderRepository.getOrder(orderId);
        Store store = storeRepository.getStore(order.getStoreId());

        if (order == null) {
            WxPayRefundResult resp = null;
            resp = new WxPayRefundResult();
            resp.setOut_trade_no(orderId.toString());
            resp.setReturn_code("FAIL");    //"FAIL"
            resp.setReturn_msg("환불이 가능한 주문이 아닙니다.");        //"payment is fail"
            return resp;
        }

        // 验证退款操作者所属门店ID和订单的门店ID是否一致（安全防范）
        Long sessionStoreId = userSession.getStoreId();
        Long orderStoreId = order.getStoreId();
        logger.info("=========▶ userSessionStoreId : " + sessionStoreId);
        logger.info("=========▶ orderStoreId : " + orderStoreId);
        if(sessionStoreId != null && orderStoreId != null){
            if(!(sessionStoreId.longValue() == orderStoreId.longValue())){
                // 抛出业务异常信息
                throw new BizException(ErrorCode.BIZ_EXCEPTION);
            }
        } else {
            // 抛出业务异常信息
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }

        // 存储退款原因到order_info表的cancel_reason字段
        if (store.storeType() == 2) {        		//店铺类型(1:餐厅 2:医院 3:购物 4:酒店)
            int id_info = orderService.cancelOrderReserve(confirmOrder);
        } else if (store.storeType() == 3) {        //店铺类型(1:餐厅 2:医院 3:购物 4:酒店)
            int id_info = orderService.cancelOrderReserve(confirmOrder);
        } else if (store.storeType() == 4) {        //店铺类型(1:餐厅 2:医院 3:购物 4:酒店)
            int id_info = orderService.cancelOrderReserve(confirmOrder);
        }

        //TODO 这是要干啥呢？！
        if (donotCanceOrderlStatus(order, store)) {
            WxPayRefundResult resp = null;
            resp = new WxPayRefundResult();
            resp.setOut_trade_no(orderId.toString());
            resp.setReturn_code("FAIL");    //"FAIL"
            resp.setReturn_msg("환불이 가능한 주문이 아닙니다.");        //"payment is fail"
            return resp;
        }

        // WxSession session = (WxSession) context.props().get(AppConfigure.BASOFT_WX_SESSION_PROP);
        String ip = Objects.toString(context.props().get("realIp"), null);
        logger.info("=========▶ Cancel Order orderId : " + orderId);

        order.setStatus(Order.Status.CANCEL_REQ);
        // Cancel order status update (Cancel Request)
        orderRepository.updateOrder(order);

        // 发起退款
        WxPayRefundResult respRst = orderService.sendWechatCancelOrder(store, order, ip, getCertStream());

        if ("SUCCESS".equals(respRst.getReturn_code())) {
            if ("SUCCESS".equals(respRst.getResult_code())) {
                // Tempagte msg
                sendTemplateMsg(order, store, confirmOrder, true);

                // 20190905 酒店库存恢复-start
                if (store.storeType() == 4 && order.getOrderType() == CommonConstants.ORDER_TYPE_NORMAL) {
                    logger.info("【酒店退款库存恢复】库存回复线程启动Start..........................................");
                    // 唉单独查一次吧::::查询酒店订单的内容信息（sku）- 酒店订单sku只有一个！！！
                    Long prodSkuId = orderRepository.queryHotelOrderItem(orderId);

                    Map<String, Object> orderInfo = Maps.newHashMap();
                    orderInfo.put("storeId", store.id());
                    orderInfo.put("skuId", prodSkuId);
                    orderInfo.put("reseveDtFrom", order.getReseveDtfrom());
                    orderInfo.put("reseveDtTo", order.getReseveDtto());

//                    Thread thread = new Thread(new HotelOrderRecoverThread(inventoryHotelRepository, orderInfo, CommonConstants.HOTEL_INVENTORY_RECOVER_REFUND, null));
//                    thread.start();
                    logger.info("【酒店退款库存恢复】库存回复线程启动Over..........................................");
                }
                // 20190905 酒店库存恢复-end

                // 20200422 零售商户产品库存恢复-start
                else if(store.storeType() == 3 && order.getOrderType() == CommonConstants.ORDER_TYPE_NORMAL){
                    // 从临时订单表里查询的订购内容，嘻嘻。。。
                    List<RetailToDoRecoverTempOrder> tempOrderList = orderQuery.queryRetailTempOrderByIdToRecover(orderId.toString(), "refund");
                    if (tempOrderList == null || tempOrderList.isEmpty()) {
                        // nothing to do
                    } else {
                        logger.info("【零售商户产品库存恢复】库存回复线程启动Start..........................................");
//                        Thread thread = new Thread(new RetailOrderRecoverThread(inventoryRetailRepository, tempOrderList, CommonConstants.RETAIL_INVENTORY_RECOVER_REFUND));
//                        thread.start();
                        logger.info("【零售商户产品库存恢复】库存回复线程启动Over..........................................");
                    }
                }
                // 20200422 零售商户产品库存恢复-start
            }
        }

        return respRst;
    }

    /**
     * //TODO What is this for?
     *
     * @param order
     * @param store
     * @return
     */
    private boolean donotCanceOrderlStatus(Order order, Store store) {
        boolean flag = false;
        if (store.storeType() == 1) {	//1:餐厅 电子菜单

        }
        else if (store.storeType() == 2) {	//2:医院

        }
        else if (store.storeType() == 3) {	//3:购物

        }
        else if (store.storeType() == 4) {	//4:酒店

        }
        return flag;
    }

    /**
     * 客户申请退款
     *
     * @param confirmOrder
     * @return
     */
    @RequestMapping(value = "/cancelOrderRequest", method = RequestMethod.POST)
    @ResponseBody
    public WxPayRefundResult cancelOrderRequest(@RequestBody ConfirmOrder confirmOrder){

        logger.debug("Start cancelOrderRequest -------");
        Long orderId = confirmOrder.getOrderId();
        Order order = orderRepository.getOrder(orderId);
        Store store = storeRepository.getStore(order.getStoreId());

        if (order == null) {
            WxPayRefundResult resp = null;
            resp = new WxPayRefundResult();
            resp.setOut_trade_no(orderId.toString());
            resp.setReturn_code("FAIL");    //"FAIL"
            resp.setReturn_msg("환불이 가능한 주문이 아닙니다.");        //"payment is fail"
            return resp;
        }

        if ((!Order.Status.get(order.getStatus()).equals(Order.Status.PAYMENY)) &&            //오더 지불 상태
            (!Order.Status.get(order.getStatus()).equals(Order.Status.ACEEPT)) &&            //오더 접수 상태
            (!Order.Status.get(order.getStatus()).equals(Order.Status.CANCEL_FAIL))) {        //Fail 상태
            WxPayRefundResult resp = null;
            resp = new WxPayRefundResult();
            resp.setOut_trade_no(orderId.toString());
            resp.setReturn_code("FAIL");    //"FAIL"
            resp.setReturn_msg("환불이 가능한 주문이 아닙니다.");        //"payment is fail"
            return resp;
        }

        // cancelReason 으로 고객이 환불 신청했는지를 판단한다.
        // by dikim on 20190625 需要业务确定以后再调整
        int id_info = orderService.cancelOrderReserve(confirmOrder);

        WxPayRefundResult resp = null;
        resp = new WxPayRefundResult();
        resp.setOut_trade_no(orderId.toString());
        resp.setReturn_code("SUCCESS");    //"FAIL"
        resp.setReturn_msg("退款申请成功了.");        //"payment is fail"

        return resp;

    }


    /**
     * 发送订单确认的模板消息
     *
     * @param order
     * @param store
     * @param confirmOrder
     */
    private void sendTemplateMsg(Order order, Store store, ConfirmOrder confirmOrder, Boolean cancel) {
        String orderId = order.getId()+"";
        String payAmtCny = order.getPayAmtCny()+"";
        String payAmtKrw = order.getAmount() + "";
        String templateId = "";

        try {
            //get TemplateMsg Config
            Map<String,Object> templateMsgConfig = (Map<String, Object>) appConfigure.getObject(AppConfigure.BASOFT_WX_TEMPLATE_CONFIG).get();

            //get token
            String token = Objects.toString(redisUtil.get("wx_token"), null);
            logger.info("Wechat Access Token::::::" + token);

            //get token
            String DEFAUT_COLOR = "#173177";
            Map<String, DataItem> data = new HashMap<String, DataItem>();

            if (cancel) {
                templateId = (String)templateMsgConfig.get("order_cancel"); // OPENTM200833809

                data.put("first", new DataItem("您的订单已取消。", DEFAUT_COLOR));
                data.put("keyword1", new DataItem(orderId, DEFAUT_COLOR));
                data.put("keyword2", new DataItem(store.name(), DEFAUT_COLOR));
                data.put("keyword3", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
                if (confirmOrder.getCancelReason() == 1) {
                    data.put("keyword4", new DataItem("因客户要求取消订单。", DEFAUT_COLOR));
                }
                else {

                    if (store.storeType() == 1) {	//店铺类型(1:餐厅 2:医院 3:购物 4:酒店)
                        data.put("keyword4", new DataItem("非常抱歉，您的订单由于商家原因（食材不足等）已被取消。", DEFAUT_COLOR));
                    }
                    else if (store.storeType() == 2) {	//店铺类型(1:餐厅 2:医院 3:购物 4:酒店)
                        data.put("keyword4", new DataItem("您所选择日期无法预约。", DEFAUT_COLOR));
                    }
                    else if (store.storeType() == 3) {
                        data.put("keyword4", new DataItem("您所订购的商品暂无库存。", DEFAUT_COLOR));
                    }
                    else if (store.storeType() == 4) {
                        data.put("keyword4", new DataItem("您所预定的房间已被订出.", DEFAUT_COLOR));
                    }

                }

                if (store.storeType() == 1) {	//店铺类型(1:餐厅 2:医院 3:购物 4:酒店)
                    String remark = "退款将在48小时内退回到您的账户，如48小时后仍未收到退款，请联系客服。\n" +
                        "美食相关问题，请在聊天窗口中输入“1”. \n" +
                        "如客服不在线，您也可到”个人中心“->”投诉，建议“中留言，我们会尽快给您答复。\n" +
                        "如需电话沟通，请拨打热线：+82 64-748-0689.";
                    data.put("remark", new DataItem(remark, DEFAUT_COLOR));
                }
                else if (store.storeType() == 2) {	//店铺类型(1:餐厅 2:医院 3:购物 4:酒店)
                    String remark = "退款将在48小时内退回到您的账户，如48小时后仍未收到退款，请联系客服。\n" +
                        "医院相关问题，请在聊天窗口中输入“3”. \n" +
                        "如客服不在线，您也可到”个人中心“->”投诉，建议“中留言，我们会尽快给您答复。\n" +
                        "如需电话沟通，请拨打热线：+82 64-748-0689.";
                    data.put("remark", new DataItem(remark, DEFAUT_COLOR));
                }
                else if (store.storeType() == 4) {	//店铺类型(1:餐厅 2:医院 3:购物 4:酒店)
                    String remark = "退款将在48小时内退回到您的账户，如48小时后仍未收到退款，请联系客服。\n" +
                        "如客服不在线，您也可到”个人中心“->”投诉，建议“中留言，我们会尽快给您答复。\n" +
                        "如需电话沟通，请拨打热线：+82 64-748-0689.";
                    data.put("remark", new DataItem(remark, DEFAUT_COLOR));
                }
                else {
                    data.put("remark", new DataItem("退款将在48小时内退回到您的账户，如48小时后仍未收到退款，请联系客服。\n" +
                        "如果变更内容有误，请联系客服中心。", DEFAUT_COLOR));
                }
            }
            else {

                if (store.storeType() == 2) {	//店铺类型(1:餐厅 2:医院 3:购物 4:酒店)
                    templateId = (String)templateMsgConfig.get("clinic_booking_confirm");

                    String remark = "医院相关问题，请在聊天窗口中输入“3”. \n" +
                        "如客服不在线，您也可到”个人中心“->”投诉，建议“中留言，我们会尽快给您答复。\n" +
                        "如需电话沟通，请拨打热线：+82 64-748-0689.";

                    data.put("first", new DataItem("亲爱的"+order.getCustNm()+" 您好，您的预约已确认。请确认预约时间后到店访问。", DEFAUT_COLOR));
                    data.put("storeName", new DataItem(store.name(), DEFAUT_COLOR));
                    data.put("bookTime", new DataItem(confirmOrder.getReseveDtfrom() + " " + confirmOrder.getReseveConfirmtime(), DEFAUT_COLOR));
                    data.put("orderId", new DataItem(orderId, DEFAUT_COLOR));
                    data.put("orderType", new DataItem(order.getProductNm(), DEFAUT_COLOR));
                    data.put("remark", new DataItem(remark, DEFAUT_COLOR));
                }

                if (store.storeType() == 3) {	//店铺类型(1:餐厅 2:医院 3:购物 4:酒店)		//OPENTM414077052
                    if (order.getShippingType() == 1) {	//自提 - 现场实时自提
                        if (confirmOrder.getStatus() == 5) {
                            templateId = (String)templateMsgConfig.get("shopping_order_confirm");
                            data.put("first", new DataItem("尊敬的 " + order.getCustNm() + " 顾客，感谢您的购买。", DEFAUT_COLOR));
                            data.put("keyword1", new DataItem(orderId, DEFAUT_COLOR));
                            data.put("keyword2", new DataItem(order.getProductNm(), DEFAUT_COLOR));
                            data.put("keyword3", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
                            data.put("keyword4", new DataItem(store.name(), DEFAUT_COLOR));
                            data.put("remark", new DataItem("您的订单已完成.\n客户服务中心 : 064-748-0689", DEFAUT_COLOR));
                        }
                    }
                    else if (order.getShippingType() == 2) {	//自提 -现场预约时间自提
                        if (confirmOrder.getStatus() == 5) {
                            templateId = (String)templateMsgConfig.get("shopping_order_confirm");
                            data.put("first", new DataItem("尊敬的 " + order.getCustNm() + " 顾客，感谢您的购买。", DEFAUT_COLOR));
                            data.put("keyword1", new DataItem(orderId, DEFAUT_COLOR));
                            data.put("keyword2", new DataItem(order.getProductNm(), DEFAUT_COLOR));
                            data.put("keyword3", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
                            data.put("keyword4", new DataItem(store.name(), DEFAUT_COLOR));
                            data.put("remark", new DataItem("您的订单正在处理，我们会尽快通知处理结果.谢谢支持！", DEFAUT_COLOR));
                        }
                        else {
                            templateId = (String)templateMsgConfig.get("shopping_order_confirm");
                            data.put("first", new DataItem("尊敬的 " + order.getCustNm() + " 顾客，感谢您的购买。", DEFAUT_COLOR));
                            data.put("keyword1", new DataItem(orderId, DEFAUT_COLOR));
                            data.put("keyword2", new DataItem(order.getProductNm(), DEFAUT_COLOR));
                            data.put("keyword3", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
                            data.put("keyword4", new DataItem(store.name(), DEFAUT_COLOR));
                            String remark = "您所订购的商品已完成备货. \n提货日:" + order.getShippingDt() + "\n请于提货日到店提取商品. \n门店详细地址可在我的订单明细中查询.\n客户服务中心 : 064-748-0689";
                            data.put("remark", new DataItem(remark, DEFAUT_COLOR));
                        }
                    }
                    else if (order.getShippingType() == 3) {	//配送区域收货
                        if (confirmOrder.getStatus() == 5) {
                            templateId = (String)templateMsgConfig.get("shopping_order_confirm");
                            data.put("first", new DataItem("尊敬的 " + order.getCustNm() + " 顾客，感谢您的购买。", DEFAUT_COLOR));
                            data.put("keyword1", new DataItem(orderId, DEFAUT_COLOR));
                            data.put("keyword2", new DataItem(order.getProductNm(), DEFAUT_COLOR));
                            data.put("keyword3", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
                            data.put("keyword4", new DataItem(store.name(), DEFAUT_COLOR));
                            data.put("remark", new DataItem("您的订单正在处理，我们会尽快通知处理结果.谢谢支持！", DEFAUT_COLOR));
                        }
                        else {
                            templateId = (String)templateMsgConfig.get("shopping_order_confirm");
                            data.put("first", new DataItem("尊敬的 " + order.getCustNm() + " 顾客，您所订购的商品已完成备货。", DEFAUT_COLOR));
                            data.put("keyword1", new DataItem(orderId, DEFAUT_COLOR));
                            data.put("keyword2", new DataItem(order.getProductNm(), DEFAUT_COLOR));
                            data.put("keyword3", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
                            data.put("keyword4", new DataItem(order.getShippingAddrNm(), DEFAUT_COLOR));
                            String remark = "您所订购的商品已发货.\n配送时间大概需要1~2日. 配送信息请咨询客服中心.\n客户服务中心 : 064-748-0689";
                            data.put("remark", new DataItem(remark, DEFAUT_COLOR));
                        }
                    }
                    else {
                        templateId = (String)templateMsgConfig.get("shopping_order_confirm");
                        data.put("first", new DataItem("尊敬的 " + order.getCustNm() + " 顾客，感谢您的购买。", DEFAUT_COLOR));
                        data.put("keyword1", new DataItem(orderId, DEFAUT_COLOR));
                        data.put("keyword2", new DataItem(order.getProductNm(), DEFAUT_COLOR));
                        data.put("keyword3", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
                        data.put("keyword4", new DataItem(store.name(), DEFAUT_COLOR));
                        data.put("remark", new DataItem("您的订单正在处理，我们会尽快通知处理结果.谢谢支持！\n客户服务中心 : 064-748-0689", DEFAUT_COLOR));
                    }
                }

                if (store.storeType() == 4) {	//店铺类型(1:餐厅 2:医院 3:购物 4:酒店)

                    if (order.getOrderType() == 2) {
                        return;
                    }

                    templateId = (String)templateMsgConfig.get("hotel_booking_confirm");

                    String remark = "如客服不在线，您也可到”个人中心“->”投诉，建议“中留言，我们会尽快给您答复。\n" +
                        "如需电话沟通，请拨打热线：+82 64-748-0689.";

                    data.put("first", new DataItem("您预订的"+store.name()+"酒店订单已确认。入住时，请务必携带护照到前台办理入住手续。", DEFAUT_COLOR));
                    data.put("order", new DataItem(orderId, DEFAUT_COLOR));
                    data.put("Name", new DataItem(order.getCustNm(), DEFAUT_COLOR));
                    data.put("datein", new DataItem(order.getReseveDtfrom(), DEFAUT_COLOR));
                    data.put("dateout", new DataItem(order.getReseveDtto(), DEFAUT_COLOR));
                    data.put("number", new DataItem("1间", DEFAUT_COLOR));
                    data.put("room type", new DataItem(order.getProductNm(), DEFAUT_COLOR));
                    data.put("pay", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
                    data.put("remark", new DataItem(remark, DEFAUT_COLOR));
                }
            }


            TemplateMessageReturn result = WechatAPI.sendTemplateMessage(token, order.getCustomerId(), templateId, "", DEFAUT_COLOR, data);
            logger.info("TemplateMessageReturn::::::" + result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 医美订单确认
     *
     * @param confirmOrder
     * @return int
     * @author dikim
     * @date 2019/5/22
     */
    @CommandHandler.AutoCommandHandler(ConfirmOrder.NAME_CLINIC)
    @Transactional
    public int confirmOrderClinic(ConfirmOrder confirmOrder) {
        confirmOrder.setStatus(ConfirmOrder.Status.ACEEPT.getStatusCode());
        int status = orderService.confirmOrderClinic(confirmOrder);
        return orderService.acceptOrderClinic(confirmOrder);
    }

    /**
     * 购物订单确认
     *
     * @param acceptOrder
     * @return int
     * @author dikim
     * @date 2019/5/22
     */
    @CommandHandler.AutoCommandHandler(ConfirmOrder.NAME_SHOP)
    @Transactional
    public int confirmOrderShop(AcceptOrder acceptOrder) {
        acceptOrder.setStatus(AcceptOrder.Status.ACEEPT.getStatusCode());
        return orderService.acceptOrder(acceptOrder);
    }



    /***************************************电子点餐下单不支付订单逻辑--START***************************************/
    /**
     * 【Wechat H5】微信下单不支付订单的保存
     *
     * @param saveOrder
     * @param context
     * @return
     */
    @CommandHandler.AutoCommandHandler(SaveOrder.NO_PAY_NAME)
    public WxPayJsResp saveNoPayOrder(SaveOrder saveOrder, CommandHandlerContext context) {
        logger.debug("Start Save No Pay Order -------");
        // 安全性验证：验证IP地址
        String ip = Objects.toString(context.props().get("realIp"), null);
        if (StringUtils.isBlank(ip)) {
            throw new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND IP");
        }

        // 获取会话信息
        WxSession session = (WxSession) context.props().get(AppConfigure.BASOFT_WX_SESSION_PROP);
        logger.info("=========▶ session.getSceneStr() : " + session.getSceneStr());

        // 下单即支付门店不允许调用该接口
        String isPayStore = session.getIsPayStore();
        if ("1".equals(isPayStore)){
            throw new BizException(ErrorCode.PARAM_INVALID, "INVALID INVOKE,NEED PAYMENT!");
        }

        // 获取门店信息
        Store store = storeRepository.getStore(session.getStoreId());
        if (store == null) {
            logger.error("▶▶▶▶▶▶▶▶▶▶ Not Found Store, session.getStoreId() : " + session.getStoreId());
            throw new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND STORE");
        }

        // 检验获取桌子
        StoreTable table = storeRepository.getStoreTableList(store)
            .stream()
            .filter(dd -> StringUtils.isNotBlank(dd.getQrCodeId()))
            .filter(st -> st.getQrCodeId().equals(session.getSceneStr()))
            .findFirst()
            .orElseThrow(() -> new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND TABLE"));

        // 生成订单编号
        Long orderId = uidGenerator.generate(BusinessTypeEnum.ORDER);

        // totalAmount为空则取值Amount
        BigDecimal totalAmount = saveOrder.getTotalAmount();
        BigDecimal x = new BigDecimal(0);
        if (totalAmount == null) {
            totalAmount = saveOrder.getAmount();
        }

        String cmt = saveOrder.getCmt();
        if(StringUtils.isNotBlank(cmt)){
            if (hasEmoji(cmt)) {
                cmt = replaceEmoji(cmt);
            }
        }

        Order order = saveOrder.build(
            orderId,
            session.getStoreId(),
            table.id(),
            session.getOpenId(),
            ip,
            DateUtil.getWxPayNowStr(),
            DateUtil.getFormatStr(DateUtil.plusWxPayDateTime(30, ChronoUnit.MINUTES)),
            new BigDecimal(0),
            new BigDecimal(0),
            totalAmount,
            cmt
        );

        // 不支付下单的订单属性特殊设置
        order.setStatus(Order.Status.PAYMENY);

        // 订单中的订购产品ID列表
        List<Long> skuIdList = order.getItemList().stream().map(OrderItem::getSkuId).collect(Collectors.toList());

        if (skuIdList.size() > 0) {
            // 根据订单中订购产品ID列表查询对应的产品列表
            Map<String, Object> param = Maps.newHashMap();
            param.put("skuIds", skuIdList);
            List<ProductSkuDTO> skuList = productQuery.getProductSkuListByMap(param);

            // 查询到的订购产品SKU详情信息列表不为空
            if (skuList != null && skuList.size() > 0) {

                // 判断订单中的sku_id列表和查询到的产品sku信息列表的数量是否一致。一致则订单中的产品sku全部合法，否则存在不合法的sku_id
                if (skuIdList.size() == skuList.size()) {
                    // 将skuList转换为Map<skuId,ProductSkuDTO>格式，循环设置或检查订单时便于获取ProductSkuDTO对象
                    Map<Long, ProductSkuDTO> maps = skuList.stream().collect(Collectors.toMap(ProductSkuDTO::getId, sku -> sku));

                    // 查询获取当前店铺的折扣产品列表（含折扣价格），用于价格的二次核查
                    Map<String, Object> discountDisplayParam = Maps.newHashMap();
                    discountDisplayParam.put("storeId", String.valueOf(session.getStoreId()));
                    List<DiscountDisplayDTO> discountDisplayList = discountQuery.getDiscountDisplayList(discountDisplayParam);
                    final Map<Long, DiscountDisplayDTO> discountDisplayMap = discountDisplayList.stream()
                        .collect(Collectors.toMap(DiscountDisplayDTO::getProdSkuId, discountDisplayDTO -> discountDisplayDTO));

                    // 订单二次核价和订单信息补充（补充韩文名称和中文名称）
                    order.getItemList().forEach(item -> {
                        Long skuId = item.getSkuId();
                        ProductSkuDTO sku = maps.get(skuId);

                        // 订单提交价格核对，防止价格被黑--start
                        logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-START◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                        // 根据折扣价进行核价
                        if (discountDisplayMap.containsKey(skuId)) {
                            DiscountDisplayDTO discount = discountDisplayMap.get(skuId);
                            // 折扣价
                            BigDecimal discountPrice = new BigDecimal(discount.getDiscPrice());
                            // 订单价
                            BigDecimal orderPrice = item.getPrice();
                            logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-折扣-折扣价格【" + discountPrice.toString()
                                +"】，订单价格【" + orderPrice.toString() +"】◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                            // 价格核查
                            if (discountPrice.compareTo(orderPrice) != 0) {
                                throw new BizException(ErrorCode.PARAM_INVALID,"Order Price Error!");
                            }
                        }
                        // 根据sku product价格核价
                        else {
                            BigDecimal dbPrice = sku.getPriceKor();
                            BigDecimal orderPrice = item.getPrice();
                            logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-SKU价格【" + dbPrice.toString()
                                +"】，订单价格【" + orderPrice.toString() +"】◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                            if (dbPrice.compareTo(orderPrice) != 0) {
                                throw new BizException(ErrorCode.PARAM_INVALID);
                            }
                        }
                        logger.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-END◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                        // 订单提交价格核对，防止价格被黑--end

                        item.setSkuNmKor(sku.getNameKor());
                        item.setSkuNmChn(sku.getNameChn());
                    });

                    // insert Order Temp为了获取正式订单的订单号
                    Long trans_id = 0L;
                    int insertCount = orderRepository.saveNoPayTempOrder(order);
                    if (insertCount > 0) {
                        trans_id = orderRepository.getOrderTransId(orderId);
                        logger.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%下单不支付短订单号为%%%%%%%%%%%%%%%%%%%%%%%%%%%%>>>" + trans_id);
                        order.setTransId(trans_id);
                    } else {
                        order.setTransId(orderId);
                    }

                    // insert Order
                    orderRepository.saveNoPayOrder(order);

                    // 不支付下单极光推送
                    try {
                        logger.info("▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶ 不支付下单极光推送-START ◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                        Event event = new Event("order_pay", null);
                        // this.eventProducer.pushMsgSend(event, order.getStoreId().toString(), order.getTableId().toString());
                        this.eventProducer.pushMsgSend(event, order.getStoreId().toString(), String.valueOf(table.getNumber()), String.valueOf(store.storeType()));
                        logger.info("▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶ 支付下单极光推送-END ◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                    } catch (Throwable e) {
                        logger.error("▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶ 支付下单极光推送-ERROR ◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀" + e.getMessage());
                        e.printStackTrace();
                    }

                    return noPayOrderSuccessResp(order, "SUCCESS", "Save No Pay Order Success!");
                }
                // 订单中的存在不合法的订购内容
                else {
                    // 订单中的产品ID数量和根据订单中的产品ID查询到的产品数量不一致，即订单中存在不合法的产品ID
                    logger.error("=======>>>> order details are not valid data <<<<======");
                }
            }
            // 查询到的订购产品SKU详情信息列表为空，即订单中的SKU_ID全部不合法
            else {
                // 根据订单中的产品ID无法查询到产品信息
                logger.error("=======>>>> order details are not valid data <<<<======");
            }
        } else {
            // 订单中无订购产品(ID)
            logger.error("========>>>> product is empty !!!! <<<<======");
        }
        return noPayOrderFailResp(order, "FAIL", "Order Content Invalid");
    }

    private WxPayJsResp noPayOrderSuccessResp(Order order, String returnCode, String ReturnMsg) {
        WxPayJsResp resp = new WxPayJsResp.Builder()
            .orderId(order.getId() + "")
            .build();
        resp.setReturn_code(returnCode);
        resp.setReturn_msg(ReturnMsg);
        return resp;
    }

    private WxPayJsResp noPayOrderFailResp(Order order, String returnCode, String ReturnMsg) {
        WxPayJsResp resp = new WxPayJsResp.Builder()
            .orderId(order.getId() + "")
            .build();
        resp.setReturn_code(returnCode);
        resp.setReturn_msg(ReturnMsg);
        return resp;
    }
    /***************************************电子点餐下单不支付订单逻辑--END***************************************/
}