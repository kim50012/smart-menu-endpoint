package com.basoft.service.param.wechat.shopMenu;

import com.basoft.service.param.PaginationParam;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:01 2018/4/12
 **/

@Data
public class ShopWxMenuQueryParam extends PaginationParam{

    private Long shopId;
    private Long menuParentId;

    private Long menuId;


}
