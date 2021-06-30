package com.basoft.eorder.interfaces.query.agent;

import com.basoft.eorder.domain.excel.ExcelColumn;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 1:53 下午 2019/11/5
 **/
@Data
public class AgentOrderExcel {
    @ExcelColumn(valueKor = "거래일자",valueChn = "交易日期",col = 1)
    private String orderDate;
    @ExcelColumn(valueKor = "주문번호",valueChn = "订单编号",col = 2)
    private Long orderId;//订单编号
    @ExcelColumn(valueKor = "영업점명",valueChn = "店铺名称",col = 3)
    private String storeName;
    @ExcelColumn(valueKor = "지역",valueChn = "地区",col = 4)
    private String areaNm;
    @ExcelColumn(valueKor = "거래금액",valueChn = "交易金额",col = 5)
    private BigDecimal orderAmount;
    @ExcelColumn(valueKor = "정산금액",valueChn = "代理商结算金额",col = 6)
    private BigDecimal agtFee;  //代理商结算金额
    @ExcelColumn(valueKor = "vat차감 정산금액",valueChn = "vat结算金额",col = 7)
    private BigDecimal vatFee;  //VAT金额

}
