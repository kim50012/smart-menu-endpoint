package com.basoft.eorder.foundation.jdbc.query;

import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.store.StoreChargeInfoRecord;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.OrderItemDTO;
import com.basoft.eorder.interfaces.query.OrderQuery;
import com.basoft.eorder.interfaces.query.SettleDTO;
import com.basoft.eorder.interfaces.query.SettleQuery;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午11:09 2019/8/29
 **/
@Slf4j
@Component("SettleQuery")
public class JdbcSettleQueryImpl extends BaseRepository implements SettleQuery  {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderQuery orderQuery;

    @Override
    public SettleDTO getAdminSettle(Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String receiveMonth = startTime.substring(0,7);
        param.put("receiveMonth",receiveMonth);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT min(start_dt) as payFrDt\n" );
        sql.append(",max(end_dt) as payToDt\n" );
        sql.append(",sum(order_count)payCnt\n" );
        sql.append(",ifnull(floor(sum(settle_sum)),0) as amount\n" );
        sql.append(",floor(max(pg_date)) as pgPlanDt\n" );
        sql.append(",floor(ifnull(sum(pg_sum),0)) as pgAmount\n" );
        sql.append(",floor(ifnull(sum(pg_service_fee),0)) as pgFee\n" );
        sql.append(",floor(max(pl_min_fee)) as plMinFee\n" );
        sql.append(",floor(ifnull(sum(pl_final_fee),0)) as serviceFee\n" );
        sql.append(" from store_settlement ss " );
        sql.append(" inner join store s on s.id=ss.store_id");
        sql.append(" left join area a on s.city= a.area_cd where 1=1 \n" );
        getAdminSettleCondition(sql,param,false);
        sql.append(" GROUP BY SETTLE_MONTH");

        SettleDTO settleDTO = this.queryForObject(sql.toString(),param,new BeanPropertyRowMapper<>(SettleDTO.class));
        if(settleDTO==null){
            settleDTO = new SettleDTO();
        }
        return settleDTO;
    }

    @Override
    public int getAdminSettleCount(Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String receiveMonth = startTime.substring(0,7);
        param.put("receiveMonth", receiveMonth);
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1)  from store_settlement ss \n");
        sql.append("join store s on s.id=ss.store_id join user u on s.manager_id = u.id and u.status not in(2,3)\n");
        sql.append(" left join area a on s.city= a.area_cd where 1=1 ");
        getAdminSettleCondition(sql,param,false);

        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<SettleDTO> getAdminSettleList(Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String receiveMonth = startTime.substring(0,7);
        String receiveDt = startTime.substring(0,7)+"-10";
        param.put("receiveMonth",receiveMonth);
        param.put("receiveDt",receiveDt);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sid " );
        sql.append(",ss.store_id as storeId,u.email\n" );
        sql.append(",settle_year_month\n" );
        sql.append(",settle_year\n" );
        sql.append(",settle_month\n" );
        sql.append(",start_dt as payFrDt\n" );
        sql.append(",end_dt as payToDt\n" );
        sql.append(",case settle_type when 3 then '不用' else '用' end isPay");
        sql.append(",case settle_type when 1 then '按营业额百分比' when 2 then '按营业额百分比或最小服务费' when 3 then '按月' end settleTypeStr");
        sql.append(",settle_type as settleType\n" );
        sql.append(",settle_rate as chargeRate\n" );
        sql.append(",settle_fee as chargeFee\n" );
        sql.append(",order_count\n" );
        sql.append(",floor(settle_sum) as amount\n" );
        sql.append(",pg_date as pgPlanDt\n" );
        sql.append(",floor(pg_sum) as pgAmount\n" );
        sql.append(",floor(pg_service_fee) as pgFee\n" );
        sql.append(",pl_date\n" );
        sql.append(",case when SETTLE_TYPE=1 then 0 else floor(pl_min_fee) end plMinFee\n" );
        sql.append(",floor(pl_service_fee) as  serviceFee\n" );
        sql.append(",floor(pl_final_fee) as finalFee\n" );
        sql.append(",pl_final_fee+pg_service_fee as serviceFeeSum\n" );
        sql.append(",s.store_type as storeType\n" );
        sql.append(",s.name as storeNm\n" );
        sql.append(",a.area_nm as areaNm" );
        sql.append(",:receiveMonth  as closingMonths    ");
        sql.append(" , (select a.after_one_working_days from eorder.calendar a where a.dt =  date_add(:receiveDt, interval 1 month)  ) as baReciveDt    ");
        sql.append(" , (SELECT date_format(updated,'%Y-%m-%d') from order_closing oc where store_id= s.id and closing_months=:receiveMonth and status=1)as baSureReciveDt    ");
        sql.append(" , ifnull((SELECT status from order_closing oc where store_id=s.id and closing_months=:receiveMonth ),0) as cloStatus    ");
        sql.append(",pl_final_fee from store_settlement ss " );
        sql.append(" join store s on s.id=ss.store_id and s.cash_settle_type='PG' join user u on s.manager_id = u.id and u.status not in(2,3)" );
        sql.append(" left join area a on s.city= a.area_cd where 1=1" );
        getAdminSettleCondition(sql,param,true);


        return this.getNamedParameterJdbcTemplate().query(sql.toString(),param,new BeanPropertyRowMapper<>(SettleDTO.class));
    }

    private StringBuilder getAdminSettleCondition(StringBuilder sql, Map<String, Object> param,Boolean isLimit) {
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String storeNm = Objects.toString(param.get("storeNm"), null);
        String city = Objects.toString(param.get("city"), null);
        String storeId = Objects.toString(param.get("storeId"), null);
        String storeType = Objects.toString(param.get("storeType"), null);
        String cloStatus = Objects.toString(param.get("cloStatus"), null);
        String isJoin = Objects.toString(param.get("isJoin"), null);

        if(StringUtils.isNotBlank(storeId)){
            sql.append(" and s.id=:storeId ");
        }
        if(StringUtils.isNotBlank(storeType)){
            sql.append(" and s.store_type=:storeType ");
        }
        if(StringUtils.isNotBlank(storeNm)){
            sql.append(" and     s.name like '%' :storeNm '%' ");
        }
        if (StringUtils.isNotBlank(city)) {
            sql.append(" and s.city = :city \n");
        }

        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            sql.append(" and ss.START_DT >= :startTime and ss.END_DT<=:endTime ");
        }
        if (StringUtils.isNotBlank(cloStatus)) {
            sql.append(" and ifnull((SELECT status from order_closing oc where store_id=s.id and closing_months=:receiveMonth),0)=:cloStatus");
        }
        sql.append("and s.IS_JOIN = '1' ");

        if (page >= 0 && size > 0 && isLimit) {
            appendPage(page,size,sql,param,isLimit);
        }
        return sql;
    }

    /**
     * 店铺总结算
     * 按照付款时间查询
     *
     * @param param
     * @return
     */
    @Override
    public SettleDTO getStoreSettleSum(Map<String, Object> param) {
        String storeId = Objects.toString(param.get("storeId"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        String currentTime = Objects.toString(param.get("currentTime"), null);
        String isDtl = Objects.toString(param.get("isDtl"), "");
        StringBuilder sql = new StringBuilder();
        String receiveMonth = startTime.substring(0,7);
        String receiveDt = startTime.substring(0,7)+"-10";

        //第一种情况：当收费类型为2，此字段存储最小服务费；第二种情况，当收费类型为3，此字段存储按月收费的收费金额。韩币单位
        StoreChargeInfoRecord  chargeInfo = getChargeInfo(storeId,currentTime);
        DecimalFormat df = new DecimalFormat("0.000");//设置保留位数
        String rate =  df.format((float)chargeInfo.getChargeRate()/100);

        param.put("storeId", storeId);
        param.put("receiveMonth", receiveMonth);
        param.put("receiveDt", receiveDt);

        //店铺总结算 以支付日期为查询条件
        sql.append("SELECT st.closing_months,st.pay_fr_dt,st.pay_to_dt,st.pg_fr_dt,st.pg_to_dt\n");
        sql.append(",floor(ifnull(st.pg_amount,0)) - floor(ifnull(cl.pg_amount,0)) as pg_amount\n");
        sql.append(",floor(ifnull(st.payCnt,0)) - -floor(ifnull(cl.payCnt,0)) as payCnt\n");
        sql.append(",floor(ifnull(st.amount,0)) - floor(ifnull(cl.amount,0)) as amount\n");
        sql.append(",floor(ifnull(st.pg_fee,0))- floor(ifnull(cl.pg_fee,0)) as pg_fee\n");
        sql.append(",floor(ifnull(st.service_fee,0))- floor(ifnull(cl.service_fee,0)) as service_fee\n");
        sql.append(",st.baReciveDt\n");
        sql.append(",st.baSureReciveDt\n");
        sql.append(",st.cloStatus \n");
        sql.append(" from (\n");
        sql.append(" select  date_format(min(op.pay_dts), '%Y-%m') as closing_months\n");
        sql.append(",date_format(min(op.pay_dts), '%Y-%m-%d') as pay_fr_dt\n");
        sql.append(",date_format(max(op.pay_dts), '%Y-%m-%d') as pay_to_dt\n ");
        sql.append(",(select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(min(op.pay_dts), '%Y-%m-%d')) as pg_fr_dt\n");
        sql.append(",(select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(max(op.pay_dts), '%Y-%m-%d')) as pg_to_dt\n ");
        sql.append(",sum(floor(case when  o.amount <50 then o.amount else o.amount * 0.98 end))  as pg_amount  \n");
        sql.append(",count(*) payCnt\n ");
        sql.append(",sum(o.amount) as amount\n ");
        sql.append(",sum(floor(case when  o.amount <50 then 0 else o.amount * 0.02 end)) as pg_fee\n");
        sql.append(",sum(floor(o.amount * "+rate+"))  as service_fee \n");
        sql.append(",(select a.after_one_working_days from eorder.calendar a where a.dt =  date_add(:receiveDt, interval 1 month)  ) as baReciveDt\n     ");
        sql.append(",(SELECT date_format(updated,'%Y-%m-%d') from order_closing oc where store_id= :storeId  and closing_months=:receiveMonth and status=1)as baSureReciveDt\n     ");
        sql.append(",ifnull((select a.status from  eorder.order_closing a where   a.store_id = s.id and a.closing_months = date_format(min(op.pay_dts), '%Y-%m') ), 0) as cloStatus\n ");
        sql.append(" from  \n");
        sql.append(" eorder.store s inner join eorder.`order` o on s.id = o.store_id and o.status in (4,5,6,7,8,9,10,11)\n");
        sql.append(" inner join eorder.order_pay op on o.id = op.order_id and op.pay_status = 1 where   1 = 1 \n");
        //sql.append(" and o.ORDER_TYPE=1\n");

        getSettleCondition(sql,param);
        sql.append(" )st \n");
        sql.append(" LEFT JOIN \n");
        sql.append("( SELECT date_format(min(op.pay_dts), '%Y-%m') as closing_months \n");
        sql.append(", date_format(min(op.pay_dts), '%Y-%m-%d') as pay_fr_dt\n");
        sql.append(", date_format(max(op.pay_dts), '%Y-%m-%d') as pay_to_dt\n ");
        sql.append(", (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(min(op.pay_dts), '%Y-%m-%d')) as pg_fr_dt\n");
        sql.append(", (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(max(op.pay_dts), '%Y-%m-%d')) as pg_to_dt\n ");
        sql.append(", sum(floor(case when  o.amount <50 then o.amount else o.amount * 0.98 end))  as pg_amount \n");
        sql.append(", count(*) payCnt \n");
        sql.append(", sum(o.amount) as amount \n");
        sql.append(", sum(floor(case when  o.amount <50 then 0 else o.amount * 0.02 end)) as pg_fee\n");
        sql.append(", sum(floor(o.amount * "+rate+"))  as service_fee  \n");
        sql.append(", (select a.after_one_working_days from eorder.calendar a where a.dt =  date_add(:receiveDt, interval 1 month)  ) as baReciveDt     \n");
        sql.append(", (SELECT date_format(updated,'%Y-%m-%d') from order_closing oc where store_id= :storeId  and closing_months=:receiveMonth and status=1)as baSureReciveDt \n");
        sql.append(",ifnull((select a.status from eorder.order_closing a where   a.store_id = :storeId and a.closing_months = date_format(min(op.pay_dts), '%Y-%m') ), 0) as cloStatus\n");
        sql.append(" from \n");
        sql.append(" eorder.store s \n");
        sql.append(" INNER JOIN  `order` o on s.id = o.store_id and o.status=7\n");
        sql.append(" INNER JOIN order_pay_cancel opc on o.id=opc.order_id\n");
        sql.append(" inner join eorder.order_pay op on o.id = op.order_id  and op.pay_status = 1\n");
        sql.append(" where 1=1 and o.`status`=7  and s.id=:storeId\n");
        //sql.append(" and o.ORDER_TYPE=1\n");

        //sql.append(" AND DATE_FORMAT(op.pay_dts,'%Y-%m-%d') < :startTime ");//找出非本月退款订单 但会导致当月数据不准先去掉
        sql.append(" and date_format(opc.createdt, '%Y-%m-%d')>= :startTime \n");
        sql.append(" and date_format(opc.createdt, '%Y-%m-%d') <= :endTime \n");
        sql.append(")cl \n");
        sql.append(" on 1=1 \n");

        SettleDTO settleDTO = this.queryForObject(sql.toString(),param,new BeanPropertyRowMapper<>(SettleDTO.class));
        if (settleDTO == null) {
             settleDTO = new SettleDTO();
        }
        settleDTO.setPlMinFee(String.valueOf(chargeInfo.getChargeFee()));
        settleDTO.setChargeRate(String.valueOf(chargeInfo.getChargeRate())+"%");
        settleDTO.setChargeType(chargeInfo.getChargeType());
        if(chargeInfo.getChargeType()==3&&!isDtl.equals("1")){
            settleDTO.setServiceFee(String.valueOf(chargeInfo.getChargeFee()));
        } else if (chargeInfo.getChargeType() ==2&&!isDtl.equals("1")) {
            if (Integer.valueOf(settleDTO.getServiceFee()) < chargeInfo.getChargeFee()) {
                settleDTO.setServiceFee(String.valueOf(chargeInfo.getChargeFee()));
            }
        }
        return settleDTO;
    }

    private StoreChargeInfoRecord getChargeInfo(String storeId, String startTime) {
        StoreChargeInfoRecord chargeInfo = storeRepository.getStoreMonthChargeInfo(Long.valueOf(storeId), Integer.valueOf(startTime.substring(0,4)), Integer.valueOf(startTime.substring(5,7)));
        Store store = storeRepository.getStore(Long.valueOf(storeId));
        if(chargeInfo==null)
               chargeInfo = new StoreChargeInfoRecord.Builder()
                   .chargeRate(store.chargeRate())
                   .chargeFee(store.chargeFee())
                   .chargeType(store.chargeType())
                   .build();
        return chargeInfo;
    }


    @Override
    public int getStoreSettleCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1) from (");
        sql.append(" select");
        sql.append(" count(1) ");
        sql.append(" from    eorder.store s");
        sql.append(" inner join eorder.`order` o");
        sql.append(" on s.id = o.store_id");
        sql.append(" and o.status in (4,5,6,7,8,9,10,11)");
        sql.append(" inner join eorder.order_pay op");
        sql.append(" on o.id = op.order_id");
        sql.append(" and op.pay_status = 1");
        sql.append(" where 1=1  ");
        getSettleCondition(sql, param);
        sql.append(" group by  (select a.after_fou_working_days \n" +
            "from eorder.calendar a where a.dt = date_format(op.pay_dts, '%Y-%m-%d')) ");
        sql.append(")t");

        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<SettleDTO> getStoreSettleList(Map<String, Object> param) {
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        // 查询结算信息
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String receiveMonth = startTime.substring(0,7);
        String receiveDt = startTime.substring(0,7)+"-10";
        param.put("receiveMonth",receiveMonth);
        param.put("receiveDt",receiveDt);

        String storeId = Objects.toString(param.get("storeId"), null);
        StoreChargeInfoRecord  chargeInfo = getChargeInfo(storeId,startTime);
        DecimalFormat df=new DecimalFormat("0.000");//设置保留位数
        String rate =  df.format((float)chargeInfo.getChargeRate()/100);

        StringBuilder sql = new StringBuilder();

        //店铺结算列表
        sql.append("SELECT  st.pgPlanDt \n");
        sql.append(",CONCAT(case when minCanceldt is null then minPaydt when minPaydt<minCanceldt then minpaydt  else minCanceldt end" +
            ",'~',case when maxPaydt>maxCanceldt then maxPaydt else maxPaydt end) as paydt\n");
        sql.append(",ifnull(st.payCnt,0) - -ifnull(cl.payCnt,0)  as payCnt\n");
        sql.append(",ifnull(st.pgAmount,0) - ifnull(cl.pgAmount,0)   as pgAmount\n");
        sql.append(",ifnull(st.amount,0) - ifnull(cl.amount,0) as amount\n");
        sql.append(",ifnull(st.pgFee,0) - ifnull(cl.pgFee,0) as pgFee\n");
        sql.append(",round(ifnull(st.serviceFee,0) - ifnull(cl.serviceFee,0)) as serviceFee\n");
        sql.append(" from( \n");
        sql.append("select  (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(op.pay_dts, '%Y-%m-%d')) as pgPlanDt \n");
        sql.append(",CONCAT(min(date_format(op.pay_dts,'%Y-%m-%d')),'~',max(date_format(op.pay_dts,'%Y-%m-%d')) )as paydt \n");
        sql.append(",min(date_format(op.pay_dts,'%Y-%m-%d'))minPaydt\n");
        sql.append(",max(date_format(op.pay_dts,'%Y-%m-%d'))maxPaydt\n");
        sql.append(", count(*) payCnt  \n");
        sql.append(",sum(floor(case when  o.amount <50 then o.amount else o.amount * 0.98 end)) as pgAmount \n");
        sql.append(",sum(floor(o.amount)) as amount  \n");
        sql.append(",sum(floor(case when  o.amount <50 then 0 else o.amount * 0.02 end)) as pgFee  \n");
        sql.append(",sum(floor(case when  o.amount <50 then 0 else o.amount * "+rate+" end) ) as serviceFee from eorder.store s  \n");
        sql.append("inner join eorder.`order` o on s.id = o.store_id and o.status in (4,5,6,7,8,9,10,11)  \n");
        sql.append("inner join eorder.order_pay op on o.id = op.order_id and op.pay_status = 1 \n");
        sql.append("where 1=1    \n");
        //sql.append(" and o.ORDER_TYPE=1\n");

        getSettleCondition(sql, param);
        sql.append("group by pgPlanDt)st \n");
        sql.append("LEFT JOIN \n");
        sql.append(" (SELECT (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(opc.createdt, '%Y-%m-%d')) as pgPlanDt \n");
        sql.append(",CONCAT(min(date_format(op.pay_dts,'%Y-%m-%d')),'~',max(date_format(op.pay_dts,'%Y-%m-%d')) )as paydt \n");
        sql.append(",min(date_format(opc.createdt,'%Y-%m-%d'))minCanceldt");
        sql.append(",max(date_format(opc.createdt,'%Y-%m-%d'))maxCanceldt");
        sql.append(", count(*) payCnt  \n");
        sql.append(",sum(floor(case when  o.amount <50 then o.amount else o.amount * 0.98 end)) as pgAmount   \n");
        sql.append(",sum(floor(o.amount)) as amount \n");
        sql.append(",sum(floor(case when o.amount <50 then 0 else o.amount * 0.02 end)) as pgFee  \n");
        sql.append(",sum(floor(case when o.amount <50 then 0 else o.amount * "+rate+" end) ) as serviceFee from eorder.store s  \n");
        sql.append("INNER JOIN  `order` o on s.id = o.store_id and o.status=7 \n");
        sql.append("INNER JOIN order_pay_cancel opc on o.id=opc.order_id \n");
        sql.append("inner join eorder.order_pay op on o.id = op.order_id  and op.pay_status = 1 \n");
        sql.append("where 1=1 and o.`status`=7  and o.store_id=:storeId \n");
        //sql.append(" and o.ORDER_TYPE=1 \n");

        //sql.append("and o.created<:startTime ");
        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            sql.append("and date_format(opc.createdt, '%Y-%m-%d')>=:startTime ");
            sql.append("and date_format(opc.createdt, '%Y-%m-%d')<=:endTime  ");
        }
        sql.append("GROUP BY pgPlanDt)cl on  st.pgPlanDt=cl.pgPlanDt\n");

        if (page >= 0 && size > 0 ) {
            int resPage = page;
            if(page > 0){
                resPage = (resPage-1)*size;
                param.put("page",resPage);
            }
            sql.append(LIMIT);
        }

        log.info(sql.toString());
        List<SettleDTO> settleDTOList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(SettleDTO.class));

        return settleDTOList;

    }

    /**
     * manager结算查询条件 admin结算没有使用这个查询条件
     * 按付款时间筛选
     *
     * @Param
     * @return java.lang.String
     * @author Dong Xifu
     * @date 2019/9/2 10:52 上午
     */
    private String getSettleCondition(StringBuilder sql, Map<String, Object> param) {
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String storeNm = Objects.toString(param.get("storeNm"), null);
        String storeId = Objects.toString(param.get("storeId"), null);
        String storeType = Objects.toString(param.get("storeType"), null);

        if(StringUtils.isNotBlank(storeId)){ //s store
            sql.append(" and s.id=:storeId ");
        }
        if(StringUtils.isNotBlank(storeType)){
            sql.append(" and s.store_type=:storeType ");
        }
        if(StringUtils.isNotBlank(storeNm)){
            sql.append(" and     s.name like '%' :storeNm '%' ");
        }
        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
           // sql.append(" and (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(op.pay_dts, '%Y-%m-%d')) >= :startTime "); //结算时间
            //sql.append(" and (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(op.pay_dts, '%Y-%m-%d')) <= :endTime ");
            sql.append(" and     DATE_FORMAT(op.pay_dts,'%Y-%m-%d') >= :startTime ");//付款时间
            sql.append(" and     DATE_FORMAT(op.pay_dts,'%Y-%m-%d') <= :endTime ");
        }

        String groupSql = StringUtils.isBlank(storeId)?" s.id,s.name,s.city":" pgPlanDt";
        return groupSql;
    }


    @Override
    public int getSettleDtlCount(Map<String, Object> param) {
        StringBuilder sql = getDtlSql(new StringBuilder("SELECT count(1) from ("),param,false);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    /**
     * 结算详情列表按照结算日期做查询条件
     *
     *
     * @Param
     * @return java.util.List<com.basoft.eorder.interfaces.query.SettleDTO>
     * @author Dong Xifu
     * @date 2019/9/6 10:11 下午
     */
    @Override
    public List<SettleDTO> getSettleDtlListByMap(Map<String, Object> param) {
        StringBuilder sql = getDtlSql(new StringBuilder("SELECT * from ("),param,true);
        List<SettleDTO> settleDTOList =  this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(SettleDTO.class));
        Map<String,Object> itemParam =  Maps.newHashMap();

        settleDTOList.stream().map(s -> {
            if (s != null&&s.getOrderId()!=null) {
                itemParam.put("orderId",s.getOrderId());
                List<OrderItemDTO> itemList =  orderQuery.getOrderItemListByMap(itemParam);
                if (itemList != null && itemList.size() > 0) {
                    s.setItemList(itemList);
                } else {
                    s.setItemList(new LinkedList<>());
                }
            }
            return s;
        }).collect(Collectors.toList());
        return settleDTOList;
    }

    private StringBuilder getDtlSql(StringBuilder sql, Map<String, Object> param,Boolean isLimit) {
            String storeId = Objects.toString(param.get("storeId"), null);
            String startTime = Objects.toString(param.get("startTime"), null);
            String receiveDt = startTime.substring(0,7)+"-10";
            StoreChargeInfoRecord  chargeInfo = getChargeInfo(storeId,startTime);
            DecimalFormat df=new DecimalFormat("0.000");//设置保留位数
            int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
            int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
            String rate = df.format((float)chargeInfo.getChargeRate()/100);

            param.put("receiveDt", receiveDt);

            //sql.append(sql);
            sql.append(" select  o.id as orderId,s.id,s.name as store_nm\n");
            sql.append(",(select a.area_nm from eorder.area a where a.area_cd = s.city) as area_nm\n");
            sql.append(",date_format(op.pay_dts, '%Y-%m') as closing_months\n");
            sql.append(", date_format(op.pay_dts, '%Y-%m-%d') as pay_fr_dt\n");
            sql.append(",date_format(op.pay_dts, '%Y-%m-%d') as pay_to_dt\n");
            sql.append(", (select a.after_fou_working_days from eorder.calendar a\n");
            sql.append(" where a.dt = date_format(op.pay_dts, '%Y-%m-%d')) as pg_fr_dt , (select a.after_fou_working_days\n");
            sql.append(" from eorder.calendar a where a.dt = date_format(op.pay_dts, '%Y-%m-%d')) as pg_to_dt\n");
            sql.append(", floor(case when  o.amount <50 then floor(o.amount) else floor(o.amount * 0.98) end) as pg_amount \n");
            sql.append(", floor(case when  o.amount <50 then 0 else floor(o.amount * 0.02) end) as pg_fee ");
            sql.append(",floor(o.amount) as amount\n");
            sql.append(",floor(o.amount * "+rate+") as service_fee \n");
            sql.append(",(select a.after_one_working_days\n");
            sql.append("from eorder.calendar a where a.dt = date_add(:receiveDt, interval 1 month) ) as baReciveDt \n");
            sql.append(",ifnull((         select     a.status     from    eorder.order_closing a\n ");
            sql.append("where   a.store_id = s.id and a.closing_months = date_format(op.pay_dts, '%Y-%m')), 0) as cloStatus\n ");
        sql.append(",o.status \n");
        sql.append("from    eorder.store s\n ");
        sql.append("inner join eorder.`order` o on s.id = o.store_id and\n ");
        sql.append("o.status in (4,5,6,7,8,9,10,11) inner join eorder.order_pay op on o.id = op.order_id and op.pay_status=1\n ");
        sql.append("where 1=1 and s.id=:storeId \n");
        //sql.append(" and o.ORDER_TYPE=1\n ");
        sql.append(" and     DATE_FORMAT(op.pay_dts,'%Y-%m-%d') >= :startTime\n ");//付款时间
        sql.append(" and     DATE_FORMAT(op.pay_dts,'%Y-%m-%d') <= :endTime\n ");
        sql.append(" union all\n");
        sql.append(" select o.id as orderId,s.id,s.name as store_nm\n");
        sql.append(",(select a.area_nm from eorder.area a where a.area_cd = s.city) as area_nm\n");
        sql.append(",date_format(op.pay_dts, '%Y-%m') as closing_months,date_format(op.pay_dts, '%Y-%m-%d') as pay_fr_dt\n");
        sql.append(",date_format(op.pay_dts, '%Y-%m-%d')as pay_to_dt\n" );
        sql.append(",(select a.after_fou_working_days from eorder.calendar a  where a.dt = date_format(opc.createdt, '%Y-%m-%d')) as pg_fr_dt\n " );
        sql.append(",(select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(opc.createdt, '%Y-%m-%d')) as pg_to_dt\n ");
        sql.append(",(case when o.amount<50 then -floor(o.amount) else -floor(o.amount * 0.98) end) as pg_amount");
        sql.append(",(case when o.amount<50 then 0 else -floor(o.amount * 0.02) end) as pg_fee ");
        sql.append(",-floor(o.amount) as amount\n ");
        sql.append(",(case when o.amount<50 then 0 else -floor(o.amount * "+rate+") end)  as service_fee ");
        sql.append(",(select a.after_one_working_days\n ");
        sql.append("from eorder.calendar a where a.dt = date_add(:receiveDt, interval 1 month) ) as baReciveDt\n ");
        sql.append(",ifnull((select a.status from eorder.order_closing a \n");
        sql.append("where a.store_id = s.id  and  a.closing_months = date_format(op.pay_dts, '%Y-%m') ), 0) as cloStatus\n");
        sql.append(",o.status \n");
        sql.append("from eorder.store s \n");
        sql.append("inner join eorder.`order` o on s.id = o.store_id and \n");
        sql.append("o.status = 7 inner join eorder.order_pay op on o.id = op.order_id and op.pay_status = 1\n " );
        sql.append("INNER JOIN eorder.order_pay_cancel opc on o.id=opc.order_id\n ");
        sql.append("where 1=1 and s.id=:storeId\n ");
        //sql.append(" and o.ORDER_TYPE=1\n");

        //getSettleCondition(sql,param);
//        sql.append(" and  ((select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(opc.createdt, '%Y-%m-%d')) >=:startTime ");
//        sql.append(" and  (select a.after_fou_working_days from eorder.calendar a where a.dt = date_format(opc.createdt, '%Y-%m-%d')) <=:endTime )");

        sql.append(" and  DATE_FORMAT(opc.createdt,'%Y-%m-%d') >= :startTime\n ");
        sql.append(" and  DATE_FORMAT(opc.createdt,'%Y-%m-%d') <=:endTime \n");
        sql.append(" )tt1\n");
        sql.append(" ORDER BY tt1.orderId asc,tt1.amount desc\n");

        if (page >= 0 && size > 0 &&isLimit) {
            int resPage = page;
            if(page > 0){
                resPage = (resPage-1)*size;
                param.put("page",resPage);
            }
            sql.append(LIMIT);
        }
        return  sql;
    }

}
