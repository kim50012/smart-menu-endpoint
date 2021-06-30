package com.basoft.core.ware.wechat.domain.mass;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

public class TextMassDomian extends MassBase {

	public static void main(String[] args) {
		List<String> touser = new ArrayList<String>();
		touser.add("a");
		touser.add("b");
		touser.add("c");

		TextMassDomian textMass = new TextMassDomian();
		Text text = new Text("hello from boxer.");

		textMass.setText(text);
		textMass.setTouser(touser);

		JSONObject jsonObject = JSONObject.fromObject(textMass);
		System.out.println(jsonObject.toString());
	}

	private Text text;

	public TextMassDomian() {
		super();
		// TODO Auto-generated constructor stub
		super.setMsgtype("text");
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}
}
