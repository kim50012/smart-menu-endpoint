package com.basoft.eorder.foundation.jdbc.query;

import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.ShipPointDTO;
import com.basoft.eorder.interfaces.query.ShipPointQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ProjectName: intellij_work
 * @Package: com.basoft.eorder.foundation.jdbc.query
 * @ClassName: JdbcStoreQueryImpl
 * @Description:
 * @Author: liminzhe
 * @Date: 2018-12-13 16:32
 * @Version: 1.0
 */
@Slf4j
@Component("ShipPointQuery")
public class JdbcShipPointQueryImpl extends BaseRepository implements ShipPointQuery {
    private static final String SHIP_POINT_BY_ID = "select\n" +
            "        a.ship_point_id as shipPointid\n" +
            "        , a.ship_point_nm as shipPointnm\n" +
            "        , a.area_id as areaId\n" +
            "        , (select b.area_nm from area b where b.area_cd = a.area_id) as areaName\n" +
            "        , a.addr as addr\n" +
            "        , a.addr_cn as addrCn\n" +
            "        , a.lat as lat\n" +
            "        , a.lon as lon\n" +
            "        , a.phone_no as phoneNo\n" +
            "        , a.cmt as cmt\n" +
            "        , a.cmt_cn as cmtCn\n" +
            "        , a.status as status\n" +
            "from    ship_point a\n" +
            "where   a.ship_point_id = :ship_point_id ";

    private static final String SHIP_POINT_LIST_SELECT = "select\n" +
            "        a.ship_point_id as shipPointid\n" +
            "        , a.ship_point_nm as shipPointnm\n" +
            "        , a.area_id as areaId\n" +
            "        , (select b.area_nm from area b where b.area_cd = a.area_id) as areaName\n" +
            "        , a.addr as addr\n" +
            "        , a.addr_cn as addrCn\n" +
            "        , a.lat as lat\n" +
            "        , a.lon as lon\n" +
            "        , a.phone_no as phoneNo\n" +
            "        , a.cmt as cmt\n" +
            "        , a.cmt_cn as cmtCn\n" +
            "        , a.status as status\n";

    private static final String SHIP_POINT_FORM = "" +
            "from    ship_point a\n" +
            "   where 1 = 1 \n";

    JdbcShipPointQueryImpl() {
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    public JdbcShipPointQueryImpl(DataSource dataSource) {
        super(dataSource);
    }

    /*private StringBuilder orderByAndPage(Map<String, Object> param, StringBuilder query, String orderBy) {
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        query.append(orderBy);

        if (page >= 0 && size > 0) {
            int resPage = page;
            if (page > 0) {
                resPage = (resPage - 1) * 10;
                param.put("page", resPage);
            }
            query.append(LIMIT);
        }
        return query;
    }*/

    private StringBuilder shipPointQueryCondition(Map<String, Object> param, StringBuilder query) {
        String shipPointid = Objects.toString(param.get("shipPointid"), null);
        String shipPointnm = Objects.toString(param.get("shipPointnm"), null);
        String areaId = Objects.toString(param.get("areaId"), null);
        String addr = Objects.toString(param.get("addr"), null);
        String addrCn = Objects.toString(param.get("addrCn"), null);
        String lat = Objects.toString(param.get("lat"), null);
        String lon = Objects.toString(param.get("lon"), null);
        String phoneNo = Objects.toString(param.get("phoneNo"), null);
        String cmt = Objects.toString(param.get("cmt"), null);
        String cmtCn = Objects.toString(param.get("cmtCn"), null);
        String status = Objects.toString(param.get("status"), null);

        if (StringUtils.isNotBlank(shipPointid)) {
            query.append(" and a.ship_point_id = :shipPointid \n");
        }
        if (StringUtils.isNotBlank(shipPointnm)) {
            query.append(" and a.ship_point_nm = :shipPointnm \n");
        }
        if (StringUtils.isNotBlank(areaId)) {
            query.append(" and a.area_id = :areaId \n");
        }
        if (StringUtils.isNotBlank(addr)) {
            query.append(" and a.addr = :addr \n");
        }
        if (StringUtils.isNotBlank(addrCn)) {
            query.append(" and a.addr_cn = :addrCn \n");
        }
        if (StringUtils.isNotBlank(lat)) {
            query.append(" and a.lat = :lat \n");
        }
        if (StringUtils.isNotBlank(lon)) {
            query.append(" and a.lon = :lon \n");
        }
        if (StringUtils.isNotBlank(phoneNo)) {
            query.append(" and a.phone_no = :phoneNo \n");
        }
        if (StringUtils.isNotBlank(cmt)) {
            query.append(" and a.cmt = :cmt \n");
        }
        if (StringUtils.isNotBlank(cmtCn)) {
            query.append(" and a.cmt_cn = :cmtCn \n");
        }
        if (StringUtils.isNotBlank(status)) {
            query.append(" and a.status = :status \n");
        }

        return query;
    }

    @Override
    public ShipPointDTO getShipPointById(long shipPointid) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", shipPointid);

        ShipPointDTO sd = this.queryForObject(SHIP_POINT_BY_ID, map, new BeanPropertyRowMapper<ShipPointDTO>(ShipPointDTO.class));
        return sd;
    }

    @Override
    public int getShipPointCount(Map<String, Object> param) {
        StringBuilder query = new StringBuilder();
        query.append(SELECT_COUNT + SHIP_POINT_FORM);
        shipPointQueryCondition(param, query);
        String sql = query.toString();
        log.info("count SQL:" + sql);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql, param, Integer.class);
    }

    @Override
    public List<ShipPointDTO> getShipPointListByMap(Map<String, Object> param) {
        StringBuilder query = new StringBuilder();
        query = new StringBuilder(SHIP_POINT_LIST_SELECT + SHIP_POINT_FORM);
        shipPointQueryCondition(param, query);
        orderByAndPage(param, query, " order by areaName asc, a.ship_point_nm asc ");

        String sql = query.toString();
        log.info("SQL list size:" + sql);

        return this.getNamedParameterJdbcTemplate().query(sql, param, new BeanPropertyRowMapper<>(ShipPointDTO.class));
    }
}
