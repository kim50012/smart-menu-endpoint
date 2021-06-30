package com.basoft.service.dto.customer;

import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午11:55 2018/4/27
 **/
@Data
public class WxUserSituationDto {
	private Integer newFans;// 新增关注人数

	private Integer outFans;// 取消关注人数

	private Integer growFans;// 净增关注人数

	private Integer totalFans;// 累计关注人数

	private String followDt;// 统计日期
}