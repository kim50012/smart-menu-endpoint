package com.basoft.eorder.interfaces.command.review;

import com.basoft.eorder.application.framework.Command;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:19 2019/5/24
 **/
public class BatchUpRevImgStatus implements Command {
    public static final String NAME = "batchUpRevImgStatus";

    private List<String> revAttachId ;

    private String isDisplay ;


    public List<String> getRevAttachId() {
        return revAttachId;
    }

    public void setRevAttachId(List<String> revAttachId) {
        this.revAttachId = revAttachId;
    }

    public String getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(String isDisplay) {
        this.isDisplay = isDisplay;
    }
}
