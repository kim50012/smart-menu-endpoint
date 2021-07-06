package com.basoft.eorder.batch.job.threads.retail;

import com.basoft.eorder.batch.job.model.retail.RetailToDoRecoverTempOrder;
import com.basoft.eorder.domain.retail.InventoryRetailRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 零售业务商户产品库存恢复线程
 *
 * @author Mentor
 * @version 1.0
 * @since 20200422
 */
@Slf4j
public class RetailOrderRecoverThread implements Runnable {
    private InventoryRetailRepository inventoryRetailRepository;

    private List<RetailToDoRecoverTempOrder> orderTempList;

    // 库存恢复类型 auto-定时任务，下单不支付类临时订单  refund-退款库存恢复
    private String recoverType;

    public RetailOrderRecoverThread(InventoryRetailRepository inventoryRetailRepository,
                                    List<RetailToDoRecoverTempOrder> orderTempList,
                                    String recoverType) {
        this.inventoryRetailRepository = inventoryRetailRepository;
        this.orderTempList = orderTempList;
        this.recoverType = recoverType;
    }

    /**
     * 零售业务商户产品库存恢复线程
     */
    @Override
    public void run() {
        try {
            inventoryRetailRepository.recoverRetailInventory(orderTempList, recoverType);
        } catch (Exception e) {
            log.error("【零售业务库存恢复】零售业务库存恢复异常，异常信息为：" + e.getMessage(), e);
        }
    }
}