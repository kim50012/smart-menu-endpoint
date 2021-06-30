package com.basoft.service.definition.customer.grade;

import java.util.List;

import com.basoft.service.dto.customer.CustGradeDto;
import com.basoft.service.dto.customer.CustSexRadioDto;
import com.basoft.service.dto.customer.WxUserTotalProvinDto;
import com.basoft.service.param.customer.CustGradeQueryParam;
import com.basoft.service.param.customer.CustShopParam;
import com.basoft.service.param.customer.WxUserQueryParam;
import com.github.pagehelper.PageInfo;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:26 2018/4/25
 **/
public interface CustGradeService {
	public PageInfo<CustGradeDto> findAllCustGrade(CustGradeQueryParam param);

	public int setGradeBycust(CustShopParam custShopParam);

	// 查询男女比列
	public CustSexRadioDto selectSexRadio(WxUserQueryParam param);

	public List<WxUserTotalProvinDto> selectSexRadioList(WxUserQueryParam param);
}
