package com.basoft.eorder.interfaces.query.hotel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class HotelProductSkuDatePriceDTO {
    private Long storeId;

    private String prodId;

    private String prodSkuId;

    private String priceDate;

    private String isOpening;

    private String prodSkuPriceKor;

    private String inventPriceKor;

    private String inventDisPriceKor;

    private String effectivePriceKor;

    /**
     * 1-产品SKU价格为有效价格。此时库存价格为空，无论库存折扣价是否为空
     * 2-库存原价为有效价格。库存折扣价为空或者比库存价还高，即库存折扣价无效
     * 3-库存折扣价为有效价格。
     */
    private String priceType;

    private String prodSkuPriceChn;

    private String inventPriceChn;

    private String inventDisPriceChn;

    private String effectivePriceChn;

    private String priceSettle;

    private String disPriceSettle;

    private String prodPriceSettle;

    private String effectivePriceSettle;
}
