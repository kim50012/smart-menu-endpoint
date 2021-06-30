package com.basoft.eorder.foundation.jdbc.query.hotel;

import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.ProductQuery;
import com.basoft.eorder.interfaces.query.hotel.HotelStoreQuery;
import com.basoft.eorder.interfaces.query.hotel.HotelStoreQueryDTO;
import com.basoft.eorder.interfaces.query.hotel.HotelStoreVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("hotelStoreQuery")
public class JdbcHotelStoreQueryImpl extends BaseRepository implements HotelStoreQuery {
    @Autowired
    private ProductQuery productQuery;

    /****************************************************SIMPLE********************************************************/
    /**
     * hotel simple query/get data count
     *
     * @param hotelStoreQueryDTO
     * @return
     */
    public int getHotelStoreSimpleCount(HotelStoreQueryDTO hotelStoreQueryDTO) {
        StringBuilder query = new StringBuilder();

        query.append("select count(1) ")
                // 拼接from子句
                .append(HotelStoreQuerySQL.hotelStoreFrom(hotelStoreQueryDTO))
                // 拼接where子句
                .append(HotelStoreQuerySQL.hotelStoreWhere(hotelStoreQueryDTO));

        String sql = query.toString();
        log.debug("酒店HOTEL数量查询count SQL:开始||" + sql + "||结束");

        return this.getNamedParameterJdbcTemplate().queryForObject(sql,
                new BeanPropertySqlParameterSource(hotelStoreQueryDTO),
                Integer.class);
    }

    /**
     * hotel simple query/get data list
     *
     * @param hotelStoreQueryDTO
     * @return
     */
    public List<HotelStoreVO> getHotelStoreSimpleList(HotelStoreQueryDTO hotelStoreQueryDTO) {
        StringBuilder query = new StringBuilder();

        // 1、拼接select子句
        String longitude = hotelStoreQueryDTO.getLongitude();
        String latitude = hotelStoreQueryDTO.getLatitude();
        // 经纬度都不为空，计算距离
        if (StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)) {
            // 拼接select子句，含distance
            query.append(HotelStoreQuerySQL.hotelStoreSelect(hotelStoreQueryDTO, true));
        }
        // 经纬度存在空值，则不计算距离
        else {
            // 拼接select子句，不含distance
            query.append(HotelStoreQuerySQL.hotelStoreSelect(hotelStoreQueryDTO, false));

            //如果不查询距离，则需要排除按距离排序
            if ("distance".equals(hotelStoreQueryDTO.getOrderBy())) {
                hotelStoreQueryDTO.setOrderBy("");
            }
        }

        // 2、拼接from子句、where子句
        query.append(HotelStoreQuerySQL.hotelStoreFrom(hotelStoreQueryDTO))
                .append(HotelStoreQuerySQL.hotelStoreWhere(hotelStoreQueryDTO));

        // 3、对内部查询包裹
        query = pagingPackage(query);

        // 4、拼接order子句
        query.append(HotelStoreQuerySQL.hotelStoreOrder(hotelStoreQueryDTO));

        // 5、分页LIMIT
        pagingLimit(hotelStoreQueryDTO, query);

        String sql = query.toString();
        log.debug("酒店HOTEL列表查询SQL List SQL:开始||" + sql + "||结束");

        // 6、查询
        List<HotelStoreVO> hotelStoreList = this.getNamedParameterJdbcTemplate().query(sql, new BeanPropertySqlParameterSource(hotelStoreQueryDTO),
                new BeanPropertyRowMapper<>(HotelStoreVO.class));

        // 7、数据加工：填充酒店的详细业务目录（即营业网点标签）和中国价格。说明：减少循环次数而合并处理
        HotelStoreQueryData.convertCategoryInfoAndPrice(productQuery.getNowExchangeRate(), hotelStoreList);

        return hotelStoreList;
    }

    /****************************************************COMPLEX********************************************************/
    /**
     * hotel complex query/get data count
     *
     * @param hotelStoreQueryDTO
     * @return
     */
    public int getHotelStoreComplexCount(HotelStoreQueryDTO hotelStoreQueryDTO) {
        StringBuilder query = new StringBuilder("select count(1) from ( ");

        // 是否计算距离
        boolean isDistance = false;
        //经纬度
        String longitude = hotelStoreQueryDTO.getLongitude();
        String latitude = hotelStoreQueryDTO.getLatitude();
        // 经纬度都不为空，计算距离
        if (StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)) {
            isDistance = true;
        }

        // 1、拼接select子句
        if (isDistance) {
            // 拼接select子句,含distance列
            query.append(HotelStoreQuerySQL.hotelStoreSelect(hotelStoreQueryDTO, true));
        } else {
            // 拼接select子句,不含distance列
            query.append(HotelStoreQuerySQL.hotelStoreSelect(hotelStoreQueryDTO, false));
        }

        // 2、拼接from子句
        query.append(HotelStoreQuerySQL.hotelStoreFrom(hotelStoreQueryDTO))
                // 拼接内部where子句
                .append(HotelStoreQuerySQL.hotelStoreWhere(hotelStoreQueryDTO))
                // 包裹内查询,并设置别名，且必须设置别名
                .append(") AS A");

        // 3、拼接外部where子句
        if (isDistance) {
            // 距离查询条件有效
            query.append(HotelStoreQuerySQL.hotelStoreOuterWhere(query, hotelStoreQueryDTO, true));
        } else {
            // 距离查询条件无效
            query.append(HotelStoreQuerySQL.hotelStoreOuterWhere(query, hotelStoreQueryDTO, false));
        }

        String sql = query.toString();
        log.debug("[COMPLEX]酒店HOTEL数量查询count SQL:开始||" + sql + "||结束");

        return this.getNamedParameterJdbcTemplate().queryForObject(sql,
                new BeanPropertySqlParameterSource(hotelStoreQueryDTO),
                Integer.class);
    }

    /**
     * hotel complex query/get data list
     *
     * @param hotelStoreQueryDTO
     * @return
     */
    public List<HotelStoreVO> getHotelStoreComplexList(HotelStoreQueryDTO hotelStoreQueryDTO) {
        // 是否计算距离
        boolean isDistance = false;
        // 经纬度
        String longitude = hotelStoreQueryDTO.getLongitude();
        String latitude = hotelStoreQueryDTO.getLatitude();
        if (StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)) {
            // 经纬度都不为空，计算距离
            isDistance = true;
        }
        // 查询SQL
        StringBuilder query = new StringBuilder();

        // 1、拼接select子句
        // 拼接select子句，含distance,计算距离
        if (isDistance) {
            query.append(HotelStoreQuerySQL.hotelStoreSelect(hotelStoreQueryDTO, true));
        }
        // 拼接select子句，不含distance,不计算距离
        else {
            // 如果不查询距离，则需要排除按距离排序
            if ("distance".equals(hotelStoreQueryDTO.getOrderBy())) {
                hotelStoreQueryDTO.setOrderBy("");
            }
            query.append(HotelStoreQuerySQL.hotelStoreSelect(hotelStoreQueryDTO, false));
        }

        //2、拼接from子句、where子句
        query.append(HotelStoreQuerySQL.hotelStoreFrom(hotelStoreQueryDTO))
                .append(HotelStoreQuerySQL.hotelStoreWhere(hotelStoreQueryDTO));

        // 3、对内部查询包裹
        query = pagingPackage(query);

        // 4、拼接外部的where子句
        if (isDistance) {
            query.append(HotelStoreQuerySQL.hotelStoreOuterWhere(query, hotelStoreQueryDTO, true));
        } else {
            query.append(HotelStoreQuerySQL.hotelStoreOuterWhere(query, hotelStoreQueryDTO, false));
        }

        // 5、拼接order子句
        query.append(HotelStoreQuerySQL.hotelStoreOrder(hotelStoreQueryDTO));

        // 6、分页LIMIT
        pagingLimit(hotelStoreQueryDTO, query);

        String sql = query.toString();
        log.debug("[COMPLEX]酒店HOTEL列表查询SQL List SQL:开始||" + sql + "||结束");

        // 7、查询
        List<HotelStoreVO> hotelStoreList = this.getNamedParameterJdbcTemplate().query(sql, new BeanPropertySqlParameterSource(hotelStoreQueryDTO),
                new BeanPropertyRowMapper<>(HotelStoreVO.class));

        // 8、数据加工：填充酒店的详细业务目录（即营业网点标签）和中国价格。说明：减少循环次数而合并处理
        HotelStoreQueryData.convertCategoryInfoAndPrice(productQuery.getNowExchangeRate(), hotelStoreList);

        return hotelStoreList;
    }
}