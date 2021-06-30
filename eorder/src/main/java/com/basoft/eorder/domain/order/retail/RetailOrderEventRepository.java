package com.basoft.eorder.domain.order.retail;

import com.basoft.eorder.domain.model.retail.order.RetailOrderEvent;

import java.util.List;

/**
 * 零售业务订单事件写服务
 *
 * @author Mentor
 * @version 1.0
 */
public interface RetailOrderEventRepository {
    /**
     * 写入订单事件
     *
     * @param retailOrderEvent
     * @return
     */
    int saveRetailOrderEvent(RetailOrderEvent retailOrderEvent);

    /**
     * 批量写入订单事件
     *
     * @param retailOrderEvents
     * @return
     */
    int batchSaveRetailOrderEvent(List<RetailOrderEvent> retailOrderEvents);

    /**
     * 写入订单事件，并且同步更新订单状态
     *
     * @param retailOrderEvent
     * @return
     */
    String saveRetailOrderEventAndOrderStatus(RetailOrderEvent retailOrderEvent);

    /**
     * 更新订单的订单变更状态由1为0
     *
     * @param retailOrderEvent
     * @return
     */
    int recoverOrderChangeStatus(RetailOrderEvent retailOrderEvent);
}