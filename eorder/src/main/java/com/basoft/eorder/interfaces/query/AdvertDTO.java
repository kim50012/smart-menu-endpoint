package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.domain.model.AdvAdvertisementDetail;
import com.basoft.eorder.domain.model.Advertisement;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:49 2019/6/10
 **/
public class AdvertDTO {

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
    private List<AdvAdvertisementDetail> detailList = new LinkedList<>();

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
        this.advName = advName;
    }

    public String getAdvChannel() {
        return advChannel;
    }

    public void setAdvChannel(String advChannel) {
        this.advChannel = advChannel;
    }

    public String getAdvLocation() {
        return advLocation;
    }

    public void setAdvLocation(String advLocation) {
        this.advLocation = advLocation;
    }

    public String getAdvType() {
        return advType;
    }

    public void setAdvType(String advType) {
        this.advType = advType;
    }

    public String getAdvContent() {
        return advContent;
    }

    public void setAdvContent(String advContent) {
        this.advContent = advContent;
    }

    public String getAdvImageId() {
        return advImageId;
    }

    public void setAdvImageId(String advImageId) {
        this.advImageId = advImageId;
    }

    public String getAdvImageUrl() {
        return advImageUrl;
    }

    public void setAdvImageUrl(String advImageUrl) {
        this.advImageUrl = advImageUrl;
    }

    public String getAdvVideoId() {
        return advVideoId;
    }

    public void setAdvVideoId(String advVideoId) {
        this.advVideoId = advVideoId;
    }

    public String getAdvVideoUrl() {
        return advVideoUrl;
    }

    public void setAdvVideoUrl(String advVideoUrl) {
        this.advVideoUrl = advVideoUrl;
    }

    public String getAdvUrl() {
        return advUrl;
    }

    public void setAdvUrl(String advUrl) {
        this.advUrl = advUrl;
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
        this.advDesc = advDesc;
    }

    public String getAdvStatus() {
        return advStatus;
    }

    public void setAdvStatus(String advStatus) {
        this.advStatus = advStatus;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
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
        this.createdUserId = createdUserId;
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

    public List<AdvAdvertisementDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<AdvAdvertisementDetail> detailList) {
        this.detailList = detailList;
    }
}
