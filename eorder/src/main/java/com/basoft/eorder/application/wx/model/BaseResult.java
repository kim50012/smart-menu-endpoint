package com.basoft.eorder.application.wx.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 微信请求状态数据
 */
public class BaseResult {

	private String return_code;
	private String return_msg;

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public final static class Builder {
		private String return_code;
		private String return_msg;

		public Builder return_code(String return_code) {
			this.return_code = return_code;
			return this;
		}

		public Builder return_msg(String return_msg) {
			this.return_msg = return_msg;
			return this;
		}

		public BaseResult build() {
			BaseResult result = new BaseResult();
			result.setReturn_code(this.return_code);
			result.setReturn_msg(this.return_msg);
			return result;
		}
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
