package com.basoft.core.ware.wechat.exception;

public class InterfaceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InterfaceException() {
		super();
	}

	public InterfaceException(String message, Throwable cause) {
		super(message, cause);
	}

	public InterfaceException(String message) {
		super(message);
	}

	public InterfaceException(Throwable cause) {
		super(cause);
	}

	public InterfaceException(Integer errcode) {
		super(errcode + "");
	}

	public InterfaceException(Integer errcode, String message) {
		super("微信接口调用错误：错误原因[errcode = " + errcode + ",errmsg=" + message + "]");
	}
}
