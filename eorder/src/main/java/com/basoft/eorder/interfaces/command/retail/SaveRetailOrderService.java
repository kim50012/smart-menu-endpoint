package com.basoft.eorder.interfaces.command.retail;

import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.retailOrderService.RetailOrderService;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 零售商户订单售后服务表。一条记录整合所有信息，不拆分存储(RetailOrderService)入参
 *
 * @author DongXifu
 * @since 2020-05-12 14:26:23
 */
@Data
public class SaveRetailOrderService implements Command {
    public static final String ACCEPTANCE = "or_acceptance";//受理

    public static final String AUDIT = "or_audit";//审核

    public static final String RETRY_REFUND = "or_refund";//退款失败后，重启退款

    private Long servId;

    private Long servCode; //售后编码，程序生成UUID

    private int servType; //售后类型，1-退货 2-换货 3-维修 4-补发商品

    private int servStatus; //售后状态，1-申请 2-受理 3-审核 4-退款中 5-退款成功 6-退款失败

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

    private int accept;//受理

    private int auditResult; //审核结果 1-通过 0-不通过。

    private Order.Status status;//订单状态

    private int changeStatus;//订单变更状态

    private String auditDesc; //审核备注

    private int auditRefundType; //退款类型，1-全款 2-部分退款 3-不退款

    private BigDecimal auditRefundAmount; //退款金额

    private String auditor; //审核人

    private Date auditTime; //审核时间

    public RetailOrderService build(UserSession userSession) {
        return RetailOrderService.builder()
                .servId(servId)
                .servCode(servCode)
                .servType(servType)
                .orderId(orderId)
                .applyCount(applyCount)
                .applyAmount(applyAmount)
                .applyDesc(applyDesc)
                .applyImages(applyImages)
                .applyDeliveryMode(applyDeliveryMode)
                .applyLinker(applyLinker)
                .applyMobile(applyMobile)
                .applyAddress(applyAddress)
                .applyTime(applyTime)
                .acceptor(userSession.getAccount())
                .acceptTime(acceptTime)
                .auditResult(auditResult)
                .auditDesc(auditDesc)
                .auditRefundType(auditRefundType)
                .auditRefundAmount(auditRefundAmount)
                .auditor(userSession.getAccount())
                .auditTime(auditTime)
                .servStatus(servStatus)
                .build();
    }

    /**
     * 售后申请审核入参校验
     *
     * @return
     */
    public boolean checkParameter() {
        if(this.servId == null || this.servId.longValue() == 0){
            return false;
        }

        if(this.servCode == null || this.servCode.longValue() == 0){
            return false;
        }

        if(this.auditResult != 1 && this.auditResult != 0){
            return false;
        }

        if(this.auditResult == 1){
            if(this.getAuditRefundType() != 1 && this.getAuditRefundType() != 2 && this.getAuditRefundType() != 3){
                // 审核通过，但是退款类型不合法
                return false;
            } else {
                // 审核通过，退款类型合法
                return true;
            }
        }
        return true;
    }

    /**
     * 设置售后状态
     */
    public void setServStatus() {
        // 审核通过
        if (this.auditResult == 1) {
            // 退款类型为1或2，1-全部退款 2-部分退款
            if (this.auditRefundType == 1 || this.auditRefundType == 2) {
                // 售后状态设置为4，退款中
                this.servStatus = 4;
            } else {
                // 售后状态设置为3，已审核完成
                this.servStatus = 3;
            }
        }
        // 审核不通过
        else if(this.auditResult == 0){
            this.servStatus = 3;
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("servId", servId)
                .append("servCode", servCode)
                .append("servType", servType)
                .append("servStatus", servStatus)
                .append("orderId", orderId)
                .append("applyCount", applyCount)
                .append("applyAmount", applyAmount)
                .append("applyDesc", applyDesc)
                .append("applyImages", applyImages)
                .append("applyDeliveryMode", applyDeliveryMode)
                .append("applyLinker", applyLinker)
                .append("applyMobile", applyMobile)
                .append("applyAddress", applyAddress)
                .append("applyTime", applyTime)
                .append("acceptor", acceptor)
                .append("acceptTime", acceptTime)
                .append("accept", accept)
                .append("auditResult", auditResult)
                .append("status", status)
                .append("changeStatus", changeStatus)
                .append("auditDesc", auditDesc)
                .append("auditRefundType", auditRefundType)
                .append("auditRefundAmount", auditRefundAmount)
                .append("auditor", auditor)
                .append("auditTime", auditTime)
                .toString();
    }
}