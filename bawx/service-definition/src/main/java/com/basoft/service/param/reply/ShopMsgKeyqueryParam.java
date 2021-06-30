package com.basoft.service.param.reply;

import com.basoft.service.param.PaginationParam;
import lombok.Data;

/**
 * 自动回复查询param
 * @author Dxf
 */

@Data
public class ShopMsgKeyqueryParam extends PaginationParam{

    private Long shopId;
    //关键字
    private String keyWord;

    private String sendFileType;
}
