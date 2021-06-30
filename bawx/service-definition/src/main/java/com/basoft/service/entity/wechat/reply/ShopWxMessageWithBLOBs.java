package com.basoft.service.entity.wechat.reply;

public class ShopWxMessageWithBLOBs extends ShopWxMessage {
    private String msgTitle;

    private String shopGoodsUrl;

    private String sendMsgBody;

    private String materialFileWxId;

    private String callcenterId;

    private String kfNick;

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle == null ? null : msgTitle.trim();
    }

    public String getShopGoodsUrl() {
        return shopGoodsUrl;
    }

    public void setShopGoodsUrl(String shopGoodsUrl) {
        this.shopGoodsUrl = shopGoodsUrl == null ? null : shopGoodsUrl.trim();
    }

    public String getSendMsgBody() {
        return sendMsgBody;
    }

    public void setSendMsgBody(String sendMsgBody) {
        this.sendMsgBody = sendMsgBody == null ? null : sendMsgBody.trim();
    }

    public String getMaterialFileWxId() {
        return materialFileWxId;
    }

    public void setMaterialFileWxId(String materialFileWxId) {
        this.materialFileWxId = materialFileWxId;
    }

    public String getCallcenterId() {
        return callcenterId;
    }

    public void setCallcenterId(String callcenterId) {
        this.callcenterId = callcenterId;
    }

    public String getKfNick() {
        return kfNick;
    }

    public void setKfNick(String kfNick) {
        this.kfNick = kfNick;
    }
}