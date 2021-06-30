package com.basoft.service.batch.wechat.thread;

import com.basoft.core.ware.wechat.domain.user.Data;
import com.basoft.core.ware.wechat.domain.user.UserListReturn;
import com.basoft.core.ware.wechat.domain.user.UserReturn;
import com.basoft.core.ware.wechat.domain.user.WXUser;
import com.basoft.core.ware.wechat.util.WeixinUserUtils;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.definition.wechat.user.WeixinUserService;
import com.basoft.service.enumerate.BatchEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 获取关注用户线程
 */
public class SyncUserThread implements Runnable {
	private final transient Log logger = LogFactory.getLog(this.getClass());

	private WechatService wechatService;

	private WeixinUserService weixinUserService;

	/**
	 * 系统ID
	 */
	private String sysId;

	/**
	 * SHOPID
	 */
	private Long shopId;

	/**
	 * 获取关注用户线程方法
	 * 
	 * @param wechatService
	 * @param sysId
	 * @param shopId
	 */
	public SyncUserThread(WechatService wechatService, WeixinUserService weixinUserService, String sysId, Long shopId) {
		this.wechatService = wechatService;
		this.weixinUserService = weixinUserService;
		this.sysId = sysId;
		this.shopId = shopId;
	}

	@Override
	public void run() {
		logger.info("=================SyncUserThread.run() =====================");
		Date ifStartDate = new Date(new Date().getTime() - 5 * 60 * 1000);
		logger.info("ifStartDate=" + ifStartDate);
		System.out.println(ifStartDate);
		try {
			logger.info("=================SyncUserThread.run(" + shopId + ") =====================");
			syscUser(null);
			logger.info("=================batchUserUnsubscribe " + shopId + " =====================");
			weixinUserService.batchUserUnsubscribe(sysId, ifStartDate);
			
			// 定时任务解锁
			wechatService.updateWxBatchLockEnd(sysId, BatchEnum.batchUser.getType());
		} catch (Exception e) {
			// 定时任务解锁
			wechatService.updateWxBatchLockEnd(sysId, BatchEnum.batchUser.getType());
			logger.error("3333" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 从微信公众平台拉取用户信息
	 * 
	 * @param openid 索引Openid
	 */
	private void syscUser(String openid){
		String token = wechatService.getAccessToken(sysId);
		logger.info("sysId==" + sysId + ", token==" + token);
		UserListReturn returns = WeixinUserUtils.getUserList(token, openid);
		if (returns.isSeccess()) {
			logger.info("["+ shopId + "]total==========" + returns.getTotal());
			logger.info("["+ shopId + "]count==========" + returns.getCount());
			logger.info("["+ shopId + "]next_openid====" + returns.getNext_openid());
			
			int index = 0;
			int count = returns.getCount();
			Data data = returns.getData();
			/////////////////////////////////////////////
//			for (String item : data.getOpenid()) {
//				logger.info("index/count =====>" + (++index) + "/" + count);
//				WXUser user = new WXUser(item);
//				user.setSysId(appInfo.getSysId());
//				try {
// 				weixinService.userSubscribe(appInfo, user);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
			/////////////////////////////////////////////////
			List<String> openidList = data.getOpenid();
			List<String> targetList = new ArrayList<String>();
			for (int i = 0; i < openidList.size(); i++) {
				targetList.add(openidList.get(i));
				if(((i + 1) % 100) == 0 || i == openidList.size() - 1){
					token = wechatService.getAccessToken(sysId);
					List<UserReturn> userReturnList = WeixinUserUtils.batchGetUserInfoByIds(token, targetList);
					for (UserReturn userReturn : userReturnList) {
						logger.info("["+ shopId + "]index/count =====>" + (++index) + "/" + count);
						WXUser user = new WXUser(userReturn.getOpenid());
						user.setSysId(sysId);
						try {
							//wechatService.userSubscribe(appInfo, user, userReturn);
							weixinUserService.userSubscribe(shopId, user, userReturn);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					logger.info("["+ shopId + "]++++++++++++++index=> + " + index + "+++++++++++++++");
					targetList.clear();
				}
			}
			
			///////////////////////////////////
			if(returns.getCount() == 10000 && returns.getTotal() > 10000){
				syscUser(returns.getNext_openid());
			}
		}
	}
}
