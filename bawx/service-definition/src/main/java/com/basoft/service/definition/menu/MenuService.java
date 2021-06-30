package com.basoft.service.definition.menu;

import java.util.List;

import com.basoft.service.dto.menu.MenuDto;
import com.basoft.service.entity.menu.MenuMst;
import com.basoft.service.param.menu.MenuAuthQueryParam;
import com.basoft.service.param.menu.MenuQueryParam;
import com.github.pagehelper.PageInfo;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:38 2018/4/8
 **/
public interface MenuService {
	public PageInfo<MenuDto> menuFindAll(MenuQueryParam param);

	public List<MenuDto> menuFindAllByDisaBled(MenuQueryParam param);

	// 根据父id查寻父菜单名称
	public List<MenuMst> menuFindByPid(Long pid);

	// 新增菜单
	public int saveMenuMst(MenuMst menuMst);

	// 回显菜单
	public MenuMst getMenuById(Long id);

	// 修改菜单
	public int upMenuMst(MenuMst menuMst);

	// 禁用启用菜单
	public int forbidMenu(Long id, Byte status);

	// 删除菜单
	public int delMenuMst(Long id);

	//根据pid查询menu
	public List<MenuDto> findMenuByPid(MenuAuthQueryParam param);
}
