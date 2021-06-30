package com.basoft.service.dao.wechatPay.alliexTrans;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface WechatPayAlliexTransMapper {
	
	/**
	 * 실행중인 Batch 작업이 있는지 확인 batch_header
	 * 
	 * @param 
	 * @return Map
	 */
	Map<String, Object> selectBatchid_batch_header(Map<String, Object> map);
	
	/**
	 * Batch job 실행을 위한 id 생성
	 * 
	 * @param
	 * @return Map
	 */
	Map<String, Object> insertSelect_batch_header(Map<String, Object> map);
	
	/**
	 * Batch 작업 결과 저장
	 * 
	 * @param
	 * 
	 */
	void updateBatchResult_batch_header(Map<String, Object> map);
	 
	/**
	 * 알리엑스로 I/F 되는 대상 선 저장 order_pay_trans
	 * 
	 * @param
	 * 
	 */
	void insertBatchInitial_order_pay_trans(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 되는 대상 대기 상태로 업데이트 order_pay_trans
	 * 
	 * @param
	 * 
	 */
	void updateTargetWait_order_pay_trans(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 되는 대상 조회 order_pay_trans
	 * 
	 * @param
	 * @return List
	 */
	List<Map<String, Object>> selectTargetList_order_pay_trans(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 결과 저장 order_pay_trans
	 * 
	 * @param
	 * 
	 */
	void updateBatchResult_order_pay_trans(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 되는 대상 선 저장 order_pay_trans_closing
	 * 
	 * @param
	 * 
	 */
	void insertBatchInitial_order_pay_trans_closing(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 되는 대상 대기 상태로 업데이트 order_pay_trans_closing
	 * 
	 * @param
	 * 
	 */
	void updateTargetWait_order_pay_trans_closing(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 되는 대상 조회 order_pay_trans_closing
	 * 
	 * @param
	 * @return List
	 */
	List<Map<String, Object>> selectTargetList_order_pay_trans_closing(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 결과 저장 order_pay_trans_closing
	 * 
	 * @param
	 * 
	 */
	void updateBatchResult_order_pay_trans_closing(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 되는 대상 조회 (환율) exchange_rate_if
	 * 
	 * @param
	 * @return Map
	 */
	Map<String, Object> selectTargetList_exchange_rate_if(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 결과 저장 (환율) exchange_rate_if
	 * 
	 * @param
	 * 
	 */
	void insertBatchResult_exchange_rate_if(Map<String, Object> map);
	
	/**
	 * 알리엑스 환욜 결과 받영 exchange_rate
	 * 
	 * @param
	 * 
	 */
	void insertBatchResult_exchange_rate(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 되는 대상 선 저장 order_pay_trans_search
	 * 
	 * @param
	 * 
	 */
	void insertBatchInitial_order_pay_trans_search(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 되는 대상 대기 상태로 업데이트 order_pay_trans_search
	 * 
	 * @param
	 * 
	 */
	void updateTargetWait_order_pay_trans_search(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 되는 대상 조회 order_pay_trans_search
	 * 
	 * @param
	 * @return List
	 */
	List<Map<String, Object>> selectTargetList_order_pay_trans_search(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 결과 저장 order_pay_trans_search
	 * 
	 * @param
	 * 
	 */
	void updateBatchResult_order_pay_trans_search(Map<String, Object> map);	

	/**
	 * 알리엑스로 I/F 되는 대상 선 저장 order_pay_trans_cancel
	 * 
	 * @param
	 * 
	 */
	void insertBatchInitial_order_pay_trans_cancel(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 되는 대상 대기 상태로 업데이트 order_pay_trans_cancel
	 * 
	 * @param
	 * 
	 */
	void updateTargetWait_order_pay_trans_cancel(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 되는 대상 조회 order_pay_trans_cancel
	 * 
	 * @param
	 * @return List
	 */
	List<Map<String, Object>> selectTargetList_order_pay_trans_cancel(Map<String, Object> map);
	
	/**
	 * 알리엑스로 I/F 결과 저장 order_pay_trans_cancel
	 * 
	 * @param
	 * 
	 */
	void updateBatchResult_order_pay_trans_cancel(Map<String, Object> map);

	/**
	 * 테스트 서버 Order check batch 실행여부 체크
	 * 
	 * @param 
	 * @return Map
	 */
	Map<String, Object> selectServerCheckFlag(Map<String, Object> map);
	
	/**
	 * 테스트 서버 Check Order List
	 * 
	 * @param
	 * @return List
	 */
	List<Map<String, Object>> selectServerCheckOrderList(Map<String, Object> map);
	
	/**
	 * Booking Order send msg List
	 * 
	 * @param
	 * @return List
	 */
	List<Map<String, Object>> selectBookingOrderSendMsgList(Map<String, Object> map);
}