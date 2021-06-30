package com.basoft.service.param.customer;

import com.basoft.service.param.PaginationParam;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:22 2018/5/9
 **/
@Data
public class CustQueryParam extends PaginationParam{

    private Long shopId;

    private String custName;
}
