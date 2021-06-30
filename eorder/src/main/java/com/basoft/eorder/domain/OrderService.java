package com.basoft.eorder.domain;

import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.application.wx.model.WxPayJsResp;
import com.basoft.eorder.application.wx.model.WxPayQueryResp;
import com.basoft.eorder.application.wx.model.WxPayRefundResult;
import com.basoft.eorder.application.wx.model.WxPayResult;
import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.retail.order.RetailOrderEvent;
import com.basoft.eorder.interfaces.command.AcceptOrder;
import com.basoft.eorder.interfaces.command.ConfirmOrder;
import com.basoft.eorder.interfaces.command.SaveCancelOrder;
import com.basoft.eorder.interfaces.command.UpSettleStatus;
import com.basoft.eorder.interfaces.command.order.retail.DealRetailOrder;
import com.basoft.eorder.interfaces.command.retail.SaveRetailOrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface OrderService {
    // String KEY = "8d43cd026216cd319f000db04f993d31";

    // TnuPayReqResult sendTnuOrder(Order order) throws Exception;

    // void receiveTnuOrder(Map<String, String> result) throws Exception;

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
    WxPayJsResp sendWechatOrder(Store store, Order order, String ip, String notifyUrl, String cert, Long transId);

    /**
     * 保存微信支付平台返回的订单支付成功信息
     *
     * @param dataMap
     * @return
     * @throws Exception
     */
    Order receiveWechatOrder(Map<String, String> dataMap) throws Exception;

    WxPayRefundResult sendWechatCancelOrder(Store store, Order order, String ip, String cert);

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
    WxPayRefundResult sendWechatCancelRetailOrder(Store store, Order order, DealRetailOrder dealRetailOrder, String ip, String cert);

    /**
     * 零售业务订单事件退款-退款申请审核4和退款申请审核9
     *
     * @param retailOrderEvent
     */
    WxPayRefundResult retailOrderRefundFromEvent(RetailOrderEvent retailOrderEvent, String realIP, HttpServletRequest request);

    /**
     * 零售业务订单售后退款
     *
     * @param saveRetailOrderService
     * @param context
     * @return
     */
    WxPayRefundResult retailOrderRefundFromOrderService(SaveRetailOrderService saveRetailOrderService, CommandHandlerContext context);

    WxPayRefundResult queryWechatCancelOrder(Store store, Order order, String ip, String cert);

    WxPayResult queryWechatOrder(Store store, Order order, String ip, String cert);

    WxPayQueryResp queryWechatOrder(Store store, Map<String, Object> dataMap, String cert) throws Exception;

    int updateOrderStatus(Order order) throws Exception;

    //服务员接单
    int acceptOrder(AcceptOrder acceptOrder);

    //结算（确认收到客户的钱）
    int upSettleStatus(UpSettleStatus upSettleStatus);

    int confirmOrderClinic(ConfirmOrder confirmOrder);

    int confirmOrderShopping(ConfirmOrder confirmOrder);
    
    int acceptOrderClinic(ConfirmOrder confirmOrder);

	int acceptOrderHotel(ConfirmOrder confirmOrder);

	int confirmOrderHotel(ConfirmOrder confirmOrder);
	
	int cancelOrderReserve(ConfirmOrder confirmOrder);

	WxPayResult queryWechatOrderManual(Store store, Order order, String ip, SaveCancelOrder saveCancelOrder, String cert);
}
