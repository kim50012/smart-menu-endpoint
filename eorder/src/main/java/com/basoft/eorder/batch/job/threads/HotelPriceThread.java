package com.basoft.eorder.batch.job.threads;

import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.inventory.hotel.InventoryHotel;
import com.basoft.eorder.domain.model.inventory.hotel.StoreDayPrice;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelQuery;
import com.basoft.eorder.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 1:58 PM 12/6/19
 **/
@Slf4j
public class HotelPriceThread implements Runnable {

    private UserSession us;
    private StoreRepository storeRepository;
    private InventoryHotelQuery inventoryHotelQuery;

    public HotelPriceThread(UserSession us, StoreRepository storeRepository
        , InventoryHotelQuery inventoryHotelQuery) {
        this.us = us;
        this.storeRepository = storeRepository;
        this.inventoryHotelQuery = inventoryHotelQuery;
    }

    @Override
    public void run() {
        long t1 = System.currentTimeMillis();
        Map param = new HashMap();
        param.put("storeId", us.getStoreId());
        param.put("startTime", DateUtil.getToday());
        param.put("endTime", DateUtil.getFatureDate(StoreDayPrice.fatureNum));
        param.put("dateTime", DateUtil.getFatureDate(StoreDayPrice.fatureNum));

        //180天酒店每日价格,其中所有房间都设置价格或者所有房间都没有设置价格的话数据正确
        List<StoreDayPrice> storeDayPrices = inventoryHotelQuery.getfuturePriceMinList(param);

        //180内所有日期的房间默认价格
        List<InventoryHotel> futureProdAll = inventoryHotelQuery.getfutureProdAll(param);

        //180内所有设置日期的房间价格
        List<InventoryHotel> setProDays = inventoryHotelQuery.getfutureSetProDays(param);

        //180内没有设置房间价格的房间的日期
        List<InventoryHotel> unSetProDays = new LinkedList<>();

        //为了取差集先取下交集
        List<InventoryHotel> interProDays = new LinkedList<>();

        //取交集集得到所有有设置房间价格的产品的日期
        for (InventoryHotel f : futureProdAll) {
            setProDays.stream().map(s -> {
                if (s.getInvDate().equals(f.getInvDate())) {
                    if (f.getProdId().longValue() == s.getProdId().longValue()
                        && f.getProdSkuId().longValue() == s.getProdSkuId().longValue()) {
                        interProDays.add(f);
                    }
                }
                return s;
            }).collect(Collectors.toList());
        }

        //取差集得到没有设置房间价格的产品的日期
        futureProdAll.removeAll(interProDays);unSetProDays = futureProdAll;

        unSetProDays.sort(Comparator.comparing((InventoryHotel::getPrice)));

        //所有没设置价格的房间与设置的价格比较
        List<StoreDayPrice> finalStoreDayPrices = storeDayPrices;
        unSetProDays.stream().forEach(u -> {
            for (StoreDayPrice s : finalStoreDayPrices) {
                if (u.getInvDate().equals(s.getInvDate())) {
                    if (s.getMinPrice() > u.getPrice().longValue()) {
                        s.setMinPrice(u.getPrice().longValue());
                    }
                    if (s.getMaxPrice() < u.getPrice().longValue()) {
                        s.setMaxPrice(u.getPrice().longValue());
                    }
                }
            }
        });

        try {
            int batchNum = storeRepository.batchSaveStoreDayPrice(finalStoreDayPrices, us.getAccount());
            log.info("酒店最低价格执行条数=" + batchNum);
            long t2 = System.currentTimeMillis();
            System.out.println(String.format("insert over! cost:%sms", (t2 - t1)));

        } catch (Exception e) {
            log.error("【酒店每日最低价更新】异常，异常信息为：" + e.getMessage(), e);
        }
    }

}
