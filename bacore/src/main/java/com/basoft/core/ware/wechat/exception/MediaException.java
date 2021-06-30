package com.basoft.core.ware.wechat.exception;

public class MediaException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MediaException() {
		// TODO Auto-generated constructor stub
	}

	public MediaException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MediaException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MediaException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public MediaException(Integer errorCode, String errorMessage) {
		super("MediaException [errorCode=" + errorCode + ",errorMessage=" + errorMessage + "]");
		// TODO Auto-generated constructor stub
	}
}