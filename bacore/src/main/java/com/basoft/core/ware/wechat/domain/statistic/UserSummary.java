package com.basoft.core.ware.wechat.domain.statistic;

/**
 * 获取用户增减数据（getusersummary) item - response
 */
public class UserSummary {
	/**
	 * 数据的日期
	 */
	private String ref_date;
	
	/**  
	* 用户的渠道，数值代表的含义如下：   
	*  		0 	代表其他合计 
	*  		1 	代表公众号搜索 
	*  		17	代表名片分享 
	*  		30	代表扫描二维码 
	*  		43	代表图文页右上角菜单 
	*  		51	代表支付后关注（在支付完成页） 
	*  		57	代表图文页内公众号名称 
	*  		75	代表公众号文章广告 
	*  		78代表朋友圈广告
	*/
	private Integer user_source;
	
	/**  
	* 新增的用户数量
	*/
	private Integer new_user;
	
	/**  
	* 取消关注的用户数量，new_user减去cancel_user即为净增用户数量
	*/
	private Integer cancel_user;

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

	public Integer getNew_user() {
		return new_user;
	}

	public void setNew_user(Integer new_user) {
		this.new_user = new_user;
	}

	public Integer getCancel_user() {
		return cancel_user;
	}

	public void setCancel_user(Integer cancel_user) {
		this.cancel_user = cancel_user;
	}

	@Override
	public String toString() {
		return "UserSummary [ref_date=" + ref_date + ", user_source=" + user_source + ", new_user=" + new_user
				+ ", cancel_user=" + cancel_user + "]";
	}
}