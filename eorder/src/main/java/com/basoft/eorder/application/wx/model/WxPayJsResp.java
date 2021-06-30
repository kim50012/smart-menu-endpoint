package com.basoft.eorder.application.wx.model;

public class WxPayJsResp extends BaseResult {
    private String appid;
    private String nonceStr;
    private String packageStr;
    private String signType;
    private String paySign;

    private String timeStamp;

    private String orderId;

    public String getAppid() {
        return appid;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public String getPackageStr() {
        return packageStr;
    }

    public String getSignType() {
        return signType;
    }

    public String getPaySign() {
        return paySign;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public final static class Builder {
        private String appid;
        private String nonceStr;
        private String packageStr;
        private String signType;
        private String paySign;

        private String timeStamp;

        private String orderId;

        public Builder appid(String appid) {
            this.appid = appid;
            return this;
        }

        public Builder nonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
            return this;
        }

        public Builder packageStr(String packageStr) {
            this.packageStr = packageStr;
            return this;
        }

        public Builder signType(String signType) {
            this.signType = signType;
            return this;
        }

        public Builder paySign(String paySign) {
            this.paySign = paySign;
            return this;
        }

        public Builder timeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public WxPayJsResp build() {
            WxPayJsResp resp = new WxPayJsResp();
            resp.appid = this.appid;
            resp.nonceStr = this.nonceStr;
            resp.packageStr = this.packageStr;
            resp.signType = this.signType;
            resp.paySign = this.paySign;
            resp.timeStamp = this.timeStamp;
            resp.orderId = this.orderId;
            return resp;
        }
    }

    public Builder createWxPayJsResp(WxPayResp resp, String signType, String orderId, String paySign, String timeStamp) {
        return new Builder()
                .appid(resp.getAppid())
                .nonceStr(resp.getNonce_str())
                .packageStr("prepay_id=" + resp.getPrepay_id())
                .signType(signType)
                .timeStamp(timeStamp)
                .orderId(orderId)
                .paySign(paySign);
    }

    public Builder createWxPayRefundJsResp(WxPayRefundResult resp, String orderId) {
        return new Builder()
                .orderId(orderId);
    }
}
