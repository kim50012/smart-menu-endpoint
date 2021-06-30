package com.basoft.service.impl.wechat.common.thread;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.basoft.core.ware.wechat.domain.msg.Image;
import com.basoft.service.entity.wechat.appinfo.AppInfo;

/**
 * 下载用户在微信对话窗口中上传的图片
 */
@SuppressWarnings("all")
public class DownloadImageMsgThread implements Runnable {
	private final transient Log logger = LogFactory.getLog(this.getClass());

	private HttpServletRequest request;

	private AppInfo appInfo;

	private Image image;

	public DownloadImageMsgThread() {
	}

	public DownloadImageMsgThread(HttpServletRequest request, AppInfo appInfo, Image image) {
		this.request = request;
		this.appInfo = appInfo;
		this.image = image;
	}

	// TODO YANCUNLING
	@Override
	public void run() {
		logger.info("=================DownloadImageMsgThread start=====================");
		/*WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
		WeixinMediaService weixinMediaService = wac.getBean(WeixinMediaService.class);
		WeixinMessageDao weixinMessageDao = wac.getBean(WeixinMessageDao.class);
		
		FileItem fileItme = weixinMediaService.downloadFile(request, appInfo.getShopId(), image.getMediaId(), DownloadFileType.get(1), null);
		
		image.setFileId(fileItme.getFileId());
		weixinMessageDao.insertImageMsg(image);*/
		logger.info("=================DownloadImageMsgThread end=====================");
	}
}