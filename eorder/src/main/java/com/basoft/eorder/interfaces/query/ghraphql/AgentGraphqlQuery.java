package com.basoft.eorder.interfaces.query.ghraphql;

import ch.qos.logback.core.CoreConstants;
import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.framework.ComponentProvider;
import com.basoft.eorder.domain.model.agent.Agent;
import com.basoft.eorder.interfaces.query.AgentStoreDTO;
import com.basoft.eorder.interfaces.query.PaginationDTO;
import com.basoft.eorder.interfaces.query.UserOrderDTO;
import com.basoft.eorder.interfaces.query.agent.*;
import com.basoft.eorder.interfaces.query.plAgt.PlAgtAmountDTO;
import com.basoft.eorder.interfaces.query.plAgt.PlAgtAmountStaticsDTO;
import com.basoft.eorder.interfaces.query.plAgt.PlAgtFeeDTO;
import com.basoft.eorder.util.DateUtil;
import com.basoft.eorder.util.SetParamUtil;
import com.google.common.collect.Maps;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 10:49 上午 2019/9/26
 **/
public class AgentGraphqlQuery {
    private static Logger logger = LoggerFactory.getLogger(DefaultGraphqlQueryInit.class);


    private static Map<String, Object> getAgentId(ComponentProvider componentFactory, DataFetchingEnvironment env) {
        AgentQuery aq = componentFactory.getComponent(AgentQuery.class);
        //如果 Context非空时， 优先获取context里头的值
        Map<String, Object> contextMap = env.getContext();
        Map<String, Object> param = new HashMap<>();
        if (contextMap != null && contextMap.size() > 0) {
            UserSession us = (UserSession) contextMap.get(AppConfigure.BASOFT_USER_SESSION_PROP);
            if (us != null) {
                logger.debug("Get agentAccount from userSeession :" + us.getAccount());
                param.put("agtCode", us.getAccount());
                Agent agent = aq.getAgent(param);
                param.put("agtId", agent.getAgtId().toString());
                param.put("isManager", true);
                param.put("agtType", agent.getAgtType());
                return param;
            }
        }
        String agtId = env.getArgument("agtId");
        param.put("agtId", agtId);
        param.put("isManager", false);

        if(StringUtils.isNotBlank(agtId)){
            Agent agent = aq.getAgent(param);
            param.put("agtType", agent==null?"":agent.getAgtType());
            return param;
        }else {
            return param;
        }
    }

    private static String getAgentIdStr(ComponentProvider componentFactory, DataFetchingEnvironment env) {
        AgentQuery aq = componentFactory.getComponent(AgentQuery.class);
        //如果 Context非空时， 优先获取context里头的值
        Map<String, Object> contextMap = env.getContext();
        Map<String, Object> param = new HashMap<>();
        if (contextMap != null && contextMap.size() > 0) {
            UserSession us = (UserSession) contextMap.get(AppConfigure.BASOFT_USER_SESSION_PROP);
            if (us != null) {
                logger.debug("Get agentAccount from userSeession :" + us.getAccount());
                param.put("agtCode", us.getAccount());
                return us.getAgentId().toString();
            }
        }
        return env.getArgument("agtId");
    }


    private static Map<String, Object> getPagingParam(DataFetchingEnvironment env) {
        Integer page = env.getArgument("page");
        Integer size = env.getArgument("size");
        Map<String, Object> param = Maps.newHashMap();

        if (page != null && size != null && page >= 0 && size > 0) {
            param.put("page", page);
            param.put("size", size);
        }
        return param;
    }


    /**
     * 查询代理商详情
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<AgentDTO> initAgent(ComponentProvider componentFactory) {
        AgentQuery query = componentFactory.getComponent(AgentQuery.class);
        return env->{
            Map<String, Object> param = setAgtStoreParam(env);
            String agtId = getAgentIdStr(componentFactory,env);
            param.put("agtId", agtId);
            AgentDTO agt = query.getAgentDto(param);
            return agt;
        };
    }
    /**
     * 查询Agent列表
     *
     * @Param saveAgent
     * @rern int
     * @author Dong Xifu
     */
    public static <T> DataFetcher<PaginationDTO<AgentDTO>> initAgents(ComponentProvider componentFactory) {
        AgentQuery query = componentFactory.getComponent(AgentQuery.class);
        return env -> {
            // 查询参数
            String agtId = env.getArgument("agtId");
            String agtName = env.getArgument("agtName");
            String agtCode = env.getArgument("agtCode");
            String agtType = env.getArgument("agtType");
            String agtStatus = env.getArgument("agtStatus");
            String agtMobile = env.getArgument("agtMobile");

            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");

            Map<String, Object> param = Maps.newHashMap();
            if (StringUtils.isNotBlank(agtId)) {
                param.put("agtId", agtId);
            }
            if (StringUtils.isNotBlank(agtName)) {
                param.put("agtName", agtName);
            }
            if (StringUtils.isNotBlank(agtCode)) {
                param.put("agtCode", agtCode);
            }
            if (StringUtils.isNotBlank(agtType)) {
                param.put("agtType", agtType);
            }
            if (StringUtils.isNotBlank(agtStatus)) {
                param.put("agtStatus", agtStatus);
            }
            if (StringUtils.isNotBlank(agtMobile)) {
                param.put("agtMobile", agtMobile);
            }

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }

            final int totalCount = query.getAgentCount(param);
            final List<AgentDTO> list = query.getAgentListByMap(param);

            return new PaginationDTO(totalCount, list);
        };
    }

    /**
     * 查询所有店铺以及绑定状态
     * 查询指定代理商所代理的店铺
     *
     * @return PaginationDTO<AgentStoreDTO>
     * @author Dong Xifu
     * @date 2019/9/19 10:42 上午
     */
    public static <T> DataFetcher<AgentPagingDTO<AgentStoreDTO>> initAgtStores(ComponentProvider componentFactory) {
        AgentQuery query = componentFactory.getComponent(AgentQuery.class);
        return env -> {
            // 查询参数
            Map<String, Object> param = setAgtStoreParam(env);
            Map<String,Object> agentMap = getAgentId(componentFactory,env);
            String agtId = (String)agentMap.get("agtId");
            if (StringUtils.isNotBlank(agtId)) {
                param.put("agtId", agtId);
            }

            List<AgentStoreDTO> list = new LinkedList<>();
            int total = 0;
            Map<String, Object> resultMap = new HashMap<>();
            if ((Boolean) agentMap.get("isManager")) {
                resultMap = query.getAgtStoreOrderSum(param);
                total = query.getIsBindCount(param);
                list = query.getIsBindStoreListByMap(param);
                return new AgentPagingDTO<>(new BigDecimal(resultMap.get("sumAmount").toString())
                    , new BigDecimal(resultMap.get("agtFee").toString())
                    , new BigDecimal(resultMap.get("vatFee").toString())
                    , Integer.valueOf(resultMap.get("qty").toString()), total, list);
            } else {
                total = query.getAgentStoreCount(param);
                if (total > 0)
                    list = query.getAgentStoreList(param);
                return new AgentPagingDTO<>(new BigDecimal(0), new BigDecimal(0)
                    ,new BigDecimal(0), 0, total, list);
            }
        };
    }

    private static Map<String, Object> setAgtStoreParam(DataFetchingEnvironment env) {
        String agtId = env.getArgument("agtId");
        String storeId = env.getArgument("storeId");//
        String agtType = env.getArgument("agtType");//代理类型
        String agtCode = env.getArgument("agtCode");//代理类型
        String storeName = env.getArgument("storeName");
        String storeType = env.getArgument("storeType");
        String city = env.getArgument("city");
        String isBind = env.getArgument("isBind");
        String startTime = env.getArgument("startTime");
        String endTime = env.getArgument("endTime");

        Integer page = env.getArgument("page");
        Integer size = env.getArgument("size");

        Map<String, Object> param = Maps.newHashMap();
        if (StringUtils.isNotBlank(agtId)) {
            param.put("agtId", agtId);
        }
        if (StringUtils.isNotBlank(agtType)) {
            param.put("agtType", agtType);
        }
        if (StringUtils.isNotBlank(agtCode)) {
            param.put("agtCode", agtCode);
        }
        //storeId
        if (StringUtils.isNotBlank(storeId)) {
            param.put("id", storeId);
        }
        if (StringUtils.isNotBlank(storeType)) {
            param.put("storeType", storeType);
        }
        if (StringUtils.isNotBlank(storeName)) {
            param.put("storeName", storeName);
        }
        if (StringUtils.isNotBlank(city)) {
            param.put("city", city);
        }
        if (StringUtils.isNotBlank(isBind)) {
            param.put("isBind", isBind);
        }
        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DAILY_DATE_PATTERN);
            param.put("startTime", DateUtil.dateFormat(startTime));//日期月份减一个月
        }

        if (page != null && size != null && page >= 0 && size > 0) {
            param.put("page", page);
            param.put("size", size);
        }
        return param;
    }

    /**
     * 某个代理商下的微信用户结算
     *
     * @return PaginationDTO<UserOrderDTO>
     * @Param
     * @author Dong Xifu
     * @date 2019/9/23 1:07 下午
     */
    public static <T> DataFetcher<AgentPagingDTO<UserOrderDTO>> initAgtUserSettle(ComponentProvider componentFactory) {
        AgentQuery aq = componentFactory.getComponent(AgentQuery.class);

        return env -> {
            AgentPagingDTO<UserOrderDTO> pageOrder = new AgentPagingDTO<>();
            Map<String, Object> param = getPagingParam(env);
            String nickName = env.getArgument("nickName");
            param.put("agtId", getAgentId(componentFactory, env).get("agtId"));
            if (StringUtils.isNotBlank(nickName)) {
                param.put("nickName", nickName);
            }
            Map<String, Object> resultMap = new HashMap<>();

            int total = aq.getAgtUserOrderCnt(param);
            List<UserOrderDTO> list = new ArrayList<>();
            if (total > 0) {
                resultMap = aq.getAgtWxUserOrderSum(param);

                pageOrder.setTotal(total);
                list = aq.getAgtUserOrderList(param);
                Base64.Decoder decoder = Base64.getDecoder();
                list.stream().map(cust -> {
                    try {
                        String nickNm = new String(decoder.decode(cust.getNickName().getBytes()), "UTF-8");
                        cust.setNickName(nickNm);
                    } catch (Exception e) {
                        cust.setNickName(cust.getNickName());
                    }
                    return cust;
                }).collect(Collectors.toList());
                pageOrder.setDataList(list);
                return new AgentPagingDTO<>(new BigDecimal(resultMap.get("sumAmount").toString())
                    , new BigDecimal(resultMap.get("agtFee").toString())
                    , new BigDecimal(resultMap.get("vatFee").toString())
                    , Integer.valueOf(resultMap.get("qty").toString()), total, list);
            } else {
                pageOrder.setDataList(list);
            }
            return new AgentPagingDTO<>(new BigDecimal(0), new BigDecimal(0)
                ,new BigDecimal(0), 0, total, list);
        };
    }

    /**
     * SA代理商下的店铺及结算信息
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<AgentPagingDTO<AgentStoreDTO>> initSaAgtStores(ComponentProvider componentFactory) {
        AgentQuery aq = componentFactory.getComponent(AgentQuery.class);
        return env -> {
            Map<String, Object> param = getPagingParam(env);
            String storeName = env.getArgument("storeName");
            String city = env.getArgument("city");
            String agtId = getAgentIdStr(componentFactory,env);
            param.put("agtId", agtId);
            param.put("storeName", storeName);
            param.put("city", city);

            List<AgentStoreDTO> list = new ArrayList<>();

            int total = aq.getSaAgtStoreSettleCount(param);
            Map<String, Object> resultMap = aq.getAgtOrderSum(param);//当前代理商指定月所赚的钱

            if (total > 0) {
                list = aq.getSaAgtStoreSettleList(param);
                return new AgentPagingDTO<>(new BigDecimal(resultMap.get("sumAmount").toString())
                    , new BigDecimal(resultMap.get("agtFee").toString())
                    , new BigDecimal(resultMap.get("vatFee").toString())
                    , Integer.valueOf(resultMap.get("qty").toString()), total, list);
            }
            return new AgentPagingDTO<>(new BigDecimal(0), new BigDecimal(0)
                ,new BigDecimal(0), 0, total, list);
        };
    }


        /**
         * 回显店铺被代理商绑定的信息
         *
         * @return List<AgentAimMapDTO>
         * @Param
         * @author Dong Xifu
         * @date 2019/9/24 4:04 下午
         */
    public static <T> DataFetcher<List<AgentAimMapDTO>> initAgtStoreMap(ComponentProvider componentFactory) {
        AgentQuery aq = componentFactory.getComponent(AgentQuery.class);
        return env -> {
            Map<String, Object> param = new HashMap<>();
            param.put("storeId", env.getArgument("storeId"));
            List<AgentAimMapDTO> list = aq.getAgtAimDtoList(param);
            if (list == null) {
                list = new LinkedList<>();
            }
            return aq.getAgtAimDtoList(param);
        };
    }

    /**
     * 代理商下的店铺结算列表
     *
     * @Param
     * @return List<AgentOrderDTO>
     * @author Dong Xifu
     * @date 2019/10/11 6:04 下午
     */
    public static <T> DataFetcher<AgentPagingDTO<AgentStoreDTO>> initAgentStoreSettle(ComponentProvider componentFactory) {
        return env -> {
            Map<String, Object> param = getAgtOrderParam(env);
            param.put("agtId", getAgentIdStr(componentFactory, env));

            AgentQuery aq = componentFactory.getComponent(AgentQuery.class);
            List<AgentStoreDTO> list = new LinkedList<>();
            int total = aq.getAgentStoreSettleCount(param);
            if (total > 0) {
                //查询代理商下的消费总额
                Map<String, Object> resultMap = aq.getCustOrderSum(param);
                list = aq.getAgentStoreSettleList(param);

                return new AgentPagingDTO<>(new BigDecimal(resultMap.get("sumAmount").toString())
                    , new BigDecimal(resultMap.get("agtFee").toString())
                    , new BigDecimal(resultMap.get("vatFee").toString())
                    , Integer.valueOf(resultMap.get("qty").toString()), total, list);
            } else {
                return new AgentPagingDTO<>(new BigDecimal(0), new BigDecimal(0)
                    ,new BigDecimal(0), 0, 0, list);
            }
        };
    }

    /**
     * 代理商下的用户消费记录
     *
     * @Param
     * @author Dong Xifu
     * @date 2019/9/27 3:31 下午
     */
    public static <T> DataFetcher<AgentPagingDTO<AgentOrderDTO>> initAgentOrders(ComponentProvider componentFactory) {
        return env -> {
            Map<String, Object> param = getAgtOrderParam(env);
            Map<String,Object> agtMap = getAgentId(componentFactory, env);
            param.put("agtId", agtMap.get("agtId"));

            AgentQuery aq = componentFactory.getComponent(AgentQuery.class);
            List<AgentOrderDTO> list = new LinkedList<>();
            String isFinish = env.getArgument("isFinish");
            int total = 0;
            if ("1".equals(isFinish)) {
                 total = aq.getFinishAgtOrderCount(param);
            }else{
                 total = aq.getAgentOrderCount(param);
            }
            if (total > 0) {
                Map<String, Object> resultMap = new HashMap<>();
                //查询ca下的店铺消费总额resultMap = aq.getAgtOr
                resultMap = aq.getAgtStoreOrderSum(param);
                if ("1".equals(isFinish)) {
                    list = aq.getAgentFinishOrderList(param);
                } else {
                    list = aq.getAgentOrderList(param);
                }
                getRealNickNameList(list);
                return new AgentPagingDTO<>(new BigDecimal(resultMap.get("sumAmount").toString())
                    , new BigDecimal(resultMap.get("agtFee").toString())
                    , new BigDecimal(resultMap.get("vatFee").toString())
                    , Integer.valueOf(resultMap.get("qty").toString()), total, list);
            }
            return new AgentPagingDTO<>(new BigDecimal(0), new BigDecimal(0)
                ,new BigDecimal(0), 0, 0, list);
        };
    }

    /**
     * app端Ca下的微信用户订单记录
     *
     * @Param
     * @author Dong Xifu
     * @date 2019/9/27 3:31 下午
     */
    public static <T> DataFetcher<PaginationDTO<AgentOrderDTO>> initCaAgentOrders(ComponentProvider componentFactory) {
        return env -> {
            Map<String, Object> param = getAgtOrderParam(env);
            param.put("agtId",getAgentIdStr(componentFactory,env));

            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");

            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DAILY_DATE_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DAILY_DATE_PATTERN);
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }

            AgentQuery aq = componentFactory.getComponent(AgentQuery.class);
            List<AgentOrderDTO> list = new LinkedList<>();
            int total = aq.getCaAgentOrderSumCount(param);
            if (total > 0) {
                //查询sa下的店铺消费总额
                list = aq.getCaAgentOrderSumList(param);

                getRealNickNameList(list);
                return new PaginationDTO<>(total, list);
            }
            return new PaginationDTO<>(0, list);
        };
    }

    /**
     * app端Sa下的店铺订单记录
     *
     * @Param
     * @author Dong Xifu
     * @date 2019/9/27 3:31 下午
     */
    public static <T> DataFetcher<PaginationDTO<AgentOrderDTO>> initSaAgentOrders(ComponentProvider componentFactory) {
        return env -> {
            Map<String, Object> param = getAgtOrderParam(env);
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");

            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DAILY_DATE_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DAILY_DATE_PATTERN);
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }
            param.put("agtId",getAgentIdStr(componentFactory,env));
            AgentQuery aq = componentFactory.getComponent(AgentQuery.class);
            List<AgentOrderDTO> list = new LinkedList<>();
            int total = aq.getSaAgentOrderSumCount(param);
            if (total > 0) {
                //查询sa下的店铺消费总额
                list = aq.getSaAgentOrderSumList(param);

                return new PaginationDTO<>(total, list);
            }
            return new PaginationDTO<>(0, list);
        };
    }

    /**
     * 设置微信昵称
     *
     * @param list
     * @return
     */
    private static List<AgentOrderDTO> getRealNickNameList(List<AgentOrderDTO> list) {
        Base64.Decoder decoder = Base64.getDecoder();
        list.stream().map(cust -> {
            try {
                String nickNm = new String(decoder.decode(cust.getNickName().getBytes()), "UTF-8");
                cust.setNickName(nickNm);
            } catch (Exception e) {
                cust.setNickName(cust.getNickName());
            }
            return cust;
        }).collect(Collectors.toList());
        return list;
    }


    private static Map<String, Object> getAgtOrderParam(DataFetchingEnvironment env) {
        String orderId = env.getArgument("orderId");
        String storeId = env.getArgument("storeId");
        String agtId = env.getArgument("agtId");
        String agtName = env.getArgument("agtName");
        String agtCode = env.getArgument("agtCode");
        String agtType = env.getArgument("agtType");
        String storeName = env.getArgument("storeName");
        String storeType = env.getArgument("storeType");
        String startTime = env.getArgument("startTime");
        String endTime = env.getArgument("endTime");
        String city = env.getArgument("city");
        String openId = env.getArgument("openId");
        String nickName = env.getArgument("nickName");
        String agtStatus = env.getArgument("agtStatus");
        String isFinish = env.getArgument("isFinish");

        Map<String, Object> param = new HashMap<>();
        param = getPagingParam(env);

        if (StringUtils.isNotBlank(orderId)) {
            param.put("orderId", orderId);
        }
        if (StringUtils.isNotBlank(storeId)) {
            param.put("storeId", env.getArgument("storeId"));
        }
        if (StringUtils.isNotBlank(agtId)) {
            param.put("agtId", agtId);
        }
        if (StringUtils.isNotBlank(storeType)) {
            param.put("storeType", storeType);
        }
        if (StringUtils.isNotBlank(storeName)) {
            param.put("storeName", storeName);
        }
        if (StringUtils.isNotBlank(nickName)) {
            param.put("nickName", nickName);
        }
        if (StringUtils.isNotBlank(openId)) {
            param.put("openId", openId);
        }
        if (StringUtils.isNotBlank(agtName)) {
            param.put("agtName", agtName);
        }
        if (StringUtils.isNotBlank(agtCode)) {
            param.put("agtCode", agtCode);
        }
        if (StringUtils.isNotBlank(agtType)) {
            param.put("agtType", agtType);
        }
        if (StringUtils.isNotBlank(agtStatus)) {
            param.put("agtStatus", agtStatus);
        }
        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DAILY_DATE_PATTERN);
            param.put("startTime", DateUtil.dateFormat(startTime));//日期月份减一个月
        }
        if(StringUtils.isNotBlank(city)){
            param.put("city",city);
        }
        return param;
    }


    /********************************************APP统计**********START********************************************/

    /**
     * 今年总收入
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T>  DataFetcher<AgtAmountQtyDTO> initAgtOrdeSumStats(ComponentProvider componentFactory) {

        AgentQuery aq = componentFactory.getComponent(AgentQuery.class);

        return env -> {
            Map<String, Object> param = Maps.newHashMap();
            param.put("agtId",getAgentIdStr(componentFactory,env));
            return  aq.getAgtYearStatics(param);
        };
    }

    /**
     * app统计图(图按月统计交易金额)
     *
     * @Param
     * @return AgentOrderStaticsDTO
     * @author Dong Xifu
     * @date 2019/10/13 5:32 下午
     */
    public static <T> DataFetcher<AgentOrderStaticsDTO> initAgtOrderStastic(ComponentProvider componentFactory) {
        return env -> {
            Map<String, Object> param = Maps.newHashMap();
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");

            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DAILY_DATE_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DAILY_DATE_PATTERN);
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }
            param.put("agtId",getAgentIdStr(componentFactory,env));

            AgentQuery aq = componentFactory.getComponent(AgentQuery.class);
            List<AgtAmountQtyDTO> agtAmountQtyDTOList = aq.getAgentOrderStatics(param);

            List<AgentOrderStaticsDTO.AmountDate> agtAmountAndDateList = agtAmountQtyDTOList.stream().map(a -> {
                AgentOrderStaticsDTO.AmountDate dto = new AgentOrderStaticsDTO.AmountDate();
                dto.setAgtSumAmount(a.getAgtSumAmount());
                dto.setDate(a.getDate());
                return dto;
            }).collect(Collectors.toList());

            List<AgentOrderStaticsDTO.QtyDate> qtyAndDateList = agtAmountQtyDTOList.stream().map(a -> {
                AgentOrderStaticsDTO.QtyDate dto = new AgentOrderStaticsDTO.QtyDate();
                dto.setQty(a.getQty());
                dto.setDate(a.getDate());
                return dto;
            }).collect(Collectors.toList());

            return getAmountAndQtyOfMonth(agtAmountAndDateList,qtyAndDateList,startTime,endTime);
        };
    }

    //按月统计2018-02
    private static AgentOrderStaticsDTO getAmountAndQtyOfMonth(List<AgentOrderStaticsDTO.AmountDate> agtAmountAndDateList,
                                                               List<AgentOrderStaticsDTO.QtyDate> qtyAndDateList, String startTime, String endTime){
        ArrayList<String> listDate = new ArrayList<>();
        int monthStart = Integer.valueOf(startTime.substring(5,7));
        int monthEnd = Integer.valueOf(endTime.substring(5,7));

        for (int i=0;i<=monthEnd-monthStart;i++) {
            listDate.add(startTime.substring(0,4)+"-"+String.format("%02d",monthStart+i));
        }

        addAmountAndDateForDate(startTime, endTime, agtAmountAndDateList, listDate);//数据为空时封装日期
        addQtyAndDateForDate(qtyAndDateList, listDate);//数据为空时封装日期

        return setAmountStatsDto(agtAmountAndDateList,qtyAndDateList,listDate);
    }

    /**
     * 将没有日期的天数放入数据(金额)
     *
     * @Param
     * @return List<AgtAmountAndDateDTO>
     * @author Dong Xifu
     * @date 2019/10/8 5:52 下午
     */
    private static List<AgentOrderStaticsDTO.AmountDate> addAmountAndDateForDate(String startTime, String endTime
                                                , List<AgentOrderStaticsDTO.AmountDate> agtAmountAndDateList
                                                , ArrayList<String> dataAll) {
        dataAll.stream().forEach(str ->{
            AgentOrderStaticsDTO.AmountDate dto =  agtAmountAndDateList.stream().filter(a->a.getDate().equals(str)).findFirst().orElseGet(()->null);
            if(dto==null){
                AgentOrderStaticsDTO.AmountDate em = new AgentOrderStaticsDTO.AmountDate();
                em.setDate(str);
                em.setAgtSumAmount(new BigDecimal(0));
                agtAmountAndDateList.add(em);
            }

        });
        Collections.sort(agtAmountAndDateList,(s1,s2)->{
            int i = s1.getDate().compareTo(s2.getDate());
            if(i>0){
                return 1;
            }else if(i==0){
                return  0;
            }else {
                return -1;
            }

        });
        return agtAmountAndDateList;
    }

    /**
     * 将没有日期的天数放入数据(数量)
     *
     * @Param
     * @return List<AgtQtyAndDateDTO>
     * @author Dong Xifu
     * @date 2019/10/8 5:51 下午
     */
    private static List<AgentOrderStaticsDTO.QtyDate> addQtyAndDateForDate( List<AgentOrderStaticsDTO.QtyDate> qtyAndDateList, ArrayList<String> dataAll) {
        dataAll.stream().forEach(str ->{

            AgentOrderStaticsDTO.QtyDate dto =  qtyAndDateList.stream().filter(a->a.getDate().equals(str)).findFirst().orElseGet(()->null);
            if(dto==null){
                AgentOrderStaticsDTO.QtyDate em = new AgentOrderStaticsDTO.QtyDate();
                em.setDate(str);
                em.setQty(0);
                qtyAndDateList.add(em);
            }
        });

        Collections.sort(qtyAndDateList,(s1,s2)->{
            int i = s1.getDate().compareTo(s2.getDate());
            if(i>0){
                return 1;
            }else if(i==0){
                return  0;
            }else {
                return -1;
            }
        });

        return qtyAndDateList;
    }

    //组装dto
    private static AgentOrderStaticsDTO setAmountStatsDto(List<AgentOrderStaticsDTO.AmountDate> payAmountAndDateList
                                                    ,List<AgentOrderStaticsDTO.QtyDate> qtyAndDateList
                                                    ,ArrayList<String> listDate
                                                    ) {
        AgentOrderStaticsDTO dto = new AgentOrderStaticsDTO();
        dto.setAgtAmountList(payAmountAndDateList);
        dto.setQtyList(qtyAndDateList);
        dto.setDateList(listDate);
        return dto;
    }


    /********************************************APP统计**********END*******************************************/




    /********************************************平台代理商利润统计******START***************************************/

    /**
     * 平台总金额 代理商总金额 平台净利润
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<List<PlAgtAmountDTO>> initPlAgtAmountSumStats(ComponentProvider componentFactory) {

        AgentQuery aq = componentFactory.getComponent(AgentQuery.class);

        return env -> {
            return  aq.getPlAgtAmountStatsSum();
        };
    }


    /**
     * 每月统计平台总金额 代理商总金额 平台净利润
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<PlAgtAmountStaticsDTO> initPlAgtAmountStatsByDate(ComponentProvider componentFactory) {
        AgentQuery aq = componentFactory.getComponent(AgentQuery.class);

        return env->{

            Map<String, Object> param = Maps.newHashMap();
            param.put("agtId",getAgentIdStr(componentFactory,env));
            String dateType = env.getArgument("dateType");
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");

            param.put("dateType", dateType);
            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DAILY_DATE_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DAILY_DATE_PATTERN);
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }

            List<PlAgtAmountDTO>  feeList = aq.getPlAmountStatsByDate(param);

            if (StringUtils.isBlank(dateType)) {
                throw new BizException(ErrorCode.PARAM_MISSING);
            }

            if(PlAgtAmountStaticsDTO.DATE_TYPE_DAY.equals(dateType)){
                return getFeeOfDay(feeList,startTime,endTime);
            }else if(PlAgtAmountStaticsDTO.DATE_TYPE_WEEK.equals(dateType)){
                return getFeeOfWeek(feeList,startTime,endTime);
            }else {
                return getPlAgtAmountOfMonth(feeList,startTime,endTime);
            }
        };
    }

    //按天统计
    private static PlAgtAmountStaticsDTO getFeeOfDay(List<PlAgtAmountDTO> feeList
                                                           , String startTime, String endTime)
        throws ParseException {
        ArrayList<String> dataAll = DateUtil.findDataAll(startTime, endTime, 1);

        addAmountOfDate(feeList, dataAll);//数据为空时封装日期

        return addPlAgtAmountDate(feeList,dataAll);
    }

    //按周统计
    private static PlAgtAmountStaticsDTO getFeeOfWeek(List<PlAgtAmountDTO> feeList
                                                            , String startTime, String endTime)
        throws ParseException {

        ArrayList<String> dateList = DateUtil.getFirstDayOfWeekList(startTime, endTime);
        feeList.stream().forEach(am-> am.setDate(DateUtil.getFirstDayOfWeek(Integer.valueOf(am.getDate().substring(0,4)),Integer.valueOf(am.getDate().substring(4,6))  )));
        addAmountOfDate(feeList, dateList);//数据为空时封装日期

        return addPlAgtAmountDate(feeList,dateList);
    }


    //按月统计  获取年初到指定日期的月份
    private static PlAgtAmountStaticsDTO getPlAgtAmountOfMonth(List<PlAgtAmountDTO> feeList
                                                            ,String startTime, String endTime){
        ArrayList<String> listDate = new ArrayList<>();
        int monthStart = Integer.valueOf(startTime.substring(5,7));
        int monthEnd = Integer.valueOf(endTime.substring(5,7));

        for (int i=0;i<=monthEnd-monthStart;i++) {
            listDate.add(startTime.substring(0,4)+"-"+String.format("%02d",monthStart+i));
        }
        return  addAmountOfDate(feeList,listDate);//数据为空时封装日期
    }

    //将没有数据的条数插入日期
    private static PlAgtAmountStaticsDTO addAmountOfDate(List<PlAgtAmountDTO> feeList,ArrayList<String> dateList) {
        for (String date : dateList) {
            PlAgtAmountDTO plDto = feeList.stream().filter(d -> d.getDate().equals(date)).findFirst().orElseGet(()->null);
            if (plDto == null) {
                PlAgtAmountDTO dto = new PlAgtAmountDTO(date,new BigDecimal(0),new BigDecimal(0),new BigDecimal(0), new BigDecimal(0));
                feeList.add(dto);
            }
        }
        //排序
        sortListPlAgtAmounts(feeList);

        return addPlAgtAmountDate(feeList,dateList);
    }

    //排序
    private static List<PlAgtAmountDTO> sortListPlAgtAmounts(List<PlAgtAmountDTO> feeList) {
        Collections.sort(feeList,(s1,s2)->{
            int i = s1.getDate().compareTo(s2.getDate());
            if(i>0){
                return 1;
            }else if(i==0){
                return  0;
            }else {
                return -1;
            }
        });
        return feeList;
    }

    //封装统计平台、代理商、平台利润dto
    private static PlAgtAmountStaticsDTO addPlAgtAmountDate(
        List<PlAgtAmountDTO> feeList, ArrayList<String> dateList) {
        PlAgtAmountStaticsDTO dto = new PlAgtAmountStaticsDTO();

        List<PlAgtFeeDTO> plFeeList = new LinkedList<>();
        List<PlAgtFeeDTO> saFeeList = new LinkedList<>();
        List<PlAgtFeeDTO> caFeeList = new LinkedList<>();
        List<PlAgtFeeDTO> profitList = new LinkedList<>();
        feeList.stream().forEach(d->{
            plFeeList.add(new PlAgtFeeDTO(d.getDate(),d.getPlFee()));
            saFeeList.add(new PlAgtFeeDTO(d.getDate(), d.getSaFee()));
            caFeeList.add(new PlAgtFeeDTO(d.getDate(), d.getCaFee()));
            profitList.add(new PlAgtFeeDTO(d.getDate(), d.getProfit()));
        });
        dto.setDateList(dateList);
        dto.setPlFeeList(plFeeList);
        dto.setSaFeeList(saFeeList);
        dto.setCaFeeList(caFeeList);
        dto.setProfitList(profitList);
        return dto;
    }

    /**********************************平台代理商利润统计***************END*********************************/


    /**
     * 代理商销售金额排行前十名
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<List<AgentOrderDTO>> initAgtFeeRank(ComponentProvider componentFactory) {

        AgentQuery aq = componentFactory.getComponent(AgentQuery.class);

        return env -> {
            String agtType = env.getArgument("agtType");//代理类型
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");
            Map<String, Object> param = SetParamUtil.setTimeParam(startTime, endTime,false,new HashMap<>());
            param.put("agtType", agtType);
            return  aq.agtFeeRank(param);
        };
    }



}