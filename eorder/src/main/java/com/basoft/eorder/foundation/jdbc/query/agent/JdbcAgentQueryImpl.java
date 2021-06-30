package com.basoft.eorder.foundation.jdbc.query.agent;

import com.basoft.eorder.batch.job.model.agent.AgentDailySettlement;
import com.basoft.eorder.batch.job.model.agent.AgentSettlement;
import com.basoft.eorder.batch.job.model.agent.AgentSettlementDetail;
import com.basoft.eorder.batch.job.model.agent.AgentStoreMap;
import com.basoft.eorder.domain.model.agent.Agent;
import com.basoft.eorder.domain.model.agent.QRCodeAgent;
import com.basoft.eorder.foundation.jdbc.query.JdbcStoreQueryImpl;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.AgentStoreDTO;
import com.basoft.eorder.interfaces.query.OrderItemDTO;
import com.basoft.eorder.interfaces.query.OrderQuery;
import com.basoft.eorder.interfaces.query.StoreDTO;
import com.basoft.eorder.interfaces.query.UserOrderDTO;
import com.basoft.eorder.interfaces.query.agent.*;
import com.basoft.eorder.interfaces.query.plAgt.PlAgtAmountDTO;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.basoft.eorder.foundation.jdbc.query.JdbcStoreQueryImpl.STORE_FORM;
import static com.basoft.eorder.foundation.jdbc.query.JdbcStoreQueryImpl.STORE_LIST_SELECT;

@Component("AgentQuery")
@Slf4j
public class JdbcAgentQueryImpl extends BaseRepository implements AgentQuery {

    @Autowired
    private OrderQuery orderQuery;

    private final static String AGENT_SELECT = "select " +
            "case\n" +
            "when round((UNIX_TIMESTAMP(CONTRACT_ED) - UNIX_TIMESTAMP(now()))/(60*60*24))>30 \n" +
            "then 1  \n" +
            "when round((UNIX_TIMESTAMP(CONTRACT_ED) - UNIX_TIMESTAMP(now()))/(60*60*24))<=30 \n" +
            "and round((UNIX_TIMESTAMP(CONTRACT_ED) - UNIX_TIMESTAMP(now()))/(60*60*24))>=1 \n" +
            "then 2\n" +
            "else 3 end renewal\n" +
            " ,a.AGT_ID" +
            ",a.AGT_TYPE " +
            ",a.AGT_NAME " +
            ",a.AGT_CODE " +
            ",a.AGT_ACCOUNT " +
            ",a.CA_CHARGE_RATE " +
            ",a.AGT_PASSWORD " +
            ",a.AGT_MOBILE " +
            ",a.AGT_EMAIl " +
            ",a.AGT_BANK_CODE " +
            ",a.AGT_QRCODE_URL " +
            ",a.AGT_TICKET " +
            ",a.AGT_QRCODE_ID " +
            ",a.CONTRACT_ST as contractSt " +
            ",DATE_FORMAT(a.CONTRACT_ED,'%Y-%m-%d') as contractEd " +
            ",a.STATUS " +
            ",a.CREATE_TIME " +
            ",a.CREATE_USER " +
            ",a.UPDATE_TIME " +
            ",a.MODIFIED_USER_ID ";

    private final static String AGENT_FROM = " from agent a  where 1=1 ";

    @Override
    public int getAgentCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder("select count(1) " + AGENT_FROM);
        getAgentCondition(sql, param, false);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<AgentDTO> getAgentListByMap(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder(AGENT_SELECT + AGENT_FROM);
        getAgentCondition(sql, param, false);

        List<AgentDTO> agentDtoList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(AgentDTO.class));
        return agentDtoList;
    }

    @Override
    public AgentDTO getAgentById(Long agtId) {
        Map<String, Object> param = new HashMap<>();
        param.put("agtId", agtId);
        StringBuilder sql = new StringBuilder();
        sql.append(AGENT_SELECT + AGENT_FROM);
        sql.append(" and agt_id=:agtId");
        AgentDTO dto = this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(AgentDTO.class));
        return dto;
    }

    @Override
    public AgentDTO getAgentDto(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(AGENT_SELECT + AGENT_FROM);
        getAgentCondition(sql, param, false);
        AgentDTO dto = this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(AgentDTO.class));
        return dto;
    }

    @Override
    public Agent getAgent(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        // 也正确，spring jdbc templete会自动驼峰规则映射
        /*sql.append("select a.AGT_ID,a.AGT_TYPE,a.AGT_NAME,a.AGT_CODE,a.AGT_ACCOUNT,a.CA_CHARGE_RATE,a.AGT_PASSWORD")
                .append(",a.AGT_MOBILE,a.AGT_EMAIl,a.AGT_BANK_CODE,a.AGT_QRCODE_URL,a.AGT_TICKET,a.AGT_QRCODE_ID")
                .append(",a.CONTRACT_ST,a.CONTRACT_ED,a.STATUS,a.CREATE_TIME,a.CREATE_USER,a.UPDATE_TIME,a.MODIFIED_USER_ID")
                .append(" from agent a where 1 = 1 ");*/
        sql.append("select a.AGT_ID as agtId,a.AGT_TYPE as agtType,a.AGT_NAME as agtName,a.AGT_CODE as agtCode,a.AGT_ACCOUNT as agtAccount,a.CA_CHARGE_RATE as caChargeRate,a.AGT_PASSWORD as agtPassword")
                .append(",a.AGT_MOBILE as agtMobile,a.AGT_EMAIl as agtEmail,a.AGT_BANK_CODE as agtBankCode,a.AGT_QRCODE_URL as agtQrcodeUrl,a.AGT_TICKET as agtTicket,a.AGT_QRCODE_ID as agtQrcodeId")
                .append(",a.CONTRACT_ST as contractSt,a.CONTRACT_ED as contractEd,a.STATUS as status,a.CREATE_TIME as createTime,a.CREATE_USER as createUser,a.UPDATE_TIME as updateTime,a.MODIFIED_USER_ID as modifiedUserId")
                .append(" from agent a where 1 = 1 ");
        getAgentCondition(sql, param, false);

        // 错误的：org.springframework.jdbc.IncorrectResultSetColumnCountException: Incorrect column count: expected 1, actual 20
        // Agent agent = this.queryForObject(sql.toString(), param, Agent.class);

        return this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(Agent.class));
    }

    @Override
    public int getIsBindCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1) from (SELECT \n" +
            "count(1)\n" +
            "from \n" +
            "agent_settlement ase \n" +
            "INNER JOIN agent_settlement_detail asd on ase.sid= asd.sid and agt_id=:agtId\n" +
            "INNER JOIN store s on asd.store_id=s.id \n" +
            "inner join area a on a.area_cd=s.city and a.use_yn='Y'\n" +
            "where 1=1 \n");
        getIsBindCondition(sql, param);
        sql.append(")t");
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<AgentStoreDTO> getIsBindStoreListByMap(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \n" +
            "s.id as storeId,s.name as storeName,s.store_type as storeType,a.area_nm as areaNm\n" +
            ",asd.ORDER_COUNT as qty \n" +
            ",asd.SETTLE_SUM as sumAmount,asd.AGT_FEE as agtFee\n" +
            ",asd.AGT_PERCENT as agtPercent,asd.AGT_RATE as agtRate,asd.PL_RATE as storeChargeRate\n" +
            "from \n" +
                "agent_settlement ase \n" +
                "INNER JOIN agent_settlement_detail asd on ase.sid= asd.sid and agt_id=:agtId\n" +
                "INNER JOIN store s on asd.store_id=s.id \n" +
                "inner join area a on a.area_cd=s.city and a.use_yn='Y'\n" +
                "where 1=1 \n");
        getIsBindCondition(sql, param);
        orderByAndPage(param, sql, " order by s.created desc, s.id desc ");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(AgentStoreDTO.class));
    }

    private StringBuilder getIsBindCondition(StringBuilder sql, Map<String, Object> param) {
        String agtType = Objects.toString(param.get("agtType"), null);
        String storeId = Objects.toString(param.get("storeId"), null);
        String storeName = Objects.toString(param.get("storeName"), null);

        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and s.id = :storeId \n");
        }
        if (StringUtils.isNotBlank(storeName)) {
            sql.append(" and s.name like '%' :storeName '%' ");
        }
        if (StringUtils.isNotBlank(agtType)) {
            sql.append(" and asm.agt_type=:agtType");
        }
        sql.append(" group by s.id");
        return sql;
    }

    @Override
    @Deprecated
    public int getEmptyStoreCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1) ");
        sql.append(STORE_FORM);
        JdbcStoreQueryImpl storeQuery = new JdbcStoreQueryImpl();
        storeQuery.storeQueryCondition(param, sql);
        sql.append(" and s.id not in ");
        sql.append("(select store_id from agent_store_map asm ");
        sql.append("where 1=1 and asm.status=1 ");
        sql.append(" and AGT_TYPE = :agtType");
        sql.append(")");
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    @Deprecated
    public List<StoreDTO> getEmptyStoreListByMap(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(STORE_LIST_SELECT);
        sql.append(STORE_FORM);
        JdbcStoreQueryImpl storeQuery = new JdbcStoreQueryImpl();
        storeQuery.storeQueryCondition(param, sql);
        sql.append("and s.id not in(select store_id from agent_store_map asm ");
        sql.append("where 1=1 and asm.status=1");
        sql.append(")");
        storeQuery.orderByAndPage(param, sql, " order by s.created desc, s.id desc ");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(StoreDTO.class));
    }

    @Override
    public int getAgentStoreCount(Map<String, Object> param) {
        JdbcStoreQueryImpl storeQuery = new JdbcStoreQueryImpl();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1) from (select  max((SELECT AGT_CODE FROM agent WHERE AGT_TYPE=1 and AGT_ID in\n" +
            "(SELECT agt_id from agent_store_map where store_id=s.id ) ))saCode\n" +
            ",group_concat((SELECT AGT_CODE  from agent  where AGT_TYPE=2 and AGT_ID=asm.AGT_ID))caCode");
        getAgentStoreSqlFrom(storeQuery, sql, param);
        sql.append(" )t");
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public int getAgentStoreSettleCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(1) \n" +
            "from agent_settlement ase \n" +
            "INNER JOIN agent agt on ase.agt_id=agt.agt_id where 1=1\n");
        getAgentStoreSettleCondition(sql, param);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<AgentStoreDTO> getAgentStoreSettleList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT agt.agt_id as agtId,agt.agt_type,agt.AGT_NAME,agt.AGT_CODE,agt.AGT_BANK_CODE\n" +
            ",ase.sid, floor(ase.SETTLE_SUM) as sumAmount,ase.ORDER_COUNT as qty,floor(ase.AGT_FEE) as agtFee,floor(AGT_VAT_FEE) as vatFee\n" +
            "from agent_settlement ase \n" +
            "INNER JOIN agent agt on ase.agt_id=agt.agt_id where 1=1\n");

        getAgentStoreSettleCondition(sql, param);
        orderByAndPage(param, sql, "");
        List<AgentStoreDTO> agentStoreList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(AgentStoreDTO.class));
        if(agentStoreList.size()>0)
            getAgentStoreSettle(agentStoreList);
        return agentStoreList;
    }

    private List<AgentStoreDTO> getAgentStoreSettle(List<AgentStoreDTO> dtoList) {
        List<Long> sid = dtoList.stream().map(AgentStoreDTO::getSid).distinct().collect(Collectors.toList());
        Map<String,Object> param = Maps.newHashMap();
        param.put("sid", sid);
        Map<Long,List<AgentAimMapDTO>> groupSettle = getAgentStoreSettleGroup(param);
        dtoList.stream().forEach(a-> a.setAimMapList(groupSettle.get(a.getSid())));

        return dtoList;
    }

    private Map<Long, List<AgentAimMapDTO>> getAgentStoreSettleGroup(Map<String, Object> param) {
        String sql = "SELECT s.name as storeName,asd.sid,asd.STORE_ID,asd.AGT_FEE\n" +
            ",asd.ORDER_COUNT as qty,asd.AGT_PERCENT,asd.AGT_RATE,asd.PL_RATE as storeChargeRate\n" +
            ",asd.SETTLE_SUM as sumAmount\n" +
            "from agent_settlement_detail asd  \n" +
            "INNER JOIN store s on asd.store_id=s.id and s.IS_PAY_SET=1\n" +
            "where asd.sid in (:sid)";

        List<AgentAimMapDTO> settleDetails = this.getNamedParameterJdbcTemplate().query(sql, param, new BeanPropertyRowMapper<>(AgentAimMapDTO.class));
        return settleDetails.stream().collect(Collectors.groupingBy(AgentAimMapDTO::getSid));
    }

    //店铺被代理的信息
    @Override
    public List<AgentStoreDTO> getAgentStoreList(Map<String, Object> param) {
        JdbcStoreQueryImpl storeQuery = new JdbcStoreQueryImpl();
        StringBuilder sql = new StringBuilder();
        sql.append("select\n" +
                "s.id as storeId\n" +
                ",s.store_type as storeType\n" +
                ",s.name as storeName\n" +
                ",s.city as city,a.area_nm as areaNm\n" +
                ",s.`CHARGE_RATE` as storeChargeRate\n" +
                ",s.charge_type as chargeType\n"+
                ",agt.AGT_NAME,agt.AGT_CODE\n" +
                ",max((SELECT AGT_CODE FROM agent WHERE AGT_TYPE=1 and AGT_ID in\n" +
                "(SELECT agt_id from agent_store_map where store_id=s.id ) ))saCode\n" +
                ",group_concat((SELECT AGT_CODE  from agent  where AGT_TYPE=2 and AGT_ID=asm.AGT_ID))caCode\n"+
                ",sum(asm.AGT_CHARGE_RATE) as AGT_CHARGE_RATE\n" +
                ",sum(asm.AGT_CHARGE_PERCENT) as AGT_CHARGE_PERCENT\n" +
                ",max((SELECT concat( AGT_CODE,'(',AGT_NAME,')' )  from agent  where AGT_TYPE=1 and AGT_ID in\n" +
                " (SELECT agt_id from agent_store_map where store_id=s.id and agt_type=1 ) ))saAgtName\n" +
                ",group_concat((SELECT concat( AGT_CODE,'(',AGT_NAME,')' )  from agent  where AGT_TYPE=2 and AGT_ID=asm.AGT_ID))caAgtName\n" +
                ",case when asm.store_id is null then 0 else 1 end isBind\n");
        getAgentStoreSqlFrom(storeQuery, sql, param);
        storeQuery.orderByAndPage(param, sql, " order by asm.CREATE_TIME desc\n");

        List<AgentStoreDTO> agtStoreDtoList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(AgentStoreDTO.class));
        getStoreAgentAims(agtStoreDtoList);
        return agtStoreDtoList;
    }

    private StringBuilder getAgentStoreSqlFrom(JdbcStoreQueryImpl storeQuery, StringBuilder sql, Map<String, Object> param) {
        sql.append(" from store s\n");
        sql.append("join user u on s.manager_id = u.id and s.status !=3 and s.IS_JOIN = 1 \n");
        sql.append("join area a on a.area_cd=s.city and a.use_yn='Y' \n");
        sql.append("left join agent_store_map asm on s.id = asm.store_id and asm.status=1 \n");
        sql.append("left join agent agt on agt.agt_id=asm.agt_id\n");

        sql.append("where 1=1 \n");
        String storeId = Objects.toString(param.get("storeId"), null);
        String storeName = Objects.toString(param.get("storeName"), null);
        String city = Objects.toString(param.get("city"), null);
        String agtCode = Objects.toString(param.get("agtCode"), null);
        String agtName = Objects.toString(param.get("agtName"), null);

        sql.append(" and s.CHARGE_RATE >0");
        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and s.id = :storeId");
        }
        if (StringUtils.isNotBlank(storeName)) {
            sql.append(" and s.name like '%' :storeName '%' ");
        }
        if (StringUtils.isNotBlank(city)) {
            sql.append(" and s.city = :city");
        }

        if (StringUtils.isNotBlank(agtName)) {
            sql.append(" and agt.AGT_NAME like '%' :agtName '%'");
        }
        String isBind = Objects.toString(param.get("isBind"), null);
        if (StringUtils.isNotBlank(isBind)) {
            if ("1".equals(isBind)) {
                sql.append(" and asm.`STORE_ID` is not null");//已被绑定过的
            } else if ("0".equals(isBind)) {
                sql.append(" and asm.`STORE_ID` is  null");//没有被绑定过的
            }
        }

        sql.append(" group by s.id \n");
        //String agtType = Objects.toString(param.get("agtType"), null);
        if (StringUtils.isNotBlank(agtCode)) {
            sql.append("  HAVING saCode like '%' :agtCode '%' or caCode like '%' :agtCode '%' ");
        }

        return sql;
    }


    private List<AgentStoreDTO> getStoreAgentAims(List<AgentStoreDTO> dtoList) {
        List<Long> storeIds = dtoList.stream().map(AgentStoreDTO::getStoreId).distinct().collect(Collectors.toList());
        Map<String,Object> param = Maps.newHashMap();
        param.put("storeIds", storeIds);
        Map<Long,List<AgentAimMapDTO>> groupAim = getStoreAimGroup(param);

        if(groupAim!=null)
            dtoList.forEach(a->a.setAimMapList(groupAim.get(a.getStoreId())));

        return dtoList;
    }

    private Map<Long, List<AgentAimMapDTO>> getStoreAimGroup(Map<String,Object> param) {
        String sql = "select " +
            " agt.AGT_CODE " +
            ",agt.AGT_NAME,agt.AGT_TYPE as agtType\n" +
            ",asm.AGT_CHARGE_RATE as agtRate\n" +
            ",asm.AGT_CHARGE_PERCENT as agtPercent\n" +
            ",asm.store_id\n"+
            "from\n" +
            "agent_store_map asm \n" +
            "left join agent agt on agt.agt_id=asm.agt_id\n" +
            "where 1=1\n" +
            "and asm.store_id in(:storeIds)\n";

        List<AgentAimMapDTO> dtoList = this.getNamedParameterJdbcTemplate().query(sql, param,new BeanPropertyRowMapper<>(AgentAimMapDTO.class));
        return dtoList.stream().collect(Collectors.groupingBy(AgentAimMapDTO::getStoreId));
    }

    @Override
    public int getAgtUserOrderCnt(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(1) from (\n" +
            "select count(1)\n" +
            "from \n" +
            "agent_wx_map awm  \n" +
            "INNER JOIN bawechat.wx_user wu on awm.open_id=wu.OPENID and awm.AGT_ID=:agtId\n" +
            "left JOIN agent_settlement ase on ase.AGT_ID=awm.AGT_ID\n" +
            "left JOIN agent_settlement_order aso on aso.OPEN_ID=wu.OPENID\n");

        sql.append("where 1=1\n");
        getAgtUserOrderCdtion(sql, param, "", false);
        sql.append(")t");

        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<UserOrderDTO> getAgtUserOrderList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \n" +
            "wu.openid,wu.NICK_UNEMOJI,wu.NICKNAME as nickName,wu.HEADIMGURL as headImgUrl\n" +
            ",DATE_FORMAT(wu.SUBSCRIBE_TIME,'%Y-%m-%d %H:%i:%s')as subscribeTime\n" +
            ",DATE_FORMAT(awm.CREATE_TIME,'%Y-%m-%d %H:%i:%s')as bindTime\n" +
            ",(SELECT  count(1) from agent_settlement_order aso where aso.OPEN_ID=wu.OPENID and aso.sid=ase.sid) as qty\n" +
            ",ifnull(sum(aso.ORDER_AMOUNT),0) as sumAmount\n" +
            ",ifnull(sum(aso.AGT_FEE),0) as agtFee\n" +
            ",ifnull(max(aso.ORDER_DATE),'')as laterTrainDate\n" +
            ",ifnull((SELECT name from store where id=(SELECT min(store_id) from agent_settlement_order\n" +
                "where open_id=wu.openid\n" +
                "HAVING \n" +
                "min(ORDER_DATE)=min(aso.ORDER_DATE))),'') as laterStore\n"+
            "from \n" +
            "agent_wx_map awm  \n" +
            "INNER JOIN bawechat.wx_user wu on awm.open_id=wu.OPENID and awm.AGT_ID=:agtId\n" +
            "left JOIN agent_settlement ase on ase.AGT_ID=awm.AGT_ID\n" +
            "left JOIN agent_settlement_order aso on aso.OPEN_ID=wu.OPENID\n");
        sql.append("where 1=1\n");
        getAgtUserOrderCdtion(sql, param, " order by sumAmount desc ", true);
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(UserOrderDTO.class));
    }

    @Override
    public int getSaAgtStoreSettleCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(1) from (SELECT \n" +
            "count(1)\n"+
                "from \n" +
                "agent_store_map asm\n" +
                "INNER JOIN store s on asm.store_id=s.id and asm.agt_id=:agtId\n" +
                "INNER JOIN area a on a.area_cd=s.city\n" +
                "LEFT join agent_settlement ase on ase.agt_id=asm.agt_id\n" +
                "LEFT JOIN agent_settlement_detail asd on asd.store_id=asm.store_id and asd.sid=ase.sid\n" +
            "where 1=1");
        getAgtStoreSettleCondition(sql,param);
        sql.append(" group by s.id ");
        sql.append(" )t");
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(),param,Integer.class);
    }

    @Override
    public List<AgentStoreDTO> getSaAgtStoreSettleList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \n" +
            "   asm.AGT_ID\n" +
            ",s.id as storeId\n" +
            ",s.name as storeName\n" +
            ",s.store_type as storeType\n" +
            ",a.area_nm\n"+
            ",ifnull(s.CHARGE_RATE,0) as storeChargeRate\n" +
            ",ifnull(asm.AGT_CHARGE_PERCENT,0) as agtPercent\n" +
            ",ifnull(sum(asd.ORDER_COUNT),0) as qty\n" +
            ",ifnull(sum(asd.SETTLE_SUM),0) as sumAmount\n" +
            ",ifnull(sum(asd.AGT_FEE),0) as agtFee\n" +
            ",(SELECT DATE_FORMAT(max(order_date),'%Y-%m-%d %H:%i:%s') from agent_settlement_order aso where aso.store_id=s.id)laterTrainDate\n" +
                "from \n" +
                "agent_store_map asm\n" +
                "INNER JOIN store s on asm.store_id=s.id and asm.agt_id=:agtId\n" +
                "INNER JOIN area a on a.area_cd=s.city\n"+
                "LEFT JOIN agent_settlement_day aday on  aday.AGT_ID=asm.AGT_ID\n"+
                "LEFT JOIN agent_settlement_detail asd on asd.store_id=asm.store_id  and asd.sid=aday.sid\n"+
            "where 1=1");
        getAgtStoreSettleCondition(sql,param);
        sql.append(" group by s.id ");
        orderByAndPage(param, sql, " order by sumAmount desc ");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(AgentStoreDTO.class));
    }

    private StringBuilder getAgtStoreSettleCondition(StringBuilder sql, Map<String, Object> param) {
        String storeName = Objects.toString(param.get("storeName"), null);
        String city = Objects.toString(param.get("city"), null);

        if (org.apache.commons.lang3.StringUtils.isNotBlank(storeName)) {
            sql.append(" and s.name like '%' :storeName '%' ");
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(city)) {
            sql.append(" and s.city = :city \n");
        }

        return  sql;

    }

    private StringBuilder getAgtUserOrderCdtion(StringBuilder sql, Map<String, Object> param, String orderBy, boolean isLimit) {
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        String nickName = Objects.toString(param.get("nickName"), null);

        if (StringUtils.isNotBlank(nickName)) {
            sql.append(" and wu.NICK_UNEMOJI like '%' :nickName '%' ");
        }

        sql.append(" group by wu.openid ");
        sql.append(orderBy);

        if (page >= 0 && size > 0 && isLimit) {
            appendPage(page, size, sql, param, isLimit);
        }
        return sql;
    }

    private StringBuilder getAgentCondition(StringBuilder sql, Map<String, Object> param, boolean isLimit) {
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String agtId = Objects.toString(param.get("agtId"), null);
        String agtType = Objects.toString(param.get("agtType"), null);
        String agtCode = Objects.toString(param.get("agtCode"), null);
        String agtName = Objects.toString(param.get("agtName"), null);
        String agtMobile = Objects.toString(param.get("agtMobile"), null);
        String agtStatus = Objects.toString(param.get("agtStatus"), null);

        if (StringUtils.isNotBlank(agtId)) {
            sql.append(" and agt_id = :agtId");
        }

        if (StringUtils.isNotBlank(agtType)) {
            sql.append(" and agt_type = :agtType");
        }
        if (StringUtils.isNotBlank(agtCode)) {
            sql.append(" and agt_code like '%' :agtCode '%'");
        }
        if (StringUtils.isNotBlank(agtName)) {
            sql.append(" and agt_name like '%' :agtName '%'");
        }

        if (StringUtils.isNotBlank(agtMobile)) {
            sql.append(" and agt_mobile = :agtMobile");
        }

        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and CREATE_TIME >= :startTime and CREATE_TIME<=:endTime ");
        }

        if (StringUtils.isNotBlank(agtStatus)) {
            sql.append(" and STATUS=:agtStatus");
        }

        sql.append(" and status != 3");

        appendPage(page, size, sql, param, isLimit);

        log.info("查询代理商信息SQL::::" + sql.toString());
        return sql;
    }

    @Override
    public List<AgentAimMapDTO> getAgtAimDtoList(Map<String, Object> param) {
        String sql = "select " +
                "agt.agt_id as agtId\n"+
                ",agt.AGT_CODE as agtCode ,agt.AGT_NAME,agt.AGT_TYPE as agtType\n" +
                ",asm.AGT_CHARGE_RATE as agtRate\n" +
                ",asm.AGT_CHARGE_PERCENT as agtPercent\n" +
                "from\n" +
                "agent_store_map asm \n" +
                "left join agent agt on agt.agt_id=asm.agt_id\n" +
                "where 1=1\n" +
                "and asm.store_id=:storeId\n" +
                "order by agt.AGT_TYPE asc\n";
        return this.getNamedParameterJdbcTemplate().query(sql, param, new BeanPropertyRowMapper<>(AgentAimMapDTO.class));

    }

    /**
     * 根据代理商ID查询代理商二维码信息
     *
     * @param agentId
     * @return
     */
    public QRCodeAgent getQRCodeByAgentId(String agentId) {
        String sql = "select q.agent_id as agentId,q.action_name as actionName,q.expire_seconds as expireSeconds,q.expire_dts as expireDts,q.scene_id as sceneId,q.scene_str as sceneStr,q.qrcode_ticket as qrcodeTicket,q.qrcode_url as qrcodeUrl from qrcode_agent q where q.agent_id = :agentId";
        Map param = Maps.newHashMap();
        param.put("agentId", agentId);
        return this.queryForObject(sql, param, new BeanPropertyRowMapper<>(QRCodeAgent.class));
    }


    @Override
    public Map<String, Object> getAgtStoreOrderSum(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ifnull(sum(floor(asd.SETTLE_SUM)),0) as sumAmount,ifnull(sum(floor(asd.AGT_FEE)),0) as agtFee\n" +
            ",ifnull(sum(floor(asd.VAT_FEE)),0) as vatFee ,ifnull(floor(sum(asd.ORDER_COUNT)),0) as qty from agent_settlement_detail asd\n" +
            "INNER JOIN agent_settlement ase on ase.sid=asd.sid \n");
        sql.append(" WHERE 1=1 ");

        String agtId = Objects.toString(param.get("agtId"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        logger.info("agtStartTime=" + startTime);
        String storeId = Objects.toString(param.get("storeId"), null);
        if (StringUtils.isNotBlank(agtId)) {
            sql.append(" and AGT_ID=:agtId\n");
        }
        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and store_id=:storeId ");
        }
        if(StringUtils.isNotBlank(startTime)){
            sql.append(" and ase.START_DT >= :startTime\n");
            sql.append(" and ase.START_DT < date_add(:startTime, interval 1 month)\n");
        }
        return  this.getNamedParameterJdbcTemplate().queryForMap(sql.toString(), param);
    }

    @Override
    public Map<String, Object> getAgtWxUserOrderSum(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ifnull(floor(sum(ORDER_AMOUNT)),0)as sumAmount,ifnull(floor(sum(AGT_FEE)),0) as agtFee\n" +
            ",ifnull(floor(sum(VAT_FEE)),0) as vatFee,count(1) as qty\n" +
            "from agent_wx_map awm \n" +
            "INNER JOIN agent_settlement_order aso on awm.OPEN_ID=aso.OPEN_ID and awm.AGT_ID=:agtId\n");
        return  this.getNamedParameterJdbcTemplate().queryForMap(sql.toString(), param);
    }


    @Override
    public Map<String, Object> getCustOrderSum(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT sum(order_amount)sumAmount,count(1)qty\n" +
            ",(SELECT ifnull(sum(AGT_FEE),0) from agent_settlement ase\n" +
            " where  DATE_FORMAT(START_DT,'%Y-%m-%d')>=:startTime and DATE_FORMAT(START_DT,'%Y-%m-%d')< date_add(:startTime, interval 1 month))agtFee\n" +
            ",(SELECT ifnull(sum(agt_vat_fee),0) from agent_settlement ase\n" +
            " where  DATE_FORMAT(START_DT,'%Y-%m-%d')>=:startTime and DATE_FORMAT(START_DT,'%Y-%m-%d')< date_add(:startTime, interval 1 month))vatFee\n"+
            " FROM(\n" +
            "        SELECT  order_amount,ORDER_ID from agent_settlement_order\n" +
            "         where  DATE_FORMAT(order_date,'%Y-%m-%d')>=:startTime and DATE_FORMAT(order_date,'%Y-%m-%d')< date_add(:startTime, interval 1 month)\n" +
            "        union\n" +
            "    SELECT  order_amount,ORDER_ID from agent_settlement_order\n" +
            " where  DATE_FORMAT(order_date,'%Y-%m-%d')>=:startTime and  DATE_FORMAT(order_date,'%Y-%m-%d')< date_add(:startTime, interval 1 month)\n" +
            ")t ");
        return  this.getNamedParameterJdbcTemplate().queryForMap(sql.toString(), param);
    }


    @Override
    public Map<String,Object>  getAgtOrderSum(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ifnull(sum(SETTLE_SUM),0) as sumAmount,ifnull(sum(AGT_FEE),0) as agtFee\n"+
            ",ifnull(sum(AGT_VAT_FEE),0) as vatFee\n" +
            ",ifnull(sum(ORDER_COUNT),0) as qty from agent_settlement ase \n");
        sql.append(" WHERE 1=1 ");

        String agtId = Objects.toString(param.get("agtId"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        if (StringUtils.isNotBlank(agtId)) {
            sql.append(" and AGT_ID=:agtId\n");
        }
        if(StringUtils.isNotBlank(startTime)){
            sql.append(" and ase.START_DT >= :startTime\n");
            sql.append(" and ase.START_DT < date_add(:startTime, interval 1 month)\n");
        }
        return  this.getNamedParameterJdbcTemplate().queryForMap(sql.toString(), param);
    }

    @Override
    public int getAgentOrderCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(1)\n" +
            "from agent_settlement ase\n" +
            "INNER JOIN agent_settlement_order aso on aso.SID=ase.SID and ase.agt_id=:agtId\n" +
            "INNER JOIN `order` o on aso.order_id=o.id and o.STATUS not in (7,8,9)\n"+
            "INNER JOIN bawechat.wx_user wu on aso.OPEN_ID=wu.OPENID\n" +
            "INNER JOIN store s on aso.store_id=s.id\n" +
            "INNER JOIN area a on a.area_cd=s.city\n");
        sql.append("where 1=1 \n");
        getAgentOrderCondition(sql,param);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public int getFinishAgtOrderCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(1)\n" +
            "from agent_settlement ase\n" +
            "INNER JOIN agent_settlement_order aso on aso.SID=ase.SID and ase.agt_id=:agtId\n" +
            "INNER JOIN `order` o on aso.order_id=o.id and o.STATUS = 9\n"+
            "INNER JOIN bawechat.wx_user wu on aso.OPEN_ID=wu.OPENID\n" +
            "INNER JOIN store s on aso.store_id=s.id\n" +
            "INNER JOIN area a on a.area_cd=s.city\n");
        sql.append("where 1=1 \n");
        getAgentOrderCondition(sql,param);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<AgentOrderDTO> getAgentOrderList( Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT aso.ORDER_DATE, aso.ORDER_ID,aso.OPEN_ID" +
            ",wu.openid as openId,wu.NICKNAME as nickName,wu.NICK_UNEMOJI as nickUnemoji,wu.HEADIMGURL as headImgUrl\n" +
            ",s.id as storeId,s.name as storeName,s.store_type ,a.area_nm as areaNm\n" +
            ",aso.ORDER_AMOUNT,floor(aso.AGT_FEE)as agtFee,floor(aso.VAT_FEE) as vatFee,aso.PL_FEE,aso.IS_REFUND\n" +
            ",case when aso.IS_REFUND=0 then DATE_FORMAT(aso.ORDER_DATE,'%Y-%m-%d %H:%i:%s') \n" +
                "when  aso.IS_REFUND=1  then DATE_FORMAT(aso.CANCEL_DT,'%Y-%m-%d %H:%i:%s') \n" +
                "end orderDate\n"+
            ",DATE_FORMAT(aso.CANCEL_DT,'%Y-%m-%d %H:%i:%s')as cancelDt \n" +
            "from agent_settlement ase\n" +
            "INNER JOIN agent_settlement_order aso on aso.SID=ase.SID and ase.agt_id=:agtId\n" +
            "INNER JOIN `order` o on aso.order_id=o.id and o.STATUS not in (7,9)\n"+
            "INNER JOIN bawechat.wx_user wu on aso.OPEN_ID=wu.OPENID\n" +
            "INNER JOIN store s on aso.store_id=s.id\n" +
            "INNER JOIN area a on a.area_cd=s.city\n");
        sql.append("where 1=1 \n");
        getAgentOrderCondition(sql,param);

        orderByAndPage(param, sql, "ORDER BY aso.ORDER_ID asc,aso.ORDER_AMOUNT desc");
        return setAgtOrderResult(sql,param);
    }

    @Override
    public List<AgentOrderDTO> getAgentFinishOrderList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DATE_FORMAT(o.updated,'%Y-%m-%d %H:%i:%s') as completeDate,aso.ORDER_DATE, aso.ORDER_ID,aso.OPEN_ID" +
            ",wu.openid as openId,wu.NICKNAME as nickName,wu.NICK_UNEMOJI as nickUnemoji,wu.HEADIMGURL as headImgUrl\n" +
            ",s.id as storeId,s.name as storeName,s.store_type ,a.area_nm as areaNm\n" +
            ",aso.ORDER_AMOUNT,floor(aso.AGT_FEE)as agtFee,floor(aso.VAT_FEE) as vatFee,aso.PL_FEE,aso.IS_REFUND\n" +
            ",case when aso.IS_REFUND=0 then DATE_FORMAT(aso.ORDER_DATE,'%Y-%m-%d %H:%i:%s') \n" +
            "when  aso.IS_REFUND=1  then DATE_FORMAT(aso.CANCEL_DT,'%Y-%m-%d %H:%i:%s') \n" +
            "end orderDate\n"+
            ",DATE_FORMAT(aso.CANCEL_DT,'%Y-%m-%d %H:%i:%s')as cancelDt \n" +
            "from agent_settlement ase\n" +
            "INNER JOIN agent_settlement_order aso on aso.SID=ase.SID and ase.agt_id=:agtId\n" +
            "INNER JOIN `order` o on aso.order_id=o.id and o.STATUS = 9\n"+
            "INNER JOIN bawechat.wx_user wu on aso.OPEN_ID=wu.OPENID\n" +
            "INNER JOIN store s on aso.store_id=s.id\n" +
            "INNER JOIN area a on a.area_cd=s.city\n");
        sql.append("where 1=1 \n");
        getAgentOrderCondition(sql,param);
        return setAgtOrderResult(sql,param);
    }

    private List<AgentOrderDTO> setAgtOrderResult(StringBuilder sql, Map<String,Object> param) {
        List<AgentOrderDTO> dtoList = this.getNamedParameterJdbcTemplate().query(sql.toString(),param,new BeanPropertyRowMapper<>(AgentOrderDTO.class));
        List<Long> orderIdList = dtoList.stream().map(AgentOrderDTO::getOrderId).collect(Collectors.toList());
        Map<String,Object> itemParam =  Maps.newHashMap();
        itemParam.put("orderIds", orderIdList);
        List<OrderItemDTO> itemList = orderQuery.getOrderItemListByMap(itemParam);//根据orderId数组查出来所有orderItem
        Map<Long, List<OrderItemDTO>> itemGroup = itemList.stream().collect(Collectors.groupingBy(OrderItemDTO::getOrderId));

        List<AgentOrderDTO> resultList = dtoList.stream().map(new Function<AgentOrderDTO, AgentOrderDTO>() {
            public AgentOrderDTO apply(AgentOrderDTO order) {
                Long id = order.getOrderId();
                List<OrderItemDTO> oiList = itemGroup.get(id);
                if (oiList != null && oiList.size() > 0) {
                    order.setItemList(oiList);
                    order.setQty(oiList.stream().mapToInt(OrderItemDTO::getQty).sum());
                }
                return order;
            }
        }).collect(Collectors.toList());
        return resultList;
    }

    @Override
    public int getCaAgentOrderSumCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1) \n");
        getCaAgentOrderFrom(sql);
        getAgentOrderSumCondition(sql, param);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<AgentOrderDTO> getCaAgentOrderSumList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
            " creadt,orderId,created,sumAmount,agtFee,orderAmount,status\n" +
            ",nickName,nickNameUn,headImgUrl,subscribeTime,storeName,storeType\n" +
            ",store_id,OPENID as openId\n");
        getCaAgentOrderFrom(sql);
        getAgentOrderSumCondition(sql, param);
        orderByAndPage(param, sql, " ORDER BY t1.created desc, t1.orderAmount desc ");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(AgentOrderDTO.class));
    }

    private StringBuilder getCaAgentOrderFrom(StringBuilder sql) {
        sql.append("from(\n" +
            "\tselect creadt,created,orderId,orderAmount,status,agtFee,@sum := @sum + agtFee as sumAmount\n" +
            ",nickName,nickNameUn,headImgUrl,storeName,storeType\n" +
            ",store_id,OPENID as openId,subscribeTime\n" +
            "FROM(\n" +
            "\tSELECT o.created as creadt, DATE_FORMAT(o.created,'%Y-%m-%d %H:%i:%s') as created\n" +
            ",wu.NICKNAME as nickName,wu.NICK_UNEMOJI as nickNameUn,wu.HEADIMGURL as headImgUrl\n" +
            ",wu.OPENID,DATE_FORMAT(wu.SUBSCRIBE_TIME,'%Y-%m-%d %H:%i:%s')as subscribeTime,o.store_id\n" +
            ",o.id as orderId,s.CHARGE_TYPE,s.CHARGE_RATE,o.amount as orderAmount,o.status\n" +
            ",s.name as storeName,s.store_type as storeType\n" +
            ",ifnull(case when \n" +
            "(SELECT AGT_CHARGE_RATE from agent_store_map asm\n" +
            "where asm.store_id=o.store_id and asm.agt_id=:agtId) is not null\n" +
            "then floor((SELECT AGT_CHARGE_RATE from agent_store_map asm\n" +
            "where asm.store_id=o.store_id and asm.agt_id=:agtId)*o.amount)\n" +
            "else floor(0.33*s.CHARGE_RATE/100*o.amount)\n" +
            "end,0) agtFee\n" +
            "FROM `order` o \n" +
            "INNER JOIN order_pay op on o.id=op.ORDER_ID and o.status in (4,5,6,7,8,9,10,11)\n"+
            "INNER JOIN bawechat.wx_user wu on o.open_id=wu.OPENID \n" +
            "INNER JOIN agent_wx_map awm on awm.open_id=wu.openid and awm.AGT_ID=:agtId\n" +
            "INNER JOIN store s on s.id=o.store_id\n" +
            "WHERE 1=1\n" +
            "and DATE_FORMAT (o.created,'%Y-%m-%d') >=:startTime and DATE_FORMAT (o.created,'%Y-%m-%d')<=:endTime\n" +
            "union all \n" +
            " SELECT o.created as creadt, DATE_FORMAT(opc.createdt,'%Y-%m-%d %H:%i:%s') as created\n" +
            ",wu.NICKNAME as nickName,wu.NICK_UNEMOJI as nickNameUn,wu.HEADIMGURL as headImgUrl\n" +
            ",wu.OPENID,DATE_FORMAT(wu.SUBSCRIBE_TIME,'%Y-%m-%d %H:%i:%s')as subscribeTime,o.store_id\n" +
            ",o.id as orderId,s.CHARGE_TYPE,s.CHARGE_RATE,-o.amount as orderAmount,o.status\n" +
            ",s.name as storeName,s.store_type\n" +
            ",ifnull(case when \n" +
            "(SELECT AGT_CHARGE_RATE from agent_store_map asm\n" +
            "where asm.store_id=o.store_id and asm.agt_id=:agtId) is not null\n" +
            "then -floor((SELECT AGT_CHARGE_RATE from agent_store_map asm\n" +
            "where asm.store_id=o.store_id and asm.agt_id=:agtId)*o.amount)\n" +
            "else -floor(0.33*s.CHARGE_RATE/100*o.amount)\n" +
            "end,0) agtFee\n" +
            "FROM `order` o \n" +
            "INNER JOIN order_pay_cancel opc on o.id=opc.order_id and o.status =7\n" +
            "INNER JOIN bawechat.wx_user wu on o.open_id=wu.OPENID\n" +
            "INNER JOIN agent_wx_map awm on awm.open_id=wu.openid and awm.AGT_ID=:agtId\n" +
            "INNER JOIN store s on s.id=o.store_id\n" +
            "WHERE 1=1\n" +
            "and DATE_FORMAT (opc.createdt,'%Y-%m-%d') >=:startTime and DATE_FORMAT (opc.createdt,'%Y-%m-%d')<=:endTime\n" +
            ")t, (select @sum := 0) s where 1=1\n" +
            "ORDER BY t.created asc,t.orderAmount desc \n" +
            ")t1 where 1=1\n"
        );
        return sql;
    }

    @Override
    public int getSaAgentOrderSumCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT count(1)\n");
        getSaAgentOrderFrom(sql);
        getAgentOrderSumCondition(sql, param);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<AgentOrderDTO> getSaAgentOrderSumList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT orderId,created,orderAmount,agtFee,status \n");
        sql.append(",storeId,storeName,storeType");
        sql.append(",sumAmount\n");
        getSaAgentOrderFrom(sql);
        getAgentOrderSumCondition(sql, param);
        orderByAndPage(param, sql, " order by created desc,sumAmount desc");

        return this.getNamedParameterJdbcTemplate().query(sql.toString(),param,new BeanPropertyRowMapper<>(AgentOrderDTO.class));
    }

    private StringBuilder getSaAgentOrderFrom(StringBuilder sql) {
        sql.append(" from(\n" +
                "\tselect creadt,created,orderId,orderAmount,status,agtFee,@sum := @sum + agtFee as sumAmount\n" +
                ",storeName,storeType,storeId\n" +
                "FROM(\n" +
                "\tSELECT o.created as creadt, DATE_FORMAT(o.created,'%Y-%m-%d %H:%i:%s') as created\n" +
                ",o.store_id as storeId\n" +
                ",o.id as orderId,s.CHARGE_TYPE,s.CHARGE_RATE,o.amount as orderAmount,o.status\n" +
                ",s.name as storeName,s.store_type as storeType\n" +
                ",ifnull(case when \n" +
                "(SELECT AGT_CHARGE_RATE from agent_store_map asm\n" +
                "where asm.store_id=o.store_id and asm.agt_id=:agtId) is not null\n" +
                "then floor((SELECT AGT_CHARGE_RATE from agent_store_map asm\n" +
                "where asm.store_id=o.store_id and asm.agt_id=:agtId)*o.amount)\n" +
                "else floor(0.33*s.CHARGE_RATE/100*o.amount)\n" +
                "end,0) agtFee\n" +
                "FROM `order` o \n" +
                "INNER JOIN order_pay op on o.id=op.ORDER_ID and o.status in (4,5,6,7,8,9,10,11)\n"+
                "INNER JOIN agent_store_map asm on asm.store_id=o.store_id and asm.AGT_ID=:agtId\n" +
                "INNER JOIN store s on s.id=o.store_id\n" +
                "WHERE DATE_FORMAT (o.created,'%Y-%m-%d') >=:startTime and DATE_FORMAT (o.created,'%Y-%m-%d')<=:endTime\n" +
                "union all \n" +
                " SELECT o.created as creadt, DATE_FORMAT(opc.createdt,'%Y-%m-%d %H:%i:%s') as created\n" +
                ",o.store_id as storeId\n" +
                ",o.id as orderId,s.CHARGE_TYPE,s.CHARGE_RATE,-o.amount as orderAmount,o.status\n" +
                ",s.name as storeName,s.store_type\n" +
                ",ifnull(case when \n" +
                "(SELECT AGT_CHARGE_RATE from agent_store_map asm\n" +
                "where asm.store_id=o.store_id and asm.agt_id=:agtId) is not null\n" +
                "then -floor((SELECT AGT_CHARGE_RATE from agent_store_map asm\n" +
                "where asm.store_id=o.store_id and asm.agt_id=:agtId)*o.amount)\n" +
                "else -floor(0.33*s.CHARGE_RATE/100*o.amount)\n" +
                "end,0) agtFee\n" +
                "FROM `order` o \n" +
                "INNER JOIN order_pay_cancel opc on o.id=opc.order_id and o.status =7\n" +
                "INNER JOIN agent_store_map asm on asm.store_id=o.store_id and asm.AGT_ID=:agtId\n" +
                "INNER JOIN store s on s.id=o.store_id\n" +
                "WHERE DATE_FORMAT (opc.createdt,'%Y-%m-%d') >=:startTime and DATE_FORMAT (opc.createdt,'%Y-%m-%d')<=:endTime\n" +
                ")t, (select @sum := 0) s where 1=1\n" +
                "ORDER BY t.created asc,t.orderAmount desc \n" +
                ")t1  where 1=1\n"
            );
        return sql;
    }

    private StringBuilder getAgentOrderCondition(StringBuilder sql, Map<String, Object> param) {
        String storeId = Objects.toString(param.get("storeId"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        String storeName = Objects.toString(param.get("storeName"), null);
        String city = Objects.toString(param.get("city"), null);
        String nickName = Objects.toString(param.get("nickName"), null);
        String openId = Objects.toString(param.get("openId"), null);

        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and s.id = :storeId \n");
        }
        if (StringUtils.isNotBlank(storeName)) {
            sql.append(" and s.name like  '%' :storeName '%' ");
        }
        if (StringUtils.isNotBlank(city)) {
            sql.append(" and s.city = :city \n");
        }
        if(StringUtils.isNotBlank(nickName)){
            sql.append(" and NICK_UNEMOJI like '%' :nickName '%' ");
        }
        if (StringUtils.isNotBlank(openId)) {
            sql.append(" and wu.openid = :openId ");
        }
        if(StringUtils.isNotBlank(startTime)){
            sql.append(" and ase.START_DT >= :startTime\n");
            sql.append(" and ase.START_DT < date_add(:startTime, interval 1 month)\n");
        }
        return sql;

    }

    private StringBuilder getAgentStoreSettleCondition(StringBuilder sql, Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String agtId = Objects.toString(param.get("agtId"), null);
        String agtType = Objects.toString(param.get("agtType"), null);
        String agtCode = Objects.toString(param.get("agtCode"), null);
        String agtName = Objects.toString(param.get("agtName"), null);
        if (StringUtils.isNotBlank(agtId)) {
            sql.append(" and agt.agt_id = :agtId");
        }
        if (StringUtils.isNotBlank(agtType)) {
            sql.append(" and agt.agt_type = :agtType");
        }
        if (StringUtils.isNotBlank(agtCode)) {
            sql.append(" and agt.agt_code like '%' :agtCode '%'");
        }
        if (StringUtils.isNotBlank(agtName)) {
            sql.append(" and agt.agt_name like '%' :agtName '%'");
        }
        if(StringUtils.isNotBlank(startTime)){
            sql.append(" and ase.START_DT >= :startTime\n");
            sql.append(" and ase.START_DT < date_add(:startTime, interval 1 month)\n");
        }
        return sql;
    }


    private StringBuilder getAgentOrderSumCondition(StringBuilder sql, Map<String, Object> param) {
        String orderId = Objects.toString(param.get("orderId"), null);
        String storeName = Objects.toString(param.get("storeName"), null);
        String city = Objects.toString(param.get("city"), null);
        String nickName = Objects.toString(param.get("nickName"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);

        if (StringUtils.isNotBlank(orderId)) {
            sql.append(" and orderId like '%' :orderId '%' ");
        }
        if (StringUtils.isNotBlank(storeName)) {
            sql.append(" and storeName like  '%' :storeName '%' ");
        }
        if (StringUtils.isNotBlank(city)) {
            sql.append(" and city = :city \n");
        }
        if(StringUtils.isNotBlank(nickName)){
            sql.append(" and nickNameUn like '%' :nickName '%' ");
        }
       /* if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            sql.append(" and DATE_FORMAT (created,'%Y-%m-%d') >= :startTime\n");
            sql.append(" and DATE_FORMAT (created,'%Y-%m-%d') <= :endTime\n");
        }*/
        return sql;
    }

    @Override
    public AgtAmountQtyDTO getAgtYearStatics(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT 'thisYear' as date,sum(AGT_FEE) as sumAmount,sum(ORDER_COUNT) as qty from agent_settlement ase \n" +
            "WHERE YEAR(START_DT) = YEAR(NOW( )) and agt_id=:agtId");
        return this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(AgtAmountQtyDTO.class));
    }

    /******************************app统计************************************/


    @Override
    public List<AgtAmountQtyDTO> getAgentOrderStatics(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DATE_FORMAT(START_DT,'%Y-%m') as date,ORDER_COUNT as qty" +
            ",SETTLE_SUM ,floor(AGT_FEE) as agtSumAmount from agent_settlement ase \n");
        sql.append(" where 1=1\n");
        sql.append(" and agt_id=:agtId");
        sql.append(" and ase.START_DT >= DATE_FORMAT(now(),'%Y')\n" +
                   " and ase.START_DT <= DATE_FORMAT(now(),'%Y-%m')");

        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(AgtAmountQtyDTO.class));
    }

    /**********************************************代理商月度结算-start**************************************************/
    /**
     * 查询代理商代理商户信息列表
     *
     * @param agent
     * @return
     */
    public List<AgentStoreMap> getAgentStoreInfo(Agent agent) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.ID,a.AGT_ID,a.STORE_ID,IFNULL(a.AGT_TYPE,0) as agtType,IFNULL(a.AGT_CHARGE_TYPE,0) as agtChargeType,IFNULL(a.AGT_CHARGE_FEE,0) as agtChargeFee,IFNULL(a.AGT_CHARGE_RATE,0) as agtChargeRate")
                .append(",IFNULL(a.AGT_CHARGE_PERCENT,0) as agtChargePercent,a.CONTRACT_ST,a.CONTRACT_ED,a.STATUS")
                .append(",a.CREATE_TIME,a.UPDATE_TIME")
                .append(" from agent_store_map a where a.STATUS = 1")
                .append(" and AGT_ID = :agtId");
        Map param = Maps.newHashMap();
        param.put("agtId", agent.getAgtId());
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(AgentStoreMap.class));
    }

    /**
     * 根据代理商类型查询代理商列表
     *
     * @param agentType
     * @return
     */
    public List<Agent> getUsingAgentList(String agentType) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.AGT_ID,a.AGT_TYPE,a.AGT_NAME,a.AGT_CODE,a.AGT_ACCOUNT,a.CA_CHARGE_RATE,a.AGT_PASSWORD")
                .append(",a.AGT_MOBILE,a.AGT_EMAIl,a.AGT_BANK_CODE,a.AGT_QRCODE_URL,a.AGT_TICKET,a.AGT_QRCODE_ID")
                .append(",a.CONTRACT_ST,a.CONTRACT_ED,a.STATUS,a.CREATE_TIME,a.CREATE_USER,a.UPDATE_TIME,a.MODIFIED_USER_ID")
                .append(" from agent a where 1 = 1");
        sql.append(" and a.STATUS = 1");
        if (StringUtils.isNotBlank(agentType)) {
            sql.append(" and AGT_TYPE = :agentType");
        }
        Map param = Maps.newHashMap();
        param.put("agentType", agentType);
        List<Agent> agentList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(Agent.class));
        return agentList;
    }

    /**
     * 根据 代理商ID 和 结算年月 查询结算信息
     *
     * @param agent
     * @param yearMonth
     * @return
     */
    public AgentSettlement queryAgentSettlement(Agent agent, String yearMonth) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SID,AGT_ID,AGT_CODE,SETTLE_YEAR_MONTH,SETTLE_YEAR,SETTLE_MONTH,DATE_FORMAT(START_DT,'%Y-%m-%d') as startDt,DATE_FORMAT(END_DT,'%Y-%m-%d') as endDt,ORDER_COUNT,SETTLE_SUM,AGT_FEE,AGT_VAT_FEE,PL_DATE,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER ")
                .append(" FROM AGENT_SETTLEMENT where 1 = 1 ")
                .append(" AND AGT_ID = :agentId")
                .append(" AND SETTLE_YEAR_MONTH = :settleYearMonth");
        Map param = Maps.newHashMap();
        param.put("agentId", agent.getAgtId());
        param.put("settleYearMonth", yearMonth);
        return this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(AgentSettlement.class));
    }

    /**
     * 查询代理商每日结算信息
     *
     * @param agent
     * @param yesterday
     * @return
     */
    public AgentDailySettlement queryAgentDailySettlement(Agent agent, String yesterday) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SID,AGT_ID,AGT_CODE,SETTLE_DATE,ORDER_COUNT,SETTLE_SUM,AGT_FEE,AGT_VAT_FEE,PL_DATE,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER ")
                .append(" FROM AGENT_SETTLEMENT_DAY WHERE ")
                .append(" AGT_ID = :agentId")
                .append(" AND DATE_FORMAT(SETTLE_DATE,'%Y-%m-%d') = :yesterday");
        Map param = Maps.newHashMap();
        param.put("agentId", agent.getAgtId());
        param.put("yesterday", yesterday);
        return this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(AgentDailySettlement.class));
    }

    /**
     * 查询代理商结算月的结算金额、代理商结算金额和VAT金额
     *
     * @return
     * @Long sid
     */
    public Map<String, Object> getAgentSettleInfo(Long sid) {
        // 查询参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("sid", sid);

        // 查询代理商结算月的结算金额、代理商结算金额和VAT金额
        String sql = "SELECT SUM(a.ORDER_AMOUNT) AS orderTotalAmount, SUM(a.AGT_FEE) AS agtTotalFee, SUM(a.AGT_FEE)*0.9 as vatTotalFee FROM agent_settlement_order a WHERE a.SID = :sid";

        return this.getNamedParameterJdbcTemplate().queryForMap(sql, param);
    }

    /**
     * 查询统计代理商的商户月度结算信息
     *
     * @param settleId
     * @param lastYear
     * @param lastMonth
     * @param yearMonthStringNoLine
     * @return
     */
    public List<AgentSettlementDetail> getAgentSettlementDetailList(Long settleId, int lastYear, int lastMonth, String yearMonthStringNoLine) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(":sid as sid,1 as settleType,IFNULL(s.CHARGE_RATE,0) AS plRate,storeAgent.storeId,storeAgent.orderCount")
                .append(",storeAgent.settleSum,storeAgent.agtFee,storeAgent.vatFee")
                .append(" from store_charge_info_record s,")
                .append(" (SELECT IFNULL(a.STORE_ID,0) AS storeId, COUNT(*) AS orderCount, IFNULL(SUM(a.ORDER_AMOUNT),0) AS settleSum, IFNULL(SUM(a.AGT_FEE),0) AS agtFee,IFNULL(SUM(a.AGT_FEE),0)*0.9 as vatFee FROM agent_settlement_order a WHERE a.SID = :sid GROUP BY a.STORE_ID) storeAgent")
                .append(" WHERE s.STORE_ID = storeAgent.storeId")
                .append(" AND s.USE_YN IN (2,3)")
                .append(" AND s.FINAL_YN=1")
                .append(" AND s.CHARGE_YEAR_MONTH = :yearMonthStringNoLine");
        Map param = Maps.newHashMap();
        param.put("sid", settleId);
        param.put("yearMonthStringNoLine", yearMonthStringNoLine);
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementDetail.class));
    }

    /**
     * 查询统计代理商的商户每日结算信息
     *
     * @param settleId
     * @return
     */
    public List<AgentSettlementDetail> getAgentDailySettlementDetailList(Long settleId) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(":sid as sid,1 as settleType,IFNULL(s.CHARGE_RATE,0) AS plRate,storeAgent.storeId,storeAgent.orderCount")
                .append(",storeAgent.settleSum,storeAgent.agtFee,storeAgent.vatFee")
                .append(" from store s,")
                .append(" (SELECT IFNULL(a.STORE_ID,0) AS storeId, COUNT(*) AS orderCount, IFNULL(SUM(a.ORDER_AMOUNT),0) AS settleSum, IFNULL(SUM(a.AGT_FEE),0) AS agtFee,IFNULL(SUM(a.AGT_FEE),0)*0.9 as vatFee FROM agent_settlement_order a WHERE a.SID = :sid GROUP BY a.STORE_ID) storeAgent")
                .append(" WHERE s.id = storeAgent.storeId");
        Map param = Maps.newHashMap();
        param.put("sid", settleId);
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(AgentSettlementDetail.class));
    }
    /**********************************************代理商月度结算-end**************************************************/


    /**
     * 根据代理商编码查询代理商信息
     *
     * @param agtCode
     * @return
     */
    public Agent getAgentByCode(String agtCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.AGT_ID,a.AGT_TYPE,a.AGT_NAME,a.AGT_CODE,a.AGT_ACCOUNT,a.CA_CHARGE_RATE,a.AGT_PASSWORD")
                .append(",a.AGT_MOBILE,a.AGT_EMAIl,a.AGT_BANK_CODE,a.AGT_QRCODE_URL,a.AGT_TICKET,a.AGT_QRCODE_ID")
                .append(",a.CONTRACT_ST,a.CONTRACT_ED,a.STATUS,a.CREATE_TIME,a.CREATE_USER,a.UPDATE_TIME,a.MODIFIED_USER_ID")
                .append(" from agent a where 1 = 1");
        sql.append(" and a.STATUS != 3 and a.AGT_CODE = :agtCode");
        Map param = Maps.newHashMap();
        param.put("agtCode", agtCode);
        Agent agent = this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(Agent.class));
        return agent;
    }

    /**********************************************平台利润代理商利润统计-start**************************************************/

    /**
     * 获取今年 平台、代理商总收入 平台净利润
     *
     * @Param param
     * @return List<PlAgtAmountDTO>
     * @author Dong Xifu
     * @date 2019/10/28 1:08 下午
     */
    public  List<PlAgtAmountDTO> getPlAgtAmountStatsSum(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dateType,plFee,saFee,caFee,(plFee-saFee-caFee)as profit,qty from (SELECT 'yestoday' as dateType,ifnull(sum(PL_FINAL_FEE),0) plFee\n" +
            " ,ifnull((SELECT ifnull(sum(asdl.AGT_FEE),0) from agent_settlement_day asdl INNER JOIN agent a on asdl.AGT_ID=a.AGT_ID and a.AGT_TYPE=1 and asdl.SETTLE_DATE=DATE_FORMAT(CURDATE()-1,'%Y-%m-%d') ),0)saFee\n" +
            " ,ifnull((SELECT ifnull(sum(asdl.AGT_FEE),0) from agent_settlement_day asdl INNER JOIN agent a on asdl.AGT_ID=a.AGT_ID and a.AGT_TYPE=2 and asdl.SETTLE_DATE=DATE_FORMAT(CURDATE()-1,'%Y-%m-%d') ),0)caFee\n" +
            " ,ifnull(sum(order_count),0)qty\n"+
            " FROM store_settlement_day sed \n" +
            " WHERE sed.SETTLE_TYPE !=3 and  DATE_FORMAT(sed.SETTLE_DATE,'%Y-%m-%d') =DATE_FORMAT(CURDATE()-1,'%Y-%m-%d'))t\n" +
            " union all\n" +
            "  SELECT dateType,plFee,saFee,caFee,(plFee-saFee-caFee)as profit,qty from (SELECT 'lastMonth' as dateType,ifnull(sum(PL_FINAL_FEE),0) plFee\n" +
            " ,ifnull((SELECT ifnull(sum(asdl.AGT_FEE),0) from agent_settlement_day asdl INNER JOIN agent a on asdl.AGT_ID=a.AGT_ID and a.AGT_TYPE=1 and DATE_FORMAT(asdl.SETTLE_DATE,'%Y%m')=DATE_FORMAT(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y%m') ),0)saFee\n" +
            " ,ifnull((SELECT ifnull(sum(asdl.AGT_FEE),0) from agent_settlement_day asdl INNER JOIN agent a on asdl.AGT_ID=a.AGT_ID and a.AGT_TYPE=2 and DATE_FORMAT(asdl.SETTLE_DATE,'%Y%m')=DATE_FORMAT(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y%m') ),0)caFee\n" +
            " ,ifnull(sum(order_count),0)qty\n"+
            " FROM store_settlement_day sed \n" +
            " WHERE sed.SETTLE_TYPE !=3 and date_format(sed.SETTLE_DATE,'%Y-%m') = date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y-%m'))t\n" +
            " union all\n" +
            " SELECT dateType,plFee,saFee,caFee,(plFee-saFee-caFee)as profit,qty from (SELECT 'month' as dateType,ifnull(sum(PL_FINAL_FEE),0) plFee\n" +
            " ,ifnull((SELECT ifnull(sum(asdl.AGT_FEE),0) from agent_settlement_day asdl INNER JOIN agent a on asdl.AGT_ID=a.AGT_ID and a.AGT_TYPE=1 and DATE_FORMAT(asdl.SETTLE_DATE,'%Y%m')=DATE_FORMAT(CURDATE(),'%Y%m' ) ),0)saFee\n" +
            " ,ifnull((SELECT ifnull(sum(asdl.AGT_FEE),0) from agent_settlement_day asdl INNER JOIN agent a on asdl.AGT_ID=a.AGT_ID and a.AGT_TYPE=2 and DATE_FORMAT(asdl.SETTLE_DATE,'%Y%m')=DATE_FORMAT(CURDATE(),'%Y%m' ) ),0)caFee\n" +
            " ,ifnull(sum(order_count),0)qty\n"+
            " FROM store_settlement_day sed \n" +
            " WHERE sed.SETTLE_TYPE !=3 and DATE_FORMAT(sed.SETTLE_DATE,'%Y%m') = DATE_FORMAT(CURDATE(),'%Y%m' ))t\n" +
            " union all\n" +
            "  SELECT dateType,plFee,saFee,caFee,(plFee-saFee-caFee)as profit,qty from (SELECT 'year' as dateType,ifnull(sum(PL_FINAL_FEE),0) plFee\n" +
            " ,ifnull((SELECT ifnull(sum(asdl.AGT_FEE),0) from agent_settlement_day asdl INNER JOIN agent a on asdl.AGT_ID=a.AGT_ID and a.AGT_TYPE=1 and YEAR(asdl.SETTLE_DATE)=YEAR(NOW()) ),0)saFee\n" +
            " ,ifnull((SELECT ifnull(sum(asdl.AGT_FEE),0) from agent_settlement_day asdl INNER JOIN agent a on asdl.AGT_ID=a.AGT_ID and a.AGT_TYPE=2 and YEAR(asdl.SETTLE_DATE)=YEAR(NOW()) ),0)caFee\n" +
            " ,ifnull(sum(order_count),0)qty\n"+
            " FROM store_settlement_day sed \n" +
            " WHERE sed.SETTLE_TYPE !=3 and YEAR(sed.SETTLE_DATE)=YEAR(NOW()))t");

        return this.getNamedParameterJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<>(PlAgtAmountDTO.class));
    }

    @Override
    public List<PlAgtAmountDTO> getPlAmountStatsByDate(Map<String, Object> param) {
        String dateType = Objects.toString(param.get("dateType"), null);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT date,plFee,saFee,caFee,(plFee-saFee-caFee)as profit from(\n");
            sql.append("SELECT "+dateSql("sed",dateType)+" as date\n");
            sql.append(",ifnull(sum(PL_FINAL_FEE),0) plFee\n" +
                ",ifnull((SELECT ifnull(sum(asdl.AGT_FEE),0) from agent_settlement_day asdl INNER JOIN agent a on asdl.AGT_ID=a.AGT_ID\n ");
            sql.append(" WHERE "+dateSql("asdl",dateType)+"=date and a.AGT_TYPE=1),0)saFee\n");
            sql.append(",ifnull((SELECT ifnull(sum(asdl.AGT_FEE),0) from agent_settlement_day asdl INNER JOIN agent a on asdl.AGT_ID=a.AGT_ID " +
                " WHERE "+dateSql("asdl",dateType)+"=date and a.AGT_TYPE=2),0)caFee\n" +
                " FROM store_settlement_day sed \n" +
                " WHERE sed.SETTLE_TYPE !=3 and DATE_FORMAT(sed.SETTLE_DATE,'%Y-%m-%d') >= :startTime\n" +
                " and DATE_FORMAT(sed.SETTLE_DATE,'%Y-%m-%d') <= :endTime\n" +
                " GROUP BY date)t");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(PlAgtAmountDTO.class));
    }

    private StringBuilder dateSql(String table,String dateType) {
        StringBuilder dateSql = new StringBuilder();
        if (dateType.equals("1")) {
            return dateSql.append("DATE_FORMAT("+table+".SETTLE_DATE,'%Y-%m-%d')");
        } else if (dateType.equals("2")) {
            return dateSql.append("DATE_FORMAT("+table+".SETTLE_DATE,'%Y-%u')");
        }else {
            return dateSql.append("DATE_FORMAT("+table+".SETTLE_DATE,'%Y-%m')");
        }

    }

    @Override
    public List<AgentOrderDTO> agtFeeRank(Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT agt.AGT_NAME,agt.AGT_CODE,ifnull(floor(sum(SETTLE_SUM)),0)sumAmount,ifnull(sum(order_count),0)qty\n" +
            ",ifnull(floor(sum(AGT_FEE)),0)agtFee FROM agent_settlement_day asd \n" +
            "INNER JOIN agent agt on agt.AGT_ID=asd.AGT_ID and agt.AGT_TYPE=:agtType\n" +
            "where DATE_FORMAT(asd.SETTLE_DATE,'%Y-%m-%d') >= :startTime\n" +
            "  and DATE_FORMAT(asd.SETTLE_DATE,'%Y-%m-%d') <= :endTime\n" +
            "GROUP BY asd.AGT_ID\n" +
            "ORDER BY sum(AGT_FEE) desc\n"+
            "LIMIT 0,10");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(AgentOrderDTO.class));
    }
}

