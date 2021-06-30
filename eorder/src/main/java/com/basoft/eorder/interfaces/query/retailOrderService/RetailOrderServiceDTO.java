package com.basoft.eorder.interfaces.query.retailOrderService;

import com.basoft.eorder.interfaces.query.OrderItemDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class RetailOrderServiceDTO {
   
    private Long servId;
   
    private Long servCode; //售后编码，程序生成UUID
   
    private int servType; //售后类型，1-退货 2-换货 3-维修 4-补发商品
   
    private Long orderId; //订单ID
   
    private Integer applyCount; //申请数量
   
    private BigDecimal applyAmount; //申请售后订单金额
   
    private String applyDesc; //申请说明。申请退货的理由；申请换货的理由，即新换产品的款式、颜色要求等

    private String applyImages; //申请图片，最多支持9张图片，图片URL拼接成英文逗号分隔的字符串保存在该字段
   
    private int applyDeliveryMode; //退货、换货、维修的邮寄方式，即将货物送回商户的快递方式，1-上门取件 2-自助快递到商户，默认为自助快递到商户，地址通过客服获取
   
    private String applyLinker; //换货、维修、补发邮寄联系人
   
    private String applyMobile; //换货、维修、补发邮寄联系手机号码
   
    private String applyAddress; //换货、维修、补发邮寄的收货地址
   
    private Date applyTime; //申请售后时间
   
    private String acceptor; //受理人
   
    private Date acceptTime; //受理时间
   
    private String auditResult; //审核结果 1-通过 0-不通过。

    private String servStatus; //售后状态 1-申请 2-受理 3-审核

    private String auditDesc; //审核备注
   
    private String auditRefundType; //退款类型，1-全款 2-部分退款 3-不退款
   
    private BigDecimal auditRefundAmount; //退款金额

    private BigDecimal amount;

    private BigDecimal paymentAmount;

    private String auditor; //审核人
   
    private Date auditTime; //审核时间

    private List<OrderItemDTO> itemList;

    private String shippingCmt; //配送备注 배송비고

    private String shippingAddrDetail;

    private String shippingAddrCountry;

    private String custNm;

    private String mobile;

    private String countryNo;

    private BigDecimal shippingWeight;

    private BigDecimal shippingCost;

    private int status;//订单状态

    public List<String> getApplyImages() {
        String arr[] = applyImages.split(",");
        List<String> imgs = new ArrayList<>(arr.length);
        for(String s:arr){
            imgs.add(s);
        }
        return imgs;
    }
}