package com.basoft.eorder.interfaces.query.agent;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 4:32 下午 2019/10/8
 **/
public class AgtAmountQtyDTO {

    private String date;

    private int qty;

    private BigDecimal agtSumAmount;

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getAgtSumAmount() {
        return agtSumAmount;
    }

    public void setAgtSumAmount(BigDecimal agtSumAmount) {
        this.agtSumAmount = agtSumAmount;
    }

}
