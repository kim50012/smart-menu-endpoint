package com.basoft.core.ware.wechat.domain.customservice;

public class WaitCase {
	private Integer createtime;// 用户来访时间，UNIX时间戳
	private String kf_account;// 指定接待的客服，为空表示未指定客服
	private String openid; // 客户openid

	public Integer getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Integer createtime) {
		this.createtime = createtime;
	}

	public String getKf_account() {
		return kf_account;
	}

	public void setKf_account(String kf_account) {
		this.kf_account = kf_account;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
}
