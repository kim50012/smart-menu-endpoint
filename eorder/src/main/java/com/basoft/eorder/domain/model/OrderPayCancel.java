package com.basoft.eorder.domain.model;


import com.basoft.eorder.application.wx.model.WxPayRefundResult;
import com.basoft.eorder.application.wx.model.WxPayResult;
import org.apache.commons.lang3.math.NumberUtils;

public class OrderPayCancel {

    private Long cancelId; //취소 ID
    private Long orderId;	//
    private String returnCode; //Return Code
    private String returnMsg; //오류 메시지
    private String appId; //Off. Account ID
    private String mchId; //Vendor ID
    private String subMchId; //Suv Vendor ID
    private String nonceStr; //Random String
    private String sign; //sign
    private String resultCode; //Result Code
    private String transactionId; //위챗 거래 ID
    private String outTradeNo; //Order ID
    private String outRefundNo; //Cancel ID
    private String refundId;
    private String refundChannel;
    private String refundFee;
    private String couponRefund_fee;
    private String totalFee;
    private String cashFee;
    private String feeType;
    private String couponRefundCount;
    private String cashRefundFee;
    private String refundFeeType;
    private String cashFeeType;
    private String cashRefundFeeType;



    public Long getCancelId() {
		return cancelId;
	}

    public Long getOrderId() {
		return orderId;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public String getAppId() {
		return appId;
	}

	public String getMchId() {
		return mchId;
	}

	public String getSubMchId() {
		return subMchId;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public String getResultCode() {
		return resultCode;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public String getRefundId() {
		return refundId;
	}

	public String getRefundChannel() {
		return refundChannel;
	}

	public String getRefundFee() {
		return refundFee;
	}

	public String getCouponRefund_fee() {
		return couponRefund_fee;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public String getCashFee() {
		return cashFee;
	}

	public String getFeeType() {
		return feeType;
	}

	public String getCouponRefundCount() {
		return couponRefundCount;
	}

	public String getCashRefundFee() {
		return cashRefundFee;
	}

	public String getRefundFeeType() {
		return refundFeeType;
	}

	public String getCashFeeType() {
		return cashFeeType;
	}

	public String getCashRefundFeeType() {
		return cashRefundFeeType;
	}

	public final static class Builder {
	    private Long cancelId; //취소 ID
	    private Long orderId;
	    private String returnCode; //Return Code
	    private String returnMsg; //오류 메시지
	    private String appId; //Off. Account ID
	    private String mchId; //Vendor ID
	    private String subMchId; //Suv Vendor ID
	    private String nonceStr; //Random String
	    private String sign; //sign
	    private String resultCode; //Result Code
	    private String transactionId; //위챗 거래 ID
	    private String outTradeNo; //Order ID
	    private String outRefundNo; //Cancel ID
	    private String refundId;
	    private String refundChannel;
	    private String refundFee;
	    private String couponRefund_fee;
	    private String totalFee;
	    private String cashFee;
	    private String feeType;
	    private String couponRefundCount;
	    private String cashRefundFee;
	    private String refundFeeType;
	    private String cashFeeType;
	    private String cashRefundFeeType;

	    public Builder cancelId(Long cancelId) {this.cancelId = cancelId; return this;}
	    public Builder orderId(Long orderId) {this.orderId = orderId; return this;}
	    public Builder returnCode(String returnCode) {this.returnCode = returnCode; return this;}
	    public Builder returnMsg(String returnMsg) {this.returnMsg = returnMsg; return this;}
	    public Builder appId(String appId) {this.appId = appId; return this;}
	    public Builder mchId(String mchId) {this.mchId = mchId; return this;}
	    public Builder subMchId(String subMchId) {this.subMchId = subMchId; return this;}
	    public Builder nonceStr(String nonceStr) {this.nonceStr = nonceStr; return this;}
	    public Builder sign(String sign) {this.sign = sign; return this;}
	    public Builder resultCode(String resultCode) {this.resultCode = resultCode; return this;}
	    public Builder transactionId(String transactionId) {this.transactionId = transactionId; return this;}
	    public Builder outTradeNo(String outTradeNo) {this.outTradeNo = outTradeNo; return this;}
	    public Builder outRefundNo(String outRefundNo) {this.outRefundNo = outRefundNo; return this;}
	    public Builder refundId(String refundId) {this.refundId = refundId; return this;}
	    public Builder refundChannel(String refundChannel) {this.refundChannel = refundChannel; return this;}
	    public Builder refundFee(String refundFee) {this.refundFee = refundFee; return this;}
	    public Builder couponRefund_fee(String couponRefund_fee) {this.couponRefund_fee = couponRefund_fee; return this;}
	    public Builder totalFee(String totalFee) {this.totalFee = totalFee; return this;}
	    public Builder cashFee(String cashFee) {this.cashFee = cashFee; return this;}
	    public Builder feeType(String feeType) {this.feeType = feeType; return this;}
	    public Builder couponRefundCount(String couponRefundCount) {this.couponRefundCount = couponRefundCount; return this;}
	    public Builder cashRefundFee(String cashRefundFee) {this.cashRefundFee = cashRefundFee; return this;}
	    public Builder refundFeeType(String refundFeeType) {this.refundFeeType = refundFeeType; return this;}
	    public Builder cashFeeType(String cashFeeType) {this.cashFeeType = cashFeeType; return this;}
	    public Builder cashRefundFeeType(String cashRefundFeeType) {this.cashRefundFeeType = cashRefundFeeType; return this;}
        

        public OrderPayCancel build() {
            OrderPayCancel payCencel = new OrderPayCancel();
            
            payCencel.cancelId = this.cancelId;
            payCencel.orderId = this.orderId;
            payCencel.returnCode = this.returnCode;
            payCencel.returnMsg = this.returnMsg;
            payCencel.appId = this.appId;
            payCencel.mchId = this.mchId;
            payCencel.subMchId = this.subMchId;
            payCencel.nonceStr = this.nonceStr;
            payCencel.sign = this.sign;
            payCencel.resultCode = this.resultCode;
            payCencel.transactionId = this.transactionId;
            payCencel.outTradeNo = this.outTradeNo;
            payCencel.outRefundNo = this.outRefundNo;
            payCencel.refundId = this.refundId;
            payCencel.refundChannel = this.refundChannel;
            payCencel.refundFee = this.refundFee;
            payCencel.couponRefund_fee = this.couponRefund_fee;
            payCencel.totalFee = this.totalFee;
            payCencel.cashFee = this.cashFee;
            payCencel.feeType = this.feeType;
            payCencel.couponRefundCount = this.couponRefundCount;
            payCencel.cashRefundFee = this.cashRefundFee;
            payCencel.refundFeeType = this.refundFeeType;
            payCencel.cashFeeType = this.cashFeeType;
            payCencel.cashRefundFeeType = this.cashRefundFeeType;

            return payCencel;
        }

    }

    public Builder createnewOrderPayCancelByResult(long cancelId, WxPayRefundResult resp, Long orderId) {

        return new Builder()
                .cancelId(cancelId)
                .orderId(orderId)
        		.returnCode(resp.getReturn_code())
        		.returnMsg(resp.getReturn_msg())
        		.appId(resp.getAppid())
        		.mchId(resp.getMch_id())
        		.subMchId(resp.getSub_mch_id())
        		.nonceStr(resp.getNonce_str())
        		.sign(resp.getSign())
        		.resultCode(resp.getResult_code())
        		.transactionId(resp.getTransaction_id())
        		.outTradeNo(resp.getOut_trade_no())
        		.outRefundNo(resp.getOut_refund_no())
        		.refundId(resp.getRefund_id())
        		.refundChannel(resp.getRefund_channel())
        		.refundFee(resp.getRefund_fee())
        		.totalFee(resp.getTotal_fee())
        		.cashFee(resp.getCash_fee())
        		.feeType(resp.getFee_type())
        		.cashRefundFee(resp.getCash_refund_fee())
        		.refundFeeType(resp.getRefund_fee_type())
        		.cashFeeType(resp.getCash_fee_type())
        		.cashRefundFeeType(resp.getCash_refund_fee_type());

    }

}


