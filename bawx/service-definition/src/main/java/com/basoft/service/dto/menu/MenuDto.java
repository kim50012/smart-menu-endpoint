package com.basoft.service.dto.menu;


import com.basoft.service.entity.menu.MenuMst;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:41 2018/4/8
 **/

public class MenuDto extends MenuMst {
    private String menuNm2;

    private String activeStStr;

    private List<MenuDto> children;

    public String getMenuNm2() {
        return menuNm2;
    }

    public void setMenuNm2(String menuNm2) {
        this.menuNm2 = menuNm2;
    }

    public String getActiveStStr() {
        return activeStStr;
    }

    public void setActiveStStr(String activeStStr) {
        this.activeStStr = activeStStr;
    }

    public List<MenuDto> getChildren() {
        return children;
    }

    public void setChildren(List<MenuDto> children) {
        this.children = children;
    }
}
