package com.basoft.eorder.batch.job.threads;

import com.basoft.eorder.batch.job.StoreOrderStatusJob;
import com.basoft.eorder.domain.OrderRepository;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.retail.RetailOrderRepository;
import com.basoft.eorder.interfaces.command.retail.SaveRetailOrderEvent;
import com.basoft.eorder.interfaces.query.OrderDTO;
import com.basoft.eorder.interfaces.query.OrderQuery;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class RetailCompleteOrderThread implements Runnable {
    private Store store;
    private OrderRepository orderRepository;
    private OrderQuery orderQuery;
    private RetailOrderRepository retailOrderRepository;
    public RetailCompleteOrderThread(Store store,OrderQuery orderQuery, OrderRepository orderRepository,RetailOrderRepository retailOrderRepository) {
        this.store = store;
        this.orderQuery = orderQuery;
        this.orderRepository = orderRepository;
        this.retailOrderRepository = retailOrderRepository;
    }

    @Override
    public void run() {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("storeId", store.id());
            param.put("storeType", store.storeType());
            param.put("dayNum", "30");
            List<OrderDTO> orderList = orderQuery.retailOrderCompleteList(param);//待处理订单
            if (orderList != null && orderList.size() > 0) {
                List<Long> longList = orderList.stream().map(o -> o.getId()).collect(Collectors.toList());
                int size = orderRepository.batchInsertRetailOrderBackupCompled(longList);
                System.out.println("reail备份订单条数size=" + size);

                List<SaveRetailOrderEvent> saveRetailOrderEvents = new LinkedList<>();
                longList.stream().forEach(l -> {
                    SaveRetailOrderEvent event = new SaveRetailOrderEvent();
                    event.setOrderId(l);
                    saveRetailOrderEvents.add(event);
                });

                retailOrderRepository.endRetailOrderbatch(saveRetailOrderEvents);
            }
        } catch (Exception e) {
            log.error("【" + StoreOrderStatusJob.retail_complete_log + "】异常，异常信息为：" + e.getMessage(), e);

        }
    }
}
