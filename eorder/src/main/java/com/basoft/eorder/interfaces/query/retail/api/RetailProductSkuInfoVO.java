package com.basoft.eorder.interfaces.query.retail.api;

import com.basoft.eorder.interfaces.query.ExchangeRateDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;

/**
 * ClassName ProductSkuDTO
 * Description
 *
 * @version 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetailProductSkuInfoVO {
    private Long prodSkuId;

    private String nameKor;

    private String nameChn;

    private BigDecimal weight;

    private Long productId;

    //对应表product_sku中price
    private BigDecimal priceKor;

    private BigDecimal priceChn;

    private int isInventory;

    private int disOrder;

    private boolean useDefault;

    private Long prodSkuInv;

    // 该sku的规格组合明细：组合明细信息拼装成字符串格式
    private String skuStdInfo;

    // 该sku的图片：组合成字符串格式
    private String skuImageInfo;

    //折扣相关
    private Long skuDiscId;

    private BigDecimal skuDiscPriceKor;

    private BigDecimal skuDiscPriceChn;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
