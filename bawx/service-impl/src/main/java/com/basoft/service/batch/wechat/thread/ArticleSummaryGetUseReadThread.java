package com.basoft.service.batch.wechat.thread;

import com.basoft.core.ware.wechat.domain.statistic.ArticleSummary;
import com.basoft.core.ware.wechat.domain.statistic.UserShare;
import com.basoft.core.ware.wechat.util.WeixinStatisticUtils;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.enumerate.BatchEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * 获取图文群发每日数据线程
 */
public class ArticleSummaryGetUseReadThread implements Runnable {
	private final transient Log logger = LogFactory.getLog(this.getClass());

	private WechatService wechatService;

	/**
	 * 系统ID
	 */
	private String sysId;

	/**
	 * SHOPID
	 */
	private Long shopId;
	
	/**
	 *  获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”
	 * 	 （比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 */
	private String begin_date;
	
	/**
	 * 获取数据的结束日期，end_date允许设置的最大值为昨日
	 */
	private String end_date;

	/**
	 * 获取图文群发每日数据线程方法
	 * @param wechatService
	 * @param sysId	系统ID
	 * @param shopId SHOPID
	 * @param begin_date 获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”
	 * 					（比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 */
	public ArticleSummaryGetUseReadThread(WechatService wechatService, String sysId, Long shopId, String begin_date, String end_date) {
		this.wechatService = wechatService;
		this.sysId = sysId;
		this.shopId = shopId;
		this.begin_date = begin_date;
		this.end_date = end_date;
	}

	@Override
	public void run() {
		try {
			logger.info("=================ArticleSummaryGetUseReadThread.run() =====================");
			String token = wechatService.getAccessToken(sysId);
			logger.info("sysId==" + sysId + ", begin_date==" + begin_date + ", end_date==" + end_date + ", token==" + token);
			// 图文统计获取
			List<ArticleSummary> userReadList = WeixinStatisticUtils.getUserRead(token, begin_date, end_date);
			// 分享人数获取
			List<UserShare> shareList = WeixinStatisticUtils.getUserShare(token, begin_date, end_date);
			if (userReadList != null && !userReadList.isEmpty()) {
				for (int i = 0; i < userReadList.size(); i++) {
					userReadList.get(i).setShopId(shopId);
				}
				wechatService.insertArticleSummaryStatics(userReadList, shopId, shareList);
			}
			
			//定时任务解锁
			wechatService.updateWxBatchLockEnd(sysId, BatchEnum.articleSummaryDetailStatics.getType());
			logger.info("=================ArticleSummaryGetUseReadThread.end() =====================");
		} catch (Exception e) {
			
			//定时任务解锁
			wechatService.updateWxBatchLockEnd(sysId, BatchEnum.articleSummaryDetailStatics.getType());
			logger.error(e.getMessage());
		}
	}
}