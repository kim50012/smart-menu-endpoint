package com.basoft.core.ware.wechat.domain.msg;

/**
 * 回复图片消息
 */
public class Location {
	private Long id;
	private String sysId = "";
	private String msgId = ""; // 消息id，64位整型
	private String toUserName = ""; // 开发者微信号
	private String fromUserName = "";// 发送方帐号（一个OpenID）
	private String createTime = "";// 消息创建时间 （整型）
	private String msgType = "";// location
	private String location_x = "";// 地理位置维度
	private String location_y = "";// 地理位置经度
	private String scale = ""; // 地图缩放大小
	private String label = ""; // 地理位置信息

	public Location() {
		super();
	}

	/**
	 * @param sysId
	 * @param msgId
	 * @param fromUserName
	 * @param toUserName
	 * @param createTime
	 * @param msgType
	 * @param location_x
	 * @param location_y
	 * @param scale
	 * @param label
	 */
	public Location(String sysId, String msgId, String fromUserName, String toUserName, String createTime,
			String msgType, String location_x, String location_y, String scale, String label) {
		super();
		this.sysId = sysId;
		this.msgId = msgId;
		this.fromUserName = fromUserName;
		this.toUserName = toUserName;
		this.createTime = createTime;
		this.msgType = msgType;
		this.location_x = location_x;
		this.location_y = location_y;
		this.scale = scale;
		this.label = label;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", sysId=" + sysId + ", msgId=" + msgId + ", toUserName=" + toUserName
				+ ", fromUserName=" + fromUserName + ", createTime=" + createTime + ", msgType=" + msgType
				+ ", location_x=" + location_x + ", location_y=" + location_y + ", scale=" + scale + ", label=" + label
				+ "]";
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

	public String getLocation_x() {
		return location_x;
	}

	public void setLocation_x(String location_x) {
		this.location_x = location_x;
	}

	public String getLocation_y() {
		return location_y;
	}

	public void setLocation_y(String location_y) {
		this.location_y = location_y;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}