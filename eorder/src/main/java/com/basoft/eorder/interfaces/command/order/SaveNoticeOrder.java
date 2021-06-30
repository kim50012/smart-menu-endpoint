package com.basoft.eorder.interfaces.command.order;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.interfaces.query.OrderDTO;

/**
 * Order notice domain.
 * Implements Command interface.
 *
 * @author Mentor
 * @version 1.0
 * @since 20190531
 */
@SuppressWarnings("unused")
public class SaveNoticeOrder implements Command {
    public static final String COMMAND_FINISHED_ORDER_NOTICE = "finishedOrderNotice";
    // 用户的openid
    private String uid;

    // 业务类型:做饭完成通知 - 1,网购商店备货完成通知 - 2,医美预约成功通知  - 3,酒店预约成功通知 - 4
    private String bizType;

    // 通知操作渠道:manager cms端:manager_cms/mc;manager app端:manager_app/ma
    private String channel;

    // 订单消息
    private OrderDTO orderInfo;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public OrderDTO getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderDTO orderInfo) {
        this.orderInfo = orderInfo;
    }
}