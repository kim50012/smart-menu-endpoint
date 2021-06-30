package com.basoft.eorder.domain.model.retailOrderService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 零售商户订单售后服务表。一条记录整合所有信息，不拆分存储(RetailOrderService)实体类
 *
 * @author DongXifu
 * @since 2020-05-12 14:26:23
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RetailOrderService {
    private Long servId;

    private Long servCode; //售后编码，程序生成UUID

    private int servType; //售后类型，1-退货 2-换货 3-维修 4-补发商品

    private Long orderId; //订单ID

    private Long storeId; // 商户ID

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

    private int auditResult; //审核结果 1-通过 0-不通过。

    private String auditDesc; //审核备注

    private int auditRefundType; //退款类型，1-全款 2-部分退款 3-不退款

    private BigDecimal auditRefundAmount; //退款金额

    private String auditor; //审核人

    private Date auditTime; //审核时间

    private int servStatus;
}