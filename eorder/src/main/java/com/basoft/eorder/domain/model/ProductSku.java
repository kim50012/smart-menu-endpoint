package com.basoft.eorder.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @Author:DongXifu
 * @author woonill
 * @Description:
 * @Date Created in 下午5:08 2018/12/4
 * @Date modify at 2018/12/12
 **/


public class ProductSku {



    public enum Status{
        NORMAL(0), OPEN(1), CLOSE(2);
        private final int code;

        Status(int typeCode){
            this.code = typeCode;
        }

        public static Status get(int code) {


            return Arrays.asList(ProductSku.Status.values())
                    .stream()
                    .filter(new Predicate<ProductSku.Status>() {
                        @Override
                        public boolean test(ProductSku.Status status) {
                            return status.code == code;
                        }
                    })
                    .findFirst()
                    .orElseGet(new Supplier<ProductSku.Status>() {
                        @Override
                        public ProductSku.Status get() {
                            return null;
                        }
                    });
        }

        public int getCode(){
            return code;
        }

    }

    public enum Type {
        NORMAL(0),OPERATION(1);

        private final int code;

        Type(int typeCode){
            this.code = typeCode;
        }

        public int getCode(){
            return code;
        }

    }

    private Long id;//
    private String name;
    private String chnName;
    private Product product;
    private Set<Standard> standardSet = new HashSet<>();
    private Type type;
    private BigDecimal weight;
    private BigDecimal salesPrice;//标准售价(原价)
    private BigDecimal priceWeekend;
    private BigDecimal priceSettle;
    private Status status = Status.OPEN;
    private Boolean useDefault;
    private int isInventory;
    private int disOrder;
    private String mainImageUrl;

    private List<ItemName> standardItemList;

    public Long id(){
        return id;
    }

    public BigDecimal weight() {
        return this.weight;
    }

    public BigDecimal price(){
        return this.salesPrice;
    }

    public BigDecimal priceWeekend() {
        return this.priceWeekend;
    }

    public BigDecimal priceSettle() {
        return  this.priceSettle;
    }


    public Type getType(){
        return type;
    }

    public Product product(){
        return this.product;
    }

    public String name() {
        return name;
    }

    public String chnName() {
        return this.chnName;
    }

    public Collection standarCollection(){
        return this.standardSet;
    }


    public Boolean useDefault(){
        return this.useDefault;
    }

    public int isInventory() {
        return this.isInventory;
    }

    public int disOrder() {
        return this.disOrder;
    }

    public String mainImageUrl() {
        return this.mainImageUrl;
    }

    public List<ItemName> standardItemList() {
       return this.standardItemList;
    }
    public ProductSku close() {

        this.status = Status.CLOSE;
        return this;
    }

    public Status status(){
        return this.status;
    }


    public boolean isSame(ProductSku product) {


        if(this.id.equals(product.id)){
            return true;
        }
        return this.standardSet.equals(product.standardSet);
    }



    @Override
    public int hashCode() {
        return this.id.hashCode();
    }



    @Override
    public boolean equals(Object obj) {

        if(this == obj){
            return true;
        }

        if(!(obj instanceof ProductSku)){
            return false;
        }

        ProductSku other = (ProductSku) obj;
        if(this.id.equals(other.id)){
            return true;
        }

        return standardSet.equals(other.standardSet);
    }


    @Data
    public static class ItemName {
        public ItemName() {

        }
        private int disOrder;
        private String itemNameChn;
        private String itemNameKor;
        private String itemNameEng;

        public static class Builder{

            private int disOrder;
            private String itemNameChn;
            private String itemNameKor;
            private String itemNameEng;

            public Builder disOrder(int disOrder){
                this.disOrder = disOrder;
                return this;
            }
            public Builder itemNameChn(String itemNameChn){
                this.itemNameChn = itemNameChn;
                return this;
            }
            public Builder itemNameKor(String itemNameKor){
                this.itemNameKor = itemNameKor;
                return this;
            }
            public Builder itemNameEng(String itemNameEng){
                this.itemNameEng = itemNameEng;
                return this;
            }

           public ItemName build(){
               ItemName itemName = new ItemName();
               itemName.disOrder = this.disOrder;
               itemName.itemNameChn = this.itemNameChn;
               itemName.itemNameKor = this.itemNameKor;
               itemName.itemNameEng = this.itemNameEng;
               return itemName;
           }

        }

    }


    public static class Builder {


        private Long id;//
        private Set<Standard> standardSet = new HashSet<>();
        private BigDecimal weight;
        private BigDecimal salesPrice;//标准售价(原价)
        private BigDecimal priceWeekend;//标准售价(原价)
        private BigDecimal priceSettle;//平台价
        private String name;
        private String chnName;
        private Status status;

        private Boolean useDefault;
        private int isInventory;
        private Long invTotal;
        private int disOrder;
        private String mainImageUrl;
        private List<ItemName> standardItemList;

        public Builder setId(Long id){
            this.id = id;
            return this;
        }

        public Builder setStandard(Standard standard){
            this.standardSet.add(standard);
            return this;
        }

        public Builder setStandards(Standard.Standars standard){
            this.standardSet.addAll(standard.toList());
            return this;
        }

        public Builder clearStandard(){
            this.standardSet.clear();
            return this;
        }

        public Builder setWeight(BigDecimal weight) {
            this.weight = weight;
            return this;
        }

        public Builder setPrice(BigDecimal price){
            this.salesPrice = price;
            return this;
        }

        public Builder setHotelPriceWeekend(BigDecimal priceWeekend){
            this.priceWeekend = priceWeekend;
            return this;
        }

        public Builder setPriceSettle(BigDecimal priceSettle) {
            this.priceSettle = priceSettle;
            return this;
        }


        public Builder setName(String name){
            this.name = name;
            return this;
        }


        public Builder setChnName(String chnName){
            this.chnName = chnName;
            return this;
        }

        public Builder setUseDefault(Boolean useDefault){
            this.useDefault = useDefault;
            return this;
        }

        public Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder isInventory(int isInventory) {
            this.isInventory = isInventory;
            return this;
        }
        public Builder invTotal(Long invTotal) {
            this.invTotal = invTotal;
            return this;
        }

        public Builder disOrder(int disOrder) {
            this.disOrder = disOrder;
            return this;
        }

        public Builder mainImageUrl(String mainImageUrl) {
            this.mainImageUrl = mainImageUrl;
            return this;
        }

        public Builder itemNames(List<ItemName> standardItemList) {
            this.standardItemList = standardItemList;
            return this;
        }

        public ProductSku build(Product product){


            if(this.id == null){
                throw new IllegalArgumentException("sku id null");
            }


            if(this.useDefault == null){
                throw new IllegalArgumentException("usedefault is null");
            }




            ProductSku sku = new ProductSku();
            sku.id = this.id;
            sku.name = this.name;
            sku.chnName = this.chnName;
            sku.weight = this.weight;
            sku.salesPrice = this.salesPrice;
            sku.priceWeekend = this.priceWeekend;
            sku.priceSettle = this.priceSettle;
            sku.product = product;
            sku.standardSet = this.standardSet;
            sku.useDefault = this.useDefault;
            sku.status = this.status == null ? Status.OPEN : this.status;
            sku.isInventory = this.isInventory;
            sku.disOrder = this.disOrder;
            sku.mainImageUrl = this.mainImageUrl;
            sku.standardItemList = this.standardItemList;
            return sku;
        }


    }
}
