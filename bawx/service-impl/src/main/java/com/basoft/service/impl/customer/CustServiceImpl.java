package com.basoft.service.impl.customer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.basoft.service.entity.wechat.wxMessage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.basoft.service.dao.customer.cust.CustMapper;
import com.basoft.service.definition.customer.cust.custShop.CustService;
import com.basoft.service.dto.customer.CustDto;
import com.basoft.service.param.customer.CustQueryParam;
import com.basoft.service.param.wechat.wxMessage.WxMessageQueryParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import static java.util.stream.Collectors.toList;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:23 2018/5/9
 **/
@Service
public class CustServiceImpl implements CustService {
	@Autowired
	private CustMapper custMapper;

	@Override
	public List<CustDto> findCustAll(CustQueryParam param) {
		List<CustDto> list = custMapper.findAllCust(param);
		return list;
	}

	@Override
	public List<WxIfStreamMsgStatsData> selectCustMsgStatic(WxMessageQueryParam queryParam) {
		return custMapper.selectCustMsgStatic(queryParam);
	}

	@Override
	public PageInfo<WxIfStreamMsgStatsData> selectCustMsgStaticTable(WxMessageQueryParam queryParam) {
		PageHelper.startPage(queryParam.getPage(), queryParam.getRows());
		List<WxIfStreamMsgStatsData> list = custMapper.selectCustMsgStatic(queryParam);
		return new PageInfo<>(list);
	}

	@Override
	public List<WxIfMsgCountPercent> selectCountIntervalPercent(Long shopId, int timeType, String startTime, String endTime) {
		return custMapper.selectCountIntervalPercent(shopId, timeType, startTime, endTime);
	}

	@Override
	public WxIfMsgSumYestoday selectMsgSumYestoday(Long shopId, int timeType, String startTime, String endTime) {
		return custMapper.selectMsgSumYestoday(shopId, timeType, startTime, endTime);
	}

	@Override
	public List<WxIfStreamMsgStatsDataWeek> selectCustMsgStaticWeek(Long shopId, Byte timeType, String startTime, String endTime) {
		return custMapper.selectCustMsgStaticWeek(shopId, timeType, startTime, endTime);
	}

	@Override
	public List<WxIfMsgCountPercent> selectCountIntervalPercentWeek(Long shopId, Byte timeType, String startTime, String endTime) {
		return custMapper.selectCountIntervalPercentWeek(shopId, timeType, startTime, endTime);
	}

	@Override
	public PageInfo<WxKeyMsgStats> findCustMsgKeyStatic(WxMessageQueryParam param) {
		PageHelper.startPage(param.getPage(), param.getRows());
		int keyType = param.getKeyType();
		if(keyType==0){
			return new PageInfo<>(custMapper.findCustMsgKeyStatic(param));
		}else if (keyType==1){
			return new PageInfo<>(custMapper.findKeyStaticByCustom(param));
		}else if(keyType==2){
			List<WxKeyMsgStats> listAll = custMapper.findCustMsgKeyStatic(param);
			List<WxKeyMsgStats> listKey = custMapper.findKeyStaticByCustom(param);

			for(WxKeyMsgStats key:listKey){
				listAll = listAll.stream().filter(all-> !key.getContent().equals(all.getContent())).collect(Collectors.toList());;
			}

			 return  new PageInfo<>(listAll);
		}
		return new PageInfo<>();
	}


}
