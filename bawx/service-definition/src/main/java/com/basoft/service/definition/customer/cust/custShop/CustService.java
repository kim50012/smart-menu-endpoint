package com.basoft.service.definition.customer.cust.custShop;

import java.util.List;

import com.basoft.service.dto.customer.CustDto;
import com.basoft.service.entity.wechat.wxMessage.*;
import com.basoft.service.param.customer.CustQueryParam;
import com.basoft.service.param.wechat.wxMessage.WxMessageQueryParam;
import com.github.pagehelper.PageInfo;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:14 2018/5/9
 **/
public interface CustService {
	public List<CustDto> findCustAll(CustQueryParam param);

	// 获得微信用户与公众号的聊天记录
	public List<WxIfStreamMsgStatsData> selectCustMsgStatic(WxMessageQueryParam queryParam);

	// 获得微信用户与公众号的聊天记录Table
	public PageInfo<WxIfStreamMsgStatsData> selectCustMsgStaticTable(WxMessageQueryParam queryParam);

	public List<WxIfMsgCountPercent> selectCountIntervalPercent(Long shopId, int timeType, String startTime, String endTime);

	// 查询昨日微信用户发送总数
	public WxIfMsgSumYestoday selectMsgSumYestoday(Long shopId, int timeType, String startTime, String endTime);

	public List<WxIfStreamMsgStatsDataWeek> selectCustMsgStaticWeek(Long shopId, Byte timeType, String startTime, String endTime);

	List<WxIfMsgCountPercent> selectCountIntervalPercentWeek(Long shopId, Byte timeType, String startTime, String endTime);

	public PageInfo<WxKeyMsgStats> findCustMsgKeyStatic(WxMessageQueryParam param);
}
