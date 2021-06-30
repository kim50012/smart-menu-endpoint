package com.basoft.eorder.interfaces.query.retail.api;

import com.basoft.eorder.interfaces.query.ExchangeRateDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;
import java.util.List;

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
public class RetailProductSkuDTO {
    private Long id;

    private Long storeId;

    private Long productId;

    private String name;

    private String nameKor;

    private String nameChn;

    //对应表product_sku中price
    private BigDecimal priceKor;

    private BigDecimal priceChn;

    //对应表product_sku中PRICE_SETTLE
    private BigDecimal priceSettle;

    private BigDecimal priceWeekend;

    private Boolean useDefault;

    private String created;

    private int isInventory;

    private Long invCount;

    private int status;

    // sku的规格组合明细
    private List<ProductSkuAloneStandardMapDTO> combinationDetails;

    private ExchangeRateDTO erd;

    //折扣相关
    private Long discId;

    private BigDecimal discPriceKor;

    private BigDecimal discPriceChn;









    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
