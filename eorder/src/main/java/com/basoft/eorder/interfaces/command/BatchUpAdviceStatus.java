package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:00 2019/5/20
 **/
public class BatchUpAdviceStatus implements Command {

    public static final String NAME = "batchDelAdviceStatus";

    private List<String> adviIds;

    private String platformId;

    private String status;

    public List<String> getAdviIds() {
        return adviIds;
    }

    public void setAdviIds(List<String> adviIds) {
        this.adviIds = adviIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }
}
