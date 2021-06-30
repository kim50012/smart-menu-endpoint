package com.basoft.eorder.interfaces.query.retail.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 产品详情页面/产品订购页面：
 * （1）产品规格信息
 * （2）产品sku信息
 *
 * @author Mentor
 * @version 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetailProductInfoVO {
    private Long id;

    private List<ProductAloneStandardInfoVO> stdInfoList;

    private List<RetailProductSkuInfoVO> skuInfoList;
}
