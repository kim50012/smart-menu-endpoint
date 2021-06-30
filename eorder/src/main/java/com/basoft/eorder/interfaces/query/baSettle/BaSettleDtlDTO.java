package com.basoft.eorder.interfaces.query.baSettle;

import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 2:23 PM 1/7/20
 **/
@Data
public class BaSettleDtlDTO {
    private Long orderId;
    private Long amount;//交易金额
    private String completeDate;
    private String settleRate;
    private String payDt;
    private String pgPlanDt;
    private Long pgAmount;
    private Long plFinalFee;  //平台服务费
    private Long pgFee;  //PG服务费
    private Long settleAmount;  //PG服务费
    private int orderCount;//订单数量
    private String reseveDtFrom;//预约时间
    private Long serviceFeeSum;//平台加店铺服务费

}
