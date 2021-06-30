package com.basoft.core.ware.wechat.domain.statistic;

import java.util.List;

/**
 * 获取图文群发总数据（getarticletotal） item
 */
public class ArticleTotal {
	/**
	 * ref_date指的是文章群发出日期
	 */
	private String ref_date;

	/**
	 * 这里的msgid实际上是由msgid（图文消息id）和index（消息次序索引）组成， 例如12003_3，
	 * 其中12003是msgid，即一次群发的id消息的； 3为index， 假设该次群发的图文消息共5个文章（因为可能为多图文）， 3表示5个中的第3个
	 */
	private String msgid;

	/**
	 * 图文消息的标题
	 */
	private String title;

	private Integer user_source;

	private List<ArticleDetail> details;

	public String getRef_date() {
		return ref_date;
	}

	public void setRef_date(String ref_date) {
		this.ref_date = ref_date;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getWx_msgid() {

		if (msgid != null && !"".equals(msgid)) {
			return msgid.split("_")[0];
		} else {
			return null;
		}

	}

	public String getWx_newsid() {
		if (msgid != null && !"".equals(msgid)) {
			return msgid.split("_")[1];
		} else {
			return null;
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getUser_source() {
		return user_source;
	}

	public void setUser_source(Integer user_source) {
		this.user_source = user_source;
	}

	public List<ArticleDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ArticleDetail> details) {
		this.details = details;
	}
}