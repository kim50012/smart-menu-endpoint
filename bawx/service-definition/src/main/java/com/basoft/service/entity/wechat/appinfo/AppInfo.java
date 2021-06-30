package com.basoft.service.entity.wechat.appinfo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class AppInfo {
    private String sysId;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long shopId;

    private String originalAppId;

    private String compNm;

    private String appId;

    private String appSecret;

    private String url;

    private String token;

    private String encordingAesKey;

    private String wechatNo;

    private Byte accountType;

    private Byte transferCustomerService;

    private Byte accountStatus;

    private Byte openBatchJob;

    private Byte interfaced;

    private String ifUserid;

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId == null ? null : sysId.trim();
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
        this.originalAppId = originalAppId == null ? null : originalAppId.trim();
    }

    public String getCompNm() {
        return compNm;
    }

    public void setCompNm(String compNm) {
        this.compNm = compNm == null ? null : compNm.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret == null ? null : appSecret.trim();
    }

    public String getUrl() {
        return url;
    }
    
	public String getDomain() {
		return url;
	}

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getEncordingAesKey() {
        return encordingAesKey;
    }

    public void setEncordingAesKey(String encordingAesKey) {
        this.encordingAesKey = encordingAesKey == null ? null : encordingAesKey.trim();
    }

    public String getWechatNo() {
        return wechatNo;
    }

    public void setWechatNo(String wechatNo) {
        this.wechatNo = wechatNo == null ? null : wechatNo.trim();
    }

    public Byte getAccountType() {
        return accountType;
    }

    public void setAccountType(Byte accountType) {
        this.accountType = accountType;
    }

    public Byte getTransferCustomerService() {
        return transferCustomerService;
    }

    public void setTransferCustomerService(Byte transferCustomerService) {
        this.transferCustomerService = transferCustomerService;
    }

    public Byte getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Byte accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Byte getOpenBatchJob() {
        return openBatchJob;
    }

    public void setOpenBatchJob(Byte openBatchJob) {
        this.openBatchJob = openBatchJob;
    }

    public Byte getInterfaced() {
        return interfaced;
    }

    public void setInterfaced(Byte interfaced) {
        this.interfaced = interfaced;
    }

    public String getIfUserid() {
        return ifUserid;
    }

    public void setIfUserid(String ifUserid) {
        this.ifUserid = ifUserid == null ? null : ifUserid.trim();
    }
}