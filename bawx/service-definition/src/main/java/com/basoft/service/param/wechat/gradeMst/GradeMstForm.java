package com.basoft.service.param.wechat.gradeMst;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午11:17 2018/5/3
 **/

@Data
public class GradeMstForm {

    @NotBlank(message = "等级名称不能为空")
    private String gradeNm;

    private Integer baseQty;

    private BigDecimal basePrice;
}
