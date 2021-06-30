package com.basoft.eorder.interfaces.query.excel;

import com.basoft.eorder.domain.excel.ExcelColumn;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 1:28 下午 2019/11/4
 **/

@Data
public class AdminSettleExcel {
    @ExcelColumn(valueKor = "서비스 분류",valueChn = "服务类型",col = 1)
    private String storeType;
    @ExcelColumn(valueKor = "영업점 명",valueChn = "营业点名称",col = 2)
    private String storeNm;
    @ExcelColumn(valueKor = "지역",valueChn = "地区",col = 3)
    private String areaNm;
    @ExcelColumn(valueKor = "결제모듈",valueChn = "微信支付",col = 4)
    private String isPay;//是否需要支付
    @ExcelColumn(valueKor = "정산유형",valueChn = "结算类型",col = 5)
    private String settleTypeStr;
    @ExcelColumn(valueKor = "거래날짜",valueChn = "交易日期",col = 6)
    private String payDt;
    @ExcelColumn(valueKor = "거래금액",valueChn = "交易金额",col = 7)
    private String amount;
    @ExcelColumn(valueKor = "PG정산예정일",valueChn = "pg结算日期",col = 8)
    private String pgPlanDt;
    @ExcelColumn(valueKor = "PG수수료",valueChn = "结算手续费",col = 9)
    private String pgFee;
    @ExcelColumn(valueKor = "최소이용료",valueChn = "最小服务费",col = 10)
    private String plMinFee;//最小服务费
    @ExcelColumn(valueKor = "서비스이용료",valueChn = "服务费",col = 11)
    private String serviceFee;//平台服务费
    @ExcelColumn(valueKor = "최종이용료",valueChn = "最终服务费",col = 12)
    private String finalFee;

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        if("1".equals(storeType)){
            this.storeType = "ORDER";
        } else if ("2".equals(storeType)) {
            this.storeType = "BOOKING";
        } else if ("3".equals(storeType)) {
            this.storeType = "RETAIL";
        } else if ("4".equals(storeType)) {
            this.storeType = "HOTEL";
        }
    }


}
