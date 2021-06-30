package com.basoft.service.entity.wechat.shopWxNews;

import java.util.Date;

public class ShopWxNewsCust extends ShopWxNewsCustKey {
    private String openid;

    private Integer sendType;

    private Integer sendSts;

    private Date sendDt;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getSendSts() {
        return sendSts;
    }

    public void setSendSts(Integer sendSts) {
        this.sendSts = sendSts;
    }

    public Date getSendDt() {
        return sendDt;
    }

    public void setSendDt(Date sendDt) {
        this.sendDt = sendDt;
    }
}