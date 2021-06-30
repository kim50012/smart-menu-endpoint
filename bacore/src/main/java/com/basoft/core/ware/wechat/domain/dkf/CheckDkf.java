package com.basoft.core.ware.wechat.domain.dkf;

/**
 * 验证是否转发到多客服系统
 */
public class CheckDkf {
	// IS_DKF IS_AUTO KF_ACCOUNT
	private int dkf;
	private int auto;
	private String kf_account;

	public int getDkf() {
		return dkf;
	}

	public boolean isDkf() {
		return dkf == 1;
	}

	public void setDkf(int dkf) {
		this.dkf = dkf;
	}

	public boolean isAuto() {
		return auto == 1;
	}

	public int getAuto() {
		return auto;
	}

	public void setAuto(int auto) {
		this.auto = auto;
	}

	public String getKf_account() {
		return kf_account;
	}

	public void setKf_account(String kf_account) {
		this.kf_account = kf_account;
	}

	@Override
	public String toString() {
		return "CheckDkf [dkf=" + dkf + ", auto=" + auto + ", kf_account=" + kf_account + "]";
	}
}
