package com.basoft.service.entity.wechat.shopWxNews;

import java.util.Date;

public class ShopWxNewsHead extends ShopWxNewsHeadKey {
    private String msgNm;

    private Integer isDelete;

    private String createdId;

    private String modifiedId;

    private Date createdDt;

    private Date modifiedDt;

    private String wxMsgId;

    private Long wxMsgDataId;

    private String wxMsgErr;

    public String getMsgNm() {
        return msgNm;
    }

    public void setMsgNm(String msgNm) {
        this.msgNm = msgNm == null ? null : msgNm.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
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

    public String getWxMsgId() {
        return wxMsgId;
    }

    public void setWxMsgId(String wxMsgId) {
        this.wxMsgId = wxMsgId;
    }

    public Long getWxMsgDataId() {
        return wxMsgDataId;
    }

    public void setWxMsgDataId(Long wxMsgDataId) {
        this.wxMsgDataId = wxMsgDataId;
    }

    public String getWxMsgErr() {
        return wxMsgErr;
    }

    public void setWxMsgErr(String wxMsgErr) {
        this.wxMsgErr = wxMsgErr == null ? null : wxMsgErr.trim();
    }
}