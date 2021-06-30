package com.basoft.core.ware.wechat.domain.statistic;

/** 
 *  获取累计用户数据（getusercumulate） item - response
 */
public class UserCumulate {
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
	* 总用户量
	*/
	private Integer cumulate_user;

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

	public Integer getCumulate_user() {
		return cumulate_user;
	}

	public void setCumulate_user(Integer cumulate_user) {
		this.cumulate_user = cumulate_user;
	}

	@Override
	public String toString() {
		return "UserCumulate [ref_date=" + ref_date + ", user_source=" + user_source + ", cumulate_user="
				+ cumulate_user + "]";
	}
}