package com.basoft.eorder.application.wx.model;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class PayReq {
	private String appid;               //公众账号
    private String mch_id;              //商户号
    private String sub_appid;           //子商户公众账号(N)
    private String sub_mch_id;          //子商户号
    private String device_info;         //设备号(N)
    private String nonce_str;           //随机字符串
    private String sign;                //签名
    private String sign_type;           //签名类型(N)
    private String body;                //商品描述
    private String detail;              //商品详情(N)       {"goods_detail":[{"goods_name":"iPhone6s 16G", "quantity":1,}, {"goods_name":"iPhone6s 32G", "quantity":1,}]}
    private String attach;              //附加数据-说明(N)
    private String out_trade_no;        //商户订单号
    private String fee_type;            //标价币种(USD, CNY, KRW)
    private String total_fee;              //标价金额
    private String spbill_create_ip;    //终端IP
    private String time_start;          //交易起始时间(N)
    private String time_expire;         //交易结束时间(N)
    private String goods_tag;			//Item Label(N)
    private String notify_url;          //接收微信支付异步通知回调地址
    private String trade_type;          //交易类型(JSAPI，NATIVE，APP)
    private String product_id;          //商品ID(N) trade_type=NATIVE时，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
    private String openid;              //用户标识(N) trade_type=JSAPI，此参数必传，用户在主商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
    private String sub_openid;          //用户子标识(N) trade_type=JSAPI，此参数必传，用户在子商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
    private String receipt;             //开发票入口开放标识
    private String version;             //版本号 1.0(N)

//    public String getAppid() {
//        return appid;
//    }
//
//    public String getMch_id() {
//        return mch_id;
//    }
//
//    public String getSub_appid() {
//        return sub_appid;
//    }
//
//    public String getSub_mch_id() {
//        return sub_mch_id;
//    }
//
//    public String getDevice_info() {
//        return device_info;
//    }
//
//    public String getNonce_str() {
//        return nonce_str;
//    }
//
//    public String getSign() {
//        return sign;
//    }
//
//    public String getSign_type() {
//        return sign_type;
//    }
//
//    public String getBody() {
//        return body;
//    }
//
//    public String getDetail() {
//        return detail;
//    }
//
//    public String getAttach() {
//        return attach;
//    }
//
//    public String getOut_trade_no() {
//        return out_trade_no;
//    }
//
//    public String getFee_type() {
//        return fee_type;
//    }
//
//    public String getTotal_fee() {
//        return total_fee;
//    }
//
//    public String getSpbill_create_ip() {
//        return spbill_create_ip;
//    }
//
//    public String getTime_start() {
//        return time_start;
//    }
//
//    public String getTime_expire() {
//        return time_expire;
//    }
//
//    public String getGoods_tag() {
//        return goods_tag;
//    }
//    
//    public String getNotify_url() {
//        return notify_url;
//    }
//
//    public String getTrade_type() {
//        return trade_type;
//    }
//
//    public String getProduct_id() {
//        return product_id;
//    }
//
//    public String getOpenid() {
//        return openid;
//    }
//
//    public String getSub_openid() {
//        return sub_openid;
//    }
//
//    public String getReceipt() {
//        return receipt;
//    }
//
//    public String getVersion() {
//        return version;
//    }
//
//    public final static class Builder{
//        private String appid;               //公众账号
//        private String mch_id;              //商户号
//        private String sub_appid;           //子商户公众账号(N)
//        private String sub_mch_id;          //子商户号
//        private String device_info;         //设备号(N)
//        private String nonce_str;           //随机字符串
//        private String sign;                //签名
//        private String sign_type;           //签名类型(N)
//        private String body;                //商品描述
//        private String detail;              //商品详情(N)       {"goods_detail":[{"goods_name":"iPhone6s 16G", "quantity":1,}, {"goods_name":"iPhone6s 32G", "quantity":1,}]}
//        private String attach;              //附加数据-说明(N)
//        private String out_trade_no;        //商户订单号
//        private String fee_type;            //标价币种(USD, CNY, KRW)
//        private String total_fee;              //标价金额
//        private String spbill_create_ip;    //终端IP
//        private String time_start;          //交易起始时间(N)
//        private String time_expire;         //交易结束时间(N)
//        private String goods_tag;			//Item Label(N)
//        private String notify_url;          //接收微信支付异步通知回调地址
//        private String trade_type;          //交易类型(JSAPI，NATIVE，APP)
//        private String product_id;          //商品ID(N) trade_type=NATIVE时，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
//        private String openid;              //用户标识(N) trade_type=JSAPI，此参数必传，用户在主商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
//        private String sub_openid;          //用户子标识(N) trade_type=JSAPI，此参数必传，用户在子商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
//        private String receipt;             //开发票入口开放标识(N)
//        private String version;             //版本号 1.0(N)
//
//
//        public Builder appid(String appid) {
//            this.appid = appid;
//            return this;
//        }
//
//        public Builder mch_id(String mch_id) {
//            this.mch_id = mch_id;
//            return this;
//        }
//
//        public Builder sub_appid(String sub_appid) {
//            this.sub_appid = sub_appid;
//            return this;
//        }
//
//        public Builder sub_mch_id(String sub_mch_id) {
//            this.sub_mch_id = sub_mch_id;
//            return this;
//        }
//
//        public Builder device_info(String device_info) {
//            this.device_info = device_info;
//            return this;
//        }
//
//        public Builder nonce_str(String nonce_str) {
//            this.nonce_str = nonce_str;
//            return this;
//        }
//
//        public Builder sign(String sign) {
//            this.sign = sign;
//            return this;
//        }
//
//        public Builder sign_type(String sign_type) {
//            this.sign_type = sign_type;
//            return this;
//        }
//
//        public Builder body(String body) {
//            this.body = body;
//            return this;
//        }
//
//        public Builder detail(String detail) {
//            this.detail = detail;
//            return this;
//        }
//
//        public Builder attach(String attach) {
//            this.attach = attach;
//            return this;
//        }
//
//        public Builder out_trade_no(String out_trade_no) {
//            this.out_trade_no = out_trade_no;
//            return this;
//        }
//
//        public Builder fee_type(String fee_type) {
//            this.fee_type = fee_type;
//            return this;
//        }
//
//        public Builder total_fee(String total_fee) {
//            this.total_fee = total_fee;
//            return this;
//        }
//
//        public Builder spbill_create_ip(String spbill_create_ip) {
//            this.spbill_create_ip = spbill_create_ip;
//            return this;
//        }
//
//        public Builder time_start(String time_start) {
//            this.time_start = time_start;
//            return this;
//        }
//
//        public Builder time_expire(String time_expire) {
//            this.time_expire = time_expire;
//            return this;
//        }
//
//        public Builder goods_tag(String goods_tag) {
//            this.time_expire = goods_tag;
//            return this;
//        }
//
//        public Builder notify_url(String notify_url) {
//            this.notify_url = notify_url;
//            return this;
//        }
//
//        public Builder trade_type(String trade_type) {
//            this.trade_type = trade_type;
//            return this;
//        }
//
//        public Builder product_id(String product_id) {
//            this.product_id = product_id;
//            return this;
//        }
//
//        public Builder openid(String openid) {
//            this.openid = openid;
//            return this;
//        }
//
//        public Builder sub_openid(String sub_openid) {
//            this.sub_openid = sub_openid;
//            return this;
//        }
//
//        public Builder receipt(String receipt) {
//            this.receipt = receipt;
//            return this;
//        }
//
//        public Builder version(String version) {
//            this.version = version;
//            return this;
//        }
//
//        public PayReq build() {
//            PayReq req = new PayReq();
//
//            req.appid = this.appid;               //公众账号
//            req.mch_id = this.mch_id;              //商户号
//            req.sub_appid = this.sub_appid;           //子商户公众账号(N)
//            req.sub_mch_id = this.sub_mch_id;          //子商户号
//            req.device_info = this.device_info;         //设备号(N)
//            req.nonce_str = this.nonce_str;           //随机字符串
//            req.sign = this.sign;                //签名
//            req.sign_type = this.sign_type;           //签名类型(N)
//            req.body = this.body;                //商品描述
//            req.detail = this.detail;              //商品详情(N)       {"goods_detail":[{"goods_name":"iPhone6s 16G", "quantity":1,}, {"goods_name":"iPhone6s 32G", "quantity":1,}]}
//            req.attach = this.attach;              //附加数据-说明(N)
//            req.out_trade_no = this.out_trade_no;        //商户订单号
//            req.fee_type = this.fee_type;            //标价币种(USD, CNY, KRW)
//            req.total_fee = this.total_fee;              //标价金额
//            req.spbill_create_ip = this.spbill_create_ip;    //终端IP
//            req.time_start = this.time_start;          //交易起始时间(N)
//            req.time_expire = this.time_expire;         //交易结束时间(N)
//            req.goods_tag = this.goods_tag;         //Item Label(N)
//            req.notify_url = this.notify_url;          //接收微信支付异步通知回调地址
//            req.trade_type = this.trade_type;          //交易类型(JSAPI，NATIVE，APP)
//            req.product_id = this.product_id;          //商品ID(N) trade_type=NATIVE时，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
//            req.openid = this.openid;              //用户标识(N) trade_type=JSAPI，此参数必传，用户在主商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
//            req.sub_openid = this.sub_openid;          //用户子标识(N) trade_type=JSAPI，此参数必传，用户在子商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
//            req.receipt = this.receipt;             //开发票入口开放标识(N)
//            req.version = this.version;             //版本号 1.0(N)
//
//            return req;
//        }
//    }

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

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
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

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_expire() {
		return time_expire;
	}

	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}

	public String getGoods_tag() {
		return goods_tag;
	}

	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSub_openid() {
		return sub_openid;
	}

	public void setSub_openid(String sub_openid) {
		this.sub_openid = sub_openid;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
