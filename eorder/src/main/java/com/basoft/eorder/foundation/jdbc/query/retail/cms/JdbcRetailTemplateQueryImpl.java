package com.basoft.eorder.foundation.jdbc.query.retail.cms;

import com.basoft.eorder.domain.model.retail.template.ProductAloneStandardTemplate;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.retail.cms.RetailTemplateQuery;
import com.basoft.eorder.interfaces.query.retail.cms.RetailTemplateDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * PRODUCT_ALONE_STANDARD_TEMPLATE零售商户产品规格模板表查询
 *
 * @author DongXifu
 * @since 2020-04-16 14:12:01
 */
@Component("ProductAloneStandardTemplateQuery")
public class JdbcRetailTemplateQueryImpl extends BaseRepository implements RetailTemplateQuery {

    private final static String RETAIL_TEMPLATE_SELECT = "SELECT t_id, STORE_ID,T_NAME_CHN,T_NAME_KOR,T_NAME_ENG,T_STATUS,DES_KOR,DES_CHN" +
            ",CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER\n";
    private final static String RETAIL_TEMPLATE_FROM = " FROM product_alone_standard_template R  WHERE 1=1\n";



     @Override
     public int getProductAloneStandardTemplateCount(Map<String, Object> param) {
         StringBuilder sql = new StringBuilder("select count(1)" + RETAIL_TEMPLATE_FROM);
         getProductAloneStandardTemplateCondition(sql, param);
         return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
     }

     @Override
     public List<RetailTemplateDTO> getProductAloneStandardTemplateListByMap(Map<String, Object> param) {
         StringBuilder sql = new StringBuilder(RETAIL_TEMPLATE_SELECT + RETAIL_TEMPLATE_FROM);
         getProductAloneStandardTemplateCondition(sql, param);
         orderByAndPage(param, sql, " ORDER BY CREATE_TIME desc");

         List<RetailTemplateDTO> productAloneStandardTemplateDtoList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(RetailTemplateDTO.class));
         return productAloneStandardTemplateDtoList;
     }

     @Override
     public RetailTemplateDTO getProductAloneStandardTemplateDto(Map<String, Object> param) {
         StringBuilder sql = new StringBuilder();
         sql.append(RETAIL_TEMPLATE_SELECT + RETAIL_TEMPLATE_FROM);
         getProductAloneStandardTemplateCondition(sql, param);
         RetailTemplateDTO dto = this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(RetailTemplateDTO.class));
         return dto;
     }

    @Override
     public ProductAloneStandardTemplate getProductAloneStandardTemplate(Map<String, Object> param) {
         StringBuilder sql = new StringBuilder();
         sql.append(RETAIL_TEMPLATE_SELECT + RETAIL_TEMPLATE_FROM);
         getProductAloneStandardTemplateCondition(sql, param);
         return  this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(ProductAloneStandardTemplate.class));
     }

     private StringBuilder getProductAloneStandardTemplateCondition(StringBuilder sql,Map<String, Object> param){
         String storeId = Objects.toString(param.get("storeId"), null);
         String tId = Objects.toString(param.get("tId"), null);
         String tStatus = Objects.toString(param.get("tStatus"), null);
         if (StringUtils.isNotBlank(tId)) {
             sql.append(" and T_ID  = :tId");
         }
         if (StringUtils.isNotBlank(storeId)) {
             sql.append(" and STORE_ID  = :storeId");
         }
         if (StringUtils.isNotBlank(tStatus)) {
             sql.append(" and T_STATUS  = :tStatus");
         }
                  
         String name = Objects.toString(param.get("name"), null);
         if (StringUtils.isNotBlank(name)) {
             sql.append(" and (T_NAME_CHN like '%' :name '%' or T_NAME_KOR like '%' :name '%') ");
         }
         return sql;
     }
}