package com.basoft.eorder.domain;

import com.basoft.eorder.domain.model.Product;
import com.basoft.eorder.domain.model.ProductGroup;
import com.basoft.eorder.domain.model.ProductSku;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.interfaces.command.UpadateProductName;
import com.basoft.eorder.interfaces.command.UpdateProGroupStatus;
import com.basoft.eorder.interfaces.command.UpdateProSkuStatus;
import com.basoft.eorder.interfaces.command.UpdateProductStatus;

import java.util.List;

public interface ProductRepository {
    Product getProduct(Store store, Long id);

    List<Product> getProductListOfStore(Store store);

    Product saveProduct(Product sku, List<ProductSku> skus);

    void updateProduct(Product sku, List<ProductSku> skus);

    void deleteProduct(Long code);

    List<ProductSku> getProductSkuList(Product product);

    // List<ProductDTO> getProductCountByGroup(Map<String,Object> param, UpdateProGroupStatus proGroupStatus);

    Long saveProductGroup(ProductGroup newGroup);

    Long updateProductGroup(ProductGroup newGroup);

    Long updateProductGroupName(ProductGroup newGroup);

    // ProductGroup getProdcutGroup(Map<String,Object> param);

    int updateProductGroupStatus(UpdateProGroupStatus proGroupStatus, Long storeId);

    List<ProductGroup> getGroupOfStore(Store store);

    Long updateProductNAME(UpadateProductName upadateProductName);

    int updateProductStatus(UpdateProductStatus productStatus, Long storeId);

    int saveProductSkuImage(List<ProductSku> productSkus);

    Long updateProSkuStatus(UpdateProSkuStatus proSkuStatus);
}
