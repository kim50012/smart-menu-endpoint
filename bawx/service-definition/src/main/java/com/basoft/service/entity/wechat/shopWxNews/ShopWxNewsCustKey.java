package com.basoft.service.entity.wechat.shopWxNews;

public class ShopWxNewsCustKey {
    private Long shopId;

    private Long msgId;

    private Long custSysId;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Long getCustSysId() {
        return custSysId;
    }

    public void setCustSysId(Long custSysId) {
        this.custSysId = custSysId;
    }
}