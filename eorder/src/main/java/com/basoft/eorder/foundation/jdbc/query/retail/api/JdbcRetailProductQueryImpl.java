package com.basoft.eorder.foundation.jdbc.query.retail.api;

import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.ExchangeRateDTO;
import com.basoft.eorder.interfaces.query.retail.api.ProductAloneStandardInfoVO;
import com.basoft.eorder.interfaces.query.retail.api.RetailProductGroupMapDTO;
import com.basoft.eorder.interfaces.query.retail.api.RetailProductQuery;
import com.basoft.eorder.interfaces.query.retail.api.RetailProductSkuInfoVO;
import com.basoft.eorder.util.ExchangeRateUtil;
import com.basoft.eorder.util.RedisUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component("RetailProductQuery")
public class JdbcRetailProductQueryImpl extends BaseRepository implements RetailProductQuery {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ExchangeRateUtil exchangeRateUtil;

    private final static String RETAIL_PRODUCT_GROUP_SELECT =
            "select" +
                    "  p.id as id," +
                    "  p.name_kor as nameKor," +
                    "  p.name_chn as nameChn," +
                    "  p.des_kor as desKor," +
                    "  p.des_chn as desChn," +
                    "  p.WEIGHT as weight," +
                    "  p.detail_desc_kor as detailDesc," +
                    "  p.detail_desc_chn as detailChnDesc," +
                    "  p.status as status," +
                    "  p.store_id as storeId," +
                    "  p.recommend as recommend," +
                    "  p.show_index as showIndex," +
                    "  p.created as created," +
                    "  p.update_time as updateTime," +
                    "  pgm.prd_group_id as prdGroupId," +
                    "  pgm.show_index as showIndex," +
                    "  pgm.product_id as productId," +
                    "  (select image_url from product_image pi where pi.product_id = p.id and pi.image_type = 1 limit 1) as mainImageUrl," +
                    "  (select group_concat(image_url) from product_image pi where pi.product_id = p.id and pi.image_type = 0) as subImageUrl," +
                    "  (select ps.id from product_sku ps where ps.product_id = p.id and ps.use_default = 1 limit 1) as defaultSkuId," +
                    "  IFNULL((select ps.price from product_sku ps where ps.product_id = p.id and ps.use_default = 1 limit 1),0) as defaultSkuPriceKor," +
                    // 查询默认SKU的活动价格
                    "  IFNULL((SELECT IFNULL(ADT.DISC_PRICE,0) FROM acty_discount_detail ADT" +
                    "        WHERE ADT.PROD_SKU_ID = (select ps.id from product_sku ps where ps.product_id = p.id and ps.use_default = 1 limit 1)" +
                    "        AND ADT.DISC_ID = " +
                    "            (SELECT AD.DISC_ID FROM acty_discount AD WHERE AD.USE_YN='1' AND AD.DISC_STATUS='2' AND AD.START_TIME <= NOW() AND AD.END_TIME >= NOW()" +
                    "                AND AD.DISC_ID IN (SELECT ADT.DISC_ID FROM acty_discount_detail ADT WHERE ADT.PROD_SKU_ID = (select ps.id from product_sku ps where ps.product_id = p.id and ps.use_default = 1 limit 1))" +
                    "                ORDER BY AD.START_TIME DESC LIMIT 1)),0) as defaultSkuDiscPriceKor," +

                    // 查询默认SKU是否设置库存
                    "IFNULL((SELECT ps.IS_INVENTORY" +
                    "        FROM product_sku ps" +
                    "        WHERE ps.product_id = p.id AND ps.use_default = 1" +
                    "        LIMIT 1),0) AS defaultSkuIsInv," +

                    // 查询默认SKU的库存量
                    "IFNULL((SELECT IR.INV_BALANCE FROM inventory_retail IR WHERE IR.PROD_SKU_ID =" +
                    "        (SELECT ps.id FROM product_sku ps WHERE ps.product_id = p.id AND ps.use_default = 1 LIMIT 1)), 9999) AS defaultSkuInv," +

                    "(SELECT COUNT(1) FROM product_sku ps WHERE ps.product_id = p.id) AS skuCount"
    ;


    private final static String RETAIL_PRODUCT_GROUP_FROM =
            " from product p join product_prdgroup_map pgm on p.id = pgm.product_id" +
                    " where p.status != 2 and p.status != 3";


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
     * 零售商户：查询产品列表（产品信息，产品规格定义，产品SKU列表及详细信息，产品SKU库存信息）
     *
     * @param param
     * @return
     */
    @Override
    public List<RetailProductGroupMapDTO> getRetailProductGroupMapListByStoreId(Map<String, Object> param) {
        // 1 获取查询参数storeId
        Long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));

        // 2 拼装查询产品SQL
        StringBuilder querySQL = new StringBuilder(1700);
        querySQL.append(RETAIL_PRODUCT_GROUP_SELECT)
                .append(RETAIL_PRODUCT_GROUP_FROM);
        if (storeId != null && storeId > 0) {
            querySQL.append(" and p.store_id = :storeId ");
        }
        querySQL.append(" order by pgm.show_index asc ");
        log.info("getRetailProductGroupMapListByStoreId::SQL::" + querySQL.toString());

        // 3 查询产品列表
        List<RetailProductGroupMapDTO> retailProductGroupMapList = this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(RetailProductGroupMapDTO.class));

        // 4、转换价格：从韩币转为人民币
        if (retailProductGroupMapList != null && retailProductGroupMapList.size() > 0) {
            BigDecimal krwCnyRate = exchangeRateUtil.getNowKrwCnyRate();
            if(krwCnyRate != null){
                for (RetailProductGroupMapDTO retailProduct : retailProductGroupMapList) {
                    retailProduct.setDefaultSkuPriceChn(retailProduct.getDefaultSkuPriceKor().multiply(krwCnyRate).setScale(2, BigDecimal.ROUND_UP));
                    retailProduct.setDefaultSkuDiscPriceChn(retailProduct.getDefaultSkuDiscPriceKor().multiply(krwCnyRate).setScale(2, BigDecimal.ROUND_UP));
                }
            }
        }

        return retailProductGroupMapList;
    }

    /**
     * 根据产品ID查询产品规格定义信息
     *
     * @param productId
     * @return
     */
    public List<ProductAloneStandardInfoVO> getRetailProductAloneStandardInfoList(Long productId) {
        // 1、封装参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("productId", productId);

        // 2、查询SQL
        StringBuilder querySQL = new StringBuilder(1024);
        querySQL.append("SELECT ")
                // 规格基础信息
                .append("PAS.STD_ID, PAS.STORE_ID, PAS.PROD_ID, PAS.STD_NAME_CHN, PAS.STD_NAME_KOR, PAS.STD_NAME_ENG, PAS.DIS_ORDER, PAS.STD_IMAGE,")

                // 规格的规格项信息
                .append(" (SELECT GROUP_CONCAT(CONCAT(PASI.ITEM_ID,'@',PASI.ITEM_NAME_CHN)) ")
                .append(" FROM product_alone_standard_item PASI WHERE PASI.STD_ID = PAS.STD_ID AND PASI.ITEM_STATUS = 1) AS productAloneStandardItemInfo")

                .append(" FROM product_alone_standard PAS WHERE PAS.PROD_ID = :productId AND PAS.STD_STATUS = 1");
        log.info("根据产品ID查询产品信息的SQL::{}", querySQL.toString());

        return this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(ProductAloneStandardInfoVO.class));
    }

    /**
     * 根据产品ID查询产品信息
     *
     * @param productId
     * @return
     */
    public List<RetailProductSkuInfoVO> getRetailProductInfoList(long productId) {
        // 1、封装参数
        Map<String, Object> param = Maps.newHashMap();
        param.put("productId", productId);

        // 2、查询SQL
        StringBuilder querySQL = new StringBuilder(1024);
        querySQL.append("SELECT ")
                // sku基本信息
                .append("PS.id AS prodSkuId, PS.name_kor AS nameKor, PS.name_chn AS nameChn, PS.product_id AS productId, PS.WEIGHT AS weight, PS.price AS priceKor,")
                .append(" PS.IS_INVENTORY AS isInventory, PS.DIS_ORDER AS disOrder, PS.use_default AS useDefault,")

                // 产品sku库存
                .append(" IFNULL((SELECT IR.INV_BALANCE FROM inventory_retail IR WHERE IR.PROD_SKU_ID = PS.id),0) AS prodSkuInv,")

                // 产品sku活动ID及活动价格
                .append(" IFNULL((SELECT ADT.DISC_ID")
                .append(" FROM acty_discount_detail ADT WHERE ADT.PROD_SKU_ID = PS.id AND ADT.DISC_ID = (")
                .append(" SELECT AD.DISC_ID FROM acty_discount AD WHERE AD.USE_YN='1' AND AD.DISC_STATUS='2' AND AD.START_TIME <= NOW() AND AD.END_TIME >= NOW()")
                .append(" AND AD.DISC_ID IN (SELECT ADT.DISC_ID FROM acty_discount_detail ADT WHERE ADT.PROD_SKU_ID = PS.id)")
                .append(" ORDER BY AD.START_TIME DESC LIMIT 1)),0) AS skuDiscId,")

                .append(" IFNULL((SELECT ADT.DISC_PRICE")
                .append(" FROM acty_discount_detail ADT WHERE ADT.PROD_SKU_ID = PS.id AND ADT.DISC_ID = (")
                .append(" SELECT AD.DISC_ID FROM acty_discount AD WHERE AD.USE_YN='1' AND AD.DISC_STATUS='2' AND AD.START_TIME <= NOW() AND AD.END_TIME >= NOW()")
                .append(" AND AD.DISC_ID IN (SELECT ADT.DISC_ID FROM acty_discount_detail ADT WHERE ADT.PROD_SKU_ID = PS.id)")
                .append(" ORDER BY AD.START_TIME DESC LIMIT 1)),0) AS skuDiscPriceKor,")

                // 产品sku信息
                .append(" IFNULL((SELECT GROUP_CONCAT(CONCAT(PSAS.STANDARD_ID,'@',PSAS.STANDARD_ITEM_ID))")
                .append(" FROM product_sku_alone_standard PSAS")
                .append(" WHERE PSAS.PRODUCT_SKU_ID = PS.id AND PSAS.`STATUS` = 1),'') AS skuStdInfo,")

                // 产品sku图片
                .append(" (SELECT GROUP_CONCAT(PSI.IMAGE_URL) FROM product_sku_image PSI WHERE PSI.PRODUCT_SKU_ID = PS.id) AS skuImageInfo")

                .append(" FROM product_sku PS WHERE PS.product_id = :productId AND PS.`status` = 1");
        log.info("根据产品ID查询产品信息的SQL::{}", querySQL.toString());

        // 3、查询
        List<RetailProductSkuInfoVO> retailProductSkuInfoList = this.getNamedParameterJdbcTemplate().query(querySQL.toString(), param, new BeanPropertyRowMapper<>(RetailProductSkuInfoVO.class));

        // 4、转换价格：从韩币转为人民币
        if (retailProductSkuInfoList != null && retailProductSkuInfoList.size() > 0) {
            // 获取汇率
            BigDecimal krwCnyRate = exchangeRateUtil.getNowKrwCnyRate();
            if(krwCnyRate != null){
                for (RetailProductSkuInfoVO skuVO : retailProductSkuInfoList) {
                    skuVO.setPriceChn(skuVO.getPriceKor().multiply(krwCnyRate).setScale(2, BigDecimal.ROUND_UP));
                    skuVO.setSkuDiscPriceChn(skuVO.getSkuDiscPriceKor().multiply(krwCnyRate).setScale(2, BigDecimal.ROUND_UP));
                }
            }
        }

        return retailProductSkuInfoList;
    }

    /**
     * 获取韩币转人民币的汇率
     *
     * @return
     */
    public ExchangeRateDTO getNowExchangeRate() {
        return this.getNamedParameterJdbcTemplate().queryForObject(EXCHAGE_RATE, Maps.newHashMap(), new BeanPropertyRowMapper<>(ExchangeRateDTO.class));
    }
}