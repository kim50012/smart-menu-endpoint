package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.domain.excel.ExcelColumn;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;
import java.util.List;

public class OrderDTO {
    @ExcelColumn(valueKor = "orderCode", valueChn = "订单号", col = 1)
    private Long id;

    @ExcelColumn(valueKor = "storeId", valueChn = "店铺id", col = 2)
    private Long storeId;

    @ExcelColumn(valueKor = "storeNm", valueChn = "店铺名称", col = 3)
    private String storeNm;

    private String managerId;

    private String storeType;

    @ExcelColumn(valueKor = "areaNm", valueChn = "地区", col = 4)
    private String areaNm;

    @ExcelColumn(valueKor = "tableId", valueChn = "餐桌id", col = 5)
    private Long tableId;

    private String openId;

    private String retailType;
    
    private BigDecimal amount;

    private BigDecimal paymentAmount;

    private BigDecimal discountAmount;

    private BigDecimal payAmtCny;

    private BigDecimal payAmtRmb;

    private BigDecimal payAmtUsd;

    private BigDecimal krwUsdRate;

    private BigDecimal usdCnyRate;

    private int status;

    private String buyerMemo;

    private String customerId;

    private String created;

    private String payDts;

    private String updated;

    private String logoUrl;

    private int tableNum;

    private int tableTag;

    private int qty;

    private Long revId;

    private String payDt;

    private String cancelDt;

    private List<OrderItemDTO> itemList;

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

    private String shippingAddrNm; //配送地址 배송지

    private String shippingDt; //配送日期 배송일자

    private int shippingTime; //配送上午下午 배송오전 오후

    private String shippingCmt; //配送备注 배송비고

    // 配送方式
    private Long shippingMode;

    private String shippingModeNameChn;

    private String shippingModeNameKor;

    private String shippingModeNameEng;

    private String shippingAddrDetail;

    private String shippingAddrCountry;

    private BigDecimal shippingWeight;

    private BigDecimal shippingCost;

    private String shippingCostRule;

    private String cmt; //备注 비고

    private String diningPlace;// 就餐地点 1-堂食 2-外带 3-配送

    private String diningTime;// 就餐时间 0-现在取餐 1-10分钟后到店 2-20分钟后到店 3-30分钟后到店

    private String reseveConfirmtime;

    private String custNo;

    private String custNmEn;

    private String nmLast;

    private String nmFirst;

    private String nmLastEn;

    private String nmFirstEn;

    // 状态8的来源
    private int status8From;

    // 订单变更状态
    private int changeStatus;

    //订单类型 1-正常商品订单 2-押金商品订单
    private int orderType;


    public List<OrderItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderItemDTO> itemList) {
        this.itemList = itemList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getPayAmtCny() {
        return payAmtCny;
    }

    public void setPayAmtCny(BigDecimal payAmtCny) {
        this.payAmtCny = payAmtCny;
    }

    public BigDecimal getPayAmtRmb() {
        return payAmtRmb;
    }

    public void setPayAmtRmb(BigDecimal payAmtRmb) {
        this.payAmtRmb = payAmtRmb;
    }

    public BigDecimal getPayAmtUsd() {
        return payAmtUsd;
    }

    public void setPayAmtUsd(BigDecimal payAmtUsd) {
        this.payAmtUsd = payAmtUsd;
    }

    public BigDecimal getKrwUsdRate() {
        return krwUsdRate;
    }

    public void setKrwUsdRate(BigDecimal krwUsdRate) {
        this.krwUsdRate = krwUsdRate;
    }

    public BigDecimal getUsdCnyRate() {
        return usdCnyRate;
    }

    public void setUsdCnyRate(BigDecimal usdCnyRate) {
        this.usdCnyRate = usdCnyRate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBuyerMemo() {
        return buyerMemo;
    }

    public void setBuyerMemo(String buyerMemo) {
        this.buyerMemo = buyerMemo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getPayDts() {
        return payDts;
    }

    public void setPayDts(String payDts) {
        this.payDts = payDts;
    }

    public String getStoreNm() {
        return storeNm;
    }

    public void setStoreNm(String storeNm) {
        this.storeNm = storeNm;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getAreaNm() {
        return areaNm;
    }

    public void setAreaNm(String areaNm) {
        this.areaNm = areaNm;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    public int getTableTag() {
        return tableTag;
    }

    public void setTableTag(int tableTag) {
        this.tableTag = tableTag;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Long getRevId() {
        return revId;
    }

    public void setRevId(Long revId) {
        this.revId = revId;
    }

    public String getPayDt() {
        return payDt;
    }

    public void setPayDt(String payDt) {
        this.payDt = payDt;
    }

    public String getCancelDt() {
        return cancelDt;
    }

    public void setCancelDt(String cancelDt) {
        this.cancelDt = cancelDt;
    }

    public String getCustNm() {
        return custNm;
    }

    public void setCustNm(String custNm) {
        this.custNm = custNm;
    }

    public int getCountryNo() {
        return countryNo;
    }

    public void setCountryNo(int countryNo) {
        this.countryNo = countryNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReseveDtfrom() {
        return reseveDtfrom;
    }

    public void setReseveDtfrom(String reseveDtfrom) {
        this.reseveDtfrom = reseveDtfrom;
    }

    public String getReseveDtto() {
        return reseveDtto;
    }

    public void setReseveDtto(String reseveDtto) {
        this.reseveDtto = reseveDtto;
    }

    public int getReseveTime() {
        return reseveTime;
    }

    public void setReseveTime(int reseveTime) {
        this.reseveTime = reseveTime;
    }

    public String getConfirmDtfrom() {
        return confirmDtfrom;
    }

    public void setConfirmDtfrom(String confirmDtfrom) {
        this.confirmDtfrom = confirmDtfrom;
    }

    public String getConfirmDtto() {
        return confirmDtto;
    }

    public void setConfirmDtto(String confirmDtto) {
        this.confirmDtto = confirmDtto;
    }

    public int getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(int confirmTime) {
        this.confirmTime = confirmTime;
    }

    public int getNumPersons() {
        return numPersons;
    }

    public void setNumPersons(int numPersons) {
        this.numPersons = numPersons;
    }

    public int getShippingType() {
        return shippingType;
    }

    public void setShippingType(int shippingType) {
        this.shippingType = shippingType;
    }

    public Long getShippingAddr() {
        return shippingAddr;
    }

    public void setShippingAddr(Long shippingAddr) {
        this.shippingAddr = shippingAddr;
    }

    public String getShippingDt() {
        return shippingDt;
    }

    public void setShippingDt(String shippingDt) {
        this.shippingDt = shippingDt;
    }

    public int getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(int shippingTime) {
        this.shippingTime = shippingTime;
    }

    public String getShippingCmt() {
        return shippingCmt;
    }

    public void setShippingCmt(String shippingCmt) {
        this.shippingCmt = shippingCmt;
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

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
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

    public String getShippingAddrNm() {
        return shippingAddrNm;
    }

    public void setShippingAddrNm(String shippingAddrNm) {
        this.shippingAddrNm = shippingAddrNm;
    }

    public String getReseveConfirmtime() {
        return reseveConfirmtime;
    }

    public void setReseveConfirmtime(String reseveConfirmtime) {
        this.reseveConfirmtime = reseveConfirmtime;
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

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getRetailType() {
        return retailType;
    }

    public void setRetailType(String retailType) {
        this.retailType = retailType;
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}