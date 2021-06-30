package com.basoft.eorder.interfaces.query;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ProductGroupMapDTO extends ProductDTO {

    private Long productId;
    private Long prdGroupId;
    private int showIndex;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public Long getPrdGroupId() {
        return prdGroupId;
    }

    @Override
    public void setPrdGroupId(Long prdGroupId) {
        this.prdGroupId = prdGroupId;
    }

    @Override
    public int getShowIndex() {
        return showIndex;
    }

    @Override
    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
