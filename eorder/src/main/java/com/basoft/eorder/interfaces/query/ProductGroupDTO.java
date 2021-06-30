package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.util.BeanUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Map;

public class ProductGroupDTO {


    private Long id;
//    private Long categoryId;
    private String categoryName;
    private String nameChn;
    private String nameKor;
    private Long storeId;
    private String storeName;
    private String areaNm;
    private int status;
    private int showIndex;
    private String created;
    private String updateTime;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameChn() {
        return nameChn;
    }

    public void setNameChn(String nameChn) {
        this.nameChn = nameChn;
    }

    public String getNameKor() {
        return nameKor;
    }

    public void setNameKor(String nameKor) {
        this.nameKor = nameKor;
    }

    public Long getStoreId() {
        return storeId;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }


    public String getAreaNm() {
        return areaNm;
    }

    public void setAreaNm(String areaNm) {
        this.areaNm = areaNm;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getShowIndex() { return showIndex; }

    public void setShowIndex(int showIndex) { this.showIndex = showIndex; }


    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }



    public static ProductGroupDTO hotOf() {
        return null;
    }

    public Map toMap() {
        return BeanUtil.apacheObjectToMap(this);
    }
}
