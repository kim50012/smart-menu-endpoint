package com.basoft.api.vo.menu;

import com.basoft.service.dto.menu.MenuDto;
import com.basoft.service.entity.menu.MenuMst;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:12 2018/4/8
 **/
@Data
public class MenuVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;

    private String menuNm;

    private String menuNm2;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuPid;

    private String menuIcon;

    private Byte menuLevel;

    private String menuUrl;

    private Integer orderBy;

    private Byte activeSts;

    private String activeStStr;

    private List<MenuDto> children;


    public MenuVO(MenuDto dto){
        this.id = dto.getId();
        this.menuId=dto.getMenuId();
        this.menuNm = dto.getMenuNm();
        this.menuPid = dto.getMenuPid();
        this.menuIcon=dto.getMenuIcon();
        this.menuLevel=dto.getMenuLevel();
        this.menuUrl=dto.getMenuUrl();
        this.orderBy=dto.getOrderBy();
        this.activeSts=dto.getActiveSts();
        this.menuNm2=dto.getMenuNm2();
        this.activeStStr=dto.getActiveStStr();
        this.children = dto.getChildren();
    }

    public MenuVO(MenuMst mst){
        this.id = mst.getId();
        this.menuId=mst.getMenuId();
        this.menuNm = mst.getMenuNm();
        this.menuPid = mst.getMenuPid();
        this.menuIcon=mst.getMenuIcon();
        this.menuLevel=mst.getMenuLevel();
        this.menuUrl=mst.getMenuUrl();
        this.orderBy=mst.getOrderBy();
        this.activeSts=mst.getActiveSts();
    }

}
