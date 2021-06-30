package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.domain.model.StrAdviceAttach;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:46 2019/6/4
 **/
public class AdviceDTO {
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

    private List<StrAdviceAttach> imgList = new LinkedList<>();


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

    public String getAdviTos() {
        return adviTos;
    }

    public void setAdviTos(String adviTos) {
        this.adviTos = adviTos;
    }

    public String getAdviType() {
        return adviType;
    }

    public void setAdviType(String adviType) {
        this.adviType = adviType;
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
        this.linker = linker;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getLinkEmail() {
        return linkEmail;
    }

    public void setLinkEmail(String linkEmail) {
        this.linkEmail = linkEmail;
    }

    public String getAdviStatus() {
        return adviStatus;
    }

    public void setAdviStatus(String adviStatus) {
        this.adviStatus = adviStatus;
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
        this.adviReplier = adviReplier;
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
        this.modifiedUserId = modifiedUserId;
    }

    public List<StrAdviceAttach> getImgList() {
        return imgList;
    }

    public void setImgList(List<StrAdviceAttach> imgList) {
        this.imgList = imgList;
    }
}
