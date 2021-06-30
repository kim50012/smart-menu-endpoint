package com.basoft.eorder.domain.model.order;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

/**
 * 商戶月度订单结算
 *
 * @author Mentor
 * @version 1.0
 * @since 20190828
 */
public class StoreSettlement {
    @JsonSerialize
    private Long sid;

    @JsonSerialize
    private Long storeID;

    private String settleYearMonth;

    private int settleYear;

    private int settleMonth;

    private String startDT;

    private String endDT;

    private int settleType;

    private double settleRate;

    private double settleFee;

    private Long orderCount;

    private BigDecimal settleSum;

    private String pgDate;

    private BigDecimal pgSum;

    private BigDecimal pgServiceFee;

    private String plDate;

    private BigDecimal plMinFee;

    private BigDecimal plServiceFee;

    private BigDecimal plFinalFee;

    private String createTime;

    private String createUser;

    private String updateTime;

    private String updateUser;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Long getStoreID() {
        return storeID;
    }

    public void setStoreID(Long storeID) {
        this.storeID = storeID;
    }

    public String getSettleYearMonth() {
        return settleYearMonth;
    }

    public void setSettleYearMonth(String settleYearMonth) {
        this.settleYearMonth = settleYearMonth;
    }

    public int getSettleYear() {
        return settleYear;
    }

    public void setSettleYear(int settleYear) {
        this.settleYear = settleYear;
    }

    public int getSettleMonth() {
        return settleMonth;
    }

    public void setSettleMonth(int settleMonth) {
        this.settleMonth = settleMonth;
    }

    public String getStartDT() {
        return startDT;
    }

    public void setStartDT(String startDT) {
        this.startDT = startDT;
    }

    public String getEndDT() {
        return endDT;
    }

    public void setEndDT(String endDT) {
        this.endDT = endDT;
    }

    public int getSettleType() {
        return settleType;
    }

    public void setSettleType(int settleType) {
        this.settleType = settleType;
    }

    public double getSettleRate() {
        return settleRate;
    }

    public void setSettleRate(double settleRate) {
        this.settleRate = settleRate;
    }

    public double getSettleFee() {
        return settleFee;
    }

    public void setSettleFee(double settleFee) {
        this.settleFee = settleFee;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getSettleSum() {
        return settleSum;
    }

    public void setSettleSum(BigDecimal settleSum) {
        this.settleSum = settleSum;
    }

    public String getPgDate() {
        return pgDate;
    }

    public void setPgDate(String pgDate) {
        this.pgDate = pgDate;
    }

    public BigDecimal getPgSum() {
        return pgSum;
    }

    public void setPgSum(BigDecimal pgSum) {
        this.pgSum = pgSum;
    }

    public BigDecimal getPgServiceFee() {
        return pgServiceFee;
    }

    public void setPgServiceFee(BigDecimal pgServiceFee) {
        this.pgServiceFee = pgServiceFee;
    }

    public String getPlDate() {
        return plDate;
    }

    public void setPlDate(String plDate) {
        this.plDate = plDate;
    }

    public BigDecimal getPlMinFee() {
        return plMinFee;
    }

    public void setPlMinFee(BigDecimal plMinFee) {
        this.plMinFee = plMinFee;
    }

    public BigDecimal getPlServiceFee() {
        return plServiceFee;
    }

    public void setPlServiceFee(BigDecimal plServiceFee) {
        this.plServiceFee = plServiceFee;
    }

    public BigDecimal getPlFinalFee() {
        return plFinalFee;
    }

    public void setPlFinalFee(BigDecimal plFinalFee) {
        this.plFinalFee = plFinalFee;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public static final class Builder {
        private Long sid;

        private Long storeID;

        private String settleYearMonth;

        private int settleYear;

        private int settleMonth;

        private String startDT;

        private String endDT;

        private int settleType;

        private double settleRate;

        private double settleFee;

        private Long orderCount;

        private BigDecimal settleSum;

        private String pgDate;

        private BigDecimal pgSum;

        private BigDecimal pgServiceFee;

        private String plDate;

        private BigDecimal plMinFee;

        private BigDecimal plServiceFee;

        private BigDecimal plFinalFee;

        private String createTime;

        private String createUser;

        private String updateTime;

        private String updateUser;

        public Builder sid(Long sid) {
            this.sid = sid;
            return this;
        }

        public Builder storeID(Long storeID) {
            this.storeID = storeID;
            return this;
        }

        public Builder settleYearMonth(String settleYearMonth) {
            this.settleYearMonth = settleYearMonth;
            return this;
        }

        public Builder settleYear(int settleYear) {
            this.settleYear = settleYear;
            return this;
        }

        public Builder settleMonth(int settleMonth) {
            this.settleMonth = settleMonth;
            return this;
        }

        public Builder startDT(String startDT) {
            this.startDT = startDT;
            return this;
        }

        public Builder endDT(String endDT) {
            this.endDT = endDT;
            return this;
        }

        public Builder settleType(int settleType) {
            this.settleType = settleType;
            return this;
        }

        public Builder settleRate(double settleRate) {
            this.settleRate = settleRate;
            return this;
        }

        public Builder settleFee(double settleFee) {
            this.settleFee = settleFee;
            return this;
        }

        public Builder orderCount(Long orderCount) {
            this.orderCount = orderCount;
            return this;
        }

        public Builder settleSum(BigDecimal settleSum) {
            this.settleSum = settleSum;
            return this;
        }

        public Builder pgDate(String pgDate) {
            this.pgDate = pgDate;
            return this;
        }

        public Builder plMinFee(BigDecimal plMinFee) {
            this.plMinFee = plMinFee;
            return this;
        }

        public Builder plServiceFee(BigDecimal plServiceFee) {
            this.plServiceFee = plServiceFee;
            return this;
        }

        public Builder plDate(String plDate) {
            this.plDate = plDate;
            return this;
        }

        public Builder pgSum(BigDecimal pgSum) {
            this.pgSum = pgSum;
            return this;
        }

        public Builder pgServiceFee(BigDecimal pgServiceFee) {
            this.pgServiceFee = pgServiceFee;
            return this;
        }

        public Builder plFinalFee(BigDecimal plFinalFee) {
            this.plFinalFee = plFinalFee;
            return this;
        }

        public Builder createTime(String createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder createUser(String createUser) {
            this.createUser = createUser;
            return this;
        }

        public Builder updateTime(String updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder updateUser(String updateUser) {
            this.updateUser = updateUser;
            return this;
        }

        public StoreSettlement build() {
            StoreSettlement storeSettlement = new StoreSettlement();
            storeSettlement.sid = this.sid;
            storeSettlement.storeID = this.storeID;
            storeSettlement.settleYearMonth = this.settleYearMonth;
            storeSettlement.settleYear = this.settleYear;
            storeSettlement.settleMonth = this.settleMonth;
            storeSettlement.startDT = this.startDT;
            storeSettlement.endDT = this.endDT;
            storeSettlement.settleType = this.settleType;
            storeSettlement.settleRate = this.settleRate;
            storeSettlement.settleFee = this.settleFee;
            storeSettlement.orderCount = this.orderCount;
            storeSettlement.settleSum = this.settleSum;
            storeSettlement.pgDate = this.pgDate;
            storeSettlement.pgSum = this.pgSum;
            storeSettlement.pgServiceFee = this.pgServiceFee;
            storeSettlement.plDate = this.plDate;
            storeSettlement.plMinFee = this.plMinFee;
            storeSettlement.plServiceFee = this.plServiceFee;
            storeSettlement.plFinalFee = this.plFinalFee;
            storeSettlement.createTime = this.createTime;
            storeSettlement.createUser = this.createUser;
            storeSettlement.updateTime = this.updateTime;
            storeSettlement.updateUser = this.updateUser;
            return storeSettlement;
        }
    }

    @Override
    public String toString() {
        return "StoreSettlement{" +
                "sid=" + sid +
                ", storeID=" + storeID +
                ", settleYearMonth='" + settleYearMonth + '\'' +
                ", settleYear=" + settleYear +
                ", settleMonth=" + settleMonth +
                ", startDT='" + startDT + '\'' +
                ", endDT='" + endDT + '\'' +
                ", settleType=" + settleType +
                ", settleRate=" + settleRate +
                ", settleFee=" + settleFee +
                ", orderCount=" + orderCount +
                ", settleSum=" + settleSum +
                ", pgDate='" + pgDate + '\'' +
                ", pgSum=" + pgSum +
                ", pgServiceFee=" + pgServiceFee +
                ", plDate='" + plDate + '\'' +
                ", plMinFee=" + plMinFee +
                ", plServiceFee=" + plServiceFee +
                ", plFinalFee=" + plFinalFee +
                ", createTime='" + createTime + '\'' +
                ", createUser='" + createUser + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", updateUser='" + updateUser + '\'' +
                '}';
    }
}