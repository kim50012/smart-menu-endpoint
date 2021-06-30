package com.basoft.service.param.wechat.shopWxNews;

import com.basoft.service.param.PaginationParam;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午11:10 2018/4/16
 **/

@Data
public class ShopWxNewsQueryParam extends PaginationParam {

    private Long shopId;

    private String newsType;

    private String param;

    private String msgIdWould;
}
