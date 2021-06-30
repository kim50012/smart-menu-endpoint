package com.basoft.eorder.interfaces.command.advert;

import com.basoft.eorder.application.framework.Command;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description: 修改广告状态
 * @Date Created in 上午10:37 2019/6/10
 **/
public class BatchUpStatusAdvert implements Command{
    public static final String NAME = "batchUpStatusAdvert";

    private List<String> advIds;

    private String advStatus;

    private String useYn;

    public List<String> getAdvIds() {
        return advIds;
    }

    public void setAdvIds(List<String> advIds) {
        this.advIds = advIds;
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
}
