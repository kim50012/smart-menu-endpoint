package com.basoft.eorder.interfaces.command.holiday;

import java.util.ArrayList;
import java.util.List;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Holiday;
import com.basoft.eorder.domain.model.HolidayItem;
import com.basoft.eorder.domain.model.OrderItem;
import com.basoft.eorder.domain.model.Review;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:54 2019/1/24
 **/
public class SaveHoliday implements Command {
    public static final String NAME = "saveHoliday";

    private String holiday;
    private String dt;
    private Long storeId;
    private int workingDay;
    private String cmt;
    private List<HolidayItem> holidayItem = new ArrayList<>();
    
	public String getHoliday() {
		return holiday;
	}
	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}
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
	public int getWorkingDay() {
		return workingDay;
	}
	public void setWorkingDay(int workingDay) {
		this.workingDay = workingDay;
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

    public Holiday build(){

        return new Holiday.Builder()
                .holiday(holiday)
                .dt(dt)
                .storeId(storeId)
                .workingDay(workingDay)
                .cmt(cmt)
	            .holidayItem(holidayItem)
	            .build();
    }	
	
}
