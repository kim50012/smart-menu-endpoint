package com.basoft.core.ware.wechat.domain.mass;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

public class SendMassStatusReturn extends WeixinReturn {
	private Long msg_id;
	private String msg_status;

	public Long getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(Long msg_id) {
		this.msg_id = msg_id;
	}

	public String getMsg_status() {
		return msg_status;
	}

	public void setMsg_status(String msg_status) {
		this.msg_status = msg_status;
	}

	@Override
	public String toString() {
		return "SendMassStatusReturn [msg_id=" + msg_id + ", msg_status=" + msg_status + ", errcode=" + getErrcode()
				+ ", errmsg=" + getErrmsg() + "]";
	}
}
