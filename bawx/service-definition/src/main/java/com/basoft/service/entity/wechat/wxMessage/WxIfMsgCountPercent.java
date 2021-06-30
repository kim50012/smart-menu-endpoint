package com.basoft.service.entity.wechat.wxMessage;

import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:21 2018/5/18
 **/
@Data
public class WxIfMsgCountPercent {

    private String sendTimes;

    private int msgUser;

    private int countInterval;

    private String percent;
}
