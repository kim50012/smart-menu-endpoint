package com.basoft.service.dto.wechat.shopMenu;

import com.basoft.service.dto.wechat.shopWxNew.ShopWxNewDto;
import com.basoft.service.entity.wechat.shopMenu.ShopWxMenu;
import lombok.Data;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:56 2018/4/12
 **/

@Data
public class ShopWxMenuDto extends ShopWxMenu {

    private List<ShopWxMenuDto> secondShopMenu;

    //用于接收前台参数
    private List<ShopWxMenu> twoShopMenuList;

    private String menuOpTypeStr;

    private String menuStsStr;

    private String menuMsgTypeStr;

    private ShopWxNewDto dto;

    List<ShopWxNewDto> ShopWxNewsItemChild;
    
    private String isLeaf;
}
