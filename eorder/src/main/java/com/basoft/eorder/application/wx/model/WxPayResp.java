package com.basoft.eorder.application.wx.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 统一下单 返回值
 */
public class WxPayResp extends WxResp {

    enum PayRespErrorCode {
        INVALID_REQUEST("INVALID_REQUEST", "参数错误", "参数格式有误或者未按规则上传"),
        NOAUTH("NOAUTH", "商户无此接口权限", "商户未开通此接口权限"),
        NOTENOUGH("NOTENOUGH", "余额不足", "用户帐号余额不足"),
        ORDERPAID("ORDERPAID", "商户订单已支付", "商户订单已支付，无需重复操作"),
        ORDERCLOSED("ORDERCLOSED", "订单已关闭", "当前订单已关闭，无法支付"),
        SYSTEMERROR("SYSTEMERROR", "系统错误", "系统超时"),
        APPID_NOT_EXIST("NOTENOUGH", "APPID不存在", "参数中缺少APPID"),
        MCHID_NOT_EXIST("NOTENOUGH", "余额不足", "参数中缺少MCHID"),
        APPID_MCHID_NOT_MATCH("APPID_MCHID_NOT_MATCH", "appid和mch_id不匹配", "appid和mch_id不匹配"),
        LACK_PARAMS("LACK_PARAMS", "缺少参数", "缺少必要的请求参数"),
        OUT_TRADE_NO_USED("OUT_TRADE_NO_USED", "商户订单号重复", "同一笔交易不能多次提交"),
        SIGNERROR("SIGNERROR", "签名错误", "参数签名结果不正确"),
        XML_FORMAT_ERROR("XML_FORMAT_ERROR", "XML格式错误", "XML格式错误"),
        REQUIRE_POST_METHOD("REQUIRE_POST_METHOD", "请使用post方法", "未使用post传递参数"),
        POST_DATA_EMPTY("POST_DATA_EMPTY", "post数据为空", "post数据为空"),
        NOT_UTF8("NOT_UTF8", "编码格式错误", "未使用指定编码格式");

        private String code;
        private String msg;
        private String reason;

        PayRespErrorCode(String code, String msg, String reason) {
            this.code = code;
            this.msg = msg;
            this.reason = reason;
        }

        public String getCode() {
            return this.code;
        }

        public String getMsg() {
            return this.msg;
        }

        public String getReason() {
            return this.reason;
        }

        public static PayRespErrorCode getPayRespErrorCode(String code) {
            for (PayRespErrorCode type : PayRespErrorCode.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
            return null;
        }

    }

    private String return_code;
    private String return_msg;
    private String appid;
    private String mch_id;
    private String sub_appid;
    private String sub_mch_id;
    private String device_info;         //设备号(N)
    private String nonce_str;
    private String sign;
    private String result_code;
    private String err_code;
    private String err_code_des;
    private String trade_type;          //交易类型(JSAPI，NATIVE，APP)
    private String prepay_id;           //预支付交易会话标识
    private String code_url;            //二维码链接(N)
    

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }
    
    public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

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

	@Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
