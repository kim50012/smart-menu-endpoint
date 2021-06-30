package com.basoft.service.param.customer;

import com.basoft.service.param.PaginationParam;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:04 2018/4/27
 **/
@Data
public class WxUserQueryParam extends PaginationParam{

    private Long shopId;

    private String startTime;//开始时间

    private String endTime;//结束时间

    private String followType;
}
