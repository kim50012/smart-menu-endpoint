package com.basoft.service.entity.customer.cust;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class Cust {
	@JsonSerialize(using = ToStringSerializer.class )
    private Long custSysId;

    private String custLoginId;

    private String wxIfOpenidP;

    private String email;

    private String mobileNo;

    private Byte wxIfIsSubscribe;

    private String wxIfNickNm;

    private String wxIfNickUnemoji;

    private Byte wxIfSexId;

    private String wxIfLanguage;

    private String wxIfCountryNm;

    private String wxIfProvinceNm;

    private String wxIfCityNm;

    private String wxIfHeadimgurl;

    private Date wxIfSubscribeTime;

    private String wxIfUnionid;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long wxIfGroupid;

    private String wxIfRemark;

    private Date wxSubscribeDt;

    private String wxIdP;

    private String custNickNm;

    public String getWxIfNickUnemoji() {
        return wxIfNickUnemoji;
    }

    public void setWxIfNickUnemoji(String wxIfNickUnemoji) {
        this.wxIfNickUnemoji = wxIfNickUnemoji;
    }

    private String custRealNm;

    private String pwd;

    private String activeSts;

    private Date modifiedDt;

    private Date createdDt;

    private String wxIfImgUrl;
    
    @JsonSerialize(using = ToStringSerializer.class )
    private Long shopId;

    public Long getCustSysId() {
        return custSysId;
    }

    public void setCustSysId(Long custSysId) {
        this.custSysId = custSysId;
    }

    public String getCustLoginId() {
        return custLoginId;
    }

    public void setCustLoginId(String custLoginId) {
        this.custLoginId = custLoginId == null ? null : custLoginId.trim();
    }

    public String getWxIfOpenidP() {
        return wxIfOpenidP;
    }

    public void setWxIfOpenidP(String wxIfOpenidP) {
        this.wxIfOpenidP = wxIfOpenidP == null ? null : wxIfOpenidP.trim();
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

    public Byte getWxIfIsSubscribe() {
        return wxIfIsSubscribe;
    }

    public void setWxIfIsSubscribe(Byte wxIfIsSubscribe) {
        this.wxIfIsSubscribe = wxIfIsSubscribe;
    }

    public String getWxIfNickNm() {
        return wxIfNickNm;
    }

    public void setWxIfNickNm(String wxIfNickNm) {
        this.wxIfNickNm = wxIfNickNm == null ? null : wxIfNickNm.trim();
    }

    public Byte getWxIfSexId() {
        return wxIfSexId;
    }

    public void setWxIfSexId(Byte wxIfSexId) {
        this.wxIfSexId = wxIfSexId;
    }

    public String getWxIfLanguage() {
        return wxIfLanguage;
    }

    public void setWxIfLanguage(String wxIfLanguage) {
        this.wxIfLanguage = wxIfLanguage == null ? null : wxIfLanguage.trim();
    }

    public String getWxIfCountryNm() {
        return wxIfCountryNm;
    }

    public void setWxIfCountryNm(String wxIfCountryNm) {
        this.wxIfCountryNm = wxIfCountryNm == null ? null : wxIfCountryNm.trim();
    }

    public String getWxIfProvinceNm() {
        return wxIfProvinceNm;
    }

    public void setWxIfProvinceNm(String wxIfProvinceNm) {
        this.wxIfProvinceNm = wxIfProvinceNm == null ? null : wxIfProvinceNm.trim();
    }

    public String getWxIfCityNm() {
        return wxIfCityNm;
    }

    public void setWxIfCityNm(String wxIfCityNm) {
        this.wxIfCityNm = wxIfCityNm == null ? null : wxIfCityNm.trim();
    }

    public String getWxIfHeadimgurl() {
        return wxIfHeadimgurl;
    }

    public void setWxIfHeadimgurl(String wxIfHeadimgurl) {
        this.wxIfHeadimgurl = wxIfHeadimgurl == null ? null : wxIfHeadimgurl.trim();
    }

    public Date getWxIfSubscribeTime() {
        return wxIfSubscribeTime;
    }

    public void setWxIfSubscribeTime(Date wxIfSubscribeTime) {
        this.wxIfSubscribeTime = wxIfSubscribeTime;
    }

    public String getWxIfUnionid() {
        return wxIfUnionid;
    }

    public void setWxIfUnionid(String wxIfUnionid) {
        this.wxIfUnionid = wxIfUnionid == null ? null : wxIfUnionid.trim();
    }

    public Long getWxIfGroupid() {
        return wxIfGroupid;
    }

    public void setWxIfGroupid(Long wxIfGroupid) {
        this.wxIfGroupid = wxIfGroupid;
    }

    public String getWxIfRemark() {
        return wxIfRemark;
    }

    public void setWxIfRemark(String wxIfRemark) {
        this.wxIfRemark = wxIfRemark == null ? null : wxIfRemark.trim();
    }

    public Date getWxSubscribeDt() {
        return wxSubscribeDt;
    }

    public void setWxSubscribeDt(Date wxSubscribeDt) {
        this.wxSubscribeDt = wxSubscribeDt;
    }

    public String getWxIdP() {
        return wxIdP;
    }

    public void setWxIdP(String wxIdP) {
        this.wxIdP = wxIdP == null ? null : wxIdP.trim();
    }

    public String getCustNickNm() {
        return custNickNm;
    }

    public void setCustNickNm(String custNickNm) {
        this.custNickNm = custNickNm == null ? null : custNickNm.trim();
    }

    public String getCustRealNm() {
        return custRealNm;
    }

    public void setCustRealNm(String custRealNm) {
        this.custRealNm = custRealNm == null ? null : custRealNm.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public String getActiveSts() {
        return activeSts;
    }

    public void setActiveSts(String activeSts) {
        this.activeSts = activeSts == null ? null : activeSts.trim();
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

    public String getWxIfImgUrl() {
        return wxIfImgUrl;
    }

    public void setWxIfImgUrl(String wxIfImgUrl) {
        this.wxIfImgUrl = wxIfImgUrl == null ? null : wxIfImgUrl.trim();
    }

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
}