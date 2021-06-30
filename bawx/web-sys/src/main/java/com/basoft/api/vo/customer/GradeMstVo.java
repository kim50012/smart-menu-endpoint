package com.basoft.api.vo.customer;

import com.basoft.service.dto.customer.GradeMstDto;
import com.basoft.service.entity.customer.grade.GradeMst;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:18 2018/4/23
 **/
@Data
public class GradeMstVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long gradeId;

    private String gradeNm;

    private Integer baseQty;

    private BigDecimal basePrice;

    private Byte isUse;

    private String isUseStr;

    //查列表vo
    public GradeMstVo (GradeMstDto dto){
        this.gradeId = dto.getGradeId();
        this.gradeNm = dto.getGradeNm();
        this.baseQty = dto.getBaseQty();
        this.basePrice = dto.getBasePrice();
        this.isUse = dto.getIsUse();
        this.isUseStr = dto.getIsUseStr();
    }

    //回显vo
    public GradeMstVo (GradeMst gradeMst){
        this.gradeId = gradeMst.getGradeId();
        this.gradeNm = gradeMst.getGradeNm();
        this.baseQty = gradeMst.getBaseQty();
        this.basePrice = gradeMst.getBasePrice();
        this.isUse = gradeMst.getIsUse();
    }
}
