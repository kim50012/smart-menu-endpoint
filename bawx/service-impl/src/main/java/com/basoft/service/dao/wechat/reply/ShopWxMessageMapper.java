package com.basoft.service.dao.wechat.reply;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.basoft.service.dto.customer.CustMsgDto;
import com.basoft.service.dto.wechat.reply.AutomaticReplyDto;
import com.basoft.service.entity.wechat.reply.ShopWxMessage;
import com.basoft.service.entity.wechat.reply.ShopWxMessageExample;
import com.basoft.service.entity.wechat.reply.ShopWxMessageWithBLOBs;
import com.basoft.service.param.customer.CustMsgQueryParam;
import com.basoft.service.param.reply.AutomaticReplyForm;
import com.basoft.service.param.reply.ShopMsgKeyqueryParam;

@Repository
public interface ShopWxMessageMapper {
	int countByExample(ShopWxMessageExample example);

	int deleteByExample(ShopWxMessageExample example);

	int deleteByPrimaryKey(Long messageId);

	int insert(ShopWxMessageWithBLOBs record);

	int insertSelective(ShopWxMessageWithBLOBs record);

	List<ShopWxMessageWithBLOBs> selectByExampleWithBLOBs(ShopWxMessageExample example);

	List<ShopWxMessage> selectByExample(ShopWxMessageExample example);

	ShopWxMessageWithBLOBs selectByPrimaryKey(Long messageId);

	int updateByExampleSelective(@Param("record") ShopWxMessageWithBLOBs record, @Param("example") ShopWxMessageExample example);

	int updateByExampleWithBLOBs(@Param("record") ShopWxMessageWithBLOBs record, @Param("example") ShopWxMessageExample example);

	int updateByExample(@Param("record") ShopWxMessage record, @Param("example") ShopWxMessageExample example);

	int updateByPrimaryKeySelective(ShopWxMessageWithBLOBs record);

	int updateByPrimaryKeyWithBLOBs(ShopWxMessageWithBLOBs record);

	int updateByPrimaryKey(ShopWxMessage record);

	/**
	 * 获取关键字回复列表
	 * 
	 * @param shopMsgKeyqueryParam
	 * @return
	 */
	List<AutomaticReplyDto> keyReplyFindAll(ShopMsgKeyqueryParam shopMsgKeyqueryParam);

	/**
	 * 新增自动／关注回复
	 * 
	 * @param automaticReplyForm
	 * @return
	 */
	int insertReply(AutomaticReplyForm automaticReplyForm);

	/**
	 * 获取关键字回复详情
	 * 
	 * @param messageId
	 * @return
	 */
	AutomaticReplyDto getKeyReplyDetail(@Param("shopId") Long shopId, @Param("messageId") Long messageId);

	/**
	 * 根据msgGroup查询微信回复（关键字回复，自动回复和关注回复），因关键字回复有其他接口，所以该接口用于自动回复和关注回复
	 * 
	 * @param shopId
	 * @param msgGroup AUTO-自动回复 FOCUS-关注回复
	 * @return
	 */
	AutomaticReplyDto getShopWxMessageByMsgGroup(@Param("shopId") Long shopId, @Param("msgGroup") String msgGroup);

    /**
     * 查询客户咨询
     * 
     * @param queryParam
     * @return
     */
	List<CustMsgDto> msgListFragment(CustMsgQueryParam queryParam);
}
