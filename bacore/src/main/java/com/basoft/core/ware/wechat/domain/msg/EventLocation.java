package com.basoft.core.ware.wechat.domain.msg;

import java.util.Date;

/**
 * 回复图片消息
 */
public class EventLocation {
	private Long id;
	private String sysId = "";
	private String openid = ""; // 开发者微信号
	private String longitude = "";// 消息创建时间 （整型）
	private String latitude = "";// 消息创建时间 （整型）
	private String precision = "";// 消息创建时间 （整型）
	private Date createDate;

	public EventLocation() {
		super();
	}

	/**
	 * @param sysId
	 * @param openid
	 * @param longitude
	 * @param latitude
	 * @param precision
	 * @param createDate
	 */
	public EventLocation(String sysId, String openid, String longitude, String latitude, String precision,
			Date createDate) {
		super();
		this.sysId = sysId;
		this.openid = openid;
		this.longitude = longitude;
		this.latitude = latitude;
		this.precision = precision;
		this.createDate = createDate;
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

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}