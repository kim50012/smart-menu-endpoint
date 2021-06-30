package com.basoft.service.param.customer;

import com.basoft.service.param.PaginationParam;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:51 2018/4/23
 **/
@Data
public class GradeMstQueryParam extends PaginationParam{

    private Long shopId;

    private String param;
}
