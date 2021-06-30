package com.basoft.eorder.domain.model.order;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * BA交易类商戶月度订单结算
 *
 * @author Mentor
 * @version 1.0
 * @since 20200103
 */
@Builder
@Data
public class StoreSettlementBA {
    @JsonSerialize
    private Long sid;

    @JsonSerialize
    private Long storeID;

    private String settleYearMonth;

    private int settleYear;

    private int settleMonth;

    private String startDT;

    private String endDT;

    private int settleType;

    private double settleRate;

    private double settleFee;

    private Long orderCount;

    private BigDecimal settleSum;

    private String pgDate;

    private BigDecimal pgSum;

    private BigDecimal pgServiceFee;

    private String plDate;

    private BigDecimal plMinFee;

    private BigDecimal plServiceFee;

    private BigDecimal plFinalFee;

    private BigDecimal cashSettleSum;

    private String createTime;

    private String createUser;

    private String updateTime;

    private String updateUser;



    private Long orderCountDaoshou;

    private BigDecimal orderPlatAmountSum;

    private BigDecimal  orderDaoshouAmountSum;

    private Long orderCountDaoshouRefund;

    private BigDecimal orderPlatAmountSumRefund;

    private BigDecimal orderDaoshouAmountSumRefund;
}