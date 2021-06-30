package com.basoft.eorder.interfaces.command.order.retail;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 零售业务下单
 *
 * @author Mentor
 * @version 1.0
 * @since 20200421
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveRetailOrder implements Command {
    public static final String NAME = "saveRetailOrder";

    private BigDecimal amount;//订单金额

    private BigDecimal paymentAmount;//支付金额

    private BigDecimal totalAmount;//总金额

    private BigDecimal discountAmount; //优惠金额

    private String buyerMemo;//买家备注

    private List<OrderItem> itemList = new ArrayList<>();

    private String custNm; //姓名 이름

    private int countryNo; //国际号码 국가번호

    private String mobile; //手机号휴대폰 번호

    private String reseveDtfrom; //预约开始日期 예약 시작일

    private String reseveDtto; //预约结束日期 예약 종료일

    private int reseveTime; //预约上午下午 예약 오전/오후

    private String confirmDtfrom; //确定开始日期 확정 시작일

    private String confirmDtto; //确定结束日期 확정 종료일

    private int confirmTime; //确定上午下午 확정 오전/오후

    private int numPersons; //预约人员数 예약 인원

    // 配送地址类型 배송지유형。1-门店实时自提 2-门店预约时间自提 3-配送到提货点 4-配送到韩国 5-配送到中国
    private int shippingType;

    // 配送地址编码 배송지 ID。 配送地址类型为3时，为韩国自提货点编码；配送地址类型为4和5，客户的配送地址编码，该编码存储没有实际意义，因为编码对应的用户地址可以被修改或删除。
    private Long shippingAddr;

    private String shippingDt; //配送日期 배송일자

    private int shippingTime; //配送上午下午 배송오전 오후

    // 配送备注 배송비고。购物业务且配送地址类型为4或5的该字段存储配送详细地址
    private String shippingCmt;

    private String cmt; //备注 비고

    private String diningPlace;// 就餐地点 1-堂食 2-外带 3-配送

    private String diningTime;// 就餐时间 0-现在取餐 1-10分钟后到店 2-20分钟后到店 3-30分钟后到店

    private Long storeId;

    private String openId;

    private String custNo;

    private String custNmEn;

    private String nmLast;

    private String nmFirst;

    private String nmLastEn;

    private String nmFirstEn;

    private Long orderId;

    // 业务类型 1-点餐 2-购物 3-医美 4-酒店
    private int bizType;

    // 订单类型 1-正常商品订单 2-押金商品订单 默认值为1。非押金产品下单前端不传该值，取此处的1，押金产品前端传值为2
    private int orderType = 1;

    // 配送方式
    private Long shippingMode;

    private String shippingModeNameChn;

    private String shippingModeNameKor;

    private String shippingModeNameEng;

    // 配送详细地址
    private String shippingAddrDetail;

    // 配送地址国别
    private String shippingAddrCountry;

    // 配送重量
    private BigDecimal shippingWeight;

    // 配送费用
    private BigDecimal shippingCost;

    // 配送费用计算规则
    private String shippingCostRule;

    /**
     * build order
     *
     * @param id
     * @param storeId
     * @param tableId
     * @param customerId
     * @param ip
     * @param timeStart
     * @param timeExpire
     * @param payAmtUsd
     * @param krwUsdRate
     * @param totalAmount
     * @param cmt
     * @param storeType
     * @return
     */
    public Order build(
            Long id,
            Long storeId,
            Long tableId,
            String customerId,
            String ip,
            String timeStart,
            String timeExpire,
            BigDecimal payAmtUsd,
            BigDecimal krwUsdRate,
            BigDecimal totalAmount,
            String cmt,
            int storeType) {
        return new Order.Builder()
                .id(id)
                .storeId(storeId)
                .tableId(tableId)
                .amount(this.amount)
                .paymentAmount(this.paymentAmount)
                .totalAmount(totalAmount)
                .discountAmount(this.discountAmount)
                .payAmtUsd(payAmtUsd)
                .krwUsdRate(krwUsdRate)
                .status(Order.Status.NORMAL)
                .buyerMemo(this.buyerMemo)
                .customerId(customerId)
                .spbillCreateIp(ip)
                .timeStart(timeStart)
                .timeExpire(timeExpire)
                .itemList(this.itemList)
                .custNm(this.custNm)
                .countryNo(this.countryNo)
                .mobile(this.mobile)
                .reseveDtfrom(this.reseveDtfrom)
                .reseveDtto(this.reseveDtto)
                .reseveTime(this.reseveTime)
                .confirmDtfrom(this.confirmDtfrom)
                .confirmDtto(this.confirmDtto)
                .confirmTime(this.confirmTime)
                .numPersons(this.numPersons)
                .shippingType(this.shippingType)
                .shippingAddr(this.shippingAddr)
                .shippingDt(this.shippingDt)
                .shippingTime(this.shippingTime)
                .shippingCmt(this.shippingCmt)
                .cmt(cmt)
                .diningPlace(this.diningPlace)
                .diningTime(this.diningTime)
                .custNo(this.custNo)
                .custNmEn(this.custNmEn)
                .nmLast(this.nmLast)
                .nmFirst(this.nmFirst)
                .nmLastEn(this.nmLastEn)
                .nmFirstEn(this.nmFirstEn)
                .bizType(storeType)
                .orderType(this.orderType)
                .shippingMode(this.shippingMode)
                .shippingModeNameChn(this.shippingModeNameChn)
                .shippingModeNameKor(this.shippingModeNameKor)
                .shippingModeNameEng(this.shippingModeNameEng)
                .shippingAddrDetail(this.shippingAddrDetail)
                .shippingAddrCountry(this.shippingAddrCountry)
                .shippingWeight(this.shippingWeight)
                .shippingCost(this.shippingCost)
                .shippingCostRule(this.shippingCostRule)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("amount", amount)
                .append("paymentAmount", paymentAmount)
                .append("totalAmount", totalAmount)
                .append("discountAmount", discountAmount)
                .append("buyerMemo", buyerMemo)
                .append("itemList", itemList)
                .append("custNm", custNm)
                .append("countryNo", countryNo)
                .append("mobile", mobile)
                .append("reseveDtfrom", reseveDtfrom)
                .append("reseveDtto", reseveDtto)
                .append("reseveTime", reseveTime)
                .append("confirmDtfrom", confirmDtfrom)
                .append("confirmDtto", confirmDtto)
                .append("confirmTime", confirmTime)
                .append("numPersons", numPersons)
                .append("shippingType", shippingType)
                .append("shippingAddr", shippingAddr)
                .append("shippingDt", shippingDt)
                .append("shippingTime", shippingTime)
                .append("shippingCmt", shippingCmt)
                .append("cmt", cmt)
                .append("diningPlace", diningPlace)
                .append("diningTime", diningTime)
                .append("storeId", storeId)
                .append("openId", openId)
                .append("custNo", custNo)
                .append("custNmEn", custNmEn)
                .append("nmLast", nmLast)
                .append("nmFirst", nmFirst)
                .append("nmLastEn", nmLastEn)
                .append("nmFirstEn", nmFirstEn)
                .append("orderId", orderId)
                .append("bizType", bizType)
                .append("orderType", orderType)
                .append("shippingMode", shippingMode)
                .append("shippingModeNameChn", shippingModeNameChn)
                .append("shippingModeNameKor", shippingModeNameKor)
                .append("shippingModeNameEng", shippingModeNameEng)
                .append("shippingAddrDetail", shippingAddrDetail)
                .append("shippingAddrCountry", shippingAddrCountry)
                .append("shippingWeight", shippingWeight)
                .append("shippingCost", shippingCost)
                .append("shippingCostRule", shippingCostRule)
                .toString();
    }
}