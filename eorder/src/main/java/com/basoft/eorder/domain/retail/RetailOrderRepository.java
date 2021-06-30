package com.basoft.eorder.domain.retail;

import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.retailOrderService.RetailOrderService;
import com.basoft.eorder.interfaces.command.order.retail.DealRetailOrder;
import com.basoft.eorder.interfaces.command.retail.SaveRetailOrderEvent;

import java.util.List;
import java.util.Map;

public interface RetailOrderRepository {
    /**
     * 保存订单信息到订单临时表
     *
     * @param order
     * @return
     */
    Order saveRetailOrderTemp(Order order);

    /**
     * 保存零售订单信息到订单临时表,同时扣减零售产品库存
     *
     * @param order
     * @return
     */
    Order saveRetailOrderTempWithInventory(Order order, List<Long> toCheckInvProdSkuIdList, Map<Long, Long> toUpdateInvMap);

    /**
     * 存储退款原因并且将订单状态变为6（退款进行中）
     *
     * @param dealRetailOrder
     * @return
     */
    int updateRetailOrderCancelReason(DealRetailOrder dealRetailOrder);

    /**
     * 更新零售业务订单退款失败
     *
     * @param order
     */
    int updateRetailOrderTo8(Order order);

    /**
     * 零售业务：根据订单编号更新订单状态
     *
     * @param order
     * @return
     */
    int updateOrderStatus(Order order);

    /**
     * 零售业务订单接单：更新订单状态为5并记录接单事件
     *
     * @param saveRetailOrderEvent
     * @return
     */
    int acceptRetailOrder(SaveRetailOrderEvent saveRetailOrderEvent);

    /**
     * 零售业务订单发货：更新订单状态为10并记录发货事件
     *
     * @param saveRetailOrderEvent
     * @return
     */
    int sendRetailOrder(SaveRetailOrderEvent saveRetailOrderEvent);

    /**
     * 零售业务订单-确认收货：更新订单状态为9并记录确认收货事件
     *
     * @param saveRetailOrderEvent
     * @return
     */
    int endRetailOrder(SaveRetailOrderEvent saveRetailOrderEvent);

    /**
     * 零售业务订单-确认收货：批量更新订单状态为9并记录确认收货事件
     *
     * @param saveRetailOrderEvents
     * @return
     */
    int endRetailOrderbatch(List<SaveRetailOrderEvent> saveRetailOrderEvents);

    /**
     * 零售业务订单-退换货/售后
     *
     * @param retailOrderService
     * @return
     */
    int retailOrderService(RetailOrderService retailOrderService, Order order);
}