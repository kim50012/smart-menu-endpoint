package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;

/**
 *
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:54 2019/1/24
 */
public class AcceptOrder implements Command {
    static final String NAME = "acceptOrder";
    static final String UPORDERSTATUS = "upOrderStatus";

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

    private int status;

    private Long orderId;

    // 操作类型 operation type：1-酒店订单完成
    private int ot;

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

    public int getOt() {
        return ot;
    }

    public void setOt(int ot) {
        this.ot = ot;
    }
}
