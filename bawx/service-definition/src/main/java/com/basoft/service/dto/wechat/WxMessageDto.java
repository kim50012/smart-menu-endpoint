package com.basoft.service.dto.wechat;

import lombok.Data;

import java.util.Date;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午11:39 2018/5/8
 **/
@Data
public class WxMessageDto{
    //昵称
    private String custNickNm;

    //头像
    private String wxIfHeadimgurl;

    //客户发送消息时间
    private Date custReceiveDt;

    //客户发送内容
    private String custContent;

    //回复内容
    private String shopContent;

    //回复时间
    private Date shopReceiveDt;
}
