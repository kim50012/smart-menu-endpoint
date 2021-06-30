package com.basoft.eorder.domain.model.activity.discount;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * Domain：折扣明细DiscountDetail
 *
 * @author Mentor
 * @version 1.0
 * @since 20190515
 */
public class DiscountDetail {
    // 折扣活动的明细ID
    private Long discDetId;

    // 折扣活动
    private Long discId;

    // 活动产品
    private Long prodId;

    // 活动产品规格
    private Long prodSkuId;

    // 所属客户
    private Long custId;

    // 店铺
    private Long storeId;

    // 活动折扣后价格
    private BigDecimal discPrice;

    // 创建时间
    private String createdDt;

    // 创建人
    private String createdUserId;

    public Long getDiscDetId() {
        return discDetId;
    }

    public void setDiscDetId(Long discDetId) {
        this.discDetId = discDetId;
    }

    public Long getDiscId() {
        return discId;
    }

    public void setDiscId(Long discId) {
        this.discId = discId;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Long getProdSkuId() {
        return prodSkuId;
    }

    public void setProdSkuId(Long prodSkuId) {
        this.prodSkuId = prodSkuId;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public BigDecimal getDiscPrice() {
        return discPrice;
    }

    public void setDiscPrice(BigDecimal discPrice) {
        this.discPrice = discPrice;
    }

    public String getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(String createdDt) {
        this.createdDt = createdDt;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public static final class Builder {
        // 折扣活动的明细ID
        private Long discDetId;

        // 折扣活动
        private Long discId;

        // 活动产品
        private Long prodId;

        // 活动产品规格
        private Long prodSkuId;

        // 所属客户
        private Long custId;

        // 店铺
        private Long storeId;

        // 活动折扣后价格
        private BigDecimal discPrice;

        // 创建时间
        private String createdDt;

        // 创建人
        private String createdUserId;

        public Builder setDiscDetId(Long discDetId) {
            this.discDetId = discDetId;
            return this;
        }

        public Builder setDiscId(Long discId) {
            this.discId = discId;
            return this;
        }

        public Builder setProdId(Long prodId) {
            this.prodId = prodId;
            return this;
        }

        public Builder setProdSkuId(Long prodSkuId) {
            this.prodSkuId = prodSkuId;
            return this;
        }

        public Builder setCustId(Long custId) {
            this.custId = custId;
            return this;
        }

        public Builder setStoreId(Long storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder setDiscPrice(BigDecimal discPrice) {
            this.discPrice = discPrice;
            return this;
        }

        public Builder setCreatedDt(String createdDt) {
            this.createdDt = createdDt;
            return this;
        }

        public Builder setCreatedUserId(String createdUserId) {
            this.createdUserId = createdUserId;
            return this;
        }

        public DiscountDetail build() {
            DiscountDetail discountDetail = new DiscountDetail();
            discountDetail.discDetId = this.discDetId;
            discountDetail.discId = this.discId;
            discountDetail.prodId = this.prodId;
            discountDetail.prodSkuId = this.prodSkuId;
            discountDetail.custId = this.custId == null ? 0L : this.custId;
            discountDetail.storeId = this.storeId;
            discountDetail.discPrice = this.discPrice;
            discountDetail.createdDt = this.createdDt;
            discountDetail.createdUserId = this.createdUserId;
            return discountDetail;
        }

        public DiscountDetail build(Discount discount) {
            DiscountDetail discountDetail = new DiscountDetail();
            discountDetail.discDetId = this.discDetId;
            discountDetail.discId = discount.getDiscId();
            discountDetail.prodId = this.prodId;
            discountDetail.prodSkuId = this.prodSkuId;
            discountDetail.custId = discount.getCustId() == null ? 0L : discount.getCustId();
            discountDetail.storeId = discount.getStoreId();
            discountDetail.discPrice = this.discPrice;
            discountDetail.createdDt = this.createdDt;
            discountDetail.createdUserId = this.createdUserId;
            return discountDetail;
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("discDetId", discDetId)
                .append("discId", discId)
                .append("prodId", prodId)
                .append("prodSkuId", prodSkuId)
                .append("custId", custId)
                .append("storeId", storeId)
                .append("discPrice", discPrice)
                .append("createdDt", createdDt)
                .append("createdUserId", createdUserId)
                .toString();
    }
}