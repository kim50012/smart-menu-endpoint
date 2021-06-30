package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.interfaces.query.agent.AgentAimMapDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 7:00 下午 2019/9/18
 **/
public class AgentStoreDTO {

    private Long id;

    private String isBind;

    private Long storeId;

    private Long sid;

    private String storeName;

    private String storeType;

    private String chargeType;

    private String city;

    private String areaNm;

    private Long agtId;

    private BigDecimal agtFee;

    private BigDecimal orderAmount;

    private BigDecimal settleSum;

    private BigDecimal vatFee;

    private Double agtRate;  //手续费百分比  db_column: CHARGE_RATE

    private String agtPercent;  //手续费百分比  db_column: CHARGE_RATE

    private String contractSt;  //合同起始时间  db_column: CONTRACT_ST

    private String contractEd;  //合同结束时间  db_column: CONTRACT_ED

    private String isValid;//是否有效

    private String storeChargeRate;//店铺结算百分比

    private BigDecimal sumAmount;//累计交易额

    private BigDecimal qty;//累积交易件数

    private String laterTrainDate;  //最近交易日期

    private String laterStore;  //最近交易店铺

    private String agtName;

    private String agtCode;

    private int agtType;

    private String agtBankCode;  //银行账号

    private String saAgtName;//sa代理商名称

    private String caAgtName;//ca代理商名称

    private List<AgentAimMapDTO> aimMapList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsBind() {
        return isBind;
    }

    public void setIsBind(String isBind) {
        this.isBind = isBind;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAreaNm() {
        return areaNm;
    }

    public void setAreaNm(String areaNm) {
        this.areaNm = areaNm;
    }

    public BigDecimal getAgtFee() {
        return agtFee;
    }

    public void setAgtFee(BigDecimal agtFee) {
        this.agtFee = agtFee;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getSettleSum() {
        return settleSum;
    }

    public void setSettleSum(BigDecimal settleSum) {
        this.settleSum = settleSum;
    }

    public BigDecimal getVatFee() {
        return vatFee;
    }

    public void setVatFee(BigDecimal vatFee) {
        this.vatFee = vatFee;
    }

    public Double getAgtRate() {
        return agtRate;
    }

    public void setAgtRate(Double agtRate) {
        this.agtRate = agtRate;
    }

    public String getAgtPercent() {
        return agtPercent;
    }

    public void setAgtPercent(String agtPercent) {
        this.agtPercent = agtPercent;
    }

    public String getContractSt() {
        return contractSt;
    }

    public void setContractSt(String contractSt) {
        this.contractSt = contractSt;
    }

    public String getContractEd() {
        return contractEd;
    }

    public void setContractEd(String contractEd) {
        this.contractEd = contractEd;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getStoreChargeRate() {
        return storeChargeRate;
    }

    public void setStoreChargeRate(String storeChargeRate) {
        this.storeChargeRate = storeChargeRate;
    }

    public BigDecimal getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(BigDecimal sumAmount) {
        this.sumAmount = sumAmount;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getLaterTrainDate() {
        return laterTrainDate;
    }

    public void setLaterTrainDate(String laterTrainDate) {
        this.laterTrainDate = laterTrainDate;
    }

    public String getLaterStore() {
        return laterStore;
    }

    public void setLaterStore(String laterStore) {
        this.laterStore = laterStore;
    }

    public Long getAgtId() {
        return agtId;
    }

    public void setAgtId(Long agtId) {
        this.agtId = agtId;
    }

    public String getAgtName() {
        return agtName;
    }

    public void setAgtName(String agtName) {
        this.agtName = agtName;
    }

    public String getAgtCode() {
        return agtCode;
    }

    public void setAgtCode(String agtCode) {
        this.agtCode = agtCode;
    }

    public int getAgtType() {
        return agtType;
    }

    public void setAgtType(int agtType) {
        this.agtType = agtType;
    }

    public String getAgtBankCode() {
        return agtBankCode;
    }

    public void setAgtBankCode(String agtBankCode) {
        this.agtBankCode = agtBankCode;
    }

    public String getSaAgtName() {
        return saAgtName;
    }

    public void setSaAgtName(String saAgtName) {
        this.saAgtName = saAgtName;
    }

    public String getCaAgtName() {
        return caAgtName;
    }

    public void setCaAgtName(String caAgtName) {
        this.caAgtName = caAgtName;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public List<AgentAimMapDTO> getAimMapList() {
        return aimMapList;
    }

    public void setAimMapList(List<AgentAimMapDTO> aimMapList) {
        this.aimMapList = aimMapList;
    }
}
