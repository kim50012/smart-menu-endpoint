package com.basoft.service.impl.shop;

import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.ware.wechat.util.WeixinMediaUtils;
import com.basoft.service.dao.shop.ShopFileMapper;
import com.basoft.service.dao.wechat.reply.ShopWxMessageMapper;
import com.basoft.service.dao.wechat.shopWxNews.ShopWxNewsItemMapper;
import com.basoft.service.definition.shop.ShopFileService;
import com.basoft.service.definition.wechat.BizConstants;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.dto.shop.ShopFileDto;
import com.basoft.service.entity.shop.ShopFile;
import com.basoft.service.entity.shop.ShopFileKey;
import com.basoft.service.entity.wechat.reply.ShopWxMessageExample;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsItemExample;
import com.basoft.service.param.shopFile.ShopFileQueryParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShopFileServiceImpl implements ShopFileService {
	@Autowired
	private ShopFileMapper shopFileMapper;

	@Autowired
	private ShopWxMessageMapper shopWxMessageMapper;

	@Autowired
	private ShopWxNewsItemMapper shopWxNewsItemMapper;

	@Autowired
	WechatService wechatService;
	/**
	 * 新增公众号文件
	 * 
	 * @param shopFile
	 * @return
	 */
	public int addShopFile(ShopFile shopFile) {
		return shopFileMapper.insertSelective(shopFile);
	}
	
    @Override
    public PageInfo<ShopFileDto> findAllByParam(ShopFileQueryParam param) {
        if(param==null){
            return null;
        }
        PageHelper.startPage(param.getPage(),param.getRows());
        List<ShopFileDto> list = shopFileMapper.findAllByParam(param);
        return new PageInfo<>(list);
    }

    @Override
    public int insertShopFile(ShopFile shopFile) {
        return shopFileMapper.insertSelective(shopFile);
    }

    @Override
    public ShopFile getShopFileBykey(Long shopId,Long fileId) {
        ShopFileKey key = new ShopFileKey();
        key.setShopId(shopId);
        key.setFileId(fileId);
        return shopFileMapper.selectByPrimaryKey(key);
    }

    @Override
    public int updateShopFile(ShopFile shopFile) {
        ShopFileKey key = new ShopFileKey();
        key.setShopId(shopFile.getShopId());
        key.setFileId(shopFile.getFileId());
        ShopFile entity = shopFileMapper.selectByPrimaryKey(key);
        if(entity==null){
            throw new BizException(ErrorCode.SYS_EMPTY);
        }
        entity.setFileNm(shopFile.getFileNm());
        entity.setModifiedId(shopFile.getModifiedId());
        return shopFileMapper.updateByPrimaryKey(entity);
    }

	/**
	 * 删除图片
	 * 如果被消息或图文使用则不允许删除； 允许删除的情况下，首先删除微信服务器端的图片，然后删除本地记录
	 * 
	 * @param shopId
	 * @param fileId
	 * @return
	 */
	public int deleteShopFile(Long shopId, Long fileId) {
		ShopFileKey key = new ShopFileKey();
		key.setShopId(shopId);
		key.setFileId(fileId);
		int result = 0;
		ShopFile shopFile = shopFileMapper.selectByPrimaryKey(key);
		if (shopFile != null) {
			ShopWxMessageExample wxMsgEx = new ShopWxMessageExample();
			wxMsgEx.createCriteria().andMaterialFileIdEqualTo(fileId);
			int shopMsgCount = shopWxMessageMapper.countByExample(wxMsgEx);
			ShopWxNewsItemExample wxNewsEx = new ShopWxNewsItemExample();
			wxNewsEx.createCriteria().andMfileIdEqualTo(fileId);
			int wxNewsCount = shopWxNewsItemMapper.countByExample(wxNewsEx);
			if (shopMsgCount < 1 && wxNewsCount < 1) {
				if(!StringUtils.isEmpty(shopFile.getMediaId())) {
					try {
						// 删除微信服务器端图片
						String token = wechatService.getAccessToken(Long.valueOf(shopId));
						WeixinMediaUtils.delMaterial(token, shopFile.getMediaId());
					}catch (Exception e) {
						// 处理微信接口调用失败【[40007]invalid media_id hint:】导致的图片无法删除问题
						// System.out.println("Deleting pic occur error>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+e.getMessage());
						log.info("Deleting pic occur error>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+e.getMessage());;
						if(e.getMessage().contains("invalid media_id")) {
							shopFile.setIsUse(BizConstants.MENU_STATE_FORBID);
							result = shopFileMapper.updateByPrimaryKeySelective(shopFile);
							return result;
						}
					}
				}
				
				// 删除本地记录-非物理删除，采用逻辑删除
				// result = shopFileMapper.deleteByPrimaryKey(key);
				shopFile.setIsUse(BizConstants.MENU_STATE_FORBID);
				result = shopFileMapper.updateByPrimaryKeySelective(shopFile);
				
			} else {
				/*// 图片禁用
				shopFile.setIsUse(BizConstants.MENU_STATE_FORBID);
				result = shopFileMapper.updateByPrimaryKeySelective(shopFile);*/
				
				// 返回9999，前端根据此值进行“不允许删除提示”
				return 9999;
			}
		}
		return result;
	}
}