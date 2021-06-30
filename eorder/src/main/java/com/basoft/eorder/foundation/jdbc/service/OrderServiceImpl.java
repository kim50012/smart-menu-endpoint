package com.basoft.eorder.foundation.jdbc.service;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.application.wx.api.WechatPayAPI;
import com.basoft.eorder.application.wx.model.WxPayJsResp;
import com.basoft.eorder.application.wx.model.WxPayQueryResp;
import com.basoft.eorder.application.wx.model.WxPayRefundResult;
import com.basoft.eorder.application.wx.model.WxPayResp;
import com.basoft.eorder.application.wx.model.WxPayResult;
import com.basoft.eorder.application.wx.sdk.WXPayUtil;
import com.basoft.eorder.batch.job.model.retail.RetailToDoRecoverTempOrder;
import com.basoft.eorder.batch.job.threads.retail.RetailOrderRecoverThread;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.OrderRepository;
import com.basoft.eorder.domain.OrderService;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.OrderPay;
import com.basoft.eorder.domain.model.OrderPayCancel;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.retail.order.RetailOrderEvent;
import com.basoft.eorder.domain.order.retail.RetailOrderEventRepository;
import com.basoft.eorder.domain.retail.InventoryRetailRepository;
import com.basoft.eorder.domain.retail.RetailOrderRepository;
import com.basoft.eorder.domain.retail.RetailOrderServiceRepository;
import com.basoft.eorder.interfaces.command.AcceptOrder;
import com.basoft.eorder.interfaces.command.ConfirmOrder;
import com.basoft.eorder.interfaces.command.SaveCancelOrder;
import com.basoft.eorder.interfaces.command.UpSettleStatus;
import com.basoft.eorder.interfaces.command.order.common.WechatTemplateMessageUtil;
import com.basoft.eorder.interfaces.command.order.retail.DealRetailOrder;
import com.basoft.eorder.interfaces.command.retail.SaveRetailOrderEvent;
import com.basoft.eorder.interfaces.command.retail.SaveRetailOrderService;
import com.basoft.eorder.interfaces.query.OrderQuery;
import com.basoft.eorder.util.BeanUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private OrderRepository orderRepository;

    private RetailOrderRepository retailOrderRepository;

    private StoreRepository storeRepository;

    private AppConfigure appConfigure;

    private RetailOrderEventRepository retailOrderEventRepository;

    private WechatTemplateMessageUtil wechatTemplateMessageUtil;

    private OrderQuery orderQuery;

    private InventoryRetailRepository inventoryRetailRepository;

    @Autowired
    private RetailOrderServiceRepository retailOrderServiceRepository;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setRetailOrderRepository(RetailOrderRepository retailOrderRepository) {
        this.retailOrderRepository = retailOrderRepository;
    }

    @Autowired
    public void setStoreRepository(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Autowired
    public void setAppConfigure(AppConfigure appConfigure) {
        this.appConfigure = appConfigure;
    }

    @Autowired
    public void setRetailOrderEventRepository(RetailOrderEventRepository retailOrderEventRepository) {
        this.retailOrderEventRepository = retailOrderEventRepository;
    }

    @Autowired
    public void setWechatTemplateMessageUtil(WechatTemplateMessageUtil wechatTemplateMessageUtil) {
        this.wechatTemplateMessageUtil = wechatTemplateMessageUtil;
    }

    @Autowired
    public void setOrderQuery(OrderQuery orderQuery) {
        this.orderQuery = orderQuery;
    }

    @Autowired
    public void setInventoryRetailRepository(InventoryRetailRepository inventoryRetailRepository) {
        this.inventoryRetailRepository = inventoryRetailRepository;
    }

    /**
     * 向微信支付平台进行下单
     *
     * @param store
     * @param order
     * @param ip
     * @param notifyUrl
     * @param cert
     * @param transId
     * @return
     */
    @Override
    public WxPayJsResp sendWechatOrder(Store store, Order order, String ip, String notifyUrl, String cert, Long transId) {
        BigDecimal decUsdAmt = order.getPayAmtUsd();
        BigDecimal wxH = new BigDecimal(100);
        BigDecimal intUsdAmt = decUsdAmt.multiply(wxH).setScale(0, BigDecimal.ROUND_UP);
        String amount = (intUsdAmt + "");
        String describe = (order.getTableId() + "");
        String attach = (order.getStoreId() + "");
        String openid = order.getCustomerId();
        String apiKey = store.apiKey();
        Order.Status status = Order.Status.NORMAL;

        WxPayResp resp = null;
        try {
            resp = WechatPayAPI.unifiedOrder(store, order.getId() + "", amount, describe, ip, attach, openid, notifyUrl, cert, transId);
            // status = Order.Status.SUCCES;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            status = Order.Status.SEND;
        }

        WxPayJsResp jsResp = null;
        if (resp != null) {
            if ("SUCCESS".equals(resp.getReturn_code())) {
                if ("SUCCESS".equals(resp.getResult_code())) {
                    status = Order.Status.SUCCES;
                    order.setStatus(status);
                    order.setPrepayId(resp.getPrepay_id());

                    // update temp Order
                     orderRepository.updateOrderTemp(order);

                    //Create Sign by dikim on 20190226
                    Long stamp = WXPayUtil.getCurrentTimestamp();
                    Map<String, String> data = Maps.newHashMap();
                    data.put("appId", resp.getAppid());
                    data.put("timeStamp", stamp.toString());
                    data.put("nonceStr", resp.getNonce_str());
                    data.put("package", "prepay_id=" + resp.getPrepay_id());
                    data.put("signType", WechatPayAPI.HMACSHA256);

                    // logger.info("▶▶▶▶▶ WxPayJsResp - sign data ◀◀◀◀◀ \n" + data.toString());

                    String paySign;
                    try {
                        paySign = WXPayUtil.generateSignature(data, apiKey);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        paySign = "";
                        e.printStackTrace();
                    }

                    // logger.info("▶▶▶▶▶ WxPayJsResp - sign paySign ◀◀◀◀◀ \n" + paySign);

                    jsResp = new WxPayJsResp().createWxPayJsResp(resp, WechatPayAPI.HMACSHA256, order.getId() + "", paySign, stamp.toString()).build();
                    jsResp.setReturn_code("SUCCESS");
                    jsResp.setReturn_msg("payment is success");

                    logger.info("▶▶▶▶▶ WxPayJsResp - return jsResp ◀◀◀◀◀ \n" + jsResp.toString());
                } else {
                    status = Order.Status.FAIL;
                    jsResp = new WxPayJsResp.Builder()
                            .orderId(order.getId() + "")
                            .build();
                    jsResp.setReturn_code("FAIL");
                    jsResp.setReturn_msg("[" + resp.getErr_code() + "]" + resp.getErr_code_des());

                    logger.error("<<<<< error >>>>> code : " + resp.getErr_code() + ", desc : " + resp.getErr_code_des());
                }
            } else {
                status = Order.Status.FAIL;
                jsResp = new WxPayJsResp.Builder()
                        .orderId(order.getId() + "")
                        .build();
                jsResp.setReturn_code("FAIL");
                jsResp.setReturn_msg("payment is fail");

                logger.error("<<<<< error >>>>> msg : sign error ");
            }
        }

        order.setStatus(status);
        orderRepository.updateOrderTemp(order);
        /*if ((!status.equals(Order.Status.SUCCES))) {		//Order Status 가 Success 가 아니니 경우 Temp Status 업데이트 처리 함
            orderRepository.updateOrderTemp(order);
        }*/
        return jsResp;
    }

    /**
     * 保存微信支付平台返回的订单支付成功信息
     *
     * @param dataMap
     * @return
     * @throws Exception
     */
    /*
        <?xml version="1.0" encoding="utf-8"?>
        <xml>
          <appid><![CDATA[wx169f9463dac237ee]]></appid>
          <attach><![CDATA[635790301684634630]]></attach>
          <bank_type><![CDATA[CCB_DEBIT]]></bank_type>
          <cash_fee><![CDATA[301]]></cash_fee>
          <cash_fee_type><![CDATA[CNY]]></cash_fee_type>
          <device_info><![CDATA[707646140248298501]]></device_info>
          <fee_type><![CDATA[USD]]></fee_type>
          <is_subscribe><![CDATA[N]]></is_subscribe>
          <mch_id><![CDATA[1413386802]]></mch_id>
          <nonce_str><![CDATA[A1ldcfYnqVUYbZrmBsYbXpiMuUbDma1k]]></nonce_str>
          <openid><![CDATA[oJ_98wGAwuJznlOlQUlghmTq1E64]]></openid>
          <out_trade_no><![CDATA[BT1000000544]]></out_trade_no>
          <rate_value><![CDATA[717420000]]></rate_value>
          <result_code><![CDATA[SUCCESS]]></result_code>
          <return_code><![CDATA[SUCCESS]]></return_code>
          <sign><![CDATA[BA280202D7E47AAC02756FEC9F1C35FCEAE0ADD5BA301EB4A497578EA48667EC]]></sign>
          <sub_appid><![CDATA[wx7202c9b1660cc48d]]></sub_appid>
          <sub_is_subscribe><![CDATA[Y]]></sub_is_subscribe>
          <sub_mch_id><![CDATA[272399448]]></sub_mch_id>
          <sub_openid><![CDATA[o2dki5sQNSZ2_iJaFeNmJBu9_ai8]]></sub_openid>
          <time_end><![CDATA[20190904164643]]></time_end>
          <total_fee>42</total_fee>
          <trade_type><![CDATA[JSAPI]]></trade_type>
          <transaction_id><![CDATA[4200000349201909042562078986]]></transaction_id>
        </xml>
    */
    @Override
    @Transactional
    public Order receiveWechatOrder(Map<String, String> dataMap) throws Exception {
        WxPayResult payResult = BeanUtil.reflectMapToObject((Map) dataMap, WxPayResult.class);
        logger.info("保存微信支付平台返回的订单支付成功信息：：receiveWechatOrder：：payResult" + payResult.toString());

        Long orderId = NumberUtils.toLong(payResult.getDevice_info());
        Long payId = NumberUtils.toLong(payResult.getOut_trade_no().substring(2));
        // String storeId = payResult.getAttach();
        BigDecimal payAmtCny = new BigDecimal(payResult.getCash_fee());
        BigDecimal rate = new BigDecimal(0.01);
        payAmtCny = payAmtCny.multiply(rate);

        // Long payId = orderRepository.getOrderPayId(orderId);
        // Long payId = null;

        // 获取临时订单信息（也会查询该临时订单对应的支付信息）
        Order orderPay = orderRepository.getOrderPay(orderId);
        // payId = orderPay.getPayId();

        /*if (payId == null || payId == 0) {
        	payId = uidGenerator.generate(BusinessTypeEnum.ORDER_PAY);
        }*/

        OrderPay pay = new OrderPay().createnewOrderPayByResult(payId, payResult).build();

        //이번 거래의 Order 건을 정상 완료 Insert & Update 한다. by dikim on 20190304
        orderRepository.insertOrderPay(pay);


        // 如果支付结果orderPay的PayId为空则保存Order
        if (!(orderPay.getPayId() > 0)) {
            Order order = new Order.Builder()
                    .id(orderId)
                    .status(Order.Status.PAYMENY)
                    .payAmtCny(payAmtCny)
                    .transId(payId)
                    .bizType(orderPay.getBizType())
                    .build();
            // 根据临时订单生成正式订单
            orderRepository.saveOrder(order);
        }

        // Order 상태를 정상으로 업데이트 한다. by dikim on 20190304
        // orderRepository.updateOrder_success(order);
        logger.debug("保存微信支付平台返回的订单支付成功信息返回对象：：Order：：" + orderPay.toString());
        return orderPay;
    }

    @Override
    public WxPayQueryResp queryWechatOrder(Store store, Map<String, Object> dataMap, String cert) throws Exception {
        return WechatPayAPI.orderQuery(store, (Map) dataMap, cert);
    }

    @Override
    public int updateOrderStatus(Order order) {
        return orderRepository.updateOrder(order);
    }

    @Override
    public int acceptOrder(AcceptOrder acceptOrder) {
        return orderRepository.acceptOrder(acceptOrder);
    }

    @Override
    public int acceptOrderClinic(ConfirmOrder confirmOrder) {
        return orderRepository.acceptOrderClinic(confirmOrder);
    }

    @Override
    public int confirmOrderClinic(ConfirmOrder confirmOrder) {
        return orderRepository.confirmOrderClinic(confirmOrder);
    }

    @Override
    public int confirmOrderShopping(ConfirmOrder confirmOrder) {
        return orderRepository.confirmOrderShopping(confirmOrder);
    }
    
    @Override
    public int acceptOrderHotel(ConfirmOrder confirmOrder) {
        return orderRepository.acceptOrderHotel(confirmOrder);
    }

    @Override
    public int confirmOrderHotel(ConfirmOrder confirmOrder) {
        return orderRepository.confirmOrderHotel(confirmOrder);
    }

    @Override
    public int cancelOrderReserve(ConfirmOrder confirmOrder) {
        return orderRepository.cancelOrderReserve(confirmOrder);
    }

    /**
     * 电子点餐、酒店和医美退款
     *
     * @param store
     * @param order
     * @param ip
     * @param cert
     * @return
     */
    @Override
    public WxPayRefundResult sendWechatCancelOrder(Store store, Order order, String ip, String cert) {
        BigDecimal decUsdAmt = order.getPayAmtUsd();
        BigDecimal wxH = new BigDecimal(100);
        BigDecimal intUsdAmt = decUsdAmt.multiply(wxH).setScale(0, BigDecimal.ROUND_UP);
        String amount = (intUsdAmt + "");
        String describe = (order.getStoreId() + "");
        String attach = (order.getTableId() + "");
        String openid = order.getCustomerId();

        // generate calcenId by dikim
        Long orderId = order.getId();
        String alliexTransId = (orderId + "");
        String refundIdTemp = "9" + alliexTransId.substring(1);
        
        // START 'REFUNDNOTEXIST error order' re-try refund by dikim on 20200610
        String cmtREFUNDNOTEXIST = order.getCmt();
        if ("REFUNDNOTEXIST".equals(cmtREFUNDNOTEXIST)) {
        	refundIdTemp = "91" + alliexTransId.substring(2);
        }
        // END 'REFUNDNOTEXIST error order' re-try refund by dikim on 20200610
        
        Long cancelId = NumberUtils.toLong(refundIdTemp);

        Order.Status status = Order.Status.NORMAL;

        WxPayRefundResult resp = null;
        try {
            resp = WechatPayAPI.refund(store, alliexTransId, amount, describe, ip, attach, openid, cert, cancelId);
            // status = Order.Status.SUCCES;
        } catch (Exception e) {
            // logger.error(e.getMessage(), e);
            status = Order.Status.CANCEL_FAIL;
            logger.error("<<<<< error >>>>> " + e.getMessage());
        }

        // WxPayRefundResult jsResp = null;

        if (resp != null) {
            if ("SUCCESS".equals(resp.getReturn_code())) {
                if ("SUCCESS".equals(resp.getResult_code())) {
                    // Map<String, String> data = (Map<String, String>) BeanUtil.reflectObjectToMap((WxPayRefundResult) resp, WxPayRefundResult.class);
                    try {
                        status = Order.Status.CANCELED;

                        //Long cancelId = uidGenerator.generate(BusinessTypeEnum.ORDER_PAY_CANCEl);
                        OrderPayCancel payCancel = new OrderPayCancel().createnewOrderPayCancelByResult(cancelId, resp, orderId).build();

                        orderRepository.insertOrderPayCancel(payCancel);
                        logger.info("▶▶▶▶▶ WxPayJsResp - return resp ◀◀◀◀◀ \n" + resp.toString());
                    } catch (Exception e) {
                        status = Order.Status.CANCEL_FAIL;
                        e.printStackTrace();
                    }
                } else {
                    status = Order.Status.CANCEL_FAIL;
                    logger.error("<<<<< error >>>>> code : " + resp.getErr_code() + ", desc : " + resp.getErr_code_des());
                }
            } else {
                status = Order.Status.CANCEL_FAIL;
                logger.error("<<<<< error >>>>> msg : sign error ");
            }
        }

        order.setStatus(status);

        // Cancel order status update
        orderRepository.updateOrder(order);

        return resp;
    }

    /**
     * 零售业务退款
     *
     * @param store
     * @param order
     * @param ip
     * @param cert
     * @return
     * @created in 20200518
     */
    @Override
    public WxPayRefundResult sendWechatCancelRetailOrder(Store store, Order order, DealRetailOrder dealRetailOrder, String ip, String cert) {
        BigDecimal decUsdAmt = order.getPayAmtUsd();
        BigDecimal wxH = new BigDecimal(100);
        BigDecimal intUsdAmt = decUsdAmt.multiply(wxH).setScale(0, BigDecimal.ROUND_UP);
        String amount = (intUsdAmt + "");
        String describe = (order.getStoreId() + "");
        String attach = (order.getTableId() + "");
        String openid = order.getCustomerId();

        // generate calcenId
        Long orderId = order.getId();
        String alliexTransId = (orderId + "");
        String refundIdTemp = "9" + alliexTransId.substring(1);
        Long cancelId = NumberUtils.toLong(refundIdTemp);

        Order.Status status = Order.Status.NORMAL;

        WxPayRefundResult resp = null;
        try {
            resp = WechatPayAPI.refund(store, alliexTransId, amount, describe, ip, attach, openid, cert, cancelId);
        } catch (Exception e) {
            status = Order.Status.CANCEL_FAIL;
            logger.error("<<<<< error >>>>> " + e.getMessage());
        }

        if (resp != null) {
            if ("SUCCESS".equals(resp.getReturn_code())) {
                if ("SUCCESS".equals(resp.getResult_code())) {
                    // Map<String, String> data = (Map<String, String>) BeanUtil.reflectObjectToMap((WxPayRefundResult) resp, WxPayRefundResult.class);
                    try {
                        status = Order.Status.CANCELED;

                        //Long cancelId = uidGenerator.generate(BusinessTypeEnum.ORDER_PAY_CANCEl);
                        OrderPayCancel payCancel = new OrderPayCancel().createnewOrderPayCancelByResult(cancelId, resp, orderId).build();

                        orderRepository.insertOrderPayCancel(payCancel);
                        logger.info("▶▶▶▶▶ WxPayJsResp - return resp ◀◀◀◀◀ \n" + resp.toString());
                    } catch (DuplicateKeyException e){
                        // 重复退款导致的订单退款信息重复插入，此时订单退款设置为成功
                        status = Order.Status.CANCELED;
                        e.printStackTrace();
                    } catch (Exception e) {
                        status = Order.Status.CANCEL_FAIL;
                        e.printStackTrace();
                    }
                } else {
                    status = Order.Status.CANCEL_FAIL;
                    logger.error("<<<<< error >>>>> code : " + resp.getErr_code() + ", desc : " + resp.getErr_code_des());
                }
            } else {
                status = Order.Status.CANCEL_FAIL;
                logger.error("<<<<< error >>>>> msg : sign error ");
            }
        }

        order.setStatus(status);
        // 退款成功
        if(status == Order.Status.CANCELED){
            // Cancel order status update
            orderRepository.updateOrder(order);
        }
        // 退款失败
        else if(status == Order.Status.CANCEL_FAIL){
            order.setStatus8From(dealRetailOrder.getEventType());
            retailOrderRepository.updateRetailOrderTo8(order);
        }
        return resp;
    }

    /**
     * 零售业务订单事件退款-退款申请审核4和退款申请审核9
     *
     * @param retailOrderEvent
     * @see  com.basoft.eorder.interfaces.command.order.retail.RetailOrderCommandHandler[cancelRetailOrder]方法，逻辑和该方法完全相同
     * @created in 20200519
     */
    public WxPayRefundResult retailOrderRefundFromEvent(RetailOrderEvent retailOrderEvent, String realIP, HttpServletRequest request){
        // 1、安全性验证：验证IP地址
        String ip = Objects.toString(realIP, null);
        if (StringUtils.isBlank(ip)) {
            throw new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND IP");
        }
        int eventType = retailOrderEvent.getEventType();
        if(eventType != 4 && eventType != 9){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 2、退款权限验证
        // 20190712验证退款权限------start
        log.info("=========▶ 非点餐业务退款权限验证=========▶START");
        UserSession userSession = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
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
        Long orderId = retailOrderEvent.getOrderId();
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
        if(eventType == 4){
            // 订单状态必须为4或8，即支付成功，才允许用户退款申请的审核
            if(orderStatus != 4 && orderStatus != 8){
                throw new BizException(ErrorCode.BIZ_EXCEPTION);
            }
        } else if(eventType == 9) {
            // 订单状态必须为5或8，即已接单，才允许用户退款申请的审核
            if(orderStatus != 5 && orderStatus != 8){
                throw new BizException(ErrorCode.BIZ_EXCEPTION);
            }
        }
        // 订单变更状态验证
        int orderChangeStatus = order.getChangeStatus();
        if(orderChangeStatus != 1){
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }

        // 6、存储退款原因到order_info表的cancel_reason字段。并且更新订单状态为6，即退款进行中
        DealRetailOrder dealRetailOrder = DealRetailOrder
                .builder()
                .orderId(retailOrderEvent.getOrderId())
                .eventType(eventType)
                .cancelReason(1)
                .build();
        retailOrderRepository.updateRetailOrderCancelReason(dealRetailOrder);

        // 7、发起微信退款
        log.info("Start cancelRetailOrder，Cancel Order orderId : {} -------", orderId);
        WxPayRefundResult respRst = this.sendWechatCancelRetailOrder(store, order, dealRetailOrder, ip, getCertStream());

        // 8、微信退款成功：（1）发送模板消息 （2）记录退款事件 （3）恢复库存
        if ("SUCCESS".equals(respRst.getReturn_code())) {
            if ("SUCCESS".equals(respRst.getResult_code())) {
                // （1）Template msg
                wechatTemplateMessageUtil.sendRetailOrderTemplateMsg(order, store, dealRetailOrder, true);

                //（2）记录退款事件，如果退款申请审核4则存储退款事件5，如果退款申请审核9则存储退款事件10
                try{
                    SaveRetailOrderEvent saveRetailOrderEvent = null;
                    if(eventType == 4){
                        saveRetailOrderEvent = SaveRetailOrderEvent
                                .builder()
                                .orderId(orderId)
                                .eventType(5)
                                .build();
                    } else if(eventType == 9){
                        saveRetailOrderEvent = SaveRetailOrderEvent
                                .builder()
                                .orderId(orderId)
                                .eventType(10)
                                .build();
                    }
                    retailOrderEventRepository.saveRetailOrderEvent(saveRetailOrderEvent.build());
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









    /*****************************************************售后退款-START***********************************************/
    /**
     * 零售业务订单售后退款
     * 进入该方法的前提是：
     * （1）零售业务订单售后申请通过审核
     * （2）退款类型为1或2，即全部退款或部分退款
     * （3）退款金额大于0且小于订单支付金额
     *
     * @param saveRetailOrderService
     * @param context
     * @return
     * @created in 20200521
     */
    public WxPayRefundResult retailOrderRefundFromOrderService(SaveRetailOrderService saveRetailOrderService, CommandHandlerContext context) {
        // 1、安全性验证：验证IP地址
        // 安全性验证：验证IP地址
        String ip = Objects.toString(context.props().get("realIp"), null);
        if (StringUtils.isBlank(ip)) {
            throw new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND IP");
        }

        // 2、退款权限验证
        // 验证退款权限------start
        log.info("=========▶ 非点餐业务退款权限验证=========▶START");
        //UserSession userSession = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
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
        // 验证退款权限------end


        // 3、获取订单和商户信息
        Long orderId = saveRetailOrderService.getOrderId();
        Order order = orderRepository.getOrder(orderId);
        Store store = storeRepository.getStore(order.getStoreId());
        if (order == null || store == null) {
            WxPayRefundResult resp = new WxPayRefundResult();
            resp.setOut_trade_no(orderId.toString());
            resp.setReturn_code("FAIL");
            resp.setReturn_msg("订单信息或商户信息异常！");
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

        // 5、订单状态和订单变更状态验证
        if(order.getStatus() != 9 && order.getChangeStatus() != 2) {
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }

        // 6、发起微信退款
        log.info("发起零售业务订单售后退款，售后申请信息为：：：：-------" + saveRetailOrderService.toString());
        WxPayRefundResult respRst = this.sendWechatCancelRetailOrderFromOrderService(store, order
                , saveRetailOrderService, ip, getCertStream());

        // 7、微信退款成功：发送模板消息
        /*if ("SUCCESS".equals(respRst.getReturn_code())) {
            if ("SUCCESS".equals(respRst.getResult_code())) {
                // Template msg
                wechatTemplateMessageUtil.sendRetailOrderTemplateMsg(order
                        , store
                        , DealRetailOrder.builder().cancelReason(3).build()
                        , true);
            }
        }*/
        // 此处退款成功处理只有发送模板消息，鉴于发送模板消息所需参数较多，故迁移至sendWechatCancelRetailOrderFromOrderService方法中发送
        log.info("零售业务订单售后退款完成！退款结果为：：：：" + respRst.toString());

        return respRst;
    }

    /**
     * 零售业务售后退款[全额退款/部分退款]
     *
     * @param store
     * @param order
     * @param saveRetailOrderService
     * @param ip
     * @param cert
     * @return
     * @created in 20200521
     */
    private WxPayRefundResult sendWechatCancelRetailOrderFromOrderService(Store store, Order order
            , SaveRetailOrderService saveRetailOrderService, String ip, String cert) {
        // 订单总金额-韩元
        BigDecimal payAmountKor = order.getPaymentAmount();
        // 订单总金额-美元
        BigDecimal payAmountUsd = order.getPayAmtUsd();
        // 退款金额-韩元
        BigDecimal refundAmountKor = saveRetailOrderService.getAuditRefundAmount();// 韩元
        // 退款金额-美元
        BigDecimal refundAmountUsd = payAmountUsd.multiply(refundAmountKor).divide(payAmountKor, 6, BigDecimal.ROUND_UP).setScale(2, BigDecimal.ROUND_UP);

        // 订单金额美元和退款金额美元转换为美分
        BigDecimal wxH = new BigDecimal(100);
        String payAmountUsdStr = payAmountUsd.multiply(wxH).setScale(0, BigDecimal.ROUND_UP) + "";
        String refundAmountUsdStr = refundAmountUsd.multiply(wxH).setScale(0, BigDecimal.ROUND_UP) + "";

        String describe = (order.getStoreId() + "");

        String attach = (order.getTableId() + "");

        String openid = order.getCustomerId();

        Long orderId = order.getId();

        // 商户订单号
        String alliexTransId = (orderId + "");

        // 商户退款单号：该次退款的退款单号，使用零售业务订单售后申请的申请编码
        Long cancelId = saveRetailOrderService.getServCode();

        // 零售业务售后状态：4-退款中 5-退款成功 6-退款失败
        int servStatus = 4;
        WxPayRefundResult resp = null;
        try {
            // 全额退款
            // resp = WechatPayAPI.refund(store, alliexTransId, amount, describe, ip, attach, openid, cert, cancelId);

            // 部分退款
            resp = WechatPayAPI.partRefund(store, alliexTransId, payAmountUsdStr, refundAmountUsdStr, describe, ip, attach, openid, cert, cancelId);
        } catch (Exception e) {
            servStatus = 6;
            logger.error("<<<<< error >>>>> " + e.getMessage());
        }

        // 是否发送模板消息，默认不发送
        boolean isSend = false;
        if (resp != null) {
            if ("SUCCESS".equals(resp.getReturn_code())) {
                if ("SUCCESS".equals(resp.getResult_code())) {
                    try {
                        servStatus = 5;
                        OrderPayCancel payCancel = new OrderPayCancel().createnewOrderPayCancelByResult(cancelId, resp, orderId).build();
                        orderRepository.insertOrderPayCancel(payCancel);
                        isSend = true;// 发送模板消息
                        logger.info("▶▶▶▶▶ WxPayJsResp - return resp ◀◀◀◀◀ \n" + resp.toString());
                    } catch (DuplicateKeyException e){
                        // 重复退款导致的订单退款信息重复插入，此时订单退款设置为成功
                        servStatus = 5;
                        e.printStackTrace();

                        // 重复退款不再发送模板消息 isSend = false
                    } catch (Exception e) {
                        servStatus = 6;
                        e.printStackTrace();

                        // 退款异常不需发送模板消息 isSend = false
                    }
                } else {
                    servStatus = 6;

                    // 退款异常不需发送模板消息 isSend = false

                    logger.error("<<<<< error >>>>> code : " + resp.getErr_code() + ", desc : " + resp.getErr_code_des());
                }
            } else {
                servStatus = 6;

                // 退款异常不需发送模板消息 isSend = false

                logger.error("<<<<< error >>>>> msg : sign error ");
            }
        }

        // 发送模板消息
        if(isSend){
            //订单总金额-人民币
            BigDecimal payAmountCny = order.getPayAmtCny();
            // 退款金额-人民币
            BigDecimal refundAmountCny = payAmountCny.multiply(refundAmountKor).divide(payAmountKor, 6, BigDecimal.ROUND_UP).setScale(2, BigDecimal.ROUND_UP);

            // Template msg
            wechatTemplateMessageUtil.sendRetailOrderTemplateMsg(order
                    , store
                    , DealRetailOrder.builder()
                            .cancelReason(3)
                            .refundAmountKor(refundAmountKor)
                            .refundAmountCny(refundAmountCny)
                            .build()
                    , true);
        }

        // 更新售后信息退款相关状态。5-退款成功 6-退款失败
        saveRetailOrderService.setServStatus(servStatus);
        retailOrderServiceRepository.updateOrderServiceStatus(saveRetailOrderService);

        return resp;
    }

    private String getCertStream() {
        String certPath = appConfigure.get(AppConfigure.BASOFT_WECHAT_CERT_PATH);
        return certPath;
    }
    /*******************************************************售后退款-END****************************************************/









    @Override
    public WxPayRefundResult queryWechatCancelOrder(Store store, Order order, String ip, String cert) {
        BigDecimal decUsdAmt = order.getPayAmtUsd();
        BigDecimal wxH = new BigDecimal(100);
        BigDecimal intUsdAmt = decUsdAmt.multiply(wxH).setScale(0, BigDecimal.ROUND_UP);
        String amount = (intUsdAmt + "");
        String describe = (order.getStoreId() + "");
        String attach = (order.getTableId() + "");
        String refundId = order.getRefundId();
        String apiKey = store.apiKey();
        Order.Status status = Order.Status.NORMAL;

        // generate calcenId by dikim
        String alliexTransId = (order.getTransId() + "");
        String refundIdTemp = "9" + alliexTransId.substring(1);
        Long cancelId = NumberUtils.toLong(refundIdTemp);

        WxPayRefundResult resp = null;
        try {
            resp = WechatPayAPI.refundQuery(store, alliexTransId, amount, describe, ip, attach, refundId, cert, cancelId);
            // status = Order.Status.SUCCES;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resp;
    }

    @Override
    public WxPayResult queryWechatOrder(Store store, Order order, String ip, String cert) {
        BigDecimal decUsdAmt = order.getPayAmtUsd();
        BigDecimal wxH = new BigDecimal(100);
        BigDecimal intUsdAmt = decUsdAmt.multiply(wxH).setScale(0, BigDecimal.ROUND_UP);
        String amount = (intUsdAmt + "");
        String describe = (order.getStoreId() + "");
        String attach = (order.getTableId() + "");
        String refundId = order.getRefundId();
        /*String apiKey = store.apiKey();
        Order.Status status = Order.Status.NORMAL;*/
        String outTradeNo = (store.transidType() + order.getId() + "");

        WxPayResult resp = null;
        try {
            resp = WechatPayAPI.queryOrder(store, outTradeNo, amount, describe, ip, attach, refundId, cert);
            // status = Order.Status.SUCCES;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return resp;
    }

    @Override
    public WxPayResult queryWechatOrderManual(Store store, Order order, String ip, SaveCancelOrder saveCancelOrder, String cert) {
        BigDecimal decUsdAmt = order.getPayAmtUsd();
        BigDecimal wxH = new BigDecimal(100);
        BigDecimal intUsdAmt = decUsdAmt.multiply(wxH).setScale(0, BigDecimal.ROUND_UP);
        String amount = (intUsdAmt + "");
        String describe = (order.getStoreId() + "");
        String attach = (order.getTableId() + "");
        String refundId = order.getRefundId();
        /*String apiKey = store.apiKey();
        Order.Status status = Order.Status.NORMAL;*/
        String outTradeNo = saveCancelOrder.getRefundId();

        WxPayResult resp = null;
        try {
            resp = WechatPayAPI.queryOrder(store, outTradeNo, amount, describe, ip, attach, refundId, cert);
            // status = Order.Status.SUCCES;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return resp;
    }

    @Override
    public int upSettleStatus(UpSettleStatus upSettleStatus) {
        return orderRepository.upSettleStatus(upSettleStatus);
    }
}