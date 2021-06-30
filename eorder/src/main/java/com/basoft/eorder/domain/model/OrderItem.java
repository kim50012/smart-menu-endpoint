package com.basoft.eorder.domain.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;

public class OrderItem {
    private Long id;

    private Long orderId;

    private Long skuId;

    private String skuNmKor;

    private String skuNmChn;

    private BigDecimal qty;

    private BigDecimal price;

    private BigDecimal priceSettle;

    // 20200421 零售商户下单时，item对应的活动ID
    private Long discId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getSkuId() {
        return skuId;
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

    public BigDecimal getQty() {
        return qty;
    }

    public BigDecimal getPriceSettle() {
        return priceSettle;
    }

    public void setPriceSettle(BigDecimal priceSettle) {
        this.priceSettle = priceSettle;
    }

    public Long getDiscId() {
        return discId;
    }

    public void setDiscId(Long discId) {
        this.discId = discId;
    }

    public static final class Builder {
        private Long id;

        private Long orderId;

        private Long skuId;

        private String skuNmKor;

        private String skuNmChn;

        private BigDecimal qty;

        private BigDecimal price;

        private BigDecimal priceSettle;

        // 20200421 零售商户下单时，item对应的活动ID
        private Long discId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder skuId(Long skuId) {
            this.skuId = skuId;
            return this;
        }

        public Builder skuNmKor(String skuNmKor) {
            this.skuNmKor = skuNmKor;
            return this;
        }

        public Builder skuNmChn(String skuNmChn) {
            this.skuNmChn = skuNmChn;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder qty(BigDecimal qty) {
            this.qty = qty;
            return this;
        }

        public Builder priceSettle(BigDecimal priceSettle) {
            this.priceSettle = priceSettle;
            return this;
        }

        public Builder discId(Long discId) {
            this.discId = discId;
            return this;
        }

        public OrderItem build() {
            OrderItem oi = new OrderItem();
            oi.id = this.id;
            oi.orderId = this.orderId;
            oi.skuId = this.skuId;
            oi.skuNmKor = this.skuNmKor;
            oi.skuNmChn = this.skuNmChn;
            oi.price = this.price;
            oi.qty = this.qty;
            oi.priceSettle = this.priceSettle;
            oi.discId = this.discId;
            return oi;
        }
    }

    public Builder createNewOrderItem(Long id, Long orderId, OrderItem oi) {
        return new Builder()
                .id(id)
                .orderId(orderId)
                .skuId(oi.skuId)
                .skuNmKor(oi.skuNmKor)
                .skuNmChn(oi.skuNmChn)
                .price(oi.price)
                .priceSettle(oi.priceSettle)
                .qty(oi.qty)
                .discId(oi.discId);
    }

    public Builder setSkuNm(String skuNm, OrderItem oi) {
        return new Builder()
                .id(oi.id)
                .orderId(oi.orderId)
                .skuId(oi.skuId)
                .skuNmKor(oi.skuNmKor)
                .skuNmChn(oi.skuNmChn)
                .price(oi.price)
                .priceSettle(oi.priceSettle)
                .qty(oi.qty)
                .discId(oi.discId);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}