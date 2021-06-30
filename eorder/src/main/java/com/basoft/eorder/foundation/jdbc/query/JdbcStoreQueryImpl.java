package com.basoft.eorder.foundation.jdbc.query;

import com.basoft.eorder.domain.model.store.StoreChargeInfoRecord;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.StoreAttachDTO;
import com.basoft.eorder.interfaces.query.StoreDTO;
import com.basoft.eorder.interfaces.query.StoreExtendDTO;
import com.basoft.eorder.interfaces.query.StoreQuery;
import com.basoft.eorder.interfaces.query.topic.StoreTopicDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ProjectName: intellij_work
 * @Package: com.basoft.eorder.foundation.jdbc.query
 * @ClassName: JdbcStoreQueryImpl
 * @Description:
 * @Author: liminzhe
 * @Date: 2018-12-13 16:32
 * @Version: 1.0
 */
@Slf4j
@Component("StoreQuery")
public class  JdbcStoreQueryImpl extends BaseRepository implements StoreQuery {
    private static final String STORE_BY_ID = "select " +
            "   s.id as id, " +
            "   s.store_type as storeType, " +
            "   s.name as name, " +
            "   s.city as city, " +
            "   s.detail_addr as detailAddr,"+
            "   s.detail_addr_chn as detailAddrChn,"+
            "   s.email as email, " +
            "   s.mobile as mobile, " +
            "   s.ceo_name as ceoName, " +
            "   s.biz_scope as bizScope, " +
            "   u.mobile as managerPhone,"+
            "   u.account as managerAccount,"+
            "   u.email as managerEmail,"+
            "   u.name as managerName,"+
            "   s.manager_id as managerId, " +
            "   s.created as created ," +
            "   s.status as status, " +
            "   s.merchant_id as merchantId, " +
            "   s.logo_url as logoUrl, " +
            "   s.shop_hour as shopHour, " +
            "   s.description as description, " +
            "   s.description_chn as descriptionChn, " +
            "   s.longitude as longitude, " +
            "   s.latitude as latitude, " +
            "   s.merchant_nm as merchantNm, " +
            "   s.gateway_pw as gatewayPw, " +
            "   s.transid_type as transidType, " +
            "   ifnull(s.IS_SELFSERVICE,0)as isSelfservice," +
            "   ifnull(IS_DELIVERY,0)as isDelivery," +
            "   ifnull(SELFSERVICE_USEYN,0)as selfserviceUseyn," +
            "   ifnull(DELIVERY_USEYN,0) as deliveryUseyn,"+
            "   ifnull(IS_PAY_SET,0) as isPaySet,"+
            "   ifnull(IS_OPENING,0) as isOpening,"+
            "   ifnull(IS_SEGMENTED,0) as isSegmented,"+
            "   MORNING_ST as morningSt," +
            "   MORNING_ET as morningEt," +
            "   NOON_ST as noonSt," +
            "   NOON_ET as noonEt," +
            "   EVENING_ST as eveningSt," +
            "   EVENING_ET as eveningEt," +
            "   AFTERNOON_ST as afternooSt," +
            "   AFTERNOON_ET as afternoonEt," +
            "   MIDNIGHT_ST as midnightSt," +
            "   MIDNIGHT_ET as midnightEt,"+
            "   ifnull(CHARGE_TYPE,0)as chargeType," +
            "   ifnull(CHARGE_RATE,0)as chargeRate," +
            "   ifnull(CHARGE_FEE,0)as chargeFee," +
            "   ifnull(TABLE_COUNT,0)as tableCount,"+
            "   s.cash_settle_type  as cashSettleType,"+
            "   s.prod_price_type as prodPriceType,"+
            "   s.st_bank_name as stBankName,"+
            "   s.st_bank_acc as stBankAcc,"+
            "   s.st_bank_acc_name as stBankAccName,"+
            "   s.is_join as isJoin,"+
            "   s.SETTLE_RATE_PRICE_ON as chargeRatePriceOn,"+
            "   a.area_nm as areaNm " +
            "  from store s " +
            "      join user u on s.manager_id = u.id join area a on s.city = a.area_cd " +
            " where s.id = :id ";

    // private static final String STORE_COUNT_SELECT = "select count(*)";

    public static final String STORE_LIST_SELECT = "select" +
            "   s.id as id, " +
            "   s.store_type as storeType, " +
            "   s.name as name, " +
            "   s.city as city, " +
            "   s.detail_addr as detailAddr,"+
            "   s.detail_addr_chn as detailAddrChn,"+
            "   s.description as description,"+
            "   s.description_chn as descriptionChn,"+
            "   s.shop_hour as shopHour,"+
            "   s.email as email, " +
            "   s.mobile as mobile, " +
            "   s.ceo_name as ceoName, " +
            "   s.biz_scope as bizScope, " +
            "   s.manager_id as managerId," +
            "   u.mobile as managerPhone,"+
            "   u.account as managerAccount,"+
            "   u.email as managerEmail,"+
            "   u.name as managerName,"+
            "   s.status as status, " +
            "   s.merchant_id as merchantId, " +
            "   s.merchant_nm as merchantNm, " +
            "   s.gateway_pw as gatewayPw, " +
            "   s.transid_type as transidType, " +
            "   s.logo_url as logoUrl, " +
            "   s.longitude as longitude, " +
            "   s.latitude as latitude, " +
            "   s.created as created, \n" +
            "   a.area_nm as areaNm," +
            "   ifnull(s.IS_SELFSERVICE, 0) as isSelfservice," +
            "   ifnull(s.IS_DELIVERY, 0) as isDelivery," +
            "   ifnull(s.SELFSERVICE_USEYN, 0) as selfserviceUseyn," +
            "   ifnull(s.DELIVERY_USEYN, 0) as deliveryUseyn, "+
            "   ifnull(s.IS_PAY_SET, 0) as isPaySet, "+
            "   ifnull(s.IS_OPENING, 0) as isOpening, "+
            "   ifnull(s.IS_SEGMENTED, 0) as isSegmented, "+
            "   s.MORNING_ST as morningSt,"+
            "   s.MORNING_ET as morningEt,"+
            "   s.NOON_ST as noonSt,"+
            "   s.NOON_ET as noonEt,"+
            "   s.EVENING_ST as eveningSt,"+
            "   s.EVENING_ET as eveningEt,"+
            "   s.AFTERNOON_ST as afternoonSt,"+
            "   s.AFTERNOON_ET as afternoonEt,"+
            "   s.MIDNIGHT_ST as midnightSt,"+
            "   s.MIDNIGHT_ET as midnightEt, "+
            "   ifnull(s.CHARGE_TYPE, 0) as chargeType, "+
            "   ifnull(s.CHARGE_RATE, 0) as chargeRate,"+
            "   ifnull(s.CHARGE_FEE, 0) as chargeFee,"+
            "   ifnull(s.TABLE_COUNT, 0) as tableCount, "+
            "   s.cash_settle_type  as cashSettleType,"+
            "   s.prod_price_type as prodPriceType,"+
            "   s.st_bank_name as stBankName,"+
            "   s.st_bank_acc as stBankAcc,"+
            "   s.st_bank_acc_name as stBankAccName,"+
            "   s.IS_JOIN as isJoin,"+
            "   s.SETTLE_RATE_PRICE_ON as chargeRatePriceOn,"+


            // 20190806性能优化拼接查询门店标签（FUNCTION_TYPE为2，不查询FUNCTION_TYPE为1的店铺关联商品分类）
            "   (SELECT GROUP_CONCAT(SCM.CATEGORY_ID SEPARATOR ',') FROM STORE_CATEGORY_MAP SCM WHERE SCM.STORE_ID = S.ID AND FUNCTION_TYPE = 2) AS categorysAllString ";
            // 20190807 查询门店商品最低价
            // "   ifnull((SELECT MIN(PS.PRICE) FROM PRODUCT_SKU PS WHERE PS.PRODUCT_ID IN (SELECT PR.ID FROM PRODUCT PR WHERE PR.STORE_ID=S.ID AND PR.STATUS !=2 AND PR.STATUS != 3)), 0) as minPriceKor, " +
            // "   ifnull((SELECT MIN(DISC_PRICE) FROM acty_discount_detail WHERE DISC_ID = (SELECT DISC_ID FROM acty_discount WHERE USE_YN = 1 AND STORE_ID = S.ID AND END_TIME > NOW() ORDER BY START_TIME DESC LIMIT 0,1)), 0) as minDiscountPriceKor ";


    public static final String STORE_FORM = "" +
            "   from store s \n " +
            "   join user u on s.manager_id = u.id " +
            "   join area a on a.area_cd=s.city and a.use_yn='Y' " +
            "   where 1 = 1 \n";

    private static final String STORE_DISDANCE = " ,ifnull(ROUND(6378.138*2*ASIN(SQRT(POW(SIN((:latitude *PI()/180-latitude *PI()/180)/2),2) +" +
        "  COS(:latitude*PI()/180)*COS(latitude*PI()/180)*" +
        "   POW(SIN((:longitude*PI()/180-longitude*PI()/180)/2),2)))*1000),-1) AS disdance ";

    private static final String STORE_DISDANCE_TERM = "select * from (";

    private static final String STORE_DISDANCE_TERM_END = ") t1 ";

    public JdbcStoreQueryImpl(){
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    public JdbcStoreQueryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public StoreDTO getStoreById(long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        StoreDTO sd = this.queryForObject(STORE_BY_ID, map, new BeanPropertyRowMapper<StoreDTO>(StoreDTO.class));

        if (sd != null) {
            setStoreChild(sd);
        }
        return sd;
    }

    /**
     * 根据查询条件查询门店数量
     *
     * @param param
     * @return
     */
    @Override
    public int getStoreCount(Map<String, Object> param) {
        String longitude = Objects.toString(param.get("longitude"), null);
        String latitude = Objects.toString(param.get("latitude"), null);

        StringBuilder query = new StringBuilder();
        if (StringUtils.isNotBlank(longitude)&&StringUtils.isNotBlank(latitude)) {
            query.append("select count(1) from (");
            query.append( STORE_LIST_SELECT+STORE_DISDANCE + STORE_FORM);
            storeQueryCondition(param,query);
            query.append(STORE_DISDANCE_TERM_END);
            query.append("where  disdance <= :far");
        }else {
            query.append(SELECT_COUNT + STORE_FORM);
            storeQueryCondition(param,query);
        }

        String sql = query.toString();
        log.info("门店数量查询count SQL:开始||"+sql + "||结束");
        return this.getNamedParameterJdbcTemplate().queryForObject(sql, param, Integer.class);
    }

    /**
     * 根据查询条件查询门店列表
     *
     * @param param
     * @return
     */
    @Override
    public List<StoreDTO> getStoreListByMap(Map<String, Object> param) {
        String longitude = Objects.toString(param.get("longitude"), null);
        String latitude = Objects.toString(param.get("latitude"), null);
        String far = Objects.toString(param.get("far"), null);

        logger.info("longitude="+longitude);
        logger.info("latitude="+latitude);

        StringBuilder query = new StringBuilder();
        if (StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)) {
            query = new StringBuilder(STORE_DISDANCE_TERM + STORE_LIST_SELECT + STORE_DISDANCE + STORE_FORM);
            storeQueryCondition(param, query);
            query.append(STORE_DISDANCE_TERM_END);
            query.append(" where 1=1  ");
            if (StringUtils.isNotBlank(far)) {
                query.append(" and disdance <= :far ");
            }
            orderByAndPage(param, query, " order by disdance asc ");
        } else {
            query = new StringBuilder(STORE_LIST_SELECT + STORE_FORM);
            storeQueryCondition(param, query);
            orderByAndPage(param, query, " order by s.created desc, s.id desc ");
        }
        String sql = query.toString();
        log.info("门店列表查询SQL List SQL:开始||" + sql + "||结束");

        List<StoreDTO> dtoList = this.getNamedParameterJdbcTemplate().query(sql, param, new BeanPropertyRowMapper<>(StoreDTO.class));
        if (dtoList != null && dtoList.size() > 0) {
            setStoreAttachList(dtoList);
            setStoreTopicList(dtoList);
        }
        return dtoList;
    }


    @Override
    public List<StoreDTO> getStoreListConsumed(Map<String, Object> param) {
        StringBuilder query = new StringBuilder();
        query.append(STORE_LIST_SELECT+STORE_FORM);
        query.append(" and  s.id in (select store_id from `order` where open_id= :platformId)");
        return this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<>(StoreDTO.class));
    }

    public StringBuilder storeQueryCondition(Map<String, Object> param, StringBuilder query) {
        String id = Objects.toString(param.get("id"), null);
        String storeType = Objects.toString(param.get("storeType"), null);
        String ban = Objects.toString(param.get("ban"), null);
        String name = Objects.toString(param.get("name"), null);
        String city = Objects.toString(param.get("city"), null);
        String mobile = Objects.toString(param.get("mobile"), null);
        // String email = Objects.toString(param.get("email"), null);
        // String ceoName = Objects.toString(param.get("ceoName"), null);
        String status = Objects.toString(param.get("status"), null);
        String mngAccount = Objects.toString(param.get("mngAccount"), null);
        String merchantId = Objects.toString(param.get("merchantId"), null);
        String managerName = Objects.toString(param.get("managerName"), null);
        String isPaySet = Objects.toString(param.get("isPaySet"), null);
        String isOpening = Objects.toString(param.get("isOpening"), null);
        String chargeType = Objects.toString(param.get("chargeType"), null);
        String isJoin = Objects.toString(param.get("isJoin"), null);
        String chargeRatePriceOn = Objects.toString(param.get("chargeRatePriceOn"), null);

        if (StringUtils.isNotBlank(id)) {
            query.append(" and s.id = :id \n");
        }

        if (StringUtils.isNotBlank(storeType)) {
            query.append(" and s.store_type = :storeType \n");
        }

        if (StringUtils.isNotBlank(name)) {
            query.append(" and s.name like '%' :name '%' ");
        }

        if (StringUtils.isNotBlank(city)) {
            query.append(" and s.city = :city \n");
        }

        if (StringUtils.isNotBlank(mobile)) {
            query.append(" and s.mobile like '%' :mobile '%' \n");
        }

        if (StringUtils.isNotBlank(managerName)) {
            query.append(" and u.name like '%' :managerName '%' \n");
        }

        if (StringUtils.isNotBlank(merchantId)) {
            query.append(" and s.merchant_id = :merchantId \n");
        }

        if (StringUtils.isNotBlank(merchantId)) {
            query.append(" and s.merchant_nm = :merchantNm \n");
        }

        if (StringUtils.isNotBlank(status)) {
            query.append(" and s.status = :status \n");
        }

        if (StringUtils.isNotBlank(mngAccount)) {
            query.append(" and s.manager_id = :mngAccount \n");
        }

        if (StringUtils.isNotBlank(isPaySet)) {
            query.append(" and s.IS_PAY_SET = :isPaySet \n");
        }

        if (StringUtils.isNotBlank(isOpening)) {
            query.append(" and s.IS_OPENING = :isOpening \n");
        }

        if (StringUtils.isNotBlank(chargeType)) {
            query.append(" and s.CHARGE_TYPE = :chargeType \n");
        }
        if (StringUtils.isNotBlank(isJoin)) {
            query.append(" and s.IS_JOIN = :isJoin \n");
        }
        if (StringUtils.isNotBlank(chargeRatePriceOn)) {
            query.append(" and s.SETTLE_RATE_PRICE_ON = :chargeRatePriceOn \n");
        }

        // 商户状态过滤：ban不为空是用户终端H5查询
        if (StringUtils.isNotBlank(ban)) {
            // ban为1是用户终端H5查询
            if ("1".equals(ban)) {
                // 20190806门店状态重定义-start
                /*
                 OLD：
                 0-use  1-use  2-禁用forbidden  3-关闭delete
                 解释：0-新建完毕，即上架可用状态；1-再次启用状态；2-禁用，从禁用再开启变为1；3-关闭
                 */
                // query.append(" and s.status != 2 and s.status != 3 ");

                // 0-新建未上架new  1-上架状态open  2-下架forbidden  3-关闭delete（所有终端不可用）  4-欠费停服务（Manager CMS可用）
                query.append(" and s.status = 1 ");
                // 20190806门店状态重定义-end
            }
        }
        // 商户状态过滤：ban为空是Admin CMS终端查询
        else {
            query.append(" and s.status != 3 ");
        }

        return query;
    }

    /*public StringBuilder orderByAndPage(Map<String, Object> param,StringBuilder query,String orderBy ){
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        query.append(orderBy);

        if (page >= 0 && size > 0) {
            int resPage = page;
            if(page > 0){
                // resPage = (resPage-1)*10;
                resPage = (resPage-1)*size;
                param.put("page",resPage);
            }
            query.append(LIMIT);
        }
        return query;
    }*/


    /********************************************20190814商家列表查询拆分-start************************************************/
    // @see STORE_LIST_SELECT，基本相同。酒店增加了最低价格和折扣最低价格的查询
    private static final String HOTEL_STORE_LIST_SELECT = "select" +
            "   s.id as id, " +
            "   s.store_type as storeType, " +
            "   s.name as name, " +
            "   s.city as city, " +
            "   s.detail_addr as detailAddr,"+
            "   s.detail_addr_chn as detailAddrChn,"+
            "   s.description as description,"+
            "   s.description_chn as descriptionChn,"+
            "   s.shop_hour as shopHour,"+
            "   s.email as email, " +
            "   s.mobile as mobile, " +
            "   s.ceo_name as ceoName, " +
            "   s.biz_scope as bizScope, " +
            "   s.manager_id as managerId," +
            "   u.mobile as managerPhone,"+
            "   u.account as managerAccount,"+
            "   u.email as managerEmail,"+
            "   u.name as managerName,"+
            "   s.status as status, " +
            "   s.merchant_id as merchantId, " +
            "   s.merchant_nm as merchantNm, " +
            "   s.gateway_pw as gatewayPw, " +
            "   s.transid_type as transidType, " +
            "   s.logo_url as logoUrl, " +
            "   s.longitude as longitude, " +
            "   s.latitude as latitude, " +
            "   s.created as created, \n" +
            "   a.area_nm as areaNm," +
            "   ifnull(s.IS_SELFSERVICE, 0) as isSelfservice," +
            "   ifnull(s.IS_DELIVERY, 0) as isDelivery," +
            "   ifnull(s.SELFSERVICE_USEYN, 0) as selfserviceUseyn," +
            "   ifnull(s.DELIVERY_USEYN, 0) as deliveryUseyn, "+
            "   ifnull(s.IS_PAY_SET, 0) as isPaySet, "+
            "   ifnull(s.IS_OPENING, 0) as isOpening, "+
            "   ifnull(s.IS_SEGMENTED, 0) as isSegmented, "+
            "   s.MORNING_ST as morningSt,"+
            "   s.MORNING_ET as morningEt,"+
            "   s.NOON_ST as noonSt,"+
            "   s.NOON_ET as noonEt,"+
            "   s.EVENING_ST as eveningSt,"+
            "   s.EVENING_ET as eveningEt,"+
            "   s.AFTERNOON_ST as afternoonSt,"+
            "   s.AFTERNOON_ET as afternoonEt,"+
            "   s.MIDNIGHT_ST as midnightSt,"+
            "   s.MIDNIGHT_ET as midnightEt, "+
            "   ifnull(s.CHARGE_TYPE, 0) as chargeType, "+
            "   ifnull(s.CHARGE_RATE, 0) as chargeRate,"+
            "   ifnull(s.CHARGE_FEE, 0) as chargeFee,"+
            "   ifnull(s.TABLE_COUNT, 0) as tableCount, "+
            "   s.cash_settle_type  as cashSettleType,"+
            "   s.prod_price_type as prodPriceType,"+
            "   s.st_bank_name as stBankName,"+
            "   s.st_bank_acc as stBankAcc,"+
            "   s.st_bank_acc_name as stBankAccName,"+
            "   s.IS_JOIN as isJoin,"+
            "   s.SETTLE_RATE_PRICE_ON as chargeRatePriceOn,"+
            // 20190806性能优化拼接查询门店标签（FUNCTION_TYPE为2，不查询FUNCTION_TYPE为1的店铺关联商品分类）
            "   (SELECT GROUP_CONCAT(SCM.CATEGORY_ID SEPARATOR ',') FROM STORE_CATEGORY_MAP SCM WHERE SCM.STORE_ID = S.ID AND FUNCTION_TYPE = 2) AS categorysAllString, " +
            // 20190807 查询门店商品最低价
            "   ifnull((SELECT MIN(PS.PRICE) FROM PRODUCT_SKU PS WHERE PS.PRODUCT_ID IN (SELECT PR.ID FROM PRODUCT PR WHERE PR.STORE_ID=S.ID AND PR.STATUS !=2 AND PR.STATUS != 3)), 0) as minPriceKor, " +
            // 同一时刻有可能有多个有效活动存在的。所以修改为in，去掉limit 0,1。
            // "   ifnull((SELECT MIN(DISC_PRICE) FROM acty_discount_detail WHERE DISC_ID = (SELECT DISC_ID FROM acty_discount WHERE USE_YN = 1 AND STORE_ID = S.ID AND END_TIME > NOW() ORDER BY START_TIME DESC LIMIT 0,1)), 0) as minDiscountPriceKor ";
            "   ifnull((SELECT MIN(DISC_PRICE) FROM acty_discount_detail WHERE DISC_ID IN (SELECT DISC_ID FROM acty_discount WHERE USE_YN = '1' AND DISC_STATUS='2' AND STORE_ID = S.ID AND NOW() >= START_TIME AND NOW() <= END_TIME ORDER BY START_TIME DESC)), 0) as minDiscountPriceKor ";

    /**
     * 酒店列表数量查询
     *
     * @param param
     * @return
     */
    public int getHotelStoreCount(Map<String, Object> param){
        String longitude = Objects.toString(param.get("longitude"), null);
        String latitude = Objects.toString(param.get("latitude"), null);

        StringBuilder query = new StringBuilder();
        if (StringUtils.isNotBlank(longitude)&&StringUtils.isNotBlank(latitude)) {
            query.append("select count(1) from (");
            query.append( HOTEL_STORE_LIST_SELECT+STORE_DISDANCE + STORE_FORM);
            storeQueryCondition(param,query);
            query.append(STORE_DISDANCE_TERM_END);
            query.append("where  disdance <= :far");
        }else {
            query.append(SELECT_COUNT + STORE_FORM);
            storeQueryCondition(param,query);
        }

        String sql = query.toString();
        log.info("酒店HOTEL数量查询count SQL:开始||"+sql + "||结束");
        return this.getNamedParameterJdbcTemplate().queryForObject(sql, param, Integer.class);
    }

    /**
     * 酒店列表查询
     *
     * @param param
     * @return
     */
    public List<StoreDTO> getHotelStoreListByMap(Map<String, Object> param){
        String longitude = Objects.toString(param.get("longitude"), null);
        String latitude = Objects.toString(param.get("latitude"), null);

        StringBuilder query = new StringBuilder();
        if (StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)) {
            query = new StringBuilder(STORE_DISDANCE_TERM + HOTEL_STORE_LIST_SELECT + STORE_DISDANCE + STORE_FORM);
            storeQueryCondition(param, query);
            query.append(STORE_DISDANCE_TERM_END);
            query.append(" where  disdance <= :far ");
            orderByAndPage(param, query, " order by disdance asc ");
        } else {
            query = new StringBuilder(HOTEL_STORE_LIST_SELECT + STORE_FORM);
            storeQueryCondition(param, query);
            orderByAndPage(param, query, " order by s.created desc, s.id desc ");
        }
        String sql = query.toString();
        log.info("酒店HOTEL列表查询SQL List SQL:开始||" + sql + "||结束");

        List<StoreDTO> dtoList = this.getNamedParameterJdbcTemplate().query(sql, param, new BeanPropertyRowMapper<>(StoreDTO.class));
        if (dtoList != null && dtoList.size() > 0) {
            setStoreExtendList(dtoList);
            setStoreAttachList(dtoList);
            setStoreTopicList(dtoList);
        }
        return dtoList;
    }

    /********************************************20190814商家列表查询拆分-end************************************************/

    private StoreDTO setStoreChild(StoreDTO storeDTO) {
        List<StoreDTO> storeDTOList = new LinkedList<>();
        storeDTOList.add(storeDTO);
        setStoreExtendList(storeDTOList);
        setStoreAttachList(storeDTOList);
        setStoreTopicList(storeDTOList);
        return storeDTO;
    }

    private List<StoreDTO> setStoreExtendList(List<StoreDTO> storeDTOList){
        List<Long> longList = storeDTOList.stream().map(s->s.getId()).collect(Collectors.toList());
        Map param = new HashMap();
        param.put("storeIds", longList);
        List<StoreExtendDTO> extendList = getExtendListByStoreId(param);

        Map<Long,List<StoreExtendDTO>> extendListMap = extendList.stream().collect(Collectors.groupingBy(StoreExtendDTO::getStoreId));
        storeDTOList.stream().map(new Function<StoreDTO, StoreDTO>() {
            @Override
            public StoreDTO apply(StoreDTO storeDTO) {
                Long storeId = storeDTO.getId();
                List<StoreExtendDTO> extendList = extendListMap.get(storeId);
                if(extendList==null){
                    storeDTO.setStoreExtendList(new LinkedList<>());
                }else if (extendList != null && extendList.size() > 0) {
                    storeDTO.setStoreExtendList(extendList);
                }
                return storeDTO;
            }
        }).collect(Collectors.toList());
        return  storeDTOList;
    }

    private List<StoreExtendDTO> getExtendListByStoreId(Map param) {
        String sql = "select EX_ID, STORE_ID, FD_GROUP_NM, FD_GROUP_ID, FD_NAME, FD_ID,\n " +
            "FD_VAL_NAME, FD_VAL_CODE, \n" +
            "STATUS, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER from store_extend \n"+
            "where store_id in (:storeIds) and status=1 ";
        return this.getNamedParameterJdbcTemplate().query(sql, param, new BeanPropertyRowMapper<>(StoreExtendDTO.class));
    }

    private List<StoreDTO> setStoreAttachList(List<StoreDTO> storeDTOList){
        List<Long> longList = storeDTOList.stream().map(s->s.getId()).collect(Collectors.toList());
        Map param = new HashMap();
        param.put("storeIds", longList);
        List<StoreAttachDTO> attachList = getAttachListByStoreId(param);

        Map<Long,List<StoreAttachDTO>> attachMapList = attachList.stream().collect(Collectors.groupingBy(StoreAttachDTO::getStoreId));
        storeDTOList.stream().map(new Function<StoreDTO, StoreDTO>() {
            @Override
            public StoreDTO apply(StoreDTO storeDTO) {
                Long storeId = storeDTO.getId();
                List<StoreAttachDTO> attachList = attachMapList.get(storeId);
                if(attachList==null){
                    storeDTO.setAttachList(new LinkedList<>());
                }else if (attachList != null && attachList.size() > 0) {
                    storeDTO.setAttachList(attachList);
                }
                return storeDTO;
            }
        }).collect(Collectors.toList());
        return  storeDTOList;
    }

    private List<StoreAttachDTO> getAttachListByStoreId(Map param) {
        String sql = "select STORE_ATTACH_ID, STORE_ID, CONTENT_ID, CONTENT_URL, DISPLAY_ORDER\n" +
            ",IS_DISPLAY, ATTACH_TYPE\n" +
            ",CREATED_DT, CREATED_USER_ID, MODIFIED_DT, MODIFIED_USER_ID from store_attach \n"+
            " where store_id in (:storeIds) and IS_DISPLAY=1 order BY DISPLAY_ORDER asc ";
        return this.getNamedParameterJdbcTemplate().query(sql, param, new BeanPropertyRowMapper<>(StoreAttachDTO.class));
    }


    /**
     * 店铺主题设置
     *
     * @param storeDTOList
     * @return
     */
    private List<StoreDTO> setStoreTopicList(List<StoreDTO> storeDTOList){
        List<Long> longList = storeDTOList.stream().map(s->s.getId()).collect(Collectors.toList());
        Map param = new HashMap();
        param.put("storeIds", longList);
        List<StoreTopicDTO> topicList = getTopicListByStoreId(param);

        Map<Long,List<StoreTopicDTO>> topicListMapList = topicList.stream().collect(Collectors.groupingBy(StoreTopicDTO::getStoreId));
        storeDTOList.stream().map(new Function<StoreDTO, StoreDTO>() {
            @Override
            public StoreDTO apply(StoreDTO storeDTO) {
                Long storeId = storeDTO.getId();
                List<StoreTopicDTO> topicList = topicListMapList.get(storeId);
                if(topicList==null){
                    storeDTO.setTopicList(new LinkedList<>());
                }else if (topicList != null && topicList.size() > 0) {
                    storeDTO.setTopicList(topicList);
                }
                return storeDTO;
            }
        }).collect(Collectors.toList());
        return  storeDTOList;
    }

    private List<StoreTopicDTO> getTopicListByStoreId(Map param) {
        String sql = "SELECT s.id as storeId ,s.name as storeName,bt.TP_ID,bt.TP_NAME,bt.TP_NAME_FOREI,bt.TP_BIZ_TYPE,bt.TP_FUNC_TYPE from store s INNER JOIN store_topic st on s.id=st.STORE_ID \n" +
            "INNER JOIN base_topic bt on st.TP_ID=bt.TP_ID\n"+
            "where store_id in (:storeIds)";
        return this.getNamedParameterJdbcTemplate().query(sql, param, new BeanPropertyRowMapper<>(StoreTopicDTO.class));
    }


    /**
     * 查询商户下月的计费信息
     *
     * @param storeId
     * @param year
     * @param month
     * @return
     */
    @Override
    public List<StoreChargeInfoRecord> getNextMonthChargeInfo(Long storeId, Integer year, Integer month) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("storeId", storeId);
        param.put("year", year);
        param.put("month", month);

        StringBuilder query = new StringBuilder();
        query.append("SELECT SCIR_ID, STORE_ID, CHARGE_YEAR_MONTH, CHARGE_YEAR, CHARGE_MONTH, CHARGE_TYPE, CHARGE_RATE, CHARGE_FEE, USE_YN, FINAL_YN, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER FROM STORE_CHARGE_INFO_RECORD WHERE USE_YN = 1 AND FINAL_YN = 1 AND STORE_ID = :storeId AND CHARGE_YEAR = :year AND CHARGE_MONTH = :month");

        return this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<>(StoreChargeInfoRecord.class));
    }
}