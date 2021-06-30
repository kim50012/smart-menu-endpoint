package com.basoft.eorder.foundation.jdbc.query;

import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.basoft.eorder.common.CommonConstants.BIZ_SHOPPING_STRING;

/**
 * @ProjectName: intellij_work
 * @Package: com.basoft.eorder.foundation.jdbc.query
 * @ClassName: JdbcProductQueryImpl
 * @Description:
 * @Author: liminzhe
 * @Date: 2018-12-17 15:18
 * @Version: 1.0
 */
@Slf4j
@Component("ProductQuery")
public class JdbcProductQueryImpl extends BaseRepository implements ProductQuery {
    private Logger logger = LoggerFactory.getLogger(JdbcProductQueryImpl.class);

    private final static String PRODUCT_BY_ID = "select " +
                "   p.id as id, " +
                "   p.recommend as recommend, " +
                "   p.name_kor as nameKor, " +
                "   p.name_chn as nameChn, " +
                "   p.weight as weight, " +
                "   p.status as status, " +
                "   p.store_id as storeId," +
                "   p.category_id as categoryId, " +
                "   p.file_id as fileId, " +
                "   p.des_kor as desKor, " +
                "   p.des_chn as desChn, " +
                "   p.detail_desc_kor as detailDesc, " +
                "   p.detail_desc_chn as detailChnDesc, " +
                "   p.IS_STANDARD as isStandard,"+
                "   p.IS_INVENTORY as isInventory,"+
                "   p.show_index as showIndex, " +
                "   p.created as created, " +
                "   p.update_time as updateTime, " +
                "    pi.image_url as imageUrl," +
                "    pi.image_type as imageType," +
                "    pi.created as imageCreated," +
                "    pi.update_time as imageUpdateTime"+
                "   from product p" +
                "       left join product_image pi on p.id = pi.product_id"+
                "   where p.id = ? ";

    private final static String PRODUCT_LIST_SELECT = "select " +
            "   p.id as id, " +
            "   p.name_kor as nameKor, " +
            "   p.name_chn as nameChn, " +
            "   p.weight as weight, " +
            "   p.des_kor as desKor, " +
            "   p.des_chn as desChn, " +
            "   p.detail_desc_kor as detailDesc, " +
            "   p.detail_desc_chn as detailChnDesc, " +
            "   p.status as status, " +
            "   p.store_id as storeId, " +
            "   p.category_id as categoryId, " +
            "   p.recommend as recommend, " +
            "   p.show_index as  showIndex, " +
            "   p.created as created, " +
            "   p.update_time as updateTime, " +
//            "   ppg.prd_group_id as prdGroupId, \n" +
            "   bc.name as categoryName, "+
            "   sku.price as defaultPrice, "+
//            "   pg.id as productGroupId , pg.name_kor as prodctGroupName,pg.name_chn as prodctGroupNameChn, "+
            "   s.name as storeName, "+
            "   a.area_nm as areaNm ";

    private final static String PRODUCT_FROM =
            " from product p " +
//            "   join product_prdgroup_map ppg on p.id = ppg.product_id " +
//        "left join file_mst fm on p.file_id = fm.file_id and fm.is_use = 1 " +
            "left join base_category bc on p.category_id = bc.id \n" +
//        " left join product_group pg on pg.category_id=bc.id and pg.status !=3 "+
            " left join store s on s.id=p.store_id "+
            " left join area a on s.city=a.area_cd "+
            " left join product_sku sku on p.id = sku.product_id and sku.use_default=1 "+
            " where 1 = 1  and p.status != 3" ;

    private final static String PRODUCT_GROUP_MAP_LIST_SELECT = "select " +
            "   p.id as id, " +
            "   p.name_kor as nameKor, " +
            "   p.name_chn as nameChn, " +
            "   p.des_kor as desKor, " +
            "   p.des_chn as desChn, " +
            "   p.detail_desc_kor as detailDesc, " +
            "   p.detail_desc_chn as detailChnDesc, " +
            "   p.status as status, " +
            "   p.store_id as storeId, " +
            "   p.category_id as categoryId, " +
            "   p.recommend as recommend, " +
            "   p.show_index as  showIndex, " +
            "   p.created as created, " +
            "   p.update_time as updateTime, " +
            "   ppg.prd_group_id as prdGroupId, \n" +
            "   ppg.show_index as showIndex, \n" +
            "   ppg.product_id as productId, \n" +
            "   bc.name as categoryName, " +
            "   sku.price as defaultPrice, " +
            "   s.name as storeName ";

    private final static String PRODUCT_GROUP_MAP_LIST_FROM =
            " from product p \n" +
            " join product_prdgroup_map ppg on p.id = ppg.product_id \n" +
            " join store s on s.id=p.store_id \n"+
            " left join base_category bc on p.category_id = bc.id \n" +
            " left join product_sku sku on p.id = sku.product_id and sku.use_default=1 \n"+
            " where 1 = 1  \n" +
            " and p.status != 3 \n" ;

    private final static String PRODUCT_SKU_BY_ID = "select " +
                "   ps.id as id, " +
                "   ps.name_kor as name, " +
                "   ps.name_kor as nameKor, " +
                "   ps.name_chn as nameChn, " +
                "   ps.weight as weight, " +
                "   ps.price as priceKor, " +
                "   ps.price_weekend as priceWeekend, " +
                "   ps.product_id as productId, " +
                "   ps.use_default as useDefault, " +
                "   ps.created as created " +
            "   from product_sku ps " +
            "   where ps.id = :id ";

    private final static String SELECT_CHECK_OPTION =
            " select ssm.sku_id, ssm.prd_standard_id, ssm.prd_standard_name_val as prdStandardNameVal, " +
            " ssm.product_id,ssm.name_chn as chnName \n from sku_standard_map ssm \n" +
            " where sku_id=:skuId";

    private final static String PRODUCT_SKU_LIST_SELECT = "select " +
            "   ps.id as id, " +
            "   ps.name_kor as name, " +
            "   ps.name_kor as nameKor, " +
            "   ps.name_chn as nameChn, " +
            "   ps.weight as weight," +
            "   ps.price as priceKor, " +
            "   ps.price_weekend as priceWeekend, " +
            "   ifnull(ps.PRICE_SETTLE,0) as priceSettle, " +
            "   ps.product_id as productId, " +
            "   ps.created as created, " +
            "   ps.use_default as useDefault, " +
            "   ps.IS_INVENTORY as isInventory, " +
            "   ps.dis_order as disOrder,"+
            "   (select IMAGE_URL from product_sku_image psi where psi.PRODUCT_SKU_ID=ps.id and psi.IMAGE_TYPE=0 limit 1)as mainImageUrl,"+
            "   p.des_kor as desKor, " +
            "   p.des_chn as desChn, " +
            "   p.store_id as storeId, " +
            "   ssm.prd_standard_id  as optionId," +
            "   ssm.prd_standard_name_val as optionNameKor," +
            "   ifnull(ssm.STANDARD_TYPE,0) as baseExtend," +
            "   ssm.name_chn as optionNameChn,\n" +
            "   ps.status";


    private final static String PRODUCT_SKU_FROM =
            "  from product_sku ps\n" +
            "   join product p on ps.product_id = p.id\n" +
            "   left join sku_standard_map ssm on ps.id = ssm.sku_id\n"+
            "   where 1 = 1 ";

    private final static String PRODUCT_GROUP_LIST_SELECT = "   select " +
                "   pg.id as id, " +
                "   name_kor as nameKor, " +
                "   name_chn as nameChn, " +
                "   store_id as storeId, " +
                "   s.name as storeName, " +
                "   a.area_nm as areaNm, " +
                "   pg.show_index," +
                " pg.created_dt as created,"+
                " pg.update_time as updateTime,"+
                "       pg.status  " +
                "   from product_group pg " +
                "   LEFT JOIN store s on pg.store_id=s.id "+
                "   LEFT JOIN area a on s.city=a.area_cd";

    private final static String PRODUCT_GROUP_COUNT_SELECT = "   select " +
                "   count(1) " +
                "   from product_group pg " +
                "   LEFT JOIN store s on pg.store_id=s.id "+
                "   LEFT JOIN area a on a.area_cd = s.city " ;


    private final static String PRODUCT_GROUP_LIST_WHERE = " where 1 = 1 ";
    private final static String PRODUCT_GROUP_LIST_ORDER = " order by show_index ";

    //  update exchange_rate set end_dt = adddate(now(), interval 20 year)
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

    JdbcProductQueryImpl(){
    }

    @Autowired
    public JdbcProductQueryImpl(DataSource dataSource) {
        super(dataSource);
    }

    static final class ProductColumn extends ProductDTO {
        private String imageUrl;
        private Integer imageType;
        private String imageCreated;
        private String imageUpdateTime;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Integer getImageType() {
            return imageType;
        }

        public void setImageType(Integer imageType) {
            this.imageType = imageType;
        }

        public ProductDTO toProductDTO() {
            ProductDTO pt = new ProductDTO();
            pt.setId(this.getId());
            pt.setRecommend(this.getRecommend());
            pt.setNameKor(this.getNameKor());
            pt.setNameChn(this.getNameChn());
            pt.setWeight(this.getWeight());
            pt.setStatus(this.getStatus());
            pt.setIsInventory(this.getIsInventory());
            pt.setIsStandard(this.getIsStandard());
            pt.setStoreId(this.getStoreId());
            pt.setCategoryId(this.getCategoryId());
            pt.setFileId(this.getFileId());
            pt.setDesKor(this.getDesKor());
            pt.setDesChn(this.getDesChn());
            pt.setDetailChnDesc(this.getDetailChnDesc());
            pt.setDetailDesc(this.getDetailDesc());
            pt.setUpdateTime(this.getUpdateTime());
            return pt;
        }

        public String getImageCreated() {
            return imageCreated;
        }

        public void setImageCreated(String imageCreated) {
            this.imageCreated = imageCreated;
        }

        public String getImageUpdateTime() {
            return imageUpdateTime;
        }

        public void setImageUpdateTime(String imageUpdateTime) {
            this.imageUpdateTime = imageUpdateTime;
        }

        public ProductImageDTO toImage() {
            ProductImageDTO imageDTO = new ProductImageDTO();
            imageDTO.setProductId(this.getId());
            imageDTO.setImageUrl(this.getImageUrl());
            imageDTO.setImageType(this.getImageType());
            imageDTO.setUpdateTime(this.imageUpdateTime);
            imageDTO.setCreated(this.imageCreated);
            return imageDTO;
        }
    }

    /**********************************************Wechat端-START******************************************************/
    /**
     * Wechat端-获取产品组
     *
     * @param param
     * @return
     */
    @Override
    public List<ProductGroupDTO> getProductGroupListByMap(Map<String, Object> param) {
        StringBuilder query = new StringBuilder(PRODUCT_GROUP_LIST_SELECT + PRODUCT_GROUP_LIST_WHERE);
        pdGroupQrConditions(query,param,true);
        log.info("GetProductGroupListByMap::SQL:"+query.toString());
        return this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<>(ProductGroupDTO.class));
    }

    /**
     * Wechat端-获取产品列表
     *
     * @param param
     * @return
     */
    @Override
    public List<ProductGroupMapDTO> getProductGroupMapListByMap(Map<String, Object> param) {
        Long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        String status = Objects.toString(param.get("status"), null);
        String recommend = Objects.toString(param.get("recommend"),null);
        StringBuilder query = new StringBuilder(PRODUCT_GROUP_MAP_LIST_SELECT + PRODUCT_GROUP_MAP_LIST_FROM);

        if (storeId != null && storeId > 0) {
            query.append("   and p.store_id = :storeId ");
        }

        // NORMAL(0), OPEN(1), CLOSED(2), DELETED(3)。0和1都是上架状态
        if (StringUtils.isNotBlank(status)) {
            // 查询条件status为0或1则查询产品状态为0或1的，即上架的产品
            if (status.equals("1") || status.equals("0")) {
                query.append(" and p.status !=2 and p.status !=3 ");
            }
            // 查询条件status为2或3则查询产品状态为2或3的，即下架产品或已删除的产品
            else if (status.equals("2") || status.equals("3")) {
                query.append(" and p.status !=0 and p.status !=1 ");
            }
        }

        if(StringUtils.isNotBlank(recommend)){
            query.append(" and p.recommend = :recommend ");
        }
        query.append(" order by ppg.show_index asc ");
        log.info("GetProductGroupMapListByMap::SQL"+query.toString());
        // 查询产品列表
        List<ProductGroupMapDTO> pgmList =  this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<>(ProductGroupMapDTO.class));

        // 对产品列表进行二次加工：获取产品图片、获取产品的SKU并对价格进行转换
        if (pgmList != null && pgmList.size() > 0) {
            // 产品图片-start
            // 获取产品ID的List
            List<Long> prdIds = pgmList.stream().map(ProductGroupMapDTO :: getId).collect(Collectors.toList());
            // 将产品ID列表prdIds中的产品图片信息查询出来（查询结果已经按照产品ID将产品图片信息进行分组并装入List）
            final Map<Long, List<ProductImageDTO>> listMap = getProductWithImage(prdIds.toArray(new Long[prdIds.size()]));
            if (listMap != null) {
                // 将产品图片列表根据产品ID放入pgmList中的产品对象
                pgmList.forEach(p -> p.setImageList(listMap.get(p.getProductId())));
            }
            // 产品图片-end

            // 产品SKU-start
            // 从产品列表中检索并去重获取门店编号的列表
            List<Long> storeIdList = pgmList.stream().map(ProductGroupMapDTO::getStoreId).distinct().collect(Collectors.toList());
            // 获取门店列表中所有产品的SKU列表
            Map<Long, List<ProductSkuDTO>> group = getProductWithSku(storeIdList.toArray(new Long[storeIdList.size()]));
            // 根据产品编号将该产品的SKU列表放入pgmList中的产品对象
            if (group != null) {
                pgmList.forEach(p ->{
                    p.setPsdList(group.get(p.getProductId()));
                });
            }
            // 产品SKU-end
        }
        return pgmList;
    }

    /**
     * 根据产品列表获取产品图片列表
     *
     * @param ids
     * @return
     */
    private Map<Long,List<ProductImageDTO>> getProductWithImage(Long ... ids) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("prodIds", Arrays.asList(ids));
        List<ProductImageDTO> pidList = getProductImageListByMap(param);
        return  pidList.stream().collect(Collectors.groupingBy(ProductImageDTO::getProductId));
    }

    /**
     * 从数据库查询产品图片列表
     *
     * @param param
     * @return
     */
    public List<ProductImageDTO> getProductImageListByMap(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder("select pi.created as created, pi.image_type as imageType, pi.image_url as imageUrl, pi.product_id as productId, pi.update_time as updateTime from product_image pi where 1 = 1 \n");
        String prodIds = Objects.toString(param.get("prodIds"), null);
        if (StringUtils.isNotBlank(prodIds)) {
            sql.append("and pi.product_id in (:prodIds)");
        }
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(ProductImageDTO.class));
    }

    /**
     * 根据门店编号列表查询所有门店的所有产品SKU列表
     *
     * @param storeIdList
     * @return
     */
    private Map<Long, List<ProductSkuDTO>> getProductWithSku(Long ... storeIdList) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeIds", Arrays.asList(storeIdList));
        // 1.查询门店列表中所有门店所有的SKU列表
        List<ProductSkuDTO> skuList =  getProductSkuListByMap(param);



        // 2.汇率转换
        if (skuList != null && skuList.size() > 0) {
            // 获取汇率
            ExchangeRateDTO erd = getNowExchangeRate();

            // 循环进行韩币和人民币转换
            if (erd != null) {
                for (ProductSkuDTO psd : skuList) {
                    //제품에 대한 환율 가격을 KRW 에서 CNY 로 바로 변환 기준 : alliex 환율 기준 by dikim on 20190304
                    //BigDecimal price = psd.getPriceKor().divide(erd.getUsdkrwRate(), 8, BigDecimal.ROUND_UP).multiply(erd.getUsdcnyRate()).setScale(2, BigDecimal.ROUND_UP);
                    BigDecimal price = psd.getPriceKor().multiply(erd.getKrwcnyRate()).setScale(2, BigDecimal.ROUND_UP);
                    psd.setPriceChn(price);
                }
            }

            // 3.将SKU按照产品ID进行分组
            return skuList.stream().collect(Collectors.groupingBy(ProductSkuDTO::getProductId));
        }
        return null;
    }

    /**
     * 根据参数查询产品SKU列表
     *
     * @param param
     * @return
     */
    @Override
    public List<ProductSkuDTO> getProductSkuListByMap(Map<String, Object> param) {
        String nameKor = Objects.toString(param.get("nameKor"), null);
        long productId = NumberUtils.toLong(Objects.toString(param.get("productId"), null));
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        long categoryId = NumberUtils.toLong(Objects.toString(param.get("categoryId"), null));
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        List<Long> storeIds = param.get("storeIds") != null ? (List<Long>) param.get("storeIds") : null;
        List<Long> skuIds = param.get("skuIds") != null ? (List<Long>) param.get("skuIds") : null;

        StringBuilder query = new StringBuilder(PRODUCT_SKU_LIST_SELECT + PRODUCT_SKU_FROM);

        if (StringUtils.isNotBlank(nameKor)) {
            query.append(" and ps.name = :nameKor \n");
        }

        if (productId > 0) {
            query.append(" and ps.product_id = :productId \n");
        }

        if (storeId > 0) {
            query.append(" and p.store_id = :storeId \n");
        }

        if (storeIds != null && storeIds.size() > 0) {
            query.append(" and p.store_id in (:storeIds) \n");
        }

        if (skuIds != null && skuIds.size() > 0) {
            query.append(" and ps.id in (:skuIds) \n");
        }

        if (categoryId > 0) {
            query.append(" and p.category_id = :category \n ");
        }
        if (categoryId > 0) {
            query.append(" and ps.status !=0  \n ");
        }
        query.append(" order by p.id \n");

        if (page >= 0 && size > 0) {
            int resPage = page;
            if(page > 0){
                resPage = (resPage-1)*10;
                param.put("page",resPage);
            }
            query.append(LIMIT);
        }

        String sql = query.toString();
        logger.debug("SQL:"+sql);

        //return toProductDTO(this.getNamedParameterJdbcTemplate().query(sql, param,new BeanPropertyRowMapper<>(ProductSKuDTOBuilder.class)));
        // 产品规格SKU列表
        List<ProductSKuDTOBuilder> productSKuDTOBuilderList = this.getNamedParameterJdbcTemplate().query(sql, param,new BeanPropertyRowMapper<>(ProductSKuDTOBuilder.class));
        // 对产品规格SKU按照产品分组
        List<ProductSkuDTO> productSkuList = toProductDTO(productSKuDTOBuilderList);
        return productSkuList;
    }

    /**
     * 获取韩币转人民币的汇率
     *
     * @return
     */
    @Override
    public ExchangeRateDTO getNowExchangeRate() {
        return this.getNamedParameterJdbcTemplate().queryForObject(EXCHAGE_RATE, Maps.newHashMap(), new BeanPropertyRowMapper<>(ExchangeRateDTO.class));
    }
    /**********************************************Wechat端-END******************************************************/








    @Override
    public ProductDTO getProductById(Long id) {
        Map<Long, ProductDTO> dtoMap = new HashMap<>();
        this.getJdbcTemplate().query(PRODUCT_BY_ID, new Object[]{id}, new BeanPropertyRowMapper<>(ProductColumn.class))
                .stream()
                .forEach(productColumn -> {
                    ProductDTO dto = dtoMap.get(productColumn.getId());
                    if (dto == null) {
                        dto = productColumn.toProductDTO();
                        dtoMap.put(productColumn.getId(), dto);
                    }
                    dto.addImage(productColumn.toImage());
                });
        return dtoMap.values().stream().findFirst().orElse(null);
    }

    @Override
    public int getProductCountByMap(Map<String, Object> param) {
        StringBuilder query = new StringBuilder(SELECT_COUNT + PRODUCT_FROM);
        query.append(getQueryConditions(param, null, false));
        return this.getNamedParameterJdbcTemplate().queryForObject(query.toString(), param, Integer.class);
    }

    private String getOrderbyConditionOfProduct(String obf) {
        if ("created".equalsIgnoreCase(obf)) {
            return " order by p.created desc\n";
        } else if ("updated".equalsIgnoreCase(obf)) {
            return " order by p.update_time desc \n";
        } else {
            return " order by p.show_index asc, p.id \n";
        }
    }

    @Override
    public List<ProductDTO> getProductListByMap(Map<String, Object> param) {
        String storeType = Objects.toString(param.get("storeType"),"");
        String orderByField = (String) param.get("orderBy");
        String orderByCondition = this.getOrderbyConditionOfProduct(orderByField);
        StringBuilder query = new StringBuilder(PRODUCT_LIST_SELECT + PRODUCT_FROM);
        query.append(getQueryConditions(param, orderByCondition, true));

        logger.debug("SQL:" + query.toString());
        List<ProductDTO> pdList = this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<>(ProductDTO.class));
        if (pdList != null && pdList.size() > 0) {
            pdList = getProductWithImage(pdList);
            return getProductWithSku(pdList,storeType);
        }
        return pdList;
    }

    private ProductDTO getProductWithImage(ProductDTO pd) {
        return getProductWithImage(Lists.newArrayList(pd)).get(0);
    }

    private List<ProductDTO> getProductWithImage(List<ProductDTO> pdList) {
        final List<Long> collect = pdList.stream().map(ProductDTO::getId).collect(Collectors.toList());
        final Map<Long, List<ProductImageDTO>> group = getProductWithImage(collect.toArray(new Long[collect.size()]));
        if (group != null) {
            pdList.forEach(p -> p.setImageList(group.get(p.getId())));
        }
        return pdList;
    }

    private ProductDTO getProductWithSku(ProductDTO pd,String storeType) {
        return getProductWithSku(Lists.newArrayList(pd),storeType).get(0);
    }

    private List<ProductDTO> getProductWithSku(List<ProductDTO> pdList,String storeType) {
        List<Long> storeIdList = pdList.stream().map(ProductDTO::getStoreId).distinct().collect(Collectors.toList());
        Map<Long, List<ProductSkuDTO>> group = getProductWithSku(storeIdList.toArray(new Long[storeIdList.size()]));
        if (group != null) {
            pdList.forEach(p -> {
                BigDecimal minPrice = group.get(p.getId()).stream().map(g -> g.getPriceKor()).min(BigDecimal::compareTo).get();
                p.setPsdList(group.get(p.getId()));
                p.setMinPrice(minPrice);
                if (BIZ_SHOPPING_STRING.equals(storeType)) {
                    List<ProductSkuDTO> skuList = group.get(p.getId());
                    skuList = skuList.stream().filter(s->s.getStatus()!=2).collect(Collectors.toList());
                    skuList.sort(Comparator.comparingInt(ProductSkuDTO::getDisOrder));
                    p.setPsdList(skuList);
                } else {
                    p.setPsdList(group.get(p.getId()));
                }
            });
        }
        return pdList;
    }


    /*private <T> List<T>  getProductWithSku(List<T> pdList, Class<T> clazz) {
        Map<String, Object> param = Maps.newHashMap();
        List<Long> storeIdList = null;
        List groupList = null;
        if (clazz.newInstance() instanceof ProductDTO) {
            storeIdList = ((List<ProductDTO>)pdList).stream().map(ProductDTO ::getStoreId).distinct().collect(Collectors.toList());
        } else if (clazz.newInstance() instanceof ProductGroupMapDTO){
            storeIdList = ((List<ProductGroupMapDTO>)pdList).stream().map(ProductGroupMapDTO ::getStoreId).distinct().collect(Collectors.toList());
        }

        if (storeIdList == null || storeIdList.size() == 0)
            return null;
        // List<Long> storeIdList = ((List<ProductDTO>)list).stream().map(ProductDTO ::getStoreId).distinct().collect(Collectors.toList());

        param.put("storeIds", storeIdList);

        List<ProductSkuDTO> psdList = getProductSkuListByMap(param);


        ExchangeRateDTO erd = getNowExchangeRate();

        if (psdList != null && psdList.size() > 0) {
            for (ProductSkuDTO psd : psdList) {
                BigDecimal price = psd.getPriceKor().divide(erd.getUsdkrwRate(), 8, BigDecimal.ROUND_UP).multiply(erd.getUsdcnyRate()).setScale(2, BigDecimal.ROUND_UP);
                psd.setPriceChn(price);
            }


            Map<Long, List<ProductSkuDTO>> groups = psdList.stream().collect(Collectors.groupingBy(ProductSkuDTO::getProductId));

            if (clazz.newInstance() instanceof ProductDTO) {
                groupList = ((List<ProductDTO>)pdList).stream().map(product -> {
                    Long id = product.getId();
                    List<ProductSkuDTO> psdList1 = groups.get(id);
                    if (psdList1 != null && psdList1.size() > 0) {
                        for (ProductSkuDTO productSkuDTO : psdList1) {
                            if (psdList1 !=null&&productSkuDTO != null) {
                                List<SkuStandardMapDTO> optionList = getOptionBySkuId(productSkuDTO.getId());
                                productSkuDTO.setOptionList(optionList);
                            }
                        }
                        product.setPsdList(psdList1);
                    }
                    return product;
                }).collect(Collectors.toList());

            } else if (clazz.newInstance() instanceof ProductGroupMapDTO){
                groupList = ((List<ProductGroupMapDTO>)pdList).stream().map(product -> {
                    Long id = product.getId();
                    List<ProductSkuDTO> psdList1 = groups.get(id);
                    if (psdList1 != null && psdList1.size() > 0) {
                        for (ProductSkuDTO productSkuDTO : psdList1) {
                            if (psdList1 !=null&&productSkuDTO != null) {
                                List<SkuStandardMapDTO> optionList = getOptionBySkuId(productSkuDTO.getId());
                                productSkuDTO.setOptionList(optionList);
                            }
                        }
                        product.setPsdList(psdList1);
                    }
                    return product;
                }).collect(Collectors.toList());
            }
            return groupList;
        }

        return pdList;
    }*/

    /**
     * @param  id
     * @return java.util.List<java.lang.Long>
     * @describe 根据skuId查询option
     * @author Dong Xifu
     * @date 2019/1/2 下午8:05
     */
    private List<SkuStandardMapDTO> getOptionBySkuId(Long id) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("skuId", id);
        return this.getNamedParameterJdbcTemplate().query(SELECT_CHECK_OPTION, param,new BeanPropertyRowMapper<>(SkuStandardMapDTO.class));
    }

    @Override
    public ProductSkuDTO getProductSkuById(Long id) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("id", id);
        return this.queryForObject(PRODUCT_SKU_BY_ID, param, new BeanPropertyRowMapper<>(ProductSkuDTO.class));
    }

    @Override
    public List<ProductSkuDTO> getProductSkuByProId(Long productId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("productId", productId);
        String sql = "select " +
            "   ps.id as id, " +
            "   ps.name_kor as name, " +
            "   ps.name_kor as nameKor, " +
            "   ps.name_chn as nameChn, " +
            "   ps.price as priceKor, " +
            "   ps.price_weekend as priceWeekend, " +
            "   ps.product_id as productId, " +
            "   ps.use_default as useDefault, " +
            "   ps.created as created " +
            "   from product_sku ps " +
            "   where ps.product_id = :productId ";
        return this.getNamedParameterJdbcTemplate().query(sql, param,new BeanPropertyRowMapper<>(ProductSkuDTO.class));
    }

    @Override
    public int getProductSkuCountByMap(Map<String, Object> param) {
        String nameKor = Objects.toString(param.get("nameKor"), null);
        long productId = NumberUtils.toLong(Objects.toString(param.get("productId"), null));
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        long categoryId = NumberUtils.toLong(Objects.toString(param.get("categoryId"), null));

        StringBuilder query = new StringBuilder(SELECT_COUNT + PRODUCT_SKU_FROM);

        if (StringUtils.isNotBlank(nameKor)) {
            query.append(" and ps.name = :nameKor \n");
        }

        if (productId > 0) {
            query.append(" and ps.product_id = :productId \n");
        }

        if (storeId > 0) {
            query.append(" and p.store_id = :storeId \n");
        }

        if (categoryId > 0) {
            query.append(" and p.category_id = :category \n ");
        }
        return this.getNamedParameterJdbcTemplate().queryForObject(query.toString(), param, Integer.class);
    }

    public List<ProductSkuDTO> toProductDTO(List<ProductSKuDTOBuilder> builderList){
        Map<Long,ProductSkuDTO> skuDTOMap = new HashMap<>();
        builderList.stream().forEach(new Consumer<ProductSKuDTOBuilder>() {
            @Override
            public void accept(ProductSKuDTOBuilder b) {
                ProductSkuDTO skuDTO = skuDTOMap.get(b.getId());
                if(skuDTO == null){
                    skuDTO = b.build();
                    skuDTOMap.put(b.getId(),skuDTO);
                }
                SkuStandardMapDTO map = b.buildOption();
                skuDTO.addStandardMap(map);
            }
        });
        return skuDTOMap.values().stream().collect(Collectors.toList());
    }

    @Override
    public int getproductGroupCount(Map<String, Object> param) {
        StringBuilder query = new StringBuilder(PRODUCT_GROUP_COUNT_SELECT + PRODUCT_GROUP_LIST_WHERE);
        pdGroupQrConditions(query, param, false);
        return this.getNamedParameterJdbcTemplate().queryForObject(query.toString(), param, Integer.class);
    }

    String getOrderByStrOfProductGroup(String inOrderFeild) {
        if ("created".equalsIgnoreCase(inOrderFeild)) {
            return " order by pg.created desc\n";
        } else if ("updated".equalsIgnoreCase(inOrderFeild)) {
            return " order by pg.update_time desc \n";
        } else {
            return PRODUCT_GROUP_LIST_ORDER;
        }
    }

    /**
     * @param  query, param, isLimit
     * @return java.lang.StringBuilder
     * @describe 封装栏目查询条件
     * @author Dong Xifu
     * @date 2019/1/4 下午1:46
     */
    private StringBuilder pdGroupQrConditions(StringBuilder query, Map<String, Object> param, Boolean isLimit){
        Long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        String storeName = Objects.toString(param.get("storeName"), null);
        String storeType = Objects.toString(param.get("storeType"), null);
        String city = Objects.toString(param.get("city"), null);
        String areaNm = Objects.toString(param.get("areaNm"), null);
        String nameKor = Objects.toString(param.get("nameKor"), null);
        String nameChn = Objects.toString(param.get("nameChn"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        Integer status = (Integer) param.get("status");
        // String categoryName = Objects.toString(param.get("categoryName"), null);
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        if (storeId != null && storeId > 0) {
            query.append("   and store_id = :storeId ");
        }
        if (StringUtils.isNotBlank(storeName)) {
            query.append("   and s.name like '%' :storeName '%' ");
        }
        if (StringUtils.isNotBlank(storeType)) {
            query.append("   and s.store_type =  :storeType ");
        }
        if (StringUtils.isNotBlank(city)) {
            query.append("   and s.city =  :city ");
        }
        if (StringUtils.isNotBlank(areaNm)) {
            query.append("   and a.area_nm = :areaNm ");
        }
        if (StringUtils.isNotBlank(nameKor)) {
            query.append(" and name_kor = :nameKor \n");
        }
        if (StringUtils.isNotBlank(nameChn)) {
            query.append(" and name_chn = :nameChn \n");
        }

        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            query.append(" and pg.update_time >= :startTime and pg.update_time<=:endTime ");
        }

        /*if (StringUtils.isNotBlank(categoryName)) {
            query.append(" and bc.name = :categoryName \n");
        }*/

        if(status != null&&status == -1){
            query.append(" and pg.status !=0  \n");
        }else if(status != null && status != -1){
            query.append(" and pg.status = :status \n");
        }

        query.append(" and pg.status != 3 ");

        String orderByStr = (String)param.get("orderBy");
        query.append(getOrderByStrOfProductGroup(orderByStr));
        appendPage(page,size,query,param,isLimit);

        return query;
    }

    static final String FECH_MENU_ITEM_SQL = "select " +
            " pm.product_id," +
            " pm.prd_group_id," +
            " pm.show_index," +
            " p.name_chn," +
            " p.name_kor," +
            " p.status" +
            " from product_prdgroup_map pm\n" +
            " join product p on p.id = pm.product_id " +
            " where prd_group_id = ? and p.store_id = ?  and p.status !=3 order by pm.show_index asc";

    @Override
    public List<MenuItemDTO> getMenuListOfGroup(Long storeId, Long groupId) {
        return this.getJdbcTemplate().query(FECH_MENU_ITEM_SQL, new Object[]{groupId, storeId}, (resultSet, i) -> {
            MenuItemDTO menu = new MenuItemDTO();
            menu.setProductGroupId(resultSet.getLong("prd_group_id"));
            menu.setProductId(resultSet.getLong("product_id"));
            menu.setShowIndex(resultSet.getInt("show_index"));
            menu.setName(resultSet.getString("name_kor"));
            menu.setNameChn(resultSet.getString("name_chn"));
            menu.setStatus(resultSet.getInt("status"));
            return menu;
        });
    }

    /*private String getQueryConditions(Map<String, Object> param,Boolean isLimit) {
        return getQueryConditions(param, null,isLimit);
    }*/

    private String getQueryConditions(Map<String, Object> param, String orderBy, Boolean isLimit) {
        String nameKor = Objects.toString(param.get("nameKor"), null);
        String nameChn = Objects.toString(param.get("nameChn"), null);
        String storeName = Objects.toString(param.get("storeName"), null);
        String storeType = Objects.toString(param.get("storeType"), null);
        String areaNm = Objects.toString(param.get("areaNm"), null);
        String city = Objects.toString(param.get("city"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String isDeposit = Objects.toString(param.get("isDeposit"), "0");
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        Integer status = (Integer) param.get("status");

        String categroyStrId = Objects.toString(param.get("categoryId"), null);
        Long categoryId = StringUtils.isEmpty(categroyStrId) ? null : Long.parseLong(categroyStrId);

        StringBuilder sb = new StringBuilder();

        if (StringUtils.isNotBlank(nameKor)) {
            sb.append(" and p.name_kor like '%' :nameKor '%' \n");
        }

        if (StringUtils.isNotBlank(nameChn)) {
            sb.append(" and p.name_chn like '%' :nameChn '%'\n");
        }

        if (categoryId != null) {
            sb.append(" and p.category_id = :categoryId \n");
        }

        if (StringUtils.isNotBlank(storeName)) {
            sb.append(" and s.name like '%' :storeName '%' \n");
        }

        if (StringUtils.isNotBlank(storeType)) {
            sb.append(" and s.store_type = :storeType  \n");
        }
        if (StringUtils.isNotBlank(city)) {
            sb.append(" and s.city = :city  \n");
        }

        if (StringUtils.isNotBlank(areaNm)) {
            sb.append(" and a.area_nm like '%' :areaNm '%' \n");
        }

        if (storeId > 0) {
            sb.append(" and p.store_id = :storeId \n");
        }

        if (StringUtils.isNotBlank(isDeposit)) {
            sb.append(" and p.is_deposit = :isDeposit");
        }

        if (status != null && status == -1) {
            sb.append(" and p.status !=0  \n");
        } else if (status != null && status != -1) {
            sb.append(" and p.status = :status \n");
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sb.append(" and p.update_time >= :startTime and p.update_time<=:endTime ");
        }

        if (StringUtils.isNotBlank(orderBy)) {
            sb.append(orderBy);
        }

        if (page >= 0 && size > 0 && isLimit) {
            int resPage = page;
            if (page > 0) {
                resPage = (resPage - 1) * size;
                param.put("page", resPage);
            }
            sb.append(LIMIT);
        }
        return sb.toString();
    }

    static class ProductSKuDTOBuilder extends ProductSkuDTO {
        private Long optionId;
        private String optionNameKor;
        private String optionNameChn;
        private int baseExtend;

        public Long getOptionId() {
            return optionId;
        }

        public void setOptionId(Long optionId) {
            this.optionId = optionId;
        }

        public String getOptionNameKor() {
            return optionNameKor;
        }

        public void setOptionNameKor(String optionNameKor) {
            this.optionNameKor = optionNameKor;
        }

        public String getOptionNameChn() {
            return optionNameChn;
        }

        public void setOptionNameChn(String optionNameChn) {
            this.optionNameChn = optionNameChn;
        }

        public int getBaseExtend() {
            return baseExtend;
        }

        public void setBaseExtend(int baseExtend) {
            this.baseExtend = baseExtend;
        }

        public ProductSkuDTO build() {
            ProductSkuDTO dto = new ProductSkuDTO();
            dto.setId(this.getId());
            dto.setName(this.getNameKor());
            dto.setNameKor(this.getNameKor());
            dto.setNameChn(this.getNameChn());
            dto.setWeight(this.getWeight());
            dto.setPriceKor(this.getPriceKor());
            dto.setPriceChn(this.getPriceChn());
            dto.setPriceSettle(this.getPriceSettle());
            dto.setUseDefault(this.getUseDefault());
            dto.setProductId(this.getProductId());
            dto.setCreated(this.getCreated());
            dto.setCategoryId(this.getCategoryId());
            dto.setStoreId(this.getStoreId());
            dto.setDiscId(this.getDiscId());
            dto.setDiscPriceKor(this.getDiscPriceKor());
            dto.setDiscPriceChn(this.getDiscPriceChn());
            dto.setIsInventory(this.getIsInventory());
            dto.setDisOrder(this.getDisOrder());
            dto.setMainImageUrl(this.getMainImageUrl());
            dto.setStatus(this.getStatus());
            return dto;
        }

        public SkuStandardMapDTO buildOption() {
            SkuStandardMapDTO sdto = new SkuStandardMapDTO();
            sdto.setPrdStandardId(this.optionId);
            sdto.setChnName(this.optionNameChn);
            sdto.setPrdStandardNameVal(this.optionNameKor);
            sdto.setBaseExtend(this.baseExtend);
            sdto.setProductId(this.getProductId());
            sdto.setSkuId(this.getId());
            return sdto;
        }
    }


    /**
     * 对wechat H5端店铺列表中的商品最低韩币价格转为人民币
     *
     * @param minPriceKorList
     */
    public void currencyConverter(List<Map> minPriceKorList) {
        // 获取汇率
        ExchangeRateDTO erd = getNowExchangeRate();
        for (Map m : minPriceKorList) {
            // 韩币价格不为null
            if (m.get("minPriceKor") != null) {
                BigDecimal minPriceKor = (BigDecimal) m.get("minPriceKor");
                BigDecimal minPriceChn = minPriceKor.multiply(erd.getKrwcnyRate()).setScale(2, BigDecimal.ROUND_UP);
                m.put("minPriceChn", minPriceChn);
            }
        }
    }
}