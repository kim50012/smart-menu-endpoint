package com.basoft.core.ware.wechat.domain.statistic;

import java.sql.Date;

public class ArticleGroupDetails {
	private Long id;

	private Long shopId;

	private String ref_date;

	private String ref_hour;

	private String msgid;

	private String title;

	private Integer userSource;

	private String stat_date;

	private Integer target_user = 0;

	private Integer int_page_read_user = 0;

	private Integer int_page_read_count = 0;

	private Integer ori_page_read_user = 0;

	private Integer ori_page_read_count = 0;

	private Integer share_scene;// 分享的场景 1代表好友转发 2代表朋友圈 3代表腾讯微博 255代表其他

	private Integer share_user = 0;

	private Integer share_count = 0;

	private Integer add_to_fav_user = 0;

	private Integer add_to_fav_count = 0;

	private Integer int_page_from_session_read_user = 0;

	private Integer int_page_from_session_read_count = 0;

	private Integer int_page_from_hist_msg_read_user = 0;

	private Integer int_page_from_hist_msg_read_count = 0;

	private Integer int_page_from_feed_read_user = 0;

	private Integer int_page_from_feed_read_count = 0;

	private Integer int_page_from_friends_read_user = 0;

	private Integer int_page_from_friends_read_count = 0;

	private Integer int_page_from_other_read_user = 0;

	private Integer int_page_from_other_read_count = 0;

	private Integer int_page_fromkanyikan_read_user = 0;// 看一看

	private Integer int_page_fromkanyikan_read_count = 0;

	private Integer int_page_souyisou_read_user = 0;// 搜一搜

	private Integer int_page_souyisou_read_count = 0;// 搜一搜

	private Integer feed_share_from_session_user = 0;

	private Integer feed_share_from_session_cnt = 0;

	private Integer feed_share_from_feed_user = 0;

	private Integer feed_share_from_feed_cnt = 0;

	private Integer feed_share_from_other_user = 0;

	private Integer feed_share_from_other_cnt = 0;

	private Date createDt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getRef_date() {
		return ref_date;
	}

	public void setRef_date(String ref_date) {
		this.ref_date = ref_date;
	}

	public String getRef_hour() {
		return ref_hour;
	}

	public void setRef_hour(String ref_hour) {
		this.ref_hour = ref_hour;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getUserSource() {
		return userSource;
	}

	public void setUserSource(Integer userSource) {
		this.userSource = userSource;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public String getStat_date() {
		return stat_date;
	}

	public void setStat_date(String stat_date) {
		this.stat_date = stat_date;
	}

	public Integer getTarget_user() {
		return target_user;
	}

	public void setTarget_user(Integer target_user) {
		if (target_user != null)
			this.target_user = target_user;
	}

	public Integer getInt_page_read_user() {
		return int_page_read_user;
	}

	public void setInt_page_read_user(Integer int_page_read_user) {
		if (int_page_read_user != null)
			this.int_page_read_user = int_page_read_user;
	}

	public Integer getInt_page_read_count() {
		return int_page_read_count;
	}

	public void setInt_page_read_count(Integer int_page_read_count) {
		if (int_page_read_count != null)
			this.int_page_read_count = int_page_read_count;
	}

	public Integer getOri_page_read_user() {
		return ori_page_read_user;
	}

	public void setOri_page_read_user(Integer ori_page_read_user) {
		if (ori_page_read_user != null)
			this.ori_page_read_user = ori_page_read_user;
	}

	public Integer getOri_page_read_count() {
		return ori_page_read_count;
	}

	public Integer getShare_scene() {
		return share_scene;
	}

	public void setShare_scene(Integer share_scene) {
		this.share_scene = share_scene;
	}

	public void setOri_page_read_count(Integer ori_page_read_count) {
		if (ori_page_read_count != null)
			this.ori_page_read_count = ori_page_read_count;
	}

	public Integer getShare_user() {
		return share_user;
	}

	public void setShare_user(Integer share_user) {
		if (share_user != null)
			this.share_user = share_user;
	}

	public Integer getShare_count() {
		return share_count;
	}

	public void setShare_count(Integer share_count) {
		if (share_count != null)
			this.share_count = share_count;
	}

	public Integer getAdd_to_fav_user() {
		return add_to_fav_user;
	}

	public void setAdd_to_fav_user(Integer add_to_fav_user) {
		if (add_to_fav_user != null)
			this.add_to_fav_user = add_to_fav_user;
	}

	public Integer getAdd_to_fav_count() {
		return add_to_fav_count;
	}

	public void setAdd_to_fav_count(Integer add_to_fav_count) {
		if (add_to_fav_count != null)
			this.add_to_fav_count = add_to_fav_count;
	}

	public Integer getInt_page_from_session_read_user() {
		return int_page_from_session_read_user;
	}

	public void setInt_page_from_session_read_user(Integer int_page_from_session_read_user) {
		if (int_page_from_session_read_user != null)
			this.int_page_from_session_read_user = int_page_from_session_read_user;
	}

	public Integer getInt_page_from_session_read_count() {
		return int_page_from_session_read_count;
	}

	public void setInt_page_from_session_read_count(Integer int_page_from_session_read_count) {
		if (int_page_from_session_read_count != null)
			this.int_page_from_session_read_count = int_page_from_session_read_count;
	}

	public Integer getInt_page_from_hist_msg_read_user() {
		return int_page_from_hist_msg_read_user;
	}

	public void setInt_page_from_hist_msg_read_user(Integer int_page_from_hist_msg_read_user) {
		if (int_page_from_hist_msg_read_user != null)
			this.int_page_from_hist_msg_read_user = int_page_from_hist_msg_read_user;
	}

	public Integer getInt_page_from_hist_msg_read_count() {
		return int_page_from_hist_msg_read_count;
	}

	public void setInt_page_from_hist_msg_read_count(Integer int_page_from_hist_msg_read_count) {
		if (int_page_from_hist_msg_read_count != null)
			this.int_page_from_hist_msg_read_count = int_page_from_hist_msg_read_count;
	}

	public Integer getInt_page_from_feed_read_user() {
		return int_page_from_feed_read_user;
	}

	public void setInt_page_from_feed_read_user(Integer int_page_from_feed_read_user) {
		if (int_page_from_feed_read_user != null)
			this.int_page_from_feed_read_user = int_page_from_feed_read_user;
	}

	public Integer getInt_page_from_feed_read_count() {
		return int_page_from_feed_read_count;
	}

	public void setInt_page_from_feed_read_count(Integer int_page_from_feed_read_count) {
		if (int_page_from_feed_read_count != null)
			this.int_page_from_feed_read_count = int_page_from_feed_read_count;
	}

	public Integer getInt_page_from_friends_read_user() {
		return int_page_from_friends_read_user;
	}

	public void setInt_page_from_friends_read_user(Integer int_page_from_friends_read_user) {
		if (int_page_from_friends_read_user != null)
			this.int_page_from_friends_read_user = int_page_from_friends_read_user;
	}

	public Integer getInt_page_from_friends_read_count() {
		return int_page_from_friends_read_count;
	}

	public void setInt_page_from_friends_read_count(Integer int_page_from_friends_read_count) {
		if (int_page_from_friends_read_count != null)
			this.int_page_from_friends_read_count = int_page_from_friends_read_count;
	}

	public Integer getInt_page_from_other_read_user() {
		return int_page_from_other_read_user;
	}

	public void setInt_page_from_other_read_user(Integer int_page_from_other_read_user) {
		if (int_page_from_other_read_user != null)
			this.int_page_from_other_read_user = int_page_from_other_read_user;
	}

	public Integer getInt_page_from_other_read_count() {
		return int_page_from_other_read_count;
	}

	public void setInt_page_from_other_read_count(Integer int_page_from_other_read_count) {
		if (int_page_from_other_read_count != null)
			this.int_page_from_other_read_count = int_page_from_other_read_count;
	}

	public Integer getInt_page_fromkanyikan_read_user() {
		return int_page_fromkanyikan_read_user;
	}

	public void setInt_page_fromkanyikan_read_user(Integer int_page_fromkanyikan_read_user) {
		if (int_page_fromkanyikan_read_user != null)
			this.int_page_fromkanyikan_read_user = int_page_fromkanyikan_read_user;
	}

	public Integer getInt_page_fromkanyikan_read_count() {
		return int_page_fromkanyikan_read_count;
	}

	public void setInt_page_fromkanyikan_read_count(Integer int_page_fromkanyikan_read_count) {
		if (int_page_fromkanyikan_read_count != null)
			this.int_page_fromkanyikan_read_count = int_page_fromkanyikan_read_count;
	}

	public Integer getInt_page_souyisou_read_user() {
		return int_page_souyisou_read_user;
	}

	public void setInt_page_souyisou_read_user(Integer int_page_souyisou_read_user) {
		if (int_page_souyisou_read_user != null)
			this.int_page_souyisou_read_user = int_page_souyisou_read_user;
	}

	public Integer getInt_page_souyisou_read_count() {
		return int_page_souyisou_read_count;
	}

	public void setInt_page_souyisou_read_count(Integer int_page_souyisou_read_count) {
		if (int_page_souyisou_read_count != null)
			this.int_page_souyisou_read_count = int_page_souyisou_read_count;
	}

	public Integer getFeed_share_from_session_user() {
		return feed_share_from_session_user;
	}

	public void setFeed_share_from_session_user(Integer feed_share_from_session_user) {
		if (feed_share_from_session_user != null)
			this.feed_share_from_session_user = feed_share_from_session_user;
	}

	public Integer getFeed_share_from_session_cnt() {
		return feed_share_from_session_cnt;
	}

	public void setFeed_share_from_session_cnt(Integer feed_share_from_session_cnt) {
		if (feed_share_from_session_cnt != null)
			this.feed_share_from_session_cnt = feed_share_from_session_cnt;
	}

	public Integer getFeed_share_from_feed_user() {
		return feed_share_from_feed_user;
	}

	public void setFeed_share_from_feed_user(Integer feed_share_from_feed_user) {
		if (feed_share_from_feed_user != null)
			this.feed_share_from_feed_user = feed_share_from_feed_user;
	}

	public Integer getFeed_share_from_feed_cnt() {
		return feed_share_from_feed_cnt;
	}

	public void setFeed_share_from_feed_cnt(Integer feed_share_from_feed_cnt) {
		if (feed_share_from_feed_cnt != null)
			this.feed_share_from_feed_cnt = feed_share_from_feed_cnt;
	}

	public Integer getFeed_share_from_other_user() {
		return feed_share_from_other_user;
	}

	public void setFeed_share_from_other_user(Integer feed_share_from_other_user) {
		if (feed_share_from_other_user != null)
			this.feed_share_from_other_user = feed_share_from_other_user;
	}

	public Integer getFeed_share_from_other_cnt() {
		return feed_share_from_other_cnt;
	}

	public void setFeed_share_from_other_cnt(Integer feed_share_from_other_cnt) {
		if (feed_share_from_other_cnt != null)
			this.feed_share_from_other_cnt = feed_share_from_other_cnt;
	}
}