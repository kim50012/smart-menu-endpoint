package com.basoft.eorder.interfaces.query.excel;

import com.basoft.eorder.domain.excel.ExcelColumn;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 3:40 下午 2019/11/4
 **/
@Data
public class StoreSettleDtlExcel {
    @ExcelColumn(valueKor = "PG정산예정일",valueChn = "pg结算日期",col = 1)
    private String pgFrDt;
    @ExcelColumn(valueKor = "주문번호",valueChn = "订单编号",col = 2)
    private String orderId;
    @ExcelColumn(valueKor = "거래일자",valueChn = "交易时间",col = 3)
    private String payFrDt;
    @ExcelColumn(valueKor = "거래금액",valueChn = "交易金额",col = 4)
    private String amount;
    @ExcelColumn(valueKor = "PG정산금액",valueChn = "pg结算金额",col = 5)
    private String pgAmount;
    @ExcelColumn(valueKor = "PG수수료",valueChn = "pg手续费",col = 6)
    private String pgFee;
    @ExcelColumn(valueKor = "서비스 이용료",valueChn = "平台服务费",col = 7)
    private String serviceFee;

}
