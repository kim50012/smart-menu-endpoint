package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:08 2019/1/7
 **/
public class UpdateUserStatus implements Command {
    public static final String NAME = "updateUserStatus";
    private List<Long> userIds;
    private int status;

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
