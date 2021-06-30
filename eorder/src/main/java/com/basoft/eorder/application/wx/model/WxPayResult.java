package com.basoft.eorder.application.wx.model;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class WxPayResult extends BaseResult {

    private String appid;               //公众账号
    private String mch_id;              //商户号
    private String sub_appid;           //子商户公众账号(N)
    private String sub_mch_id;          //子商户号
    private String device_info;         //设备号(N)
    private String nonce_str;           //随机字符串
    private String sign;                //签名
    private String sign_type;           //签名类型
    private String result_code;         //业务结果  SUCCESS/FAIL
    private String err_code;            //错误代码(N)
    private String err_code_des;        //错误代码描述(N)
    private String openid;              //用户标识 用户在商户appid下的唯一标识
    private String is_subscribe;        //是否关注公众账号 Y-关注，N-未关注（机构商户不返回）
    private String sub_openid;          //用户在子商户appid下的唯一标识
    private String sub_is_subscribe;    //用户是否关注子公众账号，Y-关注，N-未关注（机构商户不返回）
    private String trade_type;          //交易类型(JSAPI，NATIVE，APP)
    private String bank_type;           //付款银行
    private String total_fee;              //标价金额
    private String fee_type;            //标价币种(N)
    private String cash_fee;               //现金支付金额
    private String cash_fee_type;       //现金支付货币类型(N)
    private String transaction_id;      //微信支付订单号
    private String out_trade_no;        //商户订单号
    private String attach;              //商家数据包(N)
    private String time_end;            //支付完成时间
    private String rate_value;     		//汇率
    private String trade_state;         //SUCCESS—支付成功, REFUND—转入退款, NOTPAY—未支付, CLOSED—已关闭, REVOKED—已撤销(刷卡支付), USERPAYING--用户支付中, PAYERROR--支付失败(其他原因，如银行返回失败), REFUND—转入退款
    private String return_code;
    private String return_msg;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getSub_appid() {
        return sub_appid;
    }

    public void setSub_appid(String sub_appid) {
        this.sub_appid = sub_appid;
    }

    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(String is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public String getSub_openid() {
        return sub_openid;
    }

    public void setSub_openid(String sub_openid) {
        this.sub_openid = sub_openid;
    }

    public String getSub_is_subscribe() {
        return sub_is_subscribe;
    }

    public void setSub_is_subscribe(String sub_is_subscribe) {
        this.sub_is_subscribe = sub_is_subscribe;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getTrade_state() {
        return trade_state;
    }

    public void setTrade_state(String trade_state) {
        this.trade_state = trade_state;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(String cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getCash_fee_type() {
        return cash_fee_type;
    }

    public void setCash_fee_type(String cash_fee_type) {
        this.cash_fee_type = cash_fee_type;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getRate_value() {
        return rate_value;
    }

    public void setRate_value(String rate_value) {
        this.rate_value = rate_value;
    }

    public String getReturn_code() {
		return return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	@Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
