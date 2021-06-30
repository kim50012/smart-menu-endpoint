package com.basoft.service.dao.wechat.shopWxNews;

import com.basoft.service.dto.wechat.shopWxNew.NewsStatsDataDto;
import com.basoft.service.dto.wechat.shopWxNew.ShopWxNewDto;
import com.basoft.service.dto.wechat.shopWxNew.WxIfMessageStatsDetail;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsItem;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsItemExample;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsItemKey;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsItemWithBLOBs;
import com.basoft.service.param.wechat.wxMessage.WxMessageQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ShopWxNewsItemMapper {
	int countByExample(ShopWxNewsItemExample example);

	int deleteByExample(ShopWxNewsItemExample example);

	int deleteByPrimaryKey(ShopWxNewsItemKey key);

	int insert(ShopWxNewsItemWithBLOBs record);

	int insertSelective(ShopWxNewsItemWithBLOBs record);

	List<ShopWxNewsItemWithBLOBs> selectByExampleWithBLOBs(ShopWxNewsItemExample example);

	List<ShopWxNewsItem> selectByExample(ShopWxNewsItemExample example);

	ShopWxNewsItemWithBLOBs selectByPrimaryKey(ShopWxNewsItemKey key);

	int updateByExampleSelective(@Param("record") ShopWxNewsItemWithBLOBs record, @Param("example") ShopWxNewsItemExample example);

	int updateByExampleWithBLOBs(@Param("record") ShopWxNewsItemWithBLOBs record, @Param("example") ShopWxNewsItemExample example);

	int updateByExample(@Param("record") ShopWxNewsItem record, @Param("example") ShopWxNewsItemExample example);

	int updateByPrimaryKeySelective(ShopWxNewsItemWithBLOBs record);

	int updateByPrimaryKeyWithBLOBs(ShopWxNewsItemWithBLOBs record);

	int updateByPrimaryKey(ShopWxNewsItem record);

	// 根据msgId 查询顶部news
	ShopWxNewDto getNewsByMsgId(@Param("msgId") Long msgId);

	int modifyNewsMaterialItem(ShopWxNewsItem item);
	
	int modifyNewsMaterialItemFromPreviewMode(ShopWxNewsItem item);

	// 根据msgId查询news返回news 用于微信对接
	List<Map<String, Object>> selectNewsListByMsgId(@Param("msgId") Long msgId, @Param("shopId") Long shopId);

	/**
	 * 根据公众号id查询预览图文消息的user列表
	 * @param shopId
	 * @return
	 */
	List<Map<String, Object>> selectNewsPreviewUsers(Long shopId);

	// 素材发送情况统计list
	List<NewsStatsDataDto> messageStatsDataList(@Param("shopId") Long shopId, @Param("startTime") String startTime, @Param("endTime") String endTime);

	// 素材发送情况统计总数昨日
	NewsStatsDataDto messageStatsDataSum(@Param("shopId") Long shopId,@Param("dayNum") String dayNum );

	// 素材发送统计全部图文list以天分组
	List<NewsStatsDataDto> msgStatsDataListGroupDay(@Param("shopId") Long shopId, @Param("startTime") String startTime, @Param("endTime") String endTime);

	//查询发送图文的总次数 发送朋友圈阅读次数，转发阅读次数等等等
	List<WxIfMessageStatsDetail> wxIfMessageStatsDetail(WxMessageQueryParam queryParam);

	//查询图文阅读总数
	WxIfMessageStatsDetail wxIfMessageStatsSum(@Param("shopId") Long shopId, @Param("startTime") String startTime,
											   @Param("endTime") String endTime,@Param("timeType") String timeType);
}