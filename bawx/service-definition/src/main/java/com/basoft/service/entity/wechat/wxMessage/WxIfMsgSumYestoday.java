package com.basoft.service.entity.wechat.wxMessage;

import lombok.Data;

/**
 * @Author:DongXifu
 * @Description: 昨日微信客户发送消息总数统计
 * @Date Created in 下午5:22 2018/5/18
 **/
@Data
public class WxIfMsgSumYestoday {

    private int msgUser;

    private int msgCount;

    private int average;

}
