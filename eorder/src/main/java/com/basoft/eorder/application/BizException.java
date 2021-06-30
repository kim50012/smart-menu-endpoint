package com.basoft.eorder.application;

import org.apache.commons.lang3.exception.ExceptionUtils;

@SuppressWarnings("serial")
public class BizException extends RuntimeException {
	private ErrorCode ec;
	private String exceptionErrorMsg;

	public BizException(ErrorCode ec) {
		this.ec = ec;
	}

	public BizException(ErrorCode ec, Exception e) {
		this.ec = ec;
		this.exceptionErrorMsg = ExceptionUtils.getStackTrace(e);
	}

	public BizException(ErrorCode ec, String errorMsg) {
		this.ec = ec;
		this.exceptionErrorMsg = errorMsg;
	}

	public ErrorCode getEc() {
		return ec;
	}

	public void setEc(ErrorCode ec) {
		this.ec = ec;
	}

	public String getExceptionErrorMsg() {
		return exceptionErrorMsg;
	}

	public void setExceptionErrorMsg(String exceptionErrorMsg) {
		this.exceptionErrorMsg = exceptionErrorMsg;
	}
}