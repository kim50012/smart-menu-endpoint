package com.basoft.eorder.foundation.jdbc.query.restaurant;

import com.basoft.eorder.interfaces.query.ExchangeRateDTO;
import com.basoft.eorder.interfaces.query.hotel.HotelStoreVO;
import com.basoft.eorder.interfaces.query.restaurant.RestaurantStoreVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 餐厅查询结果数据加工类
 */
@Slf4j
public class RestaurantStoreQueryData {
    /**
     * 填充业务标签信息和中国价格
     *
     * @param erd
     * @param restaurantStoreList
     */
    public static void convertCategoryInfoAndPrice(ExchangeRateDTO erd, List<RestaurantStoreVO> restaurantStoreList) {
        if (restaurantStoreList != null && restaurantStoreList.size() > 0) {
            log.debug("开始填充门店的业务标签信息和中国价格信息<><><>" + restaurantStoreList.size());
            // 循环查询门店的业务目录
            for (RestaurantStoreVO restaurantStoreVO : restaurantStoreList) {
                // 韩币价格获取中国人民币价格
                if (StringUtils.isNotBlank(restaurantStoreVO.getMinPriceKor())) {
                    BigDecimal minPriceChn = new BigDecimal(restaurantStoreVO.getMinPriceKor()).multiply(erd.getKrwcnyRate()).setScale(2, BigDecimal.ROUND_UP);
                    restaurantStoreVO.setMinPriceChn(String.valueOf(minPriceChn));
                }
            }
            log.debug("填充门店的业务标签信息结束！！！~~~~~~~~~~~~~~~");
        }
    }
}