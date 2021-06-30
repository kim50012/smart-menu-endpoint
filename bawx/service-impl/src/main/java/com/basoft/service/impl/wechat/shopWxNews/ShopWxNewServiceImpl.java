package com.basoft.service.impl.wechat.shopWxNews;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.basoft.core.ware.wechat.util.WeixinMediaUtils;
import com.basoft.core.ware.wechat.util.WeixinUtils;
import com.basoft.service.definition.wechat.BizConstants;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.entity.wechat.appinfo.AppInfoWithBLOBs;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basoft.service.dao.wechat.shopWxNews.ShopWxNewsCustMapper;
import com.basoft.service.dao.wechat.shopWxNews.ShopWxNewsHeadMapper;
import com.basoft.service.dao.wechat.shopWxNews.ShopWxNewsItemMapper;
import com.basoft.service.definition.base.IdService;
import com.basoft.service.definition.wechat.shopWxNews.ShopWxNewService;
import com.basoft.service.dto.wechat.shopWxNew.NewsStatsDataDto;
import com.basoft.service.dto.wechat.shopWxNew.ShopWxNewDto;
import com.basoft.service.dto.wechat.shopWxNew.WxIfMessageStatsDetail;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsHeadExample;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsItemExample;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsItemKey;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsItemWithBLOBs;
import com.basoft.service.param.wechat.shopWxNews.SenNewsForm;
import com.basoft.service.param.wechat.shopWxNews.ShopWxNewsForm;
import com.basoft.service.param.wechat.shopWxNews.ShopWxNewsQueryParam;
import com.basoft.service.param.wechat.wxMessage.WxMessageQueryParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午11:30 2018/4/16
 **/
@Service
public class ShopWxNewServiceImpl implements ShopWxNewService {
	@Autowired
	private ShopWxNewsHeadMapper shopWxNewsHeadMapper;

	@Autowired
	private ShopWxNewsItemMapper shopWxNewsItemMapper;

	@Autowired
	private ShopWxNewsCustMapper shopWxNewsCustMapper;

	@Autowired
	private IdService idService;

	@Autowired
	private WechatService wechatService;

	/**
	 * 查询素材列表头部
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<ShopWxNewDto> matterFindAll(ShopWxNewsQueryParam param) {
		PageHelper.startPage(param.getPage(), param.getRows());
		List<ShopWxNewDto> list = shopWxNewsHeadMapper.matterFindAll(param);
		return new PageInfo<>(list);
	}

	/**
	 * 查询msg头部及以下
	 * 
	 * @param msgId
	 * @param shopId
	 * @return
	 */
	@Override
	public ShopWxNewDto getShopWxNewsItem(Long msgId, Long shopId) {
		ShopWxNewDto dto = new ShopWxNewDto();
		ShopWxNewDto shopWxNewsHead = shopWxNewsHeadMapper.getShopWxNewsHead(msgId, shopId);// 头部
		dto.setDto(shopWxNewsHead);
		List<ShopWxNewDto> dtoList = shopWxNewsHeadMapper.getShopWxNewsItemList(msgId, shopId);// 中部
		dto.setShopWxNewsItemChild(dtoList);
		return dto;
	}
	
	

	@Override
	public ShopWxNewDto getShopWxNewsHead(Long msgId, Long shopId) {
		return shopWxNewsHeadMapper.getShopWxNewsHead(msgId, shopId);
	}

	@Override
	public ShopWxNewDto getNewsByMsgId(Long msgId) {
		return shopWxNewsItemMapper.getNewsByMsgId(msgId);
	}

	@Override
	//@Transactional(transactionManager = "primaryTransactionManager", rollbackFor = Exception.class)
	@Transactional
	public int insertOrUpdateShopNewsItem(ShopWxNewsForm form, Boolean headFlag, String edit) {
		int result = 0;
		if (edit.equals("add")) {
			if (headFlag)
				result = shopWxNewsHeadMapper.insertSelective(form.getShopWxNewHead());
			result = shopWxNewsItemMapper.insertSelective(form.getShopWxNewsItemWithBLOBs());
		} else {
			shopWxNewsItemMapper.updateByPrimaryKeySelective(form.getShopWxNewsItemWithBLOBs());
		}
		return result;
	}

	@Override
	public int delShopWxNewsItem(Long newsId, Long shopId) {
		ShopWxNewsItemExample ex = new ShopWxNewsItemExample();
		ex.createCriteria().andNewsIdEqualTo(newsId).andShopIdEqualTo(shopId);
		return shopWxNewsItemMapper.deleteByExample(ex);
	}

	@Override
	public ShopWxNewsItemWithBLOBs getShopWxNewsItemOne(Long shopId, Long msgId, Long newsId) {
		ShopWxNewsItemKey key = new ShopWxNewsItemKey();
		key.setShopId(shopId);
		key.setMsgId(msgId);
		key.setNewsId(newsId);
		return shopWxNewsItemMapper.selectByPrimaryKey(key);
	}

	@Override
	@Transactional(transactionManager = "primaryTransactionManager", rollbackFor = Exception.class)
	public int delMsgId(Long msgId, Long shopId) {
		int result = 0;
        ShopWxNewDto shopWxNewsHead = shopWxNewsHeadMapper.getShopWxNewsHead(msgId, shopId);
        if(shopWxNewsHead!=null){
            String mediaId = shopWxNewsHead.getWxMsgId();
            String token = wechatService.getAccessToken(shopId);
            if(StringUtils.isNotBlank(mediaId)) {
                try {
                    WeixinMediaUtils.delMaterial(token,mediaId);
                }catch (Exception e) {
                    // 处理微信接口调用失败【[40007]invalid media_id hint:】导致的图片无法删除问题
                    System.out.println("Deleting shopWxNewsHead occur error>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + e.getMessage());
                    if (e.getMessage().contains("invalid media_id")) {
                        delMsgByMsgId(msgId,shopId);
                        return result;
                    }
                }
            }
        }

		return delMsgByMsgId(msgId,shopId);
	}

	private int delMsgByMsgId(Long msgId,Long shopId){
	    int result = 0;
        // 删除news
        ShopWxNewsItemExample ex = new ShopWxNewsItemExample();
        ex.createCriteria().andMsgIdEqualTo(msgId).andShopIdEqualTo(shopId);
        result = shopWxNewsItemMapper.deleteByExample(ex);
        // 删除head
        ShopWxNewsHeadExample head = new ShopWxNewsHeadExample();
        head.createCriteria().andMsgIdEqualTo(msgId).andShopIdEqualTo(shopId);
        result = shopWxNewsHeadMapper.deleteByExample(head);
        return result;

    }

	@Override
	//@Transactional(transactionManager = "primaryTransactionManager", rollbackFor = Exception.class)
	@Transactional
	public int insertOrUpdateShopNewsByList(ShopWxNewsForm form, Boolean headFlag) {
		int result = 0;
		if (headFlag) {
			result = shopWxNewsHeadMapper.insertSelective(form.getShopWxNewHead()); // 插入msg表
		}
		String saveType = "edt";
		Long random = 1L;
		if (form.getShopWxNewsChild() != null && form.getShopWxNewsChild().size() > 0) {
			for (ShopWxNewsItemWithBLOBs newsBlobs : form.getShopWxNewsChild()) {
				if (newsBlobs.getNewsId() == 0) {
					random += RandomUtils.nextLong(1, 999);
					saveType = "add";
					newsBlobs = setBlobs(newsBlobs, form, random, saveType);
					shopWxNewsItemMapper.insertSelective(newsBlobs);// 插入newsItem
				} else {
					saveType = "edt";
					newsBlobs = setBlobs(newsBlobs, form, random, saveType);
					shopWxNewsItemMapper.updateByPrimaryKeySelective(newsBlobs);// 修改newsItem
				}
			}
		}
		return result;
	}

	/**
	 * @author Dong Xifu
	 * @Date 2018/5/7 上午9:58
	 * @describe 封装 ShopWxNewsItemWithBLOBs
	 * @param
	 * @return
	 **/
	public ShopWxNewsItemWithBLOBs setBlobs(ShopWxNewsItemWithBLOBs newsBlobs, ShopWxNewsForm form, Long random, String saveType) {
		if ("add".equals(saveType)) {
			newsBlobs.setShopId(form.getShopId());
			newsBlobs.setMsgId(form.getMsgId());
			newsBlobs.setNewsId(idService.generateNewsItem() - random);
			newsBlobs.setCreatedId(form.getUserId());
			newsBlobs.setCreatedDt(new Date());
			newsBlobs.setIsDelete(0);
		} else {
			newsBlobs.setShopId(form.getShopId());
			newsBlobs.setMsgId(form.getMsgId());
			newsBlobs.setModifiedId(form.getUserId());
			newsBlobs.setModifiedDt(new Date());
		}
		return newsBlobs;
	}

	@Override
	//@Transactional(transactionManager = "primaryTransactionManager", rollbackFor = Exception.class)
	@Transactional
	public int sendNews(SenNewsForm form) {
		int result = 0;
		// 1-所有 2-按客户等级 3-按选择的用户
		if (form.getSendType() == 1) {
			// 更新已存在的客户和消息关系
			result = shopWxNewsCustMapper.updateShopWxNewsCustSendTypeOne(form);
			// 新增缺失的客户和消息关系
			result = shopWxNewsCustMapper.insertShopWxNewsCustSendTypeOne(form);
		} else if (form.getSendType() == 2) {
			result = shopWxNewsCustMapper.updateShopWxNewsCustSendTypeTwo(form);
			result = shopWxNewsCustMapper.insertShopWxNewsCustSendTypeTwo(form);
		} else if (form.getSendType() == 3) {
			if (form.getCustList() != null && form.getCustList().size() > 0) {
				// 逐条处理
				/*for (String cust : form.getCustList()) {
					form.setCustSysId(cust);
					result = shopWxNewsCustMapper.updateShopWxNewsCustSendTypeThree(form);
					result = shopWxNewsCustMapper.insertShopWxNewsCustSendTypeThree(form);
				}*/
				// 一次处理
				StringBuilder sb = new StringBuilder();
				for (String cust : form.getCustList()) {
					// 拼接custid
					sb.append(cust).append(",");
				}
				form.setCustSysId(sb.substring(0, sb.length()-1));
				result = shopWxNewsCustMapper.updateShopWxNewsCustSendTypeThree(form);
				result = shopWxNewsCustMapper.insertShopWxNewsCustSendTypeThree(form);
			}
		}
		return result;
	}

	/**
	 * 图文消息群发时获取目标用户得OPENID列表
	 * 
	 * 查询结果来自：WX_NEWS_CUST表【oracle数据库】
	 * 
	 * @param form
	 * @return
	 */
	@Override
	public List<String> queryOpenIdWxNewsCust(SenNewsForm form) {
		return shopWxNewsCustMapper.queryOpenIdWxNewsCust(form);
	}
	
	/**
	 * 非图文（文本、图片、音频和视频）查询群发消息目标用户OPENID列表
	 * 
	 * @param form
	 * @return
	 */
	public List<String> queryCustOpenIdList(SenNewsForm form) {
		int sendType = form.getSendType();
		// 1-所有 2-按客户等级 3-按选择的用户
		if (sendType == 1) {
			// 查询所有-该分支永远走不到，也没必要走了。。。因为微信全部发送接口不需要OPENID列表
			return shopWxNewsCustMapper.queryAllCustOpenIdList(form);
		} else if (sendType == 2) {
			return shopWxNewsCustMapper.queryCustOpenIdListByGrade(form);
		} else if (sendType == 3) {
			List<String> custList = form.getCustList();
			StringBuilder sb = new StringBuilder();
			for(String s : custList) {
				sb.append(s).append(",");
			}
			form.setCustSysId(sb.substring(0, sb.length()-1));
			// 过滤掉不接受消息的用户
			return shopWxNewsCustMapper.queryCustOpenIdListByCust(form);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> selectNewsListByMsgId(Long msgId, Long shopId) {
		return shopWxNewsItemMapper.selectNewsListByMsgId(msgId, shopId);
	}

	/**
	 * 根据公众号id查询具有预览图文消息权限的user列表
	 * 
	 * @param shopId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectNewsPreviewUsers(Long shopId) {
		return shopWxNewsItemMapper.selectNewsPreviewUsers(shopId);
	}
	
    @Override
    public List<NewsStatsDataDto> messageStatsDataList(Long shopId,String startTime,String endTime) {
        return shopWxNewsItemMapper.messageStatsDataList(shopId,startTime,endTime);
    }

    @Override
    public NewsStatsDataDto messageStatsDataSum(Long shopId,String dayNum ) {
        return shopWxNewsItemMapper.messageStatsDataSum(shopId,dayNum);
    }

    @Override
    public List<NewsStatsDataDto> msgStatsDataListGroupDay(Long shopId, String startTime, String endTime) {
        return shopWxNewsItemMapper.msgStatsDataListGroupDay(shopId,startTime,endTime);
    }

	@Override
	public List<WxIfMessageStatsDetail> wxIfMessageStatsDetail(WxMessageQueryParam queryParam) {
		return shopWxNewsItemMapper.wxIfMessageStatsDetail(queryParam);
	}

	@Override
	public PageInfo<WxIfMessageStatsDetail> wxIfMessageStatsDetailTable(WxMessageQueryParam queryParam) {
		PageHelper.startPage(queryParam.getPage(),queryParam.getRows());
		@SuppressWarnings("unused")
		List<WxIfMessageStatsDetail> list = shopWxNewsItemMapper.wxIfMessageStatsDetail(queryParam);
		return null;
	}

	@Override
	public WxIfMessageStatsDetail wxIfMessageStatsSum(Long shopId,String startTime, String endTime,String timeType) {
		return shopWxNewsItemMapper.wxIfMessageStatsSum(shopId,startTime,endTime,timeType);
	}
}