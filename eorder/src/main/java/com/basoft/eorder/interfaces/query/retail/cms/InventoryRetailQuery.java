package com.basoft.eorder.interfaces.query.retail.cms;

import java.util.List;
import java.util.Map;

public interface InventoryRetailQuery {

    /**
     * 产品下的所有规格库存查询
     *
     * @param param
     * @return
     */
    List<InventoryRetailDTO> getInventoryRetailListByMap(Map<String, Object> param);

    int getProductRetailCount(Map<String, Object> param);

    /**
     * 产品列表
     *
     * @param param
     * @return
     */
    List<InventoryRetailDTO> getProductRetailListByMap(Map<String, Object> param);

    /**
     * 零售业务：
     * 根据skuId查询对应的库存信息
     *
     * @param storeId
     * @param toCheckInvProdSkuIdList
     * @return
     */
    List<InventoryRetailDTO> getRetailInventoryListByConditions(Long storeId, List<Long> toCheckInvProdSkuIdList);
}