package com.basoft.api.vo.customer;

import com.basoft.service.dto.customer.CustGradeDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:45 2018/4/25
 **/
@Data
public class CustGradeVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long custSysId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long gradeId;

    private String custNickNm;

    private String wxIfHeadimgurl;

    private Integer baseQty;//等级

    private Date wxIfSubscribeTime;

    private String gradNm;

    public CustGradeVo(CustGradeDto dto){
        this.custSysId = dto.getCustSysId();
        this.custNickNm = dto.getCustNickNm();
        this.wxIfHeadimgurl = dto.getWxIfHeadimgurl();
        this.baseQty = dto.getBaseQty();
        this.wxIfSubscribeTime = dto.getWxIfSubscribeTime();
        this.gradeId = dto.getGradeId();
        this.gradNm = dto.getGradeNm();
    }



}
