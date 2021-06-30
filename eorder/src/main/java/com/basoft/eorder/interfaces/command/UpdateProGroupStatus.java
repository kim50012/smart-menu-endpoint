package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:42 2019/1/7
 **/
public class UpdateProGroupStatus implements Command{

    public static final String NAME = "updateProGroupStatus";


    private List<Long> productGroupIds;

    private int status;

    public List<Long> getProductGroupIds() {
        return productGroupIds;
    }

    public void setProductGroupIds(List<Long> productGroupIds) {
        this.productGroupIds = productGroupIds;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
