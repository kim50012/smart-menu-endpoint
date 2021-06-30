package com.basoft.core.ware.wechat.domain.mass;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

public class VoiceMassDomain extends MassBase {

	public static void main(String[] args) {
		List<String> touser = new ArrayList<String>();
		touser.add("a");
		touser.add("b");
		touser.add("c");

		VoiceMassDomain news = new VoiceMassDomain();
		Media media = new Media();
		media.setMedia_id("aaaaaaaaaaaaa");

		news.setVoice(media);
		news.setTouser(touser);

		JSONObject jsonObject = JSONObject.fromObject(news);
		System.out.println(jsonObject.toString());
	}

	private Media voice;

	public VoiceMassDomain() {
		super();
		setMsgtype("voice");
	}

	public Media getVoice() {
		return voice;
	}

	public void setVoice(Media voice) {
		this.voice = voice;
	}
}
