package com.basoft.api.vo.menu;

import com.basoft.service.dto.menu.MenuAuthDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:57 2018/4/20
 **/
@Data
public class MenuAuthVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long shopId;

    private String label;

    private Boolean disabled;


    private List<MenuAuthDto> children;

    public MenuAuthVo(MenuAuthDto dto){
        this.id = dto.getId();
        this.shopId = dto.getShopId();
        this.label = dto.getLabel();
        this.disabled = dto.getDisabled();
        this.children = dto.getChildren();
    }
}
