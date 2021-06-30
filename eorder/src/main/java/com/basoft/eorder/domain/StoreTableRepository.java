package com.basoft.eorder.domain;

import com.basoft.eorder.domain.model.StoreTable;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:24 2018/12/13
 **/
public interface StoreTableRepository {
    /**
     * @param  id
     * @return com.basoft.eorder.domain.model.StoreTable
     * @describe 一条餐桌信息
     * @author Dong Xifu
     * @date 2018/12/13 下午2:55
     */
    StoreTable getStoreTable(Long id);

    /**
     *获取门店的静默桌号
     *
     * @param storeId
     * @return
     */
    StoreTable getSilentStoreTable(Long storeId);
}
