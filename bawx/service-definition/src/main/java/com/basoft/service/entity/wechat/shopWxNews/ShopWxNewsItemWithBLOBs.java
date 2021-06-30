package com.basoft.service.entity.wechat.shopWxNews;

public class ShopWxNewsItemWithBLOBs extends ShopWxNewsItem {
    private String msourceUrl;

    private String mcontent;

    private String mdigest;

    private String mcontentwechat;

    public String getMsourceUrl() {
        return msourceUrl;
    }

    public void setMsourceUrl(String msourceUrl) {
        this.msourceUrl = msourceUrl == null ? null : msourceUrl.trim();
    }

    public String getMcontent() {
        return mcontent;
    }

    public void setMcontent(String mcontent) {
        this.mcontent = mcontent == null ? null : mcontent.trim();
    }

    public String getMdigest() {
        return mdigest;
    }

    public void setMdigest(String mdigest) {
        this.mdigest = mdigest == null ? null : mdigest.trim();
    }

    public String getMcontentwechat() {
        return mcontentwechat;
    }

    public void setMcontentwechat(String mcontentwechat) {
        this.mcontentwechat = mcontentwechat == null ? null : mcontentwechat.trim();
    }
}