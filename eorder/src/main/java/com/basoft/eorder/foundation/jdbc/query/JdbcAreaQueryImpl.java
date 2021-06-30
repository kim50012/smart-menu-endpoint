package com.basoft.eorder.foundation.jdbc.query;


import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.AreaDTO;
import com.basoft.eorder.interfaces.query.AreaQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("AreaQuery")
public class JdbcAreaQueryImpl extends BaseRepository implements AreaQuery {

    @Override
    public List<AreaDTO> getAreaListByMap(Map<String, Object> param) {
        String sql = "select a.area_cd as areaCd, a.area_lvl as areaLvl, a.area_nm as areaNm, a.area_status as areaStatus, a.created as created, a.parent_cd as parentCd, a.use_yn as useYn from area a where a.area_status = 1 and a.area_lvl = 1 and a.use_yn = 'Y'";
        return this.getNamedParameterJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(AreaDTO.class));
    }
}
