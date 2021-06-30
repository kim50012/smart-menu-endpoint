package com.basoft.eorder.interfaces.query.excel;

import com.basoft.eorder.domain.excel.ExcelColumn;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 2:51 下午 2019/11/4
 **/

@Data
public class AdminDtlSettleExcel {

    @ExcelColumn(valueKor = "영업점명",valueChn = "营业点名称",col = 1)
    private String storeNm;

    @ExcelColumn(valueKor = "지역",valueChn = "地区",col = 2)
    private String areaNm;

    @ExcelColumn(valueKor = "주문번호",valueChn = "订单编号",col = 3)
    private String orderId;

    @ExcelColumn(valueKor = "거래일자",valueChn = "交易日期",col = 4)
    private String payFrDt;

    @ExcelColumn(valueKor = "거래금액",valueChn = "交易金额",col = 5)
    private BigDecimal amount;

    @ExcelColumn(valueKor = "PG정산예정일",valueChn = "pg结算日期",col = 6)
    private String pgFrDt;

    @ExcelColumn(valueKor = "PG정산금액",valueChn = "pg结算金额",col = 7)
    private BigDecimal pgAmount;

    @ExcelColumn(valueKor = "PG수수료",valueChn = "结算手续费",col = 8)
    private BigDecimal pgFee;

    @ExcelColumn(valueKor = "서비스 이용료",valueChn = "最终服务费",col = 9)
    private BigDecimal serviceFee;


}
