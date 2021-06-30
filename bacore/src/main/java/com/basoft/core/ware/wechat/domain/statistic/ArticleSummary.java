package com.basoft.core.ware.wechat.domain.statistic;

/**
 * 获取图文群发每日数据（getArticleSummary） item
 */
public class ArticleSummary {

	private Long shopId;
	/**
	 * 数据的日期，需在begin_date和end_date之间
	 */
	private String ref_date;

	private String ref_hour;

	private Integer user_source;

	/**
	 * 这里的msgid实际上是由msgid（图文消息id，这也就是群发接口调用后返回的msg_data_id）和index（消息次序索引）组成，
	 * 例如12003_3， 其中12003是msgid，即一次群发的消息的id；
	 * 3为index，假设该次群发的图文消息共5个文章（因为可能为多图文），3表示5个中的第3个
	 */
	private String msgid;

	private Long newsId;

	private Long msgId;

	/**
	 * 图文消息的标题
	 */
	private String title;

	/**
	 * 图文页（点击群发图文卡片进入的页面）的阅读人数
	 */
	private Integer int_page_read_user;

	/**
	 * 图文页的阅读次数
	 */
	private Integer int_page_read_count;

	/**
	 * 原文页（点击图文页“阅读原文”进入的页面）的阅读人数，无原文页时此处数据为0
	 */
	private Integer ori_page_read_user;

	/**
	 * 原文页的阅读次数
	 */
	private Integer ori_page_read_count;

	/**
	 * 分享的人数
	 */
	private Integer share_user;

	/**
	 * 分享的次数
	 */
	private Integer share_count;

	/**
	 * 收藏的人数
	 */
	private Integer add_to_fav_user;

	/**
	 * 收藏的次数
	 */
	private Integer add_to_fav_count;

	private Integer target_user;

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getRef_hour() {
		return ref_hour;
	}

	public void setRef_hour(String ref_hour) {
		this.ref_hour = ref_hour;
	}

	public String getRef_date() {
		return ref_date;
	}

	public void setRef_date(String ref_date) {
		this.ref_date = ref_date;
	}

	public Integer getUser_source() {
		return user_source;
	}

	public void setUser_source(Integer user_source) {
		this.user_source = user_source;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
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

	public Integer getInt_page_read_user() {
		return int_page_read_user;
	}

	public void setInt_page_read_user(Integer int_page_read_user) {
		this.int_page_read_user = int_page_read_user;
	}

	public Integer getInt_page_read_count() {
		return int_page_read_count;
	}

	public void setInt_page_read_count(Integer int_page_read_count) {
		this.int_page_read_count = int_page_read_count;
	}

	public Integer getOri_page_read_user() {
		return ori_page_read_user;
	}

	public void setOri_page_read_user(Integer ori_page_read_user) {
		this.ori_page_read_user = ori_page_read_user;
	}

	public Integer getOri_page_read_count() {
		return ori_page_read_count;
	}

	public void setOri_page_read_count(Integer ori_page_read_count) {
		this.ori_page_read_count = ori_page_read_count;
	}

	public Integer getShare_user() {
		return share_user;
	}

	public void setShare_user(Integer share_user) {
		this.share_user = share_user;
	}

	public Integer getShare_count() {
		return share_count;
	}

	public void setShare_count(Integer share_count) {
		this.share_count = share_count;
	}

	public Integer getAdd_to_fav_user() {
		return add_to_fav_user;
	}

	public void setAdd_to_fav_user(Integer add_to_fav_user) {
		this.add_to_fav_user = add_to_fav_user;
	}

	public Integer getAdd_to_fav_count() {
		return add_to_fav_count;
	}

	public void setAdd_to_fav_count(Integer add_to_fav_count) {
		this.add_to_fav_count = add_to_fav_count;
	}

	public Integer getTarget_user() {
		return target_user;
	}

	public void setTarget_user(Integer target_user) {
		this.target_user = target_user;
	}

	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}


	@Override
	public String toString() {
		return "ArticleSummary [ref_date=" + ref_date + ", user_source=" + user_source + ", msgid=" + msgid + ", title="
				+ title + ", int_page_read_user=" + int_page_read_user + ", int_page_read_count=" + int_page_read_count
				+ ", ori_page_read_user=" + ori_page_read_user + ", ori_page_read_count=" + ori_page_read_count
				+ ", share_user=" + share_user + ", share_count=" + share_count + ", add_to_fav_user=" + add_to_fav_user
				+ ", add_to_fav_count=" + add_to_fav_count + ",target_user="+target_user+"]";
	}
}