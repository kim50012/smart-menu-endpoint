package com.basoft.service.param.menu;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:50 2018/4/20
 **/
@Data
public class MenuAuthForm {

    private Long shopId;

    //private List<ShopMenuAuth> shopMenuAuthList;

    private List<Long>  checkList;

    private Date createDate;


}
