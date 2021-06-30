package com.basoft.eorder.domain.events;

public class UpdateProudctEvent {

    private Long storeId;
    private Long productId;
    private String chnName;

    public UpdateProudctEvent(Long storeId, Long productId, String chnName) {
        this.storeId = storeId;
        this.productId = productId;
        this.chnName = chnName;
    }

    public Long getProductId() {
        return productId;
    }

    public String getChnName() {
        return chnName;
    }

    public Long getStoreId() {
        return storeId;
    }
}
