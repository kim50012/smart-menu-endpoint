package com.basoft.eorder.interfaces.query.restaurant;

import com.basoft.eorder.application.base.query.CategoryDTO4H5;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RestaurantStoreVO {
    private String id;

    private String name;

    private String logoUrl;

    private String detailAddrChn;

    private String detailAddr;

    private String email;

    private String mobile;

    private String status;

    private String created;

    private String shopHour;

    private String longitude;

    private String latitude;

    private String description;

    private String descriptionChn;

    private String storeType;

    private String isJoin;

    private String isSelfservice;

    private String isDelivery;

    private String selfserviceUseyn;

    private String deliveryUseyn;

    private String isPaySet;

    private String isOpening;

    private String isSegmented;

    private String morningSt;

    private String morningEt;

    private String noonSt;

    private String noonEt;

    private String eveningSt;

    private String eveningEt;

    private String afternoonSt;

    private String afternoonEt;

    private String midnightSt;

    private String midnightEt;

    private String tableCount;

    private String distance;

    private String categorysAllString;

    private String minPriceKor;

    private String reviewCount;

    private String reviewGrade;

    private String storeExtend;

    private String storeImages;

    private String minPriceChn;

    private List<CategoryDTO4H5> categoryList;
}