package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:54 2019/1/24
 **/
public class ConfirmOrder implements Command {
    static final String NAME_HOTEL = "confirmOrderHotel";
    static final String NAME_CLINIC = "confirmOrderClinic";
    static final String NAME_SHOP = "confirmOrderShop";
    static final String NAME_CONFIRM_RESERVE = "confirmOrderReserve";
    static final String NAME_CANCEL_RESERVE = "cancelOrderReserve";

    private int status;
    private Long orderId;
    private String reseveDtfrom; //预约开始日期 예약 시작일
    private String reseveDtto; //预约开始日期 예약 시작일
    private int reseveTime; //预约上午下午 예약 오전/오후
    private String reseveConfirmtime;
    private int cancelReason;
    private String shippingNo;
    private String shippingCmt;

    public enum Status {
        NORMAL(0), SEND(1), SUCCES(2), FAIL(3), PAYMENY(4), ACEEPT(5), CANCEL_REQ(6), CANCELED(7), CANCEL_FAIL(8), COMPLETE(9), SHIPPING_REDY(10), SHIPPING_COMP(11);
        private final int code;

        private Status(int code) {
            this.code = code;
        }

        public int getStatusCode() {
            return code;
        }
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReseveDtfrom() {
        return reseveDtfrom;
    }

    public void setReseveDtfrom(String reseveDtfrom) {
        this.reseveDtfrom = reseveDtfrom;
    }

    public int getReseveTime() {
        return reseveTime;
    }

    public void setReseveTime(int reseveTime) {
        this.reseveTime = reseveTime;
    }

    public String getReseveDtto() {
        return reseveDtto;
    }

    public void setReseveDtto(String reseveDtto) {
        this.reseveDtto = reseveDtto;
    }

    public String getReseveConfirmtime() {
        return reseveConfirmtime;
    }

    public void setReseveConfirmtime(String reseveConfirmtime) {
        this.reseveConfirmtime = reseveConfirmtime;
    }

    public int getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(int cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }

    public String getShippingCmt() {
        return shippingCmt;
    }

    public void setShippingCmt(String shippingCmt) {
        this.shippingCmt = shippingCmt;
    }
}
