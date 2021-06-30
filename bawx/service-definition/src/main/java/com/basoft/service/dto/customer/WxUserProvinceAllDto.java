package com.basoft.service.dto.customer;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:03 2018/5/16
 **/
@Data
public class WxUserProvinceAllDto {
    private String name;

    private int value;

    private BigDecimal proportion;
}
