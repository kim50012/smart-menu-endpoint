package com.basoft.eorder.interfaces.query;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description: 分类销量排行
 * @Date Created in 下午1:52 2019/2/14
 **/
public class CategorySaleRankDTO {

    private Long categoryId;
    private String nameChn;
    private String nameKor;
    private int qty;
    private BigDecimal paySumAmount;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getNameChn() {
        return nameChn;
    }

    public void setNameChn(String nameChn) {
        this.nameChn = nameChn;
    }

    public String getNameKor() {
        return nameKor;
    }

    public void setNameKor(String nameKor) {
        this.nameKor = nameKor;
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
