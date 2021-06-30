package com.basoft.service.impl.wechat.wxMessage;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.basoft.service.dao.wechat.wxMessage.WxMessageMapper;
import com.basoft.service.definition.wechat.wxMessage.WxMessageService;
import com.basoft.service.dto.wechat.WxMessageDto;
import com.basoft.service.param.wechat.wxMessage.WxMessageQueryParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:17 2018/5/8
 **/
@Service
public class WxMessageServiceImpl implements WxMessageService {
	@Autowired
	private WxMessageMapper wxMessageMapper;

	/**
	 * 查询客户的咨询消息（和公众号的聊天记录）
	 * 
	 * @param queryParam
	 * @return
	 */
	@Override
	public PageInfo<WxMessageDto> finCustChatContent(WxMessageQueryParam queryParam) {
		PageHelper.startPage(queryParam.getPage(), queryParam.getRows());
		List<WxMessageDto> dtoList = wxMessageMapper.finCustChatContent(queryParam);
		dtoList.stream().map(dto -> {
			String nick = baseConvertStr(dto.getCustNickNm());
			dto.setCustNickNm(nick);
			return dto;
		}).collect(Collectors.toList());

		return new PageInfo<>(dtoList);
	}

	private static String baseConvertStr(String str) {
		if (null != str) {
			Base64.Decoder decoder = Base64.getDecoder();
			try {
				return new String(decoder.decode(str.getBytes()), "UTF-8");
			} catch (Exception e) {
				return str;
			}
		}
		return null;

	}
}
