package com.basoft.core.ware.wechat.domain.pay;

public class Result {
	private String return_code;// 返回状态码 SUCCESS/FAIL
	private String return_msg;// 返回信息

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
}