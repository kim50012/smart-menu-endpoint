/*
package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.events.UpdateProudctEvent;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BachUpdateProductName implements Command {


    private List<UpdateProductWrap> productList;

    public List<UpdateProductWrap> getProductList() {
        return productList;
    }

    public void setProductList(List<UpdateProductWrap> productList) {
        this.productList = productList;
    }

    public List<UpdateProudctEvent> build() {

        return this.productList.stream()
                .map(new Function<UpdateProductWrap, UpdateProudctEvent>() {
                    @Override
                    public UpdateProudctEvent apply(UpdateProductWrap updateProductWrap) {
                        return new UpdateProudctEvent(updateProductWrap.storeId,updateProductWrap.productId,updateProductWrap.chnName);
                    }
                })
                .collect(Collectors.toList());

    }

    public static final class UpdateProductWrap {


        private Long storeId;
        private Long productId;
        private String chnName;

        public Long getStoreId() {
            return storeId;
        }

        public void setStoreId(Long storeId) {
            this.storeId = storeId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getChnName() {
            return chnName;
        }

        public void setChnName(String chnName) {
            this.chnName = chnName;
        }
    }
}
*/
