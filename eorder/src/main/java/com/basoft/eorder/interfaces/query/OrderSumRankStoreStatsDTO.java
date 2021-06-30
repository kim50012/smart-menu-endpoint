package com.basoft.eorder.interfaces.query;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:27 2019/2/14
 **/
public class OrderSumRankStoreStatsDTO {

    private Long storeId;

    private String storeNm;

    private int qty;

    private BigDecimal paySumAmount;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreNm() {
        return storeNm;
    }

    public void setStoreNm(String storeNm) {
        this.storeNm = storeNm;
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
