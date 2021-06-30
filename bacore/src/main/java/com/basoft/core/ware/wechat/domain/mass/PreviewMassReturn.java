package com.basoft.core.ware.wechat.domain.mass;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

public class PreviewMassReturn extends WeixinReturn {
	private Long msg_id;

	public Long getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(Long msg_id) {
		this.msg_id = msg_id;
	}

	@Override
	public String toString() {
		return "PreviewMassReturn [msg_id=" + msg_id + ", errcode=" + getErrcode() + ", errmsg=" + getErrmsg() + "]";
	}
}
