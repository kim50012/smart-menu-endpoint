package com.basoft.service.dto.customer;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:11 2018/5/15
 **/
@Data
public class WxUserTotalProvinDto {

    private String name;

    private int value;

    private BigDecimal proportion;
}
