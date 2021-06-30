package com.basoft.api.vo.customer;

import com.basoft.service.dto.customer.WxUserProvinceAllDto;
import com.basoft.service.dto.customer.WxUserTotalCityDto;
import com.basoft.service.dto.customer.WxUserTotalProvinDto;
import lombok.Data;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:21 2018/5/15
 **/
@Data
public class ProvinCityTotalVo {

    List<WxUserTotalProvinDto> provinDtoList;

    List<WxUserTotalCityDto> cityDtoList;

    List<WxUserProvinceAllDto> allProList;




    public ProvinCityTotalVo(List<WxUserTotalProvinDto> provinDtoList, List<WxUserTotalCityDto> cityDtoList,List<WxUserProvinceAllDto> allProList ){
        this.provinDtoList = provinDtoList;
        this.cityDtoList = cityDtoList;
        this.allProList = allProList;
    }
}
