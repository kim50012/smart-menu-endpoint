package com.basoft.service.batch.wechat.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.basoft.core.ware.wechat.util.HttpClientUtils;
import com.basoft.core.ware.wechat.util.XmlUtils;
import com.basoft.core.ware.wechat.util.SFTPClientUtils;
import com.basoft.service.definition.wechatPay.alliexTrans.WechatPayAlliexTransService;
import com.basoft.service.util.WechatPayAlliexUtils;
import com.google.gson.Gson;

/**
 * 获取关注用户线程
 */
public class AlliexTransPaymentClosingThread implements Runnable {
	private final transient Log logger = LogFactory.getLog(this.getClass());

	private WechatPayAlliexTransService wechatPayAlliexTransService;

	/**
	 * 알리엑스로 payment interface
	 * 
	 * @param AlliexTransPaymentClosingThread
	 * @param wechatPayAlliexTransService
	 */
	public AlliexTransPaymentClosingThread(WechatPayAlliexTransService wechatPayAlliexTransService) {
		this.wechatPayAlliexTransService = wechatPayAlliexTransService;
	}

	@Override
	public void run() {
		
		logger.info("++++++++++++++++ AlliexTransPaymentClosingThread.run() ++++++++++++++++");
		
		Date ifStartDate = new Date(new Date().getTime() - 5 * 60 * 1000);
		System.out.println(ifStartDate);

		Map<String,Object> map = new HashMap<String, Object>();


		logger.info("++++++++++++++++ AlliexTransPaymentClosingThread - STEP 0100 : Create Batch Header Id ++++++++++++++++");
		// order_pay_trans 배치 작업 실행을 위한 Batch Header Id 생성
		map.clear();
		map.put("batch_type", "order_pay_trans_closing");	//★★★★★★ Batch job 유형
		Map<String, Object> batchHeader = wechatPayAlliexTransService.insertSelect_batch_header(map);
		BigInteger batch_id = (BigInteger) batchHeader.get("batch_id");
		
		try {
			logger.info("++++++++++++++++ AlliexTransPaymentClosingThread - STEP 0200 : Create OrderPayTrans Data ++++++++++++++++");
			// 알리엑스 I/F 대상이 되는 데이터 생성
			map.clear();
			wechatPayAlliexTransService.insertBatchInitial_order_pay_trans_closing(map);

			
			logger.info("++++++++++++++++ AlliexTransPaymentClosingThread - STEP 0300 : Update Waite status Max 100 low ++++++++++++++++");
			// 알리엑스 I/F 대기 상태로 데이터 업데이트
			map.clear();
			map.put("batch_id", batch_id);
			wechatPayAlliexTransService.updateTargetWait_order_pay_trans_closing(map);

			
			logger.info("++++++++++++++++ AlliexTransPaymentClosingThread - STEP 0400 : Get I/F Data ++++++++++++++++");
			// 알리엑스 I/F 되는 대상 데이터 조회
			map.clear();
			map.put("batch_id", batch_id);
			List<Map<String, Object>> alliPaymentList = wechatPayAlliexTransService.selectTargetList_order_pay_trans_closing(map);

			String ftpFileData = "";
			String transdate = "";
			String sfpt_id = "";
			String sftp_pw = "";
			String sftp_directory_home = "";
			String sftp_directory = "";
			String sftp_ip = "";
			int sftp_port = 22;
			
			logger.info("++++++++++++++++ AlliexTransPaymentClosingThread - STEP 0410 : I/F Data Total Count ▶▶▶▶ " + alliPaymentList.size());
			for(int i = 0; i < alliPaymentList.size(); i++){
				Map<String,Object> alliPaymentData = alliPaymentList.get(i);
				ftpFileData += alliPaymentData.get("closing_data") + "\n";
				transdate = alliPaymentData.get("transdate").toString();
				sfpt_id = alliPaymentData.get("sfpt_id").toString();
				sftp_pw = alliPaymentData.get("sftp_pw").toString();
				sftp_directory_home = alliPaymentData.get("sftp_directory_home").toString();
				sftp_directory = alliPaymentData.get("sftp_directory").toString();
				sftp_ip = alliPaymentData.get("sftp_ip").toString();
				sftp_port = (int) alliPaymentData.get("sftp_port");
				int x = i + 1;
				logger.info("++++++++++++++++ AlliexTransPaymentClosingThread - STEP 0500 : Post I/F Data  ▶▶ " + x + " th ◀◀");
			}

			logger.info("++++++++++++++++ AlliexTransPaymentClosingThread - STEP 0600 : I/F Data ftpFile Data ▶▶▶▶ \n\r" + ftpFileData);
			
			// 알리엑스 FTP로 Transfer
			trasToAlliex(batch_id, ftpFileData, transdate, sfpt_id, sftp_pw, sftp_directory_home, sftp_directory, sftp_ip, sftp_port);
			
			logger.info("++++++++++++++++ AlliexTransPaymentClosingThread - Update Batch Run Result ++++++++++++++++");
			// BATCH Header 결과 저장 (정상처리)
			map.clear();
			map.put("status", 2);
			map.put("batch_id", batch_id);
			wechatPayAlliexTransService.updateBatchResult_batch_header(map);
			
			
		} catch (Exception e) {
			logger.error("++++++++++++++++ AlliexTransPaymentClosingThread - Error : " + e.getMessage());
			//
			// BATCH Header 결과 저장 (오류처리)
			map.clear();
			map.put("status", 9);
			map.put("batch_id", batch_id);
			wechatPayAlliexTransService.updateBatchResult_batch_header(map);
			e.printStackTrace();
		}
	}
	

	private void trasToAlliex(BigInteger batch_id, String ftpFileData, String today, String sfpt_id, String sftp_pw, String sftp_directory_home, String sftp_directory, String sftp_ip, int sftp_port){

		logger.info("++++++++++++++++ AlliexTransPaymentClosingThread - STEP 0600 : FTP Upload Start ++++++++++++++++");
		Map<String,Object> map = new HashMap<String, Object>();
				
		try {

			String filename = WechatPayAlliexUtils.FTP_FILE_NAME+today+".txt";
			
			File directory = new File(WechatPayAlliexUtils.FTP_FROM_SERVER_DIRECTORY);
			if(!directory.exists()) {
				directory.mkdirs();	
			}
			String fullPath = WechatPayAlliexUtils.FTP_FROM_SERVER_DIRECTORY + filename;
			File file = new File(fullPath);
			if(file.exists()){
				file.delete();
			}
			try {
				FileWriter fw = new FileWriter(file);
				fw.write(ftpFileData);
				fw.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			logger.info("++++++++++++++++ AlliexTransPaymentClosingThread - STEP 0610 : file name:"+filename);
			
			boolean realServer = false;
			boolean uploadFlag = SFTPClientUtils.upload(sftp_directory_home, sftp_directory, fullPath, realServer, sfpt_id, sftp_pw, sftp_ip, sftp_port);
			int status = 9;
			if (uploadFlag) {
				status = 2;
			}
			
			logger.info("++++++++++++++++ AlliexTransPaymentClosingThread -STEP 0620 : Save Result Data : ++++++++++++++++");
			// I/F 결과 저장 (정상처리)
			map.clear();
			map.put("batch_id", batch_id);
			map.put("status", status);
			try {
				wechatPayAlliexTransService.updateBatchResult_order_pay_trans_closing(map);
			}
			catch (Exception e) {
				logger.error("++++++++++++++++ AlliexTransPaymentClosingThread - STEP 0551 trasToAlliex Update Error : " + e.getMessage());
			}
			
		} catch (Exception e) {
			logger.error("++++++++++++++++ AlliexTransPaymentClosingThread - trasToAlliex Error : " + e.getMessage());
			// I/F 결과 저장 (오류처리)
			map.clear();
			map.put("batch_id", batch_id);
			map.put("status", 9);
			wechatPayAlliexTransService.updateBatchResult_order_pay_trans_closing(map);
			e.printStackTrace();
		}
	}
	
	
}
