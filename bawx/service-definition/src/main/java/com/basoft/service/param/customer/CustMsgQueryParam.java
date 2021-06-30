package com.basoft.service.param.customer;

import com.basoft.service.param.PaginationParam;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:41 2018/5/7
 **/
@Data
public class CustMsgQueryParam extends PaginationParam{
    private Long shopId;
    private String userId;
}
