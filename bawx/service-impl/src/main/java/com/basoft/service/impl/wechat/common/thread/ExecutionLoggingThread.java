package com.basoft.service.impl.wechat.common.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.basoft.core.ware.wechat.domain.ExecutionLog;
import com.basoft.core.ware.wechat.domain.WeixinPageExecutionLog;
import com.basoft.core.ware.wechat.domain.WeixinSessionPageExeLog;
import com.basoft.core.ware.wechat.util.Constants;
import com.basoft.service.definition.wechat.common.WechatService;

public class ExecutionLoggingThread implements Runnable {
	private final transient Log logger = LogFactory.getLog(this.getClass());

	private WechatService wechatService;

	private String type;

	private Object log;

	public ExecutionLoggingThread(WechatService wechatService, String type, Object log) {
		this.wechatService = wechatService;
		this.type = type;
		this.log = log;
	}

	@Override
	public void run() {
		logger.info("=================ExecutionLoggingThread start=====================");
		try {
			if (type.equals(Constants.LogType.WX_SERVER_IF)) {
				wechatService.insertExecutionLog((ExecutionLog) log);
			} else if (type.equals(Constants.LogType.SESSION_PAGE)) {
				wechatService.insertWeixinSessionPageExeLog((WeixinSessionPageExeLog) log);
			} else if (type.equals(Constants.LogType.SHARE_SETTIONG)) {
				wechatService.insertWeixinPageExecutionLog((WeixinPageExecutionLog) log);
			}
		} catch (Exception e) {
		}
		logger.info("=================ExecutionLoggingThread end=====================");
	}
}