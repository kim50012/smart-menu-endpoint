package com.basoft.eorder.application.wx.model;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class WxPayRefundResult extends BaseResult {

	private String return_code;			//Return Code
	private String return_msg;			//Return Msg
    private String result_code;         //业务结果  SUCCESS/FAIL
    private String err_code;            //错误代码(N)
    private String err_code_des;        //错误代码描述(N)
    private String appid;               //公众账号
    private String mch_id;              //商户号
    private String sub_appid;           //子商户公众账号(N)
    private String sub_mch_id;          //子商户号
    private String device_info;         //设备号(N)
    private String nonce_str;           //随机字符串
    private String sign;                //签名
    private String transaction_id;        //商户订单号
    private String out_trade_no;        //商户订单号
    private String out_refund_no;        //商户Refund订单号
    private String out_refund_id;        //商户Refund订单号
    private String refund_fee;
    private String refund_fee_type;
    private String total_fee;			//标价金额
    private String fee_type;			//标价币种(N)
    private String cash_fee;			//现金支付金额
    private String cash_fee_type;		//现金支付货币类型(N)
    private String cash_refund_fee;
    private String cash_refund_fee_type;
    private String refund_channel;
    private String refund_id;        //商户Refund订单号

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

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }
    

    public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public String getOut_refund_id() {
		return out_refund_id;
	}

	public void setOut_refund_id(String out_refund_id) {
		this.out_refund_id = out_refund_id;
	}

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}
	

	public String getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(String refund_fee) {
		this.refund_fee = refund_fee;
	}

	public String getRefund_fee_type() {
		return refund_fee_type;
	}

	public void setRefund_fee_type(String refund_fee_type) {
		this.refund_fee_type = refund_fee_type;
	}

	public String getCash_refund_fee() {
		return cash_refund_fee;
	}

	public void setCash_refund_fee(String cash_refund_fee) {
		this.cash_refund_fee = cash_refund_fee;
	}

	public String getCash_refund_fee_type() {
		return cash_refund_fee_type;
	}

	public void setCash_refund_fee_type(String cash_refund_fee_type) {
		this.cash_refund_fee_type = cash_refund_fee_type;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
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
	

	public String getRefund_channel() {
		return refund_channel;
	}

	public void setRefund_channel(String refund_channel) {
		this.refund_channel = refund_channel;
	}

	@Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
