package com.basoft.eorder.interfaces.command.order.retail;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.WxSession;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.application.wx.model.WxPayJsResp;
import com.basoft.eorder.application.wx.model.WxPayRefundResult;
import com.basoft.eorder.batch.job.model.retail.RetailToDoRecoverTempOrder;
import com.basoft.eorder.batch.job.threads.retail.RetailOrderRecoverThread;
//import com.basoft.eorder.batch.lock.RedissonUtil;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.OrderRepository;
import com.basoft.eorder.domain.OrderService;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.OrderItem;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.StoreTable;
import com.basoft.eorder.domain.model.activity.discount.DiscountDetail;
import com.basoft.eorder.domain.order.retail.RetailOrderEventRepository;
import com.basoft.eorder.domain.retail.InventoryRetailRepository;
import com.basoft.eorder.domain.retail.RetailOrderRepository;
import com.basoft.eorder.interfaces.command.order.common.OrderCommonUtil;
import com.basoft.eorder.interfaces.command.order.common.WechatTemplateMessageUtil;
import com.basoft.eorder.interfaces.command.retail.SaveRetailOrderEvent;
import com.basoft.eorder.interfaces.query.OrderQuery;
import com.basoft.eorder.interfaces.query.ProductQuery;
import com.basoft.eorder.interfaces.query.ProductSkuDTO;
import com.basoft.eorder.interfaces.query.activity.discount.DiscountQuery;
import com.basoft.eorder.interfaces.query.retail.cms.InventoryRetailDTO;
import com.basoft.eorder.interfaces.query.retail.cms.InventoryRetailQuery;
import com.basoft.eorder.util.DateUtil;
import com.basoft.eorder.util.ExchangeRateUtil;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@CommandHandler.AutoCommandHandler("RetailOrderCommandHandler")
public class RetailOrderCommandHandler {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UidGenerator idGenerator;

    @Autowired
    private DiscountQuery discountQuery;

    @Autowired
    private InventoryRetailQuery inventoryRetailQuery;

    @Autowired
    private ProductQuery productQuery;

//    @Autowired
//    private RedissonUtil redissonUtil;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RetailOrderRepository retailOrderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderQuery orderQuery;

    @Autowired
    private ExchangeRateUtil exchangeRateUtil;

    @Autowired
    private RetailOrderEventRepository retailOrderEventRepository;

    @Autowired
    private InventoryRetailRepository inventoryRetailRepository;

    @Autowired
    private WechatTemplateMessageUtil wechatTemplateMessageUtil;

    private AppConfigure appConfigure;

    @Autowired
    public RetailOrderCommandHandler(AppConfigure appConfigure) {
        this.appConfigure = appConfigure;
    }

    /**
     * 【Wechat H5】微信支付下单
     * <p>
     * 零售商户下单版本v2
     *
     * @param saveRetailOrder
     * @param context
     * @return
     * @see com.basoft.eorder.interfaces.command.OrderCmmandHandler([saveOrder]) 参考零售商户下单版本v1
     */
    @CommandHandler.AutoCommandHandler(SaveRetailOrder.NAME)
    public WxPayJsResp saveRetailOrder(SaveRetailOrder saveRetailOrder, CommandHandlerContext context) {
        log.info("Start saveRetailOrder >>>>>>----------------------------");

        // 1、安全性验证：验证IP地址
        String ip = Objects.toString(context.props().get("realIp"), null);
        if (StringUtils.isBlank(ip)) {
            throw new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND IP");
        }
        log.info("saveRetailOrder From IP IS ---------------------------- {}", ip);

        // 2、获取用户登录该零售商户的信息
        WxSession wxSession = (WxSession) context.props().get(AppConfigure.BASOFT_WX_SESSION_PROP);
        // wxSession.getSceneStr()获取的是qrcode_id。对应store_table表中的qrcode_id字段
        log.info("=========▶ session.getSceneStr() : " + wxSession.getSceneStr());

        // 2-1 下单不支付商户不允许调用该接口
        String isPayStore = wxSession.getIsPayStore();
        if ("0".equals(isPayStore)) {
            log.error("▶▶▶▶▶▶▶▶▶▶ INVALID INVOKE, IS NOT PAY STORE! ▶▶▶▶▶▶▶▶▶▶");
            throw new BizException(ErrorCode.PARAM_INVALID, "INVALID INVOKE, IS NOT PAY STORE!");
        }

        // 2-2 验证商户信息，商户必须是正常营业状态。
        // -------------@@@@@@DB@@@@@@-------------
        Store store = storeRepository.getStore(wxSession.getStoreId());
        if (store == null) {
            log.warn("▶▶▶▶▶▶▶▶▶▶ not found store, session.getStoreId() : " + wxSession.getStoreId());
            throw new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND STORE");
        }

        // 2-3 验证商户支付信息填写是否完整，不完整不允许发起支付
        OrderCommonUtil.checkStorePay(store);

        // 3、从门店的桌子列表中过滤出QRCode不为空且等于订单中QrCode的桌子。
        // -------------@@@@@@DB@@@@@@-------------
        StoreTable table = storeRepository.getStoreTableList(store)
                .stream()
                .filter(storeTable -> StringUtils.isNotBlank(storeTable.getQrCodeId()))
                .filter((stoTable) -> stoTable.getQrCodeId().equals(wxSession.getSceneStr()))
                .findFirst()
                .orElseThrow(() -> new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND ORDER TABLE"));

        // 4、生成新的订单编号
        Long orderId = idGenerator.generate(BusinessTypeEnum.ORDER);

        // 5、查询当前汇率，将韩币订单金额转为美元订单金额
        //ExchangeRateDTO erd = productQuery.getNowExchangeRate();
        // 优化汇率获取,直接写入到下面的运算中。erd.getKrwusdRate()变为exchangeRateUtil.getNowKrwUsdRate()

        // BigDecimal payAmtUsd = saveRetailOrder.getAmount().multiply(erd.getKrwusdRate()).setScale(2, BigDecimal.ROUND_UP);
        // 支付金额变为取PaymentAmount
        //BigDecimal payAmtUsd = saveRetailOrder.getPaymentAmount().multiply(erd.getKrwusdRate()).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal payAmtUsd = saveRetailOrder.getPaymentAmount().multiply(exchangeRateUtil.getNowKrwUsdRate()).setScale(2, BigDecimal.ROUND_UP);

        // 6、订单总金额：totalAmount
        BigDecimal totalAmount = saveRetailOrder.getTotalAmount();
        if (totalAmount == null) {
            totalAmount = saveRetailOrder.getAmount();
        }

        // 7、处理订单备注中的特殊字符
        String cmt = saveRetailOrder.getCmt();
        if (StringUtils.isNotBlank(cmt)) {
            if (StringUtil.hasEmoji(cmt)) {
                cmt = StringUtil.replaceEmoji(cmt);
            }
        }

        // 8、构造订单信息
        Order order = saveRetailOrder.build(
                orderId,//订单编号
                wxSession.getStoreId(),//商户编号
                table.id(),//桌号
                wxSession.getOpenId(),//用户openid
                ip,//用户ip
                DateUtil.getWxPayNowStr(),//time_start 订单开始时间
                DateUtil.getFormatStr(DateUtil.plusWxPayDateTime(30, ChronoUnit.MINUTES)),//time_expire 订单失效时间
                payAmtUsd,//订单支付金额（美元）
                //erd.getKrwusdRate(),//当前韩币和美元汇率
                exchangeRateUtil.getNowKrwUsdRate(),//当前韩币和美元汇率
                totalAmount,//订单金额
                cmt,//订单备注
                store.storeType()//订单业务类型biz_type
        );

        // 9、设置订单的即时结算费率（平台和商户之间服务费结算费率）
        if (store.chargeType() == 1 || store.chargeType() == 2) {
            order.setRateSettle(store.chargeRate());
        } else {
            order.setRateSettle(-1);
        }
        // 10、设置零售业务商户订单结算金额为-1
        order.setAmountSettle(new BigDecimal(-1));
        log.info("=========▶ order full info::::" + order);


        // 11、处理订单中的订单内容列表-start
        List<Long> skuIdList = order.getItemList().stream().map(OrderItem::getSkuId).collect(Collectors.toList());

        // 下面注释说明：1-if 2-func

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
                    // 将skuList转换为Map<skuId,ProductSkuDTO>格式，循环设置或检查订单时便于获取ProductSkuDTO对象
                    Map<Long, ProductSkuDTO> productSkuMaps = skuList.stream().collect(Collectors.toMap(ProductSkuDTO::getId, sku -> sku));


                    // 2func.零售业务订单二次核价-start

                    // 2func-1.获取下单sku对应的活动ID列表
                    List<Long> discIdList = new ArrayList<Long>();
                    List<Long> prodSkuIdList = new ArrayList<Long>();
                    List<OrderItem> itemList = order.getItemList();
                    for (OrderItem orderItem : itemList) {
                        Long discId = orderItem.getDiscId();
                        if (discId != null && discId > 0) {
                            discIdList.add(discId);
                            prodSkuIdList.add(orderItem.getSkuId());
                        }
                    }

                    // 2func-2.联合核价：：：：订单项中的sku价格存在活动价格，所以活动列表不为空，此时核价需活动sku活动价格和sku价格联合核价
                    if (discIdList.size() > 0) {
                        // 根据活动ID查询活动详情,并根据prodSkuId进行过滤
                        List<DiscountDetail> discountDetailList = discountQuery.getDiscountDetailList(discIdList, prodSkuIdList);
                        log.info("零售业务：据活动ID和产品sku查询sku的活动详情::discountDetailList::>>>" + discountDetailList);

                        //活动全部结束
                        if (discountDetailList == null || discountDetailList.size() == 0) {
                            throw new BizException(ErrorCode.RETAIL_DISCOUNT_END);
                        }

                        // 汇总查询到的活动ID
                        List<Long> queryDiscIdList = discountDetailList.stream().map(DiscountDetail::getDiscId).collect(Collectors.toList());
                        //活动部分结束
                        if (discIdList.size() != queryDiscIdList.size()) {
                            throw new BizException(ErrorCode.RETAIL_DISCOUNT_END);
                        }

                        // prodSku的活动价格Map:key-discId,value-List<DiscountDetail>
                        final Map<Long, List<DiscountDetail>> discountMap = discountDetailList.stream()
                                .collect(Collectors.groupingBy(DiscountDetail::getDiscId));

                        // 订单二次核价
                        order.getItemList().forEach(item -> {
                            Long discId = item.getDiscId();
                            Long skuId = item.getSkuId();

                            // 订单提交价格核对，防止价格被黑--start
                            log.info("▶▶▶▶▶▶▶▶▶▶订单中产品SKU-{}价格核查-START◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀", skuId);
                            // 订购项存在活动：根据活动折扣价进行核价
                            if (discountMap.containsKey(discId)) {
                                // 从discountMap获取skuId产品的discId活动折扣价
                                BigDecimal discountPrice = null;
                                for (DiscountDetail detail : discountMap.get(item.getDiscId())) {
                                    if (detail.getProdSkuId().longValue() == skuId.longValue()) {
                                        discountPrice = detail.getDiscPrice();
                                        break;
                                    }
                                }
                                if (discountPrice == null) {
                                    throw new BizException(ErrorCode.PARAM_INVALID, "Order Discount Price Error!");
                                }

                                // 订单价
                                BigDecimal orderPrice = item.getPrice();
                                log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-折扣-折扣价格【" + discountPrice.toString()
                                        + "】，订单价格【" + orderPrice.toString() + "】◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");

                                // 价格核查
                                if (discountPrice.compareTo(orderPrice) != 0) {
                                    throw new BizException(ErrorCode.PARAM_INVALID, "Order Price Error!");
                                }
                            }
                            // 订购项不存在活动：根据sku product价格核价
                            else {
                                ProductSkuDTO sku = productSkuMaps.get(skuId);
                                BigDecimal dbPrice = sku.getPriceKor();
                                BigDecimal orderPrice = item.getPrice();
                                log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-SKU价格【" + dbPrice.toString()
                                        + "】，订单价格【" + orderPrice.toString() + "】◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                                if (dbPrice.compareTo(orderPrice) != 0) {
                                    throw new BizException(ErrorCode.PARAM_INVALID);
                                }
                            }
                            log.info("▶▶▶▶▶▶▶▶▶▶订单中产品SKU-{}价格核查-END◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀", skuId);
                            // 订单提交价格核对，防止价格被黑--end
                        });
                    }

                    // 2func-3.独立核价：：：：订单项中的sku价格不存在活动价格，根据sku价格进行独立核价
                    else {
                        order.getItemList().forEach(item -> {
                            ProductSkuDTO sku = productSkuMaps.get(item.getSkuId());

                            // 订单提交价格核对，防止价格被黑--start
                            log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-START◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                            // 根据sku product价格核价
                            BigDecimal dbPrice = sku.getPriceKor();
                            BigDecimal orderPrice = item.getPrice();
                            log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-SKU价格【" + dbPrice.toString()
                                    + "】，订单价格【" + orderPrice.toString() + "】◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                            if (dbPrice.compareTo(orderPrice) != 0) {
                                throw new BizException(ErrorCode.PARAM_INVALID);
                            }
                            log.info("▶▶▶▶▶▶▶▶▶▶订单价格核查-END◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                            // 订单提交价格核对，防止价格被黑--end
                        });
                    }
                    // 2func.订单二次核价-end


                    // 3func.单库存管理-start
                    // 3func-1.过滤出需要进行库存检查的skuId
                    List<Long> toCheckInvProdSkuIdList = skuList
                            .stream()
                            .filter(productSkuDTO -> productSkuDTO.getIsInventory() == 1)
                            .map(productSkuDTO -> productSkuDTO.getId()).collect(Collectors.toList());

                    // 3func-2.无需库存检查
                    if (toCheckInvProdSkuIdList == null || toCheckInvProdSkuIdList.size() == 0) {
                        // insert Order to Temp
                        // orderRepository.saveOrderTemp(order);
                        retailOrderRepository.saveRetailOrderTemp(order);
                    }
                    // 3func-3.需库存检查
                    else {
                        log.info("<><><><><><><><><>><><><><>【零售业务下单】【商户ID-{}】支付前库存验证和库存削减<><><><><><><><><>><><><><>", store.id());
                        // 3func-2.获取具体到零售商户的锁
                        StringBuilder retailLockKey = new StringBuilder(CommonConstants.RETAIL_INVENTORY_LOCK).append(store.id());
                        // RLock lock = redissonUtil.getRLock(hotelLockKey.toString());
//                        RLock lock = redissonUtil.getFairLock(retailLockKey.toString());
                        // 获取锁，并且防止死锁。60秒后自动释放。
//                        lock.lock(60, TimeUnit.SECONDS);

                        try {
                            log.info("<><><><><><><><><>><><><><>【零售业务下单】【商户ID-{}】获取到零售商户下单验证削减锁<><><><><><><><><>><><><><>", store.id());
                            // 3func-3.查询prodSku库存信息
                            List<InventoryRetailDTO> inventoryRetailList = inventoryRetailQuery.getRetailInventoryListByConditions(store.id(), toCheckInvProdSkuIdList);
                            final Map<Long, InventoryRetailDTO> inventoryMap = inventoryRetailList.stream()
                                    .collect(Collectors.toMap(InventoryRetailDTO::getProdSkuId, inventoryRetailDTO -> inventoryRetailDTO));

                            // 库存待更新数据
                            Map<Long, Long> toUpdateInvMap = Maps.newHashMap();

                            // 3func-4.比较订购数量和库存数量
                            order.getItemList().forEach(item -> {
                                Long skuId = item.getSkuId();
                                if (inventoryMap.containsKey(skuId)) {
                                    // 库存数量 < 订购数量
                                    if (inventoryMap.get(skuId).getInvBalance().longValue() < item.getQty().longValue()) {
                                        // 库存不足，无法继续预订。释放酒店下单验证削减锁，并抛出提示异常
                                        throw new BizException(ErrorCode.RETAIL_INVENT_INSUFFICIENT);
                                    }
                                    toUpdateInvMap.put(skuId, item.getQty().longValue());
                                    log.info("<><><><><><><><><>【零售业务下单】【商户ID-{}】【SKU-{}】库存充足<><><><><><><><><>", store.id(), skuId);
                                }
                            });

                            // insert Order to Temp
                            // orderRepository.saveRetailOrderTempWithInventory(order, toCheckInvProdSkuIdList, toUpdateInvMap);
                            retailOrderRepository.saveRetailOrderTempWithInventory(order, toCheckInvProdSkuIdList, toUpdateInvMap);
                        } finally {
                            // lock.unlock();
//                            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
//                                lock.unlock();
//                                log.info("【零售业务下单】【商户ID-{}】零售下单核心逻辑已处理，库存锁释放，进入支付......", store.id());
//                            }
                        }
                    }
                    // 3func.下单库存管理-end


                    // 获取订单transId，即订单编号
                    Long transId = orderRepository.getOrderTransId(order.getId());
                    log.info("=========▶ transId::::" + transId);
                    // http://domain/eorder/wechat/api/v1/order_pay
                    String notifyUrl = appConfigure.get(AppConfigure.BASOFT_WECHAT_NOTIFY_URL);

                    //to Wechat：发起微信支付
                    WxPayJsResp resp = orderService.sendWechatOrder(store, order, ip, notifyUrl, getCertStream(), transId);
                    log.info("=========▶ orderService sendWechatOrder result::::" + resp);
                    return resp;
                }
                // 订单中的存在不合法的订购内容
                else {
                    log.error("=======>>>> ORDER DETAILS ARE SOME INVALID DATA <<<<======");
                }
            }
            // 查询到的订购产品SKU详情信息列表为空，即订单中的SKU_ID全部不合法
            else {
                log.error("=======>>>> ORDER DETAILS ARE ALL INVALID DATA <<<<======");
            }
        }
        // 订单中无订购内容
        else {
            log.error("========>>>> ORDER IS EMPTY!!!! <<<<======");
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
        resp.setReturn_msg(ReturnMsg);      //"payment is fail"
        return resp;
    }

    /**
     * CMS商户取消购物订单，即主动退款
     * （1）用户支付成功，然后商户退款，此时退款事件的类型为2
     * （2）商户已接单，然后商户退款，此时退款事件的类型为7
     *
     * @param dealRetailOrder
     * @param context
     * @return
     * @created in 20200518
     * @see com.basoft.eorder.interfaces.command.OrderCmmandHandler[cancelOrderReserve]方法。迁移改造自cancelOrderReserve方法
     */
    /*
        {
            "orderId": 1000000881,
            "eventType":2,
            "cancelReason":2
        }
        eventType 商户退款2或7，2-支付成功后商户退款 7-商户接单后商户退款
        cancelReason 1-用户退款 2-商户退款
     */
    @CommandHandler.AutoCommandHandler(DealRetailOrder.NAME_CANCEL_ORDER)
    public WxPayRefundResult cancelRetailOrder(DealRetailOrder dealRetailOrder, CommandHandlerContext context) {
        // 1、安全性验证：验证IP地址
        String ip = Objects.toString(context.props().get("realIp"), null);
        if (StringUtils.isBlank(ip)) {
            throw new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND IP");
        }
        int eventType = dealRetailOrder.getEventType();
        if(eventType != 2 && eventType != 7){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }
        if(dealRetailOrder.getCancelReason() != 1 && dealRetailOrder.getCancelReason() != 2){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 2、退款权限验证
        // 20190712验证退款权限------start
        log.info("=========▶ 非点餐业务退款权限验证=========▶START");
        UserSession userSession = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
        if (userSession == null) {
            throw new BizException(ErrorCode.SYS_TIMEOUT);
        }
        boolean permitRefund = false;
        int accountType = userSession.getAccountType();
        int accountRole = userSession.getAccountRole();
        log.info("=========▶ 非点餐业务退款权限信息【accountType||accountRole】=" + accountType + "||" + accountRole);
        // 门店超级管理员：如果账号类型为1
        if (accountType == 1) {
            permitRefund = true;
        }
        // 门店操作员
        else if (accountType == 2) {
            // 21-店长 22-退款管理员（退款、接单权限等等）
            if (accountRole == 21 || accountRole == 22) {
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
        if (!permitRefund) {
            log.info("=========▶ 非点餐退款权限验证结束=========▶不▶允许退款");
            // 抛出业务异常信息
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }
        log.info("=========▶ 非点餐业务退款权限验证结束=========▶▶允许退款");
        // 20190712验证退款权限------end


        // 3、获取订单和商户信息
        Long orderId = dealRetailOrder.getOrderId();
        Order order = orderRepository.getOrder(orderId);
        Store store = storeRepository.getStore(order.getStoreId());
        if (order == null || store == null) {
            WxPayRefundResult resp = new WxPayRefundResult();
            resp.setOut_trade_no(orderId.toString());
            // "FAIL"
            resp.setReturn_code("FAIL");
            // "payment is fail"
            resp.setReturn_msg("환불이 가능한 주문이 아닙니다.");
            return resp;
        }

        // 4、验证退款操作者所属门店ID和订单的门店ID是否一致（安全防范）
        Long sessionStoreId = userSession.getStoreId();
        Long orderStoreId = order.getStoreId();
        if (sessionStoreId != null && orderStoreId != null) {
            if (!(sessionStoreId.longValue() == orderStoreId.longValue())) {
                log.info("=========▶ userSessionStoreId : " + sessionStoreId);
                log.info("=========▶ orderStoreId : " + orderStoreId);
                log.error("=========▶ 退款操作者所属门店ID和订单的门店ID是否一致");
                // 抛出业务异常信息
                throw new BizException(ErrorCode.BIZ_EXCEPTION);
            }
        } else {
            log.error("=========▶ userSessionStoreId和orderStoreId都为空！！！");
            // 抛出业务异常信息
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }

        // 5、订单状态验证
        int orderStatus = order.getStatus();
        if(eventType == 2){
            // 订单状态必须为4，即支付成功，才允许商户退款2
            if(orderStatus != 4 && orderStatus != 8){
                throw new BizException(ErrorCode.BIZ_EXCEPTION);
            }
        } else if(eventType == 7) {
            // 订单状态必须为5，即已接单，才允许商户退款7
            if(orderStatus != 5 && orderStatus != 8){
                throw new BizException(ErrorCode.BIZ_EXCEPTION);
            }
        }

        // 6、存储退款原因到order_info表的cancel_reason字段。并且更新订单状态为6，即退款进行中
        retailOrderRepository.updateRetailOrderCancelReason(dealRetailOrder);

        // 7、发起微信退款
        log.info("Start cancelRetailOrder，Cancel Order orderId : {} -------", orderId);
        WxPayRefundResult respRst = orderService.sendWechatCancelRetailOrder(store, order, dealRetailOrder, ip, getCertStream());

        // 8、微信退款成功：（1）发送模板消息 （2）记录退款事件 （3）恢复库存
        if ("SUCCESS".equals(respRst.getReturn_code())) {
            if ("SUCCESS".equals(respRst.getResult_code())) {
                // （1）Template msg
                wechatTemplateMessageUtil.sendRetailOrderTemplateMsg(order, store, dealRetailOrder, true);

                //（2）记录退款事件
                try{
                    SaveRetailOrderEvent saveRetailOrderEvent = SaveRetailOrderEvent
                            .builder()
                            .orderId(orderId)
                            .eventType(dealRetailOrder.getEventType())
                            .build();
                    retailOrderEventRepository.saveRetailOrderEventAndOrderStatus(saveRetailOrderEvent.build());
                }catch (Exception e){
                    log.error("<<<<<<<<<<<<<<<<记录退款事件异常::::::>>>>>>>>>>" + e.getMessage());
                    e.printStackTrace();
                }

                //（3）恢复库存
                // 20200422 零售商户产品库存恢复-start
                if (store.storeType() == 3 && order.getOrderType() == CommonConstants.ORDER_TYPE_NORMAL) {
                    // 从临时订单表里查询的订购内容，嘻嘻。。。
                    List<RetailToDoRecoverTempOrder> tempOrderList = orderQuery.queryRetailTempOrderByIdToRecover(orderId.toString(), "refund");
                    if (tempOrderList == null || tempOrderList.isEmpty()) {
                        // nothing to do
                    } else {
                        log.info("【零售商户产品库存恢复】库存回复线程启动Start..........................................");
                        Thread thread = new Thread(new RetailOrderRecoverThread(inventoryRetailRepository, tempOrderList, CommonConstants.RETAIL_INVENTORY_RECOVER_REFUND));
                        thread.start();
                        log.info("【零售商户产品库存恢复】库存回复线程启动Over..........................................");
                    }
                }
                // 20200422 零售商户产品库存恢复-start
            }
        }

        return respRst;
    }
}