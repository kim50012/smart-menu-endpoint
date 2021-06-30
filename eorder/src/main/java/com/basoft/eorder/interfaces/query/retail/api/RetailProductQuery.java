package com.basoft.eorder.interfaces.query.retail.api;

import java.util.List;
import java.util.Map;

/**
 * 零售业务  for H5
 * 查询商户产品
 *
 * @author Mentor
 * @version 1.0
 */
public interface RetailProductQuery {
    /**
     * 零售商户：查询产品列表（产品信息，产品规格定义，产品SKU列表及详细信息，产品SKU库存信息）
     *
     * @param param
     * @return
     */
    List<RetailProductGroupMapDTO> getRetailProductGroupMapListByStoreId(Map<String, Object> param);

    /**
     * 根据产品ID查询产品规格定义信息
     *
     * @param productId
     * @return
     */
    List<ProductAloneStandardInfoVO> getRetailProductAloneStandardInfoList(Long productId);

    /**
     * 根据产品ID查询产品信息
     *
     * @param productId
     * @return
     */
    List<RetailProductSkuInfoVO> getRetailProductInfoList(long productId);
}
