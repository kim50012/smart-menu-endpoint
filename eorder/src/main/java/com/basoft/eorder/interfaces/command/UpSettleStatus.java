package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:48 2019/3/14
 **/
public class UpSettleStatus  implements Command {
    public static final String NAME="upSettleStatus";
    public static final int CLOSSTATUS_START=0;//等待支付
    public static final int CLOSSTATUS_RECEIVE=1;//已收到钱
    public static final int CLOSSTATUS_DELETE=2;//删除

    private Long storeId;
    private String closingMonths;
    private BigDecimal amount;
    private BigDecimal serviceFee;
    private Long userId;
    private int status;
    private String cashSettleType;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getClosingMonths() {
        return closingMonths;
    }

    public void setClosingMonths(String closingMonths) {
        this.closingMonths = closingMonths;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCashSettleType() {
        return cashSettleType;
    }

    public void setCashSettleType(String cashSettleType) {
        this.cashSettleType = cashSettleType;
    }
}
