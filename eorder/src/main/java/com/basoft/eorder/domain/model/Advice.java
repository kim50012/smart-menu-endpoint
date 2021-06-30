package com.basoft.eorder.domain.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Advice {

    public static final String ADVICE_STATUS_START="1";
    public static final String ADVICE_STATUS_RESOLVED="2";
    public static final String ADVICE_STATUS_DEL="3";

    private Long adviId;

    private String platformId;//平台id

    private String nickname;//平台用户昵称。取自第三方平台，如微信、支付宝

    private String chathead;//平台用户头像URL。开发时，建议实施获取

    private String revPlatform;//第三方平台。WE-微信平台 ALI-阿里支付宝

    private String adviTos;//投诉和建议的业务类型ToS(type of service) 。PLAT-平台；EPOS-电子点餐系统，Electronic Point-of-Sale EPOS；HOTEL-酒店业务；HOSP-医美。

    private String adviType;//类型。TS-投诉；TW-提问；JY-建议；BY-表扬

    private Long custId;//投诉和建议主体（平台、饭店、酒店、医院）所属客户ID。如果主体是平台，该字段为空。

    private Long storeId;//投诉和建议主体ADVICE SUBJECT（平台、饭店、酒店、医院）。如果主体是平台，该字段为空，其他主体存储STORE ID。

    private String storeName;

    private String adviContent;//投诉或建议的内容。文字描述，图片和视频存入投诉建议附件表。

    private Date adviTime;

    private String linker;//联系人

    private String linkPhone;//联系电话

    private String linkEmail;//联系电子邮箱

    private String adviStatus;//投诉或建议的处理状态。1-新投诉或建议；2-已处理，已答复或者已经通过电话或电子邮件联系过投诉建议人；3-隐藏状态（即删除状态），投诉因为特殊（反动等）原因需要删除隐藏。

    private String adviReplyContent;//答复内容，不支持图片或视频答复，只支持文字描述。

    private String adviReplier;//答复人

    private Date adviReplyTime;//答复时间

    private Date modifiedDt;

    private String modifiedUserId;

    private List<AdviceImg> adviceImgList = new LinkedList<>();

    public Long getAdviId() {
        return adviId;
    }

    public void setAdviId(Long adviId) {
        this.adviId = adviId;
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

    public String getAdviTos() {
        return adviTos;
    }

    public void setAdviTos(String adviTos) {
        this.adviTos = adviTos == null ? null : adviTos.trim();
    }

    public String getAdviType() {
        return adviType;
    }

    public void setAdviType(String adviType) {
        this.adviType = adviType == null ? null : adviType.trim();
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAdviContent() {
        return adviContent;
    }

    public void setAdviContent(String adviContent) {
        this.adviContent = adviContent;
    }

    public Date getAdviTime() {
        return adviTime;
    }

    public void setAdviTime(Date adviTime) {
        this.adviTime = adviTime;
    }

    public String getLinker() {
        return linker;
    }

    public void setLinker(String linker) {
        this.linker = linker == null ? null : linker.trim();
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone == null ? null : linkPhone.trim();
    }

    public String getLinkEmail() {
        return linkEmail;
    }

    public void setLinkEmail(String linkEmail) {
        this.linkEmail = linkEmail == null ? null : linkEmail.trim();
    }

    public String getAdviStatus() {
        return adviStatus;
    }

    public void setAdviStatus(String adviStatus) {
        this.adviStatus = adviStatus == null ? null : adviStatus.trim();
    }

    public String getAdviReplyContent() {
        return adviReplyContent;
    }

    public void setAdviReplyContent(String adviReplyContent) {
        this.adviReplyContent = adviReplyContent;
    }

    public String getAdviReplier() {
        return adviReplier;
    }

    public void setAdviReplier(String adviReplier) {
        this.adviReplier = adviReplier == null ? null : adviReplier.trim();
    }

    public Date getAdviReplyTime() {
        return adviReplyTime;
    }

    public void setAdviReplyTime(Date adviReplyTime) {
        this.adviReplyTime = adviReplyTime;
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

    public List<AdviceImg> getAdviceImgList() {
        return adviceImgList;
    }

    public void setAdviceImgList(List<AdviceImg> adviceImgList) {
        this.adviceImgList = adviceImgList;
    }

    public static class AdviceImg {

        public static final String ADVICEIMG_SHOW = "1";
        public static final String ADVICEIMG_HIDE = "0";

        private Long adviAttachId;

        private Long adviId;

        private String contentUrl;

        private int displayOrder;

        private int isDisplay;

        public AdviceImg(){}

        public Long getAdviAttachId() {
            return adviAttachId;
        }

        public void setAdviAttachId(Long adviAttachId) {
            this.adviAttachId = adviAttachId;
        }

        public Long getAdviId() {
            return adviId;
        }

        public void setAdviId(Long adviId) {
            this.adviId = adviId;
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

        public int getIsDisplay() {
            return isDisplay;
        }

        public void setIsDisplay(int isDisplay) {
            this.isDisplay = isDisplay;
        }
    }


    public static final class Builder{

        private Long adviId;

        private String platformId;//平台id

        private String nickname;//平台用户昵称。取自第三方平台，如微信、支付宝

        private String chathead;//平台用户头像URL。开发时，建议实施获取

        private String revPlatform;//第三方平台。WE-微信平台 ALI-阿里支付宝

        private String adviTos;//投诉和建议的业务类型ToS(type of service) 。PLAT-平台；EPOS-电子点餐系统，Electronic Point-of-Sale EPOS；HOTEL-酒店业务；HOSP-医美。

        private String adviType;//类型。TS-投诉；TW-提问；JY-建议；BY-表扬

        private Long custId;//投诉和建议主体（平台、饭店、酒店、医院）所属客户ID。如果主体是平台，该字段为空。

        private Long storeId;//投诉和建议主体ADVICE SUBJECT（平台、饭店、酒店、医院）。如果主体是平台，该字段为空，其他主体存储STORE ID。

        private String adviContent;//投诉或建议的内容。文字描述，图片和视频存入投诉建议附件表。

        private String linker;//联系人

        private String linkPhone;//联系电话

        private String linkEmail;//联系电子邮箱

        private String adviStatus;//投诉或建议的处理状态。1-新投诉或建议；2-已处理，已答复或者已经通过电话或电子邮件联系过投诉建议人；3-隐藏状态（即删除状态），投诉因为特殊（反动等）原因需要删除隐藏。

        private String adviReplyContent;//答复内容，不支持图片或视频答复，只支持文字描述。

        private String adviReplier;//答复人

        private List<AdviceImg> adviceImgList = new LinkedList<>();

        private Date adviReplyTime;//答复时间

        private Date modifiedDt;

        private String modifiedUserId;

        public Builder advId(Long adviId){
            this.adviId =adviId;
            return this;
        }
        public Builder platformId(String platformId){
            this.platformId =platformId;
            return this;
        }
        public Builder nickname(String nickname){
            this.nickname =nickname;
            return this;
        }
        public Builder chathead(String chathead){
            this.chathead =chathead;
            return this;
        }
        public Builder revPlatform(String revPlatform){
            this.revPlatform =revPlatform;
            return this;
        }
        public Builder adviTos(String adviTos){
            this.adviTos =adviTos;
            return this;
        }

        public Builder adviType(String adviType){
            this.adviType =adviType;
            return this;
        }
        public Builder custId(Long custId){
            this.custId =custId;
            return this;
        }
        public Builder storeId(Long storeId){
            this.storeId =storeId;
            return this;
        }
        public Builder adviContent(String adviContent){
            this.adviContent =adviContent;
            return this;
        }
        public Builder linker(String linker){
            this.linker =linker;
            return this;
        }
        public Builder linkPhone(String linkPhone){
            this.linkPhone =linkPhone;
            return this;
        }
        public Builder linkEmail(String linkEmail){
            this.linkEmail =linkEmail;
            return this;
        }
        public Builder adviStatus(String adviStatus){
            this.adviStatus =adviStatus;
            return this;
        }
        public Builder adviReplyContent(String adviReplyContent){
            this.adviReplyContent =adviReplyContent;
            return this;
        }
        public Builder adviReplier(String adviReplier){
            this.adviReplier =adviReplier;
            return this;
        }
        public Builder modifiedUserId(String modifiedUserId){
            this.modifiedUserId =modifiedUserId;
            return this;
        }

        public Builder adviceImgList(List<AdviceImg> adviceImgList) {
            if(adviceImgList.size()>0)
                this.adviceImgList.addAll(adviceImgList);
            return this;
        }


        public Advice build(){
            Advice advice = new Advice();
            advice.adviId = this.adviId;
            advice.platformId = this.platformId;
            advice.nickname = this.nickname;
            advice.chathead = this.chathead;
            advice.revPlatform = this.revPlatform;
            advice.adviTos = this.adviTos;
            advice.adviType = this.adviType;
            advice.custId = this.custId;
            advice.storeId = this.storeId;
            advice.adviContent = this.adviContent;
            advice.linker = this.linker;
            advice.linkPhone = this.linkPhone;
            advice.linkEmail = this.linkEmail;
            advice.adviStatus = this.adviStatus;
            advice.adviReplyContent = this.adviReplyContent;
            advice.adviReplier = this.adviReplier;
            advice.adviceImgList = this.adviceImgList;
            return advice;
        }


    }



}