/*
package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.ProductGroup;

public class OpenStoreProductGroup implements Command {


    private Long productGroupId;
    private int showIndex;
    private String showName;

    public Long getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(Long productGroupId) {
        this.productGroupId = productGroupId;
    }

    public int getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public ProductGroup build(ProductGroup group) {

        return ProductGroup.newGroup(group)
                .name(this.showName)
                .showIndex(this.showIndex)
                .build();
    }
}
*/
