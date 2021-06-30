package com.basoft.core.ware.wechat.domain.qrcode;

import java.util.Date;

public class QRCodeDomain {
	private Integer shopId; // 店铺ID
	private Integer qrcodeId; // ID
	private Integer sceneId; // 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
	private String qrcodeType; // 二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
	private Date expireDate; // 二维码有效时间
	private String ticket; // 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码
	private String wxUrl; // 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
	private String fullUrl; // 系统保存图片网页全路径
	private Integer width;// 二维码宽度
	private Integer height;// 二维码高度
	private Integer createdId;
	private Integer modifiedId;

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getQrcodeId() {
		return qrcodeId;
	}

	public void setQrcodeId(Integer qrcodeId) {
		this.qrcodeId = qrcodeId;
	}

	public Integer getSceneId() {
		return sceneId;
	}

	public void setSceneId(Integer sceneId) {
		this.sceneId = sceneId;
	}

	public String getQrcodeType() {
		return qrcodeType;
	}

	public void setQrcodeType(String qrcodeType) {
		this.qrcodeType = qrcodeType;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getWxUrl() {
		return wxUrl;
	}

	public void setWxUrl(String wxUrl) {
		this.wxUrl = wxUrl;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getCreatedId() {
		return createdId;
	}

	public void setCreatedId(Integer createdId) {
		this.createdId = createdId;
	}

	public Integer getModifiedId() {
		return modifiedId;
	}

	public void setModifiedId(Integer modifiedId) {
		this.modifiedId = modifiedId;
	}
}