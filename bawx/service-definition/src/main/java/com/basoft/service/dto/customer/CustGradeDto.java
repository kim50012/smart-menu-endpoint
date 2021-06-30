package com.basoft.service.dto.customer;

import com.basoft.service.entity.customer.cust.Cust;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:27 2018/4/25
 **/
@Data
public class CustGradeDto extends Cust{

    private Integer baseQty;//等级

    private Long gradeId;//等级

    private String gradeNm;

}
