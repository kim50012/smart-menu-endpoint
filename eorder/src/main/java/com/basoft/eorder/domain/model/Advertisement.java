package com.basoft.eorder.domain.model;

import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.interfaces.query.OrderItemDTO;
import com.basoft.eorder.util.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 广告
 */
public class Advertisement {

    public static final String START = "1";
    public static final String UPSHELF = "2";
    public static final String LOWSHELF = "3";
    public static final String ADVERT_DEL = "0";

    private Long advId;

    private String advName;//广告名称

    private String advChannel;//推广渠道。ALL-全渠道；WE-微信；ALI-阿里

    private String advLocation;//广告位置。INDEX-首页；ORDER_FINISH-订单支付成功页

    private String advType;//广告类型。图片广告-NORMAL_IMG；POP_IMG-弹出图片广告；ROLL_IMG-滚动图片广告；LOAD_IMG-加载显示图片广告；VIDEO-视频广告；TEXT-文本广告

    private String advContent;//文本广告的文本内容

    private String advImageId;//图片存储ID

    private String advImageUrl;//图片地址。以下类型广告使用该字段：POP_IMG-弹出图片广告；LOAD_IMG-加载显示图片广告

    private String advVideoId;//视频存储ID

    private String advVideoUrl;//视频地址

    private String advUrl;//业务超链接

    private Date startTime;//起始时间

    private Date endTime;//结束时间

    private Short advSeconds;//显示倒计时，以秒为单位

    private Short advDisplayDays;//显示天数

    private String advHeight;//窗口尺寸-高度

    private String advWidth;//窗口尺寸-宽度

    private String advTop;//显示距上距离

    private String advLeft;//显示距左距离

    private String advDesc;//广告备注

    private String advStatus;//广告状态。1-新建 2-发布 3-下架

    private String useYn;//可用状态。0-不可用 1-可用

    private Date createdDt;

    private String createdUserId;

    private Date modifiedDt;

    private String modifiedUserId;

    private List<AdvertDetail> detailsList = new LinkedList<>();

    public Long getAdvId() {
        return advId;
    }

    public void setAdvId(Long advId) {
        this.advId = advId;
    }

    public String getAdvName() {
        return advName;
    }

    public void setAdvName(String advName) {
        this.advName = advName == null ? null : advName.trim();
    }

    public String getAdvChannel() {
        return advChannel;
    }

    public void setAdvChannel(String advChannel) {
        this.advChannel = advChannel == null ? null : advChannel.trim();
    }

    public String getAdvLocation() {
        return advLocation;
    }

    public void setAdvLocation(String advLocation) {
        this.advLocation = advLocation == null ? null : advLocation.trim();
    }

    public String getAdvType() {
        return advType;
    }

    public void setAdvType(String advType) {
        this.advType = advType == null ? null : advType.trim();
    }

    public String getAdvContent() {
        return advContent;
    }

    public void setAdvContent(String advContent) {
        this.advContent = advContent == null ? null : advContent.trim();
    }

    public String getAdvImageId() {
        return advImageId;
    }

    public void setAdvImageId(String advImageId) {
        this.advImageId = advImageId == null ? null : advImageId.trim();
    }

    public String getAdvImageUrl() {
        return advImageUrl;
    }

    public void setAdvImageUrl(String advImageUrl) {
        this.advImageUrl = advImageUrl == null ? null : advImageUrl.trim();
    }

    public String getAdvVideoId() {
        return advVideoId;
    }

    public void setAdvVideoId(String advVideoId) {
        this.advVideoId = advVideoId == null ? null : advVideoId.trim();
    }

    public String getAdvVideoUrl() {
        return advVideoUrl;
    }

    public void setAdvVideoUrl(String advVideoUrl) {
        this.advVideoUrl = advVideoUrl == null ? null : advVideoUrl.trim();
    }

    public String getAdvUrl() {
        return advUrl;
    }

    public void setAdvUrl(String advUrl) {
        this.advUrl = advUrl == null ? null : advUrl.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Short getAdvSeconds() {
        return advSeconds;
    }

    public void setAdvSeconds(Short advSeconds) {
        this.advSeconds = advSeconds;
    }

    public Short getAdvDisplayDays() {
        return advDisplayDays;
    }

    public void setAdvDisplayDays(Short advDisplayDays) {
        this.advDisplayDays = advDisplayDays;
    }

    public String getAdvHeight() {
        return advHeight;
    }

    public void setAdvHeight(String advHeight) {
        this.advHeight = advHeight;
    }

    public String getAdvWidth() {
        return advWidth;
    }

    public void setAdvWidth(String advWidth) {
        this.advWidth = advWidth;
    }

    public String getAdvTop() {
        return advTop;
    }

    public void setAdvTop(String advTop) {
        this.advTop = advTop;
    }

    public String getAdvLeft() {
        return advLeft;
    }

    public void setAdvLeft(String advLeft) {
        this.advLeft = advLeft;
    }

    public String getAdvDesc() {
        return advDesc;
    }

    public void setAdvDesc(String advDesc) {
        this.advDesc = advDesc == null ? null : advDesc.trim();
    }

    public String getAdvStatus() {
        return advStatus;
    }

    public void setAdvStatus(String advStatus) {
        this.advStatus = advStatus == null ? null : advStatus.trim();
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn == null ? null : useYn.trim();
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId == null ? null : createdUserId.trim();
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

    public List<AdvertDetail> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<AdvertDetail> detailsList) {
        this.detailsList = detailsList;
    }

    public static class AdvertDetail {

        private Long advDetId;

        private Long advId;

        private String contentName;

        private String contentUrl;

        private String targetUrl;

        private int advDetOrder;

        private String useYn;

        public Long getAdvDetId() {
            return advDetId;
        }

        public void setAdvDetId(Long advDetId) {
            this.advDetId = advDetId;
        }

        public Long getAdvId() {
            return advId;
        }

        public void setAdvId(Long advId) {
            this.advId = advId;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getContentName() {
            return contentName;
        }

        public void setContentName(String contentName) {
            this.contentName = contentName;
        }

        public String getTargetUrl() {
            return targetUrl;
        }

        public void setTargetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
        }

        public int getAdvDetOrder() {
            return advDetOrder;
        }

        public void setAdvDetOrder(int advDetOrder) {
            this.advDetOrder = advDetOrder;
        }

        public String getUseYn() {
            return useYn;
        }

        public void setUseYn(String useYn) {
            this.useYn = useYn;
        }
    }

    public static final class Builder {
        private Long advId;

        private String advName;//广告名称

        private String advChannel;//推广渠道。ALL-全渠道；WE-微信；ALI-阿里

        private String advLocation;//广告位置。INDEX-首页；ORDER_FINISH-订单支付成功页

        private String advType;//广告类型。图片广告-NORMAL_IMG；POP_IMG-弹出图片广告；ROLL_IMG-滚动图片广告；LOAD_IMG-加载显示图片广告；VIDEO-视频广告；TEXT-文本广告

        private String advContent;//文本广告的文本内容

        private String advImageId;//图片存储ID

        private String advImageUrl;//图片地址。以下类型广告使用该字段：POP_IMG-弹出图片广告；LOAD_IMG-加载显示图片广告

        private String advVideoId;//视频存储ID

        private String advVideoUrl;//视频地址

        private String advUrl;//业务超链接

        private Date startTime;//起始时间

        private Date endTime;//结束时间

        private Short advSeconds;//显示倒计时，以秒为单位

        private Short advDisplayDays;//显示天数

        private String advHeight;//窗口尺寸-高度

        private String advWidth;//窗口尺寸-宽度

        private String advTop;//显示距上距离

        private String advLeft;//显示距左距离

        private String advDesc;//广告备注

        private String advStatus;//广告状态。1-新建 2-发布 3-下架

        private String useYn;//可用状态。0-不可用 1-可用

        private List<AdvertDetail> advertImgList = new LinkedList<>();

        public Builder advId(Long advId){
            this.advId = advId;
            return this;
        }
        public Builder advName(String advName){
            this.advName = advName;
            return this;
        }
        public Builder advChannel(String advChannel){
            this.advChannel = advChannel;
            return this;
        }
        public Builder advLocation(String advLocation){
            this.advLocation = advLocation;
            return this;
        }
        public Builder advType(String advType){
            this.advType = advType;
            return this;
        }
        public Builder advContent(String advContent){
            this.advContent = advContent;
            return this;
        }
        public Builder advImageId(String advImageId){
            this.advImageId = advImageId;
            return this;
        }
        public Builder advImageUrl(String advImageUrl){
            this.advImageUrl = advImageUrl;
            return this;
        }
        public Builder advVideoId(String advVideoId){
            this.advVideoId = advVideoId;
            return this;
        }
        public Builder advVideoUrl(String advVideoUrl){
            this.advVideoUrl = advVideoUrl;
            return this;
        }

        public Builder advUrl(String advUrl) {
            this.advUrl = advUrl;
            return  this;
        }
        public Builder startTime(Date startTime){
            this.startTime = startTime;
            return this;
        }
        public Builder endTime(Date endTime){
            this.endTime = endTime;
            return this;
        }
        public Builder advSeconds(short advSeconds){
            this.advSeconds = advSeconds;
            return this;
        }
        public Builder advDisplayDays(Short advDisplayDays){
            this.advDisplayDays = advDisplayDays;
            return this;
        }
        public Builder advHeight(String advHeight){
            this.advHeight = advHeight;
            return this;
        }
        public Builder advWidth(String advWidth){
            this.advWidth = advWidth;
            return this;
        }
        public Builder advTop(String advTop){
            this.advTop = advTop;
            return this;
        }
        public Builder advLeft(String advLeft){
            this.advLeft = advLeft;
            return this;
        }
        public Builder advDesc(String advDesc){
            this.advDesc = advDesc;
            return this;
        }
        public Builder advStatus(String advStatus){
            this.advStatus = advStatus;
            return this;
        }
        public Builder useYn(String useYn){
            this.useYn = useYn;
            return this;
        }

        public Builder advertImgList(List<AdvertDetail> advertImgList) {
            if(advertImgList.size()>0){
                this.advertImgList.addAll(advertImgList);
            }
            return this;
        }


        public Advertisement build() {
            Advertisement adv = new Advertisement();
            adv.advId = this.advId;
            adv.advName = this.advName;
            adv.advChannel = this.advChannel;
            adv.advLocation = this.advLocation;
            adv.advType = this.advType;
            adv.advContent = this.advContent;
            adv.advImageId = this.advImageId;
            adv.advImageUrl = this.advImageUrl;
            adv.advVideoId = this.advVideoId;
            adv.advVideoUrl = this.advVideoUrl;
            adv.advUrl = this.advUrl;
            adv.startTime = this.startTime;
            adv.endTime = this.endTime;
            adv.advSeconds = this.advSeconds;
            adv.advDisplayDays = this.advDisplayDays;
            adv.advHeight = this.advHeight;
            adv.advWidth = this.advWidth;
            adv.advTop = this.advTop;
            adv.advLeft = this.advLeft;
            adv.advDesc = this.advDesc;
            adv.advStatus = this.advStatus;
            adv.useYn = this.useYn;
            adv.detailsList = this.advertImgList;
            return adv;
        }
    }
}