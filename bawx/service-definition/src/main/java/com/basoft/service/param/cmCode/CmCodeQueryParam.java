package com.basoft.service.param.cmCode;

import com.basoft.service.param.PaginationParam;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:50 2018/4/11
 **/

@Data
public class CmCodeQueryParam extends PaginationParam{

    private String fdNm;

    private String cdVal;

    private String param;

    private String cdDesc;//字典描述

    private String isDelete;//是否删除
}
