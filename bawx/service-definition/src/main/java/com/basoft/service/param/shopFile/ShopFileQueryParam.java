package com.basoft.service.param.shopFile;

import com.basoft.service.param.PaginationParam;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:07 2018/4/10
 **/

@Data
public class ShopFileQueryParam extends PaginationParam{

    private Byte fileType;

    private Byte isUse;

    private Long shopId;

    private String fileName;

    private String msgIdWould;
}
