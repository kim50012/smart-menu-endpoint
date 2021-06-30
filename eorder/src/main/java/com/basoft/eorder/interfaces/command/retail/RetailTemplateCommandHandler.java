package com.basoft.eorder.interfaces.command.retail;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.retail.ProductAloneStandard;
import com.basoft.eorder.domain.model.retail.template.ProductAloneStandardTemplate;
import com.basoft.eorder.domain.retail.RetailTemplateRepository;
import com.basoft.eorder.util.UidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 *PRODUCT_ALONE_STANDARD_TEMPLATE零售商户产品规格模板表控制层
 *
 * @author DongXifu
 * @since 2020-04-16 11:26:23
 */
@CommandHandler.AutoCommandHandler("ProductAloneStandardTemplate")
public class RetailTemplateCommandHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private StoreRepository storeRepository;

    @Autowired
	private UidGenerator uidGenerator;

	@Autowired
	private RetailTemplateRepository retailTemplateRepository;


	/**
	 * 新增或修改ProductAloneStandardTemplate
	 *
	 * @Param productAloneStandardTemplate
	 * @return Long
	 */
	@CommandHandler.AutoCommandHandler(SaveRetailTemplate.NAME)
	public Long saveRetailTemplate(SaveRetailTemplate saveRetailTemplate, CommandHandlerContext context) {
		logger.debug("Start saveProductAloneStandardTemplate -------");
		UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);
		Store store = storeRepository.getStore(us.getStoreId());

		if (saveRetailTemplate.getTId() == null) {
			saveRetailTemplate.setTId(uidGenerator.generate(BusinessTypeEnum.PRODUCT_ALONE_STANDARD_TEMPLATE));
		}
		ProductAloneStandardTemplate template = saveRetailTemplate.build(saveRetailTemplate.getTId(),us);

		List<ProductAloneStandard>  standardList = buildStandards(saveRetailTemplate, store, us);

		return retailTemplateRepository.saveRetailTemplate(standardList, template);
	}


	/**
	 * 构建standard模版
	 *
	 * @param saveRetailTemplate
	 * @param store
	 * @param us
	 * @return
	 */
	private List<ProductAloneStandard> buildStandards(SaveRetailTemplate saveRetailTemplate, Store store, UserSession us) {
		Long tId = saveRetailTemplate.getTId();
			if (tId == null) {
				tId= uidGenerator.generate(BusinessTypeEnum.PRODUCT_ALONE_STANDARD_TEMPLATE);
			}
		Long finalTId = tId;
		return saveRetailTemplate.getAloneStandardList()
				.stream().map((SaveRetailTemplate.SaveAloneStandardT stand) -> {
					Long standId = stand.getStdId();
					if (standId == null) {
						standId = uidGenerator.generate(BusinessTypeEnum.PRODUCT_ALONE_STANDARD_T_S);
						stand.setStdId(standId);
					}
					return stand.build(store, finalTId, us, uidGenerator);
				}).collect(toList());
	}



	/**
	 * 批量修改模版状态
	 * @param saveRetailTemplate
	 * @return
	 */
	@CommandHandler.AutoCommandHandler(SaveRetailTemplate.UPDATE_STATUS)
	public int updateRetailTemplateStatus(SaveRetailTemplate saveRetailTemplate) {
		return retailTemplateRepository.updateRetailTemplateStatus(saveRetailTemplate.getRetailTemplates(),saveRetailTemplate.getTStatus());
	}

	/**
	 * 批量删除模版
	 * @param saveRetailTemplate
	 * @return
	 */
	@CommandHandler.AutoCommandHandler(SaveRetailTemplate.DEL_TEMPLATE)
	public int deleteTemplate(SaveRetailTemplate saveRetailTemplate, CommandHandlerContext context) {
		UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);

		return retailTemplateRepository.deleteTemplate(saveRetailTemplate.getRetailTemplates(),us);
	}

    
}