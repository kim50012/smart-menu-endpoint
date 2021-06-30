package com.basoft.core.ware.wechat.domain.msg;

import net.sf.json.JSONObject;

/**
	{
	    "touser":"OPENID",
	    "msgtype":"text",
	    "text":
	    {
	         "content":"Hello World"
	    }
	}
 */
public class TextMsg {
	private String touser = "";
	private String content = "";

	public static void main(String[] args) {
		TextMsg mx = new TextMsg("1111", "hello");
		System.out.println(mx.toJsonString());
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public TextMsg() {
		super();
	}

	public TextMsg(String touser, String content) {
		super();
		this.touser = touser;
		this.content = content;
	}

	public JSONObject toJson() {
		JSONObject text = new JSONObject();
		text.put("content", content);

		JSONObject root = new JSONObject();
		root.put("touser", touser);
		root.put("msgtype", "text");
		root.put("text", text);

		return root;
	}

	public String toJsonString() {
		return toJson().toString();
	}
}