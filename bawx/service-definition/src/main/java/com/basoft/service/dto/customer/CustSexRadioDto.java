package com.basoft.service.dto.customer;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午6:04 2018/4/28
 **/
@Data
public class CustSexRadioDto {

    private int custCnt;

    private int maleCnt;

    private int femaleCnt;

    private int unknownCnt;

    private BigDecimal maleAvg;

    private BigDecimal femaleAvg;

    private BigDecimal unknownAvg;

}
