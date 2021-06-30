package com.basoft.service.entity.shop;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class ShopFileKey {
	@JsonSerialize(using = ToStringSerializer.class )
    private Long shopId;

	@JsonSerialize(using = ToStringSerializer.class )
    private Long fileId;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}