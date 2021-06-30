package com.basoft.eorder.domain.model;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Random;

public class TnuPaymentReq implements Serializable {

    private static final long serialVersionUID = -2941754870384472954L;

    private Integer shopId;
    private String subShopId;
    private String interfaceId;
    private String interfacePass;
    private String mch_id;              //결제계정 ID (하나카드) card
    private String sub_mch_id;          //서브 결제계정 ID (고객사) 客户ID
    private String nonce_str;
    private String sign;
    private String openid;              //하나카드 공중계정 Openid (Null 처리)
    private String sub_openid;          //고객사 공증계정 Openid (필수)
    private String cust_trade_no;       //고객사 Order 번호    订单编号
    private String body;                //제품 혹은 결제 건 간단설명   说明
    private String fee_type;            //화폐종류 货币类型
    private Integer total_fee;          //총금액 金额
    private String notify_url;          //결제결과 비동기 통지 접수 URL(매개 변수 없어야 함) 后台相应地址
    private String redirect_url;        //결제 성공 페이지 URL (매개변수 없어야 함) 前台相应地址

    private String key;



    public String generateRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getSubShopId() {
        return subShopId;
    }

    public void setSubShopId(String subShopId) {
        this.subShopId = subShopId;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getInterfacePass() {
        return interfacePass;
    }

    public void setInterfacePass(String interfacePass) {
        this.interfacePass = interfacePass;
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

    public String getNonce_str() {

        if (StringUtils.isBlank(nonce_str))
            return generateRandomString(31);

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

    public String getCust_trade_no() {
        return cust_trade_no;
    }

    public void setCust_trade_no(String cust_trade_no) {
        this.cust_trade_no = cust_trade_no;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public final static class Builder {
        private Integer shopId;
        private String subShopId;
        private String interfaceId;
        private String interfacePass;
        private String mch_id;
        private String sub_mch_id;
        private String nonce_str;
        private String sign;
        private String openid;
        private String sub_openid;
        private String cust_trade_no;
        private String body;
        private String fee_type;
        private Integer total_fee;
        private String notify_url;
        private String redirect_url;
        private String key;


        public Builder shopId(Integer shopId) {
            this.shopId = shopId;
            return this;
        }

        public Builder subShopId(String subShopId) {
            this.subShopId = subShopId;
            return this;
        }

        public Builder interfaceId(String interfaceId) {
            this.interfaceId = interfaceId;
            return this;
        }

        public Builder interfacePass(String interfacePass) {
            this.interfacePass = interfacePass;
            return this;
        }

        public Builder mch_id(String mch_id) {
            this.mch_id = mch_id;
            return this;
        }

        public Builder sub_mch_id(String sub_mch_id) {
            this.sub_mch_id = sub_mch_id;
            return this;
        }

        public Builder nonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
            return this;
        }

        public Builder sign(String sign) {
            this.sign = sign;
            return this;
        }

        public Builder openid(String openid) {
            this.openid = openid;
            return this;
        }

        public Builder sub_openid(String sub_openid) {
            this.sub_openid = sub_openid;
            return this;
        }

        public Builder cust_trade_no(String cust_trade_no) {
            this.cust_trade_no = cust_trade_no;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder fee_type(String fee_type) {
            this.fee_type = fee_type;
            return this;
        }

        public Builder total_fee(Integer total_fee) {
            this.total_fee = total_fee;
            return this;
        }


        public Builder notify_url(String notify_url) {
            this.notify_url = notify_url;
            return this;
        }

        public Builder redirect_url(String redirect_url) {
            this.redirect_url = redirect_url;
            return this;
        }

        public Builder key (String key) {
            this.key = key;
            return this;
        }

        public TnuPaymentReq build() {
            TnuPaymentReq pay = new TnuPaymentReq();
            pay.shopId = this.shopId;
            pay.subShopId = this.subShopId;
            pay.interfaceId = this.interfaceId;
            pay.interfacePass = this.interfacePass;
            pay.mch_id = this.mch_id;
            pay.sub_mch_id = this.sub_mch_id;
            pay.nonce_str = this.nonce_str;
            pay.sign = this.sign;
            pay.openid = this.openid;
            pay.sub_openid = this.sub_openid;
            pay.cust_trade_no = this.cust_trade_no;
            pay.body = this.body;
            pay.fee_type = this.fee_type;
            pay.total_fee = this.total_fee;
            pay.notify_url = this.notify_url;
            pay.redirect_url = this.redirect_url;
            pay.key = this.key;

            return pay;
        }
    }


    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
