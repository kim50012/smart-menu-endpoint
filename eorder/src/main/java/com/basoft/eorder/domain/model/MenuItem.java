package com.basoft.eorder.domain.model;

public class MenuItem {


    private Long productId;
    private Long categoryId;
    private Integer index;


    public MenuItem(Long productId, Long id, Integer showIndex) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.index = showIndex;

    }

    public Long productId() {
        return this.productId;

    }

    public Long categoryId(){
        return this.categoryId;
    }

    public Integer showIndex(){
        return this.index;
    }
}
