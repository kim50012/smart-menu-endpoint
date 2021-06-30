package com.basoft.service.definition.wechat.common;

import com.basoft.core.ware.wechat.domain.*;
import com.basoft.core.ware.wechat.domain.statistic.ArticleDetail;
import com.basoft.core.ware.wechat.domain.statistic.ArticleSummary;
import com.basoft.core.ware.wechat.domain.statistic.UserShare;
import com.basoft.core.ware.wechat.domain.statistic.WxIfStreamMsgStatsData;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * WechatService
 * 
 * @author BASOFT
 */
@Service
public interface WechatService {
	/**
	 * 随机生成公众账号基本信息 包括（sysid， token， EncodingAESKey）
	 * 
	 * @return
	 */
	public WechatAccount generateAccount();

	/**
	 * 获取全部公众账号信息
	 * 
	 * @return List<AppInfo> 公众账号信息List
	 */
	public List<AppInfoWithBLOBs> selectAllAppInfoList();

	/**
	 * 根据ID获取公众账号信息
	 * 
	 * @param key ID
	 * @return AppInfo 公众账号信息
	 */
	public AppInfoWithBLOBs selectAppInfoByKey(String key);

	/**
	 * 根据shopId获取公众账号信息
	 * 
	 * @param shopId
	 * @return AppInfo 公众账号信息
	 */
	public AppInfoWithBLOBs selectAppInfoByShopId(Long shopId);

	/**
	 * 根据公众账号原始ID获取公众账号信息
	 * 
	 * @param originalAppId
	 * @return AppInfo 公众账号信息
	 */
	public AppInfoWithBLOBs selectAppInfoByOriginalAppId(String originalAppId);

	/**
	 * 根据公众账号appID获取公众账号信息
	 * 
	 * @param appId 公众账号ID
	 * @return AppInfo 公众账号信息
	 */
	public AppInfoWithBLOBs selectAppInfoByAppId(String appId);

	/**
	 * 根据公众账号appID获取公众账号信息
	 * 
	 * @param appId 公众账号ID
	 * @return AppInfo 公众账号信息
	 */
	public AppInfoWithBLOBs selectAppInfoByAppIdAndNoException(String appId);

	/**
	 * 根据ID获取支付账号信息
	 * 
	 * @param key ID
	 * @return MchInfo 支付账号信息
	 */
	public MchInfo selectMchInfoByKey(String key);

	/**
	 * 根据shopId获取支付账号信息
	 * 
	 * @param shopId
	 * @return MchInfo 支付账号信息
	 */
	public MchInfo selectMchInfoByShopId(Long shopId);

	/**
	 * 获取 access token
	 * 
	 * @param appInfo
	 * @return String access token
	 */
	public String getAccessToken(AppInfo appInfo);

	/**
	 * 获取 access token
	 * 
	 * @param shopId
	 * @return String access token
	 */
	public String getAccessToken(Long shopId);

	/**
	 * 获取 access token
	 * 
	 * @param key
	 * @return String access token
	 */
	public String getAccessToken(String key);

	/**
	 * 更新微信token
	 *
	 * @return
	 * @Param
	 * @author Dong Xifu
	 * @date 2019/6/10 下午2:01
	 */
	public String setAccessToken(Long shopId);

	/**
	 * 获取 api ticket
	 * 
	 * @param appInfo
	 * @return String api ticket
	 */
	public String getApiTicket(AppInfo appInfo);

	/**
	 * 获取 api ticket
	 * 
	 * @param shopId
	 * @return String api ticket
	 */
	public String getApiTicket(Long shopId);

	/**
	 * 更新ticket
	 * @Param
	 * @return java.lang.String
	 * @author Dong Xifu
	 * @date 2019/6/8 上午7:45
	 */
	public String setApiTicket(Long shopId);

	/**
	 * 获取 api ticket
	 * 
	 * @param key
	 * @return String api ticket
	 */
	public String getApiTicket(String key);

	/**
	 *
	 * @param executionLog
	 */
	public void insertExecutionLog(ExecutionLog executionLog);

	/**
	 * @param weixinSessionPageExeLog
	 */
	public void insertWeixinSessionPageExeLog(WeixinSessionPageExeLog weixinSessionPageExeLog);

	/**
	 *
	 * @param weixinPageExecutionLog
	 */
	public void insertWeixinPageExecutionLog(WeixinPageExecutionLog weixinPageExecutionLog);

	/**
	 *
	 * @param map
	 */
	public void insertMenuClickLogging(Map<String, Object> map);

	/**
	 * 获取微信公众平台微信号
	 * 
	 * @param shopId
	 * @return
	 */
	public String getWechatNoByShopId(Integer shopId);

	/**
	 * 检查第三方公司接口账户的有效性
	 * 
	 * @param appInfo 公众账号信息
	 * @param ifId 接口ID
	 * @param ifPw 接口密码
	 */
	/*public void accountIsValid(AppInfo appInfo, String ifId, String ifPw);*/

	/**
	 * 群发统计：图文阅读人数,分享人数,收藏人数等等(单篇图文统计) 
	 * 表：wx_if_message_stats_data
	 * 
	 * @param list
	 * @param shopId
	 */
	public void insertArticleSummary(List<ArticleSummary> list, Long shopId);

	public void updateTargetUser(ArticleDetail detail);

	/**
	 * 群发统计： 图文阅读人数,分享人数,收藏人数等等(图文详细统计) 
	 * 表：WX_IF_MESSAGE_STATS_DETAIL
	 * 
	 * @param userReadList
	 * @param shopId
	 * @param userShareList
	 */
	public void insertArticleSummaryStatics(List<ArticleSummary> userReadList, Long shopId, List<UserShare> userShareList);

	/**
	 * 群发统计：图文阅读人数,分享人数,收藏人数等等(图文分时详细统计) 
	 * 表：wx_if_message_stats_data
	 * 
	 * @param userReadHourList
	 * @param shopId
	 * @param userShareList
	 */
	public void insertArticleSummaryHourStatics(List<ArticleSummary> userReadHourList, Long shopId, List<UserShare> userShareList);


	/**
	 * 用户发送消息统计
	 * 
	 * @param msgHourlist
	 * @param shopId
	 * @param timeType
	 */
	public void insertBatchStreamMsgList(List<WxIfStreamMsgStatsData> msgHourlist, Long shopId, Byte timeType);

	/**
	 * 用户发送消息周统计
	 * 
	 * @param list
	 * @param shopId
	 * @param timeType
	 */
	public void insertBatchStreamMsgWeekList(List<WxIfStreamMsgStatsData> list, Long shopId, Byte timeType);

	/**
	 * 查询开启定时任务的AppInfo列表
	 * 
	 * @return
	 */
	public List<AppInfoWithBLOBs> selectBatchAppInfoList();
	
	/**
	 * 查询定时任务锁状态
	 * 
	 * @return
	 */
	public boolean queryWxBatchLock(String sysId, int batchType);
	
	/**
	 * 更新定时任务锁状态为非加锁状态
	 * 
	 * @return
	 */
	public int updateWxBatchLockEnd(String sysId, int batchType);

	/**
	 * 扫描代理商二维码发送提示消息
	 *
	 * @param scanResult
	 * @param fromUserName
	 * @param token
	 * @param userBindInfo
	 */
	public void sendAgentQRCodeScanMsg(int scanResult, String fromUserName, String token, Map<String, Object> userBindInfo);

	/**
	 * 验证二维码有效性
	 *
	 * @param agentId
	 * @return
	 */
	public boolean checkAgentQRCodeUseful(String agentId);
}
