package com.basoft.eorder.foundation.jdbc.query.retail.cms;

import com.basoft.eorder.domain.model.retailOrderService.RetailOrderService;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.OrderItemDTO;
import com.basoft.eorder.interfaces.query.retail.cms.RetailQuery;
import com.basoft.eorder.interfaces.query.retailOrderService.RetailOrderServiceDTO;
import com.basoft.eorder.interfaces.query.retailOrderService.RetailOrderServiceQuery;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 零售商户订单售后服务表。一条记录整合所有信息，不拆分存储查询
 *
 * @author DongXifu
 * @since 2020-05-12 14:26:23
 */
@Component("RetailOrderServiceQuery")
public class JdbcRetailOrderServiceQueryImpl extends BaseRepository implements RetailOrderServiceQuery {

    @Autowired
    private RetailQuery retailQuery;

     private final static String RETAIL_ORDER_SERVICE_SELECT = "SELECT SERV_ID,SERV_CODE,SERV_TYPE,ros.SERV_STATUS,ros.ORDER_ID,APPLY_COUNT,APPLY_AMOUNT\n" +
             ",APPLY_DESC,APPLY_IMAGES,APPLY_DELIVERY_MODE,APPLY_LINKER,APPLY_MOBILE,APPLY_ADDRESS,APPLY_TIME\n" +
             ",ACCEPTOR,ACCEPT_TIME,IFNULL(AUDIT_RESULT,0) as auditResult,AUDIT_DESC,IFNULL(AUDIT_REFUND_TYPE,0)auditRefundType,AUDIT_REFUND_AMOUNT,AUDITOR,AUDIT_TIME\n"+
             ",inf.cust_nm,inf.mobile,inf.shipping_weight,inf.shipping_cost,inf.shipping_addr_country,inf.country_no as countryNo\n" +
             ",inf.shipping_addr_detail,inf.shipping_cmt,inf.shipping_type\n"+
             ",o.amount,o.payment_amount ";
     private final static String RETAIL_ORDER_SERVICE_FROM = " FROM RETAIL_ORDER_SERVICE ros  " +
             "LEFT JOIN ORDER_INFO inf ON inf.ORDER_ID = ros.ORDER_ID\n"+
             "INNER JOIN `ORDER` o ON o.ID = inf.ORDER_ID\n" +
             "WHERE 1=1\n";

     @Override
     public int getRetailOrderServiceCount(Map<String, Object> param) {
         StringBuilder sql = new StringBuilder("select count(1) " +  RETAIL_ORDER_SERVICE_FROM);
         getRetailOrderServiceCondition(sql, param);
         return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
     }

     @Override
     public List<RetailOrderServiceDTO> getRetailOrderServiceListByMap(Map<String, Object> param) {
         StringBuilder sql = new StringBuilder(RETAIL_ORDER_SERVICE_SELECT + RETAIL_ORDER_SERVICE_FROM);
         getRetailOrderServiceCondition(sql, param);
         orderByAndPage(param, sql, " ORDER BY SERV_ID desc");

         List<RetailOrderServiceDTO> serviceDTOS = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(RetailOrderServiceDTO.class));
         return getOrderListWithItem(serviceDTOS);
     }

    private List<RetailOrderServiceDTO> getOrderListWithItem(List<RetailOrderServiceDTO> serviceDTOS) {
        Map param = Maps.newHashMap();
        List<Long> orderIdList = serviceDTOS.stream().map(RetailOrderServiceDTO::getOrderId).collect(Collectors.toList());
        param.put("orderIds", orderIdList);
        List<OrderItemDTO> itemList = retailQuery.getOrderItemListByMap(param);//根据orderId数组查出来所有orderItem

        if (itemList != null && itemList.size() > 0) {
            Map<Long, List<OrderItemDTO>> groups = itemList.stream().collect(Collectors.groupingBy(OrderItemDTO::getOrderId));
            List<RetailOrderServiceDTO> resultList = serviceDTOS.stream().map(new Function<RetailOrderServiceDTO, RetailOrderServiceDTO>() {
                public RetailOrderServiceDTO apply(RetailOrderServiceDTO serviceDTO) {
                    Long id = serviceDTO.getOrderId();
                    List<OrderItemDTO> oiList = groups.get(id);
                    if (oiList != null && oiList.size() > 0) {
                        serviceDTO.setItemList(oiList);
                        serviceDTO.setApplyCount(oiList.stream().mapToInt(OrderItemDTO::getQty).sum());
                    }
                    return serviceDTO;
                }
            }).collect(Collectors.toList());
            return resultList;
        }
        return serviceDTOS;
    }


    @Override
    public RetailOrderService getRetailOrderService(Long servId,Long servCode) {
        Map<String, Object> param = new HashMap<>();
        param.put("servId", servId);
        param.put("servCode", servCode);
        StringBuilder sql = new StringBuilder();
        sql.append(RETAIL_ORDER_SERVICE_SELECT + RETAIL_ORDER_SERVICE_FROM);
        getRetailOrderServiceCondition(sql, param);
        logger.info("根据售后ID和Code查询售后详情SQL" + sql.toString());
        RetailOrderService retailOrderService = this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(RetailOrderService.class));
        return retailOrderService;
    }

     @Override
     public RetailOrderServiceDTO getRetailOrderServiceDto(Map<String, Object> param) {
         StringBuilder sql = new StringBuilder();
         sql.append(RETAIL_ORDER_SERVICE_SELECT + RETAIL_ORDER_SERVICE_FROM);
         getRetailOrderServiceCondition(sql, param);
         RetailOrderServiceDTO dto = this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(RetailOrderServiceDTO.class));
         return dto;
     }

    @Override
    public List<RetailOrderServiceDTO> getAfterSalesApplys(Long servId,Long servCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(RETAIL_ORDER_SERVICE_SELECT + RETAIL_ORDER_SERVICE_FROM);
        sql.append(" and serv_id = :servId");
        sql.append(" and serv_code = :servCode");
        Map<String, Object> param = new HashMap<>();
        param.put("servId", servId);
        param.put("servCode", servCode);

        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(RetailOrderServiceDTO.class));
    }

    private StringBuilder getRetailOrderServiceCondition(StringBuilder sql, Map<String, Object> param){
         String storeId = Objects.toString(param.get("storeId"), null);//
         if (StringUtils.isNotBlank(storeId)) {
             sql.append(" and ros.store_Id  = :storeId");
         }

         String storeNm = Objects.toString(param.get("storeNm"), null);//
         if (StringUtils.isNotBlank(storeNm)) {
             sql.append(" and s.name  = :storeNm");
         }

         String servId = Objects.toString(param.get("servId"), null);//
         if (StringUtils.isNotBlank(servId)) {
             sql.append(" and SERV_ID  = :servId");
         }
         String servCode = Objects.toString(param.get("servCode"), null);//申请编号
         if (StringUtils.isNotBlank(servCode)) {
             sql.append(" and SERV_CODE  = :servCode");
         }

         String orderId = Objects.toString(param.get("orderId"), null);
         if (StringUtils.isNotBlank(orderId)) {
             sql.append(" and ros.ORDER_ID  = :orderId");
         }
         
         String servType = Objects.toString(param.get("servType"), null);//售后类型，1-退货 2-换货 3-维修 4-补发商品
         if (StringUtils.isNotBlank(servType)) {
             sql.append(" and SERV_TYPE  = :servType");
         }

         String applyLinker = Objects.toString(param.get("applyLinker"), null);//联系人
         if (StringUtils.isNotBlank(applyLinker)) {
             sql.append(" and APPLY_LINKER  like '%' :applyLinker '%'");
         }
                  
         String applyMobile = Objects.toString(param.get("applyMobile"), null);//联系人手机号
         if (StringUtils.isNotBlank(applyMobile)) {
             sql.append(" and APPLY_MOBILE  = :applyMobile");
         }

         String servStatus = Objects.toString(param.get("servStatus"), null);//审核状态
         if (StringUtils.isNotBlank(servStatus)) {
             sql.append(" and SERV_STATUS in ("+servStatus+") ");
         }

         String auditResult = Objects.toString(param.get("auditResult"), null);//审核结果
         if (StringUtils.isNotBlank(auditResult)) {
             sql.append(" and AUDIT_RESULT  = :auditResult");
         }

         String startTime = Objects.toString(param.get("startTime"), null);
         String endTime = Objects.toString(param.get("endTime"), null);

         if (StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
             sql.append(" and APPLY_TIME >= :startTime and APPLY_TIME <= :endTime ");
         }

         return sql;
     }
}