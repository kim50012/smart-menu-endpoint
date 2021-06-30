package com.basoft.eorder.application.wx.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 查询订单
 */
public class WxPayQueryResp extends WxResp {

    private String device_info;         //设备号(N)
    private String openid;              //用户标识 用户在商户appid下的唯一标识
    private String is_subscribe;        //是否关注公众账号 Y-关注，N-未关注（机构商户不返回）
    private String sub_openid;          //用户子标识(N)         用户在子商户appid下的唯一标识
    private String sub_is_subscribe;    //是否关注子公众账号(N)           Y-关注，N-未关注（机构商户不返回）
    private String trade_type;          //交易类型(JSAPI，NATIVE，APP)
    private String trade_state;         //SUCCESS—支付成功, REFUND—转入退款, NOTPAY—未支付, CLOSED—已关闭, REVOKED—已撤销(刷卡支付), USERPAYING--用户支付中, PAYERROR--支付失败(其他原因，如银行返回失败), REFUND—转入退款
    private String bank_type;           //付款银行
    private int total_fee;              //标价金额
    private String fee_type;            //标价币种(N)
    private int cash_fee;               //现金支付金额
    private String cash_fee_type;       //现金支付货币类型(N)
    private String transaction_id;      //微信支付订单号
    private String out_trade_no;        //商户订单号
    private String attach;              //商家数据包(N)
    private String time_end;            //支付完成时间
    private String trade_state_desc;    //交易状态描述
    private String rate;                //汇率

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
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

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public int getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(int cash_fee) {
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

    public String getTrade_state_desc() {
        return trade_state_desc;
    }

    public void setTrade_state_desc(String trade_state_desc) {
        this.trade_state_desc = trade_state_desc;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
