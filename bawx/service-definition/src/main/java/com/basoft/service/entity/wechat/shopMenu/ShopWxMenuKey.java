package com.basoft.service.entity.wechat.shopMenu;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class ShopWxMenuKey {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long shopId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}