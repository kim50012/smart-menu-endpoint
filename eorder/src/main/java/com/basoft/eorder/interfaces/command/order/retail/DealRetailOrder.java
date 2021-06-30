package com.basoft.eorder.interfaces.command.order.retail;

import com.basoft.eorder.application.framework.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 零售业务H5和CMS处理订单信息VO，用于接收CMS处理订单的参数信息
 *
 * @author Mentor
 * @version 1.0
 * @created in 20200518
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DealRetailOrder implements Command {
    static final String NAME_CANCEL_ORDER = "cancelRetailOrder";

    private Long orderId;

    // 2-商户退款 7-商户退款
    private int eventType;

    // 1-用户要求退款  2-商户主动退款 3-售后退款
    private int cancelReason;

    BigDecimal refundAmountKor;

    BigDecimal refundAmountCny;
}