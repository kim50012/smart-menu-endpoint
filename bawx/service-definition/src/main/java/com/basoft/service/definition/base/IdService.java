package com.basoft.service.definition.base;

/**
 * ID generator Interface
 * 
 * @author basoft
 */
public interface IdService {
	Long generateShop();
	
	Long generateShopFile();

	Long generateNewsHead();

	Long generateNewsItem();

	Long generateMessageKeyWord();

	Long generateWxMessage();

	Long generateWxMenu();

	Long generateShopWxFile();

	Long generateMenuId();

	Long generateMenuMst();

	Long generateMenuAuth();

	Long generateGradeMst();
	
	Long generateCust();
	
	Long generateWxResponseMessage();
}