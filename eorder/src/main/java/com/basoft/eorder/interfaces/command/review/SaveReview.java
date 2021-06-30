package com.basoft.eorder.interfaces.command.review;


import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Review;

import java.util.Date;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:21 2019/5/13
 **/
public class SaveReview  implements Command {

    public static final String NAME = "saveReview";

    private Long revId;

    private String platformId;//评价人平台ID。微信为OPENID

    private String nickname;//平台用户昵称。取自第三方平台，如微信、支付宝

    private String chathead;//平台用户头像URL。开发时，建议实施获取

    private String revPlatform;//第三方平台。WE-微信平台 ALI-阿里支付宝

    private Long orderId;//评价订单号

    private Long custId;//所属客户

    private Long storeId;//所属店铺

    private String revClass;//综合评价星级。终端用户可见。1-差 2-一般 3-还不错 4-很满意 5-强烈推荐

    private String revTag;//综合评价内容-所选标签。终端用户不可见，店铺管理类端可见。

    private String envClass;//1-差 2-一般 3-还不错 4-很满意 5-强烈推荐。终端用户不可见，店铺管理类端可见。

    private String prodClass;//1-差 2-一般 3-还不错 4-很满意 5-强烈推荐。终端用户不可见，店铺管理类端可见。

    private String serviceClass;//1-差 2-一般 3-还不错 4-很满意 5-强烈推荐。终端用户不可见，店铺管理类端可见。

    private Long averPrice;//人均菜价。此评价感觉无意义，人均菜价取决于客人点菜情况，不具备实际指导意义。

    private String revContent;//评价内容

    private String isAnonymity;//是否匿名评价。1-是 0-否

    private Date revTime;//评价时间

    private String revStatus;//评价状态。1-评论人新发布；2-已审核，可以显示；3-精品评价/优质评价；4-隐藏状态（即删除状态）

    private String revReplyContent;//答复内容

    private String revReplier;//答复人

    private Date revReplyTime;//答复时间

    private Date modifiedDt;

    private String modifiedUserId;

    private List<Review.RevImg> revImageList;


    public Long getRevId() {
        return revId;
    }

    public void setRevId(Long revId) {
        this.revId = revId;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getChathead() {
        return chathead;
    }

    public void setChathead(String chathead) {
        this.chathead = chathead;
    }

    public String getRevPlatform() {
        return revPlatform;
    }

    public void setRevPlatform(String revPlatform) {
        this.revPlatform = revPlatform;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getRevClass() {
        return revClass;
    }

    public void setRevClass(String revClass) {
        this.revClass = revClass;
    }

    public String getRevTag() {
        return revTag;
    }

    public void setRevTag(String revTag) {
        this.revTag = revTag;
    }

    public String getEnvClass() {
        return envClass;
    }

    public void setEnvClass(String envClass) {
        this.envClass = envClass;
    }

    public String getProdClass() {
        return prodClass;
    }

    public void setProdClass(String prodClass) {
        this.prodClass = prodClass;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Long getAverPrice() {
        return averPrice;
    }

    public void setAverPrice(Long averPrice) {
        this.averPrice = averPrice;
    }

    public String getRevContent() {
        return revContent;
    }

    public void setRevContent(String revContent) {
        this.revContent = revContent;
    }

    public String getIsAnonymity() {
        return isAnonymity;
    }

    public void setIsAnonymity(String isAnonymity) {
        this.isAnonymity = isAnonymity;
    }

    public Date getRevTime() {
        return revTime;
    }

    public void setRevTime(Date revTime) {
        this.revTime = revTime;
    }

    public String getRevStatus() {
        return revStatus;
    }

    public void setRevStatus(String revStatus) {
        this.revStatus = revStatus;
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

    public List<Review.RevImg> getRevImageList() {
        return revImageList;
    }

    public void setRevImageList(List<Review.RevImg> revImageList) {
        this.revImageList = revImageList;
    }

    public Review build(){

        return new Review.Builder()
            .revId(revId)
            .platformId(platformId)
            .nickname(nickname)
            .chathead(chathead)
            .revPlatform(revPlatform)
            .orderId(orderId)
            .custId(custId)
            .storeId(storeId)
            .revClass(revClass)
            .revTag(revTag)
            .envClass(envClass)
            .prodClass(prodClass)
            .serviceClass(serviceClass)
            .revContent(revContent)
            .isAnonymity(isAnonymity)
            .revStatus(Review.REV_START)
            .revReplyContent(revReplyContent)
            .revReplier(revReplier)
            .modifiedUserId(modifiedUserId)
            .revImageList(revImageList)
            .build();
    }



}
