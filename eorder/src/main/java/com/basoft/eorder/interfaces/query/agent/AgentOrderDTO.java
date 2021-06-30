package com.basoft.eorder.interfaces.query.agent;

import com.basoft.eorder.domain.excel.ExcelColumn;
import com.basoft.eorder.interfaces.query.OrderItemDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 2:22 下午 2019/9/26
 **/

@Data
public class AgentOrderDTO {

    private String agtName;  //代理商名称
    private String agtCode;  //编码
    private Long orderId;//订单编号
    private Long storeId;
    private String storeName;
    private String areaNm;
    private String storeType;
    private Long tableId;
    private BigDecimal orderAmount;
    private String storeChargeRate;  //平台结算金额
    private BigDecimal agtFee;  //代理商结算金额
    private BigDecimal vatFee;  //VAT金额
    @ExcelColumn(valueKor = "sumAmount",valueChn = "顾客交易总金额",col = 1)
    private BigDecimal sumAmount;//总金额
    private int qty;
    private int status;//订单状态
    private String isRefund;//是否退款
    private String buyerMemo;//买家备注
    private String customerId;//顾客编号
    private String cmt; //备注
    private String openId;
    private String nickName; //昵称
    private String nickUnemoji; //昵称
    private String headImgUrl; //头像
    private String subscribeTime; //关注时间
    private String created;
    private String orderDate;
    private String cancelDt;
    private String completeDate;
    private List<OrderItemDTO> itemList;


}
