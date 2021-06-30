package com.basoft.core.ware.wechat.domain.msg;

/**
 * 回复图片消息
 */
public class Link {
	private Long id;
	private String sysId = "";
	private String msgId = ""; // 消息id，64位整型
	private String fromUserName = "";// 发送方帐号（一个OpenID）
	private String toUserName = ""; // 开发者微信号
	private String createTime = "";// 消息创建时间 （整型）
	private String msgType = "";// location
	private String title = "";// 消息标题
	private String description = "";// 消息描述
	private String url = ""; // 消息链接

	public Link() {
		super();
	}

	/**
	 * @param sysId
	 * @param msgId
	 * @param fromUserName
	 * @param toUserName
	 * @param createTime
	 * @param msgType
	 * @param title
	 * @param description
	 * @param url
	 */
	public Link(String sysId, String msgId, String fromUserName, String toUserName, String createTime, String msgType,
			String title, String description, String url) {
		super();
		this.sysId = sysId;
		this.msgId = msgId;
		this.fromUserName = fromUserName;
		this.toUserName = toUserName;
		this.createTime = createTime;
		this.msgType = msgType;
		this.title = title;
		this.description = description;
		this.url = url;
	}

	@Override
	public String toString() {
		return "Link [id=" + id + ", sysId=" + sysId + ", msgId=" + msgId + ", fromUserName=" + fromUserName
				+ ", toUserName=" + toUserName + ", createTime=" + createTime + ", msgType=" + msgType + ", title="
				+ title + ", description=" + description + ", url=" + url + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}