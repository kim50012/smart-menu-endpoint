package com.basoft.eorder.application.wx.model;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class WxResp extends BaseResult {

    private String appid;               //公众账号
    private String mch_id;              //商户号
    private String sub_appid;           //子商户公众账号(N)
    private String sub_mch_id;          //子商户号
    private String nonce_str;           //随机字符串
    private String sign;                //签名
    private String result_code;          //业务结果  SUCCESS/FAIL
    private String err_code;             //错误代码(N)
    private String err_code_des;          //错误代码描述(N)


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
