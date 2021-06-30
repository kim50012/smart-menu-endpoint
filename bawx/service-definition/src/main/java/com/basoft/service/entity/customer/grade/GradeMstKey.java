package com.basoft.service.entity.customer.grade;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class GradeMstKey {
	@JsonSerialize(using = ToStringSerializer.class )
    private Long shopId;

	@JsonSerialize(using = ToStringSerializer.class )
    private Long gradeId;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }
}