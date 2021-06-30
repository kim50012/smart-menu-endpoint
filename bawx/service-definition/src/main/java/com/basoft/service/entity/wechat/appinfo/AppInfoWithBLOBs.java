package com.basoft.service.entity.wechat.appinfo;

public class AppInfoWithBLOBs extends AppInfo {
	private String ifPassword;

	private String ifSignkey;

	private String ifPushurl;

	private String ssoUrl;

	public String getIfPassword() {
		return ifPassword;
	}

	public void setIfPassword(String ifPassword) {
		this.ifPassword = ifPassword == null ? null : ifPassword.trim();
	}

	public String getIfSignkey() {
		return ifSignkey;
	}

	public void setIfSignkey(String ifSignkey) {
		this.ifSignkey = ifSignkey == null ? null : ifSignkey.trim();
	}

	public String getIfPushurl() {
		return ifPushurl;
	}

	public void setIfPushurl(String ifPushurl) {
		this.ifPushurl = ifPushurl == null ? null : ifPushurl.trim();
	}

	public String getSsoUrl() {
		return ssoUrl;
	}

	public void setSsoUrl(String ssoUrl) {
		this.ssoUrl = ssoUrl == null ? null : ssoUrl.trim();
	}

	@Override
	public String toString() {
		return "AppInfoWithBLOBs [ifPassword=" + ifPassword + ", ifSignkey=" + ifSignkey + ", ifPushurl=" + ifPushurl + ", ssoUrl=" + ssoUrl + ", getSysId()=" + getSysId()
				+ ", getShopId()=" + getShopId() + ", getOriginalAppId()=" + getOriginalAppId() + ", getCompNm()=" + getCompNm() + ", getAppId()=" + getAppId()
				+ ", getAppSecret()=" + getAppSecret() + ", getUrl()=" + getUrl() + ", getDomain()=" + getDomain() + ", getToken()=" + getToken() + ", getEncordingAesKey()="
				+ getEncordingAesKey() + ", getWechatNo()=" + getWechatNo() + ", getAccountType()=" + getAccountType() + ", getTransferCustomerService()="
				+ getTransferCustomerService() + ", getAccountStatus()=" + getAccountStatus() + ", getOpenBatchJob()=" + getOpenBatchJob() + ", getInterfaced()=" + getInterfaced()
				+ ", getIfUserid()=" + getIfUserid() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
}