package com.basoft.service.param.customer;

import com.basoft.service.param.PaginationParam;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:20 2018/4/25
 **/

@Data
public class CustGradeQueryParam extends PaginationParam{

    private Long shopId;

    private String param;

}
