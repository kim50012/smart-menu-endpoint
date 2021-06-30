package com.basoft.eorder.foundation.jdbc.eventhandler;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.basoft.eorder.application.framework.Event;
import com.basoft.eorder.application.framework.EventProducer;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.interfaces.command.SaveOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

public class EventHandler implements EventProducer {
    private JPushClient pushClient;
    private ThreadPoolExecutor executor;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public EventHandler(JPushClient client, ThreadPoolExecutor executor) {
        this.pushClient = client;
        this.executor = executor;
    }

    /**
     * 异步发送时间到极光服务器上，如果要保证数据发送的准确性的话，可以对 CompleteFuture 做进一步的操作
     *
     * @param event
     * @return Future
     */
    @Override
    public Future<Event.Result> produce(Event event) {
        logger.debug("Handle Event :" + event.getName() + " payload:" + event.getPayload());
        if (SaveOrder.NAME.equalsIgnoreCase(event.getName())) {
            /*logger.info("Start handler event now for:"+SaveOrder.NAME+"  command");
            WxSession wxSession = (WxSession) event.getContextProps().get(AppConfigure.BASOFT_WX_SESSION_PROP);

            logger.info("StoreID:"+wxSession.getStoreId().toString());

            String ssid = wxSession.getStoreId().toString();
            logger.info("Notify to store:"+ssid);
            PushPayload payload = buildPayload(ssid);
            return CompletableFuture.supplyAsync(newMessagePublisher(payload,event),this.executor);*/
        }
        return CompletableFuture.completedFuture(null);
    }


    private Supplier<Event.Result> newMessagePublisher(PushPayload payload, Event event) {
        return new Supplier<Event.Result>() {
            @Override
            public Event.Result get() {
                try {
                    final PushResult pushResult = pushClient.sendPush(payload);
                    logger.info("Got result - " + pushResult);
                    return new Event.Result(event.getName());
                } catch (Throwable e) {
                    // e.printStackTrace();
                    logger.error("send message error:" + event.getName(), e);
                    throw new IllegalStateException(e);
                }
            }
        };
    }

    /**
     * 推送极光消息
     *
     * @param event
     * @param storeId
     * @param tableId
     * @return
     */
    @Override
    public String pushMsgSend(Event event, String storeId, String tableId, String storeType) {
        logger.info("▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶ 极光推送-BUILD-PAYLOAD ◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
        PushPayload payload = buildPayload(storeId, tableId, storeType);
        logger.info("▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶ 极光推送-PUSHING ：PushINFO" + payload.toString() +"◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
        Future<Event.Result> eventResult = CompletableFuture.supplyAsync(newMsgPublisher(payload), this.executor);
        logger.info("▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶ 极光推送-PUSHED ◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
        return null;
    }

    /**
     * 创建极光PushPayload
     *
     * @param storeId
     * @param tableId
     * @return
     */
    public static PushPayload buildPayload(String storeId, String tableId, String storeType) {
        SimpleDateFormat dtformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = new Date();
        String dt = dtformat.format(time);

        // 个性化区分极光推送
        String cmt = "";
        // 医美
        if(CommonConstants.BIZ_HOSPITAL_STRING.equals(storeType)){
            cmt = dt + ", 예약이 접수 되었습니다.";
        }
        // 购物
        else if (CommonConstants.BIZ_SHOPPING_STRING.equals(storeType)){
            cmt = dt + ", 주문이 접수 되었습니다.";
        }
        // 酒店
        else if (CommonConstants.BIZ_HOTEL_STRING.equals(storeType)){
            cmt = dt + ", 예약이 접수 되었습니다.";
        }
        // 点餐
        else {
            if("0-0".equals(tableId)){
                cmt = dt + ", 외부 주문.";
            } else {
                cmt = dt + ", " + tableId + "번 테이블";
            }
        }

        // 20190628修改IOS系统无法播放消息声音
        /*return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                // .setAudience(Audience.tag(storeId))
                .setAudience(Audience.tag_and(storeId))
                .setNotification(Notification.android(cmt, "신규 주문이 접수 되었습니다.", new HashMap<>()))
                // .setNotification(Notification.alert("test message"))
                // .setNotification(Notification.android(ALERT, TITLE, null))
                .setOptions(Options.newBuilder()
                        .setTimeToLive(300)
                        .build())
                .build();*/

        // 支持安卓和iOS
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.tag_and(storeId))
                //.setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .setAlert(cmt)
                        // 安卓
                        .addPlatformNotification(
                                AndroidNotification.newBuilder()
                                        .setAlert(cmt)
                                        .setTitle("신규 주문이 접수 되었습니다.")
                                        .addExtras(new HashMap<String,String>())
                                        // 指定通知栏样式
                                        .setBuilderId(1)
                                        .build()
                        )
                        // iOS
                        .addPlatformNotification(
                                IosNotification.newBuilder()
                                        .setAlert(cmt)
                                        .incrBadge(1)
                                        .setSound("sound.caf")
                                        .addExtras(new HashMap<String,String>())
                                        .build()
                        ).build()
                )
                .setOptions(Options.newBuilder()
                        .setTimeToLive(300)
                        // ios指定为生产环境
                        .setApnsProduction(true)
                        .build())
                .build();

        // 支持iOS
        /*return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                //.setAudience(Audience.all())
                .setAudience(Audience.tag_and(storeId))
                .setNotification(Notification.newBuilder()
                        .setAlert(cmt)
                        // iOS
                        .addPlatformNotification(
                                IosNotification.newBuilder()
                                        .setAlert(cmt)
                                        .incrBadge(1)
                                        .setSound("sound.caf")
                                        .addExtras(new HashMap<String,String>())
                                        .build()
                        ).build()
                )
                .setOptions(Options.newBuilder()
                        .setTimeToLive(300)
                        .build())
                .build();*/
    }

    private Supplier<Event.Result> newMsgPublisher(PushPayload payload) {
        return new Supplier<Event.Result>() {
            @Override
            public Event.Result get() {
                try {
                    final PushResult pushResult = pushClient.sendPush(payload);
                    logger.info("newMsgPublisher result --------------> " + pushResult);
                    return new Event.Result("order_pay");
                } catch (Throwable e) {
                    logger.info("newMsgPublisher error --------------> " + e.getMessage());
                    // e.printStackTrace();
                    throw new IllegalStateException(e);
                }
            }
        };
    }
}
