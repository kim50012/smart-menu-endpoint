package com.basoft.eorder.domain.model;


import com.basoft.eorder.application.wx.model.WxPayResult;
import org.apache.commons.lang3.math.NumberUtils;

public class OrderPay {

    enum TradeState {
        SUCCESS("SUCCESS", "支付成功"),
        REFUND("REFUND", "转入退款"),
        NOTPAY("NOTPAY","未支付"),
        CLOSED("CLOSED", "已关闭"),
        REVOKED("REVOKED", "已撤销(刷卡支付)"),
        USERPAYING("USERPAYING", "用户支付中"),
        PAYERROR("PAYERROR", "支付失败(其他原因，如银行返回失败)");

        private String code;
        private String desc;

        TradeState(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode()  {
            return this.code;
        }

        public String getDesc() {
            return this.desc;
        }
    }

    private Long payId;		//지불id;支付id
    private Long orderId;		//订单id;오더id
    private String deviceInfo;		//微信支付分配的终端设备号
    private String nonceStr;		//随机字符串
    private String sign;		//签名
    private String signType;		//签名类型:HMAC-SHA256,MD5
    private String resultCode;		//业务结果:SUCCESS/FAIL
    private String errCode;		//错误代码
    private String errCodeDes;		//错误代码描述
    private String openId;		//wechat cust open id
    private String isSubscribe;		//是否关注公众账号，Y-关注，N-未关注
    private String subOpenId;       //用户在子商户appid下的唯一标识
    private String subIsSubscribe;  //用户是否关注子公众账号，Y-关注，N-未关注（机构商户不返回）
    private String tradeType;		//交易类型:JSAPI、NATIVE、APP
    private String tradeState;      //SUCCESS—支付成功,REFUND—转入退款,NOTPAY—未支付,CLOSED—已关闭,REVOKED—已撤销(刷卡支付),USERPAYING--用户支付中,PAYERROR--支付失败(其他原因，如银行返回失败)
    private String bankType;		//结算银行;결재은행
    private String totalFee;		//结算金额（公司）;결재금액（고객사）
    private String feeType;		//结算货币类型（公司）;결재화페종류(고객사)
    private String cashFee;		//结算金额（客户）;결재금액(고객)
    private String cashFeeType;		//结算货币类型;결재화페종류;
    private String transactionId;		//微信支付id;위쳇결재id
    private String outTradeNo;		//tnu 支付编号;tnu 결재번호
    private String attach;		//订单追加数据;order 추가 데이타
    private String payDts;		//结算完成时间;결재완료시간
    private int payStatus;		//结算状态；0：失败，1：成功;결재상태;0:실패, 1:성공
    private String rateValue;		//汇率;환율
    private String payDesc;		//结算说明;결재 메세지
    private String createDts;		//数据创建时间;데이타 생성 시간
    private String updateDts;		//数据更新时间;데이타 수정시간;

    public Long getPayId() {
        return payId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public String getSignType() {
        return signType;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public String getOpenId() {
        return openId;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public String getSubOpenId() {
        return subOpenId;
    }

    public String getSubIsSubscribe() {
        return subIsSubscribe;
    }

    public String getTradeType() {
        return tradeType;
    }

    public String getTradeState() {
        return tradeState;
    }

    public String getBankType() {
        return bankType;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public String getCashFee() {
        return cashFee;
    }

    public String getCashFeeType() {
        return cashFeeType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public String getAttach() {
        return attach;
    }

    public String getPayDts() {
        return payDts;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public String getRateValue() {
        return rateValue;
    }

    public String getPayDesc() {
        return payDesc;
    }

    public String getCreateDts() {
        return createDts;
    }

    public String getUpdateDts() {
        return updateDts;
    }

    public final static class Builder {
        private Long payId;		//지불id;支付id
        private Long orderId;		//订单id;오더id
        private String deviceInfo;		//微信支付分配的终端设备号
        private String nonceStr;		//随机字符串
        private String sign;		//签名
        private String signType;		//签名类型:HMAC-SHA256,MD5
        private String resultCode;		//业务结果:SUCCESS/FAIL
        private String errCode;		//错误代码
        private String errCodeDes;		//错误代码描述
        private String openId;		//wechat cust open id
        private String isSubscribe;		//是否关注公众账号，Y-关注，N-未关注
        private String subOpenId;
        private String subIsSubscribe;
        private String tradeType;		//交易类型:JSAPI、NATIVE、APP
        private String tradeState;
        private String bankType;		//结算银行;결재은행
        private String totalFee;		//结算金额（公司）;결재금액（고객사）
        private String feeType;		//结算货币类型（公司）;결재화페종류(고객사)
        private String cashFee;		//结算金额（客户）;결재금액(고객)
        private String cashFeeType;		//结算货币类型;결재화페종류;
        private String transactionId;		//微信支付id;위쳇결재id
        private String outTradeNo;		//tnu 支付编号;tnu 결재번호
        private String attach;		//订单追加数据;order 추가 데이타
        private String payDts;		//结算完成时间;결재완료시간
        private int payStatus;		//结算状态；0：失败，1：成功;결재상태;0:실패, 1:성공
        private String rateValue;		//汇率;환율
        private String payDesc;		//结算说明;결재 메세지
        private String createDts;		//数据创建时间;데이타 생성 시간
        private String updateDts;		//数据更新时间;데이타 수정시간;

        public Builder payId(Long payId) {
            this.payId = payId;
            return this;
        }

        public Builder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder deviceInfo(String deviceInfo) {
            this.deviceInfo = deviceInfo;
            return this;
        }

        public Builder nonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
            return this;
        }

        public Builder sign(String sign) {
            this.sign = sign;
            return this;
        }

        public Builder signType(String signType) {
            this.signType = signType;
            return this;
        }

        public Builder resultCode(String resultCode) {
            this.resultCode = resultCode;
            return this;
        }

        public Builder errCode(String errCode) {
            this.errCode = errCode;
            return this;
        }

        public Builder errCodeDes(String errCodeDes) {
            this.errCodeDes = errCodeDes;
            return this;
        }

        public Builder openId(String openId) {
            this.openId = openId;
            return this;
        }

        public Builder isSubscribe(String isSubscribe) {
            this.isSubscribe = isSubscribe;
            return this;
        }

        public Builder subOpenId(String subOpenId) {
            this.subOpenId = subOpenId;
            return this;
        }

        public Builder subIsSubscribe(String subIsSubscribe) {
            this.subIsSubscribe = subIsSubscribe;
            return this;
        }

        public Builder tradeType(String tradeType) {
            this.tradeType = tradeType;
            return this;
        }

        public Builder tradeState(String tradeState) {
            this.tradeState = tradeState;
            return this;
        }

        public Builder bankType(String bankType) {
            this.bankType = bankType;
            return this;
        }

        public Builder totalFee(String totalFee) {
            this.totalFee = totalFee;
            return this;
        }

        public Builder feeType(String feeType) {
            this.feeType = feeType;
            return this;
        }

        public Builder cashFee(String cashFee) {
            this.cashFee = cashFee;
            return this;
        }

        public Builder cashFeeType(String cashFeeType) {
            this.cashFeeType = cashFeeType;
            return this;
        }

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder outTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
            return this;
        }

        public Builder attach(String attach) {
            this.attach = attach;
            return this;
        }

        public Builder payDts(String payDts) {
            this.payDts = payDts;
            return this;
        }

        public Builder payStatus(int payStatus) {
            this.payStatus = payStatus;
            return this;
        }

        public Builder rateValue(String rateValue) {
            this.rateValue = rateValue;
            return this;
        }

        public Builder payDesc(String payDesc) {
            this.payDesc = payDesc;
            return this;
        }

        public Builder createDts(String createDts) {
            this.createDts = createDts;
            return this;
        }

        public Builder updateDts(String updateDts) {
            this.updateDts = updateDts;
            return this;
        }

        public OrderPay build() {
            OrderPay pay = new OrderPay();

            pay.payId = this.payId;
            pay.orderId = this.orderId;
            pay.deviceInfo = this.deviceInfo;
            pay.nonceStr = this.nonceStr;
            pay.sign = this.sign;
            pay.signType = this.signType;
            pay.resultCode = this.resultCode;
            pay.errCode = this.errCode;
            pay.errCodeDes = this.errCodeDes;
            pay.openId = this.openId;
            pay.isSubscribe = this.isSubscribe;
            pay.subOpenId = this.subOpenId;
            pay.subIsSubscribe = this.subIsSubscribe;
            pay.tradeType = this.tradeType;
            pay.tradeState = this.tradeState;
            pay.bankType = this.bankType;
            pay.totalFee = this.totalFee;
            pay.feeType = this.feeType;
            pay.cashFee = this.cashFee;
            pay.cashFeeType = this.cashFeeType;
            pay.transactionId = this.transactionId;
            pay.outTradeNo = this.outTradeNo;
            pay.attach = this.attach;
            pay.payDts = this.payDts;
            pay.payStatus = this.payStatus;
            pay.rateValue = this.rateValue;
            pay.payDesc = this.payDesc;
            pay.createDts = this.createDts;
            pay.updateDts = this.updateDts;

            return pay;
        }

    }

    public Builder createnewOrderPayByResult(long payId, WxPayResult resp) {

        return new Builder()
                .payId(payId)
                .orderId(payId)
                .deviceInfo(resp.getDevice_info())
                .nonceStr(resp.getNonce_str())
                .sign(resp.getSign())
                .signType(resp.getSign_type())
                .resultCode(resp.getResult_code())
                .errCode(resp.getErr_code())
                .errCodeDes(resp.getErr_code_des())
                .openId(resp.getOpenid())
                .isSubscribe(resp.getIs_subscribe())
                .subOpenId(resp.getSub_openid())
                .subIsSubscribe(resp.getSub_is_subscribe())
                .tradeType(resp.getTrade_type())
                .tradeState(TradeState.SUCCESS.getCode())
                .bankType(resp.getBank_type())
                .totalFee(resp.getTotal_fee())
                .feeType(resp.getFee_type())
                .cashFee(resp.getCash_fee())
                .cashFeeType(resp.getCash_fee_type())
                .transactionId(resp.getTransaction_id())
                .outTradeNo(resp.getOut_trade_no())
                .attach(resp.getAttach())
                .payDts(resp.getTime_end())
                .payStatus(1)
                .rateValue(resp.getRate_value())
                .payDesc(resp.getReturn_msg());
    }

}


