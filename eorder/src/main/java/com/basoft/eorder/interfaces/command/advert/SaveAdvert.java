package com.basoft.eorder.interfaces.command.advert;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Advertisement;
import com.basoft.eorder.util.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:58 2019/6/1
 **/
public class SaveAdvert implements Command{

    public static final String NAME = "saveAdvert";

    private String advId;

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

    private List<Advertisement.AdvertDetail> detailsList = new LinkedList<>();

    public String getAdvId() {
        return advId;
    }

    public void setAdvId(String advId) {
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

    public List<Advertisement.AdvertDetail> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<Advertisement.AdvertDetail> detailsList) {
        this.detailsList = detailsList;
    }

    public Advertisement build(){
        return new  Advertisement.Builder()
            .advId(Long.valueOf(advId))
            .advName(advName)
            .advChannel(advChannel)
            .advLocation(advLocation)
            .advType(advType)
            .advContent(advContent)
            .advImageId(advImageId)
            .advImageUrl(advImageUrl)
            .advUrl(advUrl)
            .startTime(startTime)
            .endTime(endTime)
            .advSeconds(advSeconds)
            .advDisplayDays(advDisplayDays)
            .advHeight(advHeight)
            .advWidth(advWidth)
            .advTop(advTop)
            .advLeft(advLeft)
            .advDesc(advDesc)
            .advStatus(advStatus)
            .useYn(useYn)
            .advertImgList(detailsList)
            .build();
    }
}
