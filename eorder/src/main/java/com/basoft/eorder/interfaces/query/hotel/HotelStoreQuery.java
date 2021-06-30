package com.basoft.eorder.interfaces.query.hotel;

import java.util.List;

public interface HotelStoreQuery {
    /**
     * hotel simple query/get data count
     *
     * @param hotelStoreQueryDTO
     * @return
     */
    public int getHotelStoreSimpleCount(HotelStoreQueryDTO hotelStoreQueryDTO);

    /**
     * hotel simple query/get data list
     *
     * @param hotelStoreQueryDTO
     * @return
     */
    public List<HotelStoreVO> getHotelStoreSimpleList(HotelStoreQueryDTO hotelStoreQueryDTO);

    /**
     * hotel complex query/get data count
     *
     * @param hotelStoreQueryDTO
     * @return
     */
    public int getHotelStoreComplexCount(HotelStoreQueryDTO hotelStoreQueryDTO);

    /**
     * hotel complex query/get data list
     *
     * @param hotelStoreQueryDTO
     * @return
     */
    public List<HotelStoreVO> getHotelStoreComplexList(HotelStoreQueryDTO hotelStoreQueryDTO);
}