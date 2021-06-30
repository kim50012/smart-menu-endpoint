package com.basoft.service.dto.customer;

import com.basoft.service.entity.customer.grade.GradeMst;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:48 2018/4/23
 **/

@Data
public class GradeMstDto extends GradeMst{
    private String isUseStr;

}
