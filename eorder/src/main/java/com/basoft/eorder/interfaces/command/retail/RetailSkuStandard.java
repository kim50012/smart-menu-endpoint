package com.basoft.eorder.interfaces.command.retail;

import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.domain.model.Product;
import com.basoft.eorder.domain.model.ProductSku;
import com.basoft.eorder.domain.model.retail.InventoryRetail;
import com.basoft.eorder.domain.model.retail.ProductAloneStandard;
import com.basoft.eorder.domain.model.retail.ProductSkuAloneStandard;

import lombok.Data;

import java.util.List;

/**
 * retail产品所需要的数据组合
 */
@Data
public class RetailSkuStandard {
    private Category category;
    private Product product;
    private List<ProductSku> productSkus;
    private List<ProductAloneStandard> standards;
    private List<ProductSkuAloneStandard> productSkuStandards;
    private List<InventoryRetail> inventoryRetails;

    public RetailSkuStandard() {
    }

    public RetailSkuStandard(Category category,Product product,List<ProductSku> productSkus
            ,List<ProductAloneStandard> standards
            ,List<ProductSkuAloneStandard> productSkuStandards
            ,List<InventoryRetail> inventoryRetails){
        this.category = category;
        this.product = product;
        this.productSkus = productSkus;
        this.standards = standards;
        this.productSkuStandards = productSkuStandards;
        this.inventoryRetails = inventoryRetails;
    }



}
