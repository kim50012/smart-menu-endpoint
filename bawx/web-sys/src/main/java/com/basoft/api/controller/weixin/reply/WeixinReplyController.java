package com.basoft.api.controller.weixin.reply;

import com.basoft.api.controller.BaseController;
import com.basoft.api.vo.ApiJson;
import com.basoft.api.vo.wechat.reply.ReplyVO;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.ware.wechat.domain.customservice.GetkflistReturn;
import com.basoft.core.ware.wechat.util.WeixinMessageUtils;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.base.IdService;
import com.basoft.service.definition.shop.ShopFileService;
import com.basoft.service.definition.wechat.BizConstants;
import com.basoft.service.definition.wechat.common.WechatService;
import com.basoft.service.definition.wechat.reply.ReplyService;
import com.basoft.service.dto.wechat.reply.AutomaticReplyDto;
import com.basoft.service.entity.shop.ShopFile;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import com.basoft.service.entity.wechat.reply.ShopMessageKeyword;
import com.basoft.service.param.reply.AutomaticReplyForm;
import com.basoft.service.param.reply.ShopMsgKeyqueryParam;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class WeixinReplyController extends BaseController {
	@Autowired
	private IdService idService;

	@Autowired
	private ReplyService replyService;

	@Autowired
	private WechatService wechatService;
	
	@Autowired
	private ShopFileService shopFileService;
    
    /************************************************关键字回复-start******************************************************************/
	/**
	 * 关键字回复列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/keyReplyFindAll", method = RequestMethod.GET)
	public ApiJson<List> keyReplyFindAll(
			@RequestParam(value = "page", defaultValue = "1") String page, 
			@RequestParam(value = "rows", defaultValue = "20") String rows,
			@RequestParam(value = "keyWord", defaultValue = "") String keyWord, 
			@RequestParam(value = "sendFileType", defaultValue = "-1") String sendFileType) {
        ApiJson<List> result = new ApiJson<>();

        // 关键字
		ShopMsgKeyqueryParam shopMsgKeyqueryParam = new ShopMsgKeyqueryParam();
		shopMsgKeyqueryParam.setShopId(getShopId());
		shopMsgKeyqueryParam.setKeyWord(keyWord);
		shopMsgKeyqueryParam.setSendFileType(sendFileType);
		try {
			PageInfo<AutomaticReplyDto> pageInfo = replyService.keyReplyFindAll(shopMsgKeyqueryParam);
			if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
				result.setPage(pageInfo.getPageNum());
				result.setRecords((int) pageInfo.getTotal());
				result.setTotal(pageInfo.getPages());
				result.setRows(pageInfo.getList().stream().map(data -> new ReplyVO(data)).collect(Collectors.toList()));
			} else {
				result.setPage(1);
				result.setRecords(0);
				result.setTotal(0);
				result.setRows(new ArrayList());
			}
			result.setErrorCode(0);
			result.setErrorMsg("Success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
    /**
     * 新增关键字回复-点击关键字回复的新建按钮调用，目的生成messageId
     * 
     * @return
     */
	@RequestMapping(value = "/createMessageId", method = RequestMethod.GET)
	public Map<String, Object> createMessageId() {
		Map<String, Object> map = new HashMap<>();
		Long messageId = idService.generateWxMessage();
		map.put("messageId", messageId);
		return map;
	}
	
    /**
     * 新增关键字回复-检查关键字是否已经存在
     * 
     * @return
     */
	@RequestMapping(value = "/checkKeyReply", method = RequestMethod.GET)
	public Echo<?> checkKeyReply(@RequestParam(value = "keyWord") String keyWord) {
		if (StringUtils.isEmpty(keyWord)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}
		int keyCount = replyService.checkKeyReply(getShopId(), keyWord);
		return new Echo<Integer>(keyCount);
	}
	
    /**
     * 新增关键字回复
     * 
     * @return
     */
    @PostMapping(value = "/insertKeyReply",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int insertKeyReply(@RequestBody AutomaticReplyForm form) {
		if (form.getKeywordList() == null || form.getKeywordList().size() < 1)
			throw new BizException(ErrorCode.PARAM_MISSING);
		
		Long num = 1L;
		for (ShopMessageKeyword keyword : form.getKeywordList()) {
			num++;
			keyword.setShopId(getShopId());
			keyword.setMessageId(form.getMessageId());
			keyword.setKeywordId(idService.generateMessageKeyWord() + num);
		}
		
        form.setShopId(getShopId());
        form.setCreatedId(getUserId());
        form.setModifiedId(getUserId());
        return replyService.insertKeyReply(form);
    }

    /**
     * 关键字回复详情
     * 
     * @param messageId
     * @return
     */
	@RequestMapping(value = "/getKeyReplyDetail", method = RequestMethod.GET)
	public Echo<?> getKeyReplyDetail(@RequestParam(value = "messageId") String messageId) {
		Long shopId = getShopId();
		AutomaticReplyDto dto = replyService.getKeyReplyDetail(shopId, Long.valueOf(messageId));
		if (dto == null) {
			return new Echo<>();
		}
		ShopFile shopFileBykey = new ShopFile();
		if (dto != null && dto.getMaterialFileId() != null) {
			shopFileBykey = shopFileService.getShopFileBykey(shopId, dto.getMaterialFileId());
			if (shopFileBykey != null)
				dto.setFileUrl(shopFileBykey.getFullUrl());
		}
		List<ShopMessageKeyword> keyByListMsgId = replyService.getKeyByListMsgId(Long.valueOf(messageId));
		return new Echo<>(new ReplyVO(dto, keyByListMsgId));
	}

    /**
     * 关键字回复详情
     * 
     * @param
     * @return
     */
	@RequestMapping(value = "/getCallcenterUser", method = RequestMethod.GET)
	public Echo<?> getCallcenterUser() {
		Long shopId = getShopId();

		AppInfo app = wechatService.selectAppInfoByShopId(shopId);
		String token = wechatService.getAccessToken(app);
		GetkflistReturn getkflistReturn = WeixinMessageUtils.getkflist(token);
		
		return new Echo<>(new ReplyVO(getkflistReturn));
	}
	/**
	 * 关键字回复修改
	 * 
	 * @param automaticReplyForm
	 * @return
	 */
	@PostMapping(value = "/updateKeyReplyDetail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Echo<?> updateKeyReplyDetail(@RequestBody AutomaticReplyForm automaticReplyForm) {
		automaticReplyForm.setShopId(getShopId());
		if (automaticReplyForm.getKeywordList() == null || automaticReplyForm.getKeywordList().size() < 1)
			throw new BizException(ErrorCode.PARAM_MISSING);
		
		//修改人
        automaticReplyForm.setModifiedId(getUserId());
        
        // 重新生成每个关键字条目的ID
        for (ShopMessageKeyword key: automaticReplyForm.getKeywordList()) {
        	key.setMessageId(automaticReplyForm.getMessageId());
        	key.setShopId(getShopId());
            key.setKeywordId(idService.generateMessageKeyWord());
        }
        
        int result = replyService.updateKeyReplyDetail(automaticReplyForm);
        return new Echo<Integer>(result);
    }
	
    /**
     * 删除关键字回复
     * 
     * @param messageId
     * @return
     */
    @DeleteMapping(value = "/delKeyReply", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Echo<?> delShopWxMessage(@RequestParam String messageId){
        int result = replyService.delShopWxMessage(Long.valueOf(messageId));
        return new Echo<Integer>(result);
    }
    /************************************************关键字回复-end******************************************************************/

    
    
    /************************************************自动回复和关注回复-start******************************************************************/
	/**
	 * 查詢自動回復或關注回復
	 * 
	 * @param msgGroup AUTO-自動回復 FOCUS-關注回復
	 * @return
	 */
	@RequestMapping(value = "/getAutomaticOrFocusReply", method = RequestMethod.GET)
	public Echo<?> getAutomaticOrFocusReply(@RequestParam(value = "msgGroup") String msgGroup) {
		Long shopId = getShopId();
		AutomaticReplyDto dto = replyService.getShopWxMessageByMsgGroup(shopId, msgGroup);
		if (dto == null) {
			return new Echo<>(null);
		}
		
		// 如果回复类型为图片则查询图片路径
		int sendFileType = dto.getSendFileType();
		if(sendFileType == BizConstants.MSG_TYPE_IMAGE) {
			if (dto.getMaterialFileId() != null) {
				ShopFile shopFile = shopFileService.getShopFileBykey(shopId, dto.getMaterialFileId());
				if (shopFile != null) {
					dto.setFileUrl(shopFile.getFullUrl());
					return new Echo<>(new ReplyVO(dto, shopFile.getFullUrl()));
				}
			}
		}

		return new Echo<>(new ReplyVO(dto, ""));
	}
	
    /**
     * 新增或修改自动和关注回复
     * 
     * @param form
     * @return
     */
    @PostMapping(value = "/saveAFReply",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int insertOrUpdateAutomaticAndFocusReply(@RequestBody AutomaticReplyForm form) {
        form.setShopId(getShopId());
        form.setCreatedId(getUserId());
        form.setModifiedId(getUserId());
        return replyService.insertOrUpdateAutomaticAndFocusReply(form);
    }
    /************************************************自动回复和关注回复-end******************************************************************/
}
