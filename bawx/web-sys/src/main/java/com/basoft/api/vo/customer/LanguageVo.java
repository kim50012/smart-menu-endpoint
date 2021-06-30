package com.basoft.api.vo.customer;

import com.basoft.service.dto.customer.WxUserTotalLangDto;
import com.basoft.service.dto.customer.WxUserTotalProvinDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午11:10 2018/5/19
 **/
@Data
public class LanguageVo {

    private List<WxUserTotalProvinDto> proList;

    private WxUserTotalLangDto langDto;

    public LanguageVo( List<WxUserTotalProvinDto> list ,WxUserTotalLangDto dto){
        if(list!=null&&list.size()>0){
            this.proList = list;
        }else{
            this.proList = new ArrayList<>();
        }
        this.langDto = dto;
    }

}
