package com.basoft.core.ware.wechat.domain.template;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

public class GetTemplateIdReturn extends WeixinReturn {
	private String template_id;

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
}