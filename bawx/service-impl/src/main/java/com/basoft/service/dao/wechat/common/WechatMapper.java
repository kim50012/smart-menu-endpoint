package com.basoft.service.dao.wechat.common;

import com.basoft.core.ware.wechat.domain.*;
import com.basoft.core.ware.wechat.domain.msg.*;
import com.basoft.core.ware.wechat.domain.statistic.ArticleDetail;
import com.basoft.core.ware.wechat.domain.statistic.ArticleGroupDetails;
import com.basoft.core.ware.wechat.domain.statistic.ArticleSummary;
import com.basoft.core.ware.wechat.domain.statistic.WxIfStreamMsgStatsData;
import com.basoft.core.ware.wechat.domain.user.SessionMember;
import com.basoft.core.ware.wechat.domain.user.WXUser;
import com.basoft.service.entity.customer.cust.Cust;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface WechatMapper {
	/**
	 * 根据ID获取支付账号信息
	 * 
	 * @param key ID
	 * @return MchInfo 支付账号信息
	 */
	MchInfo selectMchInfoByKey(String key);

	/**
	 * 根据shopId获取支付账号信息
	 * 
	 * @param shopId
	 * @return MchInfo 支付账号信息
	 */
	MchInfo selectMchInfoByShopId(Long shopId);

	/**
	 * 获取 access token
	 * 
	 * @param sysId
	 * @return String access token
	 */
	String getAccessToken(String sysId);

	/**
	 * 存储更新后的access token
	 * 
	 * @param accessToken
	 * @return
	 */
	int saveAccessToken(AccessToken accessToken);

	/**
	 * 获取 api ticket
	 * 
	 * @param sysId
	 * @return String api ticket
	 */
	String getApiTicket(String sysId);

	/**
	 * 存储api ticket
	 *
	 * @param appInfo
	 * @param apiTicket
	 */
	void saveApiTicket(AppInfo appInfo, ApiTicket apiTicket);

	/**
	 * @param executionLog
	 */
	void insertExecutionLog(ExecutionLog executionLog);

	/**
	 * @param weixinSessionPageExeLog
	 */
	void insertWeixinSessionPageExeLog(WeixinSessionPageExeLog weixinSessionPageExeLog);

	/**
	 * @param weixinPageExecutionLog
	 */
	void insertWeixinPageExecutionLog(WeixinPageExecutionLog weixinPageExecutionLog);

	/**
	 * @param map
	 */
	void insertMenuClickLogging(Map<String, Object> map);

	/**
	 * @param shopId
	 * @return
	 */
	String selectWechatNoByShopId(Integer shopId);

	/**
	 * @param openid
	 * @return
	 */
	SessionMember getSessionMemberById(String openid);

	/**
	 * @param userId
	 * @return
	 */
	SessionMember getSessionMemberByCustId(Long userId);

	/**
	 * @param openid
	 * @return
	 */
	WXUser getUserInfoByOpenid(String openid);

	/**
	 * 用户关注时，存储用户信息
	 * 
	 * @param user
	 */
	void userSubscribe(WXUser user);

	/**
	 * 用户取消关注时，处理用户信息
	 * 
	 * @param user
	 */
	void userUnsubscribe(WXUser user);
	
	/**
	 * 批量更新取消关注用户的关注状态为0
	 *
	 * @param sysId
	 * @param ifStartDate
	 */
	void batchUserUnsubscribe(@Param("sysId") String sysId, @Param("ifStartDate") Date ifStartDate);

	/**
	 * 存储客户信息（用户的客户表）
	 * 
	 * @param cust
	 */
	void saveCust(Cust cust);

	public Long insertTextMsg(Text text);

	public long insertImageMsg(Image image);

	public long insertVoiceMsg(Voice vocie);

	public long insertVideoMsg(Video video);

	public long insertLocationMsg(Location location);

	public long insertLinkMsg(Link link);

	public long insertEventLocation(EventLocation location);

	/**
	 * 关注回复
	 * 
	 * @param shopId
	 * @return
	 */
	List<Map<String, Object>> getFocusMessage(Long shopId);

	/**
	 * 关键字回复
	 * 
	 * @param shopId
	 * @param keyWord
	 * @return
	 */
	List<Map<String, Object>> getKeyWordMessage(@Param("shopId") Long shopId, @Param("keyWord") String keyWord);

	/**
	 * 自动回复
	 * 
	 * @param shopId
	 * @return
	 */
	List<Map<String, Object>> getAutoMessage(@Param("shopId") Long shopId);

	/**
	 *更新media
	 *
	 * @param fileId
	 * @param mediaId
	 * @return
	 */
	int updateMediaId(@Param("fileId") Long fileId, @Param("mediaId") String mediaId);

	
	// TODO
	void insertQRCodeScanedResult(Long shopId, String fromUserName, Integer valueOf);

	/**
	 * 电子菜单eorder return message
	 * 
	 * @param sysId
	 * @param keyWord
	 * @return
	 */
	List<Map<String, Object>> getReturnMessage(@Param("sysId") String sysId
			, @Param("keyWord") String keyWord, @Param("deployDomainPrefix") String deployDomainPrefix);

	List<Map<String, Object>> getClickEventReturnMessage(AppInfo appInfo, Integer messageId);

	/**
	 * 群发统计：图文阅读人数,分享人数,收藏人数等等(单篇图文统计) 
	 * 表：wx_if_message_stats_data
	 * 
	 * @param summary
	 */
	void insertArticleSummary(ArticleSummary summary);

	void updateArticleTargetUser(ArticleDetail detail);


	/**
	 * details id
	 * 
	 * @return
	 */
	Long getMaxWxIfMsgStasDetailId();

	/**
	 * ArticleSummaryStatics
	 * 
	 * @param
	 */
	void insertArticleSummaryStatics(ArticleGroupDetails details);

	/**
	 * 用户发送消息周统计：获取ID
	 */
	public Long getMaxWxIfMsgWeekId();

	/**
	 * 用户发送消息周统计
	 * 
	 * @param wxIfStreamMsgStatsData
	 */
	void insertBatchStreamMsgWeekList(WxIfStreamMsgStatsData wxIfStreamMsgStatsData);

	/**
	 * 用户发送消息统计
	 * 
	 * @param wxIfStreamMsgStatsData
	 */
	void insertBatchStreamMsgList(WxIfStreamMsgStatsData wxIfStreamMsgStatsData);

	/**
	 * 查询定时任务锁状态
	 * 
	 * @param sysId
	 * @param batchType
	 * @return
	 */
	String selectWxBatchLock(@Param("sysId") String sysId, @Param("batchType") int batchType);

	/**
	 * 新增定时任务锁-加锁状态
	 * 
	 * @param sysId
	 * @param batchType
	 * @param expires
	 */
	void insertWxBatchLockStart(@Param("sysId") String sysId, @Param("batchType") int batchType, @Param("expires") int expires);

	/**
	 * 更新定时任务锁-加锁状态
	 *
	 * @param sysId
	 * @param batchTyp
	 * @param expirese
	 */
	void updateWxBatchLockStart(@Param("sysId") String sysId, @Param("batchType") int batchTyp, @Param("expires") int expirese);

	/**
	 * 更新定时任务锁-非加锁状态
	 * 
	 * @param sysId
	 * @param batchType
	 * @return
	 */
	int updateWxBatchLockEnd(@Param("sysId") String sysId, @Param("batchType") int batchType);

	/**
	 * 关注用户
	 * 
	 * @param 
	 * @return
	 */
	List<Map<String, Object>> getListWxUser();



	/**
	 * 根据用户openid查询该用户是否关注过公众号（曾经或现在）
	 *
	 * @param fromUserName
	 * @return
	 */
	List<Map<String, Object>> getWxUserByOpenid(@Param("fromUserName") String fromUserName);

	/**
	 * 根据代理商ID和用户openid查询是否存在绑定关系
	 *
	 * @param fromUserName
	 * @param agentId
	 * @return
	 */
	List<Map<String, Object>> queryBindInfoByOpenidAndAgent(@Param("fromUserName") String fromUserName, @Param("agentId") String agentId);

	/**
	 * 根据用户openid查询是否存在绑定关系
	 *
	 * @param fromUserName
	 * @return
	 */
	List<Map<String, Object>> queryBindInfoByOpenid(@Param("fromUserName") String fromUserName);

	/**
	 * 绑定代理商和用户的关系
	 *
	 * @param fromUserName
	 * @param agentId
	 * @return
	 */
	int bandAgentAndOpenid(@Param("fromUserName") String fromUserName, @Param("agentId") String agentId);

	/**
	 * 根据代理商ID查询代理商信息
	 *
	 * @param agentId
	 * @return
	 */
	List<Map<String, Object>> getAgentListByAgentId(@Param("agentId") String agentId);
}