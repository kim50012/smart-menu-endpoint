package com.basoft.service.impl.wechat.reply;

import com.basoft.service.dao.wechat.reply.ShopMessageKeywordMapper;
import com.basoft.service.dao.wechat.reply.ShopWxMessageMapper;
import com.basoft.service.definition.base.IdService;
import com.basoft.service.definition.wechat.BizConstants;
import com.basoft.service.definition.wechat.reply.ReplyService;
import com.basoft.service.dto.customer.CustMsgDto;
import com.basoft.service.dto.wechat.reply.AutomaticReplyDto;
import com.basoft.service.entity.wechat.reply.ShopMessageKeyword;
import com.basoft.service.entity.wechat.reply.ShopMessageKeywordExample;
import com.basoft.service.entity.wechat.reply.ShopWxMessageExample;
import com.basoft.service.entity.wechat.reply.ShopWxMessageWithBLOBs;
import com.basoft.service.param.customer.CustMsgQueryParam;
import com.basoft.service.param.reply.AutomaticReplyForm;
import com.basoft.service.param.reply.ShopMsgKeyqueryParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class replyServiceImpl implements ReplyService {
	@Autowired
	private ShopWxMessageMapper shopWxMessageMapper;

	@Autowired
	private ShopMessageKeywordMapper shopMessageKeywordMapper;

	@Autowired
	private IdService idService;

	/**
	 * 关键字回复列表查询
	 * 
	 * @param shopMsgKeyqueryParam
	 * @return
	 */
	@Override
	public PageInfo<AutomaticReplyDto> keyReplyFindAll(ShopMsgKeyqueryParam shopMsgKeyqueryParam) {
		if (shopMsgKeyqueryParam == null) {
			return null;
		}
		PageHelper.startPage(shopMsgKeyqueryParam.getPage(), shopMsgKeyqueryParam.getRows());
		List<AutomaticReplyDto> automaticReplyDtosList = shopWxMessageMapper.keyReplyFindAll(shopMsgKeyqueryParam);

		return new PageInfo<>(automaticReplyDtosList);
	}
	
	/**
	 * 新增关键字回复-检查关键字是否已经存在
	 * 
	 * @param shopId
	 * @param keyWord
	 * @return
	 */
	public int checkKeyReply(Long shopId, String keyWord) {
		return shopMessageKeywordMapper.checkKeyReply(shopId, keyWord);
	}
	
    /**
     * 新增关键字回复
     * 
     * @param automaticReplyForm
     * @return
     */
    @Override
    //@Transactional(transactionManager = "primaryTransactionManager", rollbackFor = Exception.class)
    @Transactional
    public int insertKeyReply(AutomaticReplyForm automaticReplyForm) {
        shopMessageKeywordMapper.insertReplyKeyList(automaticReplyForm.getKeywordList());

        automaticReplyForm.setMsgGroup(BizConstants.KEYWORD_REPLY);
        automaticReplyForm.setSendType(automaticReplyForm.getSendFileType());
        automaticReplyForm.setSendTypeId(BizConstants.SEND_TYPE_ID_ALL);
        automaticReplyForm.setCreatedDt(new Date());
        automaticReplyForm.setIsDelete(BizConstants.IS_DELETE_N);
        return shopWxMessageMapper.insertReply(automaticReplyForm);
    }

    /**
     * 查询关键字回复详情
     * 
     * @param backSessionShopId
     * @param messageId
     * @return
     */
	@Override
	public AutomaticReplyDto getKeyReplyDetail(Long shopId, Long messageId) {
		return shopWxMessageMapper.getKeyReplyDetail(shopId, messageId);
	}
	
	/**
	 * 根据msgId获取关键字列表
	 * 
	 * @param messageId
	 * @return
	 */
	@Override
    public List<ShopMessageKeyword> getKeyByListMsgId(Long messageId) {
        ShopMessageKeywordExample ex = new ShopMessageKeywordExample();
        ex.createCriteria().andMessageIdEqualTo(messageId);
        return shopMessageKeywordMapper.selectByExample(ex);
    }
	
    /**
     * 关键字回复修改
     * 
     * @param form
     * @return
     */
    @Override
    //@Transactional(transactionManager = "primaryTransactionManager", rollbackFor = Exception.class)
    @Transactional
    public int updateKeyReplyDetail(AutomaticReplyForm form) {
    	// 删除该关键回复下的所有关键字条目
        shopMessageKeywordMapper.deleteKeyWordAllByMessageId(form.getMessageId());
        shopMessageKeywordMapper.insertReplyKeyList(form.getKeywordList());

        // 更新关键回复的主体信息
        ShopWxMessageWithBLOBs bloBs = shopWxMessageMapper.selectByPrimaryKey(form.getMessageId());
        bloBs.setModifiedDt(new Date());
        bloBs.setSendFileType(form.getSendFileType());
        bloBs.setMsgTitle(form.getMsgTitle());
        bloBs.setSendMsgBody(form.getSendMsgBody());
        bloBs.setMaterialFileId(form.getMaterialFileId());
        bloBs.setMaterialFileWxId(form.getMaterialFileWxId());
        bloBs.setCallcenterId(form.getCallcenterId());
        bloBs.setKfNick(form.getKfNick());
        return shopWxMessageMapper.updateByPrimaryKeyWithBLOBs(bloBs);
    }
	
    /**
     * 删除关键字回复
     * 
     * @param messageId
     * @return
     */
    @Override
    //@Transactional(transactionManager = "primaryTransactionManager", rollbackFor = Exception.class)
    @Transactional
    public int delShopWxMessage(Long messageId) {
        ShopMessageKeywordExample ex = new ShopMessageKeywordExample();
        ex.createCriteria().andMessageIdEqualTo(messageId);
        shopMessageKeywordMapper.deleteByExample(ex);
        return shopWxMessageMapper.deleteByPrimaryKey(messageId);
    }
	
	/**
	 * 根据msgGroup查询微信回复：自动回复和关注回复。特别说明：自动回复和关注回复有效的只有一条
	 * 
	 * @param shopId
	 * @param msgGroup AUTO-自动回复 FOCUS-关注回复
	 * @return
	 */
	@Override
	public AutomaticReplyDto getShopWxMessageByMsgGroup(Long shopId, String msgGroup) {
		return shopWxMessageMapper.getShopWxMessageByMsgGroup(shopId, msgGroup);
	}
	
    /**
     * 新增或修改自动回复或关注回复
     * 
     * @param automaticReplyForm
     * @return
     */
    @Override
    //@Transactional(transactionManager = "primaryTransactionManager", rollbackFor = Exception.class)
    @Transactional
    public int insertOrUpdateAutomaticAndFocusReply(AutomaticReplyForm automaticReplyForm) {
    	// 删除自动或关注回复
        ShopWxMessageExample example = new ShopWxMessageExample();
        example.createCriteria().andShopIdEqualTo(automaticReplyForm.getShopId()).andMsgGroupEqualTo(automaticReplyForm.getMsgGroup());
        shopWxMessageMapper.deleteByExample(example);
        // 重新新增自动或关注回复
        automaticReplyForm.setSendType(automaticReplyForm.getSendFileType());
        automaticReplyForm.setSendTypeId(BizConstants.SEND_TYPE_ID_ALL);
        automaticReplyForm.setCreatedDt(new Date());
        automaticReplyForm.setIsDelete(BizConstants.IS_DELETE_N);
        automaticReplyForm.setMessageId(idService.generateWxMessage());
        return shopWxMessageMapper.insertReply(automaticReplyForm);
    }
    
    /**
     * 查询客户咨询
     * 
     * @param queryParam
     * @return
     */
    public PageInfo<CustMsgDto> msgListFragment(CustMsgQueryParam queryParam) {
        PageHelper.startPage(queryParam.getPage(),queryParam.getRows());
        List<CustMsgDto> dtolist = shopWxMessageMapper.msgListFragment(queryParam);
        return new PageInfo<>(dtolist);
    }
}