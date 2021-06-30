package com.basoft.core.ware.common.framework.utilities;


public class UploadResult {
	private String originalFilename;
	private String destinationFilename;
	private String fileUrl;
	private String fileExt;
	private Long fileSize;
	private Integer width;
	private Integer height;
	private String realPath;

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public String getDestinationFilename() {
		return destinationFilename;
	}

	public void setDestinationFilename(String destinationFilename) {
		this.destinationFilename = destinationFilename;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
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

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	@Override
	public String toString() {
		return "UploadResult [originalFilename=" + originalFilename + ", destinationFilename=" + destinationFilename + ", fileUrl=" + fileUrl + ", fileExt="
				+ fileExt + ", fileSize=" + fileSize + ", width=" + width + ", height=" + height + ", realPath=" + realPath + "]";
	}
}
