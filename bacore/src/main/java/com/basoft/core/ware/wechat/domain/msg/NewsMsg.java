package com.basoft.core.ware.wechat.domain.msg;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

/**
	{
	    "touser":"OPENID",
	    "msgtype":"news",
	    "news":{
	        "articles": [
	         {
	             "title":"Happy Day",
	             "description":"Is Really A Happy Day",
	             "url":"URL",
	             "picurl":"PIC_URL"
	         },
	         {
	             "title":"Happy Day",
	             "description":"Is Really A Happy Day",
	             "url":"URL",
	             "picurl":"PIC_URL"
	         }
	         ]
	    }
	}
*/
public class NewsMsg {
	private String touser = "";
	private List<Article> newsList = new ArrayList<Article>();

	public static void main(String[] args) {
		NewsMsg im = new NewsMsg("touser");
		im.getNewsList().add(new Article("1", "2", "3", "4"));
		im.getNewsList().add(new Article("1", "2", "3", "4"));
		im.getNewsList().add(new Article("1", "2", "3", "4"));
		System.out.println(im.toJsonString());
	}

	public NewsMsg() {
		super();
	}

	public NewsMsg(String touser) {
		super();
		this.touser = touser;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public List<Article> getNewsList() {
		return newsList;
	}

	public void setNewsList(List<Article> newsList) {
		this.newsList = newsList;
	}

	public JSONObject toJson() {
		JSONObject news = new JSONObject();

		List<JSONObject> articleList = new ArrayList<JSONObject>();

		for (int i = 0; i < newsList.size() && i < 10; i++) {
			JSONObject article = new JSONObject();
			article.put("title", newsList.get(i).getTitle());
			article.put("description", newsList.get(i).getDescription());
			article.put("url", newsList.get(i).getUrl());
			article.put("picurl", newsList.get(i).getPicurl());

			articleList.add(article);
		}

		JSONObject articles = new JSONObject();
		articles.put("articles", articleList);

		JSONObject root = new JSONObject();
		root.put("touser", touser);
		root.put("msgtype", "news");
		root.put("news", articles);

		return root;
	}

	public String toJsonString() {
		return toJson().toString();
	}
}