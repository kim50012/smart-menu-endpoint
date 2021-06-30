package com.basoft.eorder.domain.retail;

import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.domain.model.retail.ProductAloneStandard;
import com.basoft.eorder.domain.model.retail.template.ProductAloneStandardTemplate;

import java.util.List;

/**
 *
 * @author DongXifu
 * @since 2020-04-16 11:30:00
 */
public interface RetailTemplateRepository {

	Long saveRetailTemplate(List<ProductAloneStandard> standards, ProductAloneStandardTemplate template);

	//新增PRODUCT_ALONE_STANDARD_TEMPLATE零售商户产品规格模板表
	Long  saveProductAloneStandardTemplate(ProductAloneStandardTemplate productAloneStandardTemplate);

	int updateRetailTemplateStatus(List<ProductAloneStandardTemplate> templates,String tStatus);

	int deleteTemplate(List<ProductAloneStandardTemplate> retailTemplates, UserSession us);
}