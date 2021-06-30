package com.basoft.core.ware.wechat.domain;

/**
 * 微信商户号信息
 * 
 * @author basoft
 */
public class MchInfo {
	// 系统ID
	private String sysId;
	// 商户号
	private String mchId;
	// API密钥
	private String secretKey;

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@Override
	public String toString() {
		return "MchInfo [sysId=" + sysId + ", mchId=" + mchId + ", secretKey=" + secretKey + "]";
	}
}