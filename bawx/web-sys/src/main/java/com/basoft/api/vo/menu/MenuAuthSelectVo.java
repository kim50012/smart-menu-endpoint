package com.basoft.api.vo.menu;

import lombok.Data;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:59 2018/4/23
 **/
@Data
public class MenuAuthSelectVo {
    private List<MenuAuthVo> menuAuthVoList;

    private List<String>  checkList;

    public MenuAuthSelectVo(List<MenuAuthVo> voList,List<String> list){
        this.menuAuthVoList = voList;
        this.checkList = list;
    }
}
