package com.basoft.core.ware.wechat.exception;

public class WeixinAuthException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public final static int APP_INFO_ERROR = 10001;
	public final static int MCH_INFO_ERROR = 10002;

	private int code;

	private static String getMessage(int code) {
		switch (code) {
		case APP_INFO_ERROR:
			return "微信公众账号错误";
		case MCH_INFO_ERROR:
			return "微信支付账号错误";
		default:
			return null; // cannot be
		}
	}

	public int getCode() {
		return code;
	}

	public WeixinAuthException(int code) {
		super(getMessage(code));
		this.code = code;
	}
}