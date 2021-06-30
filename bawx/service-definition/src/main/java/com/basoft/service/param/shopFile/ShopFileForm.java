package com.basoft.service.param.shopFile;

import com.basoft.service.param.BaseForm;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:55 2018/4/10
 **/

@Data
public class ShopFileForm extends BaseForm{

    private String fileNm;

    private Byte fileType;

    private Integer fileSize;

    private String fullUrl;

    private Byte isUse;

}
