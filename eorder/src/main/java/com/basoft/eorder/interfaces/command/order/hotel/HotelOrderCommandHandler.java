package com.basoft.eorder.interfaces.command.order.hotel;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.WxSession;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.application.wx.model.WxPayJsResp;
//import com.basoft.eorder.batch.lock.RedissonUtil;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.OrderRepository;
import com.basoft.eorder.domain.OrderService;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.OrderItem;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.StoreTable;
import com.basoft.eorder.interfaces.command.SaveOrder;
import com.basoft.eorder.interfaces.command.order.common.OrderCommonUtil;
import com.basoft.eorder.interfaces.query.ExchangeRateDTO;
import com.basoft.eorder.interfaces.query.ProductQuery;
import com.basoft.eorder.interfaces.query.ProductSkuDTO;
import com.basoft.eorder.interfaces.query.hotel.HotelProductSkuDatePriceDTO;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelDTO;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelQuery;
import com.basoft.eorder.util.DateUtil;
import com.basoft.eorder.util.RedisUtil;
import com.basoft.eorder.util.StringUtil;
import com.basoft.eorder.util.UidGenerator;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@CommandHandler.AutoCommandHandler("HotelOrderCommandHandler")
public class HotelOrderCommandHandler {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private ProductQuery productQuery;

    @Autowired
    private InventoryHotelQuery inventoryHotelQuery;

//    @Autowired
//    private RedissonUtil redissonUtil;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    private AppConfigure appConfigure;

    @Autowired
    public HotelOrderCommandHandler(AppConfigure appConfigure) {
        this.appConfigure = appConfigure;
    }

    /**
     * 【Wechat H5】微信支付下单
     *
     * 酒店商户下单版本v2
     *
     * @param saveOrder
     * @param context
     * @return
     *
     * @see com.basoft.eorder.interfaces.command.OrderCmmandHandler([saveOrder]) 参考酒店商户下单版本v1
     */
    @CommandHandler.AutoCommandHandler(SaveOrder.HOTEL_ORDER_NAME)
    public WxPayJsResp saveHotelOrder(SaveOrder saveOrder, CommandHandlerContext context) {
        log.info("Start saveHotelOrder ----------------------------");

        // 安全性验证：验证IP地址
        String ip = Objects.toString(context.props().get("realIp"), null);
        if (StringUtils.isBlank(ip)) {
            throw new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND IP");
        }
        log.info("saveHotelOrder From IP IS ---------------------------- {}", ip);

        // 获取用户登录该酒店商户的信息
        WxSession session = (WxSession) context.props().get(AppConfigure.BASOFT_WX_SESSION_PROP);
        // session.getSceneStr()获取的是qrcode_id。对应store_table表中的qrcode_id字段
        log.info("=========▶ session.getSceneStr() : " + session.getSceneStr());

        // 下单不支付门店不允许调用该接口
        String isPayStore = session.getIsPayStore();
        if ("0".equals(isPayStore)){
            throw new BizException(ErrorCode.PARAM_INVALID, "INVALID INVOKE, IS NOT PAY STORE!");
        }

        Store store = storeRepository.getStore(session.getStoreId());
        if (store == null) {
            log.error("▶▶▶▶▶▶▶▶▶▶ not found store, session.getStoreId() : " + session.getStoreId());
            throw new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND STORE");
        }

        //验证店铺支付信息填写是否完整，不完整不允许发起支付
        OrderCommonUtil.checkStorePay(store);

        // 从门店的桌子列表中过滤出QRCode不为空且等于订单中QrCode的桌子。
        StoreTable table = storeRepository.getStoreTableList(store)
                .stream()
                .filter(dd -> StringUtils.isNotBlank(dd.getQrCodeId()))
                .filter((st) -> st.getQrCodeId().equals(session.getSceneStr()))
                .findFirst()
                .orElseThrow(() -> new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND TABLE"));

        // 生成新的订单编号
        Long orderId = uidGenerator.generate(BusinessTypeEnum.ORDER);

        // 查询当前汇率
        ExchangeRateDTO erd = productQuery.getNowExchangeRate();

        // 将韩币订单金额转为美元订单金额
        BigDecimal payAmtUsd = saveOrder.getAmount().multiply(erd.getKrwusdRate()).setScale(2, BigDecimal.ROUND_UP);

        // totalAmount
        BigDecimal totalAmount = saveOrder.getTotalAmount();
        if (totalAmount == null) {
            totalAmount = saveOrder.getAmount();
        }

        // 处理订单备注中的特殊字符
        String cmt = saveOrder.getCmt();
        if (StringUtils.isNotBlank(cmt)) {
            if (StringUtil.hasEmoji(cmt)) {
                cmt = StringUtil.replaceEmoji(cmt);
            }
        }

        // 是否到手价结算酒店-prod price type为2则为到手价结算酒店
        // prod price type 酒店房价格类型。 1-独立平台价格，商户月结算按商户费率  2-价格分离（到手价和平台价分离），商户计算按房间价格
        log.info("▶▶▶▶▶▶▶▶▶▶ store.prodPriceType(): " + store.prodPriceType());
        boolean isDaoshouSettleHotel = ("2").equals(String.valueOf(store.prodPriceType())) ? true : false;

        // 构造订单信息
        Order order = saveOrder.build(
                orderId,//订单编号
                session.getStoreId(),//商户编号
                table.id(),//桌号
                session.getOpenId(),//用户openid
                ip,//用户ip
                DateUtil.getWxPayNowStr(),//time_start 订单开始时间
                DateUtil.getFormatStr(DateUtil.plusWxPayDateTime(30, ChronoUnit.MINUTES)),//time_expire 订单失效时间
                payAmtUsd,//订单支付金额（美元）
                erd.getKrwusdRate(),//当前韩币和美元汇率
                totalAmount,//订单金额
                cmt,//订单备注
                // 20190905-新增押金产品的支持
                store.storeType()//订单业务类型biz_type
        );
        log.info("=========▶ order full info::::" + order);

        // 处理订单中的订单内容列表-start
        // 酒店房间订单业务（订购酒店房间且不是酒店押金产品）
        //List<Long> skuIdList = order.getItemList().stream().map(OrderItem::getSkuId).collect(Collectors.toList());
        List<Long> skuIdList = new ArrayList<>();
        if (CommonConstants.BIZ_HOTEL_INT == store.storeType() && saveOrder.getOrderType() == CommonConstants.ORDER_TYPE_NORMAL) {
            // 酒店房间下单多天会在item里存放每天的价格，导致sku重复，所以先去重。当然该类去重不影响其他业务类型
            List<OrderItem> noDuplicateItemList = order.getItemList().stream().collect(
                    Collectors.collectingAndThen(Collectors.toCollection(() ->
                            new TreeSet<>(Comparator.comparing(OrderItem::getSkuId))), ArrayList::new));
            skuIdList = noDuplicateItemList.stream().map(OrderItem::getSkuId).collect(Collectors.toList());
        }

        // 1-1if.订单中订购产品SKU_ID列表
        if (skuIdList.size() > 0) {
            // 根据订购产品的SKU_ID列表查询详细的产品SKU信息
            Map<String, Object> param = Maps.newHashMap();
            param.put("skuIds", skuIdList);
            List<ProductSkuDTO> skuList = productQuery.getProductSkuListByMap(param);

            // 1-2if.查询到的订购产品SKU详情信息列表不为空
            if (skuList != null && skuList.size() > 0) {

                // 1-3if.判断订单中的sku_id列表和查询到的产品sku信息列表的数量是否一致。一致则订单中的产品sku全部合法，否则存在不合法的sku_id
                if (skuIdList.size() == skuList.size()) {

                    // 2func.订单二次核价-start
                    // 2func-1.酒店房间业务核价
                    if (CommonConstants.BIZ_HOTEL_INT == store.storeType() && saveOrder.getOrderType() == CommonConstants.ORDER_TYPE_NORMAL) {
                        try{
                            // skuIdList中有且仅有一个prod sku id，因为酒店订单只允许订购一个prod sku
                            Long prodSkuId = skuIdList.get(0);
                            log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-prodSkuId>>>>>>" + prodSkuId);

                            // 0、产品价格：：准备sku信息-产品价格（韩元）
                            // 将skuList转换为Map<skuId,ProductSkuDTO>格式，循环设置或检查订单时便于获取ProductSkuDTO对象
                            Map<Long, ProductSkuDTO> maps = skuList.stream().collect(Collectors.toMap(ProductSkuDTO::getId, sku -> sku));
                            ProductSkuDTO productSkuDTO = maps.get(prodSkuId);
                            // 产品价格（韩元）
                            BigDecimal productSkuPrice = productSkuDTO.getPriceKor();
                            // 产品到手价（韩元）
                            BigDecimal productSkuPriceSettle = productSkuDTO.getPriceSettle();
                            log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-productSkuPrice>>>>>>" + productSkuPrice);

                            // 1、库存价格：：数据库中的sku按日期价格信息-库存价格（韩元）
                            // 根据预定日期和预定的prod sku id获取该sku每天的有效价格（韩币单位）
                            List<HotelProductSkuDatePriceDTO> productSkuDatePriceList = inventoryHotelQuery
                                    .getProductSkuDatePriceBySkuIdList(prodSkuId, saveOrder.getReseveDtfrom(), saveOrder.getReseveDtto());
                            Map<String, HotelProductSkuDatePriceDTO> hotelProductSkuDatePriceMap = productSkuDatePriceList
                                    .stream().collect(Collectors.toMap(HotelProductSkuDatePriceDTO::getPriceDate, sku -> sku));
                            log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-库存价格列表>>>>>>" + hotelProductSkuDatePriceMap);


                            // 2、订单价格：：酒店订单列表内容（复用字段skuNmChn存储预订日期）
                            Map<String, OrderItem> dateOrderItemMap = order.getItemList().stream()
                                    .collect(Collectors.toMap(OrderItem::getSkuNmChn, orderItem -> orderItem));
                            log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-订单item列表>>>>>>" + dateOrderItemMap);

                            // 3、订单日期：：预订酒店的日期列表
                            List<String> dateAllList = DateUtil.findDataAll(saveOrder.getReseveDtfrom(),
                                    saveOrder.getReseveDtto(), 1);
                            dateAllList.remove(saveOrder.getReseveDtto());
                            log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查日期列表：" + dateAllList);

                            // 4、循环验证每日价格
                            BigDecimal checkTotalAmount = BigDecimal.ZERO;
                            BigDecimal totalDaoshouAmount = BigDecimal.ZERO;
                            for(String date : dateAllList){
                                log.info("\n\n");
                                log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-日期>>>>>>" + date + "开始");
                                // 按日期取订单信息
                                OrderItem orderItem = dateOrderItemMap.get(date);
                                if(orderItem == null){
                                    log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-orderItem为空，异常抛出！！！");
                                    throw new BizException(ErrorCode.PARAM_INVALID,"Order Item Error(orderItem)!");
                                } else {
                                    // 按日期取库存日期价格
                                    HotelProductSkuDatePriceDTO hotelProductSkuDatePriceDTO = hotelProductSkuDatePriceMap.get(date);
                                    if(hotelProductSkuDatePriceDTO == null){
                                        log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-hotelProductSkuDatePriceDTO为空！！！");
                                        // 不存在库存日期价格，比较产品sku价格
                                        if(orderItem.getPrice().compareTo(productSkuPrice) != 0){
                                            log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-订单价格和产品sku价格不一致，抛出异常！！！");
                                            throw new BizException(ErrorCode.PARAM_INVALID,"Order Price Error(original)!");
                                        } else {
                                            checkTotalAmount = checkTotalAmount.add(productSkuPrice);
                                        }

                                        // 20200215-写入当前日期的到手价-库存信息（库存到手价）为空，则写入产品sku到手价
                                        if(isDaoshouSettleHotel){
                                            orderItem.setPriceSettle(productSkuPriceSettle);
                                            totalDaoshouAmount = totalDaoshouAmount.add(productSkuPriceSettle);
                                        } else {
                                            orderItem.setPriceSettle(new BigDecimal(-1));
                                        }
                                    } else {
                                        log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-hotelProductSkuDatePriceDTO>>>>>>" + hotelProductSkuDatePriceDTO);
                                        // 存在库存日期价格，比较库存价格中的有效价格
                                        BigDecimal inventEffectPrice = new BigDecimal(hotelProductSkuDatePriceDTO.getEffectivePriceKor());
                                        if(orderItem.getPrice().compareTo(inventEffectPrice) != 0){
                                            log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-订单价格和库存有效价格不一致，抛出异常！！！");
                                            throw new BizException(ErrorCode.PARAM_INVALID,"Order Price Error(inventory)!");
                                        } else {
                                            checkTotalAmount = checkTotalAmount.add(inventEffectPrice);
                                        }

                                        // 20200215-写入当前日期的到手价-库存到手价为空，则写入产品sku到手价
                                        if(isDaoshouSettleHotel) {
                                            BigDecimal effectivePriceSettle = new BigDecimal(hotelProductSkuDatePriceDTO.getEffectivePriceSettle());
                                            orderItem.setPriceSettle(effectivePriceSettle);
                                            totalDaoshouAmount = totalDaoshouAmount.add(effectivePriceSettle);
                                        } else {
                                            orderItem.setPriceSettle(new BigDecimal(-1));
                                        }
                                    }
                                }
                                log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-日期>>>>>>" + date + "结束");
                                log.info("\n\n");
                            }

                            // 5、总价
                            if(totalAmount.compareTo(checkTotalAmount) != 0){
                                throw new BizException(ErrorCode.PARAM_INVALID,"Order Amount Error!");
                            }

                            // 6、补充酒店房间订单到手价总额
                            if(isDaoshouSettleHotel) {// 到手价酒店
                                order.setAmountSettle(totalDaoshouAmount);
                                order.setRateSettle(-1);
                            } else {// 非到手价酒店
                                order.setAmountSettle(new BigDecimal(-1));
                                if(store.chargeType() == 1 || store.chargeType() == 2){
                                    order.setRateSettle(store.chargeRate());
                                } else {
                                    order.setRateSettle(-1);
                                }
                            }

                        } catch (Exception e){
                            throw new BizException(ErrorCode.PARAM_INVALID,e.getMessage());
                        }
                    }
                    // 2func.订单二次核价-end


                    // 2func.20190903-酒店下单库存管理-start
                    int storeType = store.storeType();
                    int orderType = saveOrder.getOrderType();
                    log.info("<><><><><><><><><>><><><><>商店类型为{}，订单类型为{}<><><><><><><><><>><><><><>", storeType, orderType);
                    // 2func-1.酒店业务且是正常商品则处理库存
                    if (CommonConstants.BIZ_HOTEL_INT == storeType && orderType == CommonConstants.ORDER_TYPE_NORMAL) {
                        log.info("<><><><><><><><><>><><><><>【酒店下单】支付前库存验证和库存削减<><><><><><><><><>><><><><>");

                        // 具体到酒店商户锁
                        StringBuilder hotelLockKey = new StringBuilder(CommonConstants.HOTEL_INVENTORY_LOCK).append(store.id());
                        // RLock lock = redissonUtil.getRLock(hotelLockKey.toString());
//                        RLock lock = redissonUtil.getFairLock(hotelLockKey.toString());

                        // 获取锁，并且防止死锁。60秒后自动释放。
//                        lock.lock(60, TimeUnit.SECONDS);

                        try {
                            log.info("<><><><><><><><><>><><><><>【酒店下单】获取到酒店下单验证削减锁<><><><><><><><><>><><><><>");
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
                                log.info("【库存验证】库存信息>>>>>>" + inventoryHotelDTO);
                                // 存在房间关闭则直接认为库存不足
                                if("0".equals(inventoryHotelDTO.getIsOpening())){
                                    return true;
                                }
                                int invTotal = Integer.valueOf(inventoryHotelDTO.getInvTotal());
                                int invUsed = Integer.valueOf(inventoryHotelDTO.getInvUsed());
                                int invBalance = invTotal - invUsed;
                                log.info("【库存验证】验证日期，库存余量分别为>>>>>>" + inventoryHotelDTO.getInvDate() + "," + invBalance);
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
//                                log.info("【酒店下单】酒店下单核心逻辑已处理，库存锁释放，进入支付......");
//                            }
                        }
                    }
                    // 2func-2.非酒店业务和酒店业务押金产品
                    else {
                        // 20200217-走老的下单逻辑saveOrder
                    }
                    // 2func.20190903-酒店下单库存管理-start



                    Long transId = orderRepository.getOrderTransId(order.getId());
                    log.info("=========▶ transId::::" + transId);
                    // http://domain/eorder/wechat/api/v1/order_pay
                    String notifyUrl = appConfigure.get(AppConfigure.BASOFT_WECHAT_NOTIFY_URL);

                    //to Wechat
                    WxPayJsResp resp = orderService.sendWechatOrder(store, order, ip, notifyUrl, getCertStream(), transId);
                    log.info("=========▶ orderService sendWechatOrder result::::" + resp);
                    return resp;
                }
                // 订单中的存在不合法的订购内容
                else {
                    log.error("=======>>>> order details are not valid data <<<<======");
                }
            }
            // 查询到的订购产品SKU详情信息列表为空，即订单中的SKU_ID全部不合法
            else {
                log.error("=======>>>> order details are not valid data <<<<======");
            }
        }
        // 订单中无订购内容
        else {
            log.error("========>>>> product is empty !!!! <<<<======");
        }
        // 处理订单中的订单内容列表-end

        return failResp(order, "FAIL", "payment is fail");
    }

    private String getCertStream() {
        String certPath = appConfigure.get(AppConfigure.BASOFT_WECHAT_CERT_PATH);
        return certPath;
    }

    private WxPayJsResp failResp(Order order, String returnCode, String ReturnMsg) {
        WxPayJsResp resp = new WxPayJsResp.Builder()
                .orderId(order.getId() + "")
                .build();
        resp.setReturn_code(returnCode);    //"FAIL"
        resp.setReturn_msg(ReturnMsg);        //"payment is fail"
        return resp;
    }
}
