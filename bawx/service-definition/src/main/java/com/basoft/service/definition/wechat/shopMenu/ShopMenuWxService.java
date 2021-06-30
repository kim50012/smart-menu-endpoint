package com.basoft.service.definition.wechat.shopMenu;

import java.util.List;

import com.basoft.service.dto.wechat.shopMenu.ShopWxMenuDto;
import com.basoft.service.entity.wechat.shopMenu.ShopWxMenu;
import com.basoft.service.param.wechat.shopMenu.ShopWxMenuForm;
import com.basoft.service.param.wechat.shopMenu.ShopWxMenuQueryParam;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:42 2018/4/12
 **/
public interface ShopMenuWxService {


    public List<ShopWxMenuDto> findAllShopWxMenu(ShopWxMenuQueryParam param);

    //查询二级菜单
    List<ShopWxMenu> secondMenuList(Long parentId);

    //新增菜单
    public int saveShopWxMenu(ShopWxMenuForm from);

    //修改前查询菜单
    public ShopWxMenu getShopWxMenu(Long shopId,Long menuId);

    //修改菜单
    public int upShopWxMenu(ShopWxMenu shopWxMenu);

    //删除菜单
    public int deletShopWxMenu(Long menuId);

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
