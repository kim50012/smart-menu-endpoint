package com.basoft.service.definition.wechat.wxMessage;

import com.basoft.service.dto.wechat.WxMessageDto;
import com.basoft.service.param.wechat.wxMessage.WxMessageQueryParam;
import com.github.pagehelper.PageInfo;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:14 2018/5/8
 **/
public interface WxMessageService {
	/**
	 * 查询客户的咨询消息（和公众号的聊天记录）
	 * 
	 * @param queryParam
	 * @return
	 */
	public PageInfo<WxMessageDto> finCustChatContent(WxMessageQueryParam queryParam);
}
