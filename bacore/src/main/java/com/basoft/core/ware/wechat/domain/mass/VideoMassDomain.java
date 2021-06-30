package com.basoft.core.ware.wechat.domain.mass;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

public class VideoMassDomain extends MassBase {

	public static void main(String[] args) {
		List<String> touser = new ArrayList<String>();
		touser.add("a");
		touser.add("b");
		touser.add("c");

		VideoMassDomain news = new VideoMassDomain();
		VideoMedia media = new VideoMedia();
		media.setMedia_id("aaaaaaaaaaaaa");

		news.setVideo(media);
		news.setTouser(touser);

		JSONObject jsonObject = JSONObject.fromObject(news);
		System.out.println(jsonObject.toString());
	}

	private VideoMedia video;

	public VideoMassDomain() {
		super();
		setMsgtype("video");
	}

	public VideoMedia getVideo() {
		return video;
	}

	public void setVideo(VideoMedia video) {
		this.video = video;
	}
}
