package com.basoft.core.ware.wechat.domain.msg;

import java.util.ArrayList;
import java.util.List;

/**
 * 回复图文消息
 <xml>
	<ToUserName><![CDATA[toUser]]></ToUserName>
	<FromUserName><![CDATA[fromUser]]></FromUserName>
	<CreateTime>12345678</CreateTime>
	<MsgType><![CDATA[news]]></MsgType>
	<ArticleCount>2</ArticleCount>
	<Articles>
		<item>
			<Title><![CDATA[title1]]></Title> 
			<Description><![CDATA[description1]]></Description>
			<PicUrl><![CDATA[picurl]]></PicUrl>
			<Url><![CDATA[url]]></Url>
		</item>
		<item>
			<Title><![CDATA[title]]></Title>
			<Description><![CDATA[description]]></Description>
			<PicUrl><![CDATA[picurl]]></PicUrl>
			<Url><![CDATA[url]]></Url>
		</item>
	</Articles>
  </xml> 
 */
public class News {
	private String toUserName = "";
	private String fromUserName = "";
	private String createTime = "";

	private List<Article> articles = new ArrayList<Article>();

	public static void main(String[] args) {
		News news = new News("toUserName", "fromUserName", "createTime");
		Article a = new Article("标题", "说明地对地导弹", "http://localhost/3333.jpg", "3333");
		Article a2 = new Article("标题1", "说明地对地导弹1", "http://localhost/3333.jpg", "ccc");
		Article a3 = new Article("标题2", "说明地对地导弹2", "http://localhost/3333.jpg", "dddd");

		news.getArticles().add(a);
		news.getArticles().add(a2);
		news.getArticles().add(a3);

		System.out.println(news);
	}

	public News() {
		super();
	}

	public News(String toUserName, String fromUserName, String createTime) {
		super();
		this.toUserName = toUserName;
		this.fromUserName = fromUserName;
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		String format 	= "<xml>\n" 											
			            + 	"<ToUserName><![CDATA[%1$s]]></ToUserName>\n" 	 
			            + 	"<FromUserName><![CDATA[%2$s]]></FromUserName>\n" 
			            + 	"<CreateTime>%3$s</CreateTime>\n" 
			            + 	"<MsgType><![CDATA[news]]></MsgType>\n" 
			            + 	"<ArticleCount>%4$d</ArticleCount>\n" 		 
			            + 	"<Articles>\n" 		 
			            + 	"%5$s" 		 
			            + 	"</Articles>\n" 		 
			            + "</xml>\n" ;

		String item = "";
		for (int i = 0; i < articles.size() && i < 10; i++) {
			item += articles.get(i).toString();
		}

		return String.format(format, toUserName, fromUserName, createTime, getArticleCount(), item);
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	public int getArticleCount() {
		return articles.size();
	}
}