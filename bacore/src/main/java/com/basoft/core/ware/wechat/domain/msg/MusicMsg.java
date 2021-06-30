package com.basoft.core.ware.wechat.domain.msg;

import net.sf.json.JSONObject;

/**
{
    "touser":"OPENID",
    "msgtype":"music",
    "music":
    {
      "title":"MUSIC_TITLE",
      "description":"MUSIC_DESCRIPTION",
      "musicurl":"MUSIC_URL",
      "hqmusicurl":"HQ_MUSIC_URL",
      "thumb_media_id":"THUMB_MEDIA_ID" 
    }
}
 */
public class MusicMsg {
	private String touser = "";
	private String musicUrl = "";
	private String hqMusicUrl = "";
	private String thumbMediaId = "";
	private String title = "";
	private String description = "";

	public static void main(String[] args) {
		MusicMsg im = new MusicMsg("touser", "musicUrl", "hqMusicUrl", "thumbMediaId", "title", "description");
		System.out.println(im.toJsonString());
	}

	public MusicMsg() {
		super();
	}

	public MusicMsg(String touser, String musicUrl, String hqMusicUrl, String thumbMediaId) {
		super();
		this.touser = touser;
		this.musicUrl = musicUrl;
		this.hqMusicUrl = hqMusicUrl;
		this.thumbMediaId = thumbMediaId;
	}

	public MusicMsg(String touser, String musicUrl, String hqMusicUrl, String thumbMediaId, String title,
			String description) {
		super();
		this.touser = touser;
		this.musicUrl = musicUrl;
		this.hqMusicUrl = hqMusicUrl;
		this.thumbMediaId = thumbMediaId;
		this.title = title;
		this.description = description;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
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

	public String getMusicUrl() {
		return musicUrl;
	}

	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}

	public String getHqMusicUrl() {
		return hqMusicUrl;
	}

	public void setHqMusicUrl(String hqMusicUrl) {
		this.hqMusicUrl = hqMusicUrl;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public JSONObject toJson() {
		JSONObject music = new JSONObject();
		music.put("title", title);
		music.put("description", description);
		music.put("musicurl", musicUrl);
		music.put("hqmusicurl", hqMusicUrl);
		music.put("thumb_media_id", thumbMediaId);

		JSONObject root = new JSONObject();
		root.put("touser", touser);
		root.put("msgtype", "music");
		root.put("music", music);

		return root;
	}

	public String toJsonString() {
		return toJson().toString();
	}
}