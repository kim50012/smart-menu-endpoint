package com.basoft.eorder.domain.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Order {
    /**
     * 订单状态初步定义：
     * 0-新插入订单，该状态存在于临时表
     * 1-预支付发送但出现异常，该状态存在于订单临时表
     * 2-预支付成功，并且该订单已经成功同步消息给微信支付平台，该状态存在于订单临时表
     * 3-预支付失败，该状态存在于订单临时表
     * <p>
     * 4-支付完成，该状态存在于订单正式表
     * 5-店主接受订单
     */
    /**
    订单状态详细扩展定义：
    ● 餐厅
    -----------------
    PAYMENY(4) : 支付成功
    ACEEPT(5) : 订单接收
    CANCEL_REQ(6) : 退款进行中
    CANCELED(7) : 退款成功
    CANCEL_FAIL(8) : 退款失败
    COMPLETE(9) : 订单完成
    SHIPPING_REDY(10) : 商品准备完成
    SHIPPING_COMP(11) : 商品配送完成
    说明：10和11状态不会同时出现。业务规定-10为到店自取订单，11为配送订单
    ● 酒店
    -----------------
    PAYMENY(4) : 支付成功
    ACEEPT(5) : 预约确定
    CANCEL_REQ(6) : 退款进行中
    CANCELED(7) : 退款成功
    CANCEL_FAIL(8) : 退款失败
    COMPLETE(9) : 订单完成
    ● 医美
    -----------------
    PAYMENY(4) : 支付成功
    ACEEPT(5) : 预约确定
    CANCEL_REQ(6) : 退款进行中
    CANCELED(7) : 退款成功
    CANCEL_FAIL(8) : 退款失败
    COMPLETE(9) : 订单完成
    ● 购物
    -----------------
    PAYMENY(4) : 支付成功
    ACEEPT(5) : 订单接收
    CANCEL_REQ(6) : 退款进行中
    CANCELED(7) : 退款成功
    CANCEL_FAIL(8) : 退款失败
    COMPLETE(9) : 订单完成
    SHIPPING_REDY(10) : 商品准备完成
    SHIPPING_COMP(11) : 商品配送完成
    说明：10和11状态不会同时出现。业务规定-10为到店自取订单，11为配送订单
    */
    public enum Status {
        NORMAL(0), SEND(1), SUCCES(2), FAIL(3), PAYMENY(4), ACEEPT(5), CANCEL_REQ(6), CANCELED(7), CANCEL_FAIL(8), COMPLETE(9), SHIPPING_REDY(10), SHIPPING_COMP(11);
        private final int code;

        Status(int code) {
            this.code = code;
        }

        public int getStatusCode() {
            return code;
        }

        public int code() {
            return code;
        }

        public static Status get(int code) {
            return
                    Arrays.asList(Status.values())
                            .stream()
                            .filter(new Predicate<Status>() {
                                @Override
                                public boolean test(Status status) {
                                    return status.code == code;
                                }
                            })
                            .findFirst()
                            .orElseGet(new Supplier<Status>() {
                                @Override
                                public Status get() {
                                    return null;
                                }
                            });
        }
    }

    private Long id;//订单编号
    private Long storeId;
    private Long tableId;
    private BigDecimal amount;//订单金额
    private BigDecimal paymentAmount;//支付金额
    private BigDecimal totalAmount;
    private BigDecimal payAmtUsd;
    private BigDecimal payAmtCny;
    private BigDecimal discountAmount; //优惠金额
    private BigDecimal krwUsdRate;
    private Status status;//订单状态
    private String buyerMemo;//买家备注
    private String customerId;//顾客编号
    private String prepayId;
    private String spbillCreateIp; //ip
    private String timeStart; //开始时间-中国时间微信专用
    private String timeExpire;//订单订单失效时间-中国时间微信专用
    private String refundId;
    private Long payId;
    private Long orderId;
    private Long transId;
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
    private int shippingType; //配送地址类型 배송지유형
    private Long shippingAddr; //配送地址编码 배송지 ID
    private String shippingDt; //配送日期 배송일자
    private int shippingTime; //配送上午下午 배송오전 오후
    private String shippingCmt; //配送备注 배송비고
    private String cmt; //备注 비고
    private String cmtCopie;//备注翻译
    private String diningPlace;// 就餐地点 1-堂食 2-外带 3-配送
    private String diningTime;// 就餐时间 0-现在取餐 1-10分钟后到店 2-20分钟后到店 3-30分钟后到店
    private String productNm;
    private String shippingAddrNm; //配送地址编码 배송지 ID
    private String custNo; //
    private String custNmEn; //
    private String nmLast; //
    private String nmFirst; //
    private String nmLastEn; //
    private String nmFirstEn; //

    // 20190905-新增押金产品的支持-start
    // 业务类型 1-餐厅 2-医院 3-购物 4-酒店
    private int bizType;
    // 订单类型 1-正常商品订单 2-押金商品订单
    private int orderType;
    // 20190905-新增押金产品的支持-end

    /**
     * 20200212-酒店到手价改造
     * 订单中新增酒店到手价和商户时间点结算费率
     */
    private BigDecimal amountSettle;
    private double rateSettle;

    /**
     * 20200512-购物改造
     * 购物增加邮递配送
     */
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

    // 状态8的来源
    private int status8From;

    // 订单变更状态
    private int changeStatus;


    public Long getId() {
        return id;
    }

    // public Long setId()

    public Long getStoreId() {
        return storeId;
    }

    public Long getPayId() {
        return payId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getTransId() {
        return transId;
    }

    public Long getTableId() {
        return tableId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getPayAmtUsd() {
        return payAmtUsd;
    }

    public BigDecimal getPayAmtCny() {
        return payAmtCny;
    }

    public BigDecimal getKrwUsdRate() {
        return krwUsdRate;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public int getStatus() {
        return status.getStatusCode();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getBuyerMemo() {
        return buyerMemo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public List<OrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderItem> itemList) {
        this.itemList = itemList;
    }

    public List<OrderItem> items() {
        return Collections.unmodifiableList(this.itemList);
    }

    public final BigDecimal totalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        this.itemList.forEach(orderItem -> total.add(orderItem.getPrice()));
        return total;
    }

    public final BigDecimal getTotalQty() {
        return this.items() != null && this.items().size() > 0 ? new BigDecimal(this.items().size()) : BigDecimal.ZERO;
    }

    public String getCustNm() {
        return custNm;
    }

    public int getCountryNo() {
        return countryNo;
    }

    public String getMobile() {
        return mobile;
    }

    public String getReseveDtfrom() {
        return reseveDtfrom;
    }

    public String getReseveDtto() {
        return reseveDtto;
    }

    public int getReseveTime() {
        return reseveTime;
    }

    public String getConfirmDtfrom() {
        return confirmDtfrom;
    }

    public String getConfirmDtto() {
        return confirmDtto;
    }

    public int getConfirmTime() {
        return confirmTime;
    }

    public int getNumPersons() {
        return numPersons;
    }

    public int getShippingType() {
        return shippingType;
    }

    public Long getShippingAddr() {
        return shippingAddr;
    }

    public String getShippingDt() {
        return shippingDt;
    }

    public int getShippingTime() {
        return shippingTime;
    }

    public String getShippingCmt() {
        return shippingCmt;
    }

    public String getCmt() {
        return cmt;
    }

    public String getCmtCopie() {
        return cmtCopie;
    }

    public String getDiningPlace() {
        return diningPlace;
    }

    public void setDiningPlace(String diningPlace) {
        this.diningPlace = diningPlace;
    }

    public String getDiningTime() {
        return diningTime;
    }

    public void setDiningTime(String diningTime) {
        this.diningTime = diningTime;
    }

    public String getProductNm() {
        return productNm;
    }

    public String getShippingAddrNm() {
        return shippingAddrNm;
    }

    public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getCustNmEn() {
		return custNmEn;
	}

	public void setCustNmEn(String custNmEn) {
		this.custNmEn = custNmEn;
	}

	public String getNmLast() {
		return nmLast;
	}

	public void setNmLast(String nmLast) {
		this.nmLast = nmLast;
	}

	public String getNmFirst() {
		return nmFirst;
	}

	public void setNmFirst(String nmFirst) {
		this.nmFirst = nmFirst;
	}

	public String getNmLastEn() {
		return nmLastEn;
	}

	public void setNmLastEn(String nmLastEn) {
		this.nmLastEn = nmLastEn;
	}

	public String getNmFirstEn() {
		return nmFirstEn;
	}

	public void setNmFirstEn(String nmFirstEn) {
		this.nmFirstEn = nmFirstEn;
	}

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public BigDecimal getAmountSettle() {
        return amountSettle;
    }

    public void setAmountSettle(BigDecimal amountSettle) {
        this.amountSettle = amountSettle;
    }

    public double getRateSettle() {
        return rateSettle;
    }

    public void setRateSettle(double rateSettle) {
        this.rateSettle = rateSettle;
    }

    public Long getShippingMode() {
        return shippingMode;
    }

    public void setShippingMode(Long shippingMode) {
        this.shippingMode = shippingMode;
    }

    public String getShippingModeNameChn() {
        return shippingModeNameChn;
    }

    public void setShippingModeNameChn(String shippingModeNameChn) {
        this.shippingModeNameChn = shippingModeNameChn;
    }

    public String getShippingModeNameKor() {
        return shippingModeNameKor;
    }

    public void setShippingModeNameKor(String shippingModeNameKor) {
        this.shippingModeNameKor = shippingModeNameKor;
    }

    public String getShippingModeNameEng() {
        return shippingModeNameEng;
    }

    public void setShippingModeNameEng(String shippingModeNameEng) {
        this.shippingModeNameEng = shippingModeNameEng;
    }

    public String getShippingAddrDetail() {
        return shippingAddrDetail;
    }

    public void setShippingAddrDetail(String shippingAddrDetail) {
        this.shippingAddrDetail = shippingAddrDetail;
    }

    public String getShippingAddrCountry() {
        return shippingAddrCountry;
    }

    public void setShippingAddrCountry(String shippingAddrCountry) {
        this.shippingAddrCountry = shippingAddrCountry;
    }

    public BigDecimal getShippingWeight() {
        return shippingWeight;
    }

    public void setShippingWeight(BigDecimal shippingWeight) {
        this.shippingWeight = shippingWeight;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getShippingCostRule() {
        return shippingCostRule;
    }

    public void setShippingCostRule(String shippingCostRule) {
        this.shippingCostRule = shippingCostRule;
    }

    public int getStatus8From() {
        return status8From;
    }

    public void setStatus8From(int status8From) {
        this.status8From = status8From;
    }

    public int getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(int changeStatus) {
        this.changeStatus = changeStatus;
    }

    public static final class Builder {
        private Long id;//订单编号
        private Long storeId;
        private Long payId;
        private Long orderId;
        private Long tableId;
        private BigDecimal amount;//订单金额
        private BigDecimal paymentAmount;//支付金额
        private BigDecimal totalAmount;
        private BigDecimal discountAmount;
        private BigDecimal payAmtUsd;
        private BigDecimal payAmtCny;
        private BigDecimal krwUsdRate;
        private Status status;//订单状态
        private String buyerMemo;//买家备注
        private String customerId;
        private String prepayId;
        private String spbillCreateIp; //ip
        private String timeStart; //开始时间-中国时间微信专用
        private String timeExpire;//订单订单失效时间-中国时间微信专用
        private List<OrderItem> itemList = new ArrayList<>();
        private String refundId;
        private Long transId;
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
        private int shippingType; //配送地址类型 배송지유형
        private Long shippingAddr; //配送地址编码 배송지 ID
        private String shippingDt; //配送日期 배송일자
        private int shippingTime; //配送上午下午 배송오전 오후
        private String shippingCmt; //配送备注 배송비고
        private String cmt; //备注 비고
        private String cmtCopie; //备注翻译
        private String diningPlace;// 就餐地点 1-堂食 2-外带 3-配送
        private String diningTime;// 就餐时间 0-现在取餐 1-10分钟后到店 2-20分钟后到店 3-30分钟后到店
        private String productNm;
        private String shippingAddrNm;
        private String custNo; //
        private String custNmEn; //
        private String nmLast; //
        private String nmFirst; //
        private String nmLastEn; //
        private String nmFirstEn; //

        // 20190905-新增押金产品的支持-start
        // 业务类型 1-点餐 2-购物 3-医美 4-酒店
        private int bizType;
        // 订单类型 1-正常商品订单 2-押金商品订单
        private int orderType;
        // 20190905-新增押金产品的支持-end

        /**
         * 20200512-购物改造
         * 购物增加邮递配送
         */
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

        // 状态8的来源
        private int status8From;

        // 订单变更状态
        private int changeStatus;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder storeId(Long storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder payId(Long payId) {
            this.payId = payId;
            return this;
        }

        public Builder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder tableId(Long tableId) {
            this.tableId = tableId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder paymentAmount(BigDecimal paymentAmount) {
            this.paymentAmount = paymentAmount;
            return this;
        }

        public Builder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder payAmtUsd(BigDecimal payAmtUsd) {
            this.payAmtUsd = payAmtUsd;
            return this;
        }

        public Builder payAmtCny(BigDecimal payAmtCny) {
            this.payAmtCny = payAmtCny;
            return this;
        }

        public Builder krwUsdRate(BigDecimal krwUsdRate) {
            this.krwUsdRate = krwUsdRate;
            return this;
        }

        public Builder discountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder buyerMemo(String buyerMemo) {
            this.buyerMemo = buyerMemo;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder prepayId(String prepayId) {
            this.prepayId = prepayId;
            return this;
        }

        public Builder refundId(String refundId) {
            this.refundId = refundId;
            return this;
        }

        public Builder spbillCreateIp(String spbillCreateIp) {
            this.spbillCreateIp = spbillCreateIp;
            return this;
        }

        public Builder timeStart(String timeStart) {
            this.timeStart = timeStart;
            return this;
        }

        public Builder timeExpire(String timeExpire) {
            this.timeExpire = timeExpire;
            return this;
        }

        public Builder itemList(List<OrderItem> itemList) {
            this.itemList = itemList;
            return this;
        }

        public Builder transId(Long transId) {
            this.transId = transId;
            return this;
        }

        public Builder custNm(String custNm) {
            this.custNm = custNm;
            return this;
        }

        public Builder countryNo(int countryNo) {
            this.countryNo = countryNo;
            return this;
        }

        public Builder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Builder reseveDtfrom(String reseveDtfrom) {
            this.reseveDtfrom = reseveDtfrom;
            return this;
        }

        public Builder reseveDtto(String reseveDtto) {
            this.reseveDtto = reseveDtto;
            return this;
        }

        public Builder reseveTime(int reseveTime) {
            this.reseveTime = reseveTime;
            return this;
        }

        public Builder confirmDtfrom(String confirmDtfrom) {
            this.confirmDtfrom = confirmDtfrom;
            return this;
        }

        public Builder confirmDtto(String confirmDtto) {
            this.confirmDtto = confirmDtto;
            return this;
        }

        public Builder confirmTime(int confirmTime) {
            this.confirmTime = confirmTime;
            return this;
        }

        public Builder numPersons(int numPersons) {
            this.numPersons = numPersons;
            return this;
        }

        public Builder shippingType(int shippingType) {
            this.shippingType = shippingType;
            return this;
        }

        public Builder shippingAddr(Long shippingAddr) {
            this.shippingAddr = shippingAddr;
            return this;
        }

        public Builder shippingDt(String shippingDt) {
            this.shippingDt = shippingDt;
            return this;
        }

        public Builder shippingTime(int shippingTime) {
            this.shippingTime = shippingTime;
            return this;
        }

        public Builder shippingCmt(String shippingCmt) {
            this.shippingCmt = shippingCmt;
            return this;
        }

        public Builder cmt(String cmt) {
            this.cmt = cmt;
            return this;
        }
        public Builder cmtCopie(String cmtCopie) {
            this.cmtCopie = cmtCopie;
            return this;
        }

        public Builder diningPlace(String diningPlace) {
            this.diningPlace = diningPlace;
            return this;
        }

        public Builder diningTime(String diningTime) {
            this.diningTime = diningTime;
            return this;
        }

        public Builder productNm(String productNm) {
            this.productNm = productNm;
            return this;
        }

        public Builder shippingAddrNm(String shippingAddrNm) {
            this.shippingAddrNm = shippingAddrNm;
            return this;
        }

        public Builder custNo(String custNo) {
            this.custNo = custNo;
            return this;
        }

        public Builder custNmEn(String custNmEn) {
            this.custNmEn = custNmEn;
            return this;
        }

        public Builder nmLast(String nmLast) {
            this.nmLast = nmLast;
            return this;
        }

        public Builder nmFirst(String nmFirst) {
            this.nmFirst = nmFirst;
            return this;
        }

        public Builder nmLastEn(String nmLastEn) {
            this.nmLastEn = nmLastEn;
            return this;
        }

        public Builder nmFirstEn(String nmFirstEn) {
            this.nmFirstEn = nmFirstEn;
            return this;
        }

        public Builder bizType(int bizType) {
            this.bizType = bizType;
            return this;
        }

        public Builder orderType(int orderType) {
            this.orderType = orderType;
            return this;
        }

        public Builder shippingMode(Long shippingMode) {
            this.shippingMode = shippingMode;
            return this;
        }

        public Builder shippingModeNameChn(String shippingModeNameChn) {
            this.shippingModeNameChn = shippingModeNameChn;
            return this;
        }

        public Builder shippingModeNameKor(String shippingModeNameKor) {
            this.shippingModeNameKor = shippingModeNameKor;
            return this;
        }

        public Builder shippingModeNameEng(String shippingModeNameEng) {
            this.shippingModeNameEng = shippingModeNameEng;
            return this;
        }

        public Builder shippingAddrDetail(String shippingAddrDetail) {
            this.shippingAddrDetail = shippingAddrDetail;
            return this;
        }

        public Builder shippingAddrCountry(String shippingAddrCountry) {
            this.shippingAddrCountry = shippingAddrCountry;
            return this;
        }

        public Builder shippingWeight(BigDecimal shippingWeight) {
            this.shippingWeight = shippingWeight;
            return this;
        }

        public Builder shippingCost(BigDecimal shippingCost) {
            this.shippingCost = shippingCost;
            return this;
        }

        public Builder shippingCostRule(String shippingCostRule) {
            this.shippingCostRule = shippingCostRule;
            return this;
        }

        public Builder status8From(int status8From) {
            this.status8From = status8From;
            return this;
        }

        public Builder changeStatus(int changeStatus) {
            this.changeStatus = changeStatus;
            return this;
        }

        public Order build() {
            Order order = new Order();
            order.id = this.id;
            order.storeId = this.storeId;
            order.tableId = this.tableId;
            order.payId = this.payId;
            order.orderId = this.orderId;
            order.amount = this.amount;
            order.paymentAmount = this.paymentAmount;//支付金额
            order.totalAmount = this.totalAmount;
            order.payAmtUsd = this.payAmtUsd;
            order.payAmtCny = this.payAmtCny;
            order.krwUsdRate = this.krwUsdRate;
            order.discountAmount = this.discountAmount;
            order.status = this.status;//订单状态
            order.buyerMemo = this.buyerMemo;//买家备注
            order.customerId = this.customerId;
            order.prepayId = this.prepayId;
            order.spbillCreateIp = this.spbillCreateIp;
            order.timeStart = this.timeStart;
            order.timeExpire = this.timeExpire;
            order.itemList = this.itemList;
            order.refundId = this.refundId;
            order.transId = this.transId;
            order.custNm = this.custNm;
            order.countryNo = this.countryNo;
            order.mobile = this.mobile;
            order.reseveDtfrom = this.reseveDtfrom;
            order.reseveDtto = this.reseveDtto;
            order.reseveTime = this.reseveTime;
            order.confirmDtfrom = this.confirmDtfrom;
            order.confirmDtto = this.confirmDtto;
            order.confirmTime = this.confirmTime;
            order.numPersons = this.numPersons;
            order.shippingType = this.shippingType;
            order.shippingAddr = this.shippingAddr;
            order.shippingDt = this.shippingDt;
            order.shippingTime = this.shippingTime;
            order.shippingCmt = this.shippingCmt;
            order.cmt = this.cmt;
            order.cmtCopie = this.cmtCopie;
            order.diningPlace = this.diningPlace;
            order.diningTime = this.diningTime;
            order.productNm = this.productNm;
            order.shippingAddrNm = this.shippingAddrNm;
            order.custNo = this.custNo;
            order.custNmEn = this.custNmEn;
            order.nmLast = this.nmLast;
            order.nmFirst = this.nmFirst;
            order.nmLastEn = this.nmLastEn;
            order.nmFirstEn = this.nmFirstEn;
            order.bizType = this.bizType;
            order.orderType = this.orderType;
            order.shippingMode = this.shippingMode;
            order.shippingModeNameChn = this.shippingModeNameChn;
            order.shippingModeNameKor = this.shippingModeNameKor;
            order.shippingModeNameEng = this.shippingModeNameEng;
            order.shippingAddrDetail = this.shippingAddrDetail;
            order.shippingAddrCountry = this.shippingAddrCountry;
            order.shippingWeight = this.shippingWeight;
            order.shippingCost = this.shippingCost;
            order.shippingCostRule = this.shippingCostRule;
            order.status8From = this.status8From;
            order.changeStatus = this.changeStatus;
            return order;
        }
    }
	
    public Builder createOrder(Long id, Order order) {
        return new Builder()
                .id(id)
                .storeId(order.storeId)
                .tableId(order.tableId)
                .amount(order.amount)
                .discountAmount(order.discountAmount)
                .paymentAmount(order.paymentAmount)
                .totalAmount(order.totalAmount)
                .payAmtUsd(order.payAmtUsd)
                .payAmtCny(order.payAmtCny)
                .status(Status.NORMAL)
                .buyerMemo(order.buyerMemo)
                .customerId(order.customerId)
                .itemList(order.itemList)
                .refundId(order.refundId)
                .krwUsdRate(order.krwUsdRate)
                .transId(order.transId)
                .custNm(order.custNm)
                .countryNo(order.countryNo)
                .mobile(order.mobile)
                .reseveDtfrom(order.reseveDtfrom)
                .reseveDtto(order.reseveDtto)
                .reseveTime(order.reseveTime)
                .confirmDtfrom(order.confirmDtfrom)
                .confirmDtto(order.confirmDtto)
                .confirmTime(order.confirmTime)
                .numPersons(order.numPersons)
                .shippingType(order.shippingType)
                .shippingAddr(order.shippingAddr)
                .shippingDt(order.shippingDt)
                .shippingTime(order.shippingTime)
                .shippingCmt(order.shippingCmt)
                .cmt(order.cmt)
                .diningPlace(order.diningPlace)
                .diningTime(order.diningTime)
                .productNm(order.productNm)
                .shippingAddrNm(order.shippingAddrNm)
		        .custNo(order.custNo)
		        .custNmEn(order.custNmEn)
		        .nmLast(order.nmLast)
		        .nmFirst(order.nmFirst)
		        .nmLastEn(order.nmLastEn)
		        .nmFirstEn(order.nmFirstEn)
                .bizType(order.bizType)
                .orderType(order.orderType)
                .shippingMode(order.shippingMode)
                .shippingModeNameChn(order.shippingModeNameChn)
                .shippingModeNameKor(order.shippingModeNameKor)
                .shippingModeNameEng(order.shippingModeNameEng)
                .shippingAddrDetail(order.shippingAddrDetail)
                .shippingAddrCountry(order.shippingAddrCountry)
                .shippingWeight(order.shippingWeight)
                .shippingCost(order.shippingCost)
                .shippingCostRule(order.shippingCostRule)
                .status8From(order.status8From)
                .changeStatus(order.changeStatus);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}