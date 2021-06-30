package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.OrderItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SaveOrder implements Command {
    public static final String NAME = "saveOrder";

    public static final String NO_PAY_NAME = "saveNoPayOrder";
    
    public static final String SAVE_STORE_CUSTNO = "saveStoreCustNo";

    public static final String UP_SHIPPING_CMT= "upShippingCmt";


    public static final String HOTEL_ORDER_NAME = "saveHotelOrder";


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
    private int shippingType; //配送地址类型 배송지유형
    private Long shippingAddr; //配送地址编码 배송지 ID
    private String shippingDt; //配送日期 배송일자
    private int shippingTime; //配送上午下午 배송오전 오후
    private String shippingCmt; //配送备注 배송비고
    private String cmt; //备注 비고
    private String diningPlace;// 就餐地点 1-堂食 2-外带 3-配送
    private String diningTime;// 就餐时间 0-现在取餐 1-10分钟后到店 2-20分钟后到店 3-30分钟后到店
    private Long storeId;
    private String openId;
    private String custNo; //
    private String custNmEn; //
    private String nmLast; //
    private String nmFirst; //
    private String nmLastEn; //
    private String nmFirstEn; //
    private Long orderId;

    // 20190905-新增押金产品的支持-start
    // 业务类型 1-点餐 2-购物 3-医美 4-酒店
    private int bizType;
    // 订单类型 1-正常商品订单 2-押金商品订单 默认值为1。非押金产品下单前端不传该值，取此处的1，押金产品前端传值为2
    private int orderType = 1;
    // 20190905-新增押金产品的支持-end

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getBuyerMemo() {
        return buyerMemo;
    }

    public void setBuyerMemo(String buyerMemo) {
        this.buyerMemo = buyerMemo;
    }

    public List<OrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderItem> itemList) {
        this.itemList = itemList;
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

    public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
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

	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}

	public void setCountryNo(int countryNo) {
		this.countryNo = countryNo;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setReseveDtfrom(String reseveDtfrom) {
		this.reseveDtfrom = reseveDtfrom;
	}

	public void setReseveDtto(String reseveDtto) {
		this.reseveDtto = reseveDtto;
	}

	public void setReseveTime(int reseveTime) {
		this.reseveTime = reseveTime;
	}

	public void setConfirmDtfrom(String confirmDtfrom) {
		this.confirmDtfrom = confirmDtfrom;
	}

	public void setConfirmDtto(String confirmDtto) {
		this.confirmDtto = confirmDtto;
	}

	public void setConfirmTime(int confirmTime) {
		this.confirmTime = confirmTime;
	}

	public void setNumPersons(int numPersons) {
		this.numPersons = numPersons;
	}

	public void setShippingType(int shippingType) {
		this.shippingType = shippingType;
	}

	public void setShippingAddr(Long shippingAddr) {
		this.shippingAddr = shippingAddr;
	}

	public void setShippingDt(String shippingDt) {
		this.shippingDt = shippingDt;
	}

	public void setShippingTime(int shippingTime) {
		this.shippingTime = shippingTime;
	}

	public void setShippingCmt(String shippingCmt) {
		this.shippingCmt = shippingCmt;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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
            String cmt) {
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
                .build();
    }

    /**
     * 20190905-新增押金产品的支持
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
                .build();
    }
}