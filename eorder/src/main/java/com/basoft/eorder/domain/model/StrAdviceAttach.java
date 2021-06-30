package com.basoft.eorder.domain.model;

import java.util.Date;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:40 2019/6/4
 **/
public class StrAdviceAttach {

    private Long adviAttachId;

    private Long adviId;

    private String contentId;

    private String contentUrl;

    private Byte displayOrder;

    private String isDisplay;

    private Date createdDt;

    private String createdUserId;

    private Date modifiedDt;

    private String modifiedUserId;


    public Long getAdviAttachId() {
        return adviAttachId;
    }

    public void setAdviAttachId(Long adviAttachId) {
        this.adviAttachId = adviAttachId;
    }

    public Long getAdviId() {
        return adviId;
    }

    public void setAdviId(Long adviId) {
        this.adviId = adviId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
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
        this.isDisplay = isDisplay;
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
        this.createdUserId = createdUserId;
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
        this.modifiedUserId = modifiedUserId;
    }
}
