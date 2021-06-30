package com.basoft.eorder.interfaces.query.ghraphql;

import ch.qos.logback.core.CoreConstants;
import com.basoft.eorder.application.*;
import com.basoft.eorder.application.base.BaseService;
import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.application.framework.ComponentProvider;
import com.basoft.eorder.application.framework.QueryHandler;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Advice;
import com.basoft.eorder.domain.model.Review;
import com.basoft.eorder.domain.model.activity.discount.Discount;
import com.basoft.eorder.domain.model.store.StoreChargeInfoRecord;
import com.basoft.eorder.interfaces.query.*;
import com.basoft.eorder.interfaces.query.activity.discount.DiscountDTO;
import com.basoft.eorder.interfaces.query.activity.discount.DiscountDisplayDTO;
import com.basoft.eorder.interfaces.query.activity.discount.DiscountQuery;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelDTO;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelQuery;
import com.basoft.eorder.interfaces.query.retail.api.RetailProductGroupMapDTO;
import com.basoft.eorder.interfaces.query.retail.api.RetailProductQuery;
import com.basoft.eorder.interfaces.query.topic.BaseTopicDTO;
import com.basoft.eorder.interfaces.query.topic.BaseTopicQuery;
import com.basoft.eorder.util.DateUtil;
import com.basoft.eorder.util.StoreUtil;
import com.google.common.collect.Maps;
import graphql.*;
import graphql.schema.*;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

public class DefaultGraphqlQueryInit {
    private static Logger logger = LoggerFactory.getLogger(DefaultGraphqlQueryInit.class);

    /**
     * UserSession中获取当前登录门店信息
     * 获取store String类型
     *
     * @param env
     * @param storeIdProp
     * @return java.lang.String
     * @author Dong Xifu
     * @date 2019/1/25 上午10:52
     */
    private String getStoreIdStr(DataFetchingEnvironment env, String storeIdProp) {
        //如果 Context非空时， 优先获取context里头的值
        Map<String, Object> contextMap = env.getContext();
        if (contextMap != null) {
            UserSession us = (UserSession) contextMap.get(AppConfigure.BASOFT_USER_SESSION_PROP);
            if (us != null) {
                logger.debug("Get StoreID from userSeession :" + us.getStoreId());
                return String.valueOf(us.getStoreId());
            }

            final WxSession o = (WxSession) contextMap.get(AppConfigure.BASOFT_WX_SESSION_PROP);
            if (o != null) {
                logger.debug("Get StoreId from WxSession :" + o.getStoreId());
                return String.valueOf(o.getStoreId());
            }
        }
        return env.getArgument(storeIdProp);
    }

    /**
     * UserSession中获取当前登录门店信息
     *
     * @param env
     * @param storeIdProp
     * @return
     */
    private Long getStoreId(DataFetchingEnvironment env, String storeIdProp) {
        //如果 Context非空时， 优先获取context里头的值
        Map<String, Object> contextMap = env.getContext();
        if (contextMap != null) {
            UserSession us = (UserSession) contextMap.get(AppConfigure.BASOFT_USER_SESSION_PROP);
            if (us != null) {
                logger.debug("Get StoreID from userSeession :" + us.getStoreId());
                return us.getStoreId();
            }

            final WxSession o = (WxSession) contextMap.get(AppConfigure.BASOFT_WX_SESSION_PROP);
            if (o != null) {
                logger.debug("Get StoreId from WxSession :" + o.getStoreId());
                return o.getStoreId();
            }
        }
        return env.getArgument(storeIdProp);
    }

    private <T> DataFetcher<ProductSkuDTO> initProductSku(ComponentProvider componentFactory) {
        ProductQuery pq = componentFactory.getComponent(ProductQuery.class);

        return environment -> {
            long id = environment.getArgument("id");
            return pq.getProductSkuById(id);
        };
    }

    private <T> DataFetcher<List<ProductSkuDTO>> initProductSkus(ComponentProvider componentFactory) {
        ProductQuery pq = componentFactory.getComponent(ProductQuery.class);

        return environment -> {
            String name = environment.getArgument("name");
            Integer page = environment.getArgument("page");
            Integer size = environment.getArgument("size");
            Long categoryId = environment.getArgument("categoryId");
            Long productId = environment.getArgument("productId");

            Long storeId = getStoreId(environment, "storeId");

            Map<String, Object> param = Maps.newHashMap();

            if (StringUtils.isNotBlank(name)) {
                param.put("nameKor", name);
            }

            if (categoryId != null && categoryId > 0) {
                param.put("categoryId", categoryId);
            }

            if (storeId != null && storeId > 0) {
                param.put("storeId", storeId);
            }

            if (productId != null && productId > 0) {
                param.put("productId", productId);
            }

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }

            return pq.getProductSkuListByMap(param);
        };
    }

    /**
     * @param componentFactory
     * @param <T>
     * @return
     * @author liminzhe
     */
    private <T> DataFetcher<ProductDTO> initProduct(ComponentProvider componentFactory) {
        ProductQuery pq = componentFactory.getComponent(ProductQuery.class);

        return env -> {
            // 查询参数
            long id = env.getArgument("id");
            ProductDTO product = pq.getProductById(id);
            if (product != null) {
                Map<String, Object> param = Maps.newHashMap();
                Long storeId = getStoreId(env, "storeId");
                if (storeId != null) {
                    param.put("storeId", storeId);
                }
                param.put("productId", id);
                final List<ProductSkuDTO> productSkuListByMap = pq.getProductSkuListByMap(param);
                product.setPsdList(productSkuListByMap);
            }
            return product;
        };
    }


    /**
     * @param componentFactory
     * @param <T>
     * @return DataFecher<ProductDTO>
     * @author woonill
     */
    private <T> DataFetcher<PaginationDTO<ProductDTO>>  initProducts(ComponentProvider componentFactory) {
        ProductQuery pq = componentFactory.getComponent(ProductQuery.class);

        return env -> {
            // 查询参数
            String name = env.getArgument("name");
            String storeName = env.getArgument("storeName");
            String city = env.getArgument("city");
            String areaNm = env.getArgument("areaNm");
            String nameKor = env.getArgument("nameKor");
            String nameChn = env.getArgument("nameChn");
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");
            String categoryIdStr = env.getArgument("categoryId");
            String statusStr = env.getArgument("status");
            String isDeposit = env.getArgument("isDeposit");

            Long storeId = getStoreId(env, "storeId");
            String storeType = env.getArgument("storeType");


            String orderBy = env.getArgument("orderBy");

            Map<String, Object> param = Maps.newHashMap();

            if (StringUtils.isNotBlank(orderBy)) {
                param.put("orderBy", orderBy);
            }

            if (StringUtils.isNotBlank(nameKor)) {
                param.put("nameKor", nameKor);
            }
            if (StringUtils.isNotBlank(nameChn)) {
                param.put("nameChn", nameChn);
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
            if (StringUtils.isNotBlank(areaNm)) {
                param.put("areaNm", areaNm);
            }

            if (StringUtils.isNotBlank(categoryIdStr)) {
                param.put("categoryId", Long.parseLong(categoryIdStr));
            }

            if (storeId != null && storeId > 0) {
                param.put("storeId", storeId);
            }

            if (StringUtils.isNotBlank(statusStr)) {
                param.put("status", Integer.parseInt(statusStr));
            }
            if (StringUtils.isNotBlank(isDeposit)) {
                param.put("isDeposit", isDeposit);
            }else {
                param.put("isDeposit", "0");
            }

            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }


            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }

            final List<ProductDTO> productListByMap = pq.getProductListByMap(param);
            int totalCount = pq.getProductCountByMap(param);
            return new PaginationDTO(totalCount, productListByMap);

        };
    }

    private <T> DataFetcher<List<ProductGroupMapDTO>> initProductGroupMaps(ComponentProvider componentFactory) {
        ProductQuery pq = componentFactory.getComponent(ProductQuery.class);
        return env -> {
            Long storeId = getStoreId(env, "storeId");
            String recommend =  env.getArgument("recommend");
            String status =  env.getArgument("status");

            Map<String, Object> param = Maps.newHashMap();

            if (storeId != null && storeId > 0) {
                param.put("storeId", storeId);
            }

            if ( StringUtils.isNotBlank(recommend)) {
                param.put("recommend", recommend);
            }

            if ( StringUtils.isNotBlank(status)) {
                param.put("status", status);
            }

            List<ProductGroupMapDTO> pgmList = pq.getProductGroupMapListByMap(param);
            return pgmList;

        };
    }

    private <T> DataFetcher<List<RetailProductGroupMapDTO>> initRetailProductGroupMaps(ComponentProvider componentProvider) {
        RetailProductQuery retailProductQuery = componentProvider.getComponent(RetailProductQuery.class);
        return env -> {
            Long storeId = getStoreId(env, "storeId");

            Map<String, Object> param = Maps.newHashMap();
            if (storeId != null && storeId > 0) {
                param.put("storeId", storeId);
            }

            List<RetailProductGroupMapDTO> pgmList = retailProductQuery.getRetailProductGroupMapListByStoreId(param);
            return pgmList;
        };
    }

    private <T> DataFetcher<PaginationDTO<ProductGroupDTO>> initProductGroup(ComponentProvider componentFactory) {

        ProductQuery pq = componentFactory.getComponent(ProductQuery.class);

        return env -> {

            // 查询参数
            Long storeId = getStoreId(env, "storeId");
            String storeName = env.getArgument("storeName");
            String storeType = env.getArgument("storeType");
            String city = env.getArgument("city");
            String areaNm = env.getArgument("areaNm");
            String nameChn = env.getArgument("nameChn");
            String nameKor = env.getArgument("nameKor");
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");
            String orderBy = env.getArgument("orderBy");
            String statusStr = env.getArgument("status");

            Map<String, Object> param = Maps.newHashMap();
            if (storeId != null && storeId > 0) {
                param.put("storeId", storeId);
            }
            if (StringUtils.isNotBlank(storeName)) {
                param.put("storeName", storeName);
            }
            if (StringUtils.isNotBlank(storeType)) {
                param.put("storeType", storeType);
            }
            if (StringUtils.isNotBlank(areaNm)) {
                param.put("areaNm", areaNm);
            }
            if (StringUtils.isNotBlank(city)) {
                param.put("city", city);
            }

            if (StringUtils.isNotBlank(nameChn)) {
                param.put("nameChn", nameChn);
            }
            if (StringUtils.isNotBlank(nameKor)) {
                param.put("nameKor", nameKor);
            }if (StringUtils.isNotBlank(statusStr)) {
                param.put("status", Integer.parseInt(statusStr));
            }

            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);

                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }
            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }

            if (StringUtils.isNotBlank(orderBy)) {
                param.put("orderBy", orderBy);
            }


            int totalCount = pq.getproductGroupCount(param);
            List<ProductGroupDTO> pgdList = pq.getProductGroupListByMap(param);
            return new PaginationDTO(totalCount, pgdList);
        };
    }

    /**
     * @param  componentFactory
     * @return List<OrderSumStatsDTO>
     * @describe 订单总金额统计
     * @author Dong Xifu
     * @date 2019/2/12 上午11:12
     */
    private <T> DataFetcher<List<OrderSumStatsDTO>> initOrdeSumStats(ComponentProvider componentFactory) {

        OrderQuery oq = componentFactory.getComponent(OrderQuery.class);

        return env -> {

            // 查询参数
            Long storeId = getStoreId(env, "storeId");

            Map<String, Object> param = Maps.newHashMap();

            if (storeId != null && storeId > 0) {
                param.put("storeId", storeId);
            }


            return oq.getOrderSumStatisList(param);
        };
    }

    private <T> DataFetcher<List<OrderSumRankStoreStatsDTO>> initOrderSumStoreRankStats(ComponentProvider componentFactory) {
        OrderQuery oq = componentFactory.getComponent(OrderQuery.class);

        return env -> {
            // 查询参数
            Map<String, Object> param = Maps.newHashMap();
            String storeId = env.getArgument("storeId");
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");
            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);

                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }



            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }

            return oq.getOrderSumRankStoreStatisList(param);
        };

    }



    /**
     * @param  componentFactory
     * @return OrderSumStatsDTO
     * @describe 产品排行
     * @author Dong Xifu
     * @date 2019/2/12 下午5:27
     */
    private <T> DataFetcher<List<ProductSaleRankDTO>> initProductSaleRankStats(ComponentProvider componentFactory) {

        OrderQuery oq = componentFactory.getComponent(OrderQuery.class);

        return env -> {
            // 查询参数
            Long storeId = getStoreId(env, "storeId");
            Map<String, Object> param = Maps.newHashMap();
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");
            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);

                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }

            if (storeId != null && storeId > 0) {
                param.put("storeId", storeId);
            }

            return oq.getProductSaleRankList(param);
        };
    }
    /**
     * @param  componentFactory
     * @return List<CategorySaleRankDTO>
     * @describe 分类销量统计
     * @author Dong Xifu
     * @date 2019/2/14 下午2:08
     */
    private <T> DataFetcher<List<CategorySaleRankDTO>> initCategorySaleRankStats(ComponentProvider componentFactory) {
        OrderQuery oq = componentFactory.getComponent(OrderQuery.class);

        return env -> {
            // 查询参数
            String storeId = env.getArgument("storeId");
            Map<String, Object> param = Maps.newHashMap();


            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");
            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);

                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }

            return oq.getCategorySaleRankList(param);
        };
    }



    /**
     * @param  componentFactory
     * @return
     * @describe 按日期统计销售金额
     * @author Dong Xifu
     * @date 2019/2/18 上午9:42
     */
    private <T> DataFetcher<OrderByDateAmountStatsDTO> initOrderByDateAmountStats(ComponentProvider componentFactory) {
        OrderQuery oq = componentFactory.getComponent(OrderQuery.class);

        return (DataFetchingEnvironment env) -> {
            // 查询参数
            String storeId = getStoreIdStr(env, "storeId");
            Map<String, Object> param = Maps.newHashMap();


            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");
            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }else{
               throw new BizException(ErrorCode.PARAM_MISSING);
            }

            String dateType = env.getArgument("dateType");
            param.put("dateType", dateType);

            List<PayAmountAndDateDTO> payAmountAndDateList = oq.getPayAmountAndDate(param);
            List<QtyAndDateDTO> qtyAndDateList =  oq.getQtyAndDate(param);
            BigDecimal payAmountSum = oq.getSumPayAmountForDate(param);


            if(OrderByDateAmountStatsDTO.DATE_TYPE_DAY.equals(dateType)){
                return getAmountAndQtyOfDay(payAmountAndDateList,qtyAndDateList,payAmountSum,startTime,endTime);
            }else if(OrderByDateAmountStatsDTO.DATE_TYPE_WEEK.equals(dateType)){
                return getAmountAndQtyOfWeek(payAmountAndDateList,qtyAndDateList,payAmountSum,startTime,endTime);
            }else if(OrderByDateAmountStatsDTO.DATE_TYPE_MONTH.equals(dateType)){
                return getAmountAndQtyOfMonth(payAmountAndDateList,qtyAndDateList,payAmountSum,startTime,endTime);
            }
            return  null;
        };
    }

    /**
     * @param componentFactory
     * @param <T>
     * @return
     * @author dikim
     */
    private <T> DataFetcher<ShipPointDTO> initShipPoint(ComponentProvider componentFactory) {

    	ShipPointQuery sq = componentFactory.getComponent(ShipPointQuery.class);

        return env -> {
            long shipPointid = env.getArgument("shipPointid");
            ShipPointDTO dto = sq.getShipPointById(shipPointid);
            return dto;
        };
    }

    /**
     * @param componentFactory
     * @param <T>
     * @return
     * @author liminzhe
     * @Descrip 鏌ヨ搴楅摵鍒楄〃
     */
    private <T> DataFetcher<PaginationDTO<ShipPointDTO>> initShipPoints(ComponentProvider componentFactory) {

    	ShipPointQuery bq = componentFactory.getComponent(ShipPointQuery.class);

        return env -> {

            // 鏌ヨ鍙傛暟
            
            String shipPointid = env.getArgument("shipPointid");
            String shipPointnm = env.getArgument("shipPointnm");
            String areaId = env.getArgument("areaId");
            String areaName = env.getArgument("areaName");
            String addr = env.getArgument("addr");
            String addrCn = env.getArgument("addrCn");
            String lat = env.getArgument("lat");
            String lon = env.getArgument("lon");
            String phoneNo = env.getArgument("phoneNo");
            String cmt = env.getArgument("cmt");
            String cmtCn = env.getArgument("cmtCn");
            String status = env.getArgument("status");
            
            
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");

            Map<String, Object> param = Maps.newHashMap();

            if (StringUtils.isNotBlank(shipPointid)) {
                param.put("shipPointid", shipPointid);
            }
            if (StringUtils.isNotBlank(shipPointnm)) {
                param.put("shipPointnm", shipPointnm);
            }
            if (StringUtils.isNotBlank(areaId)) {
                param.put("areaId", areaId);
            }
            if (StringUtils.isNotBlank(areaName)) {
                param.put("areaName", areaName);
            }
            if (StringUtils.isNotBlank(addr)) {
                param.put("addr", addr);
            }
            if (StringUtils.isNotBlank(addrCn)) {
                param.put("addrCn", addrCn);
            }
            if (StringUtils.isNotBlank(lat)) {
                param.put("lat", lat);
            }
            if (StringUtils.isNotBlank(lon)) {
                param.put("lon", lon);
            }
            if (StringUtils.isNotBlank(phoneNo)) {
                param.put("phoneNo", phoneNo);
            }
            if (StringUtils.isNotBlank(cmt)) {
                param.put("cmt", cmt);
            }
            if (StringUtils.isNotBlank(cmtCn)) {
                param.put("cmtCn", cmtCn);
            }
            if (StringUtils.isNotBlank(status)) {
                param.put("status", status);
            }

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }

            PaginationDTO<ShipPointDTO> pageShipPoint = new PaginationDTO<>();
            int dataCount = bq.getShipPointCount(param);
            final List<ShipPointDTO> listByMap = bq.getShipPointListByMap(param);
            if(dataCount>0){
            	pageShipPoint.setTotal(dataCount);
            	pageShipPoint.setDataList(listByMap);
                return pageShipPoint;
            }

            return new PaginationDTO<>(dataCount, listByMap);
        };
    }        
    
    /**
     * @param componentFactory
     * @param <T>
     * @return
     * @author liminzhe
     * @Descrip 鏌ヨ搴楅摵鍒楄〃
     */
    private <T> DataFetcher<PaginationDTO<OrderDTO>> initMyContacts(ComponentProvider componentFactory) {

        OrderQuery bq = componentFactory.getComponent(OrderQuery.class);

        return env -> {

            // 鏌ヨ鍙傛暟
            
            String openId = env.getArgument("openId");
            String custNm = env.getArgument("custNm");
            String countryNo = env.getArgument("countryNo");
            String mobile = env.getArgument("mobile");
            String nmLastEn = env.getArgument("nmLastEn");
            String nmFirstEn = env.getArgument("nmFirstEn");
            
            
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");

            Map<String, Object> param = Maps.newHashMap();

            if (StringUtils.isNotBlank(openId)) {
                param.put("openId", openId);
            }
            if (StringUtils.isNotBlank(custNm)) {
                param.put("custNm", custNm);
            }
            if (StringUtils.isNotBlank(countryNo)) {
                param.put("countryNo", countryNo);
            }
            if (StringUtils.isNotBlank(mobile)) {
                param.put("mobile", mobile);
            }
            if (StringUtils.isNotBlank(nmLastEn)) {
                param.put("nmLastEn", nmLastEn);
            }
            if (StringUtils.isNotBlank(nmFirstEn)) {
                param.put("nmFirstEn", nmFirstEn);
            }

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }

            PaginationDTO<OrderDTO> pageShipPoint = new PaginationDTO<>();
            int dataCount = bq.getMyContactCount(param);
            final List<OrderDTO> listByMap = bq.getMyContactListByMap(param);
            if(dataCount>0){
            	pageShipPoint.setTotal(dataCount);
            	pageShipPoint.setDataList(listByMap);
                return pageShipPoint;
            }
            return new PaginationDTO<>(dataCount, listByMap);
        };
    }     

    //按天统计
    private OrderByDateAmountStatsDTO getAmountAndQtyOfDay(List<PayAmountAndDateDTO> payAmountAndDateList,
                                                           List<QtyAndDateDTO> qtyAndDateList, BigDecimal payAmountSum, String startTime, String endTime)
        throws ParseException {
        ArrayList<String> dataAll = DateUtil.findDataAll(startTime, endTime, 1);

        addAmountAndDateForDate(startTime, endTime, payAmountAndDateList, dataAll);//数据为空时封装日期
        addQtyAndDateForDate(qtyAndDateList, dataAll);//数据为空时封装日期

        return setAmountStatsDto(payAmountAndDateList,qtyAndDateList,dataAll,payAmountSum);
    }

    //按周统计
    private OrderByDateAmountStatsDTO getAmountAndQtyOfWeek(List<PayAmountAndDateDTO> payAmountAndDateList,
                                                            List<QtyAndDateDTO> qtyAndDateList, BigDecimal payAmountSum, String startTime, String endTime)
        throws ParseException {

        ArrayList<String> listDate = DateUtil.getFirstDayOfWeekList(startTime, endTime);
        payAmountAndDateList.stream().forEach(am-> am.setDate(DateUtil.getFirstDayOfWeek(Integer.valueOf(am.getDate().substring(0,4)),Integer.valueOf(am.getDate().substring(4,6))  )));
        qtyAndDateList.stream().forEach(am-> am.setDate(DateUtil.getFirstDayOfWeek(Integer.valueOf(am.getDate().substring(0,4)),Integer.valueOf(am.getDate().substring(4,6)) )));

        addAmountAndDateForDate(startTime, endTime, payAmountAndDateList, listDate);//数据为空时封装日期
        addQtyAndDateForDate( qtyAndDateList, listDate);//数据为空时封装日期

        return setAmountStatsDto(payAmountAndDateList,qtyAndDateList,listDate,payAmountSum);
    }

    //按月统计2018-02
    private OrderByDateAmountStatsDTO getAmountAndQtyOfMonth(List<PayAmountAndDateDTO> payAmountAndDateList,
                                                            List<QtyAndDateDTO> qtyAndDateList, BigDecimal payAmountSum, String startTime, String endTime){
        ArrayList<String> listDate = new ArrayList<>();
        int monthStart = Integer.valueOf(startTime.substring(5,7));
        int monthEnd = Integer.valueOf(endTime.substring(5,7));

        for (int i=0;i<=monthEnd-monthStart;i++) {
            listDate.add(startTime.substring(0,4)+"-"+String.format("%02d",monthStart+i));
        }

        addAmountAndDateForDate(startTime, endTime, payAmountAndDateList, listDate);//数据为空时封装日期
        addQtyAndDateForDate(qtyAndDateList, listDate);//数据为空时封装日期

        return setAmountStatsDto(payAmountAndDateList,qtyAndDateList,listDate,payAmountSum);
    }


    //组装dto
    private OrderByDateAmountStatsDTO setAmountStatsDto(List<PayAmountAndDateDTO> payAmountAndDateList, List<QtyAndDateDTO> qtyAndDateList,
                                                        ArrayList<String> listDate, BigDecimal payAmountSum) {
        OrderByDateAmountStatsDTO dto = new OrderByDateAmountStatsDTO();
        dto.setPayAmountList(payAmountAndDateList);
        dto.setQtyList(qtyAndDateList);
        dto.setDateList(listDate);
        dto.setPayAmountSum(payAmountSum);
        return dto;
    }


    /**
     * @param  startTime, endTime, payAmountAndDateList, dataAll
     * @return java.util.List<com.basoft.eorder.interfaces.query.PayAmountAndDateDTO>
     * @describe 将当天没有订单的数据插入日期(订单金额)
     * @author Dong Xifu
     * @date 2019/2/18 上午11:26
     */
    private List<PayAmountAndDateDTO> addAmountAndDateForDate(String startTime, String endTime, List<PayAmountAndDateDTO> payAmountAndDateList, ArrayList<String> dataAll) {
        dataAll.stream().forEach(str ->{
            PayAmountAndDateDTO dto =  payAmountAndDateList.stream().filter(a->a.getDate().equals(str)).findFirst().orElseGet(()->null);
            if(dto==null){
                PayAmountAndDateDTO em = new PayAmountAndDateDTO();
                em.setDate(str);
                em.setPayAmount(new BigDecimal(0));
                payAmountAndDateList.add(em);
            }

        });
        Collections.sort(payAmountAndDateList,(s1,s2)->{
            int i = s1.getDate().compareTo(s2.getDate());
            if(i>0){
                return 1;
            }else if(i==0){
                return  0;
            }else {
                return -1;
            }

        });

        return payAmountAndDateList;

    }

    /**
     * @param    qtyAndDateList, dataAll
     * @return java.util.List<com.basoft.eorder.interfaces.query.QtyAndDateDTO>
     * @describe 将当天没有订单的数据插入日期（订单数量）
     * @author Dong Xifu
     * @date 2019/2/18 下午1:16
     */
    private List<QtyAndDateDTO> addQtyAndDateForDate( List<QtyAndDateDTO> qtyAndDateList, ArrayList<String> dataAll) {
        dataAll.stream().forEach(str ->{

            QtyAndDateDTO dto =  qtyAndDateList.stream().filter(a->a.getDate().equals(str)).findFirst().orElseGet(()->null);
            if(dto==null){
                QtyAndDateDTO em = new QtyAndDateDTO();
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


    /**
     * 封装没有日期的酒店
     *
     * @Param ArrayList<String> dataAll List<InventoryHotelDTO> inventoryHotelDTOList
     * @return java.util.List<InventoryHotelDTO>
     * @author Dong Xifu
     * @date 2019/8/20 下午2:43
     */
    private List<InventoryHotelDTO> setInvDateHotel( List<InventoryHotelDTO> inventoryHotelDTOList, ArrayList<String> dataAll) {
        dataAll.stream().forEach(str -> {
            for(InventoryHotelDTO dto:inventoryHotelDTOList) {
                InventoryHotelDTO.InvDateAndInv dateAndInvTotal = dto.getInvDateAndTotalList().stream().filter(a -> a.getInvDate().equals(str)).findFirst().orElseGet(() -> null);
                if (dateAndInvTotal == null) {
                    InventoryHotelDTO.InvDateAndInv e = new InventoryHotelDTO.InvDateAndInv();
                    e.setPrice(new BigDecimal(0));
                    e.setDisPrice(new BigDecimal(0));
                    e.setPriceSettle(new BigDecimal(0));
                    e.setDisPriceSettle(new BigDecimal(0));
                    e.setIsOpening("");
                    e.setInvTotal("");
                    e.setInvUsed("");
                    e.setInvBalance("");
                    e.setInvDate(str);
                    dto.getInvDateAndTotalList().add(e);
                }
                // 排序
                Collections.sort(dto.getInvDateAndTotalList(), (s1, s2) -> {
                    int i = s1.getInvDate().compareTo(s2.getInvDate());
                    if (i > 0) {
                        return 1;
                    } else if (i == 0) {
                        return 0;
                    } else {
                        return -1;
                    }
                });
            }

        });
        return inventoryHotelDTOList;
    }







        /**
         * @param componentFactory
         * @param <T>
         * @return
         * @author liminzhe
         */
    private <T> DataFetcher<UserDTO> initUser(ComponentProvider componentFactory) {

        UserQuery uq = componentFactory.getComponent(UserQuery.class);

        return env -> {
            // 查询参数
            long id = env.getArgument("id");

            UserDTO user = uq.getUserById(id);
            return user;
        };
    }

    /**
     * @param componentFactory
     * @param <T>
     * @return
     * @author liminzhe
     */
    private <T> DataFetcher<PaginationDTO<UserDTO>> initUsers(ComponentProvider componentFactory) {
        UserQuery uq = componentFactory.getComponent(UserQuery.class);
        return env -> {
            // 查询参数
            String name = env.getArgument("name");
            String storeId = env.getArgument("storeId");
            String account = env.getArgument("account");
            String mobile = env.getArgument("mobile");

            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");

            Map<String, Object> param = Maps.newHashMap();
            if (StringUtils.isNotBlank(name)) {
                param.put("name", name);
            }

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }

            if (account != null && account.length() > 0) {
                param.put("account", account);
            }

            if (mobile != null && mobile.length() > 0) {
                param.put("mobile", mobile);
            }
            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }

            final int totalCount = uq.getUserCount(param);
            final List<UserDTO> list = uq.getUserListByMap(param);

            return new PaginationDTO(totalCount, list);
        };
    }

    /**
     * 门店即manager的用户管理-分页查询
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    private <T> DataFetcher<PaginationDTO<UserInManagerDTO>> initUsersInManager(ComponentProvider componentFactory) {
        UserQuery userQuery = componentFactory.getComponent(UserQuery.class);
        return env -> {
            // 当前登录门店ID
            Long storeId = getStoreId(env, "storeId");
            logger.info("门店用户列表查询参数>>>门店ID>>>" + storeId);
            if (storeId == null || storeId == 0L){
                return null;
            }

            // 查询参数
            String name = env.getArgument("name");
            String account = env.getArgument("account");
            String mobile = env.getArgument("mobile");

            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");



            Map<String, Object> param = Maps.newHashMap();
            param.put("storeId", storeId);

            if (StringUtils.isNotBlank(name)) {
                param.put("name", name);
            }

            if (account != null && account.length() > 0) {
                param.put("account", account);
            }

            if (mobile != null && mobile.length() > 0) {
                param.put("mobile", mobile);
            }

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }

            final int totalCount = userQuery.getUserInManagerCount(param);
            final List<UserInManagerDTO> list = userQuery.getUserInManagerListByMap(param);

            return new PaginationDTO(totalCount, list);
        };
    }

    /**
     * @param componentFactory
     * @param <T>
     * @return
     * @author liminzhe
     */
    private <T> DataFetcher<BannerDTO> initBanner(ComponentProvider componentFactory) {

        BannerQuery bq = componentFactory.getComponent(BannerQuery.class);

        return env -> {
            // 查询参数
            long id = env.getArgument("id");


            BannerDTO banner = bq.getBannerById(id);
            return banner;
        };
    }

    /**
     * @param componentFactory
     * @return
     * @author liminzhe
     * @description :轮播图查询
     */
    private <T> DataFetcher<List<BannerDTO>> initBanners(ComponentProvider componentFactory) {

        BannerQuery bq = componentFactory.getComponent(BannerQuery.class);

        return env -> {

            // 查询参数
            String name = env.getArgument("name");
            Long storeId = getStoreId(env, "storeId");
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");


            Map<String, Object> param = Maps.newHashMap();
            if (StringUtils.isNotBlank(name)) {
                param.put("name", name);
            }
            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }
            if (storeId != null && storeId > 0) {
                param.put("storeId", storeId);
            }


            return bq.getBannerListByMap(param);
        };
    }

    /**
     * Admin-查询门店详情
     *
     * @param componentFactory
     * @param <T>
     * @return
     * @author liminzhe
     */
    private <T> DataFetcher<StoreDTO> initStore(ComponentProvider componentFactory) {
        StoreQuery storeQuery = componentFactory.getComponent(StoreQuery.class);
        StoreRepository storeRepository = componentFactory.getComponent(StoreRepository.class);
        return env -> {
            // 从上下文信息中获取门店ID
            Long id = getStoreId(env, "id");
            logger.debug("Get Store ID:"+id);

            StoreDTO dto = storeQuery.getStoreById(id);
            // Store store = storeRepository.getStore(dto.getId());

            // 所有的关联目录（商品分类和门店标签）
            List<Category> storeCategoryList = storeRepository.getStoreCategory(id);

            // 商品分类
            dto.setCategoryList(
                    storeCategoryList
                            .stream()
                            .filter(category -> (category.functionType() == CommonConstants.CATEGORY_FUNCTION_TYPE_PRODUCT))
                            .collect(Collectors.toList())
            );
            // admin CMS管理的门店标签
            dto.setCategoryTagList(
                    storeCategoryList
                            .stream()
                            .filter(category -> (category.functionType() == CommonConstants.CATEGORY_FUNCTION_TYPE_TAG
                                    && category.manageType() == CommonConstants.CATEGORY_MANAGE_TYPE_ADMIN))
                            .collect(Collectors.toList())
            );

            // manager CMS管理的门店标签
            dto.setCategoryManagerTagList(
                    storeCategoryList
                            .stream()
                            .filter(category -> (category.functionType() == CommonConstants.CATEGORY_FUNCTION_TYPE_TAG
                                    && category.manageType() == CommonConstants.CATEGORY_MANAGE_TYPE_MANAGER))
                            .collect(Collectors.toList())
            );

            // 获取次月计费信息
            List<StoreChargeInfoRecord> newMonthRecords =  storeQuery.getNextMonthChargeInfo(id,
                    Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().plus(1, ChronoUnit.MONTHS), "yyyy")),
                    Integer.valueOf(DateUtil.getFormatStr(LocalDateTime.now().plus(1, ChronoUnit.MONTHS), "MM")));
            if(newMonthRecords != null && newMonthRecords.size() > 0){
                StoreChargeInfoRecord storeChargeInfoRecord = newMonthRecords.get(0);
                dto.setNextChargeType(storeChargeInfoRecord.getChargeType());
                dto.setNextChargeRate(storeChargeInfoRecord.getChargeRate());
                dto.setNextChargeFee(storeChargeInfoRecord.getChargeFee());
            }

            return dto;
        };
    }

    /**
     * @param componentFactory
     * @param <T>
     * @return
     * @author liminzhe
     * @Descrip 查询店铺列表
     */
    private <T> DataFetcher<PaginationDTO<StoreDTO>> initStores(ComponentProvider componentFactory) {
        StoreQuery storeQuery = componentFactory.getComponent(StoreQuery.class);
        StoreRepository storeRepository = componentFactory.getComponent(StoreRepository.class);
        return env -> {
            // 查询参数
            String channel = env.getArgument("channel");
            String id = env.getArgument("id");
            String storeType = env.getArgument("storeType");
            String ban = env.getArgument("ban");
            String name = env.getArgument("name");
            String city = env.getArgument("city");
            String mobile = env.getArgument("mobile");
            String mngAccount = env.getArgument("mngAccount");
            String managerName = env.getArgument("managerName");
            String merchantId = env.getArgument("merchantId");
            String merchantNm = env.getArgument("merchantNm");
            String longitude = env.getArgument("longitude");
            String latitude = env.getArgument("latitude");
            String far = env.getArgument("far");
            String isPaySet = env.getArgument("isPaySet");
            String isOpening = env.getArgument("isOpening");
            String chargeType = env.getArgument("chargeType");
            String isJoin = env.getArgument("isJoin");
            String chargeRatePriceOn = env.getArgument("chargeRatePriceOn");

            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");

            Map<String, Object> param = Maps.newHashMap();
            if (StringUtils.isNotBlank(id)) {
                param.put("id", id);
            }

            if (StringUtils.isNotBlank(storeType)) {
                param.put("storeType", storeType);
            }

            if (StringUtils.isNotBlank(ban)) {
                param.put("ban", ban);
            }

            if (StringUtils.isNotBlank(name)) {
                param.put("name", name);
            }

            if (StringUtils.isNotBlank(city)) {
                param.put("city", city);
            }

            if (StringUtils.isNotBlank(mngAccount)) {
                param.put("mngAccount", mngAccount);
            }

            if (StringUtils.isNotBlank(managerName)) {
                param.put("managerName", managerName);
            }

            if (StringUtils.isNotBlank(mobile)) {
                param.put("mobile", mobile);
            }

            if (StringUtils.isNotBlank(merchantId)) {
                param.put("merchantId", merchantId);
            }

            if (StringUtils.isNotBlank(merchantNm)) {
                param.put("merchantNm", merchantNm);
            }

            if (StringUtils.isNotBlank(longitude)) {
                param.put("longitude", longitude);
            }

            if (StringUtils.isNotBlank(latitude)) {
                param.put("latitude", latitude);
            }

            if (StringUtils.isNotBlank(far)) {
                param.put("far", far);
            }
            if (StringUtils.isNotBlank(isPaySet)) {
                param.put("isPaySet", isPaySet);
            }
            if (StringUtils.isNotBlank(isOpening)) {
                param.put("isOpening", isOpening);
            }
            if (StringUtils.isNotBlank(chargeType)) {
                param.put("chargeType", chargeType);
            }
            if (StringUtils.isNotBlank(isJoin)) {
                param.put("isJoin", isJoin);
            }
            if (StringUtils.isNotBlank(chargeRatePriceOn)) {
                param.put("chargeRatePriceOn", chargeRatePriceOn);
            }

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }


            /*********************************************第一：查询数据START*********************************************/
            // 对各类型店铺查询进行拆分，以便最大程度的提高查询性能-start
            /* old version -start
            // 获取数量
            int dataCount = storeQuery.getStoreCount(param);
            if(dataCount == 0){
                return new PaginationDTO<>(0, new ArrayList<>());
            }
            // 获取数据
            final List<StoreDTO> listByMap = storeQuery.getStoreListByMap(param);
            old version -end */

            int dataCount = 0;
            List<StoreDTO> listByMap =  null;

            // 酒店商家列表
            if(CommonConstants.BIZ_HOTEL_STRING.equals(storeType)){
                dataCount = storeQuery.getHotelStoreCount(param);
                if(dataCount == 0){
                    return new PaginationDTO<>(0, new ArrayList<>());
                }
                // 获取数据
                listByMap = storeQuery.getHotelStoreListByMap(param);
            }
            // 非酒店商家列表
            else {
                dataCount = storeQuery.getStoreCount(param);
                if(dataCount == 0){
                    return new PaginationDTO<>(0, new ArrayList<>());
                }
                // 获取数据
                listByMap = storeQuery.getStoreListByMap(param);
            }
            // 对各类型店铺查询进行拆分，以便最大程度的提高查询性能-end
            /*********************************************第一：查询数据END*********************************************/


            /***************************************第二：对数据二次加工START***************************************/
            if (StringUtils.isNotBlank(channel) && "wechat".equals(channel)) {// Wechat H5端
                // 如果是微信用户端的门店查询则不查询门店的业务目录

                // 20190806查询门店的详细业务目录（即营业网点标签）- start
                fillCategoryInfo(componentFactory, listByMap);
                // 20190806查询门店的详细业务目录（即营业网点标签）- end

                // 处理店铺休息状态。只有点餐和购物存在休息状态
                if(CommonConstants.BIZ_HOTEL_STRING.equals(storeType)
                        || CommonConstants.BIZ_SHOPPING_STRING.equals(storeType)) {
                    listByMap.stream().forEach(storeDTO -> {
                        storeDTO.setIsOpening(StoreUtil.ifOpening(storeDTO));
                    });
                }

            } else { // Admin CMS端
                // Admin端店铺列表不需要查询目录（商品分类和店铺标签）信息 20190809-start注释开始
                /*// 循环查询门店的业务目录
                for (StoreDTO storeDTO : listByMap) {
                    // 20190809优化查询-start
                    //Store store = storeRepository.getStore(storeDTO.getId());
                    //List<Category> storeCategoryList = storeRepository.getStoreCategory(store);
                    List<Category> storeCategoryList = storeRepository.getStoreCategory(storeDTO.getId());
                    // 20190809优化查询-end

                    storeDTO.setCategoryList(storeCategoryList);
                }*/
                // Admin端店铺列表不需要查询目录（商品分类和店铺标签）信息 20190809-start注释结束
            }
            /***************************************第二：对数据二次加工END***************************************/

            PaginationDTO<StoreDTO> pageStore = new PaginationDTO<>();
            if (dataCount > 0) {
                pageStore.setTotal(dataCount);
                pageStore.setDataList(listByMap);
                return pageStore;
            }

            return new PaginationDTO<>(dataCount, listByMap);
        };
    }

    /**
     * 用户终端H5查询门店的详细业务目录（即营业网点标签）
     *
     * @param listByMap
     * @since 20190806
     */
    private void fillCategoryInfo(ComponentProvider componentFactory, List<StoreDTO> listByMap){
        if(listByMap != null && listByMap.size() > 0){
            logger.debug("开始填充门店的业务标签信息<><><>" + listByMap.size());

            // 查询所有业务目录（即业务标签）
            BaseService baseService = componentFactory.getComponent(BaseService.class);
            Category rootCategory = baseService.getRootCategory();

            // 循环查询门店的业务目录
            for (StoreDTO storeDTO : listByMap) {
                String categorysAllString= storeDTO.getCategorysAllString();
                logger.debug("门店的业务标签IDs>>>[" + storeDTO.getId()  + "]" + categorysAllString);

                if(StringUtils.isNotBlank(categorysAllString)){
                    // 字符串数组
                    String[] categoryIdStringArray = categorysAllString.split(",");
                    // Long数组
                    Long[] categoryIdLongArray = (Long[])(ConvertUtils.convert(categoryIdStringArray,Long.class));
                    // Long List
                    List<Long> categoryIdLongList = Arrays.asList(categoryIdLongArray);

                    // 从所有业务目录（商品分类和门店标签）中过滤出该门店标签
                    List<Category> storeCategoryList=  categoryIdLongList.stream()
                            .map((oId) -> rootCategory.getCategory(oId))
                            .filter((ca) -> ca != null)
                            .collect(Collectors.toList());
                    logger.debug("门店的业务标签信息>>>[" + storeDTO.getId()  + "]" + storeCategoryList.size());
                    storeDTO.setCategoryList(storeCategoryList);
                }
            }
            logger.debug("填充门店的业务标签信息结束！！！~~~~~~~~~~~~~~~");
        }
    }

    /**
     * @param componentFactory
     * @param <T>
     * @return
     * @author liminzhe
     */
    private <T> DataFetcher<StoreTableDTO> initStoreTable(ComponentProvider componentFactory) {

        StoreTableQuery stq = componentFactory.getComponent(StoreTableQuery.class);

        return env -> {
            // 查询参数
            long id = env.getArgument("id");

            StoreTableDTO storeTable = stq.getStoreTableById(id);
            return storeTable;
        };
    }

    /**
     * @param componentFactory
     * @param <T>
     * @return
     * @author liminzhe
     */
    private <T> DataFetcher<List<StoreTableDTO>> initStoreTables(ComponentProvider componentFactory) {

        StoreTableQuery stq = componentFactory.getComponent(StoreTableQuery.class);

        return env -> {

            // 查询参数
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");

            Long storeId = getStoreId(env, "storeId");

            Map<String, Object> param = Maps.newHashMap();
            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }
            if (storeId != null && storeId > 0) {
                param.put("storeId", storeId);
            }

            return stq.getStoreTableListByMap(param);
        };
    }

    private <T> DataFetcher<OrderDTO> initOrder(ComponentProvider componentFactory) {

        OrderQuery oq = componentFactory.getComponent(OrderQuery.class);

        return env -> {
            // 查询参数
            String id = env.getArgument("id");
            Map<String, Object> param = Maps.newHashMap();
            param.put("id", id);

            OrderDTO dto = oq.getOrderByMap(param);
            return dto;
        };
    }


    /**
     * @param componentFactory
     * @param <T>
     * @return
     * @author liminzhe
     */
    private <T> DataFetcher<PaginationDTO<OrderDTO>> initOrders(ComponentProvider componentFactory) {
        OrderQuery oq = componentFactory.getComponent(OrderQuery.class);

        return env -> {
            PaginationDTO<OrderDTO> pageOrder = new PaginationDTO<>();

            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");
            String status = env.getArgument("status");
            String storeId = getStoreIdStr(env, "storeId");
            String id = env.getArgument("id");
            String customerId = env.getArgument("customerId");
            String openId = env.getArgument("openId");
            String created = env.getArgument("created");
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");
            String areaNm = env.getArgument("areaNm");
            String city = env.getArgument("city");
            String storeNm = env.getArgument("storeNm");
            String storeType = env.getArgument("storeType");
            String numTag = env.getArgument("numTag");
            String payDt = env.getArgument("payDt");
            String cancelDt = env.getArgument("cancelDt");

            String custNm = env.getArgument("custNm");
            String mobile = env.getArgument("mobile");
            String shippingType = env.getArgument("shippingType");
            String shippingDt = env.getArgument("shippingDt");

            String reseveDtfrom = env.getArgument("reseveDtfrom");
            String reseveDtto = env.getArgument("reseveDtto");
            String custNo = env.getArgument("custNo");
            String custNmEn = env.getArgument("custNmEn");
            String isDeposit = env.getArgument("isDeposit");

            Map<String, Object> param = Maps.newHashMap();

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }

            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);

                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }

            if(StringUtils.isNotBlank(reseveDtfrom)&&StringUtils.isNotBlank(reseveDtto)){
                param.put("reseveDtfrom", reseveDtfrom);
                param.put("reseveDtto", reseveDtto);
            }
            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }
            if (StringUtils.isNotBlank(status)) {
                param.put("status", status);
            }
            if (StringUtils.isNotBlank(customerId)) {
                param.put("customerId", customerId);
            }
            if (StringUtils.isNotBlank(openId)) {
                param.put("openId", openId);
            }
            if (StringUtils.isNotBlank(created)) {
                param.put("created", created);
            }

            if(StringUtils.isNotBlank(payDt)){
            	payDt = DateFormatUtils.format(Long.valueOf(payDt), CoreConstants.ISO8601_PATTERN);
                param.put("payDt", payDt);
            }

            if(StringUtils.isNotBlank(cancelDt)){
            	cancelDt = DateFormatUtils.format(Long.valueOf(cancelDt), CoreConstants.ISO8601_PATTERN);
                param.put("cancelDt", cancelDt);
            }
            
            if (StringUtils.isNotBlank(areaNm)) {
                param.put("areaNm", areaNm);
            }
            if (StringUtils.isNotBlank(city)) {
                param.put("city", city);
            }
            if (StringUtils.isNotBlank(storeNm)) {
                param.put("storeNm", storeNm);
            }
            if (StringUtils.isNotBlank(storeType)) {
                param.put("storeType", storeType);
            }
            if (StringUtils.isNotBlank(numTag)) {
                param.put("numTag", numTag);
            }
            if (StringUtils.isNotBlank(id)) {
                param.put("id", id);
            }

            if (StringUtils.isNotBlank(custNm)) {
                param.put("custNm", custNm);
            }

            if (StringUtils.isNotBlank(custNmEn)) {
                param.put("custNmEn", custNmEn);
            }
            
            if (StringUtils.isNotBlank(mobile)) {
                param.put("mobile", mobile);
            }
            if (StringUtils.isNotBlank(shippingType)) {
                param.put("shippingType", shippingType);
            }
            if (StringUtils.isNotBlank(shippingDt)) {
                param.put("shippingDt", shippingDt);
            }
            if (StringUtils.isNotBlank(custNo)) {
                param.put("custNo", custNo);
            }
            if (StringUtils.isNotBlank(isDeposit)) {
                param.put("isDeposit", isDeposit);
            }

            int total = oq.getOrderCount(param);

            if (total > 0) {
                List<OrderDTO> orderList = oq.getOrderListByMap(param);
                pageOrder.setTotal(total);
                pageOrder.setDataList(orderList);
            }else{
                pageOrder.setDataList(new ArrayList<>());
            }

            return pageOrder;
        };
    }

    /**
     * @param componentFactory
     * @param <T>
     * @return
     * @author liminzhe
     */
    private <T> DataFetcher<List<OrderItemDTO>> initOrderItems(ComponentProvider componentFactory) {
        OrderQuery oq = componentFactory.getComponent(OrderQuery.class);

        return env -> {
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");
            Long orderId = env.getArgument("orderId");
            Long storeId = getStoreId(env, "storeId");
            Long id = env.getArgument("id");
            Long skuId = env.getArgument("skuId");

            Map<String, Object> param = Maps.newHashMap();

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }
            if (storeId != null && storeId > 0) {
                param.put("storeId", storeId);
            }
            if (orderId != null && orderId > 0) {
                param.put("orderId", orderId);
            }
            if (skuId != null && skuId > 0) {
                param.put("skuId", skuId);
            }
            if (id != null && id > 0) {
                param.put("id", id);
            }

            return oq.getOrderItemListByMap(param);
        };
    }


    /**
     * @param componentFactory
     * @param <T>
     * @return
     * @author DongXifu
     */
    private <T> DataFetcher<PaginationDTO<UserOrderDTO>> initUserOrders(ComponentProvider componentFactory) {
        OrderQuery oq = componentFactory.getComponent(OrderQuery.class);

        return env -> {
            PaginationDTO<UserOrderDTO> pageOrder = new PaginationDTO<>();
            Map<String,Object> param = getUserOrderParam(env);


            int total = oq.getUserOrderCount(param);
            List<UserOrderDTO> list = new ArrayList<>();
            if(total>0){
                pageOrder.setTotal(total);
                list =  oq.getUserOrderListByMap(param);
                Base64.Decoder decoder = Base64.getDecoder();
                list.stream().map(cust->{
                    try {
                        String nickNm = new String(decoder.decode(cust.getNickName().getBytes()), "UTF-8");
                        cust.setNickName(nickNm);
                    } catch (Exception e) {
                       cust.setNickName(cust.getNickName());
                    }
                    return  cust;
                }).collect(Collectors.toList());
                pageOrder.setDataList(list);
            }else {
                pageOrder.setDataList(list);
            }

            return new PaginationDTO<>(total, list);
        };
    }

    //设置微信用户订单查询参数
    public Map<String, Object> getUserOrderParam(DataFetchingEnvironment env) {
        Integer page = env.getArgument("page");
        Integer size = env.getArgument("size");
        String nickName = env.getArgument("nickName");
        String storeName = env.getArgument("storeName");
        String custNo = env.getArgument("custNo");
        String sumAmount = env.getArgument("sumAmount");
        String qty = env.getArgument("qty");
        String laterTrainDate = env.getArgument("laterTrainDate");
        String startTime = env.getArgument("startTime");
        String endTime = env.getArgument("endTime");
        Long storeId = env.getArgument("storeId");

        Map<String, Object> param = Maps.newHashMap();

        if (page != null && size != null && page >= 0 && size > 0) {
            param.put("page", page);
            param.put("size", size);
        }
        if (StringUtils.isNotBlank(nickName)) {
            param.put("nickName", nickName);
        }
        if (StringUtils.isNotBlank(custNo)) {
            param.put("custNo", custNo);
        }
        if (StringUtils.isNotBlank(storeName)) {
            param.put("storeName", storeName);
        }
        if (StringUtils.isNotBlank(sumAmount)) {
            param.put("sumAmount", sumAmount);
        }
        if (StringUtils.isNotBlank(qty)) {
            param.put("qty", qty);
        }
        if (StringUtils.isNotBlank(laterTrainDate)) {
            param.put("laterTrainDate", laterTrainDate);
        }
        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
            endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);
            param.put("startTime", startTime);
            param.put("endTime", endTime);
        }

        if (storeId != null && storeId > 0) {
            param.put("storeId", storeId);
        }
        return  param;
    }


    /**
     * 店铺总结算
     *
     * @Param
     * @return SettleDTO
     * @author Dong Xifu
     * @date 2019/9/2 5:24 下午
     */
    private <T> DataFetcher<SettleDTO> initStoreSettle(ComponentProvider componentFactory){
        SettleQuery sq = componentFactory.getComponent(SettleQuery.class);

        return env -> {
            String storeId = getStoreIdStr(env, "storeId");
            logger.info("settle storeId="+storeId);
            Map<String, Object> param = new HashMap<>();
            String isDtl = env.getArgument("isDtl");
            String currentTime = env.getArgument("startTime");
            currentTime = DateFormatUtils.format(Long.valueOf(currentTime), CoreConstants.DAILY_DATE_PATTERN);
            if (StringUtils.isBlank(isDtl) || "0".equals(isDtl)) {
                param = setSettleParam(env, true);//日期减一个月
                currentTime =  DateUtil.dateFormat(currentTime);
            } else {
                param = setSettleParam(env, false);//日期正常
            }

            param.put("currentTime",currentTime);
            param.put("isDtl", isDtl);
            return sq.getStoreSettleSum(param);
        };
    }

    /**
     * 店铺结算列表
     *
     * @Param componentFactory
     * @return DataFetcher<PaginationDTO<SettleDTO>
     * @author Dong Xifu
     * @date 2019/9/2 5:25 下午
     */
    private <T> DataFetcher<PaginationDTO<SettleDTO>> initStoreSettles( ComponentProvider componentFactory) {
        SettleQuery sq = componentFactory.getComponent(SettleQuery.class);
        return env -> {
            PaginationDTO<SettleDTO> pageOrder = new PaginationDTO<>();
            Map<String, Object> param = setSettleParam(env,true);

            int total = sq.getStoreSettleCount(param);
            List<SettleDTO> settleList = new LinkedList<>();
            if(total>0){
                pageOrder.setTotal(total);
                settleList = sq.getStoreSettleList(param);
                pageOrder.setDataList(settleList);
                return  pageOrder;
            }
            return new PaginationDTO<>(total, settleList);
        };
    }

    /*
     * admin总结算
     *
     * @Param
     * @return SettleDTO
     * @author Dong Xifu
     * @date 2019/9/2 10:37 上午
     */
    private <T> DataFetcher<SettleDTO> initAdminSettle(ComponentProvider componentFactory){
        SettleQuery sq = componentFactory.getComponent(SettleQuery.class);

        return env -> {
            Long storeId = getStoreId(env, "storeId");
            logger.info("settle storeId="+storeId);
            Map<String, Object> param = setSettleParam(env,true);
            String cloStatus = env.getArgument("cloStatus");
            if (StringUtils.isNotBlank(cloStatus)) {
                param.put("cloStatus",env.getArgument("cloStatus"));
            }
            String city = env.getArgument("city");
            if (StringUtils.isNotBlank(city)) {
                param.put("city",env.getArgument("city"));
            }
            return sq.getAdminSettle(param);
        };
    }

    /**
     * admin结算列表
     *
     * @Param
     * @return PaginationDTO<SettleDTO>
     * @author Dong Xifu
     * @date 2019/9/2 10:38 上午
     */
    private <T> DataFetcher<PaginationDTO<SettleDTO>> initAdminSettles( ComponentProvider componentFactory) {
        SettleQuery sq = componentFactory.getComponent(SettleQuery.class);
        return env -> {
            PaginationDTO<SettleDTO> pageOrder = new PaginationDTO<>();
            Map<String, Object> param = setSettleParam(env,true);
            String cloStatus = env.getArgument("cloStatus");
            if (StringUtils.isNotBlank(cloStatus)) {
                param.put("cloStatus",env.getArgument("cloStatus"));
            }
            String city = env.getArgument("city");
            if (StringUtils.isNotBlank(city)) {
                param.put("city",env.getArgument("city"));
            }

            int total = sq.getAdminSettleCount(param);
            List<SettleDTO> settleList = new LinkedList<>();
            if(total>0){
                pageOrder.setTotal(total);
                settleList = sq.getAdminSettleList(param);
                pageOrder.setDataList(settleList);
                return  pageOrder;
            }
            return new PaginationDTO<>(total, settleList);
        };
    }

    /**
     * 结算明细version2
     *
     * @Param
     * @return DataFetcher<PaginationDTO<SettleDTO>>
     * @author Dong Xifu
     * @date 2019/9/3 11:14 上午
     */
    private <T> DataFetcher<PaginationDTO<SettleDTO>> initSettlesDtlsV2(ComponentProvider componentFactory) {
        SettleQuery sq = componentFactory.getComponent(SettleQuery.class);

        return env -> {
            PaginationDTO<SettleDTO> pageOrder = new PaginationDTO<>();
            Map<String,Object> param = setSettleParam(env,false);

            int total = sq.getSettleDtlCount(param);
            List<SettleDTO> settleList = new LinkedList<>();
            if(total>0){
                settleList = sq.getSettleDtlListByMap(param);
                pageOrder.setTotal(total);
                pageOrder.setDataList(settleList);
                return pageOrder;
            }
            return new PaginationDTO<>(total, settleList);
        };
    }


    /**
     * 总结算
     *
     * @Param
     * @return SettleDTO
     * @author Dong Xifu
     * @date 2019/9/2 10:36 上午
     */
    private <T> DataFetcher<SettleDTO> initSettle(ComponentProvider componentFactory){
        OrderQuery oq = componentFactory.getComponent(OrderQuery.class);

        return env -> {
            Long storeId = getStoreId(env, "storeId");
            logger.info("settle storeId="+storeId);
            Map<String, Object> param = setSettleParam(env,true);
            return oq.getSettleByMap(param);
        };
    }

    /**
     * @Param
     * @return list<SettleDTO>
     * @describe 查询日期区间总结算列表 settles
     * @author Dong Xifu
     * @date 2019/3/8 下午1:41
     */
    private <T> DataFetcher<PaginationDTO<SettleDTO>> initSettles( ComponentProvider componentFactory) {
        OrderQuery oq = componentFactory.getComponent(OrderQuery.class);
        return env -> {
            PaginationDTO<SettleDTO> pageOrder = new PaginationDTO<>();
            Map<String, Object> param = setSettleParam(env,true);

            int total = oq.getSettleCount(param);
            List<SettleDTO> settleList = new LinkedList<>();
            if(total>0){
                pageOrder.setTotal(total);
                settleList = oq.getSettleListByMap(param);
                pageOrder.setDataList(settleList);
                return  pageOrder;

            }
            return new PaginationDTO<>(total, settleList);
        };
    }

    /**
     * @Param
     * @return List<SettleDTO>
     * @describe 结算明细
     * @author Dong Xifu
     * @date 2019/3/8 下午2:03
     */
    private <T> DataFetcher<PaginationDTO<SettleDTO>> initSettlesDtls(ComponentProvider componentFactory) {
        OrderQuery oq = componentFactory.getComponent(OrderQuery.class);

        return env -> {
            PaginationDTO<SettleDTO> pageOrder = new PaginationDTO<>();
            Map<String,Object> param = setSettleParam(env,false);

            int total = oq.getSettleDtlCount(param);
            List<SettleDTO> settleList = new LinkedList<>();
            if(total>0){
                pageOrder.setTotal(total);
                pageOrder.setDataList(oq.getSettleDtlListByMap(param));
                return pageOrder;
            }
            return new PaginationDTO<>(total, settleList);
        };
    }

    /**
     * 封装结算查询条件
     *
     * @Param env
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author Dong Xifu
     * @date 2019/9/1 9:01 上午
     */
    private Map<String, Object> setSettleParam(DataFetchingEnvironment env,Boolean isReduceDate) {
        Integer page = env.getArgument("page");
        Integer size = env.getArgument("size");
        String storeId = getStoreIdStr(env, "storeId");
        logger.info("settles_storeId="+storeId);
        String startTime = env.getArgument("startTime");
        String endTime = env.getArgument("endTime");
        String storeNm = env.getArgument("storeNm");
        String storeType = env.getArgument("storeType");
        String isJoin = env.getArgument("isJoin");

        Map<String, Object> param = Maps.newHashMap();

        if (page != null && size != null && page >= 0 && size > 0) {
            param.put("page", page);
            param.put("size", size);
        }

        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DAILY_DATE_PATTERN); //日期减一天
            endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);
            logger.info("startTime="+startTime);
            //logger.info("endTime="+endTime);
            if (isReduceDate) {
                param.put("startTime", DateUtil.dateFormat(startTime));//日期月份减一个月
                param.put("endTime", DateUtil.getLastDayOfMonth(DateUtil.dateFormat(startTime)));
            } else {
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }
        }

        if (StringUtils.isNotBlank(storeId)) {
            param.put("storeId", storeId);
        }
        if (StringUtils.isNotBlank(storeType)) {
            param.put("storeType", storeType);
        }
        if (StringUtils.isNotBlank(storeNm)) {
            param.put("storeNm", storeNm);
        }
        if (StringUtils.isNotBlank(isJoin)) {
            param.put("isJoin", isJoin);
        }
        return param;
    }


    /**
     * @param componentFactory
     * @param <T>
     * @return
     * @author liminzhe
     */
    private <T> DataFetcher<List<AreaDTO>> initAreas(ComponentProvider componentFactory) {
        AreaQuery aq = componentFactory.getComponent(AreaQuery.class);

        return env -> {
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");
            String areaCd = env.getArgument("areaCd");
            String parentCd = env.getArgument("parentCd");
            Integer areaStatus = env.getArgument("areaStatus");
            Integer areaLvl = env.getArgument("areaLvl");

            Map<String, Object> param = Maps.newHashMap();

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }
            if (areaStatus != null && areaStatus > 0) {
                param.put("areaStatus", areaStatus);
            }
            if (areaLvl != null && areaLvl > 0) {
                param.put("areaLvl", areaLvl);
            }
            if (StringUtils.isNotBlank(areaCd)) {
                param.put("areaCd", areaCd);
            }
            if (StringUtils.isNotBlank(parentCd)) {
                param.put("parentCd", parentCd);
            }

            List<AreaDTO> areaList = aq.getAreaListByMap(param);
            return areaList;
        };
    }


    private <T> DataFetcher<List<MenuItemDTO>> initMenuItems(ComponentProvider componentFactory) {
        ProductQuery aq = componentFactory.getComponent(ProductQuery.class);

        return env -> {
            Long parentCd = env.getArgument("groupId");
            Long storeId = getStoreId(env, "storeId");
            return aq.getMenuListOfGroup(storeId, parentCd);
        };
    }

    /**
     * @Title 查询评论详情
     * @Param
     * @return graphql.schema.DataFetcher<com.basoft.eorder.interfaces.query.ReviewDTO>
     * @author Dong Xifu
     * @date 2019/5/14 上午11:09
     */
    private DataFetcher<ReviewDTO> initReview(ComponentProvider componentProvider) {

        ReviewQuery rq = componentProvider.getComponent(ReviewQuery.class);

        return env -> {
            Long storeId = getStoreId(env, "storeId");

            String revId = env.getArgument("revId");
            String platformId = env.getArgument("platformId");
            String nickname = env.getArgument("nickname");
            String storeName = env.getArgument("storeName");
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");

            Map<String, Object> param = Maps.newHashMap();

            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);
                param.put("startTime",startTime);
                param.put("endTime", endTime);
            }

            if (storeId != null && storeId > 0) {
                param.put("storeId", storeId);
            }
            if (StringUtils.isNotBlank(storeName)) {
                param.put("storeName", storeName);
            }
            if (StringUtils.isNotBlank(revId)) {
                param.put("revId", revId);
            }
            if (StringUtils.isNotBlank(platformId)) {
                param.put("platformId", platformId);
            }
            if (StringUtils.isNotBlank(nickname)) {
                param.put("nickname", nickname);
            }


            return rq.getReviewDto(param);
        };
    }



    /**
     * @Title 查询评论列表
     * @Param
     * @return PaginationDTO<ReviewDTO>
     * @author Dong Xifu
     * @date 2019/5/14 上午11:11
     */
    private <T> DataFetcher<PaginationDTO<ReviewDTO>> initReviews(ComponentProvider componentFactory) {
        ReviewQuery rq = componentFactory.getComponent(ReviewQuery.class);

        return env -> {
            PaginationDTO<ReviewDTO> pageReview = new PaginationDTO<>();
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");
            String storeId = getStoreIdStr(env, "storeId");
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");
            String storeName = env.getArgument("storeName");
            String storeType = env.getArgument("storeType");
            String revId = env.getArgument("revId");
            String platformId = env.getArgument("platformId");
            String nickname = env.getArgument("nickname");
            String orderId = env.getArgument("orderId");

            Map<String, Object> param = Maps.newHashMap();

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }
            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }

            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }
            if (StringUtils.isNotBlank(orderId)) {
                param.put("orderId", orderId);
            }
            if (StringUtils.isNotBlank(storeType)) {
                param.put("storeType", storeType);
            }
            if (StringUtils.isNotBlank(storeName)) {
                param.put("storeName", storeName);
            }
            if (StringUtils.isNotBlank(revId)) {
                param.put("revId", revId);
            }
            if (StringUtils.isNotBlank(platformId)) {
                param.put("platformId", platformId);
            }
            if (StringUtils.isNotBlank(nickname)) {
                param.put("nickname", nickname);
            }
            int total = rq.getReviewCount(param);
            List<ReviewDTO> settleList = new LinkedList<>();
            pageReview.setTotal(total);
            if (total > 0) {
                List<ReviewDTO> reviewList = rq.getReviewList(param);
                reviewList.stream().map(review -> {

                    if(Review.REV_ANONYMITY_HIDE.equals(review.getIsAnonymity())){
                        review.setNickname("");
                    }
                    return review;
                }).collect(Collectors.toList());

                pageReview.setDataList(reviewList);
                return pageReview;
            } else {
                pageReview.setDataList(settleList);
            }
            return new PaginationDTO<>(total, settleList);
        };

    }


    /**
     * @Title 投诉与建议详情
     * @Param
     * @return graphql.schema.DataFetcher<com.basoft.eorder.domain.model.Advice>
     * @author Dong Xifu
     * @date 2019/5/20 下午5:01
     */
    private DataFetcher<Advice> initAdvice(ComponentProvider componentProvider) {
        AdviceQuery aq = componentProvider.getComponent(AdviceQuery.class);

        return env -> {
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");
            String storeId = getStoreIdStr(env, "storeId");
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");
            String storeName = env.getArgument("storeName");
            String advId = env.getArgument("advId");
            String platformId = env.getArgument("platformId");
            String nickname = env.getArgument("nickname");
            String adviTos = env.getArgument("adviTos");
            String adviType = env.getArgument("adviType");

            Map<String, Object> param = Maps.newHashMap();

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }
            if(StringUtils.isNotBlank(adviTos)){
                param.put("adviTos", adviTos);
            }
            if (StringUtils.isNotBlank(adviType)) {
                param.put("adviType", adviType);
            }

            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }

            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }

            if (StringUtils.isNotBlank(storeName)) {
                param.put("storeName", storeName);
            }
            if (StringUtils.isNotBlank(advId)) {
                param.put("advId", advId);
            }
            if (StringUtils.isNotBlank(platformId)) {
                param.put("platformId", platformId);
            }
            if (StringUtils.isNotBlank(nickname)) {
                param.put("nickname", nickname);
            }

            return aq.getAdvice(param);
        };

    }


    /**
     * @Title
     * @Param
     * @return PaginationDTO<Advice>
     * @author Dong Xifu
     * @date 2019/5/20 下午4:55
     */
    private <T> DataFetcher<PaginationDTO<AdviceDTO>> initAdvices(ComponentProvider componentFactory) {
        AdviceQuery aq = componentFactory.getComponent(AdviceQuery.class);

        return env -> {
            PaginationDTO<AdviceDTO> pageReview = new PaginationDTO<>();
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");
            String storeId = getStoreIdStr(env, "storeId");
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");
            String storeName = env.getArgument("storeName");
            String adviId = env.getArgument("adviId");
            String platformId = env.getArgument("platformId");
            String nickname = env.getArgument("nickname");
            String adviTos = env.getArgument("adviTos");
            String adviType = env.getArgument("adviType");

            Map<String, Object> param = Maps.newHashMap();

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }
            if(StringUtils.isNotBlank(adviTos)){
                param.put("adviTos", adviTos);
            }
            if (StringUtils.isNotBlank(adviType)) {
                param.put("adviType", adviType);
            }

            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }

            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }

            if (StringUtils.isNotBlank(storeName)) {
                param.put("storeName", storeName);
            }
            if (StringUtils.isNotBlank(adviId)) {
                param.put("adviId", adviId);
            }
            if (StringUtils.isNotBlank(platformId)) {
                param.put("platformId", platformId);
            }
            if (StringUtils.isNotBlank(nickname)) {
                param.put("nickname", nickname);
            }
            int total = aq.getAdviceCount(param);
            List<AdviceDTO> adviceList = new LinkedList<>();
            pageReview.setTotal(total);
            if (total > 0) {
                adviceList = aq.getAdviceList(param);

                pageReview.setDataList(adviceList);
                return pageReview;
            } else {
                pageReview.setDataList(adviceList);
            }
            return new PaginationDTO<>(total, adviceList);
        };

    }





    /**
     * 查询广告详情
     * @Param
     * @return AdvertDTO
     * @author Dong Xifu
     * @date 2019/6/10 下午2:38
     */
    private DataFetcher<AdvertDTO> initAdvert(ComponentProvider componentProvider) {
        AdvertQuery aq = componentProvider.getComponent(AdvertQuery.class);

        return env -> {
            String storeId = getStoreIdStr(env, "storeId");
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");
            String advId = env.getArgument("advId");
            String advLocation = env.getArgument("advLocation");
            String advType = env.getArgument("advType");
            String advStatus = env.getArgument("advStatus");
            String useYn = env.getArgument("useYn");
            String advName = env.getArgument("advName");

            Map<String, Object> param = Maps.newHashMap();

            if(StringUtils.isNotBlank(storeId)){
                param.put("storeId", storeId);
            }
            if (StringUtils.isNotBlank(advId)) {
                param.put("advId", advId);
            }
            if (StringUtils.isNotBlank(advLocation)) {
                param.put("advLocation", advLocation);
            }
            if (StringUtils.isNotBlank(advType)) {
                param.put("advType", advType);
            }
            if (StringUtils.isNotBlank(advStatus)) {
                param.put("advStatus", advStatus);
            }
            if (StringUtils.isNotBlank(useYn)) {
                param.put("useYn", useYn);
            }
            if (StringUtils.isNotBlank(useYn)) {
                param.put("useYn", useYn);
            }
            if (StringUtils.isNotBlank(advName)) {
                param.put("advName", advName);
            }

            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }

            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }

            return aq.getAdvertDto(param);
        };
    }


    /**
     * 查询广告列表
     * @Param
     * @return List<AdvertDTO>
     * @author Dong Xifu
     * @date 2019/6/10 下午2:26
     */
    private DataFetcher<PaginationDTO<AdvertDTO>> initAdverts(ComponentProvider componentProvider) {
        AdvertQuery aq = componentProvider.getComponent(AdvertQuery.class);

        return env -> {
            PaginationDTO<AdvertDTO> pageAdvert = new PaginationDTO<>();
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");
            String storeId = getStoreIdStr(env, "storeId");
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");
            String advId = env.getArgument("advId");
            String advLocation = env.getArgument("advLocation");
            String advType = env.getArgument("advType");
            String advStatus = env.getArgument("advStatus");
            String useYn = env.getArgument("useYn");
            String advName = env.getArgument("advName");

            Map<String, Object> param = Maps.newHashMap();

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }
            if(StringUtils.isNotBlank(storeId)){
                param.put("storeId", storeId);
            }
            if (StringUtils.isNotBlank(advId)) {
                param.put("advId", advId);
            }
            if (StringUtils.isNotBlank(advLocation)) {
                param.put("advLocation", advLocation);
            }
            if (StringUtils.isNotBlank(advType)) {
                param.put("advType", advType);
            }
            if (StringUtils.isNotBlank(advStatus)) {
                param.put("advStatus", advStatus);
            }
            if (StringUtils.isNotBlank(useYn)) {
                param.put("useYn", useYn);
            }
            if (StringUtils.isNotBlank(advName)) {
                param.put("advName", advName);
            }

            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }

            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }

            int total = aq.getAdvertCount(param);
            List<AdvertDTO> advertList = new LinkedList<>();
            pageAdvert.setTotal(total);
            if (total > 0) {
                advertList = aq.getAdvertList(param);

                pageAdvert.setDataList(advertList);
                return pageAdvert;
            } else {
                pageAdvert.setDataList(advertList);
            }
            return new PaginationDTO<>(total, advertList);
        };
    }

    /**
     * 折扣的详情-DataFetcher
     *
     * @param componentProvider
     * @return graphql.schema.DataFetcher<com.basoft.eorder.domain.model.activity.discount.Discount>
     */
    private DataFetcher<DiscountDTO> initDiscount(ComponentProvider componentProvider) {
        DiscountQuery discountQuery = componentProvider.getComponent(DiscountQuery.class);
        return env -> {
            // 从GraphQL查询上下文环境中获取传递过来的参数
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");
            String discId = env.getArgument("discId");
            String storeId = getStoreIdStr(env, "storeId");

            // 封装查询参数
            Map<String, Object> param = Maps.newHashMap();
            // 分页参数
            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }
            // 折扣编号
            if (StringUtils.isNotBlank(discId)) {
                param.put("discId", discId);
            } else {
                return null;
            }

            Discount discount = discountQuery.getDiscount(param);

            // 折扣存在
            if(discount != null){
                DiscountDTO discountDTO = new DiscountDTO.Builder().build(discount);

                // 查询折扣的明细列表
                Map<String, Object> paramDetail = Maps.newHashMap();
                paramDetail.put("discId", discId);
                if (storeId != null) {
                    paramDetail.put("storeId", storeId);
                }

                // List<DiscountDetail> discountDetails = discountQuery.getDiscountDetails(paramDetail);
                discountDTO.setDetailList(discountQuery.getDiscountDetails(paramDetail));

                return discountDTO;
            }
            // 折扣不存在
            else {
                return null;
            }
        };
    }

    /**
     * 分页查询折扣列表
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    private <T> DataFetcher<PaginationDTO<Discount>> initDiscounts(ComponentProvider componentFactory) {
        // Discount Query
        DiscountQuery discountQuery = componentFactory.getComponent(DiscountQuery.class);
        return env -> {
            // 从GraphQL查询上下文环境中获取传递过来的参数
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");
            String discName = env.getArgument("discName");
            String discChannel = env.getArgument("discChannel");
            String storeId = getStoreIdStr(env, "storeId");
            String startStartTime = env.getArgument("startStartTime");
            String startEndTime = env.getArgument("startEndTime");
            String endStartTime = env.getArgument("endStartTime");
            String endEndTime = env.getArgument("endEndTime");
            String discStatus = env.getArgument("discStatus");

            // 封装查询参数
            Map<String, Object> param = Maps.newHashMap();
            // 分页参数
            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }

            // 折扣活动的名称
            if (StringUtils.isNotBlank(discName)) {
                param.put("discName", discName);
            }

            // 折扣活动的渠道
            if (StringUtils.isNotBlank(discChannel)) {
                param.put("discChannel", discChannel);
            }

            // 所属门店
            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }

            // 折扣活动的起始时间的查询起始时间
            if (StringUtils.isNotBlank(startStartTime)) {
                startStartTime = DateFormatUtils.format(Long.valueOf(startStartTime), "yyyy-MM-dd HH:mm:ss");
                param.put("startStartTime", startStartTime);
            }

            // 折扣活动的起始时间的查询结束时间
            if (StringUtils.isNotBlank(startEndTime)) {
                startEndTime = DateFormatUtils.format(Long.valueOf(startEndTime), "yyyy-MM-dd HH:mm:ss");
                param.put("startEndTime", startEndTime);
            }

            // 折扣活动的结束时间的查询起始时间
            if (StringUtils.isNotBlank(endStartTime)) {
                endStartTime = DateFormatUtils.format(Long.valueOf(endStartTime), "yyyy-MM-dd HH:mm:ss");
                param.put("endStartTime", endStartTime);
            }

            // 折扣活动的结束时间的查询结束时间
            if (StringUtils.isNotBlank(endEndTime)) {
                endEndTime = DateFormatUtils.format(Long.valueOf(endEndTime), "yyyy-MM-dd HH:mm:ss");
                param.put("endEndTime", endEndTime);
            }

            // 折扣活动的状态
            if (StringUtils.isNotBlank(discStatus)) {
                param.put("discStatus", discStatus);
            }

            // 分页DTO对象
            PaginationDTO<Discount> pageReview = new PaginationDTO<>();
            // 根据查询条件获取总记录数
            int total = discountQuery.getDiscountCount(param);
            pageReview.setTotal(total);

            List<Discount> discountList = new LinkedList<>();
            // 总记录数有效（即大于0）时进行记录数据的查询
            if (total > 0) {
                discountList = discountQuery.getDiscountList(param);
                pageReview.setDataList(discountList);
                return pageReview;
            }
            // 总记录数为0返回空列表
            else {
                pageReview.setDataList(discountList);
                return pageReview;
            }
        };
    }

    /**
     * 折扣的详情-DataFetcher
     *
     * @param componentProvider
     * @return graphql.schema.DataFetcher<com.basoft.eorder.interfaces.query.activity.discount.DiscountDisplayDTO>
     */
    private <T> DataFetcher<List<DiscountDisplayDTO>> initDiscountsDisplay(ComponentProvider componentProvider) {
        DiscountQuery discountQuery = componentProvider.getComponent(DiscountQuery.class);
        return env -> {
            Long storeId = env.getArgument("storeId");
            // 封装查询参数
            Map<String, Object> param = Maps.newHashMap();
            // 商户编号
            if (StringUtils.isNotBlank(String.valueOf(storeId))) {
                param.put("storeId", String.valueOf(storeId));
            } else {
                return null;
            }

            // 查询同时滤除
            List<DiscountDisplayDTO> discountDisplayList = discountQuery.getDiscountDisplayList(param);

            // 滤除
            // return filterOffDiscount(discountDisplayList);

            return discountDisplayList;
        };
    }

    /**
     *过滤折扣活动的产品列表
     * 同一个ProdSkuId产品规格如果存在于多个有效折扣活动中，选择活动开始晚的活动价格。
     *
     * @param discountDisplayList
     * @return
     */
    private List<DiscountDisplayDTO> filterOffDiscount(List<DiscountDisplayDTO> discountDisplayList){
        List<DiscountDisplayDTO> discountDisplayTempList = new ArrayList<DiscountDisplayDTO>(discountDisplayList.size());

        // 以ProdSkuId属性来分组，将ProdSkuId相同的放在一起
        Map<Long, List<DiscountDisplayDTO>> discountMap = discountDisplayList.stream().collect(Collectors.groupingBy(DiscountDisplayDTO::getProdSkuId));

        // 循环处理分组Map并合并成List,同时将不符合的DiscountDisplayDTO剔除，
        Set<Long> sets= discountMap.keySet();
        for (Long prodSkuId : sets){
            List<DiscountDisplayDTO> tempList = discountMap.get(prodSkuId);

            // 同一个ProdSkuId存在多个折扣活动，先剔除再合并
            if(tempList.size() > 1){
                // 按折扣活动开始时间排序
                Collections.sort(tempList);
                // 取第一个合并
                discountDisplayTempList.add(tempList.get(0));
                // TODO 生产上出现场景：济州岛机场酒店定义了两个活动，开始时间和结束时间一模一样，幸好各个活动中sku不同，此时如果
                // TODO 出现sku相同的情况，就无语了。。。懵逼了。。。
            }
            // 只有一个折扣活动，直接合并
            else {
                discountDisplayTempList.addAll(tempList);
            }
        }
        return discountDisplayTempList;
    }

    


    /**
     * @param componentFactory
     * @param <T>
     * @return
     * @author liminzhe
     */
    private <T> DataFetcher<HolidayDTO> initHoliday(ComponentProvider componentFactory) {
    	HolidayQuery pq = componentFactory.getComponent(HolidayQuery.class);

        return env -> {
            // 查询参数
            Map<String, Object> param = Maps.newHashMap();
            
            String storeId = env.getArgument("storeId");
            String dt = env.getArgument("dt");

            if (StringUtils.isNotBlank(storeId)) {
            	param.put("storeId", storeId);
            }
            if (StringUtils.isNotBlank(dt)) {
            	param.put("dt", dt);
            }

            HolidayDTO holiday = pq.getHolidayByMap(param);
            return holiday;
        };
    }



    /**
     * 查询酒店库存
     *
     * @Param
     * @return InventoryHotelDTO
     * @author Dong Xifu
     * @date 2019/8/18 下午7:46
     */
    private <T> DataFetcher<InventoryHotelDTO> initInventoryHotel(ComponentProvider componentFactory) {
        InventoryHotelQuery iq = componentFactory.getComponent(InventoryHotelQuery.class);

        return env -> {
            // 查询参数
            Map<String, Object> param = Maps.newHashMap();

            String storeId = env.getArgument("storeId");
            String dt = env.getArgument("dt");

            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }


            InventoryHotelDTO inventoryHotelDto = iq.getInventoryHotelDto(param);
            return inventoryHotelDto;
        };
    }

    /**
     * 查询酒店库存列表
     *
     * @Param
     * @return List<InventoryHotelDTO>
     * @author Dong Xifu
     * @date 2019/8/18 下午7:45
     */
    private <T> DataFetcher<List<InventoryHotelDTO>> initInventoryHotels(ComponentProvider componentFactory) {
        InventoryHotelQuery iq = componentFactory.getComponent(InventoryHotelQuery.class);
        return env -> {
            String storeId = getStoreIdStr(env, "storeId");
            String productId = env.getArgument("productId");
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");

            Map<String, Object> param = Maps.newHashMap();

            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }

            if (StringUtils.isNotBlank(productId)) {
                param.put("productId", productId);
            }

            if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DAILY_DATE_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DAILY_DATE_PATTERN);
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }

            ArrayList<String> dataAll = DateUtil.findDataAll(startTime, endTime, 1);
            List<InventoryHotelDTO> inventoryHotelList = new LinkedList<>();
            inventoryHotelList = iq.getIventHotelGroupList(param);

            setInvDateHotel(inventoryHotelList, dataAll);

            //setWeekendPrice(inventoryHotelList,componentFactory);

            return inventoryHotelList;
        };
    }

    /**
     * 设置周末价格
     *
     * @Param
     * @return List<.InventoryHotelDTO>
     * @author Dong Xifu
     * @date 1/10/20 5:23 PM
     */
    private List<InventoryHotelDTO> setWeekendPrice(List<InventoryHotelDTO> hotelList,ComponentProvider componentFactory) {
        ProductQuery pq = componentFactory.getComponent(ProductQuery.class);
        hotelList.stream().forEach(h -> {
            try {
                ProductSkuDTO skuDTO = pq.getProductSkuByProId(h.getProdId()).stream().findFirst().orElseGet(() -> new ProductSkuDTO());
                h.getInvDateAndTotalList().stream().forEach(ih->{
                    int howDay = 0;//判断当前日期是星期几
                    try {
                        howDay = DateUtil.dayForWeek(ih.getInvDate());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (howDay == 6 || howDay == 7) {
                        if(ih.getPrice().compareTo(BigDecimal.ZERO)<1){
                            ih.setPrice(skuDTO.getPriceWeekend()==null?ih.getPrice():skuDTO.getPriceWeekend());
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return hotelList;
    }


    /**
     * 查询指定酒店限定日期期间的库存列表
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    private <T> DataFetcher<List<InventoryHotelDTO>> initInventoryHotelsByConditions(ComponentProvider componentFactory) {
        InventoryHotelQuery inventoryHotelQuery = componentFactory.getComponent(InventoryHotelQuery.class);
        return env -> {
            String storeId = getStoreIdStr(env, "storeId");
            String startDate = env.getArgument("startDate");
            String endDate = env.getArgument("endDate");

            Map<String, Object> param = Maps.newHashMap();

            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }

            if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
                // startDate = DateFormatUtils.format(Long.valueOf(startDate), CoreConstants.DAILY_DATE_PATTERN);
                // endDate = DateFormatUtils.format(Long.valueOf(endDate), CoreConstants.DAILY_DATE_PATTERN);
                param.put("startDate", startDate);
                param.put("endDate", endDate);
            }

            List<InventoryHotelDTO> inventoryHotelList = inventoryHotelQuery.getHotelInventoryListByConditions(param);

            // 处理加工库存信息。按sku_id对库存信息进行分组
            inventoryHotelList = hotelInventoryGroupBySkuId(inventoryHotelList);

            return inventoryHotelList;
        };
    }

    /**
     * 每个sku获取一个决定性库存信息（指定查询日期期间内库存量最少的库存信息）
     *
     * @param hotelInventoryList
     * @return
     */
    private List<InventoryHotelDTO> hotelInventoryGroupBySkuId(List<InventoryHotelDTO> hotelInventoryList) {
        List<InventoryHotelDTO> inventoryHotelResultList = new ArrayList<InventoryHotelDTO>();
        if (hotelInventoryList == null || hotelInventoryList.size() == 0) {
            return inventoryHotelResultList;
        }

        // 将库存信息按照sku id分组hotelInventorySkuMap
        Map<Long, List<InventoryHotelDTO>> hotelInventorySkuMap = hotelInventoryList.stream().collect(Collectors.groupingBy(InventoryHotelDTO::getProdSkuId));

        // 循环处理分组hotelInventorySkuMap
        Set<Long> sets = hotelInventorySkuMap.keySet();
        for (Long prodSkuId : sets) {
            List<InventoryHotelDTO> tempList = hotelInventorySkuMap.get(prodSkuId);
            if (tempList.size() > 0) {
                // 首先将isOpening为0,即该日期的房间不对外开放，则设置已使用库存为总库存，即库存余量设置为0
                tempList.stream().forEach(inventoryHotelDTO -> {
                    if ("0".equals(inventoryHotelDTO.getIsOpening())) {
                        inventoryHotelDTO.setInvBalance("0");
                    } else {
                        inventoryHotelDTO.setInvBalance(String.valueOf(Integer.valueOf(inventoryHotelDTO.getInvTotal())
                                - Integer.valueOf(inventoryHotelDTO.getInvUsed())));
                    }
                });

                // 对相同ProdSkuId的库存信息按照库存余量进行升序排序
                Collections.sort(tempList);

                // 取第一个放入
                inventoryHotelResultList.add(tempList.get(0));
            }
        }
        return inventoryHotelResultList;
    }


    /**
     * 查询BaseTopic
     *
     * @Param saveBaseTopic
     * @return int
     * @author Dong Xifu
     */
    private <T> DataFetcher<PaginationDTO<BaseTopicDTO>> initBaseTopics(ComponentProvider componentFactory) {
        BaseTopicQuery query = componentFactory.getComponent(BaseTopicQuery.class);
        return env -> {
            // 查询参数
            Integer page = env.getArgument("page");
            Integer size = env.getArgument("size");

            Map<String, Object> param = setParamTopic(env);

            if (page != null && size != null && page >= 0 && size > 0) {
                param.put("page", page);
                param.put("size", size);
            }

            final int totalCount = query.getBaseTopicCount(param);
            final List<BaseTopicDTO> list = query.getBaseTopicListByMap(param);

            return new PaginationDTO(totalCount, list);
        };
    }

    /**
     * 查询主题详情
     *
     * @return BaseTopicDTO
     * @author Dong Xifu
     * @date 1/14/20 1:08 PM
     */
    private <T> DataFetcher<BaseTopicDTO> initBaseTopic(ComponentProvider componentFactory) {
        BaseTopicQuery query = componentFactory.getComponent(BaseTopicQuery.class);

        return env -> {
            // 查询参数
            Map<String, Object> param = setParamTopic(env);

            BaseTopicDTO inventoryHotelDto = query.getBaseTopicDto(param);
            return inventoryHotelDto;
        };
    }



    private Map<String, Object> setParamTopic(DataFetchingEnvironment env){
        Map<String, Object> param = Maps.newHashMap();

        String tpId = env.getArgument("tpId");
        String tpBizType = env.getArgument("tpBizType");
        String tpName = env.getArgument("tpName");
        String tpNameForei = env.getArgument("tpNameForei");
        String tpUrlType = env.getArgument("tpUrlType");
        String tpStatus = env.getArgument("tpStatus");

        if (StringUtils.isNotBlank(tpId)) {
            param.put("tpId", tpId);
        }
        if (StringUtils.isNotBlank(tpBizType)) {
            param.put("tpBizType", tpBizType);
        }
        if (StringUtils.isNotBlank(tpName)) {
            param.put("tpName", tpName);
        }
        if (StringUtils.isNotBlank(tpNameForei)) {
            param.put("tpNameForei", tpNameForei);
        }
        if (StringUtils.isNotBlank(tpUrlType)) {
            param.put("tpUrlType", tpUrlType);
        }
        if (StringUtils.isNotBlank(tpStatus)) {
            param.put("tpStatus", tpStatus);
        }

        return param;
    }





    //AgentGraphqlQuery agentQuery = new AgentGraphqlQuery();


    /**
     * @param componentProvider
     * @param <T>
     * @return
     * @author woonill
     */
    private <T> GraphQLObjectType createQuery(ComponentProvider componentProvider) {
        return GraphQLObjectType.newObject()
                .name("Query")
                .field(newFieldDefinition()
                        .name("ProductGroups")
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("areaNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("nameChn").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("nameKor").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("orderBy").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("categoryName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("status").type(Scalars.GraphQLString).build())
                        .type(GraphQLObjects.PRODUCT_GROUP_PAGING_TYPE)
                        .dataFetcher(initProductGroup(componentProvider)))
                .field(newFieldDefinition()
                        .name("Product")
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLLong).build())
                        .type(GraphQLObjects.PRODUCT_GRAPHQL_TYPE)
                        .dataFetcher(initProduct(componentProvider)))
                .field(newFieldDefinition()
                        .name("Products")
                        .type(GraphQLObjects.PRODUCT_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("name").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("nameKor").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("nameChn").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("areaNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("status").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("isDeposit").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("orderBy").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("categoryId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .dataFetcher(initProducts(componentProvider)))
                .field(newFieldDefinition()
                        .name("ProductGroupMaps")
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("recommend").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("status").type(Scalars.GraphQLString).build())
                        .type(GraphQLList.list(GraphQLObjects.PRD_GRP_GRAPHQL_TYPE))
                        .dataFetcher(initProductGroupMaps(componentProvider)))
                // 零售商户产品列表查询（产品信息，产品sku信息，产品sku库存信息，产品规格信息）
                .field(newFieldDefinition()
                        .name("RetailProductGroupMaps")
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLLong).build())
                        .type(GraphQLList.list(GraphQLObjects.RETAIL_PRD_GRP_GRAPHQL_TYPE))
                        .dataFetcher(initRetailProductGroupMaps(componentProvider)))
                .field(newFieldDefinition()
                        .name("ProductSku")
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLLong).build())
                        .type(GraphQLObjects.PRODUCT_SKU_GRAPHQL_TYPE)
                        .dataFetcher(initProductSku(componentProvider)))
                .field(newFieldDefinition()
                        .name("ProductSkus")
                        .argument(GraphQLArgument.newArgument().name("name").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("categoryId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("productId").type(Scalars.GraphQLLong).build())
                        .type(GraphQLList.list(GraphQLObjects.PRODUCT_SKU_GRAPHQL_TYPE))
                        .dataFetcher(initProductSkus(componentProvider)))
                .field(newFieldDefinition()
                        .name("Retail")
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("name").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("categoryId").type(Scalars.GraphQLLong).build())
                        .type(GraphQLObjects.RETAIL_PRODUCT_GRAPHQL_TYPE)
                        .dataFetcher(RetailGraphqlQuery.initRetail(componentProvider)))
                .field(newFieldDefinition()
                        .name("InventoryRetails")
                        .argument(GraphQLArgument.newArgument().name("name").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("categoryId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("productId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("status").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .type(GraphQLObjects.INVENTORY_RETAIL_PAGING_TYPE)
                        .dataFetcher(RetailGraphqlQuery.initInventoryRetails(componentProvider)))
                .field(newFieldDefinition()
                        .name("RetailTemplates")
                        .argument(GraphQLArgument.newArgument().name("tId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("name").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("tStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .type(GraphQLObjects.PRODUCT_ALONE_STANDARD_TEMPLATE_PAGING_TYPE)
                        .dataFetcher(RetailGraphqlQuery.initProductAloneStandardTemplates(componentProvider)))
                .field(newFieldDefinition()
                        .name("RetailTemplate")
                        .argument(GraphQLArgument.newArgument().name("tId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("name").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("tStatus").type(Scalars.GraphQLString).build())
                        .type(GraphQLObjects.PRODUCT_ALONE_STANDARD_TEMPLATE_GRAPHQL_TYPE)
                        .dataFetcher(RetailGraphqlQuery.initRetailTemplate(componentProvider)))
                .field(newFieldDefinition()
                        .name("PostStoreSets")
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("pssId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("name").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("targetCountryCode").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("isFree").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("status").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .type(GraphQLObjects.POST_STORE_SET_PAGING_TYPE)
                        .dataFetcher(RetailGraphqlQuery.initPostStoreSets(componentProvider)))
                .field(newFieldDefinition()
                        .name("PostStoreSet")
                        .argument(GraphQLArgument.newArgument().name("pssId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("name").type(Scalars.GraphQLString).build())
                        .type(GraphQLObjects.POST_STORE_SET_GRAPHQL_TYPE)
                        .dataFetcher(RetailGraphqlQuery.initPostStoreSet(componentProvider)))
                .field(newFieldDefinition()
                        .name("User")
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLLong).build())
                        .type(GraphQLObjects.USER_GRAPHQL_TYPE)
                        .dataFetcher(initUser(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("Users")
                        .type(GraphQLObjects.USER_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("name").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("account").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("mobile").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initUsers(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("UsersInManager")
                        .type(GraphQLObjects.USER_IN_MANAGER_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("name").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("account").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("mobile").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initUsersInManager(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("Banner")
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLLong).build())
                        .type(GraphQLObjects.BANNER_GRAPHQL_TYPE)
                        .dataFetcher(initBanner(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("Banners")
                        .type(GraphQLList.list(GraphQLObjects.BANNER_GRAPHQL_TYPE))
                        .argument(GraphQLArgument.newArgument().name("name").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initBanners(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("Store")
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .type(GraphQLObjects.STORE_GRAPHQL_TYPE)
                        .dataFetcher(initStore(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("Stores")
                        .type(GraphQLObjects.STORE_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("channel").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("ban").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("name").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("mobile").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("mngAccount").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("managerName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("merchantId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("merchantNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("longitude").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("latitude").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("far").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("status").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("isSelfservice").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("isDelivery").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("selfserviceUseyn").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("deliveryUseyn").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("isPaySet").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("isOpening").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("chargeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("isJoin").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("chargeRatePriceOn").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initStores(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("StoreTable")
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLLong).build())
                        .type(GraphQLObjects.STORE_TABLE_GRAPHQL_TYPE)
                        .dataFetcher(initStoreTable(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("StoreTables")
                        .type(GraphQLList.list(GraphQLObjects.STORE_TABLE_GRAPHQL_TYPE))
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("number").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("tag").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("numberStr").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("maxSeat").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("qrCodeImagePath").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initStoreTables(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("Order")
                        .type(GraphQLObjects.ORDER_GRAPHQL_TYPE)
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("openId").type(Scalars.GraphQLString).build())
                        .dataFetcher(initOrder(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("Orders")
                        .type(GraphQLObjects.ORDER_PAGING_GRAPHQL_TYPE)
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("openId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("numTag").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("areaNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("status").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("customerId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("created").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("payDt").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("cancelDt").type(Scalars.GraphQLString).build())

                        .argument(GraphQLArgument.newArgument().name("custNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("countryNo").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("mobile").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("reseveDtfrom").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("reseveDtto").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("reseveTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("confirmDtfrom").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("confirmDtto").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("confirmTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("numPersons").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("shippingType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("shippingAddr").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("shippingAddrNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("shippingDt").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("shippingTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("shippingCmt").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("diningPlace").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("diningTime").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("cmt").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("reseveConfirmtime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("custNo").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("custNmEn").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("nmLast").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("nmFirst").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("nmLastEn").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("nmFirstEn").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("isDeposit").type(Scalars.GraphQLString).build())

                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initOrders(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("OrderItems")
                        .type(GraphQLList.list(GraphQLObjects.ORDER_GRAPHQL_TYPE))
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("orderId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("skuId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initOrderItems(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("RetailOrders")
                        .type(GraphQLObjects.ORDER_PAGING_GRAPHQL_TYPE)
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("openId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("areaNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("custNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("mobile").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("shippingAddrCountry").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("shippingType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("spStartTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("spEndTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("status").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("status8From").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("changeStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(RetailGraphqlQuery.initRetailOrders(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("RetailOrderStatusCount")
                        .type(GraphQLList.list(GraphQLObjects.RETAIL_ORDER_STATUS_COUNT_GRAPHQL))
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("status").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("shippingType").type(Scalars.GraphQLString).build())
                        .dataFetcher(RetailGraphqlQuery.initRetailOrderStatusCount(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("RetailSerOrderStatusCount")
                        .type(GraphQLList.list(GraphQLObjects.RETAIL_ORDER_STATUS_COUNT_GRAPHQL))
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("servStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("shippingType").type(Scalars.GraphQLString).build())
                        .dataFetcher(RetailGraphqlQuery.initRetailServOrderStatusCount(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("RetailOrderServices")
                        .type(GraphQLObjects.RETAIL_ORDER_SERVICE_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("servId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("orderId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("servCode").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("servType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("applyLinker").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("applyMobile").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("servStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("auditResult").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(RetailGraphqlQuery.initRetailOrderServices(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("RetailAftSalesApplys")//根据订单编号查询申请售后记录
                        .type(GraphQLList.list(GraphQLObjects.RETAIL_ORDER_SERVICE_GRAPHQL_TYPE))
                        .argument(GraphQLArgument.newArgument().name("orderId").type(Scalars.GraphQLString).build())
                        .dataFetcher(RetailGraphqlQuery.initRetailAfterSalesApplys(componentProvider))
                        .build())
                        /***********统计start**************统计分类和产品查询卖出产品数量 其它都是查询订单数量*/
                .field(newFieldDefinition()
                        .name("OrderSumStats")//今天本周今年总统计
                        .type(GraphQLList.list(GraphQLObjects.ORDER_SUM_STATS_GRAPHQL_TYPE))
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLLong).build())
                        .dataFetcher(initOrdeSumStats(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("OrderSumStoreRankStats")
                        .type(GraphQLList.list(GraphQLObjects.ORDER_SUM_STORE_STATS_GRAPHQL_TYPE))
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .dataFetcher(initOrderSumStoreRankStats(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("ProductSaleRankStats")
                        .type(GraphQLList.list(GraphQLObjects.PRODUCT_SALE_RANK_STATS_GRAPHQL_TYPE))
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .dataFetcher(initProductSaleRankStats(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("CategorySaleRankStats")
                        .type(GraphQLList.list(GraphQLObjects.CATEGORY_SALE_RANK_STATS_GRAPHQL_TYPE))
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .dataFetcher(initCategorySaleRankStats(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("OrderByDateSaleStats")//按日 周 月 统计
                        .type(GraphQLObjects.ORDER_BY_DATE_SALE_GRAPHQL_TYPE)
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("dateType").type(Scalars.GraphQLString).build())
                        .dataFetcher(initOrderByDateAmountStats(componentProvider))
                        .build())
                /***********统计end**************统计分类和产品查询卖出产品数量 其它都是查询订单数量*/
                .field(newFieldDefinition()
                        .name("UserOrder")
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLLong).build())
                        .type(GraphQLObjects.USER_ORDER_GRAPHQL_TYPE)
                        .dataFetcher(initUser(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("UserOrders")
                        .type(GraphQLObjects.USER_ORDER_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("id").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("nickName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("sumAmount").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("qty").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("laterTrainDate").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initUserOrders(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("AdminSettleSum")
                        .type(GraphQLObjects.SETTLE_GRAPHQL_TYPE)
                        .argument(GraphQLArgument.newArgument().name("cloStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .dataFetcher(initAdminSettle(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("AdminSettles")
                        .type(GraphQLObjects.SETTLES_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("cloStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initAdminSettles(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("StoreSettleSum")
                        .type(GraphQLObjects.SETTLE_GRAPHQL_TYPE)
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("isDtl").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .dataFetcher(initStoreSettle(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("StoreSettles")
                        .type(GraphQLObjects.SETTLES_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("isDtl").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("isDtl").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initStoreSettles(componentProvider))
                        .build())
                .field(newFieldDefinition()
                    .name("SettlesDtlsV2")
                    .type(GraphQLObjects.SETTLES_PAGING_TYPE)
                    .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("storeNm").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                    .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                    .dataFetcher(initSettlesDtlsV2(componentProvider))
                    .build())
                .field(newFieldDefinition()
                        .name("SettleSum")
                        .type(GraphQLObjects.SETTLE_GRAPHQL_TYPE)
                        .argument(GraphQLArgument.newArgument().name("storeNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .dataFetcher(initSettle(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("Settles")
                        .type(GraphQLObjects.SETTLES_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("storeNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initSettles(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("SettlesDtls")
                        .type(GraphQLObjects.SETTLES_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("storeNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initSettlesDtls(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("AdminBaSumSettle")//admin ba总结算
                        .type(GraphQLObjects.BA_SETTLE_GRAPHQL_TYPE)
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("cloStatus").type(Scalars.GraphQLString).build())
                        .dataFetcher(BaSettleGraphqlQuery.initAdminBaSumSettle(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("AdminBaSettles")//admin ba结算列表
                        .type(GraphQLObjects.BA_SETTLE_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("cloStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(BaSettleGraphqlQuery.initAdminBaSettles(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("BaSettlesDtls")//admin ba结算详情（订单）列表
                        .type(GraphQLObjects.BA_SETTLE_DTL_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(BaSettleGraphqlQuery.initBASettlesDtls(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("BaSettlesDtlSum")//admin ba结算详情（订单）列表
                        .type(GraphQLObjects.BA_SETTLE_DTL_GRAPHQL_TYPE)
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(BaSettleGraphqlQuery.initBaSettleDtl(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("BaSettleSumDay")//manager ba总结算
                        .type(GraphQLObjects.BA_SETTLE_GRAPHQL_TYPE)
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("cloStatus").type(Scalars.GraphQLString).build())
                        .dataFetcher(BaSettleGraphqlQuery.initBaSettleSumDay(componentProvider))
                    .build())
                .field(newFieldDefinition()
                        .name("BaSettleDayList")//manager ba结算列表
                        .type(GraphQLObjects.BA_SETTLE_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("cloStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(BaSettleGraphqlQuery.initBaSettleDays(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("Areas")
                        .type(GraphQLList.list(GraphQLObjects.AREA_GRAPHQL_TYPE))
                        .argument(GraphQLArgument.newArgument().name("areaId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("parentId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("areaLvl").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("areaStatus").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initAreas(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("MenuItems")
                        .type(GraphQLList.list(GraphQLObjects.MenuItemDTO_TYPE))
                        .argument(GraphQLArgument.newArgument().name("groupId").type(Scalars.GraphQLLong).build())
                        .dataFetcher(initMenuItems(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("review")
                        .type(GraphQLObjects.REVIEW_TYPE)
                        .argument(GraphQLArgument.newArgument().name("revId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("platformId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("orderId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                        .dataFetcher(initReview(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("reviews")
                        .type(GraphQLObjects.REVIEW_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("revId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("platformId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("orderId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("revStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initReviews(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("advice")
                        .type(GraphQLObjects.ADVICE_TYPE)
                        .argument(GraphQLArgument.newArgument().name("adviId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("platformId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("orderId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("revStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initAdvice(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("advices")
                        .type(GraphQLObjects.ADVICE_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("adviId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("platformId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("orderId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("revStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initAdvices(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("advert")
                        .type(GraphQLObjects.ADVERT_TYPE)
                        .argument(GraphQLArgument.newArgument().name("advId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("advName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("advLocation").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("advStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("useYn").type(Scalars.GraphQLString).build())
                        .dataFetcher(initAdvert(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("adverts")
                        .type(GraphQLObjects.ADVERT_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("advId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("advName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("advLocation").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("advStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("useYn").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initAdverts(componentProvider))
                        .build())
                 // 折扣活动详情
                .field(newFieldDefinition()
                        .name("Discount")
                        //.type(GraphQLObjects.DISCOUNT_TYPE)
                        .type(GraphQLObjects.DISCOUNT_INFO_TYPE)
                        .argument(GraphQLArgument.newArgument().name("discId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initDiscount(componentProvider))
                        .build())
                //折扣活动列表
                .field(newFieldDefinition()
                        .name("Discounts")
                        .type(GraphQLObjects.DISCOUNT_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("discName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("discChannel").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startStartTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startEndTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endStartTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endEndTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("discStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initDiscounts(componentProvider))
                        .build())
                //折扣活动列表
                .field(newFieldDefinition()
                        .name("DiscountsDisplay")
                        .type(GraphQLList.list(GraphQLObjects.DISCOUNT_DISPLAY_TYPE))
                        // Validation error of type WrongType: argument 'storeId' with value 'IntValue{value=600268860403028994}' is not a valid 'String' @ 'DiscountsDisplay'
                        //.argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLLong).build())
                        .dataFetcher(initDiscountsDisplay(componentProvider))
                        .build())

                // by dikim on 2019.05.21 added ShipPoint
                .field(newFieldDefinition()
                        .name("ShipPoint")
                        .type(GraphQLObjects.SHIP_POINT_GRAPHQL_TYPE)
                        .argument(GraphQLArgument.newArgument().name("shipPointid").type(Scalars.GraphQLLong).build())
                        .dataFetcher(initShipPoint(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("ShipPoints")
                        .type(GraphQLObjects.SHIP_POINT_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("shipPointnm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("shipPointid").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("areaId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("areaName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("addr").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("addrCn").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("lat").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("lon").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("phoneNo").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("cmt").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("cmtCn").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("status").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initShipPoints(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("MyContacts")
                        .type(GraphQLObjects.MY_CONTACT_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("openId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("custNm").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("countryNo").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("mobile").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(initMyContacts(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("Holiday")
                        .type(GraphQLObjects.HOLIDAY_GRAPHQL_TYPE)
                        .argument(GraphQLArgument.newArgument().name("holiday").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("dt").type(Scalars.GraphQLString).build())
                        .dataFetcher(initHoliday(componentProvider))
                        .build())
                .field(newFieldDefinition()
                    .name("InventoryHotel")
                    .type(GraphQLObjects.INVENTORYHOTEL_GRAPHQL_TYPE)
                    .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("productNm").type(Scalars.GraphQLString).build())
                    .dataFetcher(initInventoryHotel(componentProvider))
                    .build())
                .field(newFieldDefinition()
                    .name("InventoryHotels")
                    .type(GraphQLList.list(GraphQLObjects.INVENTORYHOTEL_GRAPHQL_TYPE))
                    .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("productNm").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                    .dataFetcher(initInventoryHotels(componentProvider))
                    .build())
                .field(newFieldDefinition()
                        .name("InventoryHotelsByConditions")
                        .type(GraphQLList.list(GraphQLObjects.INVENTORYHOTEL_GRAPHQL_TYPE_BASE))
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startDate").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endDate").type(Scalars.GraphQLString).build())
                        .dataFetcher(initInventoryHotelsByConditions(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("Agent")
                        .type(GraphQLObjects.AGENT_GRAPHQL_TYPE)
                        .argument(GraphQLArgument.newArgument().name("agtId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("agtName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("agtCode").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("agtType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("agtStatus").type(Scalars.GraphQLString).build())
                        .dataFetcher(AgentGraphqlQuery.initAgent(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("Agents")
                        .type(GraphQLObjects.AGENT_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("agtId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("agtName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("agtCode").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("agtType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("agtStatus").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(AgentGraphqlQuery.initAgents(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("AgtStores")
                        .type(GraphQLObjects.AGENT_STORE_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("agtId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("agtType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("agtCode").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("mobile").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("isBind").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(AgentGraphqlQuery.initAgtStores(componentProvider))
                        .build())
                .field(newFieldDefinition()
                    .name("SaAgtStores")
                    .type(GraphQLObjects.AGENT_STORE_PAGING_TYPE)
                    .argument(GraphQLArgument.newArgument().name("agtId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                    .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                    .dataFetcher(AgentGraphqlQuery.initSaAgtStores(componentProvider))
                    .build())
                .field(newFieldDefinition()
                    .name("AgentStoreSettle")//代理商总结算
                    .type(GraphQLObjects.AGENT_STORE_PAGING_TYPE)
                    .argument(GraphQLArgument.newArgument().name("agtId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("agtType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("agtCode").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("agtName").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                    .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                    .dataFetcher(AgentGraphqlQuery.initAgentStoreSettle(componentProvider))
                    .build())
                .field(newFieldDefinition()
                        .name("AgtUserSettle")
                        .type(GraphQLObjects.USER_ORDER_PAGING_TYPE)
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLLong).build())
                        .argument(GraphQLArgument.newArgument().name("nickName").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("agtId").type(Scalars.GraphQLString).build())
                        .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                        .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                        .dataFetcher(AgentGraphqlQuery.initAgtUserSettle(componentProvider))
                        .build())
                .field(newFieldDefinition()
                        .name("AgtStoreMap")
                        .type(GraphQLList.list(GraphQLObjects.AGT_STORE_MAP))
                        .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                        .dataFetcher(AgentGraphqlQuery.initAgtStoreMap(componentProvider))
                        .build())
                .field(newFieldDefinition()
                    .name("AgentOrders")
                    .type(GraphQLObjects.AGT_ORDERS_PAGING_TYPE)
                    .argument(GraphQLArgument.newArgument().name("agtId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("openId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("nickName").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("isFinish").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                    .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                    .dataFetcher(AgentGraphqlQuery.initAgentOrders(componentProvider))
                    .build())
                .field(newFieldDefinition()
                    .name("CaAgentOrders")
                    .type(GraphQLObjects.AGT_ORDERS_PAGING_TYPE)
                    .argument(GraphQLArgument.newArgument().name("agtId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("orderId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("nickName").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                    .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                    .dataFetcher(AgentGraphqlQuery.initCaAgentOrders(componentProvider))
                    .build())
                .field(newFieldDefinition()
                    .name("SaAgentOrders")
                    .type(GraphQLObjects.AGT_ORDERS_PAGING_TYPE)
                    .argument(GraphQLArgument.newArgument().name("agtId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("orderId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("storeType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("storeName").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("city").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                    .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                    .dataFetcher(AgentGraphqlQuery.initSaAgentOrders(componentProvider))
                    .build())
                .field(newFieldDefinition()
                    .name("AgtSumAmountQtyStats")//代理商今年结算总金额
                    .type(GraphQLObjects.AGT_ORDER_SUM_STATS_GRAPHQL_TYPE)
                    .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("agtId").type(Scalars.GraphQLString).build())
                    .dataFetcher(AgentGraphqlQuery.initAgtOrdeSumStats(componentProvider))
                    .build())
                .field(newFieldDefinition()
                    .name("AgentAmountStats")//代理商结算金额 按月 统计
                    .type(GraphQLObjects.ORDER_BY_DATE_SALE_GRAPHQL_TYPE)
                    .argument(GraphQLArgument.newArgument().name("agtId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("storeId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("dateType").type(Scalars.GraphQLString).build())
                    .dataFetcher(AgentGraphqlQuery.initAgtOrderStastic(componentProvider))
                    .build())
                .field(newFieldDefinition()
                    .name("PlAgtFeeSumStats")//平台、代理商、平台利润总统计(昨天,周 月 年 利润统计)
                    .type(GraphQLList.list(GraphQLObjects.PL_AGT_AMOUNT_STATS_GRAPHQL_TYPE))
                    .dataFetcher(AgentGraphqlQuery.initPlAgtAmountSumStats(componentProvider))
                    .build())
                .field(newFieldDefinition()
                    .name("PlAgtFeeStats")//平台、代理商、平台利润每(天,周 月 年 利润统计)
                    .type(GraphQLObjects.PL_AGT_AMOUNT_STATS_LIST_GRAPHQL_TYPE)
                    .argument(GraphQLArgument.newArgument().name("dateType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                    .dataFetcher(AgentGraphqlQuery.initPlAgtAmountStatsByDate(componentProvider))
                    .build())
                .field(newFieldDefinition()
                    .name("agtFeeRank")//代理商前十名排行
                    .type(GraphQLList.list(GraphQLObjects.AGENT_ORDER_GRAPHQL_TYPE) )
                    .argument(GraphQLArgument.newArgument().name("agtType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("startTime").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("endTime").type(Scalars.GraphQLString).build())
                    .dataFetcher(AgentGraphqlQuery.initAgtFeeRank(componentProvider))
                    .build())
                .field(newFieldDefinition()
                    .name("BaseTopics")//商店主题列表
                    .type(GraphQLObjects.BASE_TOPIC_PAGING_TYPE)
                    .argument(GraphQLArgument.newArgument().name("tpId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpBizType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpFuncType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpDisType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpName").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpNameForei").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpUrlType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpStatus").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpStatus").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("page").type(Scalars.GraphQLInt).build())
                    .argument(GraphQLArgument.newArgument().name("size").type(Scalars.GraphQLInt).build())
                    .dataFetcher(initBaseTopics(componentProvider))
                    .build())
                .field(newFieldDefinition()
                    .name("BaseTopic")//商店主题详情
                    .type(GraphQLObjects.BASETOPIC_GRAPHQL_TYPE)
                    .argument(GraphQLArgument.newArgument().name("tpId").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpBizType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpFuncType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpDisType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpName").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpNameForei").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpUrlType").type(Scalars.GraphQLString).build())
                    .argument(GraphQLArgument.newArgument().name("tpStatus").type(Scalars.GraphQLString).build())
                    .dataFetcher(initBaseTopic(componentProvider))
                    .build())

                .build();
    }

    public <T> QueryHandler build(ComponentProvider componentProvider) {
        GraphQLObjectType simpsonCharacter = createQuery(componentProvider);

        final GraphQLSchema qlSchema = new GraphQLSchema.Builder().query(simpsonCharacter).build();
        GraphQL graphQl = GraphQL.newGraphQL(qlSchema).build();

        return new QueryHandler() {
            @Override
            public Object handle(String query) {
                return handle(query, Collections.EMPTY_MAP);
            }

            @Override
            public Object handle(String query, Object context) {
                ExecutionInput input = ExecutionInput.newExecutionInput().query(query).context(context).build();
                final ExecutionResult execute = graphQl.execute(input);
                if (!execute.getErrors().isEmpty()) {
                    for (GraphQLError error : execute.getErrors()) {
                        logger.info(error.getMessage());
                    }

                    return null;
                }
                return execute.getData();
            }
        };
    }
}
