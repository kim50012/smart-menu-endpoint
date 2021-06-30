package com.basoft.service.dao.wechat.shopMenu;

import com.basoft.service.dto.wechat.shopMenu.ShopWxMenuDto;
import com.basoft.service.entity.menu.MenuMst;
import com.basoft.service.entity.wechat.shopMenu.ShopWxMenu;
import com.basoft.service.entity.wechat.shopMenu.ShopWxMenuExample;
import com.basoft.service.entity.wechat.shopMenu.ShopWxMenuKey;
import com.basoft.service.param.wechat.shopMenu.ShopWxMenuQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ShopWxMenuMapper {
    int countByExample(ShopWxMenuExample example);

    int deleteByExample(ShopWxMenuExample example);

    int deleteByPrimaryKey(ShopWxMenuKey key);

    int insert(ShopWxMenu record);

    int insertSelective(ShopWxMenu record);

    List<ShopWxMenu> selectByExampleWithBLOBs(ShopWxMenuExample example);

    List<ShopWxMenu> selectByExample(ShopWxMenuExample example);

    ShopWxMenu selectByPrimaryKey(ShopWxMenuKey key);

    int updateByExampleSelective(@Param("record") ShopWxMenu record, @Param("example") ShopWxMenuExample example);

    int updateByExampleWithBLOBs(@Param("record") ShopWxMenu record, @Param("example") ShopWxMenuExample example);

    int updateByExample(@Param("record") ShopWxMenu record, @Param("example") ShopWxMenuExample example);

    int updateByPrimaryKeySelective(ShopWxMenu record);

    int updateByPrimaryKeyWithBLOBs(ShopWxMenu record);

    int updateByPrimaryKey(ShopWxMenu record);

    List<ShopWxMenuDto> findAllShopWxMenu(ShopWxMenuQueryParam param);

    int deleteAllShopWxMenu(@Param("shopId") Long shopId);

    int insertShopWxMenu(@Param("list") List<ShopWxMenuDto> list);

    int inserTwoShopMenu(@Param("list") List<ShopWxMenuDto> twoShopMenuList);
    
	/**
	 * 检验菜单设置是否符合上传发布规则
	 * 
	 * @param shopId
	 * @return
	 */
	List<MenuMst> checkMenu(Long shopId);

	/**
	 * 对发布的菜单留档
	 * 
	 * @param shopId
	 * @return
	 */
	int insertPublisMenu(Long shopId);
	
	/**
	 * 对发布的菜单留档
	 * 
	 * @param shopId
	 * @return
	 */
	Map<String, Long> insertPublisMenuWithCount(Map<String, Long> map);
	
	/**
	 * 查询一级菜单(存储过程)
	 * 
	 * @param shopId
	 * @return
	 */
	List<Map<String, Object>> selectMenuLevel1(@Param("shopId") Long shopId);

	/**
	 * 查询一级菜单
	 * @param shopId
	 * @return
	 */
	List<Map<String, Object>> selectMenuLevel1ByShop(@Param("shopId") Long shopId);

	/**
	 * 查询二级菜单
	 * 
	 * @param shopId
	 * @param menuId
	 * @return
	 */
	List<Map<String, Object>> selectMenuLevel2(@Param("shopId") Long shopId, @Param("menuId") Long menuId);
}