package com.basoft.core.ware.wechat.domain.user;

import java.util.Date;

public class Cust {
	private Integer shopId; 			// [店铺] 商店ID
	private Integer custSysId; 			// [客户] System Customer ID
	private String custLoginId; 		// [客户] (唯一)User ID
	private String wxIfOpenidP; 		// [客户] (唯一)开放平台帐号
	private String email; 				// [客户] (唯一)EMAIL
	private String mobileNo; 			// [客户] (唯一)手机号
	private Integer wxIfIsSubscribe; 	// [客户] 0:没有关注 1:关注
	private String wxIfNickNm; 			// [客户] 用户的昵称
	private Integer wxIfSexId; 			// [客户] 1男性，2女性，0未知
	private String wxIfLanguage; 		// [客户] 简体中文为zh_CN
	private String wxIfCountryNm; 		// [客户] 用户所在国家
	private String wxIfProvinceNm; 		// [客户] 用户所在省份
	private String wxIfCityNm; 			// [客户] 用户所在城市
	private String wxIfHeadimgurl; 		// [客户] 用户头像
	private Date wxIfSubscribeTime; 	// [客户] 关注时间
	private String wxIfUnionid; 		// [客户] 绑定号
	private Integer wxIfGroupid; 		// [客户] 用户所在的分组ID
	private String wxIfRemark; 			// [客户] 	公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
	private Date wxSubscribeDt; 		// [客户] 变换 WX_IF_SUBSCRIBE_TIME
	private String wxIdP; 				// [客户] 用户输入的WEIXIN ID
	private String custNickNm; 			// [客户] 复制 WX_IF_NICK_NM
	private String custRealNm; 			// [客户] 姓名
	private String pwd; 				// [客户] 密码

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getCustSysId() {
		return custSysId;
	}

	public void setCustSysId(Integer custSysId) {
		this.custSysId = custSysId;
	}

	public String getCustLoginId() {
		return custLoginId;
	}

	public void setCustLoginId(String custLoginId) {
		this.custLoginId = custLoginId;
	}

	public String getWxIfOpenidP() {
		return wxIfOpenidP;
	}

	public void setWxIfOpenidP(String wxIfOpenidP) {
		this.wxIfOpenidP = wxIfOpenidP;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Integer getWxIfIsSubscribe() {
		return wxIfIsSubscribe;
	}

	public void setWxIfIsSubscribe(Integer wxIfIsSubscribe) {
		this.wxIfIsSubscribe = wxIfIsSubscribe;
	}

	public String getWxIfNickNm() {
		return wxIfNickNm;
	}

	public void setWxIfNickNm(String wxIfNickNm) {
		this.wxIfNickNm = wxIfNickNm;
	}

	public Integer getWxIfSexId() {
		return wxIfSexId;
	}

	public void setWxIfSexId(Integer wxIfSexId) {
		this.wxIfSexId = wxIfSexId;
	}

	public String getWxIfLanguage() {
		return wxIfLanguage;
	}

	public void setWxIfLanguage(String wxIfLanguage) {
		this.wxIfLanguage = wxIfLanguage;
	}

	public String getWxIfCountryNm() {
		return wxIfCountryNm;
	}

	public void setWxIfCountryNm(String wxIfCountryNm) {
		this.wxIfCountryNm = wxIfCountryNm;
	}

	public String getWxIfProvinceNm() {
		return wxIfProvinceNm;
	}

	public void setWxIfProvinceNm(String wxIfProvinceNm) {
		this.wxIfProvinceNm = wxIfProvinceNm;
	}

	public String getWxIfCityNm() {
		return wxIfCityNm;
	}

	public void setWxIfCityNm(String wxIfCityNm) {
		this.wxIfCityNm = wxIfCityNm;
	}

	public String getWxIfHeadimgurl() {
		return wxIfHeadimgurl;
	}

	public void setWxIfHeadimgurl(String wxIfHeadimgurl) {
		this.wxIfHeadimgurl = wxIfHeadimgurl;
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
		this.wxIfUnionid = wxIfUnionid;
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
		this.wxIdP = wxIdP;
	}

	public String getCustNickNm() {
		return custNickNm;
	}

	public void setCustNickNm(String custNickNm) {
		this.custNickNm = custNickNm;
	}

	public String getCustRealNm() {
		return custRealNm;
	}

	public void setCustRealNm(String custRealNm) {
		this.custRealNm = custRealNm;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Integer getWxIfGroupid() {
		return wxIfGroupid;
	}

	public void setWxIfGroupid(Integer wxIfGroupid) {
		this.wxIfGroupid = wxIfGroupid;
	}

	public String getWxIfRemark() {
		return wxIfRemark;
	}

	public void setWxIfRemark(String wxIfRemark) {
		this.wxIfRemark = wxIfRemark;
	}
}
