package com.basoft.eorder.domain.model;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:35 2018/12/4
 **/
public class WxAppInfoTable {
    private String sysId;
    private Long shopId;

    private String originalAppId;
    private String compNm;
    private String appId;
    private String appSecret;
    private String url;
    private String token;
    private String encordingAesKey;

    public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getOriginalAppId() {
		return originalAppId;
	}

	public void setOriginalAppId(String originalAppId) {
		this.originalAppId = originalAppId;
	}

	public String getCompNm() {
		return compNm;
	}

	public void setCompNm(String compNm) {
		this.compNm = compNm;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEncordingAesKey() {
		return encordingAesKey;
	}

	public void setEncordingAesKey(String encordingAesKey) {
		this.encordingAesKey = encordingAesKey;
	}

	public static final class Builder{
	    private String sysId;
	    private Long shopId;

	    private String originalAppId;
	    private String compNm;
	    private String appId;
	    private String appSecret;
	    private String url;
	    private String token;
	    private String encordingAesKey;

        public Builder setSysId(String sysId){
            this.sysId = sysId;
            return this;
        }
        public Builder setShopId(Long shopId){
            this.shopId = shopId;
            return this;
        }
        public Builder setOriginalAppId(String originalAppId){
            this.originalAppId = originalAppId;
            return this;
        }
        public Builder setCompNm(String compNm){
            this.compNm = compNm;
            return this;
        }
        public Builder setAppId(String appId){
            this.appId = appId;
            return this;
        }
        public Builder setAppSecret(String appSecret){
            this.appSecret = appSecret;
            return this;
        }
        public Builder setUrl(String url){
            this.url = url;
            return this;
        }
        public Builder setToken(String token){
            this.token = token;
            return this;
        }
        public Builder setEncordingAesKey(String encordingAesKey){
            this.encordingAesKey = encordingAesKey;
            return this;
        }

        public WxAppInfoTable build(){
            WxAppInfoTable table = new WxAppInfoTable();
            table.sysId = this.sysId;
            table.shopId = this.shopId;
            table.originalAppId = this.originalAppId;
            table.compNm = this.compNm;
            table.appId = this.appId;
            table.appSecret = this.appSecret;
            table.url = this.url;
            table.token = this.token;
            table.encordingAesKey = this.encordingAesKey;
            return table;
        }
    }
}
