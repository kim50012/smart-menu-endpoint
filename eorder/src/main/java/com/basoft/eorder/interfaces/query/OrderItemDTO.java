package com.basoft.eorder.interfaces.query;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;

public class OrderItemDTO {

    private Long id;		//
    private Long orderId;		//
    private Long skuId;		//
    private String skuNmKor;
    private String skuNmChn;
    private BigDecimal price;		//
    private BigDecimal priceCny;
    private int qty;		//
    private String created;		//
    private String updated;		//

    private String prodUrl;
    private String prodNmKor;
    private String prodNmChn;
    private BigDecimal weight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuNmKor() {
        return skuNmKor;
    }

    public void setSkuNmKor(String skuNmKor) {
        this.skuNmKor = skuNmKor;
    }

    public String getSkuNmChn() {
        return skuNmChn;
    }

    public void setSkuNmChn(String skuNmChn) {
        this.skuNmChn = skuNmChn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceCny() {
        return priceCny;
    }

    public void setPriceCny(BigDecimal priceCny) {
        this.priceCny = priceCny;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getProdUrl() {
        return prodUrl;
    }

    public void setProdUrl(String prodUrl) {
        this.prodUrl = prodUrl;
    }

    public String getProdNmKor() {
        return prodNmKor;
    }

    public void setProdNmKor(String prodNmKor) {
        this.prodNmKor = prodNmKor;
    }

    public String getProdNmChn() {
        return prodNmChn;
    }

    public void setProdNmChn(String prodNmChn) {
        this.prodNmChn = prodNmChn;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
