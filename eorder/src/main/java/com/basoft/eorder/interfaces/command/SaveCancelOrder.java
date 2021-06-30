package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.OrderItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SaveCancelOrder implements Command {


    public static final String NAME = "saveCancelOrder";

    private Long orderId;//订单编号
    private String refundId;//refund订单编号
    private Order.Status status;//订单状态
    private int cancelReason;

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public Order.Status getStatus() {
        return status;
    }
    
    public void setStatus(Order.Status status) {
        this.status = status;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }    
    
    public String getRefundId() {
        return refundId;
    }

	public int getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(int cancelReason) {
		this.cancelReason = cancelReason;
	}
    
    public Order build(
            Long orderId,
            Order.Status status,
            String refundId
            ) {
        return new Order.Builder()
                .id(orderId)
                .status(status)
                .refundId(refundId)
                .build();
    }
}
