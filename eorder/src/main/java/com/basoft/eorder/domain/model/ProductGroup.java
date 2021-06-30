package com.basoft.eorder.domain.model;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class ProductGroup {




    public enum Status {

        NORMAL(0),OPEN(1),CLOSED(2),DELETE(3);
        private final int code;

        private Status(int code){
            this.code = code;
        }

        public int getCode(){
            return this.code;
        }


        public static final Status get(int code){


            return Arrays.asList(Status.values())
                    .stream()
                    .filter(new Predicate<Status>() {
                        @Override
                        public boolean test(Status status) {
                            return status.code == code;
                        }
                    })
                    .findFirst()
                    .orElseGet(new Supplier<Status>() {
                        @Override
                        public Status get() {
                            return null;
                        }
                    });

        }

    }


    private Long id;
    private String name;
    private String chnName;
    private int showIndex;
    private Long storeId;
    private Status status;

    private List<MenuItem> itemSet = new ArrayList<MenuItem>();

/*    public ProductGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }*/

    public String name(){
        return this.name;
    }

    public String chnName(){
        return this.chnName;
    }

    public int showIndex(){
        return this.showIndex;
    }

    public Long id() {
        return this.id;
    }


    public Long storeId(){
        return this.storeId;
    }


    public Status status(){
        return this.status;
    }


    public List<MenuItem> items(){
        return Collections.unmodifiableList(this.itemSet);
    }


    public static Builder newGroup(ProductGroup group) {
        return new Builder()
                .id(group.id)
                .showIndex(group.showIndex)
                .name(group.name)
                .items(group.itemSet)
                .storeId(group.storeId)

                .chnName(group.chnName);
    }

    public static final class Builder{

        private Long id;
        private String name;
        private String chnName;
        private int showIndex;
        private Status status;
        private Long storeId;
        private Set<MenuItem> items = new HashSet<>();


        public Builder storeId(Long id) {
            this.storeId = id;
            return this;
        }

        public Builder id(Long id){
            this.id = id;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }


        public Builder chnName(String chnName){
            this.chnName = chnName;
            return this;
        }


        public Builder showIndex(int showIndex) {
            this.showIndex = showIndex;
            return this;
        }


        public Builder item(MenuItem item){
            items.add(item);
            return this;
        }


        public ProductGroup build(){

            ProductGroup group = new ProductGroup();
            group.chnName = this.chnName;
            group.showIndex = this.showIndex;
            group.storeId = this.storeId;
            group.id = this.id;
            group.name = this.name;
            group.itemSet.addAll(this.items);

            group.status = (this.status == null) ? Status.NORMAL :this.status;
            return group;


        }

        public ProductGroup.Builder status(Status status) {
            this.status = status;
            return this;
        }


        public ProductGroup.Builder status(int statusCode) {
            this.status = Status.get(statusCode);
            return this;
        }

        public ProductGroup.Builder items(List<MenuItem> menuItemList) {

            if(menuItemList != null && !menuItemList.isEmpty()){
                this.items.addAll(menuItemList);
            }
            return this;
        }
    }


}
