package com.basoft.eorder.foundation.jdbc.query.retail.cms;

import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.OrderDTO;
import com.basoft.eorder.interfaces.query.OrderItemDTO;
import com.basoft.eorder.interfaces.query.retail.cms.*;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商户零售产品规格表查询
 *
 * @author DongXifu
 * @since 2020-04-10 17:36:42
 */
@Component("JdbcRetailQuery")
public class JdbcRetailQueryImpl extends BaseRepository implements RetailQuery {


    private final static String RETAIL_ORDER_LIST_SELECT = "SELECT o.id,s.id as storeId" +
            ",s.name as storeNm,s.manager_id as managerId,s.store_type as storeType\n" +
            ",a.area_nm as areaNm,a.area_cd as areaCd\n"+
            ",s.logo_url as logoUrl\n"+
            ",o.created,o.amount,o.payment_amount as paymentAmount\n" +
            ",o.pay_amt_cny  as payAmtCny\n" +
            ",pay.cash_fee/100 as payAmtRmb\n" +
            ",(select max(p.create_dts) from eorder.order_pay p where p.order_id = o.id and p.pay_status = 1) pay_dt\n" +
            ",(select case when s.IS_PAY_SET = 0 then o.cancel_dt else max(c.createdt) end from eorder.order_pay_cancel c " +
            " where c.order_id = o.id and c.return_code = 'SUCCESS' and c.result_code = 'SUCCESS') as cancel_dt\n" +
            ",o.buyer_memo as buyerMemo\n" +
            ",o.pay_amt_usd as payAmtUsd\n"+
            ",o.open_id as openId\n" +
            ",o.`status`,o.CHANGE_STATUS as changeStatus,STATUS_8_FROM as status8From\n"+
            ",inf.cust_nm,inf.mobile\n"+
            ",inf.cust_nm as custNm\n"+
            ",inf.shipping_mode as shippingMode \n"+
            ",inf.shipping_mode_name_chn as shippingModeNameChn \n"+
            ",inf.shipping_mode_name_kor as shippingModeNameKor \n"+
            ",inf.shipping_mode_name_eng as shippingModeNameEng \n"+
            ",inf.shipping_addr_detail as shippingAddrDetail \n"+
            ",inf.country_no as countryNo \n"+
            ",inf.shipping_addr_country as shippingAddrCountry \n"+
            ",inf.shipping_weight as shippingWeight \n"+
            ",inf.shipping_cost as shippingCost \n"+
            ",inf.shipping_cost_rule as shippingCostRule \n"+
            ",inf.shipping_cmt as shippingCmt \n"+
            ",inf.shipping_dt as shippingDt \n"+
            ",inf.shipping_time as shippingTime \n"+
            ",inf.shipping_type as shippingType \n"+
            ",inf.cmt as cmt \n";


    private final static String  RETAIL_ORDER_FROM = " from `order` o join store s on o.store_id = s.id" +
            " join area a on a.area_cd=s.city " +
            " join store_table st on o.table_id = st.id \n "+
            " inner join order_pay pay on o.id=pay.order_id \n " +
            " inner join order_info inf on o.id=inf.order_id "+
            " left join ship_point sp on inf.shipping_addr=sp.ship_point_id\n" +
            " where 1=1 and s.store_type = 3";

    private final static String  RETAIL_ORDER_ITEM_LIST_SELECT = "select oi.id as id, oi.order_id as orderId, oi.sku_id as skuId, oi.sku_nm_kor as skuNmKor" +
            ", oi.sku_nm_chn as skuNmChn, oi.price as price, oi.price_cny as priceCny, oi.qty as qty" +
            ", oi.created as created, oi.updated as updated, p.name_kor as prodNmKor, p.name_chn as prodNmChn,p.WEIGHT, pi.image_url as prodUrl \n";

    private final static String RETAIL_ORDER_ITEM_FROM = " from order_item oi join product_sku ps on oi.sku_id = ps.id join product p on ps.product_id = p.id left join product_image pi on p.id = pi.product_id and pi.image_type = 1\n where 1 = 1 ";


    private final static String PRO_STANDARD_SELECT = "SELECT STORE_ID,PROD_ID,STD_ID,STD_NAME_CHN,STD_NAME_KOR,STD_NAME_ENG,DIS_ORDER,STD_IMAGE,STD_STATUS,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER\n";
    private final static String PRO_STANDARD_FROM = " FROM product_alone_standard pas  WHERE 1=1\n";

    private final static String PRO_ITEM_SELECT = "SELECT STD_ID,ITEM_ID,ITEM_NAME_CHN,ITEM_NAME_KOR,ITEM_NAME_ENG,DIS_ORDER,ITEM_IMAGE,ITEM_STATUS,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER\n";
    private final static String PRO_ITEM_SELECT_FROM = " FROM product_alone_standard_item pasi  WHERE 1=1\n";


    private final static String PRO_STANDARD_TEMPLATE_SELECT = "SELECT STD_ID,T_ID,STD_NAME_CHN,STD_NAME_KOR,STD_NAME_ENG,DIS_ORDER,STD_IMAGE,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER\n";
    private final static String PRO_STANDARD_TEMPLATE_FROM = " FROM product_alone_standard_t_s past  WHERE 1=1\n";

    private final static String PRO_ITEM_TEMPLATE_SELECT = "SELECT ITEM_ID,T_ID,STD_ID,ITEM_NAME_CHN,ITEM_NAME_KOR,ITEM_NAME_ENG,DIS_ORDER,ITEM_IMAGE,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER\n";
    private final static String PRO_ITEM_TEMPLATE_SELECT_FROM = " FROM product_alone_standard_t_s_i pasti  WHERE 1=1\n";


    @Override
    public int getRetailOrderCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder(SELECT_COUNT + RETAIL_ORDER_FROM);
        orderCondition(sql,param);

        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<OrderDTO> getRetailOrderList(Map<String, Object> param) {
        StringBuilder query = new StringBuilder(RETAIL_ORDER_LIST_SELECT + RETAIL_ORDER_FROM);
        orderCondition(query,param);
        orderByAndPage(param, query, " order by o.created desc, o.id\n");

        List<OrderDTO> orderList = this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<>(OrderDTO.class));

        return getOrderListWithItem(orderList);

    }

    private List<OrderDTO> getOrderListWithItem(List<OrderDTO> orderList) {
        Map param = Maps.newHashMap();
        List<Long> orderIdList = orderList.stream().map(OrderDTO::getId).collect(Collectors.toList());
        param.put("orderIds", orderIdList);
        List<OrderItemDTO> itemList = getOrderItemListByMap(param);//根据orderId数组查出来所有orderItem

        if (itemList != null && itemList.size() > 0) {
            Map<Long, List<OrderItemDTO>> groups = itemList.stream().collect(Collectors.groupingBy(OrderItemDTO::getOrderId));
            List<OrderDTO> resultList = orderList.stream().map(new Function<OrderDTO, OrderDTO>() {
                public OrderDTO apply(OrderDTO order) {
                    Long id = order.getId();
                    List<OrderItemDTO> oiList = groups.get(id);
                    if (oiList != null && oiList.size() > 0) {
                        order.setItemList(oiList);
                        order.setQty(oiList.stream().mapToInt(OrderItemDTO::getQty).sum());
                    }
                    return order;
                }
            }).collect(Collectors.toList());
            return resultList;
        }
        return orderList;
    }

    @Override
    public List<OrderItemDTO> getOrderItemListByMap(Map<String, Object> param) {
        StringBuilder query = new StringBuilder(RETAIL_ORDER_ITEM_LIST_SELECT + RETAIL_ORDER_ITEM_FROM);
        query.append(getOrderItemQueryConditions(param, " order by oi.updated desc, oi.id \n"));

        List<OrderItemDTO> orderItemDTOList = this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<>(OrderItemDTO.class));
        return orderItemDTOList;
    }

    private StringBuilder orderCondition(StringBuilder sql,Map<String, Object> param) {
        String id = Objects.toString(param.get("id"), null);
        if (StringUtils.isNotBlank(id)) {
            sql.append(" and o.id = :id \n");
        }
        String ids = Objects.toString(param.get("ids"), null);
        if (StringUtils.isNotBlank(ids)) {
            sql.append(" and o.id in ("+ids+")\n");
        }

        String storeId = Objects.toString(param.get("storeId"), null);
        if (StringUtils.isNotBlank(storeId)) {
            sql.append(" and o.store_id = :storeId \n");
        }

        String city = Objects.toString(param.get("city"), null);
        if (StringUtils.isNotBlank(city)) {
            sql.append(" and a.area_cd = :city \n");
        }

        String storeNm = Objects.toString(param.get("storeNm"), null);
        if (StringUtils.isNotBlank(storeNm)) {
            sql.append(" and s.name like '%' :storeNm '%'\n");
        }

        String status = Objects.toString(param.get("status"), null);
        if (StringUtils.isNotBlank(status)) {
            sql.append(" and o.status in ("+status+") \n");
        }

        String changeStatus = Objects.toString(param.get("changeStatus"), null);
        if (StringUtils.isNotBlank(changeStatus)) {
            sql.append(" and o.CHANGE_STATUS = :changeStatus \n");
        }

        String status8From = Objects.toString(param.get("status8From"), null);
        if (StringUtils.isNotBlank(status8From)) {
            sql.append(" and o.STATUS_8_FROM = :status8From \n");
        }

        String shippingType = Objects.toString(param.get("shippingType"), null);
        if (StringUtils.isNotBlank(shippingType)) {
            sql.append(" and inf.shipping_type in ("+shippingType+") \n");
        }

        String shippingDt = Objects.toString(param.get("shippingDt"), null);//自提时间
        if (StringUtils.isNotBlank(shippingDt)) {
            sql.append(" and inf.shipping_dt = :shippingDt\n");
        }

        String shippingTime = Objects.toString(param.get("shippingTime"), null);//自提1上午 2下午
        if (StringUtils.isNotBlank(shippingTime)) {
            sql.append(" and inf.shipping_time =:shippingTime \n");
        }

        String openId = Objects.toString(param.get("openId"), null);
        if(StringUtils.isNotBlank(openId)){
            sql.append(" and o.open_id = :openId \n");
        }

        String custNm = Objects.toString(param.get("custNm"), null);
        if(StringUtils.isNotBlank(custNm)){
            sql.append(" and (inf.cust_nm like :custNm or inf.cust_nm_en like :custNm) \n");
        }

        String mobile = Objects.toString(param.get("mobile"), null);
        if(StringUtils.isNotBlank(mobile)){
            sql.append(" and inf.mobile like '%' :mobile '%' \n");
        }

        String shippingAddrCountry = Objects.toString(param.get("shippingAddrCountry"), null);
        if(StringUtils.isNotBlank(shippingAddrCountry)){
            sql.append(" and inf.SHIPPING_ADDR_COUNTRY = :shippingAddrCountry \n");
        }


        String spStartTime = Objects.toString(param.get("spStartTime"), null);
        String spEndTime = Objects.toString(param.get("spEndTime"), null);
        if (StringUtils.isNotBlank(spStartTime) && StringUtils.isNotBlank(spEndTime)) {
            sql.append(" and inf.shipping_dt >= :spStartTime and inf.shipping_dt <= :spEndTime\n");
        }

        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and o.created >= :startTime and o.created <= :endTime\n");
        }


        return sql;
    }


    private String getOrderItemQueryConditions(Map<String, Object> param, String orderBy) {
        long orderId = NumberUtils.toLong(Objects.toString(param.get("orderId"), null));
        long id = NumberUtils.toLong(Objects.toString(param.get("id"), null));
        long skuId = NumberUtils.toInt(Objects.toString(param.get("skuId"), null));

        List<Long> orderIds = param.get("orderIds") != null ? (List<Long>) param.get("orderIds") : null;

        StringBuilder sb = new StringBuilder();

        if (orderId > 0) {
            sb.append(" and oi.order_id = :orderId \n");
        }

        if (skuId > 0) {
            sb.append(" and oi.sku_id = :skuId \n");
        }

        if (id > 0) {
            sb.append(" and oi.id = :id \n");
        }

        if (orderIds != null && orderIds.size() > 0) {
            sb.append(" and oi.order_id in (:orderIds) \n");
        }

        if (StringUtils.isNotBlank(orderBy)) {
            sb.append(orderBy);
        }
        return sb.toString();
    }


    @Override
     public List<ProductAloneStandardDTO> getProductStandardListByMap(Map<String, Object> param) {

         int isTemplete = NumberUtils.toInt(Objects.toString(param.get("isTemplete"), "0"));

         StringBuilder sql = new StringBuilder();
         if (isTemplete == 1) {
             sql.append(PRO_STANDARD_TEMPLATE_SELECT + PRO_STANDARD_TEMPLATE_FROM);
         } else {
             sql.append(PRO_STANDARD_SELECT + PRO_STANDARD_FROM);
         }
         getProStandardCondition(sql, param);

         List<ProductAloneStandardDTO> prodStandardList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(ProductAloneStandardDTO.class));
         List<ProductAloneStandardItemDTO> itemList = getItemWithProStandard(prodStandardList,isTemplete);

         Map<Long, List<ProductAloneStandardItemDTO>> collect = itemList.stream().collect(Collectors.groupingBy(ProductAloneStandardItemDTO::getStdId));

         prodStandardList.forEach(standardDTO -> {
             standardDTO.setStandardItemList(collect.get(standardDTO.getStdId()));
         });
         return prodStandardList;
     }

    private List<ProductAloneStandardItemDTO> getItemWithProStandard(List<ProductAloneStandardDTO> prodStandardList,int isTemplete) {
        List<Long> standarList = prodStandardList.stream().map(standardDTO -> standardDTO.getStdId()).collect(Collectors.toList());
        Map<String, Object> itemParam = Maps.newHashMap();
        itemParam.put("stdIds", standarList.toArray());
        itemParam.put("isTemplete",isTemplete);
        return getProductAloneStandardItemListByMap(itemParam,standarList.toArray(new Long[standarList.size()]));
    }


    public List<ProductAloneStandardItemDTO> getProductAloneStandardItemListByMap(Map<String, Object> param,Long ...standardIds) {
        int isTemplete = NumberUtils.toInt(Objects.toString(param.get("isTemplete"), "0"));
        StringBuilder sql = new StringBuilder();
        if (isTemplete == 1) {
            sql.append(PRO_ITEM_TEMPLATE_SELECT + PRO_ITEM_TEMPLATE_SELECT_FROM);
        } else {
            sql.append(PRO_ITEM_SELECT + PRO_ITEM_SELECT_FROM);
        }
        param.put("stdIds", Arrays.asList(standardIds));
        if (standardIds!=null&&standardIds.length>0) {
            sql.append(" and std_id in (:stdIds)");
        }

        List<ProductAloneStandardItemDTO> productAloneStandardItemDtoList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(ProductAloneStandardItemDTO.class));

        return productAloneStandardItemDtoList;
    }


     private StringBuilder getProStandardCondition(StringBuilder sql,Map<String, Object> param){

         String storeId = Objects.toString(param.get("storeId"), null);
         int isTemplete = NumberUtils.toInt(Objects.toString(param.get("isTemplete"), "0"));

         if (0 == isTemplete) {
             if (StringUtils.isNotBlank(storeId)) {
                 sql.append(" and store_id = :storeId");
             }
         }

         String tId = Objects.toString(param.get("tId"), null);
         if (StringUtils.isNotBlank(tId)) {
             sql.append(" and T_ID = :tId");
         }
         String stdId = Objects.toString(param.get("stdId"), null);
         if (StringUtils.isNotBlank(stdId)) {
             sql.append(" and STD_ID = :stdId");
         }
                  
         String productId = Objects.toString(param.get("productId"), null);
         if (StringUtils.isNotBlank(productId)) {
             sql.append(" and PROD_ID = :productId");
         }
         return sql;
     }

    @Override
    public List<ProSkuItemNameDTO> getSkuStandardList(Map<String, Object> param) {
        String sql = "SELECT sas.PRODUCT_SKU_ID ,sas.STANDARD_ID,sas.STANDARD_ITEM_ID,pai.ITEM_ID ,pai.DIS_ORDER,pai.ITEM_NAME_CHN,pai.ITEM_NAME_KOR\n " +
                "from product_sku_alone_standard sas \n" +
                "INNER JOIN product_alone_standard pas on sas.STANDARD_ID = pas.STD_ID\n" +
                "INNER JOIN product_alone_standard_item pai on pai.item_id=sas.STANDARD_ITEM_ID\n" +
                "WHERE pas.PROD_ID=:productId";
        List<ProSkuItemNameDTO> proSkuItemDtoList = this.getNamedParameterJdbcTemplate().query(sql, param, new BeanPropertyRowMapper<>(ProSkuItemNameDTO.class));

        return proSkuItemDtoList;
    }

    @Override
    public List<StandardAndItemDTO> getStandardAndItemList(Long ... productIdList) {
        if (productIdList.length < 1) {
            return new LinkedList<>();
        }
        Map<String, Object> param = Maps.newHashMap();
        param.put("productIds", Arrays.asList(productIdList));

        String sql = "SELECT ir.INV_ID,psas.PRODUCT_SKU_ID as prodSkuId,pas.PROD_ID,pas.STD_ID,pasi.ITEM_ID\n" +
                " ,ps.is_inventory as isInventory,ir.INV_BALANCE,ir.INV_TOTAL\n"+
                " ,ps.status\n"+
                " ,ps.name_chn as stdItemNameChn\n" +
                " ,ps.name_kor as stdItemNameKor\n" +
                " ,group_concat(pasi.ITEM_NAME_ENG ORDER BY pasi.DIS_ORDER asc) as stdItemNameEng\n" +
                "from  product_alone_standard pas \n" +
                "inner JOIN product_alone_standard_item pasi on pas.STD_ID=pasi.STD_ID \n" +
                "INNER JOIN product_sku_alone_standard psas  on psas.STANDARD_ID = pas.STD_ID and psas.STANDARD_ITEM_ID = pasi.ITEM_ID\n" +
                "INNER JOIN product_sku ps on psas.PRODUCT_SKU_ID = ps.id\n" +
                "LEFT JOIN inventory_retail ir on ir.PROD_SKU_ID = ps.id\n"+
                "where pas.PROD_ID in (:productIds) and ps.status=1\n"+
                "GROUP BY psas.PRODUCT_SKU_ID\n"+
                "ORDER BY pasi.DIS_ORDER asc\n";
        return this.getNamedParameterJdbcTemplate().query(sql, param, new BeanPropertyRowMapper<>(StandardAndItemDTO.class));
    }

    @Override
    public List<StandardAndItemDTO> getNoStandardAndItemList(Long... productIdList) {
         if(productIdList.length<1){
             return new LinkedList<>();
         }
        Map<String, Object> param = Maps.newHashMap();
        param.put("productIds", Arrays.asList(productIdList));

        String sql = "SELECT ps.product_id as prodId,ps.id as prodSkuId,ir.INV_ID,ps.is_inventory,ir.INV_TOTAL,ir.INV_BALANCE,ps.status from product_sku ps \n" +
                "LEFT JOIN inventory_retail ir on ps.id=ir.PROD_SKU_ID\n" +
                "WHERE ps.product_id in(:productIds) and ps.status=1\n";
        return this.getNamedParameterJdbcTemplate().query(sql, param, new BeanPropertyRowMapper<>(StandardAndItemDTO.class));

    }

    @Override
    public List<RetailOrderStatusDTO> getRetailOrderStatusCountList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT status,count(*)as count from `order` o \n" +
                " inner join order_info inf on inf.order_id = o.id\n" +
                " WHERE o.store_id=:storeId\n");
        String status = Objects.toString(param.get("status"), null);
        if (StringUtils.isNotBlank(status)) {
            sql.append(" and status = :status\n");
        }

        String shippingType = Objects.toString(param.get("shippingType"), null);
        if (StringUtils.isNotBlank(shippingType)) {
            sql.append(" and inf.SHIPPING_TYPE in ("+shippingType+")\n");
        }

        sql.append(" group by status");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(RetailOrderStatusDTO.class));
    }

    @Override
    public List<RetailOrderStatusDTO> getRetailServOrderStatusCountList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select case serv_status when 3 then case AUDIT_RESULT when 1 then 4 when 0 then 5 else 5  end  else serv_status end servStatus\n" +
                " ,count(*)as count from retail_order_service ros\n" +
                " where ros.store_id=:storeId\n"+
                " GROUP BY case serv_status when 3 then case AUDIT_RESULT when 1 then 4 when 0 then 5 else 5 end  else serv_status end\n" +
                " ");
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(RetailOrderStatusDTO.class));
    }
}