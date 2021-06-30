package com.basoft.eorder.foundation.jdbc.query.hotel;

import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.interfaces.query.hotel.HotelStoreQueryDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 酒店查询SQL加工类
 */
public class HotelStoreQuerySQL {
    /**
     * hotel store select sub SQL/酒店商户查询的select子句
     *
     * @param hotelStoreQueryDTO
     * @param haveDistance
     * @return
     */
    public static StringBuilder hotelStoreSelect(HotelStoreQueryDTO hotelStoreQueryDTO, boolean haveDistance) {
        StringBuilder select = new StringBuilder("select ");
        // 酒店商户查询字段
        select.append("t.id,t.name,t.logo_url,t.city,t.detail_addr_chn,t.detail_addr,")

                // 商户电子邮件，电话，状态，创建时间，营业时间
                .append("t.email,t.mobile,t.`status`,t.created,t.shop_hour,")

                // 经纬度，中韩商户描述，商户类型
                .append("t.longitude,t.latitude,t.description,t.description_chn,t.store_type,")

                // 是否加盟商户，是否自助点餐，是否送餐到座位，自助服务是否可用，送餐到座位是否可用，是否支付商户
                .append("t.IS_JOIN,t.IS_SELFSERVICE,t.IS_DELIVERY,t.SELFSERVICE_USEYN,t.DELIVERY_USEYN,t.IS_PAY_SET,")

                // 是否营业中，即是否分段营业，即分段营业时间的设置信息
                .append("t.IS_OPENING,t.IS_SEGMENTED,t.MORNING_ST,t.MORNING_ET,t.NOON_ST,t.NOON_ET,t.EVENING_ST,")
                .append("t.EVENING_ET,t.AFTERNOON_ST,t.AFTERNOON_ET,t.MIDNIGHT_ST,t.MIDNIGHT_ET,")

                // 座位数-延续查询，该字段对酒店无用
                .append("t.TABLE_COUNT,");

        // 计算距离-as disdance
        if (haveDistance) {
            select.append("ifnull(ROUND(6378.138*2*ASIN(SQRT(POW(SIN((:latitude *PI()/180-latitude *PI()/180)/2),2)")
                    //.append("+ COS(40.099156*PI()/180)*")
                    .append("+ COS(:latitude*PI()/180)*")
                    .append("COS(latitude*PI()/180)*")
                    //.append("POW(SIN((116.53889*PI()/180-longitude*PI()/180)/2),2)))*1000),-1) AS distance,");
                    .append("POW(SIN((:longitude*PI()/180-longitude*PI()/180)/2),2)))*1000),-1) AS distance,");
        }

        // 酒店商户标签-as categorysAllString
        select.append("(SELECT GROUP_CONCAT(CONCAT(bc.id,'@',bc.chn_name) SEPARATOR ',') FROM STORE_CATEGORY_MAP SCM INNER JOIN base_category bc ON SCM.CATEGORY_ID = bc.id WHERE SCM.STORE_ID = t.ID AND SCM.FUNCTION_TYPE = 2) AS categorysAllString,")

                // 酒店商户价格
                //.append("(SELECT IFNULL(MIN(STP.MIN_PRICE),50000) FROM store_day_price STP WHERE STP.STORE_ID=t.id AND STP.INV_DATE BETWEEN '2019-12-09' AND '2019-12-11') AS minPrice,")
                .append("(SELECT IFNULL(MIN(STP.MIN_PRICE),50000) FROM store_day_price STP WHERE STP.STORE_ID=t.id AND STP.INV_DATE BETWEEN :startDate AND :endDate) AS minPriceKor,")

                // 酒店商户评价数量
                .append("(SELECT IFNULL(SD.REVIEW_COUNT,0) FROM store_dynamic SD WHERE SD.STORE_ID=t.id) AS reviewCount,")

                // 酒店商户平均评分
                .append("(SELECT IFNULL(SD.REVIEW_GRADE,0) FROM store_dynamic SD WHERE SD.STORE_ID=t.id) AS reviewGrade,")

                // 酒店商户扩展属性
                .append("(SELECT GROUP_CONCAT(CONCAT(SE.FD_ID,'@',SE.FD_VAL_CODE) SEPARATOR ',') FROM store_extend SE WHERE SE.STORE_ID=t.id AND SE.`STATUS`=1) AS storeExtend,")

                // 酒店商户附加的批量图片
                .append("(SELECT GROUP_CONCAT(CONCAT(SA.CONTENT_URL,'@',SA.ATTACH_TYPE) SEPARATOR ',') FROM store_attach SA WHERE SA.STORE_ID=t.id AND SA.IS_DISPLAY=1 AND SA.ATTACH_TYPE IN (1001,1002)) AS storeImages")

                // select子句之后的空格
                .append(" ");
        return select;
    }

    /**
     * hotel store from sub SQL/酒店商户查询的from子句
     *
     * @param hotelStoreQueryDTO
     * @return
     */
    public static StringBuilder hotelStoreFrom(HotelStoreQueryDTO hotelStoreQueryDTO) {
        StringBuilder from = new StringBuilder("from store t");
        // from子句之后的空格
        from.append(" ");
        return from;
    }

    /**
     * hotel store where sub SQL/酒店商户查询的where子句
     *
     * @param hotelStoreQueryDTO
     * @return
     */
    public static StringBuilder hotelStoreWhere(HotelStoreQueryDTO hotelStoreQueryDTO) {
        // 上架状态的商户
        StringBuilder innerWhere = new StringBuilder("where t.`status`=1");

        // 酒店类型商户
        innerWhere.append(" AND t.store_type=").append(CommonConstants.BIZ_HOTEL_STRING)
                // 营业中的酒店商户
                .append(" AND t.IS_OPENING=1")
                // 指定城市的商户
                //.append(" AND t.city='0017'");
                .append(" AND t.city=:city");

        // 关键字查询
        String nameLike = hotelStoreQueryDTO.getKey();
        if (StringUtils.isNotBlank(nameLike)) {
            innerWhere.append(" AND t.name like '%").append(nameLike).append("%'");
        }

        // where子句之后的空格
        innerWhere.append(" ");

        return innerWhere;
    }

    /**
     * hotel store order sub SQL/酒店商户查询的order子句
     *
     * @param hotelStoreQueryDTO
     * @return
     */
    public static StringBuilder hotelStoreOrder(HotelStoreQueryDTO hotelStoreQueryDTO) {
        StringBuilder order = new StringBuilder();
        String orderBy = hotelStoreQueryDTO.getOrderBy();
        if (StringUtils.isNotBlank(orderBy)) {
            // 按指定字段排序，默认降序
            order.append("order by ").append(orderBy).append(" ");

            // 指定排序顺序，则按照指定的排序顺序进行
            String orderSort = hotelStoreQueryDTO.getOrderSort();
            if (StringUtils.isNotBlank(orderBy)) {
                order.append(orderSort);
            }
        }
        return order;
    }

    /**
     * hotel store outer where sub SQL/酒店商户外部查询的where子句
     *
     * @param query
     * @param hotelStoreQueryDTO
     * @param isDistance
     * @return
     */
    public static StringBuilder hotelStoreOuterWhere(StringBuilder query,
                                                     HotelStoreQueryDTO hotelStoreQueryDTO,
                                                     boolean isDistance) {
        StringBuilder outerWhere = new StringBuilder(" where 1=1 ");

        if(isDistance){
            // 距离
            String minDistance = hotelStoreQueryDTO.getMinDistance();
            String maxDistance = hotelStoreQueryDTO.getMaxDistance();
            // 最小距离不为空
            if (StringUtils.isNotBlank(minDistance)) {
                // 最大距离不为空，取距离区间
                if (StringUtils.isNotBlank(maxDistance)) {
                    outerWhere.append(" AND A.distance >=")
                            .append(minDistance)
                            .append(" AND A.distance <= ")
                            .append(maxDistance);
                }
                // 最大距离为空，非法传值
            }
            // 最小距离为空
            else {
                if (StringUtils.isNotBlank(maxDistance)) {
                    outerWhere.append(" AND A.distance >=")
                            .append(maxDistance);
                }
                // 最大距离为空，无距离查询条件
            }
        }



        // 价格
        String minPrice = hotelStoreQueryDTO.getMinPrice();
        String maxPrice = hotelStoreQueryDTO.getMaxPrice();
        // 最小价格不为空
        if (StringUtils.isNotBlank(minPrice)) {
            // 最大价格不为空，取价格区间
            if (StringUtils.isNotBlank(maxPrice)) {
                outerWhere.append(" AND A.minPriceKor >=")
                        .append(minPrice)
                        .append(" AND A.minPriceKor <= ")
                        .append(maxPrice);
            }
            // 最大价格为空，非法传值
        }
        // 最小价格为空
        else {
            if (StringUtils.isNotBlank(maxPrice)) {
                outerWhere.append(" AND A.minPriceKor >=")
                        .append(maxPrice);
            }
            // 最大价格为空，无距离查询条件
        }


        // 星级
        List<String> comfortLevels = hotelStoreQueryDTO.getComfortLevels();
        if (CollectionUtils.isNotEmpty(comfortLevels)) {
            outerWhere.append(" AND (");
            for (int i = 0; i < comfortLevels.size(); i++) {
                if (i == comfortLevels.size() - 1) {
                    outerWhere.append("storeExtend like '%").append(comfortLevels.get(i)).append("%'");
                    break;
                }
                outerWhere.append("storeExtend like '%").append(comfortLevels.get(i)).append("%'").append(" OR ");
            }
            outerWhere.append(")");
        }


        // 酒店扩展属性
        List<String> storeExtends = hotelStoreQueryDTO.getStoreExtends();
        if (CollectionUtils.isNotEmpty(storeExtends)) {
            for (String storeExtend : storeExtends) {
                outerWhere.append(" AND storeExtend like '%").append(storeExtend).append("%'");
            }
        }

        // where子句之后的空格
        outerWhere.append(" ");

        return outerWhere;
    }
}