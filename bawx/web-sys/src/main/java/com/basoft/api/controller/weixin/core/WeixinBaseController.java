package com.basoft.api.controller.weixin.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.basoft.api.controller.BaseController;
import com.basoft.core.ware.common.framework.utilities.JsonUtil;

public class WeixinBaseController extends BaseController {
	protected Map<String, Object> requestPocket = new HashMap<String, Object>();

	protected Map<String, Object> sessionPocket = new HashMap<String, Object>();

	protected Map<String, Object> applicationPocket = new HashMap<String, Object>();

	protected final transient Log logger = LogFactory.getLog(WeixinBaseController.class);

	protected String key = "";

	public String getBasePath() {
		String path = request.getContextPath();
		int serverPort = request.getServerPort();
		String basePath = "";
		if (80 == serverPort) {
			basePath = request.getScheme() + "://" + request.getServerName() + path + "/";
		} else {
			basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		}
		return basePath;
	}

	public String getFullUrl() {
		String path = request.getContextPath();
		int serverPort = request.getServerPort();
		String basePath = "";
		if (80 == serverPort) {
			basePath = request.getScheme() + "://" + request.getServerName() + path;
		} else {
			basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
		}
		String url = basePath + request.getRequestURI();
		if (StringUtils.isNotEmpty(request.getQueryString())) {
			url += "?" + request.getQueryString();
		}
		return url;
	}

	/*protected String getLabel(String key){
		Map<String,String> label = LabelUtil.getLabelMap(getServletContext());	
		String val = label.get(key);
		if(StringUtils.isBlank(val)){
			val = key;
		}
		return val;
	}*/
	
	protected void render(String text, String contentType) {
		try {
			response.setContentType(contentType);
			response.getWriter().write(text);
		} catch (IOException e) {
			this.logger.error(e.getMessage(), e);
		}
	}

	protected void renderText(String text) {
		render(text, "text/plain;charset=UTF-8");
	}

	protected void renderHtml(String html) {
		render(html, "text/html;charset=UTF-8");
	}

	protected void renderXML(String xml) {
		render(xml, "text/xml;charset=UTF-8");
	}

	protected void renderJSON(Object value) {
		if (value == null) {
			render("{}", "application/json;charset=UTF-8");
		} else {
			String json = JsonUtil.getJsonString(value);
			render(json, "application/json;charset=UTF-8");
		}
	}

	protected void renderJSON(String value) {
		if (value == null) {
			render("{}", "application/json;charset=UTF-8");
		} else {
			render(value, "application/json;charset=UTF-8");
		}
	}

	@SuppressWarnings("rawtypes")
	protected void renderJSON(List value) {
		if (value == null) {
			render("{}", "application/json;charset=UTF-8");
		} else {
			String json = JsonUtil.getJsonString(value);
			render(json, "application/json;charset=UTF-8");
		}
	}

	@SuppressWarnings("rawtypes")
	protected void renderNoTitleJSON(List value) {
		if (value == null) {
			render("{}", "application/json;charset=UTF-8");
		} else {
			String json = JsonUtil.getNoTitleJsonString(value);
			render(json, "application/json;charset=UTF-8");
		}
	}

	@SuppressWarnings("rawtypes")
	public void OutputJSON(List value) {
		String responseData = JsonUtil.toJSONString(value);
		response.setContentType("application/json; charset=UTF-8;");
		response.setBufferSize(8192000);
		try {
			if (value == null) {
				response.getWriter().print("{}");
			} else {
				response.getWriter().print(responseData);
			}
		} catch (IOException e) {
		}
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
