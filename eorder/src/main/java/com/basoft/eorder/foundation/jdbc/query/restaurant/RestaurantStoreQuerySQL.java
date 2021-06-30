package com.basoft.eorder.foundation.jdbc.query.restaurant;

import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.interfaces.query.restaurant.RestaurantStoreQueryDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 餐厅查询SQL加工类
 */
public class RestaurantStoreQuerySQL {
    /**
     * restaurant store select sub SQL/餐厅商户查询的select子句
     *
     * @param restaurantStoreQueryDTO
     * @param haveDistance
     * @return
     */
    public static StringBuilder restaurantStoreSelect(RestaurantStoreQueryDTO restaurantStoreQueryDTO, boolean haveDistance) {
        StringBuilder select = new StringBuilder("select ");
        // 餐厅商户查询字段
        select.append("t.id,t.name,t.logo_url,t.city,t.detail_addr_chn,t.detail_addr,")

                // 商户电子邮件，电话，状态，创建时间，营业时间
                .append("t.email,t.mobile,t.`status`,t.created,t.shop_hour,")

                // 经纬度，中韩商户描述，商户类型
                .append("t.longitude,t.latitude,t.description,t.description_chn,t.store_type,")

                // 是否加盟店，是否自助点餐，是否送餐到座位，自助服务是否可用，送餐到座位是否可用，是否支付商户
                .append("t.IS_JOIN,t.IS_SELFSERVICE,t.IS_DELIVERY,t.SELFSERVICE_USEYN,t.DELIVERY_USEYN,t.IS_PAY_SET,")

                // 是否营业中，即是否分段营业，即分段营业时间的设置信息
                .append("t.IS_OPENING,t.IS_SEGMENTED,t.MORNING_ST,t.MORNING_ET,t.NOON_ST,t.NOON_ET,t.EVENING_ST,")
                .append("t.EVENING_ET,t.AFTERNOON_ST,t.AFTERNOON_ET,t.MIDNIGHT_ST,t.MIDNIGHT_ET,")

                // 座位数
                .append("t.TABLE_COUNT,");

        // 计算距离-as disdance
        if (haveDistance) {
            select.append("ifnull(ROUND(6378.138*2*ASIN(SQRT(POW(SIN((:latitude *PI()/180-latitude *PI()/180)/2),2)")
                    .append("+ COS(:latitude*PI()/180)*")
                    .append("COS(latitude*PI()/180)*")
                    .append("POW(SIN((:longitude*PI()/180-longitude*PI()/180)/2),2)))*1000),-1) AS distance,");
        }

        // 餐厅商户标签-as categorysAllString
        select.append("(SELECT GROUP_CONCAT(CONCAT(bc.id,'@',bc.chn_name) SEPARATOR ',') FROM STORE_CATEGORY_MAP SCM INNER JOIN base_category bc ON SCM.CATEGORY_ID = bc.id WHERE SCM.STORE_ID = t.ID AND SCM.FUNCTION_TYPE = 2) AS categorysAllString,")

                // 餐厅人均消费价格
                .append("0 AS avgPriceKor,")

                // 餐厅商户评价数量
                .append("(SELECT IFNULL(SD.REVIEW_COUNT,0) FROM store_dynamic SD WHERE SD.STORE_ID=t.id) AS reviewCount,")

                // 餐厅商户平均评分
                .append("(SELECT IFNULL(SD.REVIEW_GRADE,0) FROM store_dynamic SD WHERE SD.STORE_ID=t.id) AS reviewGrade,")

                // 餐厅商户扩展属性
                .append("(SELECT GROUP_CONCAT(CONCAT(SE.FD_ID,'@',SE.FD_VAL_CODE) SEPARATOR ',') FROM store_extend SE WHERE SE.STORE_ID=t.id AND SE.`STATUS`=1) AS storeExtend,")

                // 餐厅商户附加的批量图片
                .append("(SELECT GROUP_CONCAT(CONCAT(SA.CONTENT_URL,'@',SA.ATTACH_TYPE) SEPARATOR ',') FROM store_attach SA WHERE SA.STORE_ID=t.id AND SA.IS_DISPLAY=1 AND SA.ATTACH_TYPE IN (1001,1002)) AS storeImages")

                // select子句之后的空格
                .append(" ");
        return select;
    }

    /**
     * restaurant store from sub SQL/餐厅商户查询的from子句
     *
     * @param restaurantStoreQueryDTO
     * @return
     */
    public static StringBuilder restaurantStoreFrom(RestaurantStoreQueryDTO restaurantStoreQueryDTO) {
        StringBuilder from = new StringBuilder("from store t");
        // from子句之后的空格
        from.append(" ");
        return from;
    }

    /**
     * restaurant store where sub SQL/餐厅商户查询的where子句
     *
     * @param restaurantStoreQueryDTO
     * @return
     */
    public static StringBuilder restaurantStoreWhere(RestaurantStoreQueryDTO restaurantStoreQueryDTO) {
        // 上架状态的商户
        StringBuilder innerWhere = new StringBuilder("where t.`status`=1");

        // 餐厅类型商户
        innerWhere.append(" AND t.store_type=").append(CommonConstants.BIZ_ORDERING_STRING)
                // 营业中的餐厅商户
                .append(" AND t.IS_OPENING=1")
                // 指定城市的商户
                .append(" AND t.city=:city");

        // 关键字查询
        String nameLike = restaurantStoreQueryDTO.getKey();
        if (StringUtils.isNotBlank(nameLike)) {
            innerWhere.append(" AND t.name like '%").append(nameLike).append("%'");
        }

        // 主题查询
        String topicType = restaurantStoreQueryDTO.getTopicType();
        if("1".equals(topicType)){
            // nothing to do
        } else if("2".equals(topicType)){
            List<String> topicList = restaurantStoreQueryDTO.getTopic();
            if(topicList != null && topicList.size() > 0){
                StringBuilder ts = new StringBuilder("(");
                for (int i = 0; i <topicList.size() ; i++) {
                    ts.append(topicList.get(i)).append(",");
                    if(i == topicList.size() - 1){
                        ts.append(topicList.get(i)).append(")");
                    }
                }
                innerWhere.append(" AND t.id in (SELECT distinct st.STORE_ID FROM store_topic st WHERE st.TP_ID IN ")
                        .append(ts).append(")");
            }
        }

		// 是否支持店外预先点餐餐厅
        String isSelfService = restaurantStoreQueryDTO.getIsSelfService();
        if("1".equals(isSelfService)){
            innerWhere.append(" AND t.SELFSERVICE_USEYN = 1 and t.IS_SELFSERVICE = 1");
        }

        // 餐厅查询结果加盟店在前，即is_join为1的在前，0的灾后
        innerWhere.append(" ORDER BY t.IS_JOIN DESC");

        // where子句之后的空格
        innerWhere.append(" ");

        return innerWhere;
    }

    /**
     * restaurant store order sub SQL/餐厅商户查询的order子句
     *
     * @param restaurantStoreQueryDTO
     * @return
     */
    public static StringBuilder restaurantStoreOrder(RestaurantStoreQueryDTO restaurantStoreQueryDTO) {
        StringBuilder order = new StringBuilder();
        String orderBy = restaurantStoreQueryDTO.getOrderBy();
        if (StringUtils.isNotBlank(orderBy)) {
            // 按指定字段排序，默认降序
            order.append("order by ").append(orderBy).append(" ");

            // 指定排序顺序，则按照指定的排序顺序进行
            String orderSort = restaurantStoreQueryDTO.getOrderSort();
            if (StringUtils.isNotBlank(orderBy)) {
                order.append(orderSort);
            }
        }
        return order;
    }

    /**
     * restaurant store outer where sub SQL/餐厅商户外部查询的where子句
     *
     * @param query
     * @param restaurantStoreQueryDTO
     * @param isDistance
     * @return
     */
    public static StringBuilder restaurantStoreOuterWhere(StringBuilder query,
                                                     RestaurantStoreQueryDTO restaurantStoreQueryDTO,
                                                     boolean isDistance) {
        StringBuilder outerWhere = new StringBuilder(" where 1=1 ");

        if(isDistance){
            // 距离
            String minDistance = restaurantStoreQueryDTO.getMinDistance();
            String maxDistance = restaurantStoreQueryDTO.getMaxDistance();
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
        String minPrice = restaurantStoreQueryDTO.getMinPrice();
        String maxPrice = restaurantStoreQueryDTO.getMaxPrice();
        // 最小价格不为空
        if (StringUtils.isNotBlank(minPrice)) {
            // 最大价格不为空，取价格区间
            if (StringUtils.isNotBlank(maxPrice)) {
                outerWhere.append(" AND A.avgPriceKor >=")
                        .append(minPrice)
                        .append(" AND A.avgPriceKor <= ")
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

        // 餐厅扩展属性
        List<String> storeExtends = restaurantStoreQueryDTO.getStoreExtends();
        if (CollectionUtils.isNotEmpty(storeExtends)) {
            for (String storeExtend : storeExtends) {
                outerWhere.append(" AND storeExtend like '%").append(storeExtend).append("%'");
            }
        }

        String queryScore = restaurantStoreQueryDTO.getQueryScore();
        if(StringUtils.isNotBlank(queryScore)){
            outerWhere.append(" AND A.reviewGrade >= ")
                    .append(queryScore);
        }

        // where子句之后的空格
        outerWhere.append(" ");

        return outerWhere;
    }
}