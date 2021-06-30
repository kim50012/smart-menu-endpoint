package com.basoft.eorder.interfaces.query.retailOrderService;

import com.basoft.eorder.domain.model.retailOrderService.RetailOrderService;

import java.util.List;
import java.util.Map;

/**
 * 零售商户订单售后服务表。一条记录整合所有信息，不拆分存储(RetailOrderService)表数据库访问层
 *
 * @author DongXifu
 * @since 2020-05-12 14:26:23
 */
public interface RetailOrderServiceQuery {
    RetailOrderService getRetailOrderService(Long servId,Long servCode);

    RetailOrderServiceDTO getRetailOrderServiceDto(Map<String, Object> param);

    int getRetailOrderServiceCount(Map<String, Object> param);

    List<RetailOrderServiceDTO> getRetailOrderServiceListByMap(Map<String, Object> param);

    List<RetailOrderServiceDTO> getAfterSalesApplys(Long servId,Long servCode);
}