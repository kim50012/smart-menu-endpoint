package com.basoft.eorder.interfaces.query;

import java.util.List;
import java.util.Map;

public interface ProductQuery {
    ProductDTO getProductById(Long id);

    int getProductCountByMap(Map<String, Object> param);

    List<ProductDTO> getProductListByMap(Map<String, Object> param);

    ProductSkuDTO getProductSkuById(Long id);

    List<ProductSkuDTO> getProductSkuByProId(Long productId);

    int getProductSkuCountByMap(Map<String, Object> param);

    List<ProductSkuDTO> getProductSkuListByMap(Map<String, Object> param);

    List<ProductGroupDTO> getProductGroupListByMap(Map<String, Object> param);

    int getproductGroupCount(Map<String, Object> param);

    List<ProductImageDTO> getProductImageListByMap(Map<String, Object> param);

    ExchangeRateDTO getNowExchangeRate();

    List<MenuItemDTO> getMenuListOfGroup(Long storeId, Long groupId);

    List<ProductGroupMapDTO> getProductGroupMapListByMap(Map<String, Object> param);

    /**
     * 对wechat H5端店铺列表中的商品最低韩币价格转为人民币
     *
     * @param minPriceKorList
     */
    void currencyConverter(List<Map> minPriceKorList);
}
