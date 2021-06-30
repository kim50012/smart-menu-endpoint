package com.basoft.api.vo.wechat.shopWxNews;

import com.basoft.service.dto.wechat.shopWxNew.ShopWxNewDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午11:36 2018/4/17
 **/
@Data
public class ShopWxNewsItemVo {

    private ShopWxNewDto shopWxNewHead;

    List<ShopWxNewDto> shopWxNewsChild;

    private String wxMsgId;

    private String wxMsgErr;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long wxMsgDataId;

    public ShopWxNewsItemVo(ShopWxNewDto dto){
        this.wxMsgId = dto.getWxMsgId();
        this.wxMsgErr = dto.getWxMsgErr();
        this.wxMsgDataId = dto.getWxMsgDataId();
        if(dto.getDto()!=null)
            this.shopWxNewHead = dto.getDto();
        if(dto.getShopWxNewsItemChild()!=null)
            this.shopWxNewsChild = dto.getShopWxNewsItemChild();
    }
}
