package com.basoft.eorder.interfaces.query;

import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 9:32 AM 11/29/19
 **/
@Data
public class StoreExtendDTO {
    private Long exId;

    private Long storeId;

    private String fdGroupNm;

    private String fdGroupId;

    private String fdName;

    private String fdId;

    private String fdValName;

    private String fdValCode;

    private Integer status;

}
