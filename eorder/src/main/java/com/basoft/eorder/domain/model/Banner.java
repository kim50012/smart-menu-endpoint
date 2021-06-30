package com.basoft.eorder.domain.model;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:34 2018/12/4
 **/
public class Banner {

    private Long id;
    private Long storeId;
    private String name;
    private String imageUrl;
    private int order;
    private int status;



    public enum Status{
        NORMAL(0),OPEN(1),CLOSED(2),DELETED(3);
        private int code;

        Status(int code){
            this.code = code;
        }
        /*private Status(int code){
            code = code;
        }*/

        public int code(){
            return this.code;
        }

        public static Status valueOf(Integer value) {
            for (Status e : Status.values()) {
                if (e.code() == value) {
                    return e;
                }
            }
            return null;
        }
    }


/*
    private Byte isDelete;
    private String createdId;
    private Date createdDt;
    private String modifiedId;
    private Date modifiedDt;
*/


    public Long id() {
        return id;
    }

    public Long storeId() {
        return storeId;
    }

    public String name() {
        return name;
    }

    public String imageUrl() {
        return imageUrl;
    }

    public int order(){
        return this.order;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getStatus(){
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static final class Builder{
        private Long id;
        private Long storeId;
        private String name;
        private String imageUrl;
        private int order;
        private int status;


        public Builder setId(Long id) {
            this.id = id;
            return this;
        }
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setStoreId(Long storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder setOrder(int order) {
            this.order = order;
            return this;
        }

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }


        public Banner build(){
            Banner banner = new Banner();
            banner.id = this.id;
            banner.storeId = this.storeId;
            banner.name = name;
            banner.imageUrl = this.imageUrl;
            banner.order = this.order;
            banner.status = this.status;
            return banner;
        }
    }
}
