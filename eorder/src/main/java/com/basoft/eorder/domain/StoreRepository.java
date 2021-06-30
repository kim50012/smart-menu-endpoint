package com.basoft.eorder.domain;

import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.domain.model.*;
import com.basoft.eorder.domain.model.inventory.hotel.StoreDayPrice;
import com.basoft.eorder.domain.model.store.StoreChargeInfoRecord;
import com.basoft.eorder.foundation.jdbc.eventhandler.BannerEvent;
import com.basoft.eorder.interfaces.command.DelStoreTable;
import com.basoft.eorder.interfaces.command.UpdateShipPointStatus;
import com.basoft.eorder.interfaces.command.UpdateStoreStatus;

import java.util.List;
import java.util.Map;

public interface StoreRepository {
    Long saveStore(Store store);

    Store updateStore(Store store);

    Store getStore(Long id);

    Long deleteStore(Long storeId);

    int updateStoreStatus(UpdateStoreStatus storeStatus);

    void saveBanner(Banner banner);

    int getMaxBannerOrder(Long storeId);

    Banner getBanner(Map<String, Object> param);

    Long updateBanner(Banner banner);

    int updateBannerStatus(int status, List<BannerEvent> list);

    int delBannerStatusList(List<BannerEvent> bannerList);


    Long updateTable(StoreTable sTable);

    List<Long> updateStoreTableList(List<StoreTable> list);

    Long saveTable(StoreTable sTable);

    List<StoreTable> getStoreTableList(Store store);

    List<StoreTable> getQrStoreTableList(Store store);

    //根据storeId,tag,number 查询改餐桌是否已存在
    StoreTable getStoreTable(StoreTable storeTable);

    void deleteStoreTable(Long tableId);

    int saveTableList(List<StoreTable> saveList);

    int deleteStoreTableBatch(DelStoreTable delStoreTable);

    void updateStoreCategoryMap(Long storeId, List<StoreCategory> storeCategoryList);

    /**
     * 查询门店的关联目录（商品分类和商店标签）
     *
     * @param store 门店对象
     * @return
     */
    List<Category> getStoreCategory(Store store);

    /**
     * 查询门店的关联目录（商品分类和商店标签）
     *
     * @param storeId 门店ID
     * @return
     */
    List<Category> getStoreCategory(Long storeId);

    List<QRCode> getQRCodeListByMap(Map<String, Object> param);

    void insertQrcodeList(List<QRCode> addQrcodeList);

    int getQRCodeCountByStoreId(long storeId, List<String> actionNms);

    List<Store> getStoreList(long managerId);

    //收货区域
    Long saveShipPoint(ShipPoint shipPoint);

    Long deleteShipPoint(Long shipPointid);

    ShipPoint updateShipPoint(ShipPoint shipPoint);

    int updateShipPointStatus(UpdateShipPointStatus shipPointStatus);

    ShipPoint getShipPoint(Long shipPointid);

    /**
     * Admin CMS中单点登录Manager CMS时对storeId和managerId匹配性进行检查
     *
     * @param storeId
     * @param managerId
     * @return
     */
    Store checkStoreManager(String storeId, String managerId);

    /**
     * 根据商户ID查询商户的计费信息修改记录
     *
     * @param storeId
     * @return
     */
    List<StoreChargeInfoRecord> getStoreChargeInfoRecord(Long storeId);

    /**
     * 根据商户ID和指定年月查询商户的计费信息修改记录
     *
     * @param storeId
     * @return
     */
    List<StoreChargeInfoRecord> getStoreChargeInfoRecord(Long storeId, Integer year, Integer month);

    /**
     * 更新指定年月计费信息无效
     *
     * @param storeId
     * @param year
     * @param month
     */
    void updateStoreChargeInfo(Long storeId, Integer year, Integer month);

    /**
     * 保存最新设定的次月计费信息
     *
     * @param storeChargeInfoRecord
     */
    void saveStoreChargeInfo(StoreChargeInfoRecord storeChargeInfoRecord);

    /**
     * 获取非删除的商户列表
     *
     * @return
     */
    List<Store> getUsingStoreList();

    /**
     * 获取非删除的PG交易类或BA交易类商户列表
     *
     * @param cashSettleType
     * @return
     */
    List<Store> getUsingPGOrBAStoreList(String cashSettleType);

    /**
     * 同步计费信息到Store表
     *
     * @param id
     * @param chargeType
     * @param chargeRate
     * @param chargeFee
     */
    Long saveCurMonthMainChargeInfo(Long id, int chargeType, double chargeRate, double chargeFee);

    /**
     * 查询商户指定年月的计费信息
     *
     * @param id
     * @param lastYear
     * @param lastMonth
     * @return
     */
    StoreChargeInfoRecord getStoreMonthChargeInfo(Long id, int lastYear, int lastMonth);

    /**
     * 定格结算日期所在月的商户计费信息-to useYn=2 finalYn=1
     *
     * @param storeChargeInfoRecord
     */
    void updateStoreCharge(StoreChargeInfoRecord storeChargeInfoRecord);

    void saveStoreDailyGrade(Store store,Double avgRevClass,int count);

    int batchSaveStoreDayPrice(List<StoreDayPrice> storeDayPrices,String acount);
}