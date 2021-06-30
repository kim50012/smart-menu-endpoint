package com.basoft.eorder.foundation.jdbc.query.retail.cms;

import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.ProductImageDTO;
import com.basoft.eorder.interfaces.query.retail.cms.InventoryRetailDTO;
import com.basoft.eorder.interfaces.query.retail.cms.InventoryRetailQuery;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 零售产品库存表查询
 *
 * @author DongXifu
 * @since 2020-04-13 11:18:06
 */
@Component("InventoryRetailQuery")
public class JdbcInventoryRetailQueryImpl extends BaseRepository implements InventoryRetailQuery {

    private final static String INVENTORY_RETAIL_SELECT = "SELECT STORE_ID,PROD_ID,PROD_SKU_ID,INV_TOTAL,INV_SOLD,INV_BALANCE,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER\n";
    private final static String INVENTORY_RETAIL_FROM = " FROM inventory_retail ir  WHERE 1=1\n";


    @Override
    public List<InventoryRetailDTO> getInventoryRetailListByMap(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder("SELECT p.id as prodId,ir.PROD_SKU_ID as prodSkuId,p.name_chn as nameChn,p.name_kor as namekor,ps.id as skuId,ir.INV_ID\n" +
                ",ps.IS_INVENTORY,ifnull(ir.INV_TOTAL,0) as invTotal,ifnull(ir.INV_BALANCE,0) as invBalance \n" +
                ",case ps.IS_INVENTORY when 1 then case when INV_TOTAL>0 then 1 else 0 end else 1 end status\n" +
                "from product p\n" +
                "INNER JOIN product_sku ps on p.id=ps.product_id\n" +
                "INNER JOIN inventory_retail ir on ps.id=ir.PROD_SKU_ID where 1=1 \n");
        getProductRetailCondition(sql, param);
        orderByAndPage(param, sql, " ORDER BY CREATE_TIME desc");

        List<InventoryRetailDTO> inventoryRetailDtoList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(InventoryRetailDTO.class));
        return inventoryRetailDtoList;
    }


    @Override
    public int getProductRetailCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder("SELECT count(1)\n" +
                " from product p where 1=1 \n");
        getProductRetailCondition(sql, param);
        sql.append(" and p.status !=3 and  p.status !=2");

        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<InventoryRetailDTO> getProductRetailListByMap(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder("SELECT p.id as prodId,p.name_chn as nameChn,p.name_kor as namekor,p.IS_STANDARD\n" +
                " from product p where 1=1 \n");
        getProductRetailCondition(sql, param);
        sql.append(" and p.status !=3 and  p.status !=2");
        orderByAndPage(param, sql, " ORDER BY created desc");

        List<InventoryRetailDTO> inventoryRetailDtoList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(InventoryRetailDTO.class));

        List<Long> prdIds = inventoryRetailDtoList.stream().map(InventoryRetailDTO::getProdId).collect(Collectors.toList());

        if(prdIds!=null&&prdIds.size()>0) {
            final Map<Long, List<ProductImageDTO>> listMap = getProductWithImage(prdIds.toArray(new Long[prdIds.size()]));
            if (listMap != null) {
                // 将产品图片列表根据产品ID放入pgmList中的产品对象
                inventoryRetailDtoList.forEach(i -> i.setImageList(listMap.get(i.getProdId())));
            }
        }
        return inventoryRetailDtoList;
    }


    /**
     * 根据产品列表获取产品图片列表
     *
     * @param ids
     * @return
     */
    private Map<Long, List<ProductImageDTO>> getProductWithImage(Long... ids) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("prodIds", Arrays.asList(ids));
            List<ProductImageDTO> pidList = getProductImageListByMap(param);
        return pidList.stream().collect(Collectors.groupingBy(ProductImageDTO::getProductId));
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

    private StringBuilder getProductRetailCondition(StringBuilder sql, Map<String, Object> param) {

        String storeId = Objects.toString(param.get("storeId"), null);
        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and p.STORE_ID = :storeId");
        }

        String productId = Objects.toString(param.get("productId"), null);
        if (StringUtils.isNotBlank(productId)) {
            sql.append(" and p.id = :productId");
        }
        String categoryId = Objects.toString(param.get("categoryId"), null);
        if (StringUtils.isNotBlank(categoryId)) {
            sql.append(" and p.category_id = :categoryId");
        }

        String name = Objects.toString(param.get("name"), null);
        if (StringUtils.isNotBlank(name)) {
            sql.append(" and (p.name_kor like '%' :name '%' or p.name_chn like '%' :name '%')    ");
        }

        return sql;
    }

    /**
     * 零售业务：
     * 根据skuId查询对应的库存信息
     *
     * @param storeId
     * @param toCheckInvProdSkuIdList
     * @return
     */
    public List<InventoryRetailDTO> getRetailInventoryListByConditions(Long storeId, List<Long> toCheckInvProdSkuIdList) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("storeId", storeId);
        param.put("skuIds", toCheckInvProdSkuIdList);

        StringBuilder sql = new StringBuilder(200);
        sql.append("SELECT IR.INV_ID, IR.STORE_ID, IR.PROD_ID, IR.PROD_SKU_ID, IR.INV_TOTAL,IR.INV_SOLD,IR.INV_BALANCE FROM inventory_retail IR ")
                .append("WHERE IR.STORE_ID=:storeId AND IR.PROD_SKU_ID IN (:skuIds)");

        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(InventoryRetailDTO.class));
    }
}