package com.basoft.eorder.foundation.jdbc.query.hotel;

import com.basoft.eorder.interfaces.query.ExchangeRateDTO;
import com.basoft.eorder.interfaces.query.hotel.HotelStoreVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 酒店查询结果数据加工类
 */
@Slf4j
public class HotelStoreQueryData {
    /**
     * 填充业务标签信息和中国价格
     *
     * @param erd
     * @param hotelStoreList
     */
    public static void convertCategoryInfoAndPrice(ExchangeRateDTO erd, List<HotelStoreVO> hotelStoreList) {
        if (hotelStoreList != null && hotelStoreList.size() > 0) {
            log.debug("开始填充门店的业务标签信息和中国价格信息<><><>" + hotelStoreList.size());
            // 循环查询门店的业务目录
            for (HotelStoreVO hotelStoreVO : hotelStoreList) {
                // 韩币价格获取中国人民币价格
                if (StringUtils.isNotBlank(hotelStoreVO.getMinPriceKor())) {
                    BigDecimal minPriceChn = new BigDecimal(hotelStoreVO.getMinPriceKor()).multiply(erd.getKrwcnyRate()).setScale(2, BigDecimal.ROUND_UP);
                    hotelStoreVO.setMinPriceChn(String.valueOf(minPriceChn));
                }
            }
            log.debug("填充门店的业务标签信息结束！！！~~~~~~~~~~~~~~~");
        }
    }
}