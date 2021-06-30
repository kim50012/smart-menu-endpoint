package com.basoft.service.param.menu;

import com.basoft.service.param.PaginationParam;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:44 2018/4/8
 **/
public class MenuQueryParam extends PaginationParam{

    private int menuId;

    private String menuNm;

    private String menuLevel;

    private int pid;

    private Boolean disabled;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuNm() {
        return menuNm;
    }

    public void setMenuNm(String menuNm) {
        this.menuNm = menuNm;
    }

    public String getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(String menuLevel) {
        this.menuLevel = menuLevel;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
}
