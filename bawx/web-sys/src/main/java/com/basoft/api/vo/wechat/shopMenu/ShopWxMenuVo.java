package com.basoft.api.vo.wechat.shopMenu;

import com.basoft.service.definition.wechat.BizConstants;
import com.basoft.service.dto.wechat.shopMenu.ShopWxMenuDto;
import com.basoft.service.entity.wechat.shopMenu.ShopWxMenu;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:26 2018/4/12
 **/

@Data
public class ShopWxMenuVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;

    @JsonSerialize(using = ToStringSerializer.class)
    private String menuOpWxId;

    private String menuNm;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuParentId;

    private String menuOpType;

    private String menuOpTypeStr;

    private Integer menuMsgType;

    private String menuMsgTypeStr;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuOpId;

    private String menuOpTitle;

    private Byte menuSts;

    private String menuStsStr;

    private Byte sortNo;

    private String menuOpCom;

    private List<ShopWxMenuDto> secondShopMenu;//二级微信菜单


    public ShopWxMenuVo(ShopWxMenuDto dto){
        this.menuId = dto.getMenuId();
        this.menuNm = dto.getMenuNm();
        this.menuParentId = dto.getMenuParentId();
        this.menuOpType = dto.getMenuOpType();
        if(BizConstants.MENUOPTYPE_CLICK.equals(dto.getMenuOpType())){
            this.menuOpTypeStr = "发送消息";
        }else if(BizConstants.MENUOPTYPE_VIEW.equals(dto.getMenuOpType())){
            this.menuOpTypeStr = "跳转网页";
        }
        this.menuMsgType = dto.getMenuMsgType();
        this.menuMsgTypeStr = dto.getMenuMsgTypeStr();
        this.menuOpId = dto.getMenuOpId();
        this.menuOpTitle = dto.getMenuOpTitle();
        this.menuSts = dto.getMenuSts();
        this.menuStsStr = dto.getMenuStsStr();
        this.sortNo = dto.getSortNo();
        this.menuOpCom = dto.getMenuOpCom();
        this.secondShopMenu = dto.getSecondShopMenu();
        this.menuOpWxId = dto.getMenuOpWxId();
    }

    public ShopWxMenuVo(ShopWxMenu entity){
        this.menuId = entity.getMenuId();
        this.menuNm = entity.getMenuNm();
        this.menuParentId = entity.getMenuParentId();
        this.menuOpType = entity.getMenuOpType();
        this.menuOpId = entity.getMenuOpId();
        this.menuOpTitle = entity.getMenuOpTitle();
        this.menuSts = entity.getMenuSts();
        if(entity.getMenuSts()==BizConstants.MENU_STATE_ENABLE){
            this.menuStsStr = BizConstants.IS_DELETE_START;
        }else{
            this.menuStsStr = BizConstants.IS_DELETE_FORBID;
        }
        this.sortNo = entity.getSortNo();
        this.menuOpWxId = entity.getMenuOpWxId();
    }
}
