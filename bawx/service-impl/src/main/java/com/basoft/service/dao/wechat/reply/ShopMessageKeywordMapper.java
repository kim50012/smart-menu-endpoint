package com.basoft.service.dao.wechat.reply;

import com.basoft.service.entity.wechat.reply.ShopMessageKeyword;
import com.basoft.service.entity.wechat.reply.ShopMessageKeywordExample;
import com.basoft.service.entity.wechat.reply.ShopMessageKeywordKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopMessageKeywordMapper {
    int countByExample(ShopMessageKeywordExample example);

    int deleteByExample(ShopMessageKeywordExample example);

    int deleteByPrimaryKey(ShopMessageKeywordKey key);

    int insert(ShopMessageKeyword record);

    int insertSelective(ShopMessageKeyword record);

    List<ShopMessageKeyword> selectByExample(ShopMessageKeywordExample example);

    ShopMessageKeyword selectByPrimaryKey(ShopMessageKeywordKey key);

    int updateByExampleSelective(@Param("record") ShopMessageKeyword record, @Param("example") ShopMessageKeywordExample example);

    int updateByExample(@Param("record") ShopMessageKeyword record, @Param("example") ShopMessageKeywordExample example);

    int updateByPrimaryKeySelective(ShopMessageKeyword record);

    int updateByPrimaryKey(ShopMessageKeyword record);

    int insertReplyKeyList(List<ShopMessageKeyword> keywordList);

    int deleteKeyWordAllByMessageId(@Param("messageId")Long messageId);

	/**
	 * 新增关键字回复-检查关键字是否已经存在
	 * 
	 * @param shopId
	 * @param keyWord
	 * @return
	 */
	int checkKeyReply(@Param("shopId")Long shopId, @Param("keyWord")String keyWord);
}