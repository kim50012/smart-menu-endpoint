package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.domain.model.StrReviewAttach;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HolidayDTO {

    private String holiday;
    private String dt;
    private Long storeId;

    public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getHoliday() {
		return holiday;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	@Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
