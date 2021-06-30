package com.basoft.eorder.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description: 店铺
 * @Date Created in 下午3:03 2018/12/4
 **/
public class ShipPoint {

    @JsonSerialize
    private Long shipPointid; //Pay ID
    private String shipPointnm; //名称 이름
    private String areaId; //地区 지역
    private String areaName; //地区 지역
    private String addr; //详细地址 상세주소
    private String addrCn; //详细地址中文 상세주소 중문
    private String lat; //纬度 위도
    private String lon; //经度 경도
    private String phoneNo; //联系电话 연락처
    private String cmt; //备注 비고
    private String cmtCn; //备注中文 비고중문
    private int status; //状态 상태 (1 use, 2 forbidden, 3 delete)

    public Long shipPointid() {
    	return shipPointid;
    }
    public String shipPointnm() {
    	return shipPointnm;
    }
    public String areaId() {
    	return areaId;
    }
    public String areaName() {
    	return areaName;
    }
    public String addr() {
    	return addr;
    }
    public String addrCn() {
    	return addrCn;
    }
    public String lat() {
    	return lat;
    }
    public String lon() {
    	return lon;
    }
    public String phoneNo() {
    	return phoneNo;
    }
    public String cmt() {
    	return cmt;
    }
    public String cmtCn() {
    	return cmtCn;
    }
    public int status() {
    	return status;
    }
    


    public static Builder newBuild(ShipPoint shipPoint) {
        return new Builder()
        		.shipPointid(shipPoint.shipPointid)
        		.shipPointnm(shipPoint.shipPointnm)
        		.areaId(shipPoint.areaId)
        		.areaName(shipPoint.areaName)
        		.addr(shipPoint.addr)
        		.addrCn(shipPoint.addrCn)
        		.lat(shipPoint.lat)
        		.lon(shipPoint.lon)
        		.phoneNo(shipPoint.phoneNo)
        		.cmt(shipPoint.cmt)
        		.cmtCn(shipPoint.cmtCn)
        		.status(shipPoint.status)
                ;

    }


    public static final class Builder{

    	private Long shipPointid; //Pay ID
    	private String shipPointnm; //名称 이름
    	private String areaId; //地区 지역
    	private String areaName; //地区 지역
    	private String addr; //详细地址 상세주소
    	private String addrCn; //详细地址中文 상세주소 중문
    	private String lat; //纬度 위도
    	private String lon; //经度 경도
    	private String phoneNo; //联系电话 연락처
    	private String cmt; //备注 비고
    	private String cmtCn; //备注中文 비고중문
    	private int status; //状态 상태 (1 use, 2 forbidden, 3 delete)

    	public Builder shipPointid(Long shipPointid) {
    		this.shipPointid = shipPointid;
    		return this;
    	}
    	public Builder shipPointnm(String shipPointnm) {
    		this.shipPointnm = shipPointnm;
    		return this;
    	}
    	public Builder areaId(String areaId) {
    		this.areaId = areaId;
    		return this;
    	}
    	public Builder areaName(String areaName) {
    		this.areaName = areaName;
    		return this;
    	}
    	public Builder addr(String addr) {
    		this.addr = addr;
    		return this;
    	}
    	public Builder addrCn(String addrCn) {
    		this.addrCn = addrCn;
    		return this;
    	}
    	public Builder lat(String lat) {
    		this.lat = lat;
    		return this;
    	}
    	public Builder lon(String lon) {
    		this.lon = lon;
    		return this;
    	}
    	public Builder phoneNo(String phoneNo) {
    		this.phoneNo = phoneNo;
    		return this;
    	}
    	public Builder cmt(String cmt) {
    		this.cmt = cmt;
    		return this;
    	}
    	public Builder cmtCn(String cmtCn) {
    		this.cmtCn = cmtCn;
    		return this;
    	}
    	public Builder status(int status) {
    		this.status = status;
    		return this;
    	}


        public ShipPoint build(){
            ShipPoint shipPoint = new ShipPoint();
            shipPoint.shipPointid = this.shipPointid;
            shipPoint.shipPointnm = this.shipPointnm;
            shipPoint.areaId = this.areaId;
            shipPoint.areaName = this.areaName;
            shipPoint.addr = this.addr;
            shipPoint.addrCn = this.addrCn;
            shipPoint.lat = this.lat;
            shipPoint.lon = this.lon;
            shipPoint.phoneNo = this.phoneNo;
            shipPoint.cmt = this.cmt;
            shipPoint.cmtCn = this.cmtCn;
            shipPoint.status = this.status;
            return shipPoint;
        }

    }

}
