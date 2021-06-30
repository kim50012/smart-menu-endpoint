package com.basoft.eorder.interfaces.query.agent;

import com.basoft.eorder.interfaces.query.PaginationDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 9:47 上午 2019/9/27
 **/
public class AgentPagingDTO<T> extends PaginationDTO<T> {

    private BigDecimal sumAmount;//订单总金额
    private BigDecimal agtFee;
    private BigDecimal vatFee;
    private int qty;//订单总数

    public AgentPagingDTO() {
    }

    public AgentPagingDTO(BigDecimal sumAmount,BigDecimal agtFee,BigDecimal vatFee ,int qty,int total, List<T> dataList) {
        super(total,dataList);
        this.sumAmount = sumAmount;
        this.agtFee = agtFee;
        this.vatFee = vatFee;
        this.qty = qty;
    }

    public BigDecimal getAmount() {
        return sumAmount;
    }

    public void setAmount(BigDecimal amount) {
        this.sumAmount = amount;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(BigDecimal sumAmount) {
        this.sumAmount = sumAmount;
    }

    public BigDecimal getVatFee() {
        return vatFee;
    }

    public void setVatFee(BigDecimal vatFee) {
        this.vatFee = vatFee;
    }

    public BigDecimal getAgtFee() {
        return agtFee;
    }

    public void setAgtFee(BigDecimal agtFee) {
        this.agtFee = agtFee;
    }
}
