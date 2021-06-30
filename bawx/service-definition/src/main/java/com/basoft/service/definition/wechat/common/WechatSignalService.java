package com.basoft.service.definition.wechat.common;

import com.basoft.core.ware.wechat.domain.msg.*;
import com.basoft.service.entity.wechat.appinfo.AppInfo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * WechatSignalService
 * 
 * @author basoft
 */
@Service
public interface WechatSignalService {
	public String processingAndReplyMessage(HttpServletRequest request,AppInfo appInfo) throws Exception;

	public Long insertTextMsg(Text text);

	public long insertImageMsg(Image image);

	public long insertVoiceMsg(Voice vocie);

	public long insertVideoMsg(Video video);

	public long insertLocationMsg(Location location);

	public long insertLinkMsg(Link link);

	public long insertEventLocation(EventLocation location);
}
