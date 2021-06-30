package com.basoft.eorder.foundation.jdbc.repo;

import com.basoft.eorder.interfaces.query.BaseQueryDTO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

@Repository
public class BaseRepository extends NamedParameterJdbcDaoSupport {
    protected static final String LIMIT = " limit :page, :size";

    protected static final String SELECT_COUNT = "select count(*)";

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jd;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected BaseRepository() {
    }

    public BaseRepository(DataSource ds) {
        this.dataSource = ds;
        this.jd = new JdbcTemplate(ds);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        this.setNamedParameterJdbcTemplate(this.jd);
    }

    @Autowired
    public void setNamedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
        super.setJdbcTemplate(jdbcTemplate);
    }

    /*public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }*/


    /*public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource);
    }*/

    /**
     * 单记录（单行多列）查询，必须用RowMapper进行映射转换
     *
     * @param query
     * @param map
     * @param rowMapper
     * @param <T>
     * @return
     */
    protected <T> T queryForObject(String query, Map<String, Object> map, RowMapper<T> rowMapper) {
        try {
            return this.getNamedParameterJdbcTemplate().queryForObject(query, map, rowMapper);
        } catch (Exception e) {
            logger.error("查询异常QueryForObject[RowMapper<T>]::" + e.getMessage());
            // logger.error("查询异常QueryForObject[RowMapper<T>]" + e.getMessage(), e);
            // e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询单列值，不支持多列值返回
     *
     * @param query
     * @param map
     * @param calzz
     * @param <T>
     * @return
     */
    protected <T> T queryForObject(String query, Map<String, Object> map, Class<T> calzz) {
        try {
            return this.getNamedParameterJdbcTemplate().queryForObject(query, map, calzz);
        } catch (Exception e) {
            logger.error("查询异常QueryForObject[Class<T>]::" + e.getMessage());
            // logger.error("查询异常QueryForObject[Class<T>]::" + e.getMessage(), e);
            // e.printStackTrace();
            return null;
        }
    }

    public StringBuilder appendPage(int page, int size, StringBuilder query, Map<String, Object> param, Boolean isLimit) {
        int resPage = page;
        if (page >= 0 && size > 0 && isLimit) {
            resPage = (resPage - 1) * size;
            param.put("page", resPage);
            query.append(LIMIT);
        }
        return query;
    }

    public StringBuilder orderByAndPage(Map<String, Object> param,StringBuilder query,String orderBy ){
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        query.append(orderBy);

        if (page >= 0 && size > 0) {
            int resPage = page;
            if(page > 0){
                resPage = (resPage-1)*size;
                param.put("page",resPage);
            }
            query.append(LIMIT);
        }
        return query;
    }

    /**
     * 查询包裹
     *
     * @param query
     */
    public StringBuilder pagingPackage(StringBuilder query) {
        return new StringBuilder("SELECT * FROM (").append(query).append(") AS A ");
    }

    /**
     * @param baseQueryDTO
     * @param query
     * @return
     */
    public StringBuilder pagingLimit(BaseQueryDTO baseQueryDTO, StringBuilder query) {
        // 当前页数
        int page = NumberUtils.toInt(Objects.toString(baseQueryDTO.getPage(), null));
        // 每页条数
        int size = NumberUtils.toInt(Objects.toString(baseQueryDTO.getSize(), null));

        // 页数大于等于0，size大于0
        if (page >= 0 && size > 0) {
            // 计算查询起始行
            if (page > 0) {
                int start = (page - 1) * size;
                baseQueryDTO.setStart(start);
            }
            query.append(" LIMIT :start, :size");
        }
        return query;
    }
}
