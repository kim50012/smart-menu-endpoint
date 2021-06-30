package com.basoft.eorder.domain.model.retail;

import lombok.Builder;
import lombok.Data;


/**
 * 产品SKU的规格组合明细表(ProductSkuAloneStandard)实体类
 *
 * @author DongXifu
 * @since 2020-04-03 17:08:42
 */
@Builder
@Data
public class ProductSkuAloneStandard {

    public ProductSkuAloneStandard(){

    }

    public ProductSkuAloneStandard(Long productSkuId,Long standardId,Long standardItemId,int status) {
        this.productSkuId = productSkuId;
        this.standardId = standardId;
        this.standardItemId = standardItemId;
        this.status = status;
    }

    /**
     * 产品SKU ID
     */
    private Long productSkuId;
    
    /**
     * 规格ID，PRODUCT_ALONE_STANDARD表中的STD_ID
     */
    private Long standardId;
    
    /**
     * 规格项目ID，PRODUCT_ALONE_STANDARD_ITEM表中的ITEM_ID
     */
    private Long standardItemId;

    private int status;

    


}