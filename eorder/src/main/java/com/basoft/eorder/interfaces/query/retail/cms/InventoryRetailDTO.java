package com.basoft.eorder.interfaces.query.retail.cms;

import com.basoft.eorder.interfaces.query.ProductImageDTO;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class InventoryRetailDTO {
    private Long invId;

    private Long storeId; //零售商户ID

    private Long prodId; //零售商户产品

    private Long prodSkuId; //零售商户产品SKU

    private String nameKor;

    private String nameChn;

    private Long invTotal; //库存总量，一旦设置不可修改，持久性的由增加量或减少量决定该值

    private Long invSold; //库存情况下的销售总量，小于等于库存总量。

    private Long invBalance; //库存余量。零售库存关键字段，可以被调整增加，调整减少。调整时同步调整INV_TOTAL

    private int isInventory;//是否使用库存

    private int isStandard;//是否使用规格

    private int status;

    private List<ProductImageDTO> imageList = new LinkedList<>();

    private String mainUrl;

    List<StandardAndItemDTO> standardAndItems;

    public String getMainUrl() {
        if (imageList != null && imageList.size() > 0) {
            return imageList.stream().filter((img) -> img.getImageType() == 1).findFirst().map(ProductImageDTO::getImageUrl).get();
        }
        return mainUrl;
    }
}