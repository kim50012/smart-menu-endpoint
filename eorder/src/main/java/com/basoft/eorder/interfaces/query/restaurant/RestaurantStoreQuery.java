package com.basoft.eorder.interfaces.query.restaurant;

import java.util.List;

public interface RestaurantStoreQuery {
    /**
     * restaurant simple query/get data count
     *
     * @param restaurantStoreQueryDTO
     * @return
     */
    public int getRestaurantStoreSimpleCount(RestaurantStoreQueryDTO restaurantStoreQueryDTO);

    /**
     * restaurant simple query/get data list
     *
     * @param restaurantStoreQueryDTO
     * @return
     */
    public List<RestaurantStoreVO> getRestaurantStoreSimpleList(RestaurantStoreQueryDTO restaurantStoreQueryDTO);

    /**
     * restaurant complex query/get data count
     *
     * @param restaurantStoreQueryDTO
     * @return
     */
    public int getRestaurantStoreComplexCount(RestaurantStoreQueryDTO restaurantStoreQueryDTO);

    /**
     * restaurant complex query/get data list
     *
     * @param restaurantStoreQueryDTO
     * @return
     */
    public List<RestaurantStoreVO> getRestaurantStoreComplexList(RestaurantStoreQueryDTO restaurantStoreQueryDTO);
}