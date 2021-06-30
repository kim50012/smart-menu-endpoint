package com.basoft.eorder.domain.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.basoft.eorder.domain.model.OrderItem.Builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Holiday {
    private String holiday;
    private String dt;
    private Long storeId;
    private int workingDay;
    private String cmt;
    private List<HolidayItem> holidayItem = new ArrayList<>();
    
    public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public int getWorkingDay() {
		return workingDay;
	}

	public void setWorkingDay(int workingDay) {
		this.workingDay = workingDay;
	}

    public String getHoliday() {
		return holiday;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getCmt() {
		return cmt;
	}

	public void setCmt(String cmt) {
		this.cmt = cmt;
	}

	public List<HolidayItem> getHolidayItem() {
		return holidayItem;
	}

	public void setHolidayItem(List<HolidayItem> holidayItem) {
		this.holidayItem = holidayItem;
	}

	public static final class Builder {
	    private String holiday;
	    private String dt;
	    private Long storeId;
	    private int workingDay;
	    private String cmt;
	    private List<HolidayItem> holidayItem = new ArrayList<>();

        public Builder dt(String dt) {
            this.dt = dt;
            return this;
        }

        public Builder holiday(String holiday) {
            this.holiday = holiday;
            return this;
        }

        public Builder storeId(Long storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder cmt(String cmt) {
            this.cmt = cmt;
            return this;
        }

        public Builder workingDay(int workingDay) {
            this.workingDay = workingDay;
            return this;
        }

        public Builder holidayItem(List<HolidayItem> holidayItem) {
            this.holidayItem = holidayItem;
            return this;
        }

        public Holiday build() {
        	Holiday oi = new Holiday();
            oi.holiday = this.holiday;
            oi.dt = this.dt;
            oi.storeId = this.storeId;
            oi.workingDay = this.workingDay;
            oi.cmt = this.cmt;
            oi.holidayItem = this.holidayItem;
            return oi;
        }
    }
    
    public Builder createNewHolidayItem(Holiday oi) {
        return new Builder()
                .holiday(oi.holiday)
                .dt(oi.dt)
                .storeId(oi.storeId)
                .workingDay(oi.workingDay)
                .cmt(oi.cmt)
                .workingDay(oi.workingDay)
                .holidayItem(oi.holidayItem);
    }
	@Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
