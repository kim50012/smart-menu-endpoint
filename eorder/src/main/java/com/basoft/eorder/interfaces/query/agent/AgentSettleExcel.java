package com.basoft.eorder.interfaces.query.agent;

import com.basoft.eorder.domain.excel.ExcelColumn;
import com.basoft.eorder.domain.model.agent.Agent;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 3:29 下午 2019/11/5
 **/

@Data
public class AgentSettleExcel {

    @ExcelColumn(valueKor = "대리상 분류",valueChn = "代理商类型",col = 1)
    private String agtType;
    @ExcelColumn(valueKor = "대리상명",valueChn = "代理商名称",col = 2)
    private String agtName;
    @ExcelColumn(valueKor = "대리상코드",valueChn = "结算编码",col = 3)
    private String agtCode;
    @ExcelColumn(valueKor = "거래금액",valueChn = "交易金额",col = 4)
    private String sumAmount;
    @ExcelColumn(valueKor = "거래건수",valueChn = "交易件数",col = 5)
    private BigDecimal qty;
    @ExcelColumn(valueKor = "정산금액",valueChn = "结算总金额",col = 6)
    private String agtFee;
    @ExcelColumn(valueKor = "vat차감 정산금액",valueChn = "vat结算总金额",col = 7)
    private String vatFee;
    @ExcelColumn(valueKor = "정산계좌",valueChn = "代理商银行账号",col = 8)
    private String agtBankCode;  //银行账号

    public String getAgtType() {
        return agtType;
    }

    public void setAgtType(String agtType) {
        if (String.valueOf(Agent.SA_TP).equals(agtType)) {
            this.agtType = Agent.SA;
        } else {
            this.agtType = Agent.CA;
        }
    }


}
