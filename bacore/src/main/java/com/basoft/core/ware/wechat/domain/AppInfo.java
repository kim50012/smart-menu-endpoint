package com.basoft.core.ware.wechat.domain;

/**
 * 公众账号信息
 * 
 * @author basoft
 */
public class AppInfo {
	private String sysId;
	private Integer shopId;
	private String shopNm;
	private String originalAppId;
	private String appId;
	private String appSecret;
	private String url;
	private String token;
	private String encordingAesKey;
	private Integer accountType;
	private Integer transferCustomerService;

	// 第三方调用接口凭证
	private Integer interfaced; // 是否使用第三方接口
	private String ifUserId;// id
	private String ifPassword; // 密码
	private String ifSignKey; // 加密锁
	private String ifPushUrl; // 接口网站
	private String ssoUrl; // SSO登陆地址

	// 开放平台
	private String authorizerRefreshToken; // 接口调用凭据刷新令牌
	private String authorizerAccessToken; // 授权方令牌
	private String authorizerAccessTokenExpiresIn; // 授权方令牌 过期时间
	private String nickName; // 授权方微信昵称
	private String headImg; // 授权方头像
	private String verifyTypeInfo; // 授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
	private String alias; // 授权方公众号所设置的微信号，可能为空
	private String qrcodeUrl; // 授权方二维码图片url
	private String isAuthorizer; // 授权状态：0未授权 1 已授权

	private String openStore;// open_store:是否开通微信门店功能 （0代表未开通，1代表已开通）
	private String openScan;// open_scan:是否开通微信扫商品功能 （0代表未开通，1代表已开通）
	private String openPay;// open_pay:是否开通微信支付功能 （0代表未开通，1代表已开通）
	private String openCard;// open_card:是否开通微信卡券功能 （0代表未开通，1代表已开通）
	private String openShake;// open_shake:是否开通微信摇一摇功能 （0代表未开通，1代表已开通）

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getShopNm() {
		return shopNm;
	}

	public void setShopNm(String shopNm) {
		this.shopNm = shopNm;
	}

	public String getOriginalAppId() {
		return originalAppId;
	}

	public void setOriginalAppId(String originalAppId) {
		this.originalAppId = originalAppId;
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

	public String getDomain() {
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

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Integer getTransferCustomerService() {
		return transferCustomerService;
	}

	public void setTransferCustomerService(Integer transferCustomerService) {
		this.transferCustomerService = transferCustomerService;
	}

	public Integer getInterfaced() {
		return interfaced;
	}

	public void setInterfaced(Integer interfaced) {
		this.interfaced = interfaced;
	}

	public String getIfUserId() {
		return ifUserId;
	}

	public void setIfUserId(String ifUserId) {
		this.ifUserId = ifUserId;
	}

	public String getIfPassword() {
		return ifPassword;
	}

	public void setIfPassword(String ifPassword) {
		this.ifPassword = ifPassword;
	}

	public String getIfSignKey() {
		return ifSignKey;
	}

	public void setIfSignKey(String ifSignKey) {
		this.ifSignKey = ifSignKey;
	}

	public String getIfPushUrl() {
		return ifPushUrl;
	}

	public void setIfPushUrl(String ifPushUrl) {
		this.ifPushUrl = ifPushUrl;
	}

	public String getSsoUrl() {
		return ssoUrl;
	}

	public void setSsoUrl(String ssoUrl) {
		this.ssoUrl = ssoUrl;
	}

	public String getAuthorizerRefreshToken() {
		return authorizerRefreshToken;
	}

	public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
		this.authorizerRefreshToken = authorizerRefreshToken;
	}

	public String getAuthorizerAccessToken() {
		return authorizerAccessToken;
	}

	public void setAuthorizerAccessToken(String authorizerAccessToken) {
		this.authorizerAccessToken = authorizerAccessToken;
	}

	public String getAuthorizerAccessTokenExpiresIn() {
		return authorizerAccessTokenExpiresIn;
	}

	public void setAuthorizerAccessTokenExpiresIn(String authorizerAccessTokenExpiresIn) {
		this.authorizerAccessTokenExpiresIn = authorizerAccessTokenExpiresIn;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getVerifyTypeInfo() {
		return verifyTypeInfo;
	}

	public void setVerifyTypeInfo(String verifyTypeInfo) {
		this.verifyTypeInfo = verifyTypeInfo;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getQrcodeUrl() {
		return qrcodeUrl;
	}

	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}

	public String getIsAuthorizer() {
		return isAuthorizer;
	}

	public void setIsAuthorizer(String isAuthorizer) {
		this.isAuthorizer = isAuthorizer;
	}

	public String getOpenStore() {
		return openStore;
	}

	public void setOpenStore(String openStore) {
		this.openStore = openStore;
	}

	public String getOpenScan() {
		return openScan;
	}

	public void setOpenScan(String openScan) {
		this.openScan = openScan;
	}

	public String getOpenPay() {
		return openPay;
	}

	public void setOpenPay(String openPay) {
		this.openPay = openPay;
	}

	public String getOpenCard() {
		return openCard;
	}

	public void setOpenCard(String openCard) {
		this.openCard = openCard;
	}

	public String getOpenShake() {
		return openShake;
	}

	public void setOpenShake(String openShake) {
		this.openShake = openShake;
	}
}