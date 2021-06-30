package com.basoft.api.vo.customer;

import com.basoft.service.dto.customer.CustDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:28 2018/5/9
 **/
@Data
public class CustVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long custSysId;

    private String custLoginId;

    private String wxIfOpenidP;

    private String email;

    private String mobileNo;

    private Byte wxIfIsSubscribe;

    private String wxIfNickNm;

    private Byte wxIfSexId;

    private String wxIfSexIdStr;

    public CustVo(CustDto dto){
        this.custSysId = dto.getCustSysId();
        this.custLoginId = dto.getCustLoginId();
        this.wxIfOpenidP = dto.getWxIfOpenidP();
        this.email = dto.getEmail();
        this.mobileNo = dto.getMobileNo();
        this.wxIfIsSubscribe = dto.getWxIfIsSubscribe();
        this.wxIfNickNm = dto.getWxIfNickNm();
        this.wxIfSexId = dto.getWxIfSexId();
        this.wxIfSexIdStr = dto.getWxIfSexIdStr();
    }
}
