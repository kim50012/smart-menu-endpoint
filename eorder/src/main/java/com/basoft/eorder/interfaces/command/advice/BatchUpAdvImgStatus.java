package com.basoft.eorder.interfaces.command.advice;

import com.basoft.eorder.application.framework.Command;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午11:33 2019/6/4
 **/
public class BatchUpAdvImgStatus implements Command{
    public static final String NAME = "batchUpAdviceImgStatus";

    private List<String> adviAttachId;

    private String isDisplay;

    public List<String> getAdviAttachId() {
        return adviAttachId;
    }

    public void setAdviAttachId(List<String> adviAttachId) {
        this.adviAttachId = adviAttachId;
    }

    public String getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(String isDisplay) {
        this.isDisplay = isDisplay;
    }
}
