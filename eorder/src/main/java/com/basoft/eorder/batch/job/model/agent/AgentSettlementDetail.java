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
public class AgentSettlementDetail {
    // 结算编号
    private Long sid;

    // 商户ID
    private Long storeId;

    // 结算类型 1-按营业额百分比 2-定额，当前业务设定永远按照百分比进行结算
    private int settleType;

    // 平台结算百分比
    private int plRate;

    // 代理商结算百分比
    private BigDecimal agtRate;

    // 代理商结算百分比占平台结算百分比的百分数，例平台结算3%，
    // 即订单额的3%给平台，其中一半即订单总额的1.5%给代理商，则该字段值为50，即表示平台结算的50%给代理商
    private int agtPercent;

    // 结算订单数量
    private Integer orderCount;

    // 结算总金额
    private BigDecimal settleSum;

    // 代理商结算金额
    private String agtFee;

    // 代理商结算VAT金额
    private String vatFee;

    // 创建时间
    private Date createTime;

    // 创建人
    private String createUser;

    // 修改时间
    private Date updateTime;

    //修改人
    private String updateUser;
}

