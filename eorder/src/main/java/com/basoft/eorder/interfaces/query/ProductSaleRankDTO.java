package com.basoft.eorder.interfaces.query;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:37 2019/2/12
 **/
public class ProductSaleRankDTO {
    private Long productId;
    private String nameChn;
    private String nameKor;
    private int qty;
    private BigDecimal price;
    private BigDecimal paySumAmount;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPaySumAmount() {
        return paySumAmount;
    }

    public void setPaySumAmount(BigDecimal paySumAmount) {
        this.paySumAmount = paySumAmount;
    }
}
