package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.domain.model.store.StoreChargeInfoRecord;

import java.util.List;
import java.util.Map;

public interface StoreQuery {
    StoreDTO getStoreById(long id);

    int getStoreCount(Map<String, Object> param);

    List<StoreDTO> getStoreListByMap(Map<String, Object> param);

    List<StoreDTO> getStoreListConsumed(Map<String, Object> param);

    /**
     * 酒店列表数量查询
     *
     * @param param
     * @return
     */
    int getHotelStoreCount(Map<String, Object> param);

    /**
     * 酒店列表查询
     *
     * @param param
     * @return
     */
    List<StoreDTO> getHotelStoreListByMap(Map<String, Object> param);

    /**
     *查询商户下月的计费信息
     *
     * @param storeId
     * @param year
     * @param month
     * @return
     */
    List<StoreChargeInfoRecord> getNextMonthChargeInfo(Long storeId, Integer year, Integer month);
}
