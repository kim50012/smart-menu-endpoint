package com.basoft.eorder.foundation.jdbc.query;

import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.UserDTO;
import com.basoft.eorder.interfaces.query.UserInManagerDTO;
import com.basoft.eorder.interfaces.query.UserQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ProjectName: eorder
 * @Package: com.basoft.eorder.foundation.jdbc.query
 * @ClassName: JdbcUserQueryImpl
 * @Author: liminzhe
 * @Date: 2018-12-12 10:04
 * @Version: 1.0
 */
@Component("UserQuery")
public class JdbcUserQueryImpl extends BaseRepository implements UserQuery {
    private static final String USER_BY_ID = "select u.id as id, \n" +
            " u.name as name, \n" +
            " u.account as account, \n" +
            " u.status as status, \n" +
            " u.email as email, " +
            " u.password as password,\n" +
            " u.mobile as mobile,\n" +
            " u.created as created \n" +
            " from user u \n" +
            "  where id = :id";

    private static final String INADMIN_USER_LIST_SELLECT = " select " +
            "   u.id as id," +
            "   u.email as email, " +
            "   u.status as status, " +
            "   u.name as name, " +
            "   u.account as account, " +
            "   u.password as password, " +
            "   u.mobile as mobile, " +
            "   u.created as created, " +
            "   s.id as storeId,\n"+
            "   s.name as storeName\n";

    private static final String INADMIN_USER_FROM = "from user u left join store s on s.manager_id=u.id \n where u.ACCOUNT_TYPE = 1 and u.status !=3 \n";

    private static final String INMANAGER_USER_LIST_SELLECT = " select " +
            "   u.id as id," +
            "   u.email as email, " +
            "   u.status as status, " +
            "   u.name as name, " +
            "   u.account as account, " +
            "   u.password as password, " +
            "   u.mobile as mobile, " +
            "   u.created as created, " +
            "   u.ACCOUNT_TYPE as accountType, " +
            "   u.BIZ_TYPE as bizType, " +
            "   u.ACCOUNT_ROLE as accountRole \n";

    private static final String INMANAGER_USER_FROM = "from user u \n where u.ACCOUNT_TYPE = 2 and u.status !=3 \n";

    private static final String LIMIT = "limit :page, :size";

    @Override
    public UserDTO getUserById(long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return this.queryForObject(USER_BY_ID, map, new BeanPropertyRowMapper<UserDTO>(UserDTO.class));
    }

    /**
     * admin分管用户-查询用户数量
     *
     * @param param
     * @return
     */
    @Override
    public int getUserCount(Map<String, Object> param) {


        StringBuilder query = new StringBuilder(SELECT_COUNT + INADMIN_USER_FROM);

        getCondition(param, query, false);

        String sql = query.toString();

        Integer count = this.getNamedParameterJdbcTemplate().queryForObject(sql, param, Integer.class);
        return count.intValue();
    }

    /**
     * admin分管用户-查询用户列表
     *
     * @param param
     * @return
     * @author woonill 2018/12/17 update b.name to u.name
     */
    @Override
    public List<UserDTO> getUserListByMap(Map<String, Object> param) {
        StringBuilder query = new StringBuilder(INADMIN_USER_LIST_SELLECT + INADMIN_USER_FROM);
        getCondition(param,query,true);


        String sql = query.toString();
        return this.getNamedParameterJdbcTemplate().query(sql, param, new BeanPropertyRowMapper<UserDTO>(UserDTO.class));
        // return this.getNamedParameterJdbcTemplate().queryForList(query, param, UserDTO.class);
    }

    private StringBuilder getCondition(Map<String, Object> param, StringBuilder query, boolean isLimit) {
        String storeId = Objects.toString(param.get("storeId"), null);
        String name = Objects.toString(param.get("name"), null);
        String account = Objects.toString(param.get("account"), null);
        String mobile = Objects.toString(param.get("mobile"), null);

        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));

        if (StringUtils.isNotBlank(name)) {
            query.append(" and u.name like '%' :name '%' \n");
        }

        if (StringUtils.isNotBlank(account)) {
            query.append(" and u.account like '%' :account '%'\n");
        }

        if (StringUtils.isNotBlank(mobile)) {
            query.append(" and u.mobile  like '%' :mobile '%'\n");
        }
        if (StringUtils.isNotBlank(storeId)) {
            query.append(" and s.id is null or s.id=:storeId\n");
        }
        if ("0".equals(storeId)) {
            query.append(" and s.id is null \n");
        }

        query.append(" order by u.created desc ");

        appendPage(page, size,query, param, isLimit);

        return query;
    }



    /**
     * manager分管用户-查询用户数量
     *
     * @param param
     * @return
     */
    public int getUserInManagerCount(Map<String, Object> param) {
        String name = Objects.toString(param.get("name"), null);
        String account = Objects.toString(param.get("account"), null);
        String mobile = Objects.toString(param.get("mobile"), null);

        StringBuffer query = new StringBuffer(SELECT_COUNT + INMANAGER_USER_FROM);

        query.append(" and u.STORE_ID = :storeId \n");

        if (StringUtils.isNotBlank(name)) {
            query.append(" and u.name = :name \n");
        }

        if (StringUtils.isNotBlank(account)) {
            query.append(" and u.account = :account \n");
        }

        if (StringUtils.isNotBlank(mobile)) {
            query.append(" and u.mobile = :mobile \n");
        }
        String sql = query.toString();

        Integer count = this.getNamedParameterJdbcTemplate().queryForObject(sql, param, Integer.class);
        return count.intValue();
    }

    /**
     * manager分管用户-查询用户列表
     *
     * @param param
     * @return
     */
    public List<UserInManagerDTO> getUserInManagerListByMap(Map<String, Object> param) {
        String name = Objects.toString(param.get("name"), null);
        String account = Objects.toString(param.get("account"), null);
        String mobile = Objects.toString(param.get("mobile"), null);
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));

        StringBuilder query = new StringBuilder(INMANAGER_USER_LIST_SELLECT + INMANAGER_USER_FROM);

        query.append(" and u.STORE_ID = :storeId \n");

        if (StringUtils.isNotBlank(name)) {
            query = query.append(" and u.name = :name \n");
        }

        if (StringUtils.isNotBlank(account)) {
            query = query.append(" and u.account = :account \n");
        }

        if (StringUtils.isNotBlank(mobile)) {
            query = query.append(" and u.mobile = :mobile \n");
        }

        query.append(" order by u.created desc ");

        if (page >= 0 && size > 0) {
            int resPage = page;
            if (page > 0) {
                resPage = (resPage - 1) * size;
                param.put("page", resPage);
            }
            query = query.append(LIMIT);
        }
        return this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<UserInManagerDTO>(UserInManagerDTO.class));
    }
}