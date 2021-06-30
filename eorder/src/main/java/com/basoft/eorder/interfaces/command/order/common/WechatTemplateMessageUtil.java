package com.basoft.eorder.interfaces.command.order.common;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.wx.api.WechatAPI;
import com.basoft.eorder.application.wx.model.DataItem;
import com.basoft.eorder.application.wx.model.TemplateMessageReturn;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.interfaces.command.order.retail.DealRetailOrder;
import com.basoft.eorder.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class WechatTemplateMessageUtil {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AppConfigure appConfigure;

    /**
     * 发送订单确认的模板消息
     *
     * @param order
     * @param store
     * @param dealRetailOrder
     * @param cancel
     */
    public void sendRetailOrderTemplateMsg(Order order, Store store, DealRetailOrder dealRetailOrder, Boolean cancel) {
        try {
            String orderId = order.getId() + "";
            String payAmtCny = order.getPayAmtCny() + "";
            String payAmtKrw = order.getAmount() + "";
            String templateId = "";

            //get TemplateMsg Config
            Map<String, Object> templateMsgConfig = (Map<String, Object>) appConfigure.getObject(AppConfigure.BASOFT_WX_TEMPLATE_CONFIG).get();

            //get token
            String token = Objects.toString(redisUtil.get("wx_token"), null);
            log.info("Wechat Access Token::::::" + token);

            String DEFAUT_COLOR = "#173177";
            Map<String, DataItem> data = new HashMap<String, DataItem>();

            // 退款模板消息发送
            if (cancel) {
                // OPENTM200833809
                templateId = (String) templateMsgConfig.get("order_cancel");

                data.put("first", new DataItem("您的订单已取消。", DEFAUT_COLOR));

                data.put("keyword1", new DataItem(orderId, DEFAUT_COLOR));

                data.put("keyword2", new DataItem(store.name(), DEFAUT_COLOR));

                int cancelReason = dealRetailOrder.getCancelReason();
                // 售后退款
                if(cancelReason == 3){
                    data.put("keyword3", new DataItem("￥ " + dealRetailOrder.getRefundAmountCny() + "(￦" + dealRetailOrder.getRefundAmountKor() + ")", DEFAUT_COLOR));
                } else {
                    data.put("keyword3", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
                }

                // 客户要求取消订单
                if (cancelReason == 1) {
                    data.put("keyword4", new DataItem("因客户要求取消订单。", DEFAUT_COLOR));
                }
                // 商户原因取消订单
                else if (cancelReason == 2) {
                    // 商户退款2
                    if (dealRetailOrder.getEventType() == 2) {
                        data.put("keyword4", new DataItem("您所订购的商品暂无库存。", DEFAUT_COLOR));
                    }
                    // 商户退款7
                    else if (dealRetailOrder.getEventType() == 7) {
                        data.put("keyword4", new DataItem("您所订购的商品暂无库存。", DEFAUT_COLOR));
                    }
                } else if(cancelReason == 3){
                    data.put("keyword4", new DataItem("订单售后退款。", DEFAUT_COLOR));
                }

                if(cancelReason == 3){
                    data.put("remark", new DataItem("退款将在48小时内退回到您的账户，如48小时后仍未收到退款，请联系客服。\n" +
                            "如果变更内容有误，请联系客服中心。\n因金额转换精度会导致退款金额存在细小误差，在此一并说明。", DEFAUT_COLOR));
                } else {
                    data.put("remark", new DataItem("退款将在48小时内退回到您的账户，如48小时后仍未收到退款，请联系客服。\n" +
                            "如果变更内容有误，请联系客服中心。", DEFAUT_COLOR));
                }

            }
            // 接单模板消息发送
            else {
                // TODO
            }

            // 发送模板消息
            TemplateMessageReturn result = WechatAPI.sendTemplateMessage(token, order.getCustomerId(), templateId, "", DEFAUT_COLOR, data);
            log.info("TemplateMessageReturn::::::" + result.toString());
        } catch (Exception e) {
            log.error("<<<<<<<<<<<<<<<<发送模板消息异常::::::>>>>>>>>>>" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 根据订单事件发送模板消息
     *
     * @param order
     * @param eventType
     */
    /*
        1、订单变化-模板消息
        模版ID     B0VFM4hncMls9DLT7uR_GgNYgQSzdme_GcqKtNCu7ig
                  开发者调用模版消息接口时需提供模版ID
        标题      订单状态提醒
        行业      餐饮 - 餐饮
        详细内容
                 {{first.DATA}}
                订单号：{{keyword1.DATA}}
                订单状态：{{keyword2.DATA}}
                时间：{{keyword3.DATA}}
                {{remark.DATA}}
        在发送时，需要将内容中的参数（{{.DATA}}内为参数）赋值替换为需要的信息

        例：
        您好，您有一个订单状态已更新
        订单号：0451
        订单状态：商家确认
        时间：2014年7月21日 18:36
        请等待商家配送。
     */
    public void sendRetailOrderTemplateMsgFromEvent(Order order, int eventType) {
        log.info("【零售业务商户接单】发送模板消息>>>>>>{}", eventType);
        try {
            //get TemplateMsg Config
            Map<String, Object> templateMsgConfig = (Map<String, Object>) appConfigure.getObject(AppConfigure.BASOFT_WX_TEMPLATE_CONFIG).get();
            String templateId = (String)templateMsgConfig.get("order_status_change");

            //get access token
            String token = Objects.toString(redisUtil.get("wx_token"), null);
            log.info("【零售业务商户接单】发送模板消息Wechat Access Token::::::{}", token);

            String DEFAUT_COLOR = "#173177";
            Map<String, DataItem> data = new HashMap<String, DataItem>();
            // 接单
            if (eventType == CommonConstants.RETAIL_ORDER_EVENT_TYPE_6) {
                    data.put("first", new DataItem("尊敬的 " + order.getCustNm() + " 顾客，感谢您购买我们的商品。", DEFAUT_COLOR));
                    data.put("keyword1", new DataItem(order.getId() + "", DEFAUT_COLOR));
                    data.put("keyword2", new DataItem("已接单", DEFAUT_COLOR));
                    data.put("keyword3", new DataItem(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"), DEFAUT_COLOR));
                    data.put("remark", new DataItem("我们将尽快为您发货，请您耐心等待。", DEFAUT_COLOR));
            }
            // 发货
            else if (eventType == CommonConstants.RETAIL_ORDER_EVENT_TYPE_11) {
                data.put("first", new DataItem("尊敬的 " + order.getCustNm() + " 顾客，您购买的商品已发货。", DEFAUT_COLOR));
                data.put("keyword1", new DataItem(order.getId() + "", DEFAUT_COLOR));
                data.put("keyword2", new DataItem("已发货", DEFAUT_COLOR));
                data.put("keyword3", new DataItem(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"), DEFAUT_COLOR));
                data.put("remark", new DataItem("您购买的宝贝已经在路上，请您耐心等待。", DEFAUT_COLOR));
            }

            // 发送模板消息
            TemplateMessageReturn result = WechatAPI.sendTemplateMessage(token, order.getCustomerId(), templateId, "", DEFAUT_COLOR, data);
            log.info("【零售业务商户接单】发送模板消息返回结果TemplateMessageReturn::::::{}", result);
        } catch (Exception e) {
            log.error("<<<<<<<<<<<<<<<<发送模板消息异常::::::>>>>>>>>>>" + e.getMessage());
            e.printStackTrace();
        }
        log.info("【零售业务商户接单】发送模板消息>>>>>>{}", eventType);
    }
}
