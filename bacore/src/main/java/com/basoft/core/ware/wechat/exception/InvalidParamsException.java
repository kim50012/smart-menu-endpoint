package com.basoft.core.ware.wechat.exception;

public class InvalidParamsException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String field = "";

	public InvalidParamsException(String field) {
		super(field);
		this.field = field;
	}

	@Override
	public String getMessage() {
		String message = "参数错误! " + field + "字段必填";
		return message;
	}
}