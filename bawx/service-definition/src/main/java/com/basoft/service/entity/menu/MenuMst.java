package com.basoft.service.entity.menu;

import javax.validation.constraints.NotNull;

public class MenuMst {
    @NotNull(message = "菜单id不能为空")
    private Long id;

    private Long menuId;

    private String menuNm;

    private Long menuPid;

    private String menuIcon;

    private Byte menuLevel;

    private String menuUrl;

    private Integer orderBy;

    private Byte activeSts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuNm() {
        return menuNm;
    }

    public void setMenuNm(String menuNm) {
        this.menuNm = menuNm == null ? null : menuNm.trim();
    }

    public Long getMenuPid() {
        return menuPid;
    }

    public void setMenuPid(Long menuPid) {
        this.menuPid = menuPid;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon == null ? null : menuIcon.trim();
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
        this.menuUrl = menuUrl == null ? null : menuUrl.trim();
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