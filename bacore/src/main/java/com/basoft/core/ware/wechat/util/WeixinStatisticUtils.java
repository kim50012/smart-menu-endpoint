package com.basoft.core.ware.wechat.util;

import java.util.List;

import com.basoft.core.ware.wechat.domain.statistic.ArticleSummary;
import com.basoft.core.ware.wechat.domain.statistic.ArticleTotal;
import com.basoft.core.ware.wechat.domain.statistic.GetArticleSummaryReturn;
import com.basoft.core.ware.wechat.domain.statistic.GetArticleTotalReturn;
import com.basoft.core.ware.wechat.domain.statistic.GetInterfaceSummaryHourReturn;
import com.basoft.core.ware.wechat.domain.statistic.GetInterfaceSummaryReturn;
import com.basoft.core.ware.wechat.domain.statistic.GetUpstreamMsgReturn;
import com.basoft.core.ware.wechat.domain.statistic.GetUserCumulateReturn;
import com.basoft.core.ware.wechat.domain.statistic.GetUserShareReturn;
import com.basoft.core.ware.wechat.domain.statistic.GetUserSummaryReturn;
import com.basoft.core.ware.wechat.domain.statistic.InterfaceSummary;
import com.basoft.core.ware.wechat.domain.statistic.InterfaceSummaryHour;
import com.basoft.core.ware.wechat.domain.statistic.UpstreamMsg;
import com.basoft.core.ware.wechat.domain.statistic.UserCumulate;
import com.basoft.core.ware.wechat.domain.statistic.UserShare;
import com.basoft.core.ware.wechat.domain.statistic.UserSummary;
import com.basoft.core.ware.wechat.domain.statistic.WxIfStreamMsgStatsData;
import com.basoft.core.ware.wechat.domain.statistic.WxIfStreamMsgStatsDataReturn;
import com.basoft.core.ware.wechat.exception.WeixinErrorException;
import com.google.gson.Gson;

import net.sf.json.JSONObject;

/** 
* 数据统计接口工具类
*/
public class WeixinStatisticUtils extends WeixinBaseUtils {
	/************************************************************************************/
	/******************************* 用户分析数据接口 *******************************/
	/************************************************************************************/
	/**
	 * 获取用户增减数据（getusersummary）
	 */
	private static final String DATACUBE_GET_USERS_UMMARY_URL = "https://api.weixin.qq.com/datacube/getusersummary?access_token=ACCESS_TOKEN";

	/**
	 * 获取累计用户数据（getusercumulate）
	 */
	private static final String DATACUBE_GET_USER_CUMULATE_URL = "https://api.weixin.qq.com/datacube/getusercumulate?access_token=ACCESS_TOKEN";

	/************************************************************************************/
	/******************************* 图文分析数据接口 *******************************/
	/************************************************************************************/
	/**
	 * 获取图文群发每日数据（getarticlesummary）
	 */
	private static final String DATACUBE_GET_ARTICLE_SUMMARY_URL = "https://api.weixin.qq.com/datacube/getarticlesummary?access_token=ACCESS_TOKEN";

	/**
	 * 获取图文群发总数据（getarticletotal）
	 */
	private static final String DATACUBE_GET_ARTICLE_TOTAL_URL = "https://api.weixin.qq.com/datacube/getarticletotal?access_token=ACCESS_TOKEN";

	/**
	 * 获取图文统计数据（getuserread）
	 */
	private static final String DATACUBE_GET_USER_READ_URL = "https://api.weixin.qq.com/datacube/getuserread?access_token=ACCESS_TOKEN";

	/**
	 * 获取图文统计分时数据（getuserreadhour）
	 */
	private static final String DATACUBE_GET_USER_READ_HOUR_URL = "https://api.weixin.qq.com/datacube/getuserreadhour?access_token=ACCESS_TOKEN";

	/**
	 * 获取图文分享转发数据（getusershare）
	 */
	private static final String DATACUBE_GET_USER_SHARE_URL = "https://api.weixin.qq.com/datacube/getusershare?access_token=ACCESS_TOKEN";

	/**
	 * 获取图文分享转发分时数据（getusersharehour）
	 */
	private static final String DATACUBE_GET_USER_SHARE_HOUR_URL = "https://api.weixin.qq.com/datacube/getusersharehour?access_token=ACCESS_TOKEN";

	/************************************************************************************/
	/******************************* 消息分析数据接口 *******************************/
	/************************************************************************************/
	/**
	 * 获取消息发送概况数据（getupstreammsg）
	 */
	private static final String DATACUBE_GET_UPSTREAM_MSG_URL = "https://api.weixin.qq.com/datacube/getupstreammsg?access_token=ACCESS_TOKEN";

	/**
	 * 获取消息分送分时数据（getupstreammsghour）
	 */
	private static final String DATACUBE_GET_UPSTREAM_MSG_HOUR_URL = "https://api.weixin.qq.com/datacube/getupstreammsghour?access_token=ACCESS_TOKEN";

	/**
	 * 获取消息发送周数据（getupstreammsgweek）
	 */
	private static final String DATACUBE_GET_UPSTREAM_MSG_WEEK_URL = "https://api.weixin.qq.com/datacube/getupstreammsgweek?access_token=ACCESS_TOKEN";

	/**
	 * 获取消息发送月数据（getupstreammsgmonth）
	 */
	private static final String DATACUBE_GET_UPSTREAM_MSG_MONTH_URL = "https://api.weixin.qq.com/datacube/getupstreammsgmonth?access_token=ACCESS_TOKEN";

	/**
	 * 获取消息发送分布数据（getupstreammsgdist）
	 */
	private static final String DATACUBE_GET_UPSTREAM_MSG_DIST_URL = "https://api.weixin.qq.com/datacube/getupstreammsgdist?access_token=ACCESS_TOKEN";

	/**
	 * 获取消息发送分布周数据（getupstreammsgdistweek）
	 */
	private static final String DATACUBE_GET_UP_STREAM_MSG_DIST_WEEK_URL = "https://api.weixin.qq.com/datacube/getupstreammsgdistweek?access_token=ACCESS_TOKEN";

	/**
	 * 获取消息发送分布月数据（getupstreammsgdistmonth）
	 */
	private static final String DATACUBE_GET_UPSTREAM_MSG_DIST_MONTH_URL = "https://api.weixin.qq.com/datacube/getupstreammsgdistmonth?access_token=ACCESS_TOKEN";

	/************************************************************************************/
	/******************************* 接口分析数据接口 *******************************/
	/************************************************************************************/
	/**
	 * 获取接口分析数据（getinterfacesummary）
	 */
	private static final String DATACUBE_GET_INTERFACE_SUMMARY_URL = "https://api.weixin.qq.com/datacube/getinterfacesummary?access_token=ACCESS_TOKEN";

	/**
	 * 获取接口分析分时数据（getinterfacesummaryhour)
	 */
	private static final String DATACUBE_GET_INTERFACE_SUMMARY_HOUR_URL = "https://api.weixin.qq.com/datacube/getinterfacesummaryhour?access_token=ACCESS_TOKEN";
	

	/************************************************************************************/
	/*******************************    用户分析数据接口      *******************************/
	/************************************************************************************/
	/**
	 * 用户分析数据接口 - 获取用户增减数据（getusersummary） - 最大时间跨度 7天
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date
	 *            获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”（比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<UserSummary> getUserSummary(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_USERS_UMMARY_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		GetUserSummaryReturn returns = new Gson().fromJson(result, GetUserSummaryReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}

	/**
	 * 用户分析数据接口 - 获取累计用户数据（getusercumulate） - 最大时间跨度 7天
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date
	 *            获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”（比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<UserCumulate> getUserCumulate(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_USER_CUMULATE_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		GetUserCumulateReturn returns = new Gson().fromJson(result, GetUserCumulateReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}
	
	/************************************************************************************/
	/*******************************    图文分析数据接口      *******************************/
	/************************************************************************************/
	/**
	 * 图文分析数据接口 - 获取图文群发每日数据 最大时间跨度 1
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date 获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”
	 *            （比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<ArticleSummary> getArticleSummary(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_ARTICLE_SUMMARY_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		GetArticleSummaryReturn returns = new Gson().fromJson(result, GetArticleSummaryReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}

	/**
	 * 图文分析数据接口 - 获取图文群发总数据 最大时间跨度 1
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date 获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”
	 *            （比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<ArticleTotal> getArticleTotal(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_ARTICLE_TOTAL_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		GetArticleTotalReturn returns = new Gson().fromJson(result, GetArticleTotalReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}

	/**
	 * 图文分析数据接口 - 获取图文统计数据 最大时间跨度 3
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date 获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”
	 *            （比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<ArticleSummary> getUserRead(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_USER_READ_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		GetArticleSummaryReturn returns = new Gson().fromJson(result, GetArticleSummaryReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}

	/**
	 * 图文分析数据接口 - 获取图文统计分时数据 最大时间跨度 1
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date 获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”
	 *            （比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<ArticleSummary> getUserReadHour(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_USER_READ_HOUR_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		GetArticleSummaryReturn returns = new Gson().fromJson(result, GetArticleSummaryReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}

	/**
	 * 图文分析数据接口 - 获取图文分享转发数据 最大时间跨度 7
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date 获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”
	 *            （比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<UserShare> getUserShare(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_USER_SHARE_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		GetUserShareReturn returns = new Gson().fromJson(result, GetUserShareReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}

	/**
	 * 图文分析数据接口 - 获取图文分享转发分时数据 最大时间跨度 1
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date 获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”
	 *            （比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<UserShare> getUserShareHour(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_USER_SHARE_HOUR_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		GetUserShareReturn returns = new Gson().fromJson(result, GetUserShareReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}

	/************************************************************************************/
	/*******************************    消息分析数据接口      *******************************/
	/************************************************************************************/
	/**
	 * 消息分析数据接口 - 获取消息发送概况数据（getupstreammsg） - 最大时间跨度 7天
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date
	 *            获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”（比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<WxIfStreamMsgStatsData> getUpstreamMsg(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_UPSTREAM_MSG_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WxIfStreamMsgStatsDataReturn returns = new Gson().fromJson(result, WxIfStreamMsgStatsDataReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}

	/**
	 * 消息分析数据接口 - 获取消息分送分时数据（getUpstreamMsgHour） - 最大时间跨度1天
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date
	 *            获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”（比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<WxIfStreamMsgStatsData> getUpstreamMsgHour(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_UPSTREAM_MSG_HOUR_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WxIfStreamMsgStatsDataReturn returns = new Gson().fromJson(result, WxIfStreamMsgStatsDataReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}

	/**
	 * 消息分析数据接口 - 获取消息发送周数据（getupstreammsgweek） - 最大时间跨度 30天
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date
	 *            获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”（比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<WxIfStreamMsgStatsData> getUpstreamMsgWeek(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_UPSTREAM_MSG_WEEK_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WxIfStreamMsgStatsDataReturn returns = new Gson().fromJson(result, WxIfStreamMsgStatsDataReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}
	
	/**
	 * 消息分析数据接口 - 获取消息发送月数据（getupstreammsgmonth） - 最大时间跨度 30天
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date
	 *            获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”（比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<UpstreamMsg> getUpstreamMsgMonth(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_UPSTREAM_MSG_MONTH_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		GetUpstreamMsgReturn returns = new Gson().fromJson(result, GetUpstreamMsgReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}

	/**
	 * 消息分析数据接口 - 获取消息发送分布数据（getupstreammsgdist） - 最大时间跨度 15天
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date
	 *            获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”（比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<WxIfStreamMsgStatsData> getUpstreamMsgDist(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_UPSTREAM_MSG_DIST_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WxIfStreamMsgStatsDataReturn returns = new Gson().fromJson(result, WxIfStreamMsgStatsDataReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}

	/**
	 * 消息分析数据接口 - 获取消息发送分布周数据（getupstreammsgdistweek） - 最大时间跨度 30天
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date
	 *            获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”（比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<WxIfStreamMsgStatsData> getUpstreamMsgDistWeek(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_UP_STREAM_MSG_DIST_WEEK_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		WxIfStreamMsgStatsDataReturn returns = new Gson().fromJson(result, WxIfStreamMsgStatsDataReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}

	/**
	 * 消息分析数据接口 - 获取消息发送分布月数据（getupstreammsgdistmonth） - 最大时间跨度 30天
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date
	 *            获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”（比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<UpstreamMsg> getUpstreamMsgDistMonth(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_UPSTREAM_MSG_DIST_MONTH_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		GetUpstreamMsgReturn returns = new Gson().fromJson(result, GetUpstreamMsgReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}
	
	
	/************************************************************************************/
	/*******************************    接口分析数据接口     *******************************/
	/************************************************************************************/
	/**
	 * 接口分析数据接口 - 获取接口分析数据（getinterfacesummary） - 最大时间跨度 30天
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date
	 *            获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”（比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<InterfaceSummary> getInterfaceSummary(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_INTERFACE_SUMMARY_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		GetInterfaceSummaryReturn returns = new Gson().fromJson(result, GetInterfaceSummaryReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}
	
	/**
	 * 接口分析数据接口 - 获取接口分析分时数据（getinterfacesummaryhour） - 最大时间跨度 1天
	 * 
	 * @param access_token 调用接口凭证
	 * @param begin_date
	 *            获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”（比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	 * @param end_date 获取数据的结束日期，end_date允许设置的最大值为昨日
	 * @return
	 */
	public static List<InterfaceSummaryHour> getInterfaceSummaryHour(String access_token, String begin_date, String end_date) {
		String url = getInterfaceUrl(DATACUBE_GET_INTERFACE_SUMMARY_HOUR_URL, access_token);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("begin_date", begin_date);
		jsonObject.put("end_date", end_date);
		String result = HttpClientUtils.requestPost(url, jsonObject);
		GetInterfaceSummaryHourReturn returns = new Gson().fromJson(result, GetInterfaceSummaryHourReturn.class);
		if (returns.isError()) {
			throw new WeixinErrorException(returns);
		}
		return returns.getList();
	}
}
