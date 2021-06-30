package com.basoft.api.vo.customer;

import com.basoft.api.vo.ApiJson;
import com.basoft.service.dto.customer.WxUserSituationDto;
import lombok.Data;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:52 2018/4/27
 **/
@Data
public class UserSituationVo {
	// private List<Integer> userSituatList; //废弃
	
	// private List<String> userSituDate; //废弃

	// 昨日新关注人数
	private Integer sumNewFans;

	// 昨日取消关注人数
	private Integer sumOutFans;

	// 昨日净增关注人数
	private Integer sumGrowFans;

	// 昨日累计关注人数
	private Integer sumTotalFans;
	
	// 日期数组
	private List<String> date;
	
	// 用户关注现况统计-一天一条记录，形成此列表
	private List<WxUserSituationDto> userDtoSituatList;

	private ApiJson<List<?>> pageUser;
}
