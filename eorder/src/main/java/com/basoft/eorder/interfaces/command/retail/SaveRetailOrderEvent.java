package com.basoft.eorder.interfaces.command.retail;

import com.basoft.eorder.domain.model.retail.order.RetailOrderEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveRetailOrderEvent {
    private Long orderId;

    private int eventType;

    private int eventInitiator;

    private int isMain;

    private String eventTime;

    private String eventName;

    private String eventTarget;

    private int eventResult;

    private String eventResultDesc;

    private Long servId;

    /**
     * 订单事件定义：
     * 1-支付成功
     *
     * 2-已退款
     * 说明：商户发起退款，该订单完结
     *
     * 3-申请退款
     * 说明：支付成功后立即申请退款，此时必须对申请退款做出审核，拒绝后可以接单，同意后该订单完结
     * 4-商户审核退款申请
     * 说明：通过-平台退款；不通过，继续接单
     * 5-已退款
     *
     * 6-商户已接单，正在为您准备商品
     *
     * 7-已退款
     * 说明：商户发起退款，该订单完结
     *
     * 8-申请退款
     * 说明：商户接单后用户发起申请退款，此时必须对申请退款做出审核，拒绝后可以发货，同意后该订单完结
     * 9-商户审核退款申请
     * 10-已退款
     *
     * 11-已发货
     * 12-确认收货
     */
    public RetailOrderEvent build() {
        // 1-支付成功
        if (this.eventType == 1) {
            return RetailOrderEvent.builder()
                    .orderId(this.orderId)
                    .eventType(1)
                    .eventInitiator(1)
                    .isMain(1)
                    .eventName("支付成功")
                    .eventTarget("支付")
                    .eventResult(1)
                    .eventResultDesc("")
                    .servId(0L)
                    .build();
        }
        // 2-已退款
        // 支付成功后，商户直接发起退款
        else if (this.eventType == 2) {
            return RetailOrderEvent.builder()
                    .orderId(this.orderId)
                    .eventType(2)
                    .eventInitiator(2)
                    .isMain(1)
                    .eventName("商户已退款")
                    .eventTarget("退款")
                    .eventResult(1)
                    .eventResultDesc("")
                    .servId(0L)
                    .build();
        }
        // 3-申请退款
        // 说明：支付成功后立即申请退款，此时必须对申请退款做出审核，拒绝后可以接单，同意后该订单完结
        else if (this.eventType == 3) {
            return RetailOrderEvent.builder()
                    .orderId(this.orderId)
                    .eventType(3)
                    .eventInitiator(1)
                    .isMain(1)
                    .eventName("申请退款")
                    .eventTarget("退款")
                    .eventResult(1)
                    .eventResultDesc("")
                    .servId(0L)
                    .build();
        }
        // 4-商户审核退款申请
        // 说明：通过-平台退款；不通过，继续接单
        else if (this.eventType == 4) {
            return RetailOrderEvent.builder()
                    .orderId(this.orderId)
                    .eventType(4)
                    .eventInitiator(2)
                    .isMain(1)
                    .eventName("商户已受理退款申请")
                    .eventTarget("审核退款申请")
                    //记录审核结果和审核信息
                    .eventResult(this.eventResult)
                    .eventResultDesc(this.eventResultDesc)
                    .servId(0L)
                    .build();
        }
        // 5-已退款
        else if (this.eventType == 5) {
            return RetailOrderEvent.builder()
                    .orderId(this.orderId)
                    .eventType(5)
                    .eventInitiator(3)
                    .isMain(1)
                    //.eventName("退款申请通过审核，已退款")
                    .eventName("已退款")
                    .eventTarget("退款")
                    .eventResult(1)
                    .eventResultDesc("")
                    .servId(0L)
                    .build();
        }
        // 6-商户已接单，正在为您准备商品
        else if (eventType == 6) {
            return RetailOrderEvent.builder()
                    .orderId(this.orderId)
                    .eventType(6)
                    .eventInitiator(2)
                    .isMain(1)
                    .eventName("商户已接单，正在为您准备商品")
                    .eventTarget("接单")
                    .eventResult(1)
                    .eventResultDesc("")
                    .servId(0L)
                    .build();
        }
        // 7-已退款
        else if (this.eventType == 7) {
            return RetailOrderEvent.builder()
                    .orderId(this.orderId)
                    .eventType(7)
                    .eventInitiator(2)
                    .isMain(1)
                    .eventName("商户已退款")
                    .eventTarget("退款")
                    .eventResult(1)
                    .eventResultDesc("")
                    .servId(0L)
                    .build();
        }

        // 8-申请退款
        // 说明：商户接单后用户发起申请退款，此时必须对申请退款做出审核，拒绝后可以发货，同意后该订单完结
        else if (this.eventType == 8) {
            return RetailOrderEvent.builder()
                    .orderId(this.orderId)
                    .eventType(8)
                    .eventInitiator(1)
                    .isMain(1)
                    .eventName("申请退款")
                    .eventTarget("退款")
                    .eventResult(1)
                    .eventResultDesc("")
                    .servId(0L)
                    .build();
        }
        // 9-商户审核退款申请
        // 说明：通过-平台退款；不通过，继续发货
        // TODO
        else if (this.eventType == 9) {
            return RetailOrderEvent.builder()
                    .orderId(this.orderId)
                    .eventType(9)
                    .eventInitiator(2)
                    .isMain(1)
                    .eventName("商户已受理退款申请")
                    .eventTarget("审核退款申请")
                    //记录审核结果和审核信息
                    .eventResult(this.eventResult)
                    .eventResultDesc(this.eventResultDesc)
                    .servId(0L)
                    .build();
        }
        // 10-已退款
        else if (this.eventType == 10) {
            return RetailOrderEvent.builder()
                    .orderId(this.orderId)
                    .eventType(10)
                    .eventInitiator(3)
                    .isMain(1)
                    //.eventName("退款申请通过审核，已退款")
                    .eventName("已退款")
                    .eventTarget("退款")
                    .eventResult(1)
                    .eventResultDesc("")
                    .servId(0L)
                    .build();
        }
        // 11-已发货
        // TODO
        else if (this.eventType == 11) {
            return RetailOrderEvent.builder()
                    .orderId(this.orderId)
                    .eventType(11)
                    .eventInitiator(2)
                    .isMain(1)
                    .eventName("商户已发货")
                    .eventTarget("货物运输")
                    .eventResult(1)
                    //记录发货信息
                    .eventResultDesc(this.eventResultDesc)
                    .servId(0L)
                    .build();
        }
        // 12-确认收货
        else if (this.eventType == 12) {
            return RetailOrderEvent.builder()
                    .orderId(this.orderId)
                    .eventType(12)
                    .eventInitiator(1)
                    .isMain(1)
                    .eventName("确认收货")
                    .eventTarget("结束订单")
                    .eventResult(1)
                    .eventResultDesc("")
                    .servId(0L)
                    .build();
        }
        // 13-退换/售后
        // TODO
        else if (this.eventType == 13) {
            return RetailOrderEvent.builder()
                    .orderId(this.orderId)
                    .eventType(13)
                    .eventInitiator(1)
                    .isMain(0)
                    .eventName("退换/售后")
                    .eventTarget("退换/售后")
                    .eventResult(1)
                    .eventResultDesc("")
                    .servId(this.servId)
                    .build();
        }
        return null;
    }
}