package com.basoft.eorder.domain.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 为了 Mapping store 和 category 所生成的类
 *
 * @author woonill
 */


public class StoreCategory {
    private Long categoryId;

    private Long storeId;

    private int functionType;

    private int manageType;

    public StoreCategory(Long aLong, Long id, int functionType, int manageType) {
        this.categoryId = aLong;
        this.storeId = id;
        this.functionType = functionType;
        this.manageType = manageType;
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

    public int getFunctionType() {
        return functionType;
    }

    public void setFunctionType(int functionType) {
        this.functionType = functionType;
    }

    public int getManageType() {
        return manageType;
    }

    public void setManageType(int manageType) {
        this.manageType = manageType;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.categoryId.hashCode()).append(this.storeId.hashCode()).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof StoreCategory)) {
            return false;
        }

        StoreCategory other = (StoreCategory) obj;
        if (this.categoryId.equals(other.categoryId)
                && this.storeId.equals(other.storeId)) {
            return true;
        }
        return super.equals(obj);
    }
}
