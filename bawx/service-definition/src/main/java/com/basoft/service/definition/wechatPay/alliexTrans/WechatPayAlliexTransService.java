package com.basoft.service.definition.wechatPay.alliexTrans;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * WechatPayAlliexTransService
 * 
 * @author BASOFT
 */
@Service
public interface WechatPayAlliexTransService {

	public Map<String, Object> selectBatchid_batch_header(Map<String, Object> map);
	public Map<String, Object> insertSelect_batch_header(Map<String, Object> map);
	public void updateBatchResult_batch_header(Map<String, Object> map);
	
	public void insertBatchInitial_order_pay_trans(Map<String, Object> map);
	public void updateTargetWait_order_pay_trans(Map<String, Object> map);
	public List<Map<String, Object>> selectTargetList_order_pay_trans(Map<String, Object> map);
	public void updateBatchResult_order_pay_trans(Map<String, Object> map);
	
	public void insertBatchInitial_order_pay_trans_closing(Map<String, Object> map);
	public void updateTargetWait_order_pay_trans_closing(Map<String, Object> map);
	public List<Map<String, Object>> selectTargetList_order_pay_trans_closing(Map<String, Object> map);
	public void updateBatchResult_order_pay_trans_closing(Map<String, Object> map);
	
	public Map<String, Object> selectTargetList_exchange_rate_if(Map<String, Object> map);
	public void insertBatchResult_exchange_rate_if(Map<String, Object> map);
	public void insertBatchResult_exchange_rate(Map<String, Object> map);
	
	public void insertBatchInitial_order_pay_trans_search(Map<String, Object> map);
	public void updateTargetWait_order_pay_trans_search(Map<String, Object> map);
	public List<Map<String, Object>> selectTargetList_order_pay_trans_search(Map<String, Object> map);
	public void updateBatchResult_order_pay_trans_search(Map<String, Object> map);
	
	public boolean getBatchRunFlag(Map<String, Object> map);

	public void insertBatchInitial_order_pay_trans_cancel(Map<String, Object> map);
	public void updateTargetWait_order_pay_trans_cancel(Map<String, Object> map);
	public List<Map<String, Object>> selectTargetList_order_pay_trans_cancel(Map<String, Object> map);
	public void updateBatchResult_order_pay_trans_cancel(Map<String, Object> map);

	public boolean getServerCheckBatchRunFlag(Map<String, Object> map);
	public Map<String, Object> selectServerCheckFlag(Map<String, Object> map);
	public List<Map<String, Object>> selectServerCheckOrderList(Map<String, Object> map);
	public List<Map<String, Object>> selectBookingOrderSendMsgList(Map<String, Object> map);
}
