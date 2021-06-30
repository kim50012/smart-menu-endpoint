package com.basoft.core.ware.wechat.domain.msg;

import net.sf.json.JSONObject;

/**
 {
    "touser":"OPENID",
    "msgtype":"image",
    "image":
    {
      "media_id":"MEDIA_ID"
    }
 }
 */
public class ImageMsg {
	private String touser = "";
	private String mediaId = "";

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public ImageMsg() {
		super();
	}

	public ImageMsg(String touser, String mediaId) {
		super();
		this.touser = touser;
		this.mediaId = mediaId;
	}

	public JSONObject toJson() {
		JSONObject image = new JSONObject();
		image.put("media_id", mediaId);

		JSONObject root = new JSONObject();
		root.put("touser", touser);
		root.put("msgtype", "image");
		root.put("image", image);

		return root;
	}

	public String toJsonString() {
		return toJson().toString();
	}

	public static void main(String[] args) {
		ImageMsg im = new ImageMsg("1111", "222");
		System.out.println(im.toJsonString());
	}
}