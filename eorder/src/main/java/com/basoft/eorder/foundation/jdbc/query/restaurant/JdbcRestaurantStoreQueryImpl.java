package com.basoft.eorder.foundation.jdbc.query.restaurant;

import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.ProductQuery;
import com.basoft.eorder.interfaces.query.restaurant.RestaurantStoreQuery;
import com.basoft.eorder.interfaces.query.restaurant.RestaurantStoreQueryDTO;
import com.basoft.eorder.interfaces.query.restaurant.RestaurantStoreVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("restaurantStoreQuery")
public class JdbcRestaurantStoreQueryImpl extends BaseRepository implements RestaurantStoreQuery {
    //@Autowired
    //private ProductQuery productQuery;

    /****************************************************SIMPLE********************************************************/
    /**
     * restaurant simple query/get data count
     *
     * @param restaurantStoreQueryDTO
     * @return
     */
    public int getRestaurantStoreSimpleCount(RestaurantStoreQueryDTO restaurantStoreQueryDTO) {
        StringBuilder query = new StringBuilder();

        query.append("select count(1) ")
                // 拼接from子句
                .append(RestaurantStoreQuerySQL.restaurantStoreFrom(restaurantStoreQueryDTO))
                // 拼接where子句
                .append(RestaurantStoreQuerySQL.restaurantStoreWhere(restaurantStoreQueryDTO));

        String sql = query.toString();
        log.info("餐厅Restaurant数量查询count SQL:开始||" + sql + "||结束");

        return this.getNamedParameterJdbcTemplate().queryForObject(sql,
                new BeanPropertySqlParameterSource(restaurantStoreQueryDTO),
                Integer.class);
    }

    /**
     * restaurant simple query/get data list
     *
     * @param restaurantStoreQueryDTO
     * @return
     */
    public List<RestaurantStoreVO> getRestaurantStoreSimpleList(RestaurantStoreQueryDTO restaurantStoreQueryDTO) {
        StringBuilder query = new StringBuilder();

        // 1、拼接select子句
        String longitude = restaurantStoreQueryDTO.getLongitude();
        String latitude = restaurantStoreQueryDTO.getLatitude();
        // 经纬度都不为空，计算距离
        if (StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)) {
            // 拼接select子句，含distance
            query.append(RestaurantStoreQuerySQL.restaurantStoreSelect(restaurantStoreQueryDTO, true));
        }
        // 经纬度存在空值，则不计算距离
        else {
            // 拼接select子句，不含distance
            query.append(RestaurantStoreQuerySQL.restaurantStoreSelect(restaurantStoreQueryDTO, false));

            //如果不查询距离，则需要排除按距离排序
            if ("distance".equals(restaurantStoreQueryDTO.getOrderBy())) {
                restaurantStoreQueryDTO.setOrderBy("");
            }
        }

        // 2、拼接from子句、where子句
        query.append(RestaurantStoreQuerySQL.restaurantStoreFrom(restaurantStoreQueryDTO))
                .append(RestaurantStoreQuerySQL.restaurantStoreWhere(restaurantStoreQueryDTO));

        // 3、对内部查询包裹
        query = pagingPackage(query);

        // 4、拼接order子句
        query.append(RestaurantStoreQuerySQL.restaurantStoreOrder(restaurantStoreQueryDTO));

        // 5、分页LIMIT
        pagingLimit(restaurantStoreQueryDTO, query);

        String sql = query.toString();
        log.info("餐厅Restaurant列表查询SQL List SQL:开始||" + sql + "||结束");

        // 6、查询
        List<RestaurantStoreVO> restaurantStoreList = this.getNamedParameterJdbcTemplate().query(sql, new BeanPropertySqlParameterSource(restaurantStoreQueryDTO),
                new BeanPropertyRowMapper<>(RestaurantStoreVO.class));

        // 7、数据加工：填充餐厅的详细业务目录（即营业网点标签）和中国价格。说明：减少循环次数而合并处理
        // RestaurantStoreQueryData.convertCategoryInfoAndPrice(productQuery.getNowExchangeRate(), restaurantStoreList);

        return restaurantStoreList;
    }

    /****************************************************COMPLEX********************************************************/
    /**
     * restaurant complex query/get data count
     *
     * @param restaurantStoreQueryDTO
     * @return
     */
    public int getRestaurantStoreComplexCount(RestaurantStoreQueryDTO restaurantStoreQueryDTO) {
        StringBuilder query = new StringBuilder("select count(1) from ( ");

        // 是否计算距离
        boolean isDistance = false;
        //经纬度
        String longitude = restaurantStoreQueryDTO.getLongitude();
        String latitude = restaurantStoreQueryDTO.getLatitude();
        // 经纬度都不为空，计算距离
        if (StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)) {
            isDistance = true;
        }

        // 1、拼接select子句
        if (isDistance) {
            // 拼接select子句,含distance列
            query.append(RestaurantStoreQuerySQL.restaurantStoreSelect(restaurantStoreQueryDTO, true));
        } else {
            // 拼接select子句,不含distance列
            query.append(RestaurantStoreQuerySQL.restaurantStoreSelect(restaurantStoreQueryDTO, false));
        }

        // 2、拼接from子句
        query.append(RestaurantStoreQuerySQL.restaurantStoreFrom(restaurantStoreQueryDTO))
                // 拼接内部where子句
                .append(RestaurantStoreQuerySQL.restaurantStoreWhere(restaurantStoreQueryDTO))
                // 包裹内查询,并设置别名，且必须设置别名
                .append(") AS A");

        // 3、拼接外部where子句
        if (isDistance) {
            // 距离查询条件有效
            query.append(RestaurantStoreQuerySQL.restaurantStoreOuterWhere(query, restaurantStoreQueryDTO, true));
        } else {
            // 距离查询条件无效
            query.append(RestaurantStoreQuerySQL.restaurantStoreOuterWhere(query, restaurantStoreQueryDTO, false));
        }

        String sql = query.toString();
        log.info("[COMPLEX]餐厅Restaurant数量查询count SQL:开始||" + sql + "||结束");

        return this.getNamedParameterJdbcTemplate().queryForObject(sql,
                new BeanPropertySqlParameterSource(restaurantStoreQueryDTO),
                Integer.class);
    }

    /**
     * restaurant complex query/get data list
     *
     * @param restaurantStoreQueryDTO
     * @return
     */
    public List<RestaurantStoreVO> getRestaurantStoreComplexList(RestaurantStoreQueryDTO restaurantStoreQueryDTO) {
        // 是否计算距离
        boolean isDistance = false;
        // 经纬度
        String longitude = restaurantStoreQueryDTO.getLongitude();
        String latitude = restaurantStoreQueryDTO.getLatitude();
        if (StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)) {
            // 经纬度都不为空，计算距离
            isDistance = true;
        }
        // 查询SQL
        StringBuilder query = new StringBuilder();

        // 1、拼接select子句
        // 拼接select子句，含distance,计算距离
        if (isDistance) {
            query.append(RestaurantStoreQuerySQL.restaurantStoreSelect(restaurantStoreQueryDTO, true));
        }
        // 拼接select子句，不含distance,不计算距离
        else {
            // 如果不查询距离，则需要排除按距离排序
            if ("distance".equals(restaurantStoreQueryDTO.getOrderBy())) {
                restaurantStoreQueryDTO.setOrderBy("");
            }
            query.append(RestaurantStoreQuerySQL.restaurantStoreSelect(restaurantStoreQueryDTO, false));
        }

        //2、拼接from子句、where子句
        query.append(RestaurantStoreQuerySQL.restaurantStoreFrom(restaurantStoreQueryDTO))
                .append(RestaurantStoreQuerySQL.restaurantStoreWhere(restaurantStoreQueryDTO));

        // 3、对内部查询包裹
        query = pagingPackage(query);

        // 4、拼接外部的where子句
        if (isDistance) {
            query.append(RestaurantStoreQuerySQL.restaurantStoreOuterWhere(query, restaurantStoreQueryDTO, true));
        } else {
            query.append(RestaurantStoreQuerySQL.restaurantStoreOuterWhere(query, restaurantStoreQueryDTO, false));
        }

        // 5、拼接order子句
        query.append(RestaurantStoreQuerySQL.restaurantStoreOrder(restaurantStoreQueryDTO));

        // 6、分页LIMIT
        pagingLimit(restaurantStoreQueryDTO, query);

        String sql = query.toString();
        log.info("[COMPLEX]餐厅Restaurant列表查询SQL List SQL:开始||" + sql + "||结束");

        // 7、查询
        List<RestaurantStoreVO> restaurantStoreList = this.getNamedParameterJdbcTemplate().query(sql, new BeanPropertySqlParameterSource(restaurantStoreQueryDTO),
                new BeanPropertyRowMapper<>(RestaurantStoreVO.class));

        // 8、数据加工：填充酒店的详细业务目录（即营业网点标签）和中国价格。说明：减少循环次数而合并处理
        // RestaurantStoreQueryData.convertCategoryInfoAndPrice(productQuery.getNowExchangeRate(), restaurantStoreList);

        return restaurantStoreList;
    }
}