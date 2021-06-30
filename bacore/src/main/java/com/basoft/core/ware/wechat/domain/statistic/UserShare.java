package com.basoft.core.ware.wechat.domain.statistic;

/** 
 * @Description  
 *  response item
 *   获取图文分享转发分时数据（getusersharehour）
 *   获取图文分享转发数据（getusershare）
 */
public class UserShare {
	/**  
	* 数据的日期，需在begin_date和end_date之间
	*/
	private String ref_date;
	
	/**  
	* 数据的小时，包括从000到2300，分别代表的是[000,100)到[2300,2400)，即每日的第1小时和最后1小时
	* 注：“获取图文分享转发分时数据”接口用于此字段
	*/
	private Integer ref_hour;
	
	/**  
	* 注：接口文档未定义
	*/
	private Integer user_source;
	
	/**  
	* 分享的场景 1代表好友转发 2代表朋友圈 3代表腾讯微博 255代表其他
	*/
	private Integer share_scene;
	
	/**  
	* 分享的次数
	*/
	private Integer share_count;
	
	/**  
	* 分享的人数
	*/
	private Integer share_user;

	public String getRef_date() {
		return ref_date;
	}

	public void setRef_date(String ref_date) {
		this.ref_date = ref_date;
	}

	public Integer getRef_hour() {
		return ref_hour;
	}

	public void setRef_hour(Integer ref_hour) {
		this.ref_hour = ref_hour;
	}

	public Integer getUser_source() {
		return user_source;
	}

	public void setUser_source(Integer user_source) {
		this.user_source = user_source;
	}

	public Integer getShare_scene() {
		return share_scene;
	}

	public void setShare_scene(Integer share_scene) {
		this.share_scene = share_scene;
	}

	public Integer getShare_count() {
		return share_count;
	}

	public void setShare_count(Integer share_count) {
		this.share_count = share_count;
	}

	public Integer getShare_user() {
		return share_user;
	}

	public void setShare_user(Integer share_user) {
		this.share_user = share_user;
	}
}