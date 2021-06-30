package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.foundation.jdbc.eventhandler.BannerEvent;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:05 2018/12/20
 **/
public class BachUpdateBannerStatus implements Command {
    public static final String NAME_UPSTATUS_LIST = "upbnStatusList";
    public static final String NAME_DELSTATUS_LIST = "delbnStatusList";
    private String status;
    private List<BannerEvent> bannerList;


    public List<BannerEvent> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerEvent> bannerList) {
        this.bannerList = bannerList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


