package com.basoft.eorder.domain.model;

import lombok.Data;

import java.util.Date;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 4:51 PM 11/28/19
 **/
@Data
public class StoreExtend {
    private Long exId;

    private Long storeId;

    private String fdGroupNm;

    private String fdGroupId;

    private String fdName;

    private String fdId;

    private String fdValName;

    private String fdValCode;

    private Integer status;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;
}
