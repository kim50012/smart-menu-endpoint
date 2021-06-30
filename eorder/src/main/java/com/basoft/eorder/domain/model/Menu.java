package com.basoft.eorder.domain.model;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class Menu {


    private Set<ProductGroup> groups = null;
    private List<Product> products = null;

    public Set<ProductGroup> productGroups(){
        return groups;
    }

    public List<Product> productList(){
        return products;
    }


    public ProductGroup getProductGroup(Long categoryId) {
        return this.groups.stream()
                .filter((ProductGroup group) -> {return group.id().equals(categoryId);})
                .findFirst().orElseGet(new Supplier<ProductGroup>() {
                    @Override
                    public ProductGroup get() {
                        return null;
                    }
                });
    }
}
