package com.basoft.core.ware.wechat.domain.material;

public class FileItem {
	/**
	 * file id
	 */
	private Integer fileId;

	/**
	 * shop id
	 */
	private Integer shopId;

	/**
	 * 媒体文件ID
	 */
	private String media_id;

	/**
	 * 文件类型 1图片 2声音 3视频
	 */
	private Integer fileType;

	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 文件系统名
	 */
	private String fileSysName;

	/**
	 * 文件大小
	 */
	private Long fileSize;

	/**
	 * 文件网页全路径
	 */
	private String fullUrl;

	/**
	 * 图片文件宽度-px
	 */
	private Integer imageWidth;

	/**
	 * 图片文件高度-px
	 */
	private Integer imageHeight;

	/**
	 * 声音文件时长-秒
	 */
	private Long duration;

	/**
	 * 客户系统Id
	 */
	private Integer custSysId;

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSysName() {
		return fileSysName;
	}

	public void setFileSysName(String fileSysName) {
		this.fileSysName = fileSysName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public Integer getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}

	public Integer getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Integer getCustSysId() {
		return custSysId;
	}

	public void setCustSysId(Integer custSysId) {
		this.custSysId = custSysId;
	}
}