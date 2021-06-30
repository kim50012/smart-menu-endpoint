package com.basoft.service.dto.customer;

import com.basoft.service.entity.customer.cust.Cust;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:11 2018/5/9
 **/
@Data
public class CustDto extends Cust{

    private String wxIfSexIdStr;
}
