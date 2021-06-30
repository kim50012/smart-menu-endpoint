package com.basoft.service.definition.wechat.reply;

import java.util.List;

import com.basoft.service.dto.customer.CustMsgDto;
import com.basoft.service.dto.wechat.reply.AutomaticReplyDto;
import com.basoft.service.entity.wechat.reply.ShopMessageKeyword;
import com.basoft.service.param.customer.CustMsgQueryParam;
import com.basoft.service.param.reply.AutomaticReplyForm;
import com.basoft.service.param.reply.ShopMsgKeyqueryParam;
import com.github.pagehelper.PageInfo;

public interface ReplyService {
    /**
     * 获取关键字回复列表
     * 
     * @param shopMsgKeyqueryParam
     * @return
     */
    public PageInfo<AutomaticReplyDto> keyReplyFindAll(ShopMsgKeyqueryParam shopMsgKeyqueryParam);
    
	/**
	 * 新增关键字回复-检查关键字是否已经存在
	 * 
	 * @param shopId
	 * @param keyWord
	 * @return
	 */
	public int checkKeyReply(Long shopId, String keyWord);
    
    /**
     * 新增关键字回复
     * 
     * @param automaticReplyForm
     * @return
     */
    public int insertKeyReply(AutomaticReplyForm automaticReplyForm);
    
    /**
     * 查询关键字回复详情
     * 
     * @param backSessionShopId
     * @param messageId
     * @return
     */
    public AutomaticReplyDto getKeyReplyDetail(Long backSessionShopId,Long messageId);
    
    /**
     * 根据msgId获取关键字列表
     * 
     * @param messageId
     * @return
     */
    public List<ShopMessageKeyword> getKeyByListMsgId(Long messageId);
    
    /**
     * 关键字回复修改
     * 
     * @param automaticReplyForm
     * @return
     */
    public int updateKeyReplyDetail(AutomaticReplyForm automaticReplyForm);
    
    /**
     * 删除关键字回复
     * 
     * @param messageId
     * @return
     */
    public int delShopWxMessage(Long messageId);

	/**
	 * 根据msgGroup查询微信回复：自动回复和关注回复。特别说明：自动回复和关注回复有效的只有一条
	 * 
	 * @param shopId
	 * @param msgGroup AUTO-自动回复 FOCUS-关注回复
	 * @return
	 */
    public AutomaticReplyDto getShopWxMessageByMsgGroup(Long shopId,String msgGroup);
    
    
    /**
     * 新增或修改自动回复或关注回复
     * 
     * @param automaticReplyForm
     * @return
     */
    public int insertOrUpdateAutomaticAndFocusReply(AutomaticReplyForm automaticReplyForm);
    
    /**
     * 查询客户咨询
     * 
     * @param queryParam
     * @return
     */
    public PageInfo<CustMsgDto> msgListFragment(CustMsgQueryParam queryParam);
}
