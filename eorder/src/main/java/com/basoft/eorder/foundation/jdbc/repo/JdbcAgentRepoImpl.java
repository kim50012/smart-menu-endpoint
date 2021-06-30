package com.basoft.eorder.foundation.jdbc.repo;


import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.batch.job.model.agent.AgentDailySettlement;
import com.basoft.eorder.batch.job.model.agent.AgentSettlement;
import com.basoft.eorder.batch.job.model.agent.AgentSettlementDetail;
import com.basoft.eorder.batch.job.model.agent.AgentSettlementOrder;
import com.basoft.eorder.domain.AgentRepository;
import com.basoft.eorder.domain.model.agent.Agent;
import com.basoft.eorder.domain.model.agent.AgentAimMap;
import com.basoft.eorder.domain.model.agent.QRCodeAgent;
import com.basoft.eorder.util.UidGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class JdbcAgentRepoImpl extends BaseRepository implements AgentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UidGenerator uidGenerator;

    @Override
    public Long insertAgent(Agent agent) {
        String saveAgentSql = "insert  into agent" +
            "(AGT_ID,AGT_TYPE,AGT_NAME,AGT_CODE,AGT_ACCOUNT,CA_CHARGE_RATE,AGT_PASSWORD,AGT_MOBILE" +
            ",AGT_EMAIl,AGT_BANK_CODE,AGT_QRCODE_URL,CONTRACT_ST,CONTRACT_ED" +
            ",STATUS,CREATE_TIME,CREATE_USER,UPDATE_TIME,MODIFIED_USER_ID)" +
            "values" +
            "(?,?,?,?,?,?,?,?" +
            ",?,?,?,?,?" +
            ",1,now(),?,?,?)";

        this.jdbcTemplate.update(saveAgentSql,
            new Object[]{
                agent.getAgtId(),
                agent.getAgtType(),
                agent.getAgtName(),
                agent.getAgtCode(),
                agent.getAgtAccount(),
                agent.getCaChargeRate(),
                agent.getAgtPassword(),
                agent.getAgtMobile(),
                agent.getAgtEmail(),
                agent.getAgtBankCode(),
                agent.getAgtQrcodeUrl(),
                agent.getContractSt(),
                agent.getContractEd(),
                agent.getCreateUser(),
                agent.getUpdateTime(),
                agent.getModifiedUserId()
            });

        return agent.getAgtId();
    }

    @Override
    public Long updateAgent(Agent agent) {
        StringBuilder updateAgentSql = new StringBuilder();
        updateAgentSql.append("update agent set ");
        updateAgentSql.append("AGT_NAME=?,CA_CHARGE_RATE=? ");
        if (StringUtils.isNotBlank(agent.getAgtPassword())) {
            updateAgentSql.append(",AGT_PASSWORD=? ");
        }
        updateAgentSql.append(",AGT_MOBILE=?,AGT_EMAIl=?,AGT_BANK_CODE=? ");
        updateAgentSql.append(",AGT_QRCODE_URL=?,UPDATE_TIME=now(),MODIFIED_USER_ID=? ");
        updateAgentSql.append("where AGT_ID=? ");
        Object args[] = null;
        if (StringUtils.isNotBlank(agent.getAgtPassword())) {
            args = new Object[]{
                agent.getAgtName(), agent.getCaChargeRate(), agent.getAgtPassword(),
                agent.getAgtMobile(), agent.getAgtEmail(), agent.getAgtBankCode(), agent.getAgtQrcodeUrl(),
                agent.getModifiedUserId(), agent.getAgtId()
            };
        } else {
            args = new Object[]{
                agent.getAgtName(), agent.getCaChargeRate(),
                agent.getAgtMobile(), agent.getAgtEmail(), agent.getAgtBankCode(), agent.getAgtQrcodeUrl(),
                agent.getModifiedUserId(), agent.getAgtId()
            };
        }

        this.jdbcTemplate.update(updateAgentSql.toString(), args);

        return agent.getAgtId();

    }

    @Override
    public Long renewAgent(Agent agent) {
        String sql = "update agent set CONTRACT_ED=? , UPDATE_TIME=now() where agt_id=?";
        this.jdbcTemplate.update(sql,
            new Object[]{
                agent.getContractEd(),
                agent.getAgtId()
            });

        return agent.getAgtId();
    }

    @Override
    public Long updateAgentStatus(Agent agent) {
        String upStaSql = "update agent set " +
            "STATUS=? "+
            "where AGT_ID=?";
        this.jdbcTemplate.update(upStaSql,
            new Object[]{
                agent.getStatus(),
                agent.getAgtId()
            });

        return agent.getAgtId();
    }

    @Override
    public int insertAgentStoreMap(AgentAimMap agentAimMap) {
        String saveAgentStoreMapSql = "insert  into agent_store_map" +
            "(ID,AGT_ID,STORE_ID,AGT_TYPE,AGT_CHARGE_RATE,AGT_CHARGE_PERCENT" +
            ",CONTRACT_ST,CONTRACT_ED,STATUS,CREATE_TIME,UPDATE_TIME)"+
            "values" +
            "(?,?,?,?,?,?" +
            ",?,?,1,now(),?)";

        String delSql = "delete from agent_store_map  where store_id=? ";
        this.jdbcTemplate.update(delSql,agentAimMap.getStoreId());

        this.jdbcTemplate.batchUpdate(saveAgentStoreMapSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Long id = uidGenerator.generate(BusinessTypeEnum.AGENT_STORE_MAP);
                AgentAimMap aimMap = agentAimMap.getAimMapList().get(i);
                ps.setLong(1,id);
                ps.setLong(2,aimMap.getAgtId());
                ps.setString(3, aimMap.getStoreId());
                ps.setInt(4,aimMap.getAgtType());
                ps.setString(5, aimMap.getAgtRate());
                ps.setString(6, aimMap.getAgtPercent());
                ps.setDate(7, agentAimMap.getContractSt());
                ps.setDate(8,  agentAimMap.getContractEd());
                ps.setDate(9, agentAimMap.getUpdateTime());
            }
            @Override
            public int getBatchSize() {
                return agentAimMap.getAimMapList().size();
            }
        });
        return agentAimMap.getAimMapList().size();
    }

    @Override
    public int updateAgentStoreMap(AgentAimMap agentAimMap) {
        String upAgtStoreMapSql ="update agent_store_map set "+
            "STATUS=?,CHARGE_RATE=?,UPDATE_TIME=now() "+
            "where ID=:id";

        this.jdbcTemplate.batchUpdate(upAgtStoreMapSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                AgentAimMap aimMap = agentAimMap.getAimMapList().get(i);
                ps.setInt(1,aimMap.getStatus());
                ps.setString(2, aimMap.getAgtRate());
                ps.setLong(3, agentAimMap.getId());
            }
            @Override
            public int getBatchSize() {
                return agentAimMap.getAimMapList().size();
            }
        });

        return agentAimMap.getAimMapList().size();

    }

    @Override
    public Long insertAgentWxMap(AgentAimMap agentAimMap) {
        String saveAgentWxMapSql="insert  into agent_wx_map" +
            "(ID,AGT_ID,OPEN_ID,STATUS,CONTRACT_ST,CREATE_TIME,UPDATE_TIME)"+
            "values" +
            "(?,?,?,1,?,now(),?)";

        this.jdbcTemplate.update(saveAgentWxMapSql,
            new Object[]{
                agentAimMap.getId(),
                agentAimMap.getAgtId(),
                agentAimMap.getOpenId(),
                agentAimMap.getContractSt(),
                agentAimMap.getUpdateTime(),
            });

        return agentAimMap.getId();
    }

    @Override
    public Long updateAgentWxMap(AgentAimMap agentAimMap) {
        String updateAgentWxMapSql ="update agent_wx_map set "+
            "AGT_ID=?,OPEN_ID=?,STATUS=?,CONTRACT_ST=?,UPDATE_TIME=now() "+
            "where ID=:id";

        this.jdbcTemplate.update(updateAgentWxMapSql,
            new Object[]{
                agentAimMap.getId(),
                agentAimMap.getAgtId(),
                agentAimMap.getOpenId(),
                agentAimMap.getStatus(),
                agentAimMap.getContractSt(),
                agentAimMap.getCreateTime(),
                agentAimMap.getUpdateTime(),
                agentAimMap.getId()
            });

        return agentAimMap.getId();
    }

    /**
     * 根据代理商二维码信息更新代理商信息
     *
     * @param agentQRCode
     */
    public void updateQRCodeAgent(QRCodeAgent agentQRCode) {
        String updateQRCodeAgentSql = "update agent set AGT_TICKET=?,AGT_QRCODE_ID=? where AGT_ID= ?";
        this.jdbcTemplate.update(updateQRCodeAgentSql,
                new Object[]{
                        agentQRCode.getQrcodeTicket(),
                        agentQRCode.getAgentId(),
                        agentQRCode.getAgentId()
                });
    }

    /**
     * 新增代理商二维码信息
     *
     * @param agentQRCode
     */
    public void insertQRCodeAgent(QRCodeAgent agentQRCode) {
        /*
        String saveQRCodeAgentSql = "insert into qrcode_agent " +
                "(action_name, expire_seconds, expire_dts, scene_id, scene_str, qrcode_ticket, qrcode_url)" +
                " values (?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(saveQRCodeAgentSql,
                new Object[]{
                        agentQRCode.getAgentId(),
                        agentQRCode.getActionName(),
                        agentQRCode.getExpireSeconds(),
                        agentQRCode.getExpireDts(),
                        agentQRCode.getSceneId(),
                        agentQRCode.getSceneStr(),
                        agentQRCode.getQrcodeTicket(),
                        agentQRCode.getQrcodeUrl()
                });
        */

        StringBuilder sql = new StringBuilder("insert into qrcode_agent ");
        sql.append("(agent_id, action_name, expire_seconds, expire_dts, scene_id, scene_str, qrcode_ticket, qrcode_url) ")
                .append("values (:agentId, :actionName, :expireSeconds, :expireDts, :sceneId, :sceneStr, :qrcodeTicket, :qrcodeUrl) ")
                .append("on duplicate key update ")
                .append(" action_name = :actionName,")
                .append(" expire_seconds = :expireSeconds,")
                .append(" expire_dts = :expireDts,")
                .append(" scene_id = :sceneId,")
                .append(" scene_str = :sceneStr,")
                .append(" qrcode_ticket = :qrcodeTicket,")
                .append(" qrcode_url = :qrcodeUrl");
        this.getNamedParameterJdbcTemplate().update(sql.toString(), new BeanPropertySqlParameterSource(agentQRCode));
    }

    /**********************************************代理商月度结算-start**************************************************/
    /**
     * 新增代理商月度结算信息
     *
     * @param newAgentSettlement
     * @return
     */
    public int insertAgentSettlement(AgentSettlement newAgentSettlement) {
        StringBuilder sql = new StringBuilder("insert into AGENT_SETTLEMENT ");
        sql.append("(SID,AGT_ID,AGT_CODE,SETTLE_YEAR_MONTH,SETTLE_YEAR,SETTLE_MONTH,START_DT,END_DT,ORDER_COUNT,SETTLE_SUM,AGT_FEE,AGT_VAT_FEE,PL_DATE,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER) ")
                .append("values (:sid, :agtId, :agtCode, :settleYearMonth, :settleYear, :settleMonth, :startDT, :endDT, :orderCount, :settleSum, :agtFee, :agtVatFee, DATE_FORMAT(NOW(),'%Y-%m-%d'), now(), 'BA', now(), 'BA')");
        return this.getNamedParameterJdbcTemplate().update(sql.toString(), new BeanPropertySqlParameterSource(newAgentSettlement));
    }

    /**
     * 批量新增代理商结算订单
     *
     * @param settleId
     * @param agentAllOrderList
     * @return
     */
    @Transactional
    public int batchInsertAgentOrder(Long settleId, List<AgentSettlementOrder> agentAllOrderList) {
        // 删除当前结算号对应的无用的订单（定时任务重复执行会产生）
        this.getJdbcTemplate().update("delete from agent_settlement_order where SID = ?",
                new Object[]{settleId});

        // 批量新增
        StringBuilder insertSql = new StringBuilder("insert into agent_settlement_order");
        insertSql
                .append(" (SID, ORDER_ID, ORDER_DATE, STORE_ID , OPEN_ID, ORDER_AMOUNT, CANCEL_DT, IS_REFUND, PL_FEE,")
                .append("  AGT_FEE, VAT_FEE, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER)")
                .append(" values")
                .append(" (:sid, :orderId, :orderDate, :storeId, :openId, :orderAmount, :cancelDt, :isRefund, :plFee, ")
                .append("  :agtFee, :vatFee, now(), 'BA', now(), 'BA')");
        int[] ra = this.getNamedParameterJdbcTemplate().batchUpdate(insertSql.toString(), SqlParameterSourceUtils.createBatch(agentAllOrderList.toArray()));

        // 统计插入总和
        int j = 0;
        for (int i = 0; i < ra.length; i++) {
            if (ra[i] > 0) {
                j += ra[i];
            }
        }
        return j;
    }

    /**
     * 更新代理商结算信息（订单数量、结算金额、代理商结算金额、VAT金额）
     *
     * @param agentSettlement
     * @return
     */
    public int updateAgentSettlement(AgentSettlement agentSettlement) {
        return this.jdbcTemplate.update("update agent_settlement set ORDER_COUNT=?,SETTLE_SUM=?,AGT_FEE=?,AGT_VAT_FEE=? where SID = ?",
                new Object[]{
                        agentSettlement.getOrderCount(),
                        agentSettlement.getSettleSum(),
                        agentSettlement.getAgtFee(),
                        agentSettlement.getAgtVatFee(),
                        agentSettlement.getSid()
                });
    }

    /**
     * 批量新增结算明细
     *
     * @param settleId
     * @param agentSettlementDetailList
     * @return
     */
    public int batchInsertAgentOrderDetail(Long settleId, List<AgentSettlementDetail> agentSettlementDetailList) {
        // 删除当前结算号对应的无用的结算明细（定时任务重复执行会产生）
        this.getJdbcTemplate().update("delete from agent_settlement_detail where SID = ?",
                new Object[]{settleId});

        StringBuilder insertSql = new StringBuilder("insert into agent_settlement_detail");
        insertSql
                .append(" (SID, STORE_ID, SETTLE_TYPE, PL_RATE , AGT_RATE, AGT_PERCENT, ORDER_COUNT,")
                .append("  SETTLE_SUM, AGT_FEE, VAT_FEE, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER)")
                .append(" values")
                .append(" (:sid, :storeId, :settleType, :plRate, :agtRate, :agtPercent, :orderCount, ")
                .append("  :settleSum, :agtFee, :vatFee, now(), 'BA', now(), 'BA')");
        int[] ra = this.getNamedParameterJdbcTemplate().batchUpdate(insertSql.toString(), SqlParameterSourceUtils.createBatch(agentSettlementDetailList.toArray()));

        // 统计插入总和
        int j = 0;
        for (int i = 0; i < ra.length; i++) {
            if (ra[i] > 0) {
                j += ra[i];
            }
        }
        return j;
    }
    /**********************************************代理商月度结算-end**************************************************/

    /**********************************************代理商每日结算-start**************************************************/
    /**
     * 新增代理商每日结算信息
     *
     * @param newAgentDailySettlement
     * @return
     */
    public int insertAgentDailySettlement(AgentDailySettlement newAgentDailySettlement) {
        StringBuilder sql = new StringBuilder("insert into AGENT_SETTLEMENT_DAY ");
        sql.append("(SID,AGT_ID,AGT_CODE,SETTLE_DATE,ORDER_COUNT,SETTLE_SUM,AGT_FEE,AGT_VAT_FEE,PL_DATE,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER) ")
                .append("values (:sid, :agtId, :agtCode, :settleDate, :orderCount, :settleSum, :agtFee, :agtVatFee, DATE_FORMAT(NOW(),'%Y-%m-%d'), now(), 'BA', now(), 'BA')");
        return this.getNamedParameterJdbcTemplate().update(sql.toString(), new BeanPropertySqlParameterSource(newAgentDailySettlement));
    }

    /**
     * 更新代理商每日结算信息（订单数量、结算金额、代理商结算金额、VAT金额）
     *
     * @param agentDailySettlement
     * @return
     */
    public int updateAgentDailySettlement(AgentDailySettlement agentDailySettlement) {
        return this.jdbcTemplate.update("update agent_settlement_day set ORDER_COUNT=?,SETTLE_SUM=?,AGT_FEE=?,AGT_VAT_FEE=? where SID = ?",
                new Object[]{
                        agentDailySettlement.getOrderCount(),
                        agentDailySettlement.getSettleSum(),
                        agentDailySettlement.getAgtFee(),
                        agentDailySettlement.getAgtVatFee(),
                        agentDailySettlement.getSid()
                });
    }
    /**********************************************代理商每日结算-end**************************************************/
}



