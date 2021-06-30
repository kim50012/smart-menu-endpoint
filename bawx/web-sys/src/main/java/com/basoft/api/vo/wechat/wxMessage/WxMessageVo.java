package com.basoft.api.vo.wechat.wxMessage;

import com.basoft.service.dto.wechat.WxMessageDto;
import lombok.Data;

import java.util.Date;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:34 2018/5/8
 **/
@Data
public class WxMessageVo {


    private String custNickNm;

    private String wxIfHeadimgurl;

    //客户发送消息时间
    private Date custReceiveDt;

    //客户发送内容
    private String custContent;

    //回复内容
    private String shopContent;

    //回复时间
    private Date shopReceiveDt;

    public WxMessageVo(WxMessageDto dto){
        this.custNickNm = dto.getCustNickNm();
        this.wxIfHeadimgurl = dto.getWxIfHeadimgurl();
        this.custReceiveDt = dto.getCustReceiveDt();
        this.custContent = dto.getCustContent();
        this.shopContent = dto.getShopContent();
        this.shopReceiveDt = dto.getShopReceiveDt();
    }

}
