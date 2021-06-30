package com.basoft.service.dto.customer;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 语种统计-扇形图
 * 
 * @Author:DongXifu
 * @Date Created in 下午1:24 2018/5/15
 **/
@Data
public class WxUserTotalLangDto {
	// 简体中文数量
    private int zhCnSum;

    // 英语数量
    private int zhEnSum;

    // 韩文数量
    private int zhKoSum;
    
    // 其他数量
    private int zhOtherSum;

    // 简体中文百分比
    private BigDecimal zhCnSumAvg;

    // 英语百分比
    private BigDecimal zhEnSumAvg;

    // 韩文百分比
    private BigDecimal zhKoSumAvg;
    
    // 其他百分比
    private BigDecimal zhOtherSumAVG;
}
