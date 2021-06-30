package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:48 2019/5/14
 **/
public class BatchUpReviewStatus implements Command {
    public static final String NAME = "batchUpReviewStatus";
    private List<String> revIds;

    private String platformId;

    private String revStatus;

    public List<String> getRevIds() {
        return revIds;
    }

    public void setRevIds(List<String> revIds) {
        this.revIds = revIds;
    }

    public String getRevStatus() {
        return revStatus;
    }

    public void setRevStatus(String revStatus) {
        this.revStatus = revStatus;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }
}
