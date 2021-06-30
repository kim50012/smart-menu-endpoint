package com.basoft.eorder.interfaces.query.plAgt;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 1:32 下午 2019/11/12
 **/
@Data
public class PlAgtFeeDTO {
    public static final String PL_FEE = "1";
    public static final String SA_FEE = "2";
    public static final String CA_FEE = "3";
    public static final String PL_PROFIT = "4";

    public PlAgtFeeDTO() {

    }

    public  PlAgtFeeDTO(String date, BigDecimal fee) {
        this.date = date;
        this.fee = fee;
    }

    private String aimType;

    private BigDecimal fee;

    private String date;
}
