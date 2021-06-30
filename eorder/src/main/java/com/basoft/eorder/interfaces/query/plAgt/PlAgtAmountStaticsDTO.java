package com.basoft.eorder.interfaces.query.plAgt;

import lombok.Data;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 11:27 上午 2019/10/28
 **/
@Data
public class PlAgtAmountStaticsDTO {
    public static final String DATE_TYPE_DAY = "1";//按天统计
    public static final String DATE_TYPE_WEEK = "2";//按周统计
    public static final String DATE_TYPE_MONTH = "3";//按月统计


    private List<PlAgtFeeDTO> plFeeList;
    private List<PlAgtFeeDTO> saFeeList;
    private List<PlAgtFeeDTO> caFeeList;
    private List<PlAgtFeeDTO> profitList;

    private List<String> dateList;


}
