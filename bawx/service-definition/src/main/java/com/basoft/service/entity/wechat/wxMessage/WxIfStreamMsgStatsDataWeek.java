package com.basoft.service.entity.wechat.wxMessage;

import java.math.BigDecimal;

public class WxIfStreamMsgStatsDataWeek {

    private String refDate;

    private Byte msgType;

    private Integer msgUser;

    private Integer msgCount;

    private Byte countInterval;

    private BigDecimal average;

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate == null ? null : refDate.trim();
    }

    public Byte getMsgType() {
        return msgType;
    }

    public void setMsgType(Byte msgType) {
        this.msgType = msgType;
    }

    public Integer getMsgUser() {
        return msgUser;
    }

    public void setMsgUser(Integer msgUser) {
        this.msgUser = msgUser;
    }

    public Integer getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(Integer msgCount) {
        this.msgCount = msgCount;
    }

    public Byte getCountInterval() {
        return countInterval;
    }

    public void setCountInterval(Byte countInterval) {
        this.countInterval = countInterval;
    }

    public BigDecimal getAverage() {
        return average;
    }

    public void setAverage(BigDecimal average) {
        this.average = average;
    }
}