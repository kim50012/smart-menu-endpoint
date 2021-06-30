package com.basoft.eorder.interfaces.query.plAgt;


import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 11:13 上午 2019/10/28
 **/

@Data
public class PlAgtAmountDTO {
    public static final String PL_FEE = "1";
    public static final String SA_FEE = "2";
    public static final String CA_FEE = "3";
    public static final String PL_PROFIT = "4";

    public PlAgtAmountDTO() {

    }

    public PlAgtAmountDTO(String date, BigDecimal plFee, BigDecimal saFee, BigDecimal caFee, BigDecimal profit) {
        this.date = date;
        this.plFee = plFee;
        this.saFee = saFee;
        this.caFee = caFee;
        this.profit = profit;
    }
    private String date;
    private BigDecimal plFee;
    private BigDecimal saFee;
    private BigDecimal caFee;
    private BigDecimal profit;
    private String aimType;
    private String dateType;

    private BigDecimal amount;
    private int qty;


}
