package com.basoft.service.entity.customer.custShop;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class CustShop extends CustShopKey {
    private Date createdDt;

    @JsonSerialize(using = ToStringSerializer.class )
    private Long gradeId;

    private Integer custPoint;

    private Date followDt;

    private Date followDtCan;

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getCustPoint() {
        return custPoint;
    }

    public void setCustPoint(Integer custPoint) {
        this.custPoint = custPoint;
    }

    public Date getFollowDt() {
        return followDt;
    }

    public void setFollowDt(Date followDt) {
        this.followDt = followDt;
    }

    public Date getFollowDtCan() {
        return followDtCan;
    }

    public void setFollowDtCan(Date followDtCan) {
        this.followDtCan = followDtCan;
    }
}