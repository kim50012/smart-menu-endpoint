package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.ShipPoint;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.User;

import java.math.BigDecimal;

public class UpdateShipPoint implements Command {
    public static final String NAME = "updateShipPoint";

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

	public Long getShipPointid() {
		return shipPointid;
	}

	public void setShipPointid(Long shipPointid) {
		this.shipPointid = shipPointid;
	}

	public String getShipPointnm() {
		return shipPointnm;
	}

	public void setShipPointnm(String shipPointnm) {
		this.shipPointnm = shipPointnm;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAddrCn() {
		return addrCn;
	}

	public void setAddrCn(String addrCn) {
		this.addrCn = addrCn;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getCmt() {
		return cmt;
	}

	public void setCmt(String cmt) {
		this.cmt = cmt;
	}

	public String getCmtCn() {
		return cmtCn;
	}

	public void setCmtCn(String cmtCn) {
		this.cmtCn = cmtCn;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
    public ShipPoint build(ShipPoint shipPoint) {

        return ShipPoint.newBuild(shipPoint)
        		.shipPointnm(this.shipPointnm)
        		.areaId(this.areaId)
        		.addr(this.addr)
        		.addrCn(this.addrCn)
        		.lat(this.lat)
        		.lon(this.lon)
        		.phoneNo(this.phoneNo)
        		.cmt(this.cmt)
        		.cmtCn(this.cmtCn)
        		.status(this.status)
                .build();
    }

}
