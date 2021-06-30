package com.basoft.eorder.interfaces.query;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description: 订单总金额统计
 * @Date Created in 上午11:11 2019/2/12
 **/
public class OrderSumStatsDTO {

    private String date;

    private int qty;

    private BigDecimal paySumAmount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getPaySumAmount() {
        return paySumAmount;
    }

    public void setPaySumAmount(BigDecimal paySumAmount) {
        this.paySumAmount = paySumAmount;
    }
}
