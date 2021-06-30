package com.basoft.eorder.batch.job.model.agent;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
public class AgentSettlement {
    // 结算编号
    @JsonSerialize
    private Long sid;

    // 代理商ID
    @JsonSerialize
    private Long agtId;

    // 代理商编码
    private String agtCode;

    // 结算年月
    private String settleYearMonth;

    // 结算年
    private int settleYear;

    // 结算月
    private int settleMonth;

    // 结算起始日期
    private String startDT;

    // 结算结束日期
    private String endDT;

    // 结算订单数量
    private Long orderCount;

    // 结算总金额
    private BigDecimal settleSum;

    // 代理商结算金额
    private BigDecimal agtFee;

    // VAT金额
    private BigDecimal agtVatFee;

    // 平台结算日期
    private String plDate;

    // 创建时间
    private Date createTime;

    // 创建人
    private String createUser;

    // 修改时间
    private Date updateTime;

    //修改人
    private String updateUser;
}

