package com.basoft.eorder.interfaces.query.agent;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 3:15 下午 2019/9/24
 **/
public class AgentAimMapDTO {
    //columns START
    private Long id;  //id

    private Long agtId;  //代理商id

    private Long sid;

    private String agtCode;  //代理商编码

    private String agtName;  //代理商名称

    private Long storeId;  //店铺id

    private String storeName;  //店铺名称

    private String storeChargeRate;//店铺结算百分比

    private BigDecimal sumAmount;//累计交易额

    private String openId;  //微信用户id

    private Integer status;  //状态：1-正常 2禁用 3 删除

    private Integer agtType;  //代理商类型 1-SA  2-CA

    private BigDecimal agtFee;  //手续费百分比

    private int qty;

    private Double agtRate;  //手续费百分比

    private String agtPercent;  //手续费百分比

    private Date contractSt;  //合同起始时间

    private Date contractEd;  //合同结束时间

    private Date createTime;  //创建时间

    private Date updateTime;  //updateTime

    //private List<AgentAimMap> aimMapList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgtId() {
        return agtId;
    }

    public void setAgtId(Long agtId) {
        this.agtId = agtId;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getAgtCode() {
        return agtCode;
    }

    public void setAgtCode(String agtCode) {
        this.agtCode = agtCode;
    }

    public String getAgtName() {
        return agtName;
    }

    public void setAgtName(String agtName) {
        this.agtName = agtName;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAgtType() {
        return agtType;
    }

    public void setAgtType(Integer agtType) {
        this.agtType = agtType;
    }

    public BigDecimal getAgtFee() {
        return agtFee;
    }

    public void setAgtFee(BigDecimal agtFee) {
        this.agtFee = agtFee;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
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

    public Date getContractSt() {
        return contractSt;
    }

    public void setContractSt(Date contractSt) {
        this.contractSt = contractSt;
    }

    public Date getContractEd() {
        return contractEd;
    }

    public void setContractEd(Date contractEd) {
        this.contractEd = contractEd;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
