package com.basoft.api.vo.customer;

import java.util.ArrayList;
import java.util.List;

import com.basoft.service.dto.customer.CustSexRadioDto;
import com.basoft.service.dto.customer.WxUserTotalProvinDto;

import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午11:47 2018/5/19
 **/
@Data
public class SexRadioVo {
	// private CustSexRadioDto custSexRadioDto;
	private List<WxUserTotalProvinDto> sexRadioList;

	public SexRadioVo(CustSexRadioDto dto, List<WxUserTotalProvinDto> list) {
		// this.custSexRadioDto = dto;
		if (list != null && list.size() > 0) {
			this.sexRadioList = list;
		} else {
			this.sexRadioList = new ArrayList<>();
		}
	}
}
