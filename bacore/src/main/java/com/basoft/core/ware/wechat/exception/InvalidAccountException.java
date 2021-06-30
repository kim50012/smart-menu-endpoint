package com.basoft.core.ware.wechat.exception;

public class InvalidAccountException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidAccountException() {
		super();
	}

	public InvalidAccountException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public InvalidAccountException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidAccountException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidAccountException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}