package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.domain.excel.ExcelColumn;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:43 2019/3/7
 **/
@Data
public class SettleDTO {

    private String orderId;
    private Long storeId;
    @ExcelColumn(valueKor = "storeNm",valueChn = "营业点名称",col = 6)
    private String storeNm;
    @ExcelColumn(valueKor = "storeType",valueChn = "服务类型",col = 7)
    private String storeType;
    @ExcelColumn(valueKor = "areaNm",valueChn = "地区",col = 8)
    private String areaNm;
    private String closingMonths;
    private String settleType;//结算类型
    @ExcelColumn(valueKor = "settleType",valueChn = "结算类型",col = 9)
    private String settleTypeStr;
    @ExcelColumn(valueKor = "isPay",valueChn = "微信支付",col = 10)
    private String isPay;//是否需要支付
    @ExcelColumn(valueKor = "plMinFee",valueChn = "最小服务费",col = 11)
    private String plMinFee;//最小服务费
    private String serviceFeeSum;//总服务费

    //付款
    @ExcelColumn(valueKor = "payDt",valueChn = "交易日期",col = 2)
    private String payDt;
    private String payFrDt;
    private String payToDt;
    private int payCnt;
    @ExcelColumn(valueKor = "총 거래금액",valueChn = "总交易金额",col = 3)
    private BigDecimal amount;
    //pg
    @ExcelColumn(valueKor = "pgPlanDt",valueChn = "pg结算日期",col = 1)
    private String pgPlanDt;
    private String pgFrDt;
    private String pgToDt;
    @ExcelColumn(valueKor = "pgAmount",valueChn = "pg结算金额",col = 4)
    private String pgAmount;
    @ExcelColumn(valueKor = "총 PG수수료 / PG정산금액",valueChn = "总结算手续费",col = 5)
    private String pgFee;


    private String baReciveDt;
    @ExcelColumn(valueKor = "baSureReciveDt",valueChn = "存款完成日期",col = 14)
    private String baSureReciveDt;



    @ExcelColumn(valueKor = "총 서비스 이용료",valueChn = "总服务费",col = 12)
    private String serviceFee;//平台服务费
    @ExcelColumn(valueKor = "finalFee",valueChn = "最终服务费",col = 13)
    private String finalFee;//平台服务费
    private String chargeRate;//服务费百分比
    private int chargeType;//收费类型 1-按营业额百分比 2-按营业额百分比或最小服务费 3-按月
    private String email;
    private int status;
    private int cloStatus;

    private List<OrderItemDTO> itemList;



    @Override
    public String toString() {
        return "SettleDTO{" +
                "orderId='" + orderId + '\'' +
                ", storeId=" + storeId +
                ", storeNm='" + storeNm + '\'' +
                ", areaNm='" + areaNm + '\'' +
                ", closingMonths='" + closingMonths + '\'' +
                ", payDt='" + payDt + '\'' +
                ", pgPlanDt='" + pgPlanDt + '\'' +
                ", payFrDt='" + payFrDt + '\'' +
                ", payToDt='" + payToDt + '\'' +
                ", pgFrDt='" + pgFrDt + '\'' +
                ", pgToDt='" + pgToDt + '\'' +
                ", baReciveDt='" + baReciveDt + '\'' +
                ", baSureReciveDt='" + baSureReciveDt + '\'' +
                ", payCnt=" + payCnt +
                ", pgAmount='" + pgAmount + '\'' +
                ", amount='" + amount + '\'' +
                ", pgFee='" + pgFee + '\'' +
                ", serviceFee='" + serviceFee + '\'' +
                ", status=" + status +
                ", itemList=" + itemList +
                '}';
    }
}
