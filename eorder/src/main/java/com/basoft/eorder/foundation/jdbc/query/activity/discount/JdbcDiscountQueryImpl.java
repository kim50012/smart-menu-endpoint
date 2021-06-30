package com.basoft.eorder.foundation.jdbc.query.activity.discount;

import com.basoft.eorder.domain.model.activity.discount.Discount;
import com.basoft.eorder.domain.model.activity.discount.DiscountDetail;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.ExchangeRateDTO;
import com.basoft.eorder.interfaces.query.activity.discount.DiscountDisplayDTO;
import com.basoft.eorder.interfaces.query.activity.discount.DiscountQuery;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 折扣活动查询
 *
 * @author Mentor
 * @version 1.0
 * @since 20180522
 */
@Slf4j
@Component("DiscountQuery")
public class JdbcDiscountQueryImpl extends BaseRepository implements DiscountQuery {
    // 折扣查询SQL-select子句
    private static final String DISCOUNT_SELECT = "SELECT DISC_ID,DISC_NAME,DISC_CHANNEL,CUST_ID,STORE_ID," +
            "DISC_RATE,START_TIME,END_TIME,DISC_STATUS,USE_YN,CREATED_DT,CREATED_USER_ID,MODIFIED_DT,MODIFIED_USER_ID ";

    // 折扣查询SQL-from子句和Where子句
    private static final String DISCOUNT_FROM = " FROM ACTY_DISCOUNT WHERE USE_YN = '1'";

    // 折扣明细查询SQL-select子句
    private static final String DISCOUNT_DETAIL_SELECT = "SELECT DISC_DET_ID,DISC_ID,PROD_ID,PROD_SKU_ID,CUST_ID," +
            "STORE_ID,DISC_PRICE,CREATED_DT,CREATED_USER_ID ";

    // 折扣明细查询SQL-from子句和Where子句
    private static final String DISCOUNT_DETAIL_FROM = " FROM ACTY_DISCOUNT_DETAIL WHERE 1 = 1";

    /**
     * 查询折扣活动详情
     *
     * @param param
     * @return
     */
    @Override
    public Discount getDiscount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(DISCOUNT_SELECT).append(DISCOUNT_FROM).append(" AND DISC_ID = :discId");
        return this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(Discount.class));
    }

    /**
     * 查询折扣活动详情的明细部分
     *
     * @param param
     * @return
     */
    @Override
    public List<DiscountDetail> getDiscountDetails(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(DISCOUNT_DETAIL_SELECT)
                .append(DISCOUNT_DETAIL_FROM)
                .append(" AND DISC_ID = :discId AND STORE_ID = :storeId ORDER BY DISC_DET_ID");
        return this.getNamedParameterJdbcTemplate()
                .query(sql.toString(), param, new BeanPropertyRowMapper<>(DiscountDetail.class));
    }

    /**
     * 根据查询条件查询记录总数
     *
     * @param param
     * @return
     */
    @Override
    public int getDiscountCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1)");
        sql.append(DISCOUNT_FROM);
        discountCondtion(sql, param, false);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    /**
     * 根据查询条件查询记录信息
     *
     * @param param
     * @return
     */
    @Override
    public List<Discount> getDiscountList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(DISCOUNT_SELECT).append(DISCOUNT_FROM);
        discountCondtion(sql, param, true);
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(Discount.class));
    }

    /**
     * 查询条件处理
     *
     * @param sql
     * @param param
     * @param isLimit
     * @return
     */
    private StringBuilder discountCondtion(StringBuilder sql, Map<String, Object> param, boolean isLimit) {
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        String discName = Objects.toString(param.get("discName"), null);
        String discChannel = Objects.toString(param.get("discChannel"), null);
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        String startStartTime = Objects.toString(param.get("startStartTime"), null);
        String startEndTime = Objects.toString(param.get("startEndTime"), null);
        String endStartTime = Objects.toString(param.get("endStartTime"), null);
        String endEndTime = Objects.toString(param.get("endEndTime"), null);
        String discStatus = Objects.toString(param.get("discStatus"), null);

        // 所属门店
        if (storeId > 0) {
            sql.append(" AND STORE_ID = :storeId \n");
        }

        // 折扣名称
        if (StringUtils.isNotBlank(discName)) {
            sql.append(" AND DISC_NAME LIKE '%' :discName '%' \n");
        }

        // 折扣渠道
        if (StringUtils.isNotBlank(discChannel)) {
            sql.append(" AND DISC_CHANNEL = :discChannel \n");
        }

        // 起始时间：查询起始时间不为空，查询结束时间为空，查询大于查询起始时间的数据
        if (StringUtils.isNotBlank(startStartTime) && StringUtils.isBlank(startEndTime)) {
            sql.append(" AND START_TIME >= STR_TO_DATE(:startStartTime,'%Y-%m-%d %H:%i:%s') ");
        }

        // 起始时间：查询起始时间不为空，查询结束时间不为空，查询大于查询起始时间且小于查询结束时间的数据
        if (StringUtils.isNotBlank(startStartTime) && StringUtils.isNotBlank(startEndTime)) {
            sql.append(" AND START_TIME >= STR_TO_DATE(:startStartTime,'%Y-%m-%d %H:%i:%s') AND START_TIME <= STR_TO_DATE(:startEndTime,'%Y-%m-%d %H:%i:%s') ");
        }

        // 起始时间：查询起始时间为空，查询结束时间不为空，查询小于查询结束时间的数据
        if (StringUtils.isBlank(startStartTime) && StringUtils.isNotBlank(startEndTime)) {
            sql.append(" AND START_TIME <= STR_TO_DATE(:startEndTime,'%Y-%m-%d %H:%i:%s') ");
        }

        // 结束时间：查询起始时间不为空，查询结束时间为空，查询大于查询起始时间的数据
        if (StringUtils.isNotBlank(endStartTime) && StringUtils.isBlank(endEndTime)) {
            sql.append(" AND END_TIME >= STR_TO_DATE(:endStartTime,'%Y-%m-%d %H:%i:%s') ");
        }

        // 结束时间：查询起始时间不为空，查询结束时间不为空，查询大于查询起始时间且小于查询结束时间的数据
        if (StringUtils.isNotBlank(endStartTime) && StringUtils.isNotBlank(endEndTime)) {
            sql.append(" AND END_TIME >= STR_TO_DATE(:endStartTime,'%Y-%m-%d %H:%i:%s') AND END_TIME <= STR_TO_DATE(:endEndTime,'%Y-%m-%d %H:%i:%s') ");
        }

        // 结束时间：查询起始时间为空，查询结束时间不为空，查询小于查询结束时间的数据
        if (StringUtils.isBlank(endStartTime) && StringUtils.isNotBlank(endEndTime)) {
            sql.append(" AND END_TIME <= STR_TO_DATE(:endEndTime,'%Y-%m-%d %H:%i:%s') ");
        }

        // 折扣状态
        if (StringUtils.isNotBlank(discStatus)) {
            sql.append(" AND DISC_STATUS = :discStatus \n");
        }

        sql.append(" ORDER BY CREATED_DT DESC ");

        appendPage(page, size, sql, param, isLimit);

        log.info("完整的查询SQL为>>>" + sql.toString());
        return sql;
    }

    /**
     * 根据门店ID查询该门店的折扣产品列表
     *
     * @param param
     * @return
     */
    @Override
    public List<DiscountDisplayDTO> getDiscountDisplayList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder("SELECT AD.DISC_ID,AD.DISC_NAME,AD.DISC_CHANNEL,AD.STORE_ID,AD.DISC_RATE,AD.START_TIME,AD.END_TIME,DD.DISC_DET_ID,DD.PROD_ID,DD.PROD_SKU_ID,DD.DISC_PRICE " +
                "FROM ACTY_DISCOUNT AD,ACTY_DISCOUNT_DETAIL DD " +
                "WHERE AD.USE_YN='1' AND AD.DISC_STATUS='2' AND AD.START_TIME <= NOW() AND AD.END_TIME >= NOW() AND AD.STORE_ID = DD.STORE_ID AND AD.DISC_ID = DD.DISC_ID AND DD.STORE_ID=");
        sql.append(":storeId");
        log.info("根据门店ID查询该门店的折扣产品列表::getDiscountDisplayDTOList::完整的查询SQL为>>>" + sql.toString());

        List<DiscountDisplayDTO> discountDisplayList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(DiscountDisplayDTO.class));

        log.info("根据门店ID查询该门店的折扣产品列表数量{}::::完整的结果为>>>{}",  discountDisplayList.size(), discountDisplayList);
        // 对于同一个产品SKU存在多个折扣活动的情况进行过滤处理
        discountDisplayList = distinctProductSkuDiscount(discountDisplayList);
        log.info("根据门店ID查询该门店的折扣产品列表过滤后的数量{}::::过滤后完整的结果为>>>{}",  discountDisplayList.size(), discountDisplayList);

        return tauxDeChange(discountDisplayList);
    }

    /**
     * 同一个产品SKU存在多个折扣活动时过滤，选择活动开始晚的活动价格。
     *
     * @param discountDisplayList
     */
    private List<DiscountDisplayDTO> distinctProductSkuDiscount(List<DiscountDisplayDTO> discountDisplayList) {
        List<DiscountDisplayDTO> distinctList =  new ArrayList<DiscountDisplayDTO>(discountDisplayList.size());
        Map<Long, List<DiscountDisplayDTO>> discountMap = discountDisplayList.stream().collect(Collectors.groupingBy(DiscountDisplayDTO::getProdSkuId));
        Set<Long> sets= discountMap.keySet();
        for(Long productSkuId : sets){
            List<DiscountDisplayDTO> ddList = discountMap.get(productSkuId);

            // 同一个ProdSkuId存在多个折扣活动，先剔除再合并
            if(ddList.size() > 1){
                // 按折扣活动开始时间排序
                Collections.sort(ddList);
                // 取第一个合并
                distinctList.add(ddList.get(0));
            }
            // 只有一个折扣活动，直接合并
            else {
                distinctList.addAll(ddList);
            }
        }
        return distinctList;
    }

    private List<DiscountDisplayDTO> tauxDeChange(List<DiscountDisplayDTO> discountDisplayList) {
        // 获取汇率
        ExchangeRateDTO erd = getNowExchangeRate();
        // 循环进行韩币和人民币转换
        if (erd != null) {
            discountDisplayList.stream().forEach(discountDisplayDTO -> {
                BigDecimal discPrice = new BigDecimal(discountDisplayDTO.getDiscPrice());
                BigDecimal discPriceChn = discPrice.multiply(erd.getKrwcnyRate()).setScale(2, BigDecimal.ROUND_UP);
                discountDisplayDTO.setDiscPriceChn(discPriceChn.toString());
            });
        }
        return discountDisplayList;
    }

    private final static String EXCHAGE_RATE = " \n" +
            "select \n" +
            "        t1.rate_id as rateId\n" +
            "        , date_format(t1.start_dt, '%Y%m%d%H%i%s') as startDt\n" +
            "        , date_format(t1.end_dt, '%Y%m%d%H%i%s') as endDt\n" +
            "        , t1.usd_cny_rate as usdCnyRate\n" +
            "        , t1.usd_krw_rate as usdKrwRate\n" +
            "        , t1.krw_cny_rate as krwCnyRate\n" +
            "        , t1.krw_usd_rate as krwUsdRate\n" +
            "        , t1.create_dts as createDts\n" +
            "        , t1.update_dts as updateDts\n" +
            "from    exchange_rate t1\n" +
            "where   t1.rate_id = (\n" +
            "            select\n" +
            "                    ifnull(max(a.rate_id), (select max(b.rate_id) from exchange_rate b where b.usd_cny_rate is not null))\n" +
            "            from    exchange_rate a\n" +
            "            where   a.start_dt <= now()     \n" +
            "            and     a.end_dt > now()     \n" +
            "            and     a.usd_cny_rate is not null\n" +
            "        )";

    /**
     * 获取韩币转人民币的汇率
     *
     * @return
     */
    private ExchangeRateDTO getNowExchangeRate() {
        return this.getNamedParameterJdbcTemplate().queryForObject(EXCHAGE_RATE, Maps.newHashMap(), new BeanPropertyRowMapper<>(ExchangeRateDTO.class));
    }

    /**
     * 根据门店ID查询该门店的折扣产品列表-用于订单核价(为了提高系统性能不进行汇率计算)
     *
     * @param param
     * @return
     */
    public List<DiscountDisplayDTO> getDiscountDisplayList4OrderCheck(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder("SELECT AD.DISC_ID,AD.DISC_NAME,AD.DISC_CHANNEL,AD.STORE_ID,AD.DISC_RATE,AD.START_TIME,AD.END_TIME,DD.DISC_DET_ID,DD.PROD_ID,DD.PROD_SKU_ID,DD.DISC_PRICE " +
                "FROM ACTY_DISCOUNT AD,ACTY_DISCOUNT_DETAIL DD " +
                "WHERE AD.USE_YN='1' AND AD.DISC_STATUS='2' AND AD.START_TIME <= NOW() AND AD.END_TIME >= NOW() AND AD.STORE_ID = DD.STORE_ID AND AD.DISC_ID = DD.DISC_ID AND DD.STORE_ID=");
        sql.append(":storeId");
        log.info("根据门店ID查询该门店的折扣产品列表::getDiscountDisplayDTOList::完整的查询SQL为>>>" + sql.toString());

        // 为了提高系统性能不进行汇率计算
        // List<DiscountDisplayDTO> discountDisplayList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(DiscountDisplayDTO.class));
        // return tauxDeChange(discountDisplayList);
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(DiscountDisplayDTO.class));
    }

    /**
     * 零售业务：
     * 根据活动ID和产品sku查询sku的活动详情
     *
     * @param discIdList
     * @param prodSkuIdList
     * @return
     */
    public List<DiscountDetail> getDiscountDetailList(List<Long> discIdList, List<Long> prodSkuIdList) {
        StringBuilder sql = new StringBuilder(512);
        /**
         * 查询当前有效活动（活动状态正常且活动时间当前有效）且活动id等于指定查询条件活动discIds的活动明细,
         * 并且从上述明细中根据查询条件prodSkuIds过滤-查询结果为指定活动discId和skuId的活动明细列表，存在skuId重复的情况。
         */
        sql.append("SELECT TEMP.DISC_ID, TEMP.PROD_SKU_ID, TEMP.DISC_PRICE FROM (")
                .append("SELECT ASDD.DISC_ID, ASDD.PROD_SKU_ID, ASDD.DISC_PRICE FROM acty_discount_detail ASDD WHERE ASDD.DISC_ID IN (")
                .append("SELECT AD.DISC_ID FROM acty_discount AD WHERE AD.USE_YN='1' AND AD.DISC_STATUS='2' AND AD.START_TIME <= NOW() AND AD.END_TIME >= NOW()")
                .append(" AND AD.DISC_ID IN (:discIds))) AS TEMP")
                .append(" WHERE TEMP.PROD_SKU_ID IN (:prodSkuIds)");

        log.info("零售业务：据活动ID和产品sku查询sku的活动详情::getDiscountDetailList::完整的查询SQL为>>>" + sql.toString());

        Map<String, Object> param = Maps.newHashMap();
        param.put("discIds", discIdList);
        param.put("prodSkuIds", prodSkuIdList);

        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(DiscountDetail.class));
    }
}
