package com.basoft.eorder.domain.retail;

import com.basoft.eorder.domain.model.retailOrderService.RetailOrderService;
import com.basoft.eorder.interfaces.command.retail.SaveRetailOrderService;

/**
 * 零售商户订单售后服务表。一条记录整合所有信息，不拆分存储(RetailOrderService)表数据库访问层
 *
 * @author DongXifu
 * @since 2020-05-12 14:26:23
 */
public interface RetailOrderServiceRepository {
    /**
     * 零售业务订单退换货/售后
     *
     * @param retailOrderService
     * @return
     */
    int saveRetailOrderService(RetailOrderService retailOrderService);

    /**
     * 受理：零售业务订单退换货/售后申请的受理
     *
     * @param retailOrderService
     * @return
     */
    int acceptance(RetailOrderService retailOrderService);

    /**
     * 审核：售业务订单退换货/售后申请的审核
     *
     * @param retailOrderService
     * @return
     */
    int audit(RetailOrderService retailOrderService);

    /**
     * 更新售后申请记录的状态
     *
     * @param saveRetailOrderService
     */
    int updateOrderServiceStatus(SaveRetailOrderService saveRetailOrderService);
}