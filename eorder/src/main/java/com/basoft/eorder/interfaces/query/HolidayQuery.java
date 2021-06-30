package com.basoft.eorder.interfaces.query;

import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:18 2019/7/24
 **/
public interface HolidayQuery {

	HolidayDTO getHolidayByMap(Map<String, Object> param);
}
