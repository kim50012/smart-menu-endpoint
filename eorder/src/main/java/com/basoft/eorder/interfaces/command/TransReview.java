package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;

/**
 * @Author:DongXifu 翻译评价
 * @Description:
 * @Date Created in 下午6:28 2019/5/28
 **/
public class TransReview implements Command{

    public static final String NAME = "translateReview";

    private Long revId;

    private Long orderId;//评价订单号

    private String revContent;//评价内容

    private String revContentCopie;//评价内容翻译

    private String revReplyContent;//回复内容

    private String revReplyContentCopie;//回复内容翻译

    private String query;

    public Long getRevId() {
        return revId;
    }

    public void setRevId(Long revId) {
        this.revId = revId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getRevContent() {
        return revContent;
    }

    public void setRevContent(String revContent) {
        this.revContent = revContent;
    }

    public String getRevReplyContent() {
        return revReplyContent;
    }

    public void setRevReplyContent(String revReplyContent) {
        this.revReplyContent = revReplyContent;
    }

    public String getRevContentCopie() {
        return revContentCopie;
    }

    public void setRevContentCopie(String revContentCopie) {
        this.revContentCopie = revContentCopie;
    }

    public String getRevReplyContentCopie() {
        return revReplyContentCopie;
    }

    public void setRevReplyContentCopie(String revReplyContentCopie) {
        this.revReplyContentCopie = revReplyContentCopie;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
