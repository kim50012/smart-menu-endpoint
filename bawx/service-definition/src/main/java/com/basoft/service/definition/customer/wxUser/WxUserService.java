package com.basoft.service.definition.customer.wxUser;

import java.util.List;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import com.basoft.service.dto.customer.WxUserSituationDto;
import com.basoft.service.dto.customer.WxUserTotalCityDto;
import com.basoft.service.dto.customer.WxUserTotalLangDto;
import com.basoft.service.dto.customer.WxUserTotalProvinDto;
import com.basoft.service.param.customer.WxUserQueryParam;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:43 2018/4/27
 **/
public interface WxUserService {
	public List<Integer> selectFollowSituation(WxUserQueryParam param);

	public List<String> userSituatDate(WxUserQueryParam param);

	// 新增粉丝数
	public int sumNewFans(WxUserQueryParam param);

	// 取关粉丝数
	public int sumOutFans(WxUserQueryParam param);

	// 总粉丝数
	public int totalFans(WxUserQueryParam param);

	public PageInfo<WxUserSituationDto> selectSituationDto(WxUserQueryParam param);

	// 语言统计扇形
	public WxUserTotalLangDto totalLanguigeType(Long shopId);

	// 语言统计list
	public List<WxUserTotalProvinDto> selectLanguigeTypeList(@Param("shopId") Long shopId);

	// 城市统计
	public List<WxUserTotalCityDto> totalCity(Long shopId);

	// 省份统计
	public List<WxUserTotalProvinDto> totalProvince(Long shopId);
}