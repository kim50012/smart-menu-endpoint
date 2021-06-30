package com.basoft.eorder;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.WxSession;
import com.basoft.eorder.application.framework.Event;
import com.basoft.eorder.foundation.jdbc.eventhandler.EventHandler;
import com.basoft.eorder.interfaces.command.SaveOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class MessagePushTest {



    public static void main(String...args){

        //secret : 38b8a757964781ca42c62473
        //AppKey : 96a4e078fc06a27c63cfcd7d

//        final PushPayload payload = EventHandler.buildPayload("540050625280152582");
        JPushClient jpushClient = new JPushClient("38b8a757964781ca42c62473", "96a4e078fc06a27c63cfcd7d", null, ClientConfig.getInstance());

/*        try {
            PushResult result = jpushClient.sendPush(payload);
            System.out.println("Got result - " + result);

        } catch (APIConnectionException e) {
            // Connection error, should retry later
            e.printStackTrace();
//            LOG.error("Connection error, should retry later", e);

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            e.printStackTrace();
        }
        finally {
            jpushClient.close();
        }*/

        WxSession ws = new WxSession.Builder().storeId(new Long("548790696292455429")).build();

        Map<String,Object> context = new HashMap<>();
        context.put(AppConfigure.BASOFT_WX_SESSION_PROP,ws);

        ThreadPoolExecutor tpe = new ThreadPoolExecutor(1,10,new Long(10),TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(100));

        EventHandler eh =new EventHandler(jpushClient,tpe);
        Event event = new Event(SaveOrder.NAME,"payload",context);
        final Future<Event.Result> produce = eh.produce(event);


        try {
            produce.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        tpe.shutdownNow();

    }
}
