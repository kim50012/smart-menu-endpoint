package com.basoft.eorder.batch.job.model.agent;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AgentStoreMap {
    // 结算编号
    @JsonSerialize
    private Long id;

    // 代理商ID
    @JsonSerialize
    private Long agtId;

    // 商户ID
    @JsonSerialize
    private Long storeId;

    // 代理商类型
    private int agtType;

    // 代理商结算类型-计费方式: 1-百分比  2-定额
    private int agtChargeType;

    // 代理商结算金额-手续费金额(当charge_type为2时才有值)
    private int agtChargeFee;

    // 按营业额百分比收取手续费
    private BigDecimal agtChargeRate;

    // 占店铺营业额百分比中的百分之多少
    private int agtChargePercent;

    // 合同起始时间
    private String contractSt;

    // 合同结束时间
    private String contractEd;

    private int status;

    // 创建时间
    private Date createTime;

    // 修改时间
    private Date updateTime;
}

