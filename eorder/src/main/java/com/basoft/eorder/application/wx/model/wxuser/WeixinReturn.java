package com.basoft.eorder.application.wx.model.wxuser;

/**
 * 微信接口返回值
 * 
 * @author basoft
 */
public class WeixinReturn {
	// 错误码
	private int errcode;

	// 错误信息
	private String errmsg;

	public boolean isSeccess() {
		return (errcode == 0);
	}

	public boolean isError() {
		return !isSeccess();
	}

	public WeixinReturn() {
		super();
	}

	public WeixinReturn(int errcode, String errmsg) {
		super();
		this.errcode = errcode;
		this.errmsg = errmsg;
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	@Override
	public String toString() {
		return "WeixinReturn [errcode=" + errcode + ", errmsg=" + errmsg + "]";
	}
}