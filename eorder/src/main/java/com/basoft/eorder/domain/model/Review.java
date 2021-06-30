package com.basoft.eorder.domain.model;

import com.basoft.eorder.application.BusinessTypeEnum;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Title 评论
 * @author Dong Xifu
 * @date 2019/5/13 下午2:22
 */
public class Review {

    public static final String REV_START = "1";
    public static final String REV_STATUS_REPLYED = "3";
    public static final String REV_STATUS_DEL = "4";
    public static final String REV_ANONYMITY_HIDE = "1";
    public static final String REV_ANONYMITY_SHOW = "0";

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

    private String revContentCopie;//评价内容翻译

    private String isAnonymity;//是否匿名评价。1-是 0-否

    private Date revTime;//评价时间

    private String revStatus;//评价状态。1-评论人新发布；2-已审核，可以显示；3-精品评价/优质评价；4-隐藏状态（即删除状态）

    private String revReplyContent;//答复内容

    private String revReplyContentCopie;//回复内容翻译

    private String revReplier;//答复人

    private Date revReplyTime;//答复时间

    private Date modifiedDt;

    private List<RevImg> revImageList = new LinkedList<>();


    private String modifiedUserId;

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
        this.platformId = platformId == null ? null : platformId.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getChathead() {
        return chathead;
    }

    public void setChathead(String chathead) {
        this.chathead = chathead == null ? null : chathead.trim();
    }

    public String getRevPlatform() {
        return revPlatform;
    }

    public void setRevPlatform(String revPlatform) {
        this.revPlatform = revPlatform == null ? null : revPlatform.trim();
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
        this.revClass = revClass == null ? null : revClass.trim();
    }

    public String getRevTag() {
        return revTag;
    }

    public void setRevTag(String revTag) {
        this.revTag = revTag == null ? null : revTag.trim();
    }

    public String getEnvClass() {
        return envClass;
    }

    public void setEnvClass(String envClass) {
        this.envClass = envClass == null ? null : envClass.trim();
    }

    public String getProdClass() {
        return prodClass;
    }

    public void setProdClass(String prodClass) {
        this.prodClass = prodClass == null ? null : prodClass.trim();
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass == null ? null : serviceClass.trim();
    }

    public Long getAverPrice() {
        return averPrice;
    }

    public void setAverPrice(Long averPrice) {
        this.averPrice = averPrice;
    }

    public String getIsAnonymity() {
        return isAnonymity;
    }

    public void setIsAnonymity(String isAnonymity) {
        this.isAnonymity = isAnonymity == null ? null : isAnonymity.trim();
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
        this.revStatus = revStatus == null ? null : revStatus.trim();
    }

    public String getRevReplier() {
        return revReplier;
    }

    public void setRevReplier(String revReplier) {
        this.revReplier = revReplier == null ? null : revReplier.trim();
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
        this.modifiedUserId = modifiedUserId == null ? null : modifiedUserId.trim();
    }

    public String getRevContent() {
        return revContent;
    }

    public void setRevContent(String revContent) {
        this.revContent = revContent;
    }

    public String getRevContentCopie() {
        return revContentCopie;
    }

    public void setRevContentCopie(String revContentCopie) {
        this.revContentCopie = revContentCopie;
    }

    public String getRevReplyContent() {
        return revReplyContent;
    }

    public void setRevReplyContent(String revReplyContent) {
        this.revReplyContent = revReplyContent;
    }

    public String getRevReplyContentCopie() {
        return revReplyContentCopie;
    }

    public void setRevReplyContentCopie(String revReplyContentCopie) {
        this.revReplyContentCopie = revReplyContentCopie;
    }

    public List<RevImg> getRevImageList() {
        return revImageList;
    }

    public void setRevImageList(List<RevImg> revImageList) {
        this.revImageList = revImageList;
    }

    public static class RevImg{

        public static final String REVIMG_SHOW = "1";
        public static final String REVIMG_HIDE = "0";

        private Long revAttachId;

        private Long revId;

        private String contentUrl;

        private int displayOrder;

        private String isDisplay;

        private Date createdDt;

        private RevImg() {

        }

        private RevImg (String pImg,int disIndex, String isShow) {
            this.contentUrl = pImg;
            this.displayOrder = disIndex;
            this.isDisplay = isShow;
        }


        public static RevImg of(String pImg,int disIndex, String isShow){
            return new RevImg(pImg, disIndex, isShow);
        }



        public Long getRevAttachId() {
            return revAttachId;
        }

        public void setRevAttachId(Long revAttachId) {
            this.revAttachId = revAttachId;
        }

        public Long getRevId() {
            return revId;
        }

        public void setRevId(Long revId) {
            this.revId = revId;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public int getDisplayOrder() {
            return displayOrder;
        }

        public void setDisplayOrder(int displayOrder) {
            this.displayOrder = displayOrder;
        }

        public String getIsDisplay() {
            return isDisplay;
        }

        public void setIsDisplay(String isDisplay) {
            this.isDisplay = isDisplay;
        }

        public Date getCreatedDt() {
            return createdDt;
        }

        public void setCreatedDt(Date createdDt) {
            this.createdDt = createdDt;
        }


        public static final class Builder {
            private Long revId;
            public Builder revId(Long revId) {
                this.revId = revId;
                return this;
            }

            public RevImg build() {
                RevImg revImg = new RevImg();
                revImg.revId = this.revId;
                return revImg;
            }
        }
    }

    public static final class Builder {

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

        private String revContent;//评价内容

        private String revContentCopie;//翻译评论内容

        private String isAnonymity;//是否匿名评价。1-是 0-否


        private String revStatus;//评价状态。1-评论人新发布；2-已审核，可以显示；3-精品评价/优质评价；4-隐藏状态（即删除状态）

        private String revReplyContent;//答复内容

        private String revReplyContentCopie;//回复内容翻译

        private String revReplier;//答复人

        private Date revReplyTime;//答复时间

        private String modifiedUserId;//答复时间

        private List<RevImg> revImageList=new LinkedList<>();//评论照片


        public Builder revId(Long revId) {
            this.revId = revId;
            return this;
        }

        public Builder platformId(String platformId) {
            this.platformId = platformId;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder chathead(String chathead) {
            this.chathead = chathead;
            return this;
        }

        public Builder revPlatform(String revPlatform) {
            this.revPlatform = revPlatform;
            return this;
        }

        public Builder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder custId(Long custId) {
            this.custId = custId;
            return this;
        }

        public Builder storeId(Long storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder revClass(String revClass){
            this.revClass = revClass;
            return  this;
        }
        public Builder revTag(String revTag){
            this.revTag = revTag;
            return  this;
        }
        public Builder envClass(String envClass){
            this.envClass = envClass;
            return  this;
        }
        public Builder prodClass(String prodClass){
            this.prodClass = prodClass;
            return  this;
        }
        public Builder serviceClass(String serviceClass){
            this.serviceClass = serviceClass;
            return  this;
        }

        public Builder revContent(String revContent) {
            this.revContent = revContent;
            return this;
        }

        public Builder revContentCopie(String revContentCopie) {
            this.revContentCopie = revContentCopie;
            return this;
        }

        public Builder isAnonymity(String isAnonymity) {
            this.isAnonymity = isAnonymity;
            return this;
        }

        public Builder revStatus(String revStatus) {
            this.revStatus = revStatus;
            return this;
        }

        public Builder revReplyContent(String revReplyContent) {
            this.revReplyContent = revReplyContent;
            return this;
        }

        public Builder revReplyContentCopie(String revReplyContentCopie) {
            this.revReplyContentCopie = revReplyContentCopie;
            return this;
        }

        public Builder revReplier(String revReplier) {
            this.revReplier = revReplier;
            return this;
        }

        public Builder modifiedUserId(String modifiedUserId) {
            this.modifiedUserId = modifiedUserId;
            return this;
        }

        public Builder revImageList(List<RevImg> revImageList) {
            if(revImageList.size()>0)
                this.revImageList.addAll(revImageList);
            return this;
        }



        public Review build(){
            Review review = new Review();
            review.revId = this.revId;
            review.platformId =this.platformId;
            review.nickname = this.nickname;
            review.chathead = this.chathead;
            review.revPlatform = this.revPlatform;
            review.orderId = this.orderId;
            review.custId = this.custId;
            review.storeId = this.storeId;
            review.revClass = this.revClass;
            review.revTag = this.revTag;
            review.envClass = this.envClass;
            review.prodClass = this.prodClass;
            review.serviceClass = this.serviceClass;
            review.revContent = this.revContent;
            review.revContentCopie = this.revContentCopie;
            review.isAnonymity = this.isAnonymity;
            review.revStatus = this.revStatus;
            review.revReplyContent = this.revReplyContent;
            review.revReplyContentCopie = this.revReplyContentCopie;
            review.revReplier = this.revReplier;
            review.modifiedUserId = this.modifiedUserId;
            if(this.revImageList!=null&&this.revImageList.size()>0)
                review.revImageList.addAll(this.revImageList);
            return review;
        }

    }
}