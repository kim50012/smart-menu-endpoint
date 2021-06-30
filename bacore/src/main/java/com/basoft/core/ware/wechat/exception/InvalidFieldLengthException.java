package com.basoft.core.ware.wechat.exception;

/**
 * @author basoft
 */
public class InvalidFieldLengthException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String field = "";

	private Integer length;

	public InvalidFieldLengthException(String field, Integer length) {
		super(field);
		this.field = field;
		this.length = length;
	}

	@Override
	public String getMessage() {
		String message = "参数错误! " + field + "字段长度不能超过" + length + "字符";
		return message;
	}
}