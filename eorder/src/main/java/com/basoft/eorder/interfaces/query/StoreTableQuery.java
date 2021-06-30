package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.domain.model.Store;

import java.util.List;
import java.util.Map;

public interface StoreTableQuery {


    StoreTableDTO getStoreTableByMap(Map<String, Object> param);

    StoreTableDTO getStoreTableById(long id);

    int getStoreTableCount(Map<String, Object> param);

    List<StoreTableDTO> getStoreTableListByMap(Map<String, Object> param);
}
