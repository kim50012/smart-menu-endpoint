package com.basoft.service.entity.wechat.wxMessage;

public class WxMessageWithBLOBs extends WxMessage {
    private String content;

    private String label;

    private String linkDes;

    private String linkUrl;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    public String getLinkDes() {
        return linkDes;
    }

    public void setLinkDes(String linkDes) {
        this.linkDes = linkDes == null ? null : linkDes.trim();
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl == null ? null : linkUrl.trim();
    }
}