package com.basoft.service.definition.wechat.user;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.basoft.core.ware.wechat.domain.user.SessionMember;
import com.basoft.core.ware.wechat.domain.user.UserReturn;
import com.basoft.core.ware.wechat.domain.user.WXUser;

public interface WeixinUserService {
	/**
	 * @param openid
	 * @return
	 */
	public SessionMember getSessionMemberById(String openid);

	/**
	 * @param userId
	 * @return
	 */
	public SessionMember getSessionMemberByCustId(Long userId);

	/**
	 * 根据ID获取用户信息
	 * 
	 * @param openid
	 * @return
	 */
	public WXUser getUserInfoByOpenid(String openid);

	/**
	 * 用户关注并同步用户信息
	 * 
	 * @param token
	 * @param user
	 * @throws Exception
	 */
	public void userSubscribe(String token, WXUser user) throws Exception;

	/**
	 * 【批量定时任务】用户关注并同步用户信息
	 * 
	 * @param shopId
	 * @param token
	 * @param user
	 * @throws Exception
	 */
	public void userSubscribe(Long shopId, WXUser user, UserReturn userReturn) throws Exception;

	/**
	 * 用户取消关注并同步信息
	 * 
	 * @param user
	 * @return
	 */
	public void userUnsubscribe(WXUser user);

	/**
	 * 批量用户取消关注
	 * 
	 * @param sysId
	 * @param ifStartDate
	 */
	public void batchUserUnsubscribe(String sysId, Date ifStartDate) throws Exception;

	/**
	 * 关注用户
	 * 
	 * @param sysId
	 * @param ifStartDate
	 */
	public List<Map<String, Object>> getListWxUser();
	
}