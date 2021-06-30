package com.basoft.eorder.domain.model;

import java.util.Date;

/**
 * @Description
 * @author Dong Xifu
 * @date 2019/5/24 上午11:09
 */
public class StrReviewAttach {

    private Long revAttachId;

    private Long revId;

    private String contentId;

    private String contentUrl;

    private Byte displayOrder;

    private String isDisplay;

    private Date createdDt;

    private String createdUserId;

    private Date modifiedDt;

    private String modifiedUserId;

    public Long getRevAttachId() {
        return revAttachId;
    }

    public void setRevAttachId(Long revAttachId) {
        this.revAttachId = revAttachId;
    }

    public Long getRevId() {
        return revId;
    }

    public void setRevId(Long revId) {
        this.revId = revId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId == null ? null : contentId.trim();
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl == null ? null : contentUrl.trim();
    }

    public Byte getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Byte displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(String isDisplay) {
        this.isDisplay = isDisplay == null ? null : isDisplay.trim();
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId == null ? null : createdUserId.trim();
    }

    public Date getModifiedDt() {
        return modifiedDt;
    }

    public void setModifiedDt(Date modifiedDt) {
        this.modifiedDt = modifiedDt;
    }

    public String getModifiedUserId() {
        return modifiedUserId;
    }

    public void setModifiedUserId(String modifiedUserId) {
        this.modifiedUserId = modifiedUserId == null ? null : modifiedUserId.trim();
    }
}