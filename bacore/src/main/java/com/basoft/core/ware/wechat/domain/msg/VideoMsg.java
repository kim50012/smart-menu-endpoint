package com.basoft.core.ware.wechat.domain.msg;

import net.sf.json.JSONObject;

/**
{
	"touser": "OPENID",
	"msgtype": "video",
	"video": {
		"media_id": "MEDIA_ID",
		"thumb_media_id": "MEDIA_ID",
		"title": "TITLE",
		"description": "DESCRIPTION"
	}
}
 */
public class VideoMsg {
	private String touser = "";
	private String mediaId = "";
	private String thumbMediaId = "";
	private String title = "";
	private String description = "";

	public static void main(String[] args) {
		VideoMsg im = new VideoMsg("touser", "mediaId", "thumbMediaId", "title", "description");
		System.out.println(im.toJsonString());
	}

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

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public VideoMsg() {
		super();
	}

	public VideoMsg(String touser, String mediaId) {
		super();
		this.touser = touser;
		this.mediaId = mediaId;
	}

	public VideoMsg(String touser, String mediaId, String thumbMediaId) {
		super();
		this.touser = touser;
		this.mediaId = mediaId;
		this.thumbMediaId = thumbMediaId;
	}

	public VideoMsg(String touser, String mediaId, String thumbMediaId, String title, String description) {
		super();
		this.touser = touser;
		this.mediaId = mediaId;
		this.thumbMediaId = thumbMediaId;
		this.title = title;
		this.description = description;
	}

	public JSONObject toJson() {
		JSONObject video = new JSONObject();
		video.put("media_id", mediaId);
		video.put("thumb_media_id", thumbMediaId);
		video.put("title", title);
		video.put("description", description);

		JSONObject root = new JSONObject();
		root.put("touser", touser);
		root.put("msgtype", "video");
		root.put("video", video);

		return root;
	}

	public String toJsonString() {
		return toJson().toString();
	}
}