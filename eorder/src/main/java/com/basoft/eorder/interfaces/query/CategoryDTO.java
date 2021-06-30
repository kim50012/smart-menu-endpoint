package com.basoft.eorder.interfaces.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

/**
 * @ProjectName: intellij_work
 * @Package: com.basoft.eorder.interfaces.query
 * @ClassName: CategoryDTO
 * @Description: TODO
 * @Author: liminzhe
 * @Date: 2018-12-17 10:25
 * @Version: 1.0
 */

public class CategoryDTO {

    private String createdDt;		//

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;		//
    private String nameKor;		//
    private String nameChn;
    private int showIndex;		//
    private int status;		//0:不使用;1：使用中;2：删除
    private Long storeId;		//

    private List<ProductDTO> productDTOList;

    public String getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(String createdDt) {
        this.createdDt = createdDt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameKor() {
        return nameKor;
    }

    public void setNameKor(String nameKor) {
        this.nameKor = nameKor;
    }

    public String getNameChn() {
        return nameChn;
    }

    public void setNameChn(String nameChn) {
        this.nameChn = nameChn;
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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public List<ProductDTO> getProductDTOList() { return productDTOList; }

    public void setProductDTOList(List<ProductDTO> productDTOList) { this.productDTOList = productDTOList; }

    public static class Builder {
        private String createdDt;		//
        private Long id;		//
        private String nameKor;		//
        private String nameChn;
        private int showIndex;		//
        private int status;		//0:不使用;1：使用中;2：删除
        private Long storeId;		//

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder nameKor(String nameKor) {
            this.nameKor = nameKor;
            return this;
        }

        public Builder nameChn(String nameChn) {
            this.nameChn = nameChn;
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

        public Builder storeId(Long storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder createdDt(String createdDt) {
            this.createdDt = createdDt;
            return this;
        }

        public CategoryDTO build() {
            CategoryDTO cd = new CategoryDTO();

            cd.id = this.id;
            cd.nameKor = this.nameKor;
            cd.nameChn = this.nameChn;
            cd.showIndex = this.showIndex;
            cd.status = this.status;
            cd.storeId = this.storeId;
            cd.createdDt = this.createdDt;

            return cd;
        }
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
