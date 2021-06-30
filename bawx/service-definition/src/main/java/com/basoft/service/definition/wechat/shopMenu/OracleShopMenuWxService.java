package com.basoft.service.definition.wechat.shopMenu;

/**
 * 特异化service for oracle db
 * @Date Created in 2018/7/4
 */
public interface OracleShopMenuWxService {

	/**
	 * 检验菜单设置是否符合上传发布规则
	 * 
	 * @param shopId
	 * @return
	 */
	public boolean checkMenu(Long shopId);

	/**
	 * 发布菜单留档并进行发布
	 * 
	 * @param shopId
	 * @return
	 */
	public Long publishMenu(Long shopId);
}
