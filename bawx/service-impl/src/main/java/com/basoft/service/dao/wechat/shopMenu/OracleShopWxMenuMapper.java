package com.basoft.service.dao.wechat.shopMenu;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.basoft.service.dto.wechat.shopMenu.ShopWxMenuDto;
import com.basoft.service.entity.menu.MenuMst;

@Repository
public interface OracleShopWxMenuMapper {
	/**
	 * 检验菜单设置是否符合上传发布规则
	 * 
	 * @param shopId
	 * @return
	 */
	List<MenuMst> checkMenu(Map<String, Object> param);
	
	/**
	 * 对发布的菜单留档
	 * 
	 * @param shopId
	 * @return
	 */
	Map<String, Long> insertPublisMenuWithCount(Map<String, Long> map);
	
	/**
	 * 查询一级菜单
	 * 
	 * @param shopId
	 * @return
	 */
	List<ShopWxMenuDto> selectMenuLevel1(Map<String, Object> param);

	/**
	 * 查询二级菜单
	 * 
	 * @param shopId
	 * @param menuId
	 * @return
	 */
	List<ShopWxMenuDto> selectMenuLevel2(Map<String, Object> param);
}