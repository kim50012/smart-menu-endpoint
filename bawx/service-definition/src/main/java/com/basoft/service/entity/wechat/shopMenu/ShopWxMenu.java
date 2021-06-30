package com.basoft.service.entity.wechat.shopMenu;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Date;

public class ShopWxMenu extends ShopWxMenuKey {
    private String menuNm;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuParentId;

    @JsonSerialize(using = ToStringSerializer.class)
    private String menuOpWxId;//微信对接

    private String menuOpType;

    private Integer menuMsgType;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuOpId;

    private String menuOpTitle;

    private Byte menuSts;

    private Byte sortNo;

    private String createId;

    private Date createDt;

    private String modifyId;

    private Date modifyDt;

    private String menuOpCom;

    public String getMenuNm() {
        return menuNm;
    }

    public void setMenuNm(String menuNm) {
        this.menuNm = menuNm == null ? null : menuNm.trim();
    }

    public Long getMenuParentId() {
        return menuParentId;
    }

    public void setMenuParentId(Long menuParentId) {
        this.menuParentId = menuParentId;
    }

    public String getMenuOpType() {
        return menuOpType;
    }

    public void setMenuOpType(String menuOpType) {
        this.menuOpType = menuOpType == null ? null : menuOpType.trim();
    }

    public Integer getMenuMsgType() {
        return menuMsgType;
    }

    public void setMenuMsgType(Integer menuMsgType) {
        this.menuMsgType = menuMsgType;
    }

    public Long getMenuOpId() {
        return menuOpId;
    }

    public void setMenuOpId(Long menuOpId) {
        this.menuOpId = menuOpId;
    }

    public String getMenuOpTitle() {
        return menuOpTitle;
    }

    public void setMenuOpTitle(String menuOpTitle) {
        this.menuOpTitle = menuOpTitle == null ? null : menuOpTitle.trim();
    }

    public Byte getMenuSts() {
        return menuSts;
    }

    public void setMenuSts(Byte menuSts) {
        this.menuSts = menuSts;
    }

    public Byte getSortNo() {
        return sortNo;
    }

    public void setSortNo(Byte sortNo) {
        this.sortNo = sortNo;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId == null ? null : createId.trim();
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId == null ? null : modifyId.trim();
    }

    public Date getModifyDt() {
        return modifyDt;
    }

    public void setModifyDt(Date modifyDt) {
        this.modifyDt = modifyDt;
    }

    public String getMenuOpCom() {
        return menuOpCom;
    }

    public void setMenuOpCom(String menuOpCom) {
        this.menuOpCom = menuOpCom == null ? null : menuOpCom.trim();
    }

    public String getMenuOpWxId() {
        return menuOpWxId;
    }

    public void setMenuOpWxId(String menuOpWxId) {
        this.menuOpWxId = menuOpWxId;
    }
}