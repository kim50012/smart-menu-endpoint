package com.basoft.service.dao.wechat.shopWxNews;

import com.basoft.service.dto.wechat.shopWxNew.ShopWxNewDto;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsHead;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsHeadExample;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsHeadKey;
import com.basoft.service.param.wechat.shopWxNews.ShopWxNewsQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ShopWxNewsHeadMapper {
    int countByExample(ShopWxNewsHeadExample example);

    int deleteByExample(ShopWxNewsHeadExample example);

    int deleteByPrimaryKey(ShopWxNewsHeadKey key);

    int insert(ShopWxNewsHead record);

    int insertSelective(ShopWxNewsHead record);

    List<ShopWxNewsHead> selectByExampleWithBLOBs(ShopWxNewsHeadExample example);

    List<ShopWxNewsHead> selectByExample(ShopWxNewsHeadExample example);

    ShopWxNewsHead selectByPrimaryKey(ShopWxNewsHeadKey key);

    int updateByExampleSelective(@Param("record") ShopWxNewsHead record, @Param("example") ShopWxNewsHeadExample example);

    int updateByExampleWithBLOBs(@Param("record") ShopWxNewsHead record, @Param("example") ShopWxNewsHeadExample example);

    int updateByExample(@Param("record") ShopWxNewsHead record, @Param("example") ShopWxNewsHeadExample example);

    int updateByPrimaryKeySelective(ShopWxNewsHead record);

    int updateByPrimaryKeyWithBLOBs(ShopWxNewsHead record);

    int updateByPrimaryKey(ShopWxNewsHead record);

	/**
	 * 查询素材列表头部
	 * 
	 * @param param
	 * @return
	 */
	public List<ShopWxNewDto> matterFindAll(ShopWxNewsQueryParam param);

	/**
	 * 素材头部
	 * 
	 * @param msgId
	 * @param shopId
	 * @return
	 */
	ShopWxNewDto getShopWxNewsHead(@Param("msgId") Long msgId, @Param("shopId") Long shopId);

	/**
	 * 素材内容
	 * 
	 * @param msgId
	 * @param shopId
	 * @return
	 */
	List<ShopWxNewDto> getShopWxNewsItemList(@Param("msgId") Long msgId, @Param("shopId") Long shopId);

    Long modifyNewsMaterial(Map<String,Object> map);

    int saveSendResultHead(Map<String, Object> map);
}