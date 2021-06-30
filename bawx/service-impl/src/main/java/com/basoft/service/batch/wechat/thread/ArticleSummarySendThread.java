package com.basoft.service.batch.wechat.thread;

import com.basoft.core.ware.wechat.domain.statistic.ArticleSummary;
import com.basoft.core.ware.wechat.domain.statistic.ArticleTotal;
import com.basoft.core.ware.wechat.util.WeixinStatisticUtils;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.enumerate.BatchEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.List;

/**
 * 获取图文群发每日数据线程
 */
public class ArticleSummarySendThread implements Runnable {
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
	 * @param sysId	系统ID
	 * @param shopId SHOPID
	 * @param begin_date 获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”
	 * 					（比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 */
	public ArticleSummarySendThread(WechatService wechatService, String sysId, Long shopId, String begin_date, String end_date) {
		this.wechatService = wechatService;
		this.sysId = sysId;
		this.shopId = shopId;
		this.begin_date = begin_date;
		this.end_date = end_date;
	}

	@Override
	public void run() {
		try {
			logger.info("=================ArticleSummarySendThread.run() =====================");
			String token = wechatService.getAccessToken(sysId);
			logger.info("sysId==" + sysId + ", begin_date==" + begin_date + ", end_date==" + end_date + ", token==" + token);
			List<ArticleSummary> articleSummaryList = WeixinStatisticUtils.getArticleSummary(token, begin_date, end_date);


			Iterator<ArticleSummary> it = articleSummaryList.iterator();
			while(it.hasNext()){
				ArticleSummary article = it.next();
				if(article.getInt_page_read_count()<1&&article.getInt_page_read_user()<1
					&&article.getOri_page_read_count()<1&&article.getOri_page_read_user()<1&&article.getShare_count()<1
					&&article.getShare_user()<1&&article.getAdd_to_fav_count()<1&&article.getAdd_to_fav_user()<1){
					it.remove();
				}
			}
			if (articleSummaryList != null && !articleSummaryList.isEmpty()) {
				for (int i = 0; i < articleSummaryList.size(); i++) {
                    ArticleSummary article = articleSummaryList.get(i);
                    if(article.getInt_page_read_count()<1&&article.getInt_page_read_user()<1
                        &&article.getOri_page_read_count()<1&&article.getOri_page_read_user()<1&&article.getShare_count()<1
                        &&article.getShare_user()<1&&article.getAdd_to_fav_count()<1&&article.getAdd_to_fav_user()<1){
						logger.info("articleSummaryList_size"+articleSummaryList.size());
                    }else {
                        articleSummaryList.get(i).setShopId(shopId);
                    }

				}
				if(articleSummaryList.size()>0)
				    wechatService.insertArticleSummary(articleSummaryList, shopId);
			}

			for (ArticleSummary a : articleSummaryList) {
				List<ArticleTotal> totalList = WeixinStatisticUtils.getArticleTotal(token, a.getRef_date(), a.getRef_date());
				totalList.stream().forEach(t->{
					if(t.getMsgid().equals(a.getMsgid())){
						t.getDetails().stream().forEach(detail -> {
							wechatService.updateTargetUser(detail);
						});
					}
				});
			}
			
			//定时任务解锁
			wechatService.updateWxBatchLockEnd(sysId, BatchEnum.articleSummaryGroupHair.getType());
			logger.info("=================ArticleSummarySendThread.end() =====================");
		} catch (Exception e) {
			//定时任务解锁
			wechatService.updateWxBatchLockEnd(sysId, BatchEnum.articleSummaryGroupHair.getType());
			
			logger.error("======ren(" + shopId + ") error========");
			logger.error(e.getMessage());
		}
	}
}