package com.basoft.eorder.application.framework;

import java.util.concurrent.Future;

public interface EventProducer {
    Future<Event.Result> produce(Event event);

    // CompletableFuture<Event.Result> produce(Event event);

    String pushMsgSend(Event event, String storeId, String tableId, String storeType);
}
