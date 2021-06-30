package com.basoft.eorder.interfaces.query.inventory.hotel;

import com.basoft.eorder.domain.model.inventory.hotel.InventoryHotel;
import com.basoft.eorder.domain.model.inventory.hotel.StoreDayPrice;
import com.basoft.eorder.interfaces.query.hotel.HotelProductSkuDatePriceDTO;

import java.util.List;
import java.util.Map;

public interface InventoryHotelQuery {
    InventoryHotelDTO getInventoryHotelDto(Map<String, Object> param);

    InventoryHotel getInventoryHotel(Map<String, Object> param);

    int getInventoryHotelCount(Map<String, Object> param);

    List<InventoryHotelDTO> getIventHotelGroupList(Map<String, Object> param);
    

    List<InventoryHotelDTO> getInventoryHotelList(Map<String, Object> param);

    /**
     * 查询店铺下未来180天每日最低价格
     *
     * @Param storeId
     * startTime endTime
     * @author Dong Xifu
     * @date 12/9/19 10:04 AM
     */
    List<StoreDayPrice> getfuturePriceMinList(Map<String, Object> param);

    /**
     * 没有设置酒店价格的房间
     * @param param inv_date 日期
     * @param param storeId
     * @return prodId,price,date
     */
    InventoryHotelDTO.InvDateAndInv getUnSetHotelProds(Map<String, Object> param);

    /**
     * 未来n天所有房间的默认价格
     *
     * @Param
     * @return List<StoreDayPrice>
     * @author Dong Xifu
     * @date 12/15/19 12:59 PM
     */
    List<InventoryHotel> getfutureProdAll(Map<String, Object> param);

    /**
     * 未来n天所有房间已设置的价格
     *
     * @Param
     * @return
     * @author Dong Xifu
     * @date 12/15/19 12:59 PM
     */
    List<InventoryHotel> getfutureSetProDays(Map<String, Object> param);





    /**
     * 查询指定酒店商户和查询日期期间的库存信息
     *
     * @param param storeId 商户id 必选
     *              skuId skuId 可选
     *              startDate 必选
     *              endDate 必选
     * @return
     */
    List<InventoryHotelDTO> getHotelInventoryListByConditions(Map<String, Object> param);

    /**
     * 查询指定酒店的所有产品的所有sku的日期期间内的价格
     *
     * @param storeId
     * @param startDate
     * @param endDate
     * @return
     */
    List<HotelProductSkuDatePriceDTO> getProductSkuDatePriceList(long storeId, String startDate, String endDate);

    /**
     * 查询指定酒店的指定sku的日期期间内的价格
     *
     * @param prodSkuId
     * @param startDate
     * @param endDate
     * @return
     */
    List<HotelProductSkuDatePriceDTO> getProductSkuDatePriceBySkuIdList(long prodSkuId, String startDate, String endDate);
}