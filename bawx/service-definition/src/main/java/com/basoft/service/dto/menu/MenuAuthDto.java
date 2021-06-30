package com.basoft.service.dto.menu;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:17 2018/4/20
 **/
public class MenuAuthDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Long shopId;

    private String label;
    
    private Boolean disabled;

    private List<MenuAuthDto> children;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

	public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
            this.disabled = disabled;
    }

    public List<MenuAuthDto> getChildren() {
        return children;
    }

    public void setChildren(List<MenuAuthDto> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
