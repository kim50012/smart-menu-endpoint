package com.basoft.eorder.interfaces.controller.h5.retail;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.framework.CommandHandleEngine;
import com.basoft.eorder.application.framework.QueryHandler;
import com.basoft.eorder.application.wx.model.WxPayRefundResult;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.OrderRepository;
import com.basoft.eorder.domain.OrderService;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.retail.order.RetailOrderEvent;
import com.basoft.eorder.domain.order.retail.RetailOrderEventRepository;
import com.basoft.eorder.domain.retail.RetailOrderRepository;
import com.basoft.eorder.interfaces.command.order.common.WechatTemplateMessageUtil;
import com.basoft.eorder.interfaces.command.retail.SaveRetailOrderEvent;
import com.basoft.eorder.interfaces.controller.CQRSAbstractController;
import com.basoft.eorder.interfaces.controller.h5.retail.thread.RetailOrderWechatTemplateMessageThread;
import com.basoft.eorder.interfaces.query.order.retail.RetailOrderQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公众号端零售商城类业务CMS API
 *
 * @version 1.0
 * @Date 2020518
 **/
@Slf4j
@Controller
@RequestMapping("/manager/api/v2/retail")
public class SpringManagerRetailController extends CQRSAbstractController {
    @Autowired
    private RetailOrderEventRepository retailOrderEventRepository;

    @Autowired
    private RetailOrderQuery retailOrderQuery;

    @Autowired
    private RetailOrderRepository retailOrderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private WechatTemplateMessageUtil wechatTemplateMessageUtil;

    @Autowired
    public SpringManagerRetailController(
            QueryHandler queryHandler,
            CommandHandleEngine handleEngine) {
        super(queryHandler, handleEngine);
    }

    /**
     * 事件：：：：购物类订单用户退款申请的审核4 和 用户退款申请的审核9
     *
     * @param saveRetailOrderEvent
     * @return
     */
    /*
        {
            "orderId": 1000000881,
            "eventType":4,
            "eventResult":1
        }
        eventType取值4或9 4-用户退款申请的审核4  9-用户退款申请的审核9
        eventResult取值1或0 1-审核通过 0-审核不通过
     */
    @RequestMapping(value = "/roa", method = RequestMethod.POST)
    @ResponseBody
    public String retailOrderEventAudit(HttpServletRequest request
            , @RequestBody SaveRetailOrderEvent saveRetailOrderEvent) {
        // 验证参数的正确性
        if (saveRetailOrderEvent.getOrderId() == null || saveRetailOrderEvent.getOrderId().longValue() == 0) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }
        int eventResult = saveRetailOrderEvent.getEventResult();
        if(eventResult != 1 && eventResult != 0){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }
        if(saveRetailOrderEvent.getEventType() != 4 && saveRetailOrderEvent.getEventType() != 9){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 存储审核用户退款的事件
        RetailOrderEvent retailOrderEvent = saveRetailOrderEvent.build();
        int i = 0;
        try{
            i = retailOrderEventRepository.saveRetailOrderEvent(retailOrderEvent);
        } catch (DuplicateKeyException e){
            log.error("存储审核用户退款的事件失败，异常信息::::{}", e.getMessage());
            e.printStackTrace();
            return "REPEAT AUDIT";
        }

        // 根据审核结果做出不同处理
        if(i > 0){
            // 审核没通过
            if(eventResult == 0){
                // 将订单的变更状态由1变为0
                retailOrderEventRepository.recoverOrderChangeStatus(retailOrderEvent);
            }
            // 审核通过
            else if(eventResult == 1){
                // 退款
                WxPayRefundResult wxPayRefundResult = orderService.retailOrderRefundFromEvent(retailOrderEvent, getRealIP(request), request);
            }
        } else {
            return "FAIL";
        }
        return "SUCCESS";
    }

    /**
     * 零售业务订单
     * 接单，修改订单状态为5并记录接单事件6
     *
     * @param request
     * @param saveRetailOrderEvent
     * @return
     */
    /*
        入参：
        {
            "orderId": 1000000881
        }
        返回值：1-正常 0-失败 异常-失败
     */
    @RequestMapping(value = "/aro", method = RequestMethod.POST)
    @ResponseBody
    public int acceptRetailOrder(HttpServletRequest request
            , @RequestBody SaveRetailOrderEvent saveRetailOrderEvent) {
        // 验证参数的正确性
        if (saveRetailOrderEvent.getOrderId() == null || saveRetailOrderEvent.getOrderId().longValue() == 0) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 接单：更新订单状态为5并记录接单事件6
        int c = retailOrderRepository.acceptRetailOrder(saveRetailOrderEvent);

        // 发送接单成功的模板消息
        log.info("【零售业务商户接单】启动发送模板消息线程阈值：：：：{}", c);
        if (c == 1) {
            log.info("【零售业务商户接单】启动发送模板消息线程......");
            try {
                new Thread(new RetailOrderWechatTemplateMessageThread(orderRepository
                        , wechatTemplateMessageUtil
                        , saveRetailOrderEvent
                        , CommonConstants.RETAIL_ORDER_EVENT_TYPE_6))
                        .start();
            } catch (Exception e) {
                log.error("【零售业务商户接单】发送商户接单模板消息线程异常，异常信息为：" + e.getMessage(), e);
                e.printStackTrace();
            }
            log.info("【零售业务商户接单】启动发送模板消息线程完毕......");
        }

        return c;
    }

    /**
     * 零售业务订单
     * 发货，修改订单状态为10并记录发货事件11
     *
     * @param request
     * @param saveRetailOrderEvent
     * @return
     */
    /*
        入参：
        {
            "orderId": 1000000881,
            "eventResultDesc":"发货信息JSON"
        }
        返回值：1-正常 0-失败 异常-失败
     */
    @RequestMapping(value = "/sro", method = RequestMethod.POST)
    @ResponseBody
    public int sendRetailOrder(HttpServletRequest request
            , @RequestBody SaveRetailOrderEvent saveRetailOrderEvent) {
        // 验证参数的正确性
        if (saveRetailOrderEvent.getOrderId() == null || saveRetailOrderEvent.getOrderId().longValue() == 0) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 发货：更新订单状态为10并记录发货事件11
        int c = retailOrderRepository.sendRetailOrder(saveRetailOrderEvent);

        // 发送发货成功的模板消息
        if (c == 1) {
            try {
                new Thread(new RetailOrderWechatTemplateMessageThread(orderRepository
                        , wechatTemplateMessageUtil
                        , saveRetailOrderEvent
                        , CommonConstants.RETAIL_ORDER_EVENT_TYPE_11))
                        .start();
            } catch (Exception e) {
                log.error("【零售业务商户接单】发送商户接单模板消息线程异常，异常信息为：" + e.getMessage(), e);
                e.printStackTrace();
            }
        }

        return c;
    }

    /**
     * 根据订单id获取订单事件列表
     *
     * @param request
     * @param oid
     * @return
     */
    @RequestMapping(value = "/roes/{oid}", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> retailOrderEventList(HttpServletRequest request
            , @PathVariable String oid) {
        // 验证参数的正确性
        if (oid == null || "".equals(oid.trim())) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 查询时间列表
        return retailOrderQuery.queryRetailOrderEventList(Long.valueOf(oid));
    }

    /**
     * 重写父类的context处理机制
     *
     * @param request
     * @return
     */
    @Override
    protected Map<String, Object> newCommandHandlerContextMap(HttpServletRequest request) {
        UserSession session = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
        Map<String, Object> props = new HashMap<>();
        props.put("realIp", getRealIP(request));
        props.put(AppConfigure.BASOFT_USER_SESSION_PROP, session);
        return props;
    }
}