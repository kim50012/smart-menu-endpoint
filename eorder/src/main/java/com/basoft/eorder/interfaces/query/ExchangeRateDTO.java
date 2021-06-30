package com.basoft.eorder.interfaces.query;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;
import java.util.Objects;

public class ExchangeRateDTO {
    private int rateId;
    private String startDt;
    private String endDt;
    private BigDecimal usdcnyRate;
    private BigDecimal usdkrwRate;
    private BigDecimal cnykrwRate;
    private BigDecimal krwcnyRate;
    private BigDecimal krwusdRate;
    private String createDts;
    private String updateDts;


    public int getRateId() {
        return rateId;
    }

    public void setRateId(int rateId) {
        this.rateId = rateId;
    }

    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public BigDecimal getUsdcnyRate() {
        return usdcnyRate;
    }

    public void setUsdcnyRate(BigDecimal usdcnyRate) {
        this.usdcnyRate = usdcnyRate;
    }

    public BigDecimal getUsdkrwRate() {
        return usdkrwRate;
    }

    public void setUsdkrwRate(BigDecimal usdkrwRate) {
        this.usdkrwRate = usdkrwRate;
    }

    public BigDecimal getCnykrwRate() {
        return cnykrwRate;
    }

    public void setCnykrwRate(BigDecimal cnykrwRate) {
        this.cnykrwRate = cnykrwRate;
    }

    public BigDecimal getKrwcnyRate() {
		return krwcnyRate;
	}

    public BigDecimal getKrwusdRate() {
		return krwusdRate;
	}

	public void setKrwcnyRate(BigDecimal krwcnyRate) {
		this.krwcnyRate = krwcnyRate;
	}

	public void setKrwusdRate(BigDecimal krwusdRate) {
		this.krwusdRate = krwusdRate;
	}

	public String getCreateDts() {
        return createDts;
    }

    public void setCreateDts(String createDts) {
        this.createDts = createDts;
    }

    public String getUpdateDts() {
        return  updateDts;
    }

    public void setUpdateDts(String updateDts) {
        this.updateDts = updateDts;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
