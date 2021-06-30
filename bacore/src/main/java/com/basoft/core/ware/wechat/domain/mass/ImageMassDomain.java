package com.basoft.core.ware.wechat.domain.mass;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

public class ImageMassDomain extends MassBase {

	public static void main(String[] args) {
		List<String> touser = new ArrayList<String>();
		touser.add("a");
		touser.add("b");
		touser.add("c");

		ImageMassDomain news = new ImageMassDomain();
		Media media = new Media();
		media.setMedia_id("aaaaaaaaaaaaa");

		news.setImage(media);
		news.setTouser(touser);

		JSONObject jsonObject = JSONObject.fromObject(news);
		System.out.println(jsonObject.toString());
	}

	private Media image;

	public ImageMassDomain() {
		super();
		super.setMsgtype("image");
	}

	public Media getImage() {
		return image;
	}

	public void setImage(Media image) {
		this.image = image;
	}
}
