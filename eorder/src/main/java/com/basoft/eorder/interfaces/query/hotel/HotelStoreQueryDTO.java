package com.basoft.eorder.interfaces.query.hotel;

import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.interfaces.query.BaseQueryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class HotelStoreQueryDTO extends BaseQueryDTO {
    private String channel;

    private String city;

    private String longitude;

    private String latitude;

    private String storeType;

    private String startDate;

    private String endDate;

    private String key;

    // 距离-单位米
    private String minDistance;
    private String maxDistance;

    // 价格
    private String minPrice;
    private String maxPrice;

    // 星级
    private List<String> comfortLevels;

    // 酒店扩展属性
    private List<String> storeExtends;

    private String orderBy;

    private String orderSort;

    // 查询模式-前端来判断
    private String qmd;

    @Builder
    public HotelStoreQueryDTO(String orderBy, String orderSort, Integer page, Integer size,
                              Integer start, String channel, String city, String longitude,
                              String latitude, String storeType, String startDate, String endDate,
                              String key, String minDistance, String maxDistance, String minPrice, String maxPrice,
                              List<String> comfortLevels, List<String> storeExtends) {
        super(page, size, start);
        this.channel = channel;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
        this.storeType = storeType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.key = key;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.comfortLevels = comfortLevels;
        this.storeExtends = storeExtends;
        this.orderBy = orderBy;
        this.orderSort = orderSort;
    }

    @Override
    public String toString() {
        return "HotelStoreQueryDTO{" +
                "channel='" + channel + '\'' +
                ", city='" + city + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", storeType='" + storeType + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", key='" + key + '\'' +
                ", minDistance='" + minDistance + '\'' +
                ", maxDistance='" + maxDistance + '\'' +
                ", minPrice='" + minPrice + '\'' +
                ", maxPrice='" + maxPrice + '\'' +
                ", comfortLevels=" + comfortLevels +
                ", storeExtends=" + storeExtends +
                ", orderBy='" + orderBy + '\'' +
                ", orderSort='" + orderSort + '\'' +
                ", page='" + super.getPage() + '\'' +
                ", size='" + super.getSize() + '\'' +
                ", queryMode='" + qmd + '\'' +
                '}';
    }

    /**
     * 初始化
     */
    public void initValue() {
        if (StringUtils.isBlank(this.channel)) {
            this.channel = "wechat";
        }

        if (StringUtils.isBlank(this.storeType)) {
            this.storeType = CommonConstants.BIZ_HOTEL_STRING;
        }

        if (this.getPage() == null || this.getPage() == 0) {
            this.setPage(1);
        }

        if (this.getSize() == null || this.getSize() == 0) {
            this.setSize(20);
        }

        // 排序字段
        if ("1".equals(this.getOrderBy())) {
            // 距离
            this.setOrderBy("distance");
        } else if ("2".equals(this.getOrderBy())) {
            // 价格（最低价格）
            this.setOrderBy("minPriceKor");
        } else if ("3".equals(this.getOrderBy())) {
            // 评分
            this.setOrderBy("reviewGrade");
        } else if ("4".equals(this.getOrderBy())) {
            this.setOrderBy("");
        } else if ("5".equals(this.getOrderBy())) {
            this.setOrderBy("");
        } else {
            this.setOrderBy("");
        }

        // 排序顺序
        if ("1".equals(this.getOrderSort())) {
            this.setOrderSort("ASC");
        } else if ("2".equals(this.getOrderSort())) {
            this.setOrderSort("DESC");
        } else {
            this.setOrderSort("ASC");
        }
    }
}

