package com.basoft.service.batch.wechat;

import com.basoft.core.util.GetDateUtils;
import com.basoft.service.batch.wechat.thread.AlliexTransExchangeThread;
import com.basoft.service.batch.wechat.thread.AlliexTransPaymentClosingThread;
import com.basoft.service.batch.wechat.thread.AlliexTransPaymentQueryThread;
import com.basoft.service.batch.wechat.thread.AlliexTransPaymentThread;
import com.basoft.service.batch.wechat.thread.AlliexTransRefundThread;
import com.basoft.service.batch.wechat.thread.ArticleSummaryGetUseReadHourThread;
import com.basoft.service.batch.wechat.thread.ArticleSummaryGetUseReadThread;
import com.basoft.service.batch.wechat.thread.ArticleSummarySendThread;
import com.basoft.service.batch.wechat.thread.BookingOrderSendMsgThread;
import com.basoft.service.batch.wechat.thread.GetStreamMsgHourThread;
import com.basoft.service.batch.wechat.thread.GetStreamMsgThread;
import com.basoft.service.batch.wechat.thread.GetStreamMsgWeekThread;
import com.basoft.service.batch.wechat.thread.ServerOrderCheckThread;
import com.basoft.service.batch.wechat.thread.SyncUserManualThread;
import com.basoft.service.batch.wechat.thread.SyncUserThread;
import com.basoft.service.batch.wechat.thread.WxUpTokenThread;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.definition.wechat.user.WeixinUserService;
import com.basoft.service.definition.wechatPay.alliexTrans.WechatPayAlliexTransService;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;
import com.basoft.service.enumerate.BatchEnum;
import com.basoft.service.util.HttpClientUtils;
import com.basoft.service.util.OuterIPUtil;
import com.basoft.service.util.RedisUtil;
import com.basoft.service.util.RedissonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**********************************************************************
 Spring @Scheduled 설정
 ex)
 @Scheduled(cron="0 0 05 * * ?") = 매일 5시에 실행
 @Scheduled(cron="0 0 02 2,20 * ?") = 매월 2일,20일 새벽2시에 실행
  ***********************************************************************
 ---  cron 양식 ---
 -초 0-59 , - * /
 -분 0-59 , - * /
 -시 0-23 , - * /
 -일 1-31 , - * ? / L W
 -월 1-12 or JAN-DEC , - * /
 -요일 1-7 or SUN-SAT , - * ? / L #
 -년(옵션) 1970-2099 , - * /

  * : 모든 값
 ? : 특정 값 없음
 - : 범위 지정에 사용
 , : 여러 값 지정 구분에 사용
 / : 초기값과 증가치 설정에 사용
 L : 지정할 수 있는 범위의 마지막 값
 W : 월~금요일 또는 가장 가까운 월/금요일
 # : 몇 번째 무슨 요일 2#1 => 첫 번째 월요일

 초 분 시 일 월 주(년)
 "0 0 12 * * ?" : 아무 요일, 매월, 매일 12:00:00
 "0 15 10 ? * *" : 모든 요일, 매월, 아무 날이나 10:15:00
 "0 15 10 * * ?" : 아무 요일, 매월, 매일 10:15:00
 "0 15 10 * * ? *" : 모든 연도, 아무 요일, 매월, 매일 10:15
 "0 15 10 * * ? : 2005" 2005년 아무 요일이나 매월, 매일 10:15
 "0 * 14 * * ?" : 아무 요일, 매월, 매일, 14시 매분 0초
 "0 0/5 14 * * ?" : 아무 요일, 매월, 매일, 14시 매 5분마다 0초
 "0 0/5 14,18 * * ?" : 아무 요일, 매월, 매일, 14시, 18시 매 5분마다 0초
 "0 0-5 14 * * ?" : 아무 요일, 매월, 매일, 14:00 부터 매 14:05까지 매 분 0초
 "0 10,44 14 ? 3 WED" : 3월의 매 주 수요일, 아무 날짜나 14:10:00, 14:44:00
 "0 15 10 ? * MON-FRI" : 월~금, 매월, 아무 날이나 10:15:00
 "0 15 10 15 * ?" : 아무 요일, 매월 15일 10:15:00
 "0 15 10 L * ?" : 아무 요일, 매월 마지막 날 10:15:00
 "0 15 10 ? * 6L" : 매월 마지막 금요일 아무 날이나 10:15:00
 "0 15 10 ? * 6L 2002-2005" : 2002년부터 2005년까지 매월 마지막 금요일 아무 날이나 10:15:00
 "0 15 10 ? * 6#3" : 매월 3번째 금요일 아무 날이나 10:15:00
 */


/**
 * 微信公众号统计数据拉取定时任务
 *
 * @author BASOFT
 */
@Slf4j
@Component
public class BatchWechatJob {
	@Autowired
	private WechatService wechatService;

	@Autowired
	WeixinUserService weixinUserService;

	@Autowired
	private WechatPayAlliexTransService wechatPayAlliexTransService;
	@Autowired
	private RedisUtil redisUtil;

	@Value("${aws.batch.ip1}")
	private String ip1;
	@Value("${aws.batch.ip2}")
	private String ip2;

	// private static final transient Log logger = LogFactory.getLog(BatchWechatJob.class);


	// ---------------------------------------------------------------------------------------------------------------------------------------------
	// BATCH JOB START
	// ---------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * 	获取token，每格60分钟执行
	 */
//	@Scheduled(cron = "0 0 * * * ?")
	public void getWxToken(){
		log.info("=========> BATCH getWxToken <=========");
		boolean ifRun = isRunning(BatchEnum.batchGetWxToken.getType(),60*30L);
		if(ifRun) {
			log.info("<><><><><><><><><><>BATCH_getWxToken<><><><><><><><><><>");
			log.info("<><><><><><><><><><>执行token任务时间<><><><><><><><><><>" + new Date());
			List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();
			for (AppInfoWithBLOBs bloBs : appInfoList) {
				Thread thread = new Thread(new WxUpTokenThread(wechatService, bloBs.getSysId()));
				thread.start();
			}
		}
	}

	/*public static int  num = 0;
	@Scheduled(cron = "0/30 * * * * ? ")
	public void colonyTest() {

		if (num == 0) {
			log.info("numnumnumnumnumnum");
			redisUtil.hset(ip1,String.valueOf(BatchEnum.batchColonyTest.getType()), ip1,50L);
			redisUtil.hset(ip2,String.valueOf(BatchEnum.batchColonyTest.getType()), ip2,50L);
			num++;
		}
		log.info("num="+num);
		if (isRun(BatchEnum.batchColonyTest.getType(),50L)) {
			log.info("colonyTestcolonyTestcolonyTest");
			List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();
		}
	}*/


	/**
	 * BATCH1:获取图文群发每日数据并插入(单条图文统计)
	 * 	表：WX_IF_MESSAGE_STATS_DATA
	 * 	每天凌晨7点10分执行
	 */
//	@Scheduled(cron = "0 10 7 ? * *")
	//@Scheduled(cron = "0/5 * * * * ? ")
	public void articleSummaryGroupHair() {
		log.info("=========> BATCH articleSummaryGroupHair <=========");
		if(isRunning(BatchEnum.articleSummaryGroupHair.getType(),60*60*24+20L)) {
			List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();
			for (AppInfoWithBLOBs bloBs : appInfoList) {
				log.info("getArticleSummaryGroupHair||获取图文群发每日数据并插入(单条图文统计)==AppInfoWithBLOBs>>>>" + bloBs);
				log.info("当前时间==" + new Date());
				// 锁检查
				boolean isRun = wechatService.queryWxBatchLock(bloBs.getSysId(), BatchEnum.articleSummaryGroupHair.getType());
				// 非加锁状态
				if (!isRun) {
					Thread thread = new Thread(new ArticleSummarySendThread(wechatService, bloBs.getSysId(), bloBs.getShopId(),
							GetDateUtils.getYesterdayStr(), GetDateUtils.getYesterdayStr()));
					thread.start();
				} else {
					log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||articleSummaryGroupHair BATCH1 IDLE|||||||||||||||||||||||||||||||||||||||||||||||||||");
				}
			}
		}
	}

	/**
	 * BATCH2:获取图文群发统计数据并插入(详细统计)，统计阅读人数 分享人数 收藏人数等等
	 * 	表：WX_IF_MESSAGE_STATS_DETAIL
	 * 	每天7点执行
	 */
//	@Scheduled(cron = "0 20 7 ? * *")
	public void articleSummaryDetailStatics() {
		log.info("=========> BATCH articleSummaryDetailStatics <=========");
		if(isRunning(BatchEnum.articleSummaryDetailStatics.getType(),60*60*24+20L)) {
			List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();
			for (AppInfoWithBLOBs bloBs : appInfoList) {
				log.info("ArticleSummaryDetailStatics||获取图文群发统计数据并插入(详细统计)==AppInfoWithBLOBs>>>>" + bloBs);
				// 锁检查
				boolean isRun = wechatService.queryWxBatchLock(bloBs.getSysId(), BatchEnum.articleSummaryDetailStatics.getType());
				// 非加锁状态
				if (!isRun) {
					Thread thread = new Thread(new ArticleSummaryGetUseReadThread(wechatService, bloBs.getSysId(), bloBs.getShopId(),
							GetDateUtils.getYesterdayStr(), GetDateUtils.getYesterdayStr()));
					thread.start();
				} else {
					log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||articleSummaryDetailStatics BATCH2 IDLE|||||||||||||||||||||||||||||||||||||||||||||||||||");
				}
			}
		}
	}

	/**
	 * BATCH3:获取图文群发统计数据并插入(分时详细统计)
	 * 	统计阅读人数、分享人数、收藏人数等等(WX_IF_MESSAGE_STATS_DETAIL)
	 *
	 */
//	@Scheduled(cron = "0 25 7 ? * *")
	public void articleSummaryHourDetailStatics() {
		log.info("=========> BATCH articleSummaryHourDetailStatics <=========");
		if(isRunning(BatchEnum.articleSummaryHourDetailStatics.getType(),60*60*24+20L)) {
			List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();
			for (AppInfoWithBLOBs bloBs : appInfoList) {
				log.info("ArticleSummaryHourDetailStatics||获取图文群发统计数据并插入(分时详细统计)==AppInfoWithBLOBs>>>>" + bloBs);
				// 锁检查
				boolean isRun = wechatService.queryWxBatchLock(bloBs.getSysId(), BatchEnum.articleSummaryHourDetailStatics.getType());
				// 非加锁状态
				if (!isRun) {
					Thread thread = new Thread(new ArticleSummaryGetUseReadHourThread(wechatService, bloBs.getSysId(), bloBs.getShopId(),
							GetDateUtils.getYesterdayStr(), GetDateUtils.getYesterdayStr()));
					thread.start();
				} else {
					log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||articleSummaryHourDetailStatics BATCH3 IDLE|||||||||||||||||||||||||||||||||||||||||||||||||||");
				}
			}
		}
	}

	/**
	 * BATCH4:获取昨天发送消息数(分时统计)
	 *
	 */
//	@Scheduled(cron = "0 30 7 ? * *")
	public void batchInsertStreamMsgYesTodayHour() {
		log.info("=========> BATCH batchInsertStreamMsgYesTodayHour <=========");
		if(isRunning(BatchEnum.batchInsertStreamMsgYesTodayHour.getType(),60*60*24+20L)) {
			List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();
			for (AppInfoWithBLOBs bloBs : appInfoList) {
				log.info("batchInsertStreamMsgYesTodayHour||获取昨天发送消息数(分时统计)==AppInfoWithBLOBs>>>>" + bloBs);
				// 锁检查
				boolean isRun = wechatService.queryWxBatchLock(bloBs.getSysId(), BatchEnum.batchInsertStreamMsgYesTodayHour.getType());
				// 非加锁状态
				if (!isRun) {
					Thread thread = new Thread(new GetStreamMsgHourThread(wechatService, bloBs.getSysId(), bloBs.getShopId(),
							GetDateUtils.getYesterdayStr(), GetDateUtils.getYesterdayStr()));
					thread.start();
				} else {
					log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||batchInsertStreamMsgYesTodayHour BATCH4 IDLE|||||||||||||||||||||||||||||||||||||||||||||||||||");
				}
			}
		}
	}

	/**
	 * BATCH5:获取昨天发送消息数
	 * 	插入表：WX_IF_STREAM_MSG_STATS_DATA
	 * 	每天凌晨7点30分执行
	 */
//	@Scheduled(cron = "0 40 7 ? * *")
	public void batchInsertStreamMsgYesToday() {
		log.info("=========> BATCH batchInsertStreamMsgYesToday <=========");
		if(isRunning(BatchEnum.batchInsertStreamMsgYesToday.getType(),60*60*24+20L)) {
			List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();
			for (AppInfoWithBLOBs bloBs : appInfoList) {
				log.info("获取昨天消息发送数=====batchInsertStreamMsgYesToday");
				// 锁检查
				boolean isRun = wechatService.queryWxBatchLock(bloBs.getSysId(), BatchEnum.batchInsertStreamMsgYesToday.getType());
				// 非加锁状态
				if (!isRun) {
					Thread thread = new Thread(new GetStreamMsgThread(wechatService, bloBs.getSysId(), bloBs.getShopId(),
							GetDateUtils.getYesterdayStr(), GetDateUtils.getYesterdayStr()));
					thread.start();
				} else {
					log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||batchInsertStreamMsgYesToday BATCH5 IDLE|||||||||||||||||||||||||||||||||||||||||||||||||||");
				}
			}
		}
	}

	/**
	 * BATCH6:获取上周消息发送统计
	 * 	每周一凌晨3点15分执行
	 */
//	@Scheduled(cron = "0 15 3 ? * MON")
	//@Scheduled(cron = "0 21 14 ? * *")下午两点21
	public void batchInsertStreamMsgLastWeek() {
		log.info("=========> BATCH batchInsertStreamMsgLastWeek <=========");
		if(isRunning(BatchEnum.batchInsertStreamMsgLastWeek.getType(),60*60*24*7+20L)) {
			List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();
			for (AppInfoWithBLOBs bloBs : appInfoList) {
				// 锁检查
				boolean isRun = wechatService.queryWxBatchLock(bloBs.getSysId(), BatchEnum.batchInsertStreamMsgLastWeek.getType());
				// 非加锁状态
				if (!isRun) {
					Thread thread = new Thread(new GetStreamMsgWeekThread(wechatService, bloBs.getSysId(), bloBs.getShopId(),
							GetDateUtils.getBeginDayOfLastWeekStr(), GetDateUtils.getEndDayOfLastWeekStr()));
					thread.start();
				} else {
					log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||batchInsertStreamMsgLastWeek BATCH6 IDLE|||||||||||||||||||||||||||||||||||||||||||||||||||");
				}
			}
		}
	}

	/**
	 * BATCH7:获取关注的用户信息
	 * 	每天凌晨1点执行
	 */
//	@Scheduled(cron = "0 0 1 * * *")
//	@Scheduled(cron = "0 0/1  * * * ? ")
	//@Scheduled(cron = "30 21 14 ? * *")
	public void batchUser() {
		log.info("=========> BATCH batchUser <=========");
		if(isRunning(BatchEnum.batchUser.getType(),60*60*24+20L)) {
			List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();
			for (AppInfoWithBLOBs bloBs : appInfoList) {
				// 锁检查
				boolean isRun = wechatService.queryWxBatchLock(bloBs.getSysId(), BatchEnum.batchUser.getType());
				// 非加锁状态
				if (!isRun) {
					Thread thread = new Thread(new SyncUserThread(wechatService, weixinUserService, bloBs.getSysId(), bloBs.getShopId()));
					thread.start();
				} else {
					log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||batchInsertWxUse Cust|||||||||||||||||||||||||||||||||||||||||||||||||||");
				}
			}
		}
	}

	/**
	 * BATCH8:Wechat Pay 결과 Alli-Ex 로 전송  정상거래 전송
	 *
	 */
//	@Scheduled(cron = "0 0/5 * * * ?")	//每5分钟
//	@Scheduled(cron = "0/10 * * * * ?")	//TEST : 每天每隔10秒钟执行一次, 매 10초 마다 한번 실행
	public void batchAlliexTransPayment() {
		log.info("=========> BATCH batchAlliexTransPayment <=========");
		if(isRunning(BatchEnum.batchAlliexTransPayment.getType(),60*5+20L)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", 1);
			map.put("batch_type", "order_pay_trans");
			boolean isRun = wechatPayAlliexTransService.getBatchRunFlag(map);
			if (!isRun) {
				log.info("▶▶▶▶▶▶▶▶▶▶ 알리엑스 정상 ◀◀◀◀◀◀◀◀◀◀ batchAlliexTransPayment BATCH8 Start Run +++++++++++++++");
				Thread thread = new Thread(new AlliexTransPaymentThread(wechatPayAlliexTransService));
				thread.start();
			} else {
				log.info("▶▶▶▶▶▶▶▶▶▶ 알리엑스 정상 ◀◀◀◀◀◀◀◀◀◀ batchAlliexTransPayment BATCH8 Not Run +++++++++++++++");
			}
		}
	}

	/**
	 * BATCH9:Alli-Ex 환율 조회
	 *
	 */
//	@Scheduled(cron = "0 0/10 * * * ?")	//매 10분에 실행 by dikim 20190614
//	@Scheduled(cron = "0/5 * * * * ?")	//TEST : 每天每隔5秒钟执行一次, 매 5초마다 실행
//	@Scheduled(cron = "0 10 0/1 * * ?")	//매 1시간마다 10분에 실행
	public void batchAlliexTransExchange() {
		log.info("=========> BATCH batchAlliexTransExchange <=========");
		if(isRunning(BatchEnum.batchAlliexTransExchange.getType(),60*10+20L)) {
			log.info("▶▶▶▶▶▶▶▶▶▶ 알리엑스 환율 ◀◀◀◀◀◀◀◀◀◀ batchAlliexTransExchange  Start Run +++++++++++++++");
			Thread thread = new Thread(new AlliexTransExchangeThread(wechatPayAlliexTransService, redisUtil));
			thread.start();
		}
	}

	/**
	 * BATCH10:Wechat Pay Closing FTP Alli-Ex 로 전송 - 거래대사
	 *
	 */
//	@Scheduled(cron = "0 0 2 * * ?")	//每天2点30分 매일 2시 30분
	public void batchAlliexTransPaymentClosing() {
		log.info("=========> BATCH batchAlliexTransPaymentClosing <=========");
		if(isRunning(BatchEnum.batchAlliexTransPaymentClosing.getType(),60*60*24+20L)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", 1);
			map.put("batch_type", "order_pay_trans_closing");
			boolean isRun = wechatPayAlliexTransService.getBatchRunFlag(map);
			if (!isRun) {
				log.info("▶▶▶▶▶▶▶▶▶▶ 알리엑스 거래대사 ◀◀◀◀◀◀◀◀◀◀ batchAlliexTransPaymentClosing BATCH10 Start Run +++++++++++++++");
				Thread thread = new Thread(new AlliexTransPaymentClosingThread(wechatPayAlliexTransService));
				thread.start();
			} else {
				log.info("▶▶▶▶▶▶▶▶▶▶ 알리엑스 거래대사 ◀◀◀◀◀◀◀◀◀◀ batchAlliexTransPaymentClosing BATCH10 Not Run +++++++++++++++");
			}
		}
	}

	/**
	 * BATCH11:Wechat Pay Alli-Ex 로 전송결과 조회
	 *
	 */
//	@Scheduled(cron = "0 5 0/1 * * ?")	//每1个小时一次运行
//	@Scheduled(cron = "1/5 * * * * ?")	TEST : 每天每隔5秒钟执行一次, 매 5분 마다 한번 실행
	public void batchAlliexTransPaymentQuery() {
		log.info("=========> BATCH batchAlliexTransPaymentQuery <=========");
		if(isRunning(BatchEnum.batchAlliexTransPaymentQuery.getType(),60*60+20L)){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", 1);
			map.put("batch_type", "order_pay_trans");
			boolean isRun = wechatPayAlliexTransService.getBatchRunFlag(map);
			if (!isRun) {
				log.info("▶▶▶▶▶▶▶▶▶▶ 알리엑스 조회 ◀◀◀◀◀◀◀◀◀◀ batchAlliexTransPaymentQuery BATCH11 Start Run +++++++++++++++");
				Thread thread = new Thread(new AlliexTransPaymentQueryThread(wechatPayAlliexTransService));
				thread.start();
			} else {
				log.info("▶▶▶▶▶▶▶▶▶▶ 알리엑스 조회 ◀◀◀◀◀◀◀◀◀◀ batchAlliexTransPaymentQuery BATCH11 Not Run +++++++++++++++");
			}
		}
	}


	/**
	 * BATCH12:Wechat Pay 결과 Alli-Ex 로 전송 취소거래 전송
	 *
	 */
//	@Scheduled(cron = "0 4/5 * * * ?")	//每5分钟, 매 4,9분 마다 한번 실행
//	@Scheduled(cron = "5/10 * * * * ?")	//TEST : 每天每隔10秒钟执行一次, 매 10초 마다 한번 실행
	public void batchAlliexTransRefund() {
		log.info("=========> BATCH batchAlliexTransRefund <=========");
		if(isRunning(BatchEnum.batchAlliexTransRefund.getType(),60*5+20L)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", 1);
			map.put("batch_type", "order_pay_trans");
			boolean isRun = wechatPayAlliexTransService.getBatchRunFlag(map);
			if (!isRun) {
				log.info("▶▶▶▶▶▶▶▶▶▶ 알리엑스 취소 ◀◀◀◀◀◀◀◀◀◀ batchAlliexTransRefund BATCH12 Start Run +++++++++++++++");
				Thread thread = new Thread(new AlliexTransRefundThread(wechatPayAlliexTransService));
				thread.start();
			} else {
				log.info("▶▶▶▶▶▶▶▶▶▶ 알리엑스 취소 ◀◀◀◀◀◀◀◀◀◀ batchAlliexTransRefund BATCH12 Not Run +++++++++++++++");
			}
		}
	}


	/**
	 * BATCH13:TEST SERVER Order checking (send template message)
	 *
	 */
//	@Scheduled(cron = "0 0/30 * * * ?")	//每30分钟, 매 30분 마다 한번 실행
//	@Scheduled(cron = "5/10 * * * * ?")	//TEST : 每天每隔10秒钟执行一次, 매 10초 마다 한번 실행
	public void batchTestServerOrderCheck() {
		log.info("=========> BATCH batchTestServerOrderCheck <=========");
		if(isRunning(BatchEnum.batchAlliexTransRefund.getType(),60*30+20L)) {
			List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();
			for (AppInfoWithBLOBs bloBs : appInfoList) {

				Map<String,Object> map = new HashMap<String, Object>();
				map.put("server_type", "TEST_SERVER");
				boolean isRun = wechatPayAlliexTransService.getServerCheckBatchRunFlag(map);
				if(isRun) {
					log.info("▶▶▶▶▶▶▶▶▶▶ 오더현황체크 실행 ◀◀◀◀◀◀◀◀◀◀ batchTestServerOrderCheck BATCH13 Start Run +++++++++++++++");
					Thread thread = new Thread(new ServerOrderCheckThread(wechatPayAlliexTransService, wechatService, bloBs.getShopId()));
					thread.start();
				}
				else {
					log.info("▶▶▶▶▶▶▶▶▶▶ 오더현황체크 종료 ◀◀◀◀◀◀◀◀◀◀ batchTestServerOrderCheck BATCH13 Reject Run +++++++++++++++");
				}

			}
		}
	}


	/**
	 * BATCH12:Wechat Pay 결과 Alli-Ex 로 전송 취소거래 전송
	 *
	 */ 
//	@Scheduled(cron = "0 0 10 * * ?")	//每天9点  매일 10시
//	@Scheduled(cron = "0 0/1 * * * ?")	//TEST
	public void batchBookingOrderMsgSend() {
		log.info("=========> BATCH batchBookingOrderMsgSend <=========");
		if(isRunning(BatchEnum.batchBookingOrderSendMsg.getType(),60*5+20L)) {

			List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();

			for (AppInfoWithBLOBs bloBs : appInfoList) {
				log.info("▶▶▶▶▶▶▶▶▶▶ 예약확인 메시지 발송 ◀◀◀◀◀◀◀◀◀◀ batchBookingOrderMsgSend BATCH13 Start Run +++++++++++++++");
				Thread thread = new Thread(new BookingOrderSendMsgThread(wechatPayAlliexTransService, wechatService, bloBs.getSysId(), bloBs.getShopId()));
				thread.start();
			}	
			
		}
	}
	

	// ---------------------------------------------------------------------------------------------------------------------------------------------
	// BATCH JOB END
	// ---------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 判断当前服务器是否执行
	 *
	 * @return java.lang.Boolean
	 * @Param batchType 定时任务编号
	 * @Param outTime redis过期时间
	 * @author Dong Xifu
	 * @date 2019/6/27 上午10:11
	 */
	@Deprecated
	private Boolean isRun(int batchType, Long outTime) {
		//redisUtil.del(String.valueOf(batchType));
		//redisUtil.hset(String.valueOf(batchType),getIp(), getIp(),outTime);
		redisUtil.hset(getIp(), String.valueOf(batchType), getIp(), outTime);

		String ip1Val = (String) redisUtil.hget(ip1, String.valueOf(batchType));
		String ip2Val = (String) redisUtil.hget(ip2, String.valueOf(batchType));
		log.info("##################ip1Val=" + ip1Val);
		log.info("##################ip2Val=" + ip2Val);
		if (getIp().equals(ip1Val)) {
			log.info("当前时间=" + new Date());
			log.info("##################第一台服务器启动ip为：" + ip1Val);
			return true;
		} else if (StringUtils.isBlank(ip1Val) && !getIp().equals(ip1Val) && StringUtils.isNotBlank(ip2Val)) {
			log.info("当前时间=" + new Date());
			log.info("##################备用服务器启动ip为：" + ip2Val);
			return true;
		}
		return false;
	}

	/**
	 * 获得当前服务器IP-AWS云服务器可用
	 *
	 * @return java.lang.String
	 * @author Dong Xifu
	 * @date 2019/6/27 上午10:11
	 */
	@Deprecated
	// AWS云版本
	/*private static String getIp() {
		String ip = "";
		try {
			InetAddress address = InetAddress.getLocalHost();
			ip = address.getHostAddress();
			log.info("当前服务器IP地址>>>>>>" + ip);
		} catch (UnknownHostException e) {
			log.info("当前服务器IP地址获取失败>>>>>>" + e.getMessage());
			e.printStackTrace();
		}
		return ip;
	}*/
	// Tencent云版本
	private static String getIp() {
		String ip = "";
		try {
			//InetAddress address = InetAddress.getLocalHost();
			//ip = address.getHostAddress();
			Optional ipOptional = OuterIPUtil.getLocalIp4Address();
			Inet4Address ipTemp = (Inet4Address)ipOptional.get();
			ip = ipTemp.getHostAddress();
			log.info("【isRun】当前服务器IP地址>>>>>>" + ip);
		} catch (SocketException e){
			log.error("【isRunning】当前服务器IP地址获取失败>>>>>>" + e.getMessage());
			log.error("【isRunning】当前服务器IP地址获取失败>>>>>>", e);
		}
		return ip;
	}

	/**
	 * 判断当前服务器是否执行该定时任务
	 *
	 * @return java.lang.Boolean
	 * @Param batchType 定时任务编号
	 * @Param outTime redis过期时间
	 */
	private Boolean isRunning(int batchType, Long outTime) {
		log.info("【isRunning】验证任务【" + batchType + "】是否执行>>>>>>START");
		// 获取当前服务器IP地址
		String localIP = getIp(String.valueOf(batchType));
		if(localIP == null || "".equals(localIP)){
			return false;
		}

		// 将当前IP地址和定时任务编号存入redis，有效期为outTime
		redisUtil.hset(localIP, String.valueOf(batchType), localIP, outTime);

		// 肯定能够获取到自己的缓存IP，因为上面一句执行成功了!
		String ip1Val = (String) redisUtil.hget(ip1, String.valueOf(batchType));
		String ip2Val = (String) redisUtil.hget(ip2, String.valueOf(batchType));
		log.info("【isRunning】ip1Val>>>>>>" + ip1Val);
		log.info("【isRunning】ip1Val>>>>>>" + ip2Val);

		// 第一台服务器执行
		if (localIP.equals(ip1Val)) {
			log.info("【isRunning】当前时间：>>>>>>" + new Date());
			log.info("【isRunning】第一台服务器启动，IP为：>>>>>>" + ip1Val);
			return true;
		}
		// 本机IP地址不等于设定的第一台服务器IP：值不相等或者ip1Val为null
		else {
			// 没有取到第一台服务器的缓存IP地址，则等待60秒再去取（因为两台server同时执行任务，有可能造成取不到第一台的缓存IP）
			if(ip1Val == null || "".equals(ip1Val)){
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					log.error("【isRunning】InterruptedException：>>>>>>" + e.getMessage());
					e.printStackTrace();
					// 异常发生，直接按照第一台服务器执行了任务，第二台服务器不执行
					return false;
				}

				// 休眠60秒后再取ip1Val
				String ip1ValAgain = (String) redisUtil.hget(ip1, String.valueOf(batchType));

				// 取到了ip1Val
				if(ip1ValAgain != null && !"".equals(ip1ValAgain)){
					// 第一台服务器执行了定时任务，返回false
					return false;
				}
				// 没有取到ip1Val
				else {
					/*if(StringUtils.isBlank(ip1ValAgain) && !localIP.equals(ip1ValAgain) && StringUtils.isNotBlank(ip2Val)){
						return true;
					}*/
					log.info("【isRunning】当前时间：>>>>>>" + new Date());
					log.info("【isRunning】备用服务器启动，IP为：>>>>>>" + ip2Val);
					return true;
				}
			}
		}

		/*// 备用服务器执行任务
		// 第一台服务器缓存ip为空，且本机IP地址不等于设定的一台服务器IP，且第二台服务器的缓存IP不为空
		else if (StringUtils.isBlank(ip1Val) && !localIP.equals(ip1Val) && StringUtils.isNotBlank(ip2Val)) {
			logger.info("【isRunning】当前时间：>>>>>>" + new Date());
			logger.info("【isRunning】备用服务器启动，IP为：>>>>>>" + ip2Val);
			return true;
		}*/

		return false;
	}

	/**
	 * 获得当前服务器IP-AWS云服务器可用
	 * 20190729停用
	 *
	 * @return java.lang.String
	 */
	@Deprecated
	private static String getIpAWS(String batchType) {
		String ip = "";
		try {
			InetAddress address = InetAddress.getLocalHost();
			ip = address.getHostAddress();
			log.info("【isRunning】【" + batchType + "】当前服务器IP地址>>>>>>" + ip);
		} catch (UnknownHostException e) {
			log.info("【isRunning】【" + batchType + "】当前服务器IP地址获取失败>>>>>>" + e.getMessage());
			e.printStackTrace();
		}
		return ip;
	}

	/**
	 * 获得当前服务器IP-Tencent云服务器可用
	 *20190729启用
	 *
	 * @return java.lang.String
	 */
	private static String getIp(String batchType) {
		String ip = "";
		try {
			//InetAddress address = InetAddress.getLocalHost();
			//ip = address.getHostAddress();
			Optional ipOptional = OuterIPUtil.getLocalIp4Address();
			Inet4Address ipTemp = (Inet4Address)ipOptional.get();
			ip = ipTemp.getHostAddress();
			log.info("【isRunning】【" + batchType + "】当前服务器IP地址>>>>>>" + ip);
		} catch (SocketException e){
			log.error("【isRunning】【" + batchType + "】当前服务器IP地址获取失败>>>>>>" + e.getMessage());
			log.error("【isRunning】【" + batchType + "】当前服务器IP地址获取失败>>>>>>", e);
		}
		return ip;
	}
	
	

	/**
	 * ONLY TEST BY DIKIM
	 * 	
	 */
	
//	@Scheduled(cron = "0 */1 * * * ?")
//	@Scheduled(cron = "0 0/5 * * * ?")
	public void manualGetWxToken(){
		log.info("=========> BATCH manualGetWxToken <=========");
		log.info("执行token任务时间=" + new Date());
		List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();

		for (AppInfoWithBLOBs bloBs : appInfoList) {
			Thread thread = new Thread(new WxUpTokenThread(wechatService, bloBs.getSysId()));
			thread.start();
		}
	}	

//	@Scheduled(cron = "0 0 1 * * *")
//	@Scheduled(cron = "0 3/5 * * * ?")
	public void manualBatchUser() {

		// by dikim only test on 20190715
		String token = "";

		JSONObject params = new JSONObject();
		params.put("orderId", "1000000310");
        
		try {
			
			String result = HttpClientUtils.requestGet("http://manager.bacommerce.co.kr/eorder/manager/api/v1/login?account=jeju001&password=admin1234");
			log.info("++++++++++++++++ Result postData : \n" + result);
			Map<String,String> requestMap = new Gson().fromJson(result, Map.class);
			String tokenLogin = requestMap.get("token");

			String postData = HttpClientUtils.requestPost("http://manager.bacommerce.co.kr/eorder/manager/api/v1/command/getAccessToken", params, tokenLogin);
			log.info("++++++++++++++++ Result postData : \n" + postData);

		    JsonElement jelement = new JsonParser().parse(postData);
		    JsonObject  jobject = jelement.getAsJsonObject();
		    JsonObject  jobject2 = jobject.getAsJsonObject("biz_data");
		    
		    token = jobject2.get("return_msg").getAsString();

			log.info("++++++++++++++++ token : \n" + token);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        String runFlag = (String) redisUtil.get("manualBatchUser");
        
		List<AppInfoWithBLOBs> appInfoList = wechatService.selectBatchAppInfoList();
		
		if ("Y".equals(runFlag)) {
			log.info("=========> Not Run BATCH manualBatchUser <========= runFlag : " + runFlag);
		}
		else {
			redisUtil.set("manualBatchUser","Y",60*119);

			log.info("=========> BATCH manualBatchUser <=========");
			
			for (AppInfoWithBLOBs bloBs : appInfoList) {
				
				// 锁检查
				boolean isRun = wechatService.queryWxBatchLock(bloBs.getSysId(), BatchEnum.batchUser.getType());
				// 非加锁状态
				if (!isRun) {
					Thread thread = new Thread(new SyncUserManualThread(wechatService, weixinUserService, bloBs.getSysId(), bloBs.getShopId(), token, redisUtil));
					thread.start();
				} else {
					log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||batchInsertWxUse Cust|||||||||||||||||||||||||||||||||||||||||||||||||||");
				}
			}
		}
	}

	@Autowired
	private RedissonUtil redissonUtil;

	/**
	 * 	测试Redisson，每格1分钟执行
	 */
	@Scheduled(cron = "0 0/2 * * * ?")
	public void redissonLockCorrect() {
		log.info("定时任务启动");
		String batchKey = "REDISSON_LOCK_KEY_CORRECT";
		RLock lock = redissonUtil.getRLock(batchKey);
		boolean getLock = false;
		try {
			// 加锁时间一定要小于调度时间。此处零等待，加锁60秒。
			if (getLock = lock.tryLock(0, 60, TimeUnit.SECONDS)) {
				//执行的业务代码
				log.info("===================================执行定时任务啦啦啦啦啦=====================================");
			} else {
				log.info("Redisson分布式锁没有获取到锁:{},ThreadName :{}", batchKey, Thread.currentThread().getName());
			}
		} catch (InterruptedException e) {
			log.error("Redisson获取分布式锁异常", e);
		} finally {
			if (!getLock) {
				return;
			}

			try {
				// 休眠40秒再释放锁，防止另一台服务器因为某些原因（服务器时间差）得到锁而执行
				Thread.sleep(40000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			lock.unlock();
			log.info("Redisson分布式锁释放锁:{},ThreadName :{}", batchKey, Thread.currentThread().getName());
		}
		log.info("定时任务结束");
	}
}