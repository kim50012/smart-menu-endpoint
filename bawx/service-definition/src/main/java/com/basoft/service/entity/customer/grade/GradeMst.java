package com.basoft.service.entity.customer.grade;

import java.math.BigDecimal;
import java.util.Date;

public class GradeMst extends GradeMstKey {

    private String gradeNm;

    private Integer baseQty;//等级

    private BigDecimal basePrice;

    private Byte isUse;

    private Date modifiedDt;

    private Date createdDt;

    private String modifiedId;

    private String createdId;

    private Integer evCycle;

    private Integer extended;

    private Integer buyPoint;

    private Integer sellForCash;

    public String getGradeNm() {
        return gradeNm;
    }

    public void setGradeNm(String gradeNm) {
        this.gradeNm = gradeNm == null ? null : gradeNm.trim();
    }

    public Integer getBaseQty() {
        return baseQty;
    }

    public void setBaseQty(Integer baseQty) {
        this.baseQty = baseQty;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Byte getIsUse() {
        return isUse;
    }

    public void setIsUse(Byte isUse) {
        this.isUse = isUse;
    }

    public Date getModifiedDt() {
        return modifiedDt;
    }

    public void setModifiedDt(Date modifiedDt) {
        this.modifiedDt = modifiedDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public String getModifiedId() {
        return modifiedId;
    }

    public void setModifiedId(String modifiedId) {
        this.modifiedId = modifiedId == null ? null : modifiedId.trim();
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId == null ? null : createdId.trim();
    }

    public Integer getEvCycle() {
        return evCycle;
    }

    public void setEvCycle(Integer evCycle) {
        this.evCycle = evCycle;
    }

    public Integer getExtended() {
        return extended;
    }

    public void setExtended(Integer extended) {
        this.extended = extended;
    }

    public Integer getBuyPoint() {
        return buyPoint;
    }

    public void setBuyPoint(Integer buyPoint) {
        this.buyPoint = buyPoint;
    }

    public Integer getSellForCash() {
        return sellForCash;
    }

    public void setSellForCash(Integer sellForCash) {
        this.sellForCash = sellForCash;
    }
}