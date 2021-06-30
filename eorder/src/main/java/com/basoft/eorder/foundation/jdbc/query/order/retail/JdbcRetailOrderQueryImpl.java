package com.basoft.eorder.foundation.jdbc.query.order.retail;

import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.order.retail.RetailOrderQuery;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component("RetailOrderQuery")
public class JdbcRetailOrderQueryImpl extends BaseRepository implements RetailOrderQuery {
    /**
     * 根据订单编号查询订单事件列表
     *
     * @param orderId
     * @return
     */
    @Override
    public List<Map<String, Object>> queryRetailOrderEventList(Long orderId) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("orderId", orderId);
        StringBuilder querySQL = new StringBuilder(210);
        querySQL.append("SELECT ORDER_ID, EVENT_TYPE, EVENT_INITIATOR, IS_MAIN, EVENT_TIME, EVENT_NAME, ")
                .append("EVENT_TARGET, EVENT_RESULT, EVENT_RESULT_DESC, SERV_ID ")
                .append("FROM RETAIL_ORDER_EVENT ")
                .append("WHERE ORDER_ID = :orderId ")
                .append("ORDER BY EVENT_TIME ASC");
        return this.getNamedParameterJdbcTemplate().queryForList(querySQL.toString(), param);
    }

    /**
     * 根据订单编号查询订单售后列表
     *
     * @param orderId
     * @return
     */
    public List<Map<String, Object>> queryRetailOrderServiceList(Long orderId) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("orderId", orderId);
        StringBuilder querySQL = new StringBuilder(512);
        querySQL.append("SELECT SERV_ID, SERV_CODE, STORE_ID, ORDER_ID, SERV_TYPE, SERV_STATUS, ")
                .append("APPLY_COUNT, APPLY_AMOUNT, APPLY_DESC, APPLY_IMAGES, ")
                .append("APPLY_DELIVERY_MODE, APPLY_LINKER, APPLY_MOBILE, APPLY_ADDRESS, ")
                .append("APPLY_TIME, ACCEPTOR, ACCEPT_TIME, AUDIT_RESULT, ")
                .append("AUDIT_DESC, AUDIT_REFUND_TYPE, AUDIT_REFUND_AMOUNT, AUDITOR, AUDIT_TIME ")
                .append("FROM RETAIL_ORDER_SERVICE ")
                .append("WHERE ORDER_ID = :orderId ")
                .append("ORDER BY APPLY_TIME ASC");
        return this.getNamedParameterJdbcTemplate().queryForList(querySQL.toString(), param);
    }
}
