package com.basoft.eorder.domain.model;


import com.basoft.eorder.application.BusinessTypeEnum;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.math.NumberUtils;

public class TnuPaymentResp extends TnuResult {

    private String appid;               //공증계정ID
    private String sub_appid;           //서브공증계정ID
    private String mch_id;              //결제계정 ID (하나카드)
    private String sub_mch_id;          //서브 결제계정 ID (고객사)
    private String nonce_str;           //보안 Sign Key
    private String sign;                //보안 문자열
    private String result_code;         //업무결과
    private String err_code;            //오류 코드
    private String err_code_des;        //오류 코드 설명
    private String openid;              //하나카드 공중계정 Openid
    private String is_subscribe;        //openid 팔로우 여부
    private String sub_openid;          //고객사 공증계정 Openid
    private String sub_is_subscribe;    //sub_openid 팔로우 여부
    private String bank_type;           //결제은행
    private int total_fee;              //
    private String fee_type;            //결제금액 화폐 종류 (고객사)
    private int cash_fee;               //결제금액 (고객)
    private String cash_fee_type;       //결제금액  화폐 종류 (고객)
    private String transaction_id;      //위쳇 Order 번호
    private String out_trade_no;        //GWAY Order 번호
    private String cust_trade_no;       //고객사 Order 번호
    private String attach;              //시스템 Order 추가데이터
    private String time_end;            //결제 완료 신간
    private String trade_type;          //거래유형(JSAPI、NATIVE、APP)
    private String rate_value;          //환율 汇率

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSub_appid() {
        return sub_appid;
    }

    public void setSub_appid(String sub_appid) {
        this.sub_appid = sub_appid;
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

    public String getCust_trade_no() {
        return cust_trade_no;
    }

    public void setCust_trade_no(String cust_trade_no) {
        this.cust_trade_no = cust_trade_no;
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

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getRate_value() {
        return rate_value;
    }

    public void setRate_value(String rate_value) {
        this.rate_value = rate_value;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }


}
