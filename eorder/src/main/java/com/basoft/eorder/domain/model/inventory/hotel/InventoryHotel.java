package com.basoft.eorder.domain.model.inventory.hotel;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class InventoryHotel {

    //columns START
    private Long invId;  //invId  db_column: INV_ID

    private Long storeId;  //酒店ID  db_column: STORE_ID

    private Long prodId;  //酒店库存对应的产品  db_column: PROD_ID

    private Long prodSkuId;  //酒店库存对应的产品SKU  db_column: PROD_SKU_ID

    private BigDecimal price;//价格

    private BigDecimal disPrice;//折扣价格

    private BigDecimal priceWeekend;//周末价

    private BigDecimal priceSettle;//产品库存的到手价格

    private BigDecimal disPriceSettle;//产品库存的到手折扣价格

    private Integer invYear;  //库存年  db_column: INV_YEAR

    private Integer invMonth;  //库存月  db_column: INV_MONTH

    private Integer invDay;  //库存日  db_column: INV_DAY

    private String invDate;  //库存日期  db_column: INV_DATE

    private Integer isOpening;  //当前日期是否开放该房间，默认值为1 1-开放 0-关闭  db_column: IS_OPENING

    private Integer invTotal;  //当前日期的库存总量，可修改，设定值要大于已订购数量。  db_column: INV_TOTAL

    private Integer invUsed;  //当前日期的被订购总量，小于等于库存总量。  db_column: INV_USED

    private Integer invBalance;  //当前日期的库存余量。由于库存总量可以随时被修改，所以此字段无参考意义  db_column: INV_BALANCE

    private Date createTime;  //创建时间생성시간  db_column: CREATE_TIME

    private String createUser;  //创建人  db_column: CREATE_USER

    private Date updateTime;  //修改时间수정시간  db_column: UPDATE_TIME

    private String updateUser;  //修改人  db_column: UPDATE_USER

    //columns END

    public static final class Builder {
        //columns START
        private Long invId;  //invId  db_column: INV_ID

        private Long storeId;  //酒店ID  db_column: STORE_ID

        private Long prodId;  //酒店库存对应的产品  db_column: PROD_ID

        private Long prodSkuId;  //酒店库存对应的产品SKU  db_column: PROD_SKU_ID

        private BigDecimal price;

        private BigDecimal disPrice;

        private BigDecimal priceSettle;//产品库存的到手价格

        private BigDecimal disPriceSettle;//产品库存的到手折扣价格

        private Integer invYear;  //库存年  db_column: INV_YEAR

        private Integer invMonth;  //库存月  db_column: INV_MONTH

        private Integer invDay;  //库存日  db_column: INV_DAY

        private String invDate;  //库存日期  db_column: INV_DATE

        private Integer isOpening;  //当前日期是否开放该房间，默认值为1 1-开放 0-关闭  db_column: IS_OPENING

        private Integer invTotal;  //当前日期的库存总量，可修改，设定值要大于已订购数量。  db_column: INV_TOTAL

        private Integer invUsed;  //当前日期的被订购总量，小于等于库存总量。  db_column: INV_USED

        private Integer invBalance;  //当前日期的库存余量。由于库存总量可以随时被修改，所以此字段无参考意义  db_column: INV_BALANCE

        private Date createTime;  //创建时间생성시간  db_column: CREATE_TIME

        private String createUser;  //创建人  db_column: CREATE_USER

        private Date updateTime;  //修改时间수정시간  db_column: UPDATE_TIME

        private String updateUser;  //修改人  db_column: UPDATE_USER

        //columns END
        public Builder invId(Long invId) {
            this.invId = invId;
            return this;
        }

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
        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }
        public Builder disPrice(BigDecimal disPrice) {
            this.disPrice = disPrice;
            return this;
        }

        public Builder priceSettle(BigDecimal priceSettle) {
            this.priceSettle = priceSettle;
            return this;
        }

        public Builder disPriceSettle(BigDecimal disPriceSettle){
            this.disPriceSettle = disPriceSettle;
            return this;
        }

        public Builder invYear(Integer invYear) {
            this.invYear = invYear;
            return this;
        }

        public Builder invMonth(Integer invMonth) {
            this.invMonth = invMonth;
            return this;
        }

        public Builder invDay(Integer invDay) {
            this.invDay = invDay;
            return this;
        }

        public Builder invDate(String invDate) {
            this.invDate = invDate;
            return this;
        }

        public Builder isOpening(Integer isOpening) {
            this.isOpening = isOpening;
            return this;
        }

        public Builder invTotal(Integer invTotal) {
            this.invTotal = invTotal;
            return this;
        }

        public Builder invUsed(Integer invUsed) {
            this.invUsed = invUsed;
            return this;
        }

        public Builder invBalance(Integer invBalance) {
            this.invBalance = invBalance;
            return this;
        }

        public Builder createTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder createUser(String createUser) {
            this.createUser = createUser;
            return this;
        }

        public Builder updateTime(Date updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder updateUser(String updateUser) {
            this.updateUser = updateUser;
            return this;
        }

        public InventoryHotel build() {
            InventoryHotel inventoryHotel = new InventoryHotel();
            inventoryHotel.invId = this.invId;
            inventoryHotel.storeId = this.storeId;
            inventoryHotel.prodId = this.prodId;
            inventoryHotel.prodSkuId = this.prodSkuId;
            inventoryHotel.price = price;
            inventoryHotel.disPrice = disPrice;
            inventoryHotel.priceSettle = priceSettle;
            inventoryHotel.disPriceSettle = disPriceSettle;
            inventoryHotel.invYear = this.invYear;
            inventoryHotel.invMonth = this.invMonth;
            inventoryHotel.invDay = this.invDay;
            inventoryHotel.invDate = this.invDate;
            inventoryHotel.isOpening = this.isOpening;
            inventoryHotel.invTotal = this.invTotal;
            inventoryHotel.invUsed = this.invUsed;
            inventoryHotel.invBalance = this.invBalance;
            inventoryHotel.createTime = this.createTime;
            inventoryHotel.createUser = this.createUser;
            inventoryHotel.updateTime = this.updateTime;
            inventoryHotel.updateUser = this.updateUser;
            return inventoryHotel;
        }
    }
}


