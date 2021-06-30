package com.basoft.service.param.wechat.wxMessage;

import com.basoft.service.param.PaginationParam;

import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:11 2018/5/8
 **/
@Data
public class WxMessageQueryParam extends PaginationParam{
    private Long shopId;

    private Byte timeType;

    private Byte keyType;

    private String param;

    private String startTime;//开始时间

    private String endTime;//结束时间
}
