package com.basoft.service.dao.wechat.shopWxNews;

import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsCust;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsCustExample;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsCustKey;
import com.basoft.service.param.wechat.shopWxNews.SenNewsForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ShopWxNewsCustMapper {
    int countByExample(ShopWxNewsCustExample example);

    int deleteByExample(ShopWxNewsCustExample example);

    int deleteByPrimaryKey(ShopWxNewsCustKey key);

    int insert(ShopWxNewsCust record);

    int insertSelective(ShopWxNewsCust record);

    List<ShopWxNewsCust> selectByExample(ShopWxNewsCustExample example);

    ShopWxNewsCust selectByPrimaryKey(ShopWxNewsCustKey key);

    int updateByExampleSelective(@Param("record") ShopWxNewsCust record, @Param("example") ShopWxNewsCustExample example);

    int updateByExample(@Param("record") ShopWxNewsCust record, @Param("example") ShopWxNewsCustExample example);

    int updateByPrimaryKeySelective(ShopWxNewsCust record);

    int updateByPrimaryKey(ShopWxNewsCust record);

    int updateShopWxNewsCustSendTypeOne(SenNewsForm form);

    int insertShopWxNewsCustSendTypeOne(SenNewsForm form);

    int updateShopWxNewsCustSendTypeTwo(SenNewsForm form);

    int insertShopWxNewsCustSendTypeTwo(SenNewsForm form);

    int insertShopWxNewsCustSendTypeThree(SenNewsForm form);

    int updateShopWxNewsCustSendTypeThree(SenNewsForm form);

    List<String> queryOpenIdWxNewsCust(SenNewsForm form);

    int saveSendResultNewsCust(Map<String,Object> map);

	/**
	 * 查询群发消息目标用户OPENID列表
	 * 
	 * @param form
	 * @return
	 */
	List<String> queryCustOpenIdList(SenNewsForm form);

	/**
	 * 全部微信有效用户openid：关注、已订阅
	 * @param form
	 * @return
	 */
	List<String> queryAllCustOpenIdList(SenNewsForm form);

	/**
	 * 按等级：关注、已订阅
	 * 
	 * @param form
	 * @return
	 */
	List<String> queryCustOpenIdListByGrade(SenNewsForm form);

	/**
	 * 根据cust id查询其中已订阅的微信用户openid列表
	 * @param form
	 * @return
	 */
	List<String> queryCustOpenIdListByCust(SenNewsForm form);
}