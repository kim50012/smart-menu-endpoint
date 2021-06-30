package com.basoft.core.ware.wechat.domain.template;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

public class SendTemplateMessageReturn extends WeixinReturn {
	private Long msgid;

	public Long getMsgid() {
		return msgid;
	}

	public void setMsgid(Long msgid) {
		this.msgid = msgid;
	}
}