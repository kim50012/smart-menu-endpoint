package com.basoft.eorder.batch.job.threads;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.domain.InventoryHotelRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 酒店库存恢复线程
 *
 * @author Mentor
 * @version 1.0
 * @since 20190905
 */
@Slf4j
public class HotelOrderRecoverThread implements Runnable {
    private InventoryHotelRepository inventoryHotelRepository;
    private Map<String, Object> orderTemp;
    // 库存恢复类型 auto-定时任务，下单不支付类临时订单  refund-退款库存恢复
    private String recoverType;
    private AppConfigure appConfigure;

    public HotelOrderRecoverThread(InventoryHotelRepository inventoryHotelRepository,
                                   Map<String, Object> orderTemp,
                                   String recoverType,
                                   AppConfigure appConfigure) {
        this.inventoryHotelRepository = inventoryHotelRepository;
        this.orderTemp = orderTemp;
        this.recoverType = recoverType;
        this.appConfigure = appConfigure;
    }

    /**
     * 酒店库存恢复线程
     */
    @Override
    public void run() {
        try {
            Long transId = (Long) orderTemp.get("transId");
            Long storeId = (Long) orderTemp.get("storeId");
            Long skuId = (Long) orderTemp.get("skuId");
            String reseveDtFrom = (String) orderTemp.get("reseveDtFrom");
            String reseveDtTo = (String) orderTemp.get("reseveDtTo");
            inventoryHotelRepository.recoverHotelInventory(transId, storeId, skuId, reseveDtFrom, reseveDtTo, recoverType, appConfigure);
        } catch (Exception e) {
            log.error("【酒店库存恢复】酒店库存恢复异常，异常信息为：" + e.getMessage(), e);
        }
    }
}
