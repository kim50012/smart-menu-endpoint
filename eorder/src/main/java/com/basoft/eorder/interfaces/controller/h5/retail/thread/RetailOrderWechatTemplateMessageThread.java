package com.basoft.eorder.interfaces.controller.h5.retail.thread;

import com.basoft.eorder.domain.OrderRepository;
import com.basoft.eorder.interfaces.command.order.common.WechatTemplateMessageUtil;
import com.basoft.eorder.interfaces.command.retail.SaveRetailOrderEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 零售业务订单模板消息发送线程
 *
 * @author Mentor
 * @version 1.0
 * @since 20200528
 */
@Slf4j
public class RetailOrderWechatTemplateMessageThread implements Runnable {
    private OrderRepository orderRepository;

    private WechatTemplateMessageUtil wechatTemplateMessageUtil;

    private SaveRetailOrderEvent saveRetailOrderEvent;

    private int eventType;

    public RetailOrderWechatTemplateMessageThread(OrderRepository orderRepository
            , WechatTemplateMessageUtil wechatTemplateMessageUtil
            , SaveRetailOrderEvent saveRetailOrderEvent
            , int eventType) {
        this.orderRepository = orderRepository;
        this.wechatTemplateMessageUtil = wechatTemplateMessageUtil;
        this.saveRetailOrderEvent = saveRetailOrderEvent;
        this.eventType = eventType;
    }

    /**
     * 根据订单事件不同发送不同的模板消息
     */
    @Override
    public void run() {
        // Order order = orderRepository.getOrder(saveRetailOrderEvent.getOrderId());
        wechatTemplateMessageUtil.sendRetailOrderTemplateMsgFromEvent(
                orderRepository.getOrder(saveRetailOrderEvent.getOrderId())
                , eventType);
    }
}