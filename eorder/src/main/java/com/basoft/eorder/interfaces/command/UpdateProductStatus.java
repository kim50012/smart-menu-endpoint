package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:11 2019/1/7
 **/
public class UpdateProductStatus implements Command{
    public static final String NAME = "updateProductStatus";
    private List<Long> productIds;

    private int status;

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
