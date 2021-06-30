package com.basoft.eorder.domain;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.domain.model.inventory.hotel.InventoryHotel;

import java.util.List;

public interface InventoryHotelRepository {
    //新增
    int saveInventoryHotel(InventoryHotel inventoryHotel);

    //批量新增
    int saveInventoryHotels(List<InventoryHotel> inventoryHotels);

    //批量修改
    int upInventoryHotels(List<InventoryHotel> inventoryHotels);

    //修改
    int BatchUpdateInventHotelStatus(List<InventoryHotel> inventoryHotels);

    /**
     * 恢复酒店库存，恢复数量1
     *
     * @param transId
     * @param storeId
     * @param prodSkuId
     * @param fromDate
     * @param toDate
     * @param recoverType
     */
    void recoverHotelInventory(Long transId, Long storeId, Long prodSkuId, String fromDate,
                               String toDate, String recoverType, AppConfigure appConfigure);
}
