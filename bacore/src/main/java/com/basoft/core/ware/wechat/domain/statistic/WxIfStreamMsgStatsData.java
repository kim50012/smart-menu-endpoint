package com.basoft.core.ware.wechat.domain.statistic;

public class WxIfStreamMsgStatsData{

	private Long id;

	private Long shopId;
	 
    private String ref_date;

    private String ref_hour;

    private Byte msg_type;

    private Integer msg_user;

    private Integer msg_count;

    private Byte count_interval;
    
    private Byte time_Type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getRef_date() {
		return ref_date;
	}

	public void setRef_date(String ref_date) {
		this.ref_date = ref_date;
	}

	public String getRef_hour() {
		return ref_hour;
	}

	public void setRef_hour(String ref_hour) {
		this.ref_hour = ref_hour;
	}

	public Byte getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(Byte msg_type) {
		this.msg_type = msg_type;
	}

	public Integer getMsg_user() {
		return msg_user;
	}

	public void setMsg_user(Integer msg_user) {
		this.msg_user = msg_user;
	}

	public Integer getMsg_count() {
		return msg_count;
	}

	public void setMsg_count(Integer msg_count) {
		this.msg_count = msg_count;
	}

	

	public Byte getCount_interval() {
		return count_interval;
	}

	public void setCount_interval(Byte count_interval) {
		this.count_interval = count_interval;
	}

	public Byte getTime_Type() {
		return time_Type;
	}

	public void setTime_Type(Byte time_Type) {
		this.time_Type = time_Type;
	}
	
	

	
}