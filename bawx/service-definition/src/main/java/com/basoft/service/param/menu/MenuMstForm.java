package com.basoft.service.param.menu;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:30 2018/4/8
 **/
public class MenuMstForm{

    @NotBlank(message = "不能为空")
    private String menuNm;

    private Integer menuPid;

    private String menuIcon;

    private Byte menuLevel;

    private String menuUrl;

    private Integer orderBy;

    private Byte activeSts;

    public String getMenuNm() {
        return menuNm;
    }

    public void setMenuNm(String menuNm) {
        this.menuNm = menuNm;
    }

    public Integer getMenuPid() {
        return menuPid;
    }

    public void setMenuPid(Integer menuPid) {
        this.menuPid = menuPid;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Byte getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(Byte menuLevel) {
        this.menuLevel = menuLevel;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public Byte getActiveSts() {
        return activeSts;
    }

    public void setActiveSts(Byte activeSts) {
        this.activeSts = activeSts;
    }
}
