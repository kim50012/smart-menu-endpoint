package com.basoft.service.entity.wechat.reply;

import java.util.Date;

public class ShopWxMessage {
    private Long messageId;

    private Long shopId;

    private String msgGroup;

    private Integer sendFileType;

    private Long materialFileId;

    private Integer coverPageId;

    private Integer sendType;

    private Date sendTime;

    private String createdId;

    private String modifiedId;

    private Date createdDt;

    private Date modifiedDt;

    private Integer isDelete;

    private Integer sendTypeId;

    //private String callcenterId;
    
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getMsgGroup() {
        return msgGroup;
    }

    public void setMsgGroup(String msgGroup) {
        this.msgGroup = msgGroup == null ? null : msgGroup.trim();
    }

    public Integer getSendFileType() {
        return sendFileType;
    }

    public void setSendFileType(Integer sendFileType) {
        this.sendFileType = sendFileType;
    }

    public Long getMaterialFileId() {
        return materialFileId;
    }

    public void setMaterialFileId(Long materialFileId) {
        this.materialFileId = materialFileId;
    }

    public Integer getCoverPageId() {
        return coverPageId;
    }

    public void setCoverPageId(Integer coverPageId) {
        this.coverPageId = coverPageId;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId == null ? null : createdId.trim();
    }

    public String getModifiedId() {
        return modifiedId;
    }

    public void setModifiedId(String modifiedId) {
        this.modifiedId = modifiedId == null ? null : modifiedId.trim();
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getModifiedDt() {
        return modifiedDt;
    }

    public void setModifiedDt(Date modifiedDt) {
        this.modifiedDt = modifiedDt;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getSendTypeId() {
        return sendTypeId;
    }

    public void setSendTypeId(Integer sendTypeId) {
        this.sendTypeId = sendTypeId;
    }


    
}