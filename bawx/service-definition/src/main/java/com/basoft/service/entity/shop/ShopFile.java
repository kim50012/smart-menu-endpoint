package com.basoft.service.entity.shop;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Date;

public class ShopFile extends ShopFileKey {
    private String fileNm;

    private String fileSysNm;

    private Byte fileType;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long fileSize;

    private String fullUrl;

    private Byte isUse;

    private Date modifiedDt;

    private Date createdDt;

    private String modifiedId;

    private String createdId;

    private Byte fileGroup;

    private String mediaId;

    private String mediaUrl;

    private Date mediaUpDt;

    private Integer imgWidth;

    private Integer imgHeight;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long compId;

    public String getFileNm() {
        return fileNm;
    }

    public void setFileNm(String fileNm) {
        this.fileNm = fileNm == null ? null : fileNm.trim();
    }

    public String getFileSysNm() {
        return fileSysNm;
    }

    public void setFileSysNm(String fileSysNm) {
        this.fileSysNm = fileSysNm == null ? null : fileSysNm.trim();
    }

    public Byte getFileType() {
        return fileType;
    }

    public void setFileType(Byte fileType) {
        this.fileType = fileType;
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
        this.fullUrl = fullUrl == null ? null : fullUrl.trim();
    }

    public Byte getIsUse() {
        return isUse;
    }

    public void setIsUse(Byte isUse) {
        this.isUse = isUse;
    }

    public Date getModifiedDt() {
        return modifiedDt;
    }

    public void setModifiedDt(Date modifiedDt) {
        this.modifiedDt = modifiedDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public String getModifiedId() {
        return modifiedId;
    }

    public void setModifiedId(String modifiedId) {
        this.modifiedId = modifiedId == null ? null : modifiedId.trim();
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId == null ? null : createdId.trim();
    }

    public Byte getFileGroup() {
        return fileGroup;
    }

    public void setFileGroup(Byte fileGroup) {
        this.fileGroup = fileGroup;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId == null ? null : mediaId.trim();
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Date getMediaUpDt() {
        return mediaUpDt;
    }

    public void setMediaUpDt(Date mediaUpDt) {
        this.mediaUpDt = mediaUpDt;
    }

    public Integer getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(Integer imgWidth) {
        this.imgWidth = imgWidth;
    }

    public Integer getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(Integer imgHeight) {
        this.imgHeight = imgHeight;
    }

    public Long getCompId() {
        return compId;
    }

    public void setCompId(Long compId) {
        this.compId = compId;
    }
}