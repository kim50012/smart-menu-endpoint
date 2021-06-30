package com.basoft.service.entity.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.basoft.service.entity.shop.ShopWithBLOBs;

public class Group {
    private Integer gCorpId;

    private String gCorpNm;

    private String contactNm;

    private String contactMobileNo;

    private String contactEmail;

    private String contactQq;

    private String adminUserId;

    private String createdUserId;

    private Byte sortNo;

    private Date modifiedDt;

    private Date createdDt;

    private Integer isDelete;
    
    // 扩展字段：公司下的微信公众号列表
    List<ShopWithBLOBs> shopList = new ArrayList<ShopWithBLOBs>();

    public Integer getgCorpId() {
        return gCorpId;
    }

    public void setgCorpId(Integer gCorpId) {
        this.gCorpId = gCorpId;
    }

    public String getgCorpNm() {
        return gCorpNm;
    }

    public void setgCorpNm(String gCorpNm) {
        this.gCorpNm = gCorpNm == null ? null : gCorpNm.trim();
    }

    public String getContactNm() {
        return contactNm;
    }

    public void setContactNm(String contactNm) {
        this.contactNm = contactNm == null ? null : contactNm.trim();
    }

    public String getContactMobileNo() {
        return contactMobileNo;
    }

    public void setContactMobileNo(String contactMobileNo) {
        this.contactMobileNo = contactMobileNo == null ? null : contactMobileNo.trim();
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail == null ? null : contactEmail.trim();
    }

    public String getContactQq() {
        return contactQq;
    }

    public void setContactQq(String contactQq) {
        this.contactQq = contactQq == null ? null : contactQq.trim();
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId == null ? null : adminUserId.trim();
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId == null ? null : createdUserId.trim();
    }

    public Byte getSortNo() {
        return sortNo;
    }

    public void setSortNo(Byte sortNo) {
        this.sortNo = sortNo;
    }

    public Date getModifiedDt() {
        return modifiedDt;
    }

    public void setModifiedDt(Date modifiedDt) {
        this.modifiedDt = modifiedDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

	public List<ShopWithBLOBs> getShopList() {
		return shopList;
	}

	public void setShopList(List<ShopWithBLOBs> shopList) {
		this.shopList = shopList;
	}
}