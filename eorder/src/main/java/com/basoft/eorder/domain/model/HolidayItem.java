package com.basoft.eorder.domain.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.basoft.eorder.domain.model.OrderItem.Builder;

import java.math.BigDecimal;

public class HolidayItem {
    private String dt;
    private String cmt;
    private Long storeId;
    private int workingDay;

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

	public static final class Builder {
        private String dt;
        private String cmt;
        private int workingDay;
        private Long storeId;

        public Builder cmt(String cmt) {
            this.cmt = cmt;
            return this;
        }

        public Builder dt(String dt) {
            this.dt = dt;
            return this;
        }

        public Builder storeId(Long storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder workingDay(int workingDay) {
            this.workingDay = workingDay;
            return this;
        }

        public HolidayItem build() {
        	HolidayItem oi = new HolidayItem();
            oi.cmt = this.cmt;
            oi.dt = this.dt;
            oi.storeId = this.storeId;
            oi.workingDay = this.workingDay;
            return oi;
        }
    }
    
    public Builder createNewHolidayItem(Long storeId, String cmt, HolidayItem oi) {
        return new Builder()
                .storeId(storeId)
                .cmt(cmt)
                .workingDay(oi.workingDay)
                .dt(oi.dt);
    }
	@Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
