package com.basoft.eorder.interfaces.query.order.retail;

import java.util.List;
import java.util.Map;

/**
 * 购物零售业务-订单查询
 *
 * @author Mentor
 * @version 1.0
 * @created in 20200514
 */
public interface RetailOrderQuery {
    /**
     * 根据订单编号查询订单事件列表
     * @param orderId
     *
     * @return
     */
    List<Map<String, Object>> queryRetailOrderEventList(Long orderId);

    /**
     * 根据订单编号查询订单售后列表
     *
     * @param orderId
     * @return
     */
    List<Map<String, Object>> queryRetailOrderServiceList(Long orderId);
}
