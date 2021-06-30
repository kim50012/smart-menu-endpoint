package com.basoft.service.definition.menu;

import com.basoft.service.dto.menu.MenuAuthDto;
import com.basoft.service.dto.menu.MenuDto;
import com.basoft.service.entity.menu.MenuMst;
import com.basoft.service.param.menu.MenuAuthForm;
import com.basoft.service.param.menu.MenuAuthQueryParam;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:16 2018/4/20
 **/
public interface MenuAuthService {

    //查询一级菜单
    public List<MenuAuthDto> findMenuByParam(MenuAuthQueryParam param);

    //保存权限菜单
    public int saveMenuAuth(MenuAuthForm form);

    List<String> findSelectMenu(Long shopId);

    //查询当前公众号能看见哪些菜单
    List<MenuDto> findMenuAuthByShop(MenuAuthQueryParam param);

    public List<MenuMst> findLevelOne(Long shopId);
}
