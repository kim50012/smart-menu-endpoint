package com.basoft.eorder.interfaces.query.retail.api;

import com.basoft.eorder.interfaces.query.ProductImageDTO;
import com.basoft.eorder.interfaces.query.retail.cms.ProductAloneStandardDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetailProductDTO {
    private Long id;

    private String nameKor;

    private String nameChn;

    private BigDecimal weight;

    private String detailDesc;

    private String detailChnDesc;

    private Long categoryId;

    private String categoryName;

    private int status;

    private Long storeId;

    private String storeName;

    private Long defaultSkuId;

    private BigDecimal defaultSkuPriceKor;

    private BigDecimal defaultSkuPriceChn;

    private BigDecimal defaultSkuDiscPriceKor;

    private BigDecimal defaultSkuDiscPriceChn;

    private int defaultSkuIsInv;

    private Long defaultSkuInv;

    private Long skuCount;

    private int isStandard;

    private int isInventory;

    private String created;

    private String updateTime;

    private String desChn;

    private String desKor;

    private Integer recommend;

    private Boolean useDefault;

    private String fileId;

    private String fileName;

    private String fileSysName;

    private String fileType;

    private int fileSize;

    private String fileUrl;

    private String fileOriginalName;

    private String mainImageUrl;

    private String subImageUrl;

    private List<ProductImageDTO> imageList;

    private List<RetailProductSkuDTO> skuList;

    private List<ProductAloneStandardDTO> stdList;
	
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
