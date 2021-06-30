package com.basoft.eorder.domain.model.inventory.hotel;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StoreDayPrice {

    public static int fatureNum = 189;//未来190天的价格

    private Long sdpId;

    private Long storeId;

    private Long prodId;

    private Long prodSkuId;

    private String invDate;

    private Long minPrice;

    private Long maxPrice;

    private Long avgPrice;

    private Long skuMaxPrice;

    private Long skuMinPrice;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    @Data
    public class futurePriceAll{
        private String invDate;
        private List<InventoryHotel> hotelList;
    }

    @Data
    public static final class Builder{
        private Long storeId;

        private Long prodId;

        private Long prodSkuId;

        private String invDate;

        private Long minPrice;

        private Long maxPrice;

        public Builder storeId(Long storeId) {
            this.storeId = storeId;
            return this;
        }
        public Builder prodId(Long prodId) {
            this.prodId = prodId;
            return this;
        }
        public Builder prodSkuId(Long prodSkuId) {
            this.prodSkuId = prodSkuId;
            return this;
        }
        public Builder invDate(String invDate) {
            this.invDate = invDate;
            return this;
        }
        public Builder minPrice(Long minPrice) {
            this.minPrice = minPrice;
            return this;
        }
        public Builder maxPrice(Long maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public StoreDayPrice build(){
            StoreDayPrice entity = new StoreDayPrice();
            entity.storeId = this.storeId;
            entity.prodId = this.prodId;
            entity.prodSkuId = this.prodSkuId;
            entity.invDate = this.invDate;
            entity.maxPrice = this.maxPrice;
            entity.minPrice = this.minPrice;
            return entity;
        }
    }






}