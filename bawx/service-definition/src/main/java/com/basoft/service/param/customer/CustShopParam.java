package com.basoft.service.param.customer;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:15 2018/4/28
 **/
@Data
public class CustShopParam {
	private Long shopId;

	@NotBlank(message = "等级Id不能为空")
	private Long gradeId;

	List<Long> custList;
}
