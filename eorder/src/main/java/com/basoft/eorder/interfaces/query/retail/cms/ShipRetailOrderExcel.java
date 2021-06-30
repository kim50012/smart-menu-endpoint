package com.basoft.eorder.interfaces.query.retail.cms;

import com.basoft.eorder.domain.excel.ExcelColumn;
import com.basoft.eorder.interfaces.query.OrderItemDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShipRetailOrderExcel {
    @ExcelColumn(valueKor = "No",valueChn = "No",col = 1)
    private String numer;
    @ExcelColumn(valueKor = "주문번호",valueChn = "订单号",col = 2)
    private String id;
    @ExcelColumn(valueKor = "상품명",valueChn = "产品名称",col = 3)
    private String productNm;
    @ExcelColumn(valueKor = "구매수량",valueChn = "数量",col = 4)
    private int qty;
    @ExcelColumn(valueKor = "주문가격",valueChn = "价格",col = 5)
    private BigDecimal price;
    private List<OrderItemDTO> itemList;
    @ExcelColumn(valueKor = "예악자명",valueChn = "姓名",col = 6)
    private String custNm; //姓名 이름
    @ExcelColumn(valueKor = "전화번호",valueChn = "手机号",col = 7)
    private String mobile; //手机号휴대폰 번호
    @ExcelColumn(valueKor = "환불시간",valueChn = "取货时间",col = 8)
    private String shippingDt; //手机号휴대폰 번호
    @ExcelColumn(valueKor = "상태",valueChn = "订单状态",col = 9)
    private String status; //状态
    @ExcelColumn(valueKor = "고객비고",valueChn = "备注",col = 10)
    private String cmtt; //备注 비고
    private String countryNo; //国家编码
    private String isEnd;
}
