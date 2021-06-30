package com.basoft.eorder.batch.job.threads;

import com.basoft.eorder.batch.job.StoreOrderStatusJob;
import com.basoft.eorder.domain.OrderRepository;
import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.interfaces.query.OrderDTO;
import com.basoft.eorder.interfaces.query.OrderQuery;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 3:25 PM 12/5/19
 **/
@Slf4j
public class UpdateOrderStatusThread implements Runnable {
    private Store store;
    private OrderRepository orderRepository;
    private OrderQuery orderQuery;
    public UpdateOrderStatusThread(Store store,OrderQuery orderQuery, OrderRepository orderRepository){
        this.store = store;
        this.orderQuery = orderQuery;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run() {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("storeId", store.id());
            param.put("storeType", store.storeType());
            param.put("dayNum", "1");
            List<OrderDTO> orderList = orderQuery.orderCompleteList(param);
            List<Long> longList = orderList.stream().map(o -> o.getId()).collect(Collectors.toList());
            int size = orderRepository.batchInsertOrderBackupCompled(longList);
            System.out.println("订单条数size=" + size);
            orderRepository.batchUpOrderStatus(longList, Order.Status.COMPLETE.getStatusCode());
        } catch (Exception e) {
            log.error("【" + StoreOrderStatusJob.logTitle + "】异常，异常信息为：" + e.getMessage(), e);

        }
    }
}
