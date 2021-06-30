package com.basoft.core.ware.wechat.domain.customservice;

/**
 * Record
 * 
 * 操作ID(会化状态）定义：
 * ID值 说明 1000 创建未接入会话 1001 接入会话 1002 主动发起会话 1004 关闭会话 1005 抢接会话 2001 公众号收到消息 2002 客服发送消息 2003 客服收到消息
 */
public class Record {
	private String worker; // 客服账号
	private String openid;// 用户的标识，对当前公众号唯一
	private Integer opercode;// 操作ID（会话状态），具体说明见下文
	private Long time;// 操作时间，UNIX时间戳
	private String text;// 聊天记录

	public String getWorker() {
		return worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getOpercode() {
		return opercode;
	}

	public void setOpercode(Integer opercode) {
		this.opercode = opercode;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		String opernm = "";
		if (opercode == 1000) {
			opernm = "创建未接入会话";
		} else if (opercode == 1001) {
			opernm = "接入会话";
		} else if (opercode == 1002) {
			opernm = "主动发起会话";
		} else if (opercode == 1003) {
			opernm = "转接会话";
		} else if (opercode == 1004) {
			opernm = "关闭会话";
		} else if (opercode == 1005) {
			opernm = "抢接会话";
		} else if (opercode == 2001) {
			opernm = "公众号收到消息";
		} else if (opercode == 2002) {
			opernm = "客服发送消息";
		} else if (opercode == 2003) {
			opernm = "客服收到消息";
		}
		return "Record [worker=" + worker + ", openid=" + openid + ", opercode=" + opercode + ", opernm=" + opernm
				+ ", time=" + time + ", text=" + text + "]";
	}
}