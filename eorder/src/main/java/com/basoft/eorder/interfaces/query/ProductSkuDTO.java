package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.interfaces.query.retail.cms.ProSkuItemNameDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * @ProjectName: intellij_work
 * @Package: com.basoft.eorder.interfaces.query
 * @ClassName: ProductSkuDTO
 * @Description:
 * @Author: liminzhe
 * @Date: 2018-12-17 13:27
 * @Version: 1.0
 */
public class ProductSkuDTO {
    private Long id;
    private String name;
    private String nameKor;
    private String nameChn;
    private BigDecimal weight;
    private BigDecimal priceKor;//对应表product_sku中price
    private BigDecimal priceChn;
    private BigDecimal priceSettle;//对应表product_sku中PRICE_SETTLE
    private BigDecimal priceWeekend;
    private Boolean useDefault;
    private Long productId;
    private String created;
    private Long categoryId;
    private Long storeId;
    private List<SkuStandardMapDTO> optionList = new LinkedList<>();
    private ExchangeRateDTO erd;

    //折扣相关
    private BigDecimal discPriceKor;
    private BigDecimal discPriceChn;
    private Long discId;
    private List<ProSkuItemNameDTO> standardItemList;
    private int isInventory;
    private Long invTotal;
    private int disOrder;
    private String mainImageUrl;
    private int status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameKor() {
        return nameKor;
    }

    public void setNameKor(String nameKor) {
        this.nameKor = nameKor;
    }

    public String getNameChn() {
        return nameChn;
    }

    public void setNameChn(String nameChn) {
        this.nameChn = nameChn;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getPriceKor() {
        return priceKor;
    }

    public void setPriceKor(BigDecimal priceKor) {
        this.priceKor = priceKor;
    }

    public BigDecimal getPriceChn() {
        return priceChn;
    }

    public void setPriceChn(BigDecimal priceChn) {
        this.priceChn = priceChn;
    }

    public BigDecimal getPriceWeekend() {
        return priceWeekend;
    }

    public void setPriceWeekend(BigDecimal priceWeekend) {
        this.priceWeekend = priceWeekend;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Boolean getUseDefault() {
        return useDefault;
    }

    public void setUseDefault(Boolean useDefault) {
        this.useDefault = useDefault;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public ExchangeRateDTO getErd() {
        return erd;
    }

    public void setErd(ExchangeRateDTO erd) {
        this.erd = erd;
    }

    public List<SkuStandardMapDTO> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<SkuStandardMapDTO> optionList) {
        this.optionList = optionList;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public void addStandardMap(SkuStandardMapDTO map) {
        this.optionList.add(map);
    }

    public BigDecimal getDiscPriceKor() {
        return discPriceKor;
    }

    public void setDiscPriceKor(BigDecimal discPriceKor) {
        this.discPriceKor = discPriceKor;
    }

    public BigDecimal getDiscPriceChn() {
        return discPriceChn;
    }

    public void setDiscPriceChn(BigDecimal discPriceChn) {
        this.discPriceChn = discPriceChn;
    }

    public Long getDiscId() {
        return discId;
    }

    public void setDiscId(Long discId) {
        this.discId = discId;
    }

    public BigDecimal getPriceSettle() {
        return priceSettle;
    }

    public void setPriceSettle(BigDecimal priceSettle) {
        this.priceSettle = priceSettle;
    }

    public List<ProSkuItemNameDTO> getStandardItemList() {
        return standardItemList;
    }

    public void setStandardItemList(List<ProSkuItemNameDTO> standardItemList) {
        this.standardItemList = standardItemList;
    }

    public int getIsInventory() {
        return isInventory;
    }

    public void setIsInventory(int isInventory) {
        this.isInventory = isInventory;
    }

    public Long getInvTotal() {
        return invTotal;
    }

    public void setInvTotal(Long invTotal) {
        this.invTotal = invTotal;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public int getDisOrder() {
        return disOrder;
    }

    public void setDisOrder(int disOrder) {
        this.disOrder = disOrder;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
