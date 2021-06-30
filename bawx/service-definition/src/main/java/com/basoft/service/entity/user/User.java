package com.basoft.service.entity.user;

import java.util.Date;

public class User {
    private String userId;

    private String userNickNm;

    private String userRealNm;

    private String wxOpenidU;

    private String wxIdU;

    private String email;

    private String mobileNo;

    private String qqId;

    private String pwd;

    private String dept;

    private Integer compId;

    private Integer shopId;

    private String activeSts;

    private Integer sex;

    private Integer imgId;

    private String imgNm;

    private String imgSysNm;

    private Integer imgSize;

    private String imgUrl;

    private Date modifiedDt;

    private Date createdDt;

    private String userAuth;

    private String singNm;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserNickNm() {
        return userNickNm;
    }

    public void setUserNickNm(String userNickNm) {
        this.userNickNm = userNickNm == null ? null : userNickNm.trim();
    }

    public String getUserRealNm() {
        return userRealNm;
    }

    public void setUserRealNm(String userRealNm) {
        this.userRealNm = userRealNm == null ? null : userRealNm.trim();
    }

    public String getWxOpenidU() {
        return wxOpenidU;
    }

    public void setWxOpenidU(String wxOpenidU) {
        this.wxOpenidU = wxOpenidU == null ? null : wxOpenidU.trim();
    }

    public String getWxIdU() {
        return wxIdU;
    }

    public void setWxIdU(String wxIdU) {
        this.wxIdU = wxIdU == null ? null : wxIdU.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId == null ? null : qqId.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept == null ? null : dept.trim();
    }

    public Integer getCompId() {
        return compId;
    }

    public void setCompId(Integer compId) {
        this.compId = compId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getActiveSts() {
        return activeSts;
    }

    public void setActiveSts(String activeSts) {
        this.activeSts = activeSts == null ? null : activeSts.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getImgId() {
        return imgId;
    }

    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public String getImgNm() {
        return imgNm;
    }

    public void setImgNm(String imgNm) {
        this.imgNm = imgNm == null ? null : imgNm.trim();
    }

    public String getImgSysNm() {
        return imgSysNm;
    }

    public void setImgSysNm(String imgSysNm) {
        this.imgSysNm = imgSysNm == null ? null : imgSysNm.trim();
    }

    public Integer getImgSize() {
        return imgSize;
    }

    public void setImgSize(Integer imgSize) {
        this.imgSize = imgSize;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
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

    public String getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth == null ? null : userAuth.trim();
    }

    public String getSingNm() {
        return singNm;
    }

    public void setSingNm(String singNm) {
        this.singNm = singNm == null ? null : singNm.trim();
    }
}