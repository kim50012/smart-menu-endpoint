package com.basoft.eorder.interfaces.query.agent;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 4:07 下午 2019/10/8
 **/
public class AgentOrderStaticsDTO {
    public static final String DATE_TYPE_DAY = "1";//按天统计
    public static final String DATE_TYPE_WEEK = "2";//按周统计
    public static final String DATE_TYPE_MONTH = "3";//按月统计
    private List<String> dateList;

    private List<AmountDate> agtAmountList;

    private List<QtyDate> qtyList;

    private BigDecimal agtSumAmount;

    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public List<AmountDate> getAgtAmountList() {
        return agtAmountList;
    }

    public void setAgtAmountList(List<AmountDate> agtAmountList) {
        this.agtAmountList = agtAmountList;
    }

    public List<QtyDate> getQtyList() {
        return qtyList;
    }

    public void setQtyList(List<QtyDate> qtyList) {
        this.qtyList = qtyList;
    }

    public BigDecimal getAgtSumAmount() {
        return agtSumAmount;
    }

    public void setAgtSumAmount(BigDecimal agtSumAmount) {
        this.agtSumAmount = agtSumAmount;
    }

    public static  class AmountDate {
        private BigDecimal agtSumAmount;
        private String date;

        public BigDecimal getAgtSumAmount() {
            return agtSumAmount;
        }

        public void setAgtSumAmount(BigDecimal agtSumAmount) {
            this.agtSumAmount = agtSumAmount;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    public static class QtyDate {
        private int qty;
        private String date;

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
    }


}
