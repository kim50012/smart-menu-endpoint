package com.basoft.service.impl.base;

import org.springframework.stereotype.Service;

import com.basoft.service.definition.base.IdService;
import com.basoft.service.enumerate.BusinessTypeEnum;
import com.basoft.service.util.UidGenerator;

/**
 * ID generator implement
 * 
 * @author basoft
 */
@Service
public class IdServiceImpl implements IdService {

	@Override
	public Long generateShop() {
		return new UidGenerator().generate(BusinessTypeEnum.SHOP);
	}
	
	@Override
	public Long generateShopFile() {
		return new UidGenerator().generate(BusinessTypeEnum.SHOPFILE);
	}

	@Override
	public Long generateNewsHead() {
		return new UidGenerator().generate(BusinessTypeEnum.SHOP_WX_NEWS_HEAD);
	}

	@Override
	public Long generateNewsItem() {
		return new UidGenerator().generate(BusinessTypeEnum.SHOP_WX_NEWS_ITEM);
	}

	@Override
	public Long generateMessageKeyWord() {
		return new UidGenerator().generate(BusinessTypeEnum.SHOP_WX_MESSAGE);
	}

	@Override
	public Long generateWxMessage() {
		return new UidGenerator().generate(BusinessTypeEnum.SHOP_WX_MESSAGE_KEYWORD);
	}

	@Override
	public Long generateWxMenu() {
		return new UidGenerator().generate(BusinessTypeEnum.SHOP_WX_MENU);
	}

	@Override
	public Long generateShopWxFile() {
		return new UidGenerator().generate(BusinessTypeEnum.SHOP_WX_FILE);
	}

	@Override
	public Long generateMenuId() {
		return new UidGenerator().generate(BusinessTypeEnum.MENU_MST_ID);
	}

	@Override
	public Long generateMenuMst() {
		return new UidGenerator().generate(BusinessTypeEnum.MENU_MST);
	}

	@Override
	public Long generateMenuAuth() {
		return new UidGenerator().generate(BusinessTypeEnum.MENU_AUTH);
	}

	@Override
	public Long generateGradeMst() {
		return new UidGenerator().generate(BusinessTypeEnum.GRADE_MST);
	}
	
	@Override
	public Long generateCust() {
		return new UidGenerator().generate(BusinessTypeEnum.WX_CUST);
	}
	
	@Override
	public Long generateWxResponseMessage() {
		return new UidGenerator().generate(BusinessTypeEnum.WX_RESPONSE_MESSAGE);
	}
}
