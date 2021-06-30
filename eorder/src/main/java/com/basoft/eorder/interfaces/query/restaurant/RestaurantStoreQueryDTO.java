package com.basoft.eorder.interfaces.query.restaurant;

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
public class RestaurantStoreQueryDTO extends BaseQueryDTO {
    private String channel;

    private String city;

    private String longitude;

    private String latitude;

    private String storeType;

    private String key;

    // 1-查询所有的主题 2-查询topic list属性指定主题
    private String topicType;

    // 主题
    private List<String> topic;

    // 高分值
    private String queryScore;

    // 是否支持店外提前点餐  1-支持 0-不支持
    private String isSelfService;

    // 距离-单位米
    private String minDistance;
    private String maxDistance;

    // 人均消费价格
    private String minPrice;
    private String maxPrice;

    // 酒店扩展属性
    private List<String> storeExtends;

    private String orderBy;

    private String orderSort;

    // 查询模式-前端来判断
    private String qmd;

    @Builder
    public RestaurantStoreQueryDTO(String channel, String city, String longitude,
                                   String latitude, String storeType, String key, String minDistance, String maxDistance,
                                   String minPrice, String maxPrice,
                                   String topicType, List<String> topic, String queryScore,
                                   String isSelfService, List<String> storeExtends,
                                   String qmd, String orderBy, String orderSort,
                                   Integer page, Integer size, Integer start) {
        super(page, size, start);
        this.channel = channel;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
        this.storeType = storeType;
        this.key = key;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.topicType = topicType;
        this.topic = topic;
        this.queryScore = queryScore;
        this.isSelfService = isSelfService;
        this.storeExtends = storeExtends;
        this.qmd = qmd;
        this.orderBy = orderBy;
        this.orderSort = orderSort;
    }

    /**
     * 初始化
     */
    public void initValue() {
        if (StringUtils.isBlank(this.channel)) {
            this.channel = "wechat";
        }

        if (StringUtils.isBlank(this.storeType)) {
            this.storeType = CommonConstants.BIZ_ORDERING_STRING;
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
            this.setOrderBy("");
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

    @Override
    public String toString() {
        return "RestaurantStoreQueryDTO{" +
                "channel='" + channel + '\'' +
                ", city='" + city + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", storeType='" + storeType + '\'' +
                ", key='" + key + '\'' +
                ", topicType='" + topicType + '\'' +
                ", topic=" + topic +
                ", queryScore=" + queryScore +
                ", isSelfService=" + isSelfService +
                ", minDistance='" + minDistance + '\'' +
                ", maxDistance='" + maxDistance + '\'' +
                ", minPrice='" + minPrice + '\'' +
                ", maxPrice='" + maxPrice + '\'' +
                ", storeExtends=" + storeExtends +
                ", orderBy='" + orderBy + '\'' +
                ", orderSort='" + orderSort + '\'' +
                ", qmd='" + qmd + '\'' +
                '}';
    }
}

