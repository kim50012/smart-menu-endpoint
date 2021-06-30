package com.basoft.service.param.menu;

import com.basoft.service.entity.menu.MenuMst;
import com.basoft.service.param.PaginationParam;
import lombok.Data;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:25 2018/4/20
 **/
@Data
public class MenuAuthQueryParam extends PaginationParam{

    private int menuLevel;//菜单等级

    private Long menuId;//菜单id

    private List<MenuMst> menuList;

    private String shopId;

    private Long pid;

}
