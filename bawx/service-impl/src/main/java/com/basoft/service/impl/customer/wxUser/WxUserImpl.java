package com.basoft.service.impl.customer.wxUser;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.basoft.service.dao.customer.wxUser.WxUserMapper;
import com.basoft.service.definition.customer.wxUser.WxUserService;
import com.basoft.service.dto.customer.WxUserSituationDto;
import com.basoft.service.dto.customer.WxUserTotalCityDto;
import com.basoft.service.dto.customer.WxUserTotalLangDto;
import com.basoft.service.dto.customer.WxUserTotalProvinDto;
import com.basoft.service.param.customer.WxUserQueryParam;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:51 2018/4/27
 **/
@Service
public class WxUserImpl implements WxUserService {
	@Autowired
	private WxUserMapper wxUserMapper;

	@Override
	public List<Integer> selectFollowSituation(WxUserQueryParam param) {
		return wxUserMapper.selectSituation(param);
	}

	@Override
	public List<String> userSituatDate(WxUserQueryParam param) {
		return wxUserMapper.userSituatDate(param);
	}

	@Override
	public int sumNewFans(WxUserQueryParam param) {
		return wxUserMapper.sumNewFans(param);
	}

	@Override
	public int sumOutFans(WxUserQueryParam param) {
		return wxUserMapper.sumOutFans(param);
	}

	@Override
	public int totalFans(WxUserQueryParam param) {
		return wxUserMapper.totalFans(param);
	}

	@Override
	public PageInfo<WxUserSituationDto> selectSituationDto(WxUserQueryParam param) {
		PageHelper.startPage(param.getPage(), param.getRows());
        List<WxUserSituationDto> wxUserList = wxUserMapper.selectSituationDto(param);
        return new PageInfo<>(wxUserList);
	}

	@Override
	public WxUserTotalLangDto totalLanguigeType(Long shopId) {
		return wxUserMapper.totalLanguigeType(shopId);
	}

	@Override
	public List<WxUserTotalProvinDto> selectLanguigeTypeList(Long shopId) {
		return wxUserMapper.selectLanguigeTypeList(shopId);
	}

	@Override
	public List<WxUserTotalCityDto> totalCity(Long shopId) {
		return wxUserMapper.totalCity(shopId);
	}

	@Override
	public List<WxUserTotalProvinDto> totalProvince(Long shopId) {
		return wxUserMapper.totalProvince(shopId);
	}
}
