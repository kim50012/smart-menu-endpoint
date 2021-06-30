package com.basoft.eorder.interfaces.query.retail.cms;

import com.basoft.eorder.domain.model.retail.template.ProductAloneStandardTemplate;

import java.util.List;
import java.util.Map;

/**
 * PRODUCT_ALONE_STANDARD_TEMPLATE零售商户产品规格模板表(ProductAloneStandardTemplate)表数据库访问层
 *
 * @author DongXifu
 * @since 2020-04-16 14:00:04
 */
public interface RetailTemplateQuery {
    ProductAloneStandardTemplate getProductAloneStandardTemplate(Map<String, Object> param);

    RetailTemplateDTO getProductAloneStandardTemplateDto(Map<String, Object> param);

    int getProductAloneStandardTemplateCount(Map<String, Object> param);

    List<RetailTemplateDTO> getProductAloneStandardTemplateListByMap(Map<String, Object> param);

}