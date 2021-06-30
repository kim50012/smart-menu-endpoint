package com.basoft.eorder.interfaces.query;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:45 2019/2/18
 **/
public class OrderByDateAmountStatsDTO {


    public static final String DATE_TYPE_DAY = "1";//按天统计
    public static final String DATE_TYPE_WEEK = "2";//按周统计
    public static final String DATE_TYPE_MONTH = "3";//按月统计
    private List<String> dateList;

    private List<PayAmountAndDateDTO> payAmountList;

    private List<QtyAndDateDTO> qtyList;

    private BigDecimal payAmountSum;


    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public List<PayAmountAndDateDTO> getPayAmountList() {
        return payAmountList;
    }

    public void setPayAmountList(List<PayAmountAndDateDTO> payAmountList) {
        this.payAmountList = payAmountList;
    }

    public List<QtyAndDateDTO> getQtyList() {
        return qtyList;
    }

    public void setQtyList(List<QtyAndDateDTO> qtyList) {
        this.qtyList = qtyList;
    }

    public BigDecimal getPayAmountSum() {
        return payAmountSum;
    }

    public void setPayAmountSum(BigDecimal payAmountSum) {
        this.payAmountSum = payAmountSum;
    }
}
