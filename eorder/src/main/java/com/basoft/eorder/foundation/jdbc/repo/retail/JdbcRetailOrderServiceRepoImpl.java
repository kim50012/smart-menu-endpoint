package com.basoft.eorder.foundation.jdbc.repo.retail;

import com.basoft.eorder.domain.model.retailOrderService.RetailOrderService;
import com.basoft.eorder.domain.retail.RetailOrderServiceRepository;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.command.retail.SaveRetailOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * 零售商户订单售后服务表。一条记录整合所有信息，不拆分存储数据库访问层
 *
 * @author DongXifu
 * @since 2020-05-12 14:40:27
 */
@Repository
public class JdbcRetailOrderServiceRepoImpl extends BaseRepository implements RetailOrderServiceRepository {
    private static final String INSERT_RETAIL_ORDER_SERVICE_SQL = "insert into retail_order_service (SERV_CODE, STORE_ID, ORDER_ID, SERV_TYPE, SERV_STATUS, APPLY_COUNT, APPLY_AMOUNT, APPLY_DESC, APPLY_IMAGES, APPLY_DELIVERY_MODE, APPLY_LINKER, APPLY_MOBILE, APPLY_ADDRESS, APPLY_TIME) values (:servCode, :storeId, :orderId, :servType, :servStatus, :applyCount, :applyAmount, :applyDesc, :applyImages, :applyDeliveryMode, :applyLinker, :applyMobile, :applyAddress, now())";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 零售业务订单退换货/售后
     *
     * @param retailOrderService
     * @return
     */
    @Override
    public int saveRetailOrderService(RetailOrderService retailOrderService) {
        return this.getNamedParameterJdbcTemplate().update(INSERT_RETAIL_ORDER_SERVICE_SQL, new BeanPropertySqlParameterSource(retailOrderService));
    }

    /**
     * 零售业务订单退换货/售后申请的受理
     *
     * @param retailOrderService
     * @return
     */
    @Override
    public int acceptance(RetailOrderService retailOrderService) {
        String acceptanceSql = "update retail_order_service set ACCEPTOR = ?, SERV_STATUS = 2, ACCEPT_TIME = now() where SERV_ID = ? and SERV_CODE = ?";
        return this.jdbcTemplate.update(acceptanceSql,
                new Object[]{
                        retailOrderService.getAcceptor(),
                        retailOrderService.getServId(),
                        retailOrderService.getServCode()
                });
    }

    /**
     * 审核：售业务订单退换货/售后申请的审核
     *
     * @param retailOrderService
     * @return
     */
    @Override
    public int audit(RetailOrderService retailOrderService) {
        String upRetailOrderServiceSql = "update retail_order_service set \n" +
                "SERV_STATUS = ?," +
                "AUDIT_REFUND_TYPE = ?," +
                "AUDIT_REFUND_AMOUNT = ?," +
                "AUDIT_RESULT = ?," +
                "AUDIT_DESC = ?," +
                "AUDITOR = ?," +
                "AUDIT_TIME = now() " +
                "where SERV_ID = ? and SERV_CODE = ?";

        return this.jdbcTemplate.update(upRetailOrderServiceSql,
                new Object[]{
                        retailOrderService.getServStatus(),
                        retailOrderService.getAuditRefundType(),
                        retailOrderService.getAuditRefundAmount(),
                        retailOrderService.getAuditResult(),
                        retailOrderService.getAuditDesc(),
                        retailOrderService.getAuditor(),
                        retailOrderService.getServId(),
                        retailOrderService.getServCode()
                });
    }

    /**
     * 更新售后申请记录的状态
     *
     * @param saveRetailOrderService
     */
    public int updateOrderServiceStatus(SaveRetailOrderService saveRetailOrderService) {
        String updateSql = "update retail_order_service set SERV_STATUS = ? where SERV_ID = ? and SERV_CODE = ?";

        return this.jdbcTemplate.update(updateSql,
                new Object[]{
                        saveRetailOrderService.getServStatus(),
                        saveRetailOrderService.getServId(),
                        saveRetailOrderService.getServCode()
                });
    }
}