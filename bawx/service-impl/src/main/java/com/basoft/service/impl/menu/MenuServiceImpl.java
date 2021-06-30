package com.basoft.service.impl.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.basoft.service.dao.menu.MenuMstMapper;
import com.basoft.service.definition.menu.MenuService;
import com.basoft.service.definition.wechat.BizConstants;
import com.basoft.service.dto.menu.MenuDto;
import com.basoft.service.entity.menu.MenuMst;
import com.basoft.service.entity.menu.MenuMstExample;
import com.basoft.service.param.menu.MenuAuthQueryParam;
import com.basoft.service.param.menu.MenuQueryParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:39 2018/4/8
 **/
@Service
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuMstMapper menuMstMapper;

	@Override
	public PageInfo<MenuDto> menuFindAll(MenuQueryParam param) {
		if (param == null) {
			return null;
		}
		PageHelper.startPage(param.getPage(), param.getRows());
		List<MenuDto> menuMstsList = menuMstMapper.menuFindAll(param);
		return new PageInfo<>(menuMstsList);
	}

	@Override
	public List<MenuDto> menuFindAllByDisaBled(MenuQueryParam param) {
		return menuMstMapper.menuFindAll(param);
	}

	@Override
	public List<MenuMst> menuFindByPid(Long pid) {
		return menuMstMapper.menuFindByPid(pid);
	}

	@Override
	public int saveMenuMst(MenuMst menuMst) {
		int order = menuMstMapper.selectMaxOrder();
		if (order == 0)
			order = 1;
		if (menuMst.getMenuPid() == 0) {
			order += 20;
		} else {
			order += 1;
		}
		if (menuMst.getMenuPid() == 0) {
			menuMst.setMenuLevel((byte) 1);
		} else {
			menuMst.setMenuLevel((byte) 2);
		}
		menuMst.setOrderBy(order);
		if(menuMst.getActiveSts()==null||"".equals(menuMst.getActiveSts())){
			menuMst.setActiveSts(BizConstants.MENU_STATE_ENABLE);
		}
		return menuMstMapper.insertSelective(menuMst);
	}

	@Override
	public MenuMst getMenuById(Long id) {
		return menuMstMapper.selectByPrimaryKey(id);
	}

	@Override
	public int upMenuMst(MenuMst mst) {
		return menuMstMapper.updateByPrimaryKeySelective(mst);
	}

	@Override
	public int forbidMenu(Long id, Byte status) {
		int result = 0;
		MenuMst menuMst = menuMstMapper.selectByPrimaryKey(id);
		if (menuMst.getMenuLevel() == 1 && status == BizConstants.MENU_STATE_FORBID) {
			MenuMstExample ex = new MenuMstExample();
			ex.createCriteria().andMenuPidEqualTo(menuMst.getMenuId());
			List<MenuMst> menuMstList = menuMstMapper.selectByExample(ex);
			menuMst.setActiveSts(BizConstants.MENU_STATE_FORBID);
			menuMstMapper.updateChildMenu(menuMst, menuMstList);
		}
		menuMst.setActiveSts(status);
		menuMstMapper.updateByPrimaryKey(menuMst);
		return result;
	}

	@Override
	public int delMenuMst(Long id) {
		MenuMst entity = menuMstMapper.selectByPrimaryKey(id);
		if (entity.getMenuLevel() == 1) {
			MenuMstExample ex = new MenuMstExample();
			ex.createCriteria().andMenuPidEqualTo(entity.getMenuId());
			menuMstMapper.deleteByExample(ex);
		}
		return menuMstMapper.deleteByPrimaryKey(id);
	}
	
	@Override
	public List<MenuDto> findMenuByPid(MenuAuthQueryParam param) {
		return menuMstMapper.findMenuByPid(param);
	}
}
