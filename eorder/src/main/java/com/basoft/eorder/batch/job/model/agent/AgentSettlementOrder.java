package com.basoft.eorder.batch.job.model.agent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentSettlementOrder {
    // 记录编号
    private Long soId;

    // 结算编号
    private Long sid;

    // 订单ID
    private Long orderId;

    // 订单时间
    private Date orderDate;

    // 商户ID
    private Long storeId;

    // 商户的平台结算比率
    private BigDecimal storePlatRate;

    // openid
    private String openId;

    // 订单金额
    private BigDecimal orderAmount;

    // 订单状态
    private int orderStatus;

    // 退款日期
    private Date cancelDt;

    // 是否退款订单 1-是 0-否
    private int isRefund;

    // 平台结算金额
    private BigDecimal plFee;

    // 代理商结算金额
    private BigDecimal agtFee;

    // VAT金额
    private BigDecimal vatFee;

    // 创建时间
    private Date createTime;

    // 创建人
    private String createUser;

    // 修改时间
    private Date updateTime;

    //修改人
    private String updateUser;
}