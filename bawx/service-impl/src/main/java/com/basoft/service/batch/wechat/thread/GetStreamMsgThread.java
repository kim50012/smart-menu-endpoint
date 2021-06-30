package com.basoft.service.batch.wechat.thread;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.basoft.core.ware.wechat.domain.statistic.WxIfStreamMsgStatsData;
import com.basoft.core.ware.wechat.util.WeixinStatisticUtils;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.enumerate.BatchEnum;

/**
 * 获取消息发送天数据和分布数据线程
 */
public class GetStreamMsgThread implements Runnable {
	private final transient Log logger = LogFactory.getLog(this.getClass());

	private WechatService wechatService;

	private static final Byte TIME_TYPE = 0;// 日报

	@SuppressWarnings("unused")
	private static final Byte TIME_TYPE_HOUR = 1;// 小时报

	@SuppressWarnings("unused")
	private static final Byte TIME_TYPE_WEEK = 2;// 周报

	private static final Byte TIME_TYPE_DISTRIB = 4;// 分布数据

	/**
	 * 系统ID
	 */
	private String sysId;

	/**
	 * SHOPID
	 */
	private Long shopId;

	/**
	 * 获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”
	 * （比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 */
	private String begin_date;

	/**
	 * 获取数据的结束日期，end_date允许设置的最大值为昨日
	 */
	private String end_date;

	/**
	 * 获取图文群发每日数据线程方法
	 * 
	 * @param weixinService
	 * @param sysId 系统ID
	 * @param shopId SHOPID
	 * @param begin_date 获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”
	 *            （比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 */
	public GetStreamMsgThread(WechatService wechatService, String sysId, Long shopId, String begin_date, String end_date) {
		this.wechatService = wechatService;
		this.sysId = sysId;
		this.shopId = shopId;
		this.begin_date = begin_date;
		this.end_date = end_date;
	}

	@Override
	public void run() {
		try {
			logger.info("=================GetStreamMsgThread.run() =====================");
			String token = wechatService.getAccessToken(sysId);
			logger.info("sysId==" + sysId + ", begin_date==" + begin_date + ", end_date==" + end_date + ", token==" + token);
			// 天数据
			List<WxIfStreamMsgStatsData> dayList = WeixinStatisticUtils.getUpstreamMsg(token, begin_date, end_date);
			if (dayList != null && !dayList.isEmpty()) {
				logger.info("dayListSize="+dayList.size());
				// TIME_TYPE 0
				wechatService.insertBatchStreamMsgList(dayList, shopId, TIME_TYPE);
			}
			// 分布数据
			List<WxIfStreamMsgStatsData> distList = WeixinStatisticUtils.getUpstreamMsgDist(token, begin_date, end_date);
			if (distList != null && !distList.isEmpty()) {
				logger.info("distListSize="+distList.size());
				// TIME_TYPE 4
				wechatService.insertBatchStreamMsgList(distList, shopId, TIME_TYPE_DISTRIB);
			}
			
			//定时任务解锁
			wechatService.updateWxBatchLockEnd(sysId, BatchEnum.batchInsertStreamMsgYesToday.getType());
			logger.info("=================GetStreamMsgThread.end() =====================");
		} catch (Exception e) {
			//定时任务解锁
			wechatService.updateWxBatchLockEnd(sysId, BatchEnum.batchInsertStreamMsgYesToday.getType());
			
			logger.error(e.getMessage());
		}
	}
}