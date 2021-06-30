package com.basoft.eorder.domain.retail;

import com.basoft.eorder.domain.model.Product;
import com.basoft.eorder.domain.model.ProductSku;

import com.basoft.eorder.domain.model.retail.ProductAloneStandard;
import com.basoft.eorder.domain.model.retail.ProductAloneStandardItem;
import com.basoft.eorder.interfaces.command.retail.RetailSkuStandard;

import java.util.List;

/**
 * 商户零售产品规格表(ProductAloneStandard)表数据库访问层
 *
 * @author DongXifu
 * @since 2020-04-02 18:50:50
 */
public interface RetailRepository {

    //新增或修改standar item  规格standar关系
    int  saveProductStandards(RetailSkuStandard poting);

    Long updateProduct(Product product, List<ProductSku> productSkus);

    //修改商户零售产品规格表状态
    Long  updateProductAloneStandardStatus(ProductAloneStandard productAloneStandard);

    //修改商户零售产品规格项目表
    Long  updateProductAloneStandardItem (ProductAloneStandardItem productAloneStandardItem);

    //修改商户零售产品规格项目表状态
    Long  updateProductAloneStandardItemStatus(ProductAloneStandardItem productAloneStandardItem);

}
