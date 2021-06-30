package com.basoft.eorder.domain;

import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.OrderPay;
import com.basoft.eorder.domain.model.OrderPayCancel;
import com.basoft.eorder.domain.model.order.StoreDailySettlement;
import com.basoft.eorder.domain.model.order.StoreDailySettlementBA;
import com.basoft.eorder.domain.model.order.StoreSettlement;
import com.basoft.eorder.domain.model.order.StoreSettlementBA;
import com.basoft.eorder.interfaces.command.AcceptOrder;
import com.basoft.eorder.interfaces.command.ConfirmOrder;
import com.basoft.eorder.interfaces.command.SaveOrder;
import com.basoft.eorder.interfaces.command.UpSettleStatus;
import com.basoft.eorder.interfaces.query.order.HotelOrderDTO;

import java.util.List;
import java.util.Map;

public interface OrderRepository {
    /**
     * 保存订单信息到订单临时表
     *
     * @param order
     * @return
     */
    Order saveOrderTemp(Order order);

    /**
     * 酒店保存订单信息到订单临时表,同时扣减酒店库存
     *
     * @param order
     * @return
     */
    Order saveOrderTempWithInventory(Order order);

    /**
     * 插入微信支付成功后的OrderPay信息
     *
     * @param pay
     */
    void insertOrderPay(OrderPay pay);

    /**
     * 支付成功并响应微信支付平台后生成正式的订单
     *
     * @param order
     * @return
     */
    Order saveOrder(Order order);

    Order saveOrderFromTemp(Order order);

    Order getOrder(Long id);


    int updateOrderPay_fail(Order order);

    int updateOrder(Order order);

    int updateOrderStatusAndChange(Order order);

    int updateSuccessOrder(Order order);

    int updateOrder_success(Order order);

    int acceptOrder(AcceptOrder acceptOrder);

    int batchInsertOrderBackupCompled(List<Long> orderIds);

    int batchInsertRetailOrderBackupCompled(List<Long> orderIds);

    int batchUpOrderStatus(List<Long> orderIds, int status);


    void insertOrderPayCancel(OrderPayCancel payCancel);

    Long getOrderPayId(Long orderId);

    /**
     * 调用微信支付后，更新临时订单状态
     *
     * @param order
     * @return
     */
    int updateOrderTemp(Order order);

    int getClosing(Map<String, Object> param);

    int insertClosing(UpSettleStatus upSettleStatus);

    int upSettleStatus(UpSettleStatus upSettleStatus);

    /**
     * 生成正式订单（支付成功后的订单）时查询临时订单信息
     *
     * @param id
     * @return
     */
    Order getOrderPay(Long id);

    Order getOrderTemp(Long orderId);

    Long getOrderTransId(Long orderId);

    int confirmOrderClinic(ConfirmOrder confirmOrder);

    int acceptOrderClinic(ConfirmOrder confirmOrder);

    int acceptOrderHotel(ConfirmOrder confirmOrder);

    int confirmOrderHotel(ConfirmOrder confirmOrder);

    int confirmOrderShopping(ConfirmOrder confirmOrder);

    int cancelOrderReserve(ConfirmOrder confirmOrder);

    /**
     * 保存下单不支付的临时订单
     *
     * @param order
     */
    int saveNoPayTempOrder(Order order);

    /**
     * 保存下单不支付的订单
     *
     * @param order
     */
    void saveNoPayOrder(Order order);

    int saveStoreCustNo(SaveOrder saveOrder);

    int upShippingCmt(SaveOrder saveOrder);

    /**
     * 酒店库存恢复，查询酒店订单的sku
     *
     * @param orderId
     * @return
     */
    Long queryHotelOrderItem(Long orderId);

    /**
     * 删除PG交易类商户的月度结算结果
     *
     * @param storeId
     * @param year
     * @param month
     */
    public Long deleteStoreSettlement(Long storeId, int year, int month);

    /**
     * 存储PG交易类商户的月度结算结果
     *
     * @param storeSettlement
     */
    public void saveStoreSettlement(StoreSettlement storeSettlement);

    /**
     * 存储PG交易类商户的月度结算结果(手工)
     *
     * @param storeId
     * @param year
     * @param month
     * @param storeSettlement
     */
    public void saveStoreSettlementManual(Long storeId, int year, int month, StoreSettlement storeSettlement);

    /**
     * 插入更新（存在则先删除）PG交易类商户结算日的结算结果
     *
     * @param storeDailySettlement
     */
    void saveStoreDailySettlement(StoreDailySettlement storeDailySettlement);


    /**
     * 删除BA交易类商户的月度结算结果
     *
     * @param storeId
     * @param year
     * @param month
     */
    public Long deleteBAStoreSettlement(Long storeId, int year, int month);

    /**
     * 存储BA交易类商户的月度结算结果
     *
     * @param storeSettlementBA
     */
    public void saveBAStoreSettlement(StoreSettlementBA storeSettlementBA);

    /**
     * 存储BA交易类商户的月度结算结果(手工)
     *
     * @param storeId
     * @param year
     * @param month
     * @param storeSettlementBA
     */
    public void saveBAStoreSettlementManual(Long storeId, int year, int month, StoreSettlementBA storeSettlementBA);

    /**
     * 插入更新（存在则先删除）PG交易类商户结算日的结算结果
     *
     * @param storeDailySettlementBA
     */
    void saveBAStoreDailySettlement(StoreDailySettlementBA storeDailySettlementBA);

    /**
     * 查询指定ID的酒店订单信息
     *
     * @param orderId
     * @return
     */
    HotelOrderDTO getHotelOrderInfo(Long orderId);

    /**
     * 查询指定酒店商户、指定房间prodSKU、指定日期期间的已经被订单确认或订单完成的明细
     *
     * @param params
     * @return
     */
    List<HotelOrderDTO> getHotelOrderDetailList(Map<String, Object> params);
}
