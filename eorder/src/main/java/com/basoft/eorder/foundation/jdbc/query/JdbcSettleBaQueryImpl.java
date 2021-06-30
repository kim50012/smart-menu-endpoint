package com.basoft.eorder.foundation.jdbc.query;

import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.store.StoreChargeInfoRecord;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.baSettle.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component("StoreSettlementBaQuery")
public class JdbcSettleBaQueryImpl extends BaseRepository implements BaSettleQuery {

    @Autowired
    private StoreRepository storeRepository;

    private final static String StoreSettlementBa_SELECT = "select ss.SID " +
        ",ss.STORE_ID " +
        ",ss.SETTLE_YEAR_MONTH " +
        ",ss.SETTLE_YEAR " +
        ",ss.SETTLE_MONTH " +
        ",ss.START_DT " +
        ",ss.END_DT " +
        ",ss.SETTLE_TYPE " +
        ",ss.SETTLE_RATE " +
        ",ss.SETTLE_FEE " +
        ",ifnull(ss.ORDER_COUNT,0)+ifnull(ss.ORDER_COUNT_DAOSHOU,0) as ORDER_COUNT" +
        ",ifnull(ss.SETTLE_SUM,0)+ifnull(ss.ORDER_DAOSHOU_AMOUNT_SUM,0) as sumAmount" +
        ",ss.PG_DATE " +
        ",ss.PG_SUM " +
        ",ss.PG_SERVICE_FEE " +
        ",ss.PL_DATE " +
        ",ss.PL_MIN_FEE " +
        ",ss.PL_SERVICE_FEE pgFee" +
        ",ss.PL_FINAL_FEE " +
        ",ifnull(ss.CASH_SETTLE_SUM,0) CASH_SETTLE_SUM" +
        ",ss.CREATE_TIME " +
        ",ss.CREATE_USER " +
        ",ss.UPDATE_TIME " +
        ",ss.UPDATE_USER " +
        ",IFNULL(PL_FINAL_FEE,0)+ifnull(PG_SERVICE_FEE,0)+ifnull(ss.ORDER_PlAT_AMOUNT_SUM,0)-ifnull(ss.ORDER_DAOSHOU_AMOUNT_SUM,0)serviceFeeSum" +
        ",s.name as storeName" +
        ",s.store_type as storeType" +
        ",s.CHARGE_RATE" +
        ",st_bank_name" +
        ",st_bank_acc" +
        ",st_bank_acc_name" +
        ",a.area_nm as areaNm" +
        ",ifnull((select a.status from eorder.order_closing a\n" +
        "where a.store_id = ss.store_id and a.cash_settle_type='PG' and a.closing_months = date_format(ss.START_DT, '%Y-%m') ), 0) as cloStatus";

    private final static String StoreSettlementBa_FROM = " from store_settlement_ba ss inner join store s on s.id=ss.store_id and s.cash_settle_type='BA'" +
        "  join area a on s.city= a.area_cd where 1=1 \n";


    private final static String StoreSettlementDayBa_SELECT = "select ss.SID " +
        ",ss.STORE_ID " +
        ",date_format(ss.SETTLE_DATE,'%Y-%m-%d')SETTLE_DATE " +
        ",ss.SETTLE_TYPE " +
        ",ss.SETTLE_RATE " +
        ",ss.SETTLE_FEE \n" +
        ",ifnull(ss.ORDER_COUNT,0)+ifnull(ss.ORDER_COUNT_DAOSHOU,0) as ORDER_COUNT " +
        ",ifnull(ss.SETTLE_SUM,0)+ifnull(ss.ORDER_DAOSHOU_AMOUNT_SUM,0) as sumAmount\n" +
        ",ss.PG_DATE " +
        ",ss.PG_SUM " +
        ",ss.PG_SERVICE_FEE pgFee" +
        ",ss.PL_DATE " +
        ",ss.PL_MIN_FEE " +
        ",ss.PL_SERVICE_FEE " +
        ",ss.PL_FINAL_FEE \n" +
        ",ifnull(ss.CASH_SETTLE_SUM,0) CASH_SETTLE_SUM" +
        ",ss.CREATE_TIME " +
        ",ss.CREATE_USER " +
        ",ss.UPDATE_TIME " +
        ",ss.UPDATE_USER \n" +
        ",IFNULL(PL_FINAL_FEE,0)+ifnull(PG_SERVICE_FEE,0)+ifnull(ss.ORDER_PlAT_AMOUNT_SUM,0)-ifnull(ss.ORDER_DAOSHOU_AMOUNT_SUM,0)serviceFeeSum" +
        ",s.name as storeName" +
        ",s.store_type as storeType" +
        ",s.CHARGE_RATE" +
        ",a.area_nm as areaNm\n";

    private final static String StoreSettlementDayBa_FROM = " from store_settlement_day_ba ss inner join store s on s.id=ss.store_id \n" +
        "  join area a on s.city= a.area_cd where 1=1 \n";

    @Override
    public StoreSettlementBaDTO getStoreSettlementBaDto(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(StoreSettlementBa_SELECT + StoreSettlementBa_FROM);
        getSettleBaCondition(sql, param);
        StoreSettlementBaDTO dto = this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(StoreSettlementBaDTO.class));
        return dto;
    }

    @Override
    public StoreSettlementBa getStoreSettlementBa(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(StoreSettlementBa_SELECT + StoreSettlementBa_FROM);
        getSettleBaCondition(sql, param);
        return this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(StoreSettlementBa.class));
    }

    @Override
    public StoreSettlementBaDTO getAdminSettleSum(Map<String, Object> param) {

        StringBuilder sql = new StringBuilder();
        sql.append("select :receiveMonth  as closingMonths,ifnull(sum(PL_FINAL_FEE),0) plFinalFee,ifnull(sum(PG_SERVICE_FEE),0)pgFee\n");
        sql.append(",ifnull(ss.SETTLE_SUM,0)+ifnull(ss.ORDER_DAOSHOU_AMOUNT_SUM,0) as sumAmount,ifnull(sum(CASH_SETTLE_SUM),0) cashSettleSum");
        sql.append(",ifnull(ss.ORDER_COUNT,0)+ifnull(ss.ORDER_COUNT_DAOSHOU,0) orderCount");
        sql.append(",IFNULL(PL_FINAL_FEE,0)+ifnull(PG_SERVICE_FEE,0)+ifnull(ss.ORDER_PlAT_AMOUNT_SUM,0)-ifnull(ss.ORDER_DAOSHOU_AMOUNT_SUM,0)serviceFeeSum");//总服务费
        sql.append(" from STORE_SETTLEMENT_BA ss  join store s on ss.store_id=s.id  where 1=1 \n");
        getSettleBaCondition(sql, param);
        return this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(StoreSettlementBaDTO.class));
    }

    @Override
    public int getAdminSettleBaCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder("select count(1)" + StoreSettlementBa_FROM);
        getSettleBaCondition(sql, param);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<StoreSettlementBaDTO> getAdminSettleBaList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder(StoreSettlementBa_SELECT + StoreSettlementBa_FROM);
        getSettleBaCondition(sql, param);
        orderByAndPage(param, sql, " order by PL_FINAL_FEE desc ");

        List<StoreSettlementBaDTO> storeSettlementBaDtoList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(StoreSettlementBaDTO.class));
        return storeSettlementBaDtoList;
    }


    @Override
    public StoreSettlementDayBaDTO getSettleSumDay(Map<String, Object> param) {
        setParam(param);
        StringBuilder sql = new StringBuilder();
        sql.append("select :receiveMonth  as closingMonths,ifnull(sum(PL_FINAL_FEE),0) plFinalFee \n");
        sql.append(",ifnull(sum(PG_SERVICE_FEE),0)pgFee,ifnull(ss.SETTLE_SUM,0)+ifnull(ss.ORDER_DAOSHOU_AMOUNT_SUM,0) as sumAmount,ifnull(sum(CASH_SETTLE_SUM),0) cashSettleSum");
        sql.append(",ifnull(ss.ORDER_COUNT,0)+ifnull(ss.ORDER_COUNT_DAOSHOU,0) orderCount");
        sql.append(",IFNULL(PL_FINAL_FEE,0)+ifnull(PG_SERVICE_FEE,0)+ifnull(ss.ORDER_PlAT_AMOUNT_SUM,0)-ifnull(ss.ORDER_DAOSHOU_AMOUNT_SUM,0)serviceFeeSum");//总服务费
        sql.append(",ifnull(SETTLE_RATE,:settleRate)SETTLE_RATE");
        sql.append(" from store_settlement_day_ba ss where 1=1\n");
        getSettleBaDayCondition(sql, param);
        return this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(StoreSettlementDayBaDTO.class));
    }

    @Override
    public int getSettlesDayCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder("select count(1)" + StoreSettlementDayBa_FROM);
        getSettleBaDayCondition(sql, param);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<StoreSettlementDayBaDTO> getSettleDayListByMap(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder(StoreSettlementDayBa_SELECT + StoreSettlementDayBa_FROM);
        getSettleBaDayCondition(sql, param);
        orderByAndPage(param, sql, " order by SETTLE_DATE desc ");

        List<StoreSettlementDayBaDTO> storeSettlementDayBaDtoList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(StoreSettlementDayBaDTO.class));
        return storeSettlementDayBaDtoList;
    }

    @Override
    public BaSettleDtlDTO getBaSettleDtl(Map<String, Object> param) {
        setParam(param);

        StringBuilder sql = new StringBuilder();
        sql.append("select sum(amount) amount ,sum(pgFee)pgFee,sum(pgAmount)pgAmount,sum(plFinalFee)plFinalFee,sum(settleAmount)settleAmount\n");
        sql.append(",sum(pgFee+plFinalFee)serviceFeeSum");
        sql.append(",ifnull(sum(CASE WHEN orderId<1 THEN 0 ELSE 1 END),0) orderCount");
        sql.append(",:completeDate as completeDate");
        sql.append(",:settleRate as settleRate");
        sql.append(" from");
        getSettleDtl(sql, param);
        return this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(BaSettleDtlDTO.class));
    }

    @Override
    public int getBaSettleDtlCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1) from \n");
        getSettleDtl(sql, param);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<BaSettleDtlDTO> getBaSettleDtlListByMap(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder("select * from ");
        getSettleDtl(sql, param);

        List<BaSettleDtlDTO> dtlDTOList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(BaSettleDtlDTO.class));

        return dtlDTOList;
    }

    private StringBuilder getSettleDtl(StringBuilder sql, Map<String, Object> param) {
        setParam(param);
        String rate = Objects.toString(param.get("rate"), null);

        sql.append("(select date_format(o.updated,'%Y-%m-%d')as completeDate,o.id as orderId, date_format(op.pay_dts,'%Y-%m-%d')as payDt,\n" +
                "case when o.AMOUNT_SETTLE>0 then AMOUNT_SETTLE else amount end amount,\n" +
            "       (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(op.pay_dts, '%Y-%m-%d')) as pgPlanDt\n" +
            ",inf.reseve_dt_from" +
            ",floor(case when  o.amount <50 then 0 else o.amount * 0.02 end) as pgFee\n" +
            ",floor(case when  o.amount <50 then o.amount else o.amount * 0.98 end) as pgAmount\n" +
            ",case when  o.AMOUNT_SETTLE>0 then o.amount-o.AMOUNT_SETTLE else floor(case when  o.amount <50 then o.amount else o.amount * " + rate + " end) end plFinalFee\n" +
            ",case when o.AMOUNT_SETTLE>0 then o.AMOUNT_SETTLE else (o.amount-floor(case when  o.amount <50 then 0 else o.amount * 0.02 end) - floor(case when o.amount <50 then 0 else o.amount * "+rate+" end))end settleAmount\n" +
            ",floor(case when  o.amount <50 then 0 else o.amount * 0.02 end)+floor(case when  o.amount <50 then 0 else o.amount * " + rate + " end)serviceFeeSum\n" +
            "from `order` o\n" +
            "INNER JOIN store s on s.id=o.store_id\n"+
            "inner join order_pay op on o.id=op.order_id and o.status=9\n" +
            "left join order_info inf on o.id=inf.order_id \n" +
            "where 1=1  \n");
        getDtlCondition(sql, param);
        sql.append(")t");
        return sql;
    }

    private StringBuilder getDtlCondition(StringBuilder sql, Map<String, Object> param) {
        String storeId = Objects.toString(param.get("storeId"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);

        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and o.store_id = :storeId ");
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and date_format(o.updated,'%Y-%m-%d')>= :startTime and date_format(o.updated,'%Y-%m-%d')<= :endTime ");
        }

        return sql;
    }

    private StringBuilder getSettleBaDayCondition(StringBuilder sql, Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String storeId = Objects.toString(param.get("storeId"), null);

        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and ss.store_id = :storeId ");
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and ss.SETTLE_DATE >= :startTime and ss.SETTLE_DATE<=:endTime ");
        }

        return sql;
    }


    private StringBuilder getSettleBaCondition(StringBuilder sql, Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String sid = Objects.toString(param.get("sid"), null);
        String city = Objects.toString(param.get("city"), null);
        String storeId = Objects.toString(param.get("storeId"), null);
        String storeType = Objects.toString(param.get("storeType"), null);
        String cloStatus = Objects.toString(param.get("cloStatus"), null);

        if (StringUtils.isNotBlank(sid)) {
            sql.append(" and sid = :sid");
        }
        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and ss.STORE_ID=:storeId ");
        }
        if (StringUtils.isNotBlank(storeType)) {
            sql.append(" and ss.store_type=:storeType ");
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and ss.START_DT >= :startTime and ss.END_DT<=:endTime ");
        }
        if (StringUtils.isNotBlank(city)) {
            sql.append(" and s.city = :city \n");
        }
        if (StringUtils.isNotBlank(cloStatus)) {
            sql.append(" and ifnull((SELECT status from order_closing oc where store_id=s.id and closing_months=:receiveMonth),0)=:cloStatus");
        }

        return sql;
    }


    private StoreChargeInfoRecord getChargeInfo(String storeId, String startTime) {
        StoreChargeInfoRecord chargeInfo = storeRepository.getStoreMonthChargeInfo(Long.valueOf(storeId), Integer.valueOf(startTime.substring(0, 4)), Integer.valueOf(startTime.substring(5, 7)));
        Store store = storeRepository.getStore(Long.valueOf(storeId));
        if (chargeInfo == null)
            chargeInfo = new StoreChargeInfoRecord.Builder()
                .chargeRate(store.chargeRate())
                .chargeFee(store.chargeFee())
                .chargeType(store.chargeType())
                .build();
        return chargeInfo;
    }

    private Map<String, Object> setParam(Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String storeId = Objects.toString(param.get("storeId"), null);
        StoreChargeInfoRecord chargeInfo = getChargeInfo(storeId, startTime);
        DecimalFormat df = new DecimalFormat("0.000");//设置保留位数
        String rate = df.format((float) chargeInfo.getChargeRate() / 100);
        param.put("rate", rate);
        param.put("settleRate", Double.valueOf(rate)*100);
        param.put("completeDate", startTime);
        return param;
    }
}

