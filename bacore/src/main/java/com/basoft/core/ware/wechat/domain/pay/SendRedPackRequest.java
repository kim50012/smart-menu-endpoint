package com.basoft.core.ware.wechat.domain.pay;

/**
 * 红包发放
 * http://pay.weixin.qq.com/wiki/doc/api/cash_coupon.php?chapter=13_5
 */
public class SendRedPackRequest {
	private String nonce_str ; 		//随机字符串 (R) 随机字符串，不长于32位
	private String sign ; 			//签名  (R) 
	private String mch_billno ; 	//商户订单号 (R) （每个订单号必须唯一）
									//组成： mch_id+yyyymmdd+10位一天内不能重复的数字。
									//接口根据商户订单号支持重入， 如出现超时可再调用。
	private String mch_id; 			//微信支付分配的商户号 (R) 
	private String sub_mch_id; 		//微信支付分配的子商户号，受理模式下必填
	private String wxappid; 		//公众账号appid (R) 
	private String nick_name; 		//提供方名称 (R) 
	private String send_name; 		//红包发送者名称 (R) 
	private String re_openid; 		//接受收红包的用户 - 用户在wxappid下的openid (R) 
	private Integer total_amount; 	//付款金额，单位分 (R) 
	private Integer min_value; 		//最小红包金额，单位分 (R) 
	private Integer max_value; 		//最大红包金额，单位分 (R) 
	private Integer total_num; 		//红包发放总人数 (R) 
	private String wishing; 		//红包祝福语 (R) 
	private String client_ip; 		//Ip地址 (R) 
	private String act_name; 		//活动名称 (R) 
	private String remark; 			//备注 (R) 
	private String logo_imgurl; 	//商户logo的url
	private String share_content; 	//分享文案
	private String share_url; 		//分享链接
	private String share_imgurl; 	//分享的图片

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getMch_billno() {
		return mch_billno;
	}

	public void setMch_billno(String mch_billno) {
		this.mch_billno = mch_billno;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getSub_mch_id() {
		return sub_mch_id;
	}

	public void setSub_mch_id(String sub_mch_id) {
		this.sub_mch_id = sub_mch_id;
	}

	public String getWxappid() {
		return wxappid;
	}

	public void setWxappid(String wxappid) {
		this.wxappid = wxappid;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getSend_name() {
		return send_name;
	}

	public void setSend_name(String send_name) {
		this.send_name = send_name;
	}

	public String getRe_openid() {
		return re_openid;
	}

	public void setRe_openid(String re_openid) {
		this.re_openid = re_openid;
	}

	public Integer getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(Integer total_amount) {
		this.total_amount = total_amount;
	}

	public Integer getMin_value() {
		return min_value;
	}

	public void setMin_value(Integer min_value) {
		this.min_value = min_value;
	}

	public Integer getMax_value() {
		return max_value;
	}

	public void setMax_value(Integer max_value) {
		this.max_value = max_value;
	}

	public Integer getTotal_num() {
		return total_num;
	}

	public void setTotal_num(Integer total_num) {
		this.total_num = total_num;
	}

	public String getWishing() {
		return wishing;
	}

	public void setWishing(String wishing) {
		this.wishing = wishing;
	}

	public String getClient_ip() {
		return client_ip;
	}

	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	public String getAct_name() {
		return act_name;
	}

	public void setAct_name(String act_name) {
		this.act_name = act_name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLogo_imgurl() {
		return logo_imgurl;
	}

	public void setLogo_imgurl(String logo_imgurl) {
		this.logo_imgurl = logo_imgurl;
	}

	public String getShare_content() {
		return share_content;
	}

	public void setShare_content(String share_content) {
		this.share_content = share_content;
	}

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public String getShare_imgurl() {
		return share_imgurl;
	}

	public void setShare_imgurl(String share_imgurl) {
		this.share_imgurl = share_imgurl;
	}
}