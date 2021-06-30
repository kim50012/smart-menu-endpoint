package com.basoft.service.entity.wechat.reply;

public class ShopMessageKeyword extends ShopMessageKeywordKey {
    private String keyword;

    private String tableNm;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public String getTableNm() {
        return tableNm;
    }

    public void setTableNm(String tableNm) {
        this.tableNm = tableNm == null ? null : tableNm.trim();
    }
}