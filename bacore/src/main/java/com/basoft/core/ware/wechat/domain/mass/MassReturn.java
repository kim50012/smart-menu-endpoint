package com.basoft.core.ware.wechat.domain.mass;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

public class MassReturn extends WeixinReturn {
	private String type;// 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb），次数为news，即图文消息

	private Long msg_id;// 消息ID

	private Long msg_data_id;// 消息的数据ID，该字段只有在群发图文消息时，才会出现。可以用于在图文分析数据接口中，
	// 获取到对应的图文消息的数据，是图文分析数据接口中的msgid字段中的前半部分，详见图文分析数据接口中的msgid字段的介绍。

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(Long msg_id) {
		this.msg_id = msg_id;
	}

	public Long getMsg_data_id() {
		return msg_data_id;
	}

	public void setMsg_data_id(Long msg_data_id) {
		this.msg_data_id = msg_data_id;
	}

	@Override
	public String toString() {
		return "MassRetuen [type=" + type + ", msg_id=" + msg_id + ", msg_data_id=" + msg_data_id + "]";
	}
}