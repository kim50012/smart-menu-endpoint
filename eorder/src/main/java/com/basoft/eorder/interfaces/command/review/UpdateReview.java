package com.basoft.eorder.interfaces.command.review;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Review;

import java.util.Date;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:39 2019/5/14
 **/
public class UpdateReview implements Command{

    public static final String REPLY_NAME = "replyReview";
    public static final String DEL_REVIEW = "delReview";

    private Long revId;

    private String revReplyContent;//答复内容

    private String revReplier;//答复人

    private Date revReplyTime;//答复时间

    private Date modifiedDt;

    private String modifiedUserId;

    private String revStatus;

    public Long getRevId() {
        return revId;
    }

    public void setRevId(Long revId) {
        this.revId = revId;
    }

    public String getRevReplyContent() {
        return revReplyContent;
    }

    public void setRevReplyContent(String revReplyContent) {
        this.revReplyContent = revReplyContent;
    }

    public String getRevReplier() {
        return revReplier;
    }

    public void setRevReplier(String revReplier) {
        this.revReplier = revReplier;
    }

    public Date getRevReplyTime() {
        return revReplyTime;
    }

    public void setRevReplyTime(Date revReplyTime) {
        this.revReplyTime = revReplyTime;
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

    public String getRevStatus() {
        return revStatus;
    }

    public void setRevStatus(String revStatus) {
        this.revStatus = revStatus;
    }

    public Review build(Review r){
        return new Review.Builder()
            .revId(revId)
            .orderId(r.getOrderId())
            .custId(r.getCustId())
            .storeId(r.getStoreId())
            .revReplyContent(revReplyContent)
            .revReplier(revReplier)
            .modifiedUserId(modifiedUserId)
            .revStatus(revStatus)
            .build();
    }




}
