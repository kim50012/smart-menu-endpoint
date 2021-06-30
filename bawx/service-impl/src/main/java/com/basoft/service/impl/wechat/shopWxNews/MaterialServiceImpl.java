package com.basoft.service.impl.wechat.shopWxNews;

import com.basoft.service.dao.wechat.shopWxNews.ShopWxNewsHeadMapper;
import com.basoft.service.dao.wechat.shopWxNews.ShopWxNewsItemMapper;
import com.basoft.service.definition.wechat.shopWxNews.MaterialService;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:32 2018/5/10
 **/
@Service
public class MaterialServiceImpl implements MaterialService {
	@Autowired
	private ShopWxNewsHeadMapper shopWxNewsHeadMapper;

	@Autowired
	private ShopWxNewsItemMapper shopWxNewsItemMapper;

	@Override
	public Long modifyNewsMaterial(Map<String, Object> map) {
		// 操作shop_wx_news_head表
		Long msgId = shopWxNewsHeadMapper.modifyNewsMaterial(map);
		List<ShopWxNewsItem> newsItemList = (List<ShopWxNewsItem>) map.get("newsItemList");
		for (ShopWxNewsItem item : newsItemList) {
			// 操作shop_wx_news_item表
			shopWxNewsItemMapper.modifyNewsMaterialItemFromPreviewMode(item);
		}
		return msgId;
	}
}
