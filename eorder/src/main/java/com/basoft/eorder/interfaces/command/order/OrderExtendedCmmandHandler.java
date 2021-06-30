package com.basoft.eorder.interfaces.command.order;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.application.wx.api.WechatAPI;
import com.basoft.eorder.application.wx.model.WeixinReturn;
import com.basoft.eorder.interfaces.query.OrderDTO;
import com.basoft.eorder.interfaces.query.OrderItemDTO;
import com.basoft.eorder.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

@Slf4j
@CommandHandler.AutoCommandHandler("OrderExtendedCmmandHandler")
public class OrderExtendedCmmandHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String UTF_8 = "utf-8";

    private AppConfigure appConfigure;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    public OrderExtendedCmmandHandler(AppConfigure appConfigure) {
        this.appConfigure = appConfigure;
    }

    /**
     * 订单完成通知
     * 1.业务类型bizType
     * （1）做饭完成通知 - 1
     * （2）网购商店备货完成通知 - 2
     * （3）医美预约成功通知  - 3
     * （4）酒店预约成功通知 - 4
     * 2.调用终端channel
     * manager cms端:manager_cms/mc
     * manager app端:manager_app/ma
     *
     * @param saveNoticeOrder
     * @param context
     * @return
     */
    @CommandHandler.AutoCommandHandler(SaveNoticeOrder.COMMAND_FINISHED_ORDER_NOTICE)
    public String finishedDinnerNotice(SaveNoticeOrder saveNoticeOrder, CommandHandlerContext context) {
        logger.debug("Start Finished Order Notice -------");

        // 安全验证
        String ip = Objects.toString(context.props().get("realIp"), null);
        System.out.println("User IP>>>>>>>" + ip);
        /*if (StringUtils.isBlank(ip)) {
            throw new BizException(ErrorCode.PARAM_INVALID, "NOT FOUND IP");
        }*/

        // openid
        String openId = saveNoticeOrder.getUid();

        // 组装订单完成的消息通知
        String bizType = saveNoticeOrder.getBizType();
        OrderDTO orderInfo = saveNoticeOrder.getOrderInfo();
        StringBuilder noticeContent = new StringBuilder();
        // 电子点餐做饭完成通知
        if ("1".equals(bizType)) {
            noticeContent.append("取餐通知");
            noticeContent.append("\n----------------------------------------");
            noticeContent.append("\n您订购的菜品已烹饪完成，请到前台取餐，谢谢支持。");
            noticeContent.append("\n订单号码:").append(orderInfo.getId().toString());
            noticeContent.append("\n下单时间: ").append(orderInfo.getCreated().split("\\.")[0]);

            // 订单项
            List<OrderItemDTO> orderItemList = orderInfo.getItemList();
            if (orderItemList != null && orderItemList.size() > 0) {
                noticeContent.append("\n订单内容: ");
                for (OrderItemDTO orderItem : orderItemList) {
                    noticeContent.append("\n\t\t\t").append(orderItem.getProdNmChn()).append(" x ").append(orderItem.getQty());
                }
            }

            // 去评价URL
            System.out.println(">>>>>>"+appConfigure.get("wechatDomain"));
            // String targetUrl = "http://" + appConfigure.get("wechatDomain") + "/eorder/wechat/api/v1/menuAuth?qpa=/evaluate?id=" + orderInfo.getId().toString();
            String targetUrl = "http://" + appConfigure.get("wechatDomain") + "/eorder/wechat/api/v1/menuAuth?qpa=/myOrderDetail?id=" + orderInfo.getId().toString();
            noticeContent.append("\n\n欢迎您再次光临本店。<a href='" + getReviewLinkUrl(targetUrl) + "'>☞去评价☜</a>");
            noticeContent.append("\n");
            log.debug("订单完成通知内容：：" + noticeContent.toString());
        } else if ("2".equals(bizType)) {
            //TODO
        } else if ("3".equals(bizType)) {
            //TODO
        } else if ("4".equals(bizType)) {
            //TODO
        }

        // 启动线程发送通知
        /*try {
            Thread thread = new Thread(new FinishedOrderNoticeThread());
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // 获取微信公众平台的access_token
        String token = Objects.toString(redisUtil.get("wx_token"), null);
        logger.info("Wechat Access Token::::::" + token);
        WechatAPI wechatAPI = WechatAPI.getInstance();

        // 发送微信普通消息
        WeixinReturn weixinReturn = wechatAPI.sendTextMsg(token, openId, noticeContent.toString());
        logger.info("End Finished Order Notice.Result is::::" + weixinReturn);
        if (weixinReturn.getErrcode() == 0) {
            return "success";
        }
        // 用户和微信公众号互通超时，不允许再向用户发送客服消息
        else if (weixinReturn.getErrcode() == 45015) {
            // errcode=45015, errmsg=response out of time limit or subscription is canceled hint: [7.pw4a0487shb1]
            return "timeout";
        }
        // 发送失败
        else {
            return "failure";
        }

        // 更新订单状态并记录通知次数
        //Order orderCheck = orderRepository.updateOrder(orderInfo.getId());
    }

    /**
     *获取去评价的全链接
     *
     * @param targetUrl
     * @return
     */
    private String getReviewLinkUrl(String targetUrl) {
        System.out.println(appConfigure.get("wechatAppId"));
        System.out.println(appConfigure.get("wechatDomain"));
        try {
            if (StringUtils.isNotEmpty(targetUrl)) {
                String encodedTargetUrl = URLEncoder.encode(targetUrl, UTF_8);
                StringBuffer sb = new StringBuffer();
                sb.append("https://open.weixin.qq.com/connect/oauth2/authorize")
                        .append("?appid=").append(appConfigure.get("wechatAppId"))
                        .append("&redirect_uri=").append(encodedTargetUrl)
                        .append("&response_type=").append("code")
                        .append("&scope=").append("snsapi_base")
                        .append("&state=").append('n')
                        .append("#wechat_redirect");
                System.out.println(sb.toString());
                return sb.toString();
            } else {
                return targetUrl;
            }
        } catch (Exception e) {
            return targetUrl;
        }
    }
}
