package com.basoft.eorder.interfaces.query.ghraphql;

import ch.qos.logback.core.CoreConstants;
import com.basoft.eorder.application.framework.ComponentProvider;
import com.basoft.eorder.interfaces.query.PaginationDTO;
import com.basoft.eorder.interfaces.query.baSettle.BaSettleDtlDTO;
import com.basoft.eorder.interfaces.query.baSettle.BaSettleQuery;
import com.basoft.eorder.interfaces.query.baSettle.StoreSettlementBaDTO;
import com.basoft.eorder.interfaces.query.baSettle.StoreSettlementDayBaDTO;
import com.basoft.eorder.util.DateUtil;
import com.google.common.collect.Maps;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 3:12 PM 1/3/20
 **/
public class BaSettleGraphqlQuery {

    private static Logger logger = LoggerFactory.getLogger(DefaultGraphqlQueryInit.class);


    private static Map<String, Object> getSettleParam(DataFetchingEnvironment env, Boolean isReduceDate) {
        Integer page = env.getArgument("page");
        Integer size = env.getArgument("size");
        String storeId = BaseGraphql.getStoreIdStr(env);
        String startTime = env.getArgument("startTime");
        String endTime = env.getArgument("endTime");
        String storeName = env.getArgument("storeName");
        String storeType = env.getArgument("storeType");

        Map<String, Object> param = Maps.newHashMap();

        if (page != null && size != null && page >= 0 && size > 0) {
            param.put("page", page);
            param.put("size", size);
        }

        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DAILY_DATE_PATTERN);
            endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);
            String receiveMonth = "";
            if (isReduceDate) {
                param.put("startTime", DateUtil.dateFormat(startTime));//日期月份减一个月
                param.put("endTime", DateUtil.getLastDayOfMonth(DateUtil.dateFormat(startTime)));
                receiveMonth = DateUtil.dateFormat(startTime).substring(0,7);
                param.put("receiveMonth",receiveMonth);
            } else {
                param.put("startTime", startTime);
                param.put("endTime", endTime);
                receiveMonth = startTime.substring(0,7);
                param.put("receiveMonth",receiveMonth);
            }
        }

        if (StringUtils.isNotBlank(storeId)) {
            param.put("storeId", storeId);
        }
        if (StringUtils.isNotBlank(storeType)) {
            param.put("storeType", storeType);
        }
        if (StringUtils.isNotBlank(storeName)) {
            param.put("storeName", storeName);
        }
        String cloStatus = env.getArgument("cloStatus");//结算状态
        if (StringUtils.isNotBlank(cloStatus)) {
            param.put("cloStatus",env.getArgument("cloStatus"));
        }
        String city = env.getArgument("city");
        if (StringUtils.isNotBlank(city)) {
            param.put("city",env.getArgument("city"));
        }
        return param;
    }


    /**
     * admin结算总金额
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<StoreSettlementBaDTO> initAdminBaSumSettle(ComponentProvider componentFactory){
        BaSettleQuery bsq = componentFactory.getComponent(BaSettleQuery.class);

        return env -> {
            Map<String, Object> param = getSettleParam(env,true);

            return bsq.getAdminSettleSum(param);
        };
    }

    /**
     * admin端店铺列表ba结算
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<PaginationDTO<StoreSettlementBaDTO>> initAdminBaSettles(ComponentProvider componentFactory) {
        BaSettleQuery baSq = componentFactory.getComponent(BaSettleQuery.class);
        return env -> {
            PaginationDTO<StoreSettlementBaDTO> pageOrder = new PaginationDTO<>();
            Map<String, Object> param = getSettleParam(env,true);

            int total = baSq.getAdminSettleBaCount(param);
            List<StoreSettlementBaDTO> settleList = new LinkedList<>();
            if(total>0){
                pageOrder.setTotal(total);
                settleList = baSq.getAdminSettleBaList(param);
                pageOrder.setDataList(settleList);
                return  pageOrder;
            }
            return new PaginationDTO<>(total, settleList);
        };
    }

    /**
     * 获取结算详情总数据
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<BaSettleDtlDTO> initBaSettleDtl(ComponentProvider componentFactory){
        BaSettleQuery bsq = componentFactory.getComponent(BaSettleQuery.class);

        return env -> {
            Map<String, Object> param = getSettleParam(env,false);

            return bsq.getBaSettleDtl(param);
        };
    }


    /**
     * 结算详情
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<PaginationDTO<BaSettleDtlDTO>> initBASettlesDtls(ComponentProvider componentFactory) {
        BaSettleQuery bsq = componentFactory.getComponent(BaSettleQuery.class);

        return env -> {
            PaginationDTO<BaSettleDtlDTO> pageOrder = new PaginationDTO<>();
            Map<String,Object> param = getSettleParam(env,false);

            int total = bsq.getBaSettleDtlCount(param);
            List<BaSettleDtlDTO> settleList = new LinkedList<>();
            if(total>0){
                settleList = bsq.getBaSettleDtlListByMap(param);
                pageOrder.setTotal(total);
                pageOrder.setDataList(settleList);
                return pageOrder;
            }
            return new PaginationDTO<>(total, settleList);
        };
    }



    /**
     * manager端店铺总结算
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<StoreSettlementDayBaDTO> initBaSettleSumDay(ComponentProvider componentFactory){
        BaSettleQuery bsq = componentFactory.getComponent(BaSettleQuery.class);

        return env -> {
            Map<String, Object> param = getSettleParam(env,true);

            return bsq.getSettleSumDay(param);
        };
    }


    /**
     * manager端每日结算
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<PaginationDTO<StoreSettlementDayBaDTO>> initBaSettleDays(ComponentProvider componentFactory) {
        BaSettleQuery bsq = componentFactory.getComponent(BaSettleQuery.class);

        return env -> {
            PaginationDTO<StoreSettlementDayBaDTO> pageOrder = new PaginationDTO<>();
            Map<String,Object> param = getSettleParam(env,true);

            int total = bsq.getSettlesDayCount(param);
            List<StoreSettlementDayBaDTO> settleList = new LinkedList<>();
            if(total>0){
                settleList = bsq.getSettleDayListByMap(param);
                pageOrder.setTotal(total);
                pageOrder.setDataList(settleList);
                return pageOrder;
            }
            return new PaginationDTO<>(total, settleList);
        };
    }



}
