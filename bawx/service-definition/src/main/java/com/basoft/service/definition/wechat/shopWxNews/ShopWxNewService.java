package com.basoft.service.definition.wechat.shopWxNews;

import java.util.List;
import java.util.Map;

import com.basoft.service.dto.wechat.shopWxNew.NewsStatsDataDto;
import com.basoft.service.dto.wechat.shopWxNew.ShopWxNewDto;
import com.basoft.service.dto.wechat.shopWxNew.WxIfMessageStatsDetail;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsItemWithBLOBs;
import com.basoft.service.param.wechat.shopWxNews.SenNewsForm;
import com.basoft.service.param.wechat.shopWxNews.ShopWxNewsForm;
import com.basoft.service.param.wechat.shopWxNews.ShopWxNewsQueryParam;
import com.basoft.service.param.wechat.wxMessage.WxMessageQueryParam;
import com.github.pagehelper.PageInfo;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午11:03 2018/4/16
 **/
public interface ShopWxNewService {
	/**
	 * 素材列表头部
	 * 
	 * @param param
	 * @return
	 */
	public PageInfo<ShopWxNewDto> matterFindAll(ShopWxNewsQueryParam param);

	/**
	 * 查询msg头部及以下
	 * 
	 * @param msgId
	 * @param shopId
	 * @return
	 */
	public ShopWxNewDto getShopWxNewsItem(Long msgId, Long shopId);

	public ShopWxNewDto getShopWxNewsHead(Long msgId, Long shopId);

	// 根据msgId 查询顶部news
	public ShopWxNewDto getNewsByMsgId(Long msgId);

	public int insertOrUpdateShopNewsItem(ShopWxNewsForm form, Boolean headFlag, String edit);

	// 删除素材
	public int delShopWxNewsItem(Long newsId, Long shopId);

	// 查询单个news
	public ShopWxNewsItemWithBLOBs getShopWxNewsItemOne(Long shopId, Long msgId, Long newsId);

	// 删除整个素材
	int delMsgId(Long msgId, Long shopId);

	// 根据list添加素材
	public int insertOrUpdateShopNewsByList(ShopWxNewsForm form, Boolean headFlag);

	public int sendNews(SenNewsForm form);

	/**
	 * 图文消息群发时获取目标用户得OPENID列表
	 * 
	 * @param form
	 * @return
	 */
	public List<String> queryOpenIdWxNewsCust(SenNewsForm form);

	/**
	 * 根据公众号id查询具有预览图文消息权限的user列表
	 * 
	 * @param shopId
	 * @return
	 */
	public List<Map<String, Object>> selectNewsPreviewUsers(Long shopId);

	// 根据msgId查询news用于微信对接
	public List<Map<String, Object>> selectNewsListByMsgId(Long msgId, Long shopId);

	// 素材发送统计List(单篇)
	public List<NewsStatsDataDto> messageStatsDataList(Long shopId, String startTime, String endTime);

	// 素材发送统计Sum
	public NewsStatsDataDto messageStatsDataSum(Long shopId, String dayNum);

	// 素材发送统计全部图文list以天分组
	public List<NewsStatsDataDto> msgStatsDataListGroupDay(Long shopId, String startTime, String endTime);

	// 获取图文的原文阅读次数 朋友圈阅读次数 对话阅读次数 等等等
	public List<WxIfMessageStatsDetail> wxIfMessageStatsDetail(WxMessageQueryParam queryParam);

	// 获取图文的原文阅读次数 朋友圈阅读次数 对话阅读次数 等等等分页
	public PageInfo<WxIfMessageStatsDetail> wxIfMessageStatsDetailTable(WxMessageQueryParam queryParam);

	// 查询图文阅读总数
	public WxIfMessageStatsDetail wxIfMessageStatsSum(Long shopId, String startTime, String endTime, String timeType);

	/**
	 * 非图文（文本、图片、音频和视频）查询群发消息目标用户OPENID列表
	 * 
	 * @param form
	 * @return
	 */
	public List<String> queryCustOpenIdList(SenNewsForm form);
}