package com.basoft.eorder.foundation.jdbc.repo.order.retail;

import com.basoft.eorder.domain.model.retail.order.RetailOrderEvent;
import com.basoft.eorder.domain.order.retail.RetailOrderEventRepository;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 零售业务订单事件写服务
 *
 * @author Mentor
 * @version 1.0
 */
@Repository
public class JdbcRetailOrderEventRepoImpl extends BaseRepository implements RetailOrderEventRepository {
    /**
     * 写入订单事件
     *
     * @param retailOrderEvent
     * @return
     */
    @Override
    public int saveRetailOrderEvent(RetailOrderEvent retailOrderEvent) {
        return this.getNamedParameterJdbcTemplate().update("insert into RETAIL_ORDER_EVENT \n" +
                "(\n" +
                "    ORDER_ID\n" +
                "    , EVENT_TYPE\n" +
                "    , EVENT_INITIATOR\n" +
                "    , IS_MAIN\n" +
                "    , EVENT_TIME\n" +
                "    , EVENT_NAME\n" +
                "    , EVENT_TARGET\n" +
                "    , EVENT_RESULT\n" +
                "    , EVENT_RESULT_DESC\n" +
                "    , SERV_ID\n" +
                ") \n" +
                "values \n" +
                "(\n" +
                "    :orderId\n" +
                "    , :eventType\n" +
                "    , :eventInitiator\n" +
                "    , :isMain\n" +
                "    , now()\n" +
                "    , :eventName\n" +
                "    , :eventTarget\n" +
                "    , :eventResult\n" +
                "    , :eventResultDesc\n" +
                "    , :servId\n" +
                ")", new BeanPropertySqlParameterSource(retailOrderEvent));
    }

    @Override
    public int batchSaveRetailOrderEvent(List<RetailOrderEvent> retailOrderEvents) {
        String saveEventSql = "insert into RETAIL_ORDER_EVENT \n" +
                "(\n" +
                "    ORDER_ID\n" +
                "    , EVENT_TYPE\n" +
                "    , EVENT_INITIATOR\n" +
                "    , IS_MAIN\n" +
                "    , EVENT_TIME\n" +
                "    , EVENT_NAME\n" +
                "    , EVENT_TARGET\n" +
                "    , EVENT_RESULT\n" +
                "    , EVENT_RESULT_DESC\n" +
                "    , SERV_ID\n" +
                ") \n" +
                "values \n" +
                "(?, ?, ?, ?,now(),?, ?, ?, ?, ?)";
            this.getJdbcTemplate().batchUpdate(saveEventSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    int num = 1;
                    RetailOrderEvent event = retailOrderEvents.get(i);
                    preparedStatement.setLong(num++, event.getOrderId());
                    preparedStatement.setInt(num++, event.getEventType());
                    preparedStatement.setInt(num++, event.getEventInitiator());
                    preparedStatement.setInt(num++, event.getIsMain());
                    preparedStatement.setString(num++, event.getEventName());
                    preparedStatement.setString(num++, event.getEventTarget());
                    preparedStatement.setInt(num++, event.getEventResult());
                    preparedStatement.setString(num++, event.getEventResultDesc());
                    preparedStatement.setLong(num++, event.getServId());
                }

                @Override
                public int getBatchSize() {
                    return retailOrderEvents.size();
                }
            });

        return 1;
    }

    /**
     * 写入订单事件，并且同步更新订单状态
     *
     * @param retailOrderEvent
     * @return
     */
    @Override
    @Transactional
    public String saveRetailOrderEventAndOrderStatus(RetailOrderEvent retailOrderEvent) {
        // 更新订单状态
        int c = updateOrderStatus(retailOrderEvent);

        // 如果更新到了订单才新增 申请退款3或8 事件
        if(c > 0){
            int d = this.getNamedParameterJdbcTemplate().update("insert into RETAIL_ORDER_EVENT \n" +
                    "(\n" +
                    "    ORDER_ID\n" +
                    "    , EVENT_TYPE\n" +
                    "    , EVENT_INITIATOR\n" +
                    "    , IS_MAIN\n" +
                    "    , EVENT_TIME\n" +
                    "    , EVENT_NAME\n" +
                    "    , EVENT_TARGET\n" +
                    "    , EVENT_RESULT\n" +
                    "    , EVENT_RESULT_DESC\n" +
                    "    , SERV_ID\n" +
                    ") \n" +
                    "values \n" +
                    "(\n" +
                    "    :orderId\n" +
                    "    , :eventType\n" +
                    "    , :eventInitiator\n" +
                    "    , :isMain\n" +
                    "    , now()\n" +
                    "    , :eventName\n" +
                    "    , :eventTarget\n" +
                    "    , :eventResult\n" +
                    "    , :eventResultDesc\n" +
                    "    , :servId\n" +
                    ")", new BeanPropertySqlParameterSource(retailOrderEvent));
            return c + "@" + d;
        }
        return "0@0";
    }

    /**
     * 申请退款更新订单变更状态
     *
     * @param retailOrderEvent
     * @return
     */
    private int updateOrderStatus(RetailOrderEvent retailOrderEvent) {
        // 事件类型
        int eventType = retailOrderEvent.getEventType();
        // 3-申请退款（用户支付成功后，立即进行退款申请）
        if (eventType == 3) {
            // 更新条件是订单状态为4，订单变更状态为0,
            String upSql = "update `order` set CHANGE_STATUS = 1 where id = ? and status = 4 and CHANGE_STATUS = 0";
            return this.getJdbcTemplate().update(upSql, new Object[]{
                    retailOrderEvent.getOrderId()
            });
        }
        // 8-申请退款（商户接单后发货前，用户进行退款申请）
        else if (eventType == 8) {
            // 更新条件是订单状态为5，订单变更状态为0,
            String upSql = "update `order` set CHANGE_STATUS = 1 where id = ? and status = 5 and CHANGE_STATUS = 0";
            return this.getJdbcTemplate().update(upSql, new Object[]{
                    retailOrderEvent.getOrderId()
            });
        }

        return 0;
    }

    /**
     * 更新订单的订单变更状态由1为0
     *
     * @param retailOrderEvent
     * @return
     */
    public int recoverOrderChangeStatus(RetailOrderEvent retailOrderEvent) {
        // 事件类型
        int eventType = retailOrderEvent.getEventType();
        // 4-申请退款（用户支付成功后，立即进行退款申请） 的 审核
        if (eventType == 4) {
            // 更新条件是订单状态为4，订单变更状态为1,
            String upSql = "update `order` set CHANGE_STATUS = 0 where id = ? and status = 4 and CHANGE_STATUS = 1";
            return this.getJdbcTemplate().update(upSql, new Object[]{
                    retailOrderEvent.getOrderId()
            });
        }
        // 9-申请退款（商户接单后发货前，用户进行退款申请） 的 审核
        else if (eventType == 9) {
            // 更新条件是订单状态为5，订单变更状态为1,
            String upSql = "update `order` set CHANGE_STATUS = 0 where id = ? and status = 5 and CHANGE_STATUS = 1";
            return this.getJdbcTemplate().update(upSql, new Object[]{
                    retailOrderEvent.getOrderId()
            });
        }

        return 0;
    }
}
