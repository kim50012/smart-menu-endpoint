package com.basoft.service.param.wechat.shopMenu;

import com.basoft.service.dto.wechat.shopMenu.ShopWxMenuDto;
import lombok.Data;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:10 2018/4/12
 **/
@Data
public class ShopWxMenuForm{
    private Long shopId;

    private List<ShopWxMenuDto> wxMenuList;

}
