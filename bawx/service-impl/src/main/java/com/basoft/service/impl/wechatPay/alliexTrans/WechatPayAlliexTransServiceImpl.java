package com.basoft.service.impl.wechatPay.alliexTrans;

import com.basoft.service.dao.wechatPay.alliexTrans.WechatPayAlliexTransMapper;
import com.basoft.service.definition.wechatPay.alliexTrans.WechatPayAlliexTransService;
import com.basoft.service.enumerate.BatchEnum;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WechatPayAlliexTrans Service
 * 
 * @author basoft
 */
@Service
public class WechatPayAlliexTransServiceImpl implements WechatPayAlliexTransService {

	@Resource
	private WechatPayAlliexTransMapper wechatPayAlliexTransMapper;
	
	
	public Map<String, Object> selectBatchid_batch_header(Map<String, Object> map){
		return wechatPayAlliexTransMapper.selectBatchid_batch_header(map);
	}
	public Map<String, Object> insertSelect_batch_header(Map<String, Object> map){
		return wechatPayAlliexTransMapper.insertSelect_batch_header(map);
	}
	public void updateBatchResult_batch_header(Map<String, Object> map){
		wechatPayAlliexTransMapper.updateBatchResult_batch_header(map);
	}
	
	public void insertBatchInitial_order_pay_trans(Map<String, Object> map){
		wechatPayAlliexTransMapper.insertBatchInitial_order_pay_trans(map);
	}
	public void updateTargetWait_order_pay_trans(Map<String, Object> map){
		wechatPayAlliexTransMapper.updateTargetWait_order_pay_trans(map);
	}
	public List<Map<String, Object>> selectTargetList_order_pay_trans(Map<String, Object> map){
		return wechatPayAlliexTransMapper.selectTargetList_order_pay_trans(map);
	}
	public void updateBatchResult_order_pay_trans(Map<String, Object> map){
		wechatPayAlliexTransMapper.updateBatchResult_order_pay_trans(map);
	}
	
	public void insertBatchInitial_order_pay_trans_closing(Map<String, Object> map){
		wechatPayAlliexTransMapper.insertBatchInitial_order_pay_trans_closing(map);
	}
	public void updateTargetWait_order_pay_trans_closing(Map<String, Object> map){
		wechatPayAlliexTransMapper.updateTargetWait_order_pay_trans_closing(map);
	}
	public List<Map<String, Object>> selectTargetList_order_pay_trans_closing(Map<String, Object> map){
		return wechatPayAlliexTransMapper.selectTargetList_order_pay_trans_closing(map);
	}
	public void updateBatchResult_order_pay_trans_closing(Map<String, Object> map){
		wechatPayAlliexTransMapper.updateBatchResult_order_pay_trans_closing(map);
	}
	public Map<String, Object> selectTargetList_exchange_rate_if(Map<String, Object> map){
		return wechatPayAlliexTransMapper.selectTargetList_exchange_rate_if(map);
	}
	public void insertBatchResult_exchange_rate_if(Map<String, Object> map){
		wechatPayAlliexTransMapper.insertBatchResult_exchange_rate_if(map);
	}
	public void insertBatchResult_exchange_rate(Map<String, Object> map){
		wechatPayAlliexTransMapper.insertBatchResult_exchange_rate(map);
	}
	public void insertBatchInitial_order_pay_trans_search(Map<String, Object> map){
		wechatPayAlliexTransMapper.insertBatchInitial_order_pay_trans_search(map);
	}
	public void updateTargetWait_order_pay_trans_search(Map<String, Object> map){
		wechatPayAlliexTransMapper.updateTargetWait_order_pay_trans_search(map);
	}
	public List<Map<String, Object>> selectTargetList_order_pay_trans_search(Map<String, Object> map){
		return wechatPayAlliexTransMapper.selectTargetList_order_pay_trans_search(map);
	}
	public void updateBatchResult_order_pay_trans_search(Map<String, Object> map){
		wechatPayAlliexTransMapper.updateBatchResult_order_pay_trans_search(map);
	}
	
	public void insertBatchInitial_order_pay_trans_cancel(Map<String, Object> map){
		wechatPayAlliexTransMapper.insertBatchInitial_order_pay_trans_cancel(map);
	}
	public void updateTargetWait_order_pay_trans_cancel(Map<String, Object> map){
		wechatPayAlliexTransMapper.updateTargetWait_order_pay_trans_cancel(map);
	}
	public List<Map<String, Object>> selectTargetList_order_pay_trans_cancel(Map<String, Object> map){
		return wechatPayAlliexTransMapper.selectTargetList_order_pay_trans_cancel(map);
	}
	public void updateBatchResult_order_pay_trans_cancel(Map<String, Object> map){
		wechatPayAlliexTransMapper.updateBatchResult_order_pay_trans_cancel(map);
	}
	
	public List<Map<String, Object>> selectServerCheckOrderList(Map<String, Object> map){
		return wechatPayAlliexTransMapper.selectServerCheckOrderList(map);
	}

	public List<Map<String, Object>> selectBookingOrderSendMsgList(Map<String, Object> map){
		return wechatPayAlliexTransMapper.selectBookingOrderSendMsgList(map);
	}
	
	@Transactional
	public boolean getBatchRunFlag(Map<String, Object> map) {
		Map<String, Object> mapBatchid = wechatPayAlliexTransMapper.selectBatchid_batch_header(map);
		long batch_id = (long) mapBatchid.get("batch_id"); 
		if (batch_id == 0) {
			return false;
		} else {
			return true;
		}
	}

	public Map<String, Object> selectServerCheckFlag(Map<String, Object> map){
		return wechatPayAlliexTransMapper.selectServerCheckFlag(map);
	}
	
	@Transactional
	public boolean getServerCheckBatchRunFlag(Map<String, Object> map) {
		Map<String, Object> mapBatchid = wechatPayAlliexTransMapper.selectServerCheckFlag(map);
		String payMsgSendFlag =  mapBatchid.get("pay_msg_send_flag").toString(); 
		if ("Y".equals(payMsgSendFlag)) {
			return true;
		} else {
			return false;
		}
	}
}