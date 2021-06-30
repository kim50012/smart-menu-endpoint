package com.basoft.eorder.interfaces.query;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @ProjectName: intellij_work
 * @Package: com.basoft.eorder.interfaces.query
 * @ClassName: BannerDTO
 * @Description: TODO
 * @Author: liminzhe
 * @Date: 2018-12-13 13:35
 * @Version: 1.0
 */

public class BannerDTO {

    private Long id;		//
    private String name;		//
    private String imagePath;		//
    private String created;		//
    private Long storeId;		//
    private int showIndex;		//
    private int status;		//


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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public int getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class Builder {
        BannerDTO bd = new BannerDTO();

        private Long id;		//
        private String name;		//
        private String imagePath;		//
        private String created;		//
        private Long storeId;		//
        private int showIndex;		//
        private int status;		//

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder imagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public Builder created(String created) {
            this.created = created;
            return this;
        }

        public Builder storeId(Long storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder showIndex(int showIndex) {
            this.showIndex = showIndex;
            return this;
        }
        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public BannerDTO build() {
            BannerDTO banner = new BannerDTO();

            banner.id = this.id;
            banner.name = this.name;
            banner.imagePath = this.imagePath;
            banner.created = this.created;
            banner.storeId = this.storeId;
            banner.showIndex = this.showIndex;
            banner.status = this.status;

            return banner;
        }

    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
