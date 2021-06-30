package com.basoft.eorder.foundation.jdbc.repo;

import com.basoft.eorder.domain.UserRepository;
import com.basoft.eorder.domain.model.User;
import com.basoft.eorder.interfaces.command.UpdateUserStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class JdbcUserRepoImpl extends BaseRepository implements UserRepository {
    private final static String INSERT_USER = "insert into user(id, name, account, password, mobile,email, created,status) values (?, ?, ?, ?, ?,?, now(),?)";
    private final static String NAMED_INSERT_USER = "insert into user (id, name, account, password, mobile,email, created,status) values (:id, :name, :account, :password,:email, :mobile, now(),:status)";
    private final static String UPDATE_USERNAME_BY_ID = "update user set name = ?,account=?, password=?, mobile=?,email=? where id = ?";

    @Override
    public int insertUser(User user) {
        Object[] obj = {user.getId(), user.getName(), user.getAccount(), user.getPassword(), user.getMobile(),user.getEmail(),user.getStatus().code()};
        return this.getJdbcTemplate().update(INSERT_USER, obj);
    }

    /**
     * 代理商用户信息插入
     *
     * @param user
     * @return
     */
    @Override
    public int insertUserForAgent(User user) {
        String inSql = "insert into user (id, name, account, password, mobile, email, created, ACCOUNT_TYPE, BIZ_TYPE, ACCOUNT_ROLE, STORE_ID, status) values (?,?,?,?,?,?, now(),?,?,?,?,?)";
        Object[] obj = {user.getId(), user.getName(), user.getAccount(), user.getPassword(), user.getMobile(), user.getEmail(), user.getAccountType(), user.getBizType(), user.getAccountRole(), user.getStoreId(), user.getStatus().code()};
        return this.getJdbcTemplate().update(inSql.toString(), obj);
    }

    /*
     * @Author lee
     * @Description TODO
     * @Date 00:23 2018-12-11
     * @Param [user]
     * @return long
    **/
    @Override
    public long insertUserNamedPkValue(User user) {
        MapSqlParameterSource msps = new MapSqlParameterSource();

        msps.addValue("id", user.getId());
        msps.addValue("name", user.getName());
        msps.addValue("account", user.getAccount());
        msps.addValue("mobile", user.getMobile());
        msps.addValue("password", user.getPassword());
        msps.addValue("email",user.getEmail());
        msps.addValue("status",user.getStatus().code());

        KeyHolder keyHolder = new GeneratedKeyHolder();

//        this.getNamedParameterJdbcTemplate().update(NAMED_INSERT_USER, msps, keyHolder, new String[]{"id"});

        this.getNamedParameterJdbcTemplate().update(NAMED_INSERT_USER, msps);
        return user.getId();

//        Map<String, ?> map = BeanUtil.reflectObjectToMap(user);
//        msps.addValues(map);
//        return this.getNamedParameterJdbcTemplate().update(NAMED_INSERT_USER, map);

//        return keyHolder.getKey().longValue();
    }


    @Override
    public int updateUser(User user) {
        // name = ?,account=?, password=?, mobile=?,email=? where id = ?

        Object[] obj = {user.getName(),user.getAccount(),user.getPassword(),user.getMobile(),user.getEmail(), user.getId()};
        return this.getJdbcTemplate().update(UPDATE_USERNAME_BY_ID, obj);
    }

    /**
     * 修改代理商的用户信息
     *
     * @param user
     * @return
     */
    public int updateUserForAgent(User user) {
        StringBuilder upSql = new StringBuilder("update user set name=?, mobile=?, email=? ");
        // 密码不为空
        if (StringUtils.isNotBlank(user.getPassword())) {
            upSql.append(", password=? ");
            upSql.append("where account = ?");
            Object[] obj = {user.getName(), user.getMobile(), user.getEmail(), user.getPassword(), user.getAccount()};
            logger.info("修改代理商用户信息SQL[1]>>>>>>" + upSql.toString());
            return this.getJdbcTemplate().update(upSql.toString(), obj);
        }
        // 密码为空
        else {
            upSql.append("where account = ?");
            logger.info("修改代理商用户信息SQL[2]>>>>>>" + upSql.toString());
            Object[] obj = {user.getName(), user.getMobile(), user.getEmail(), user.getAccount()};
            return this.getJdbcTemplate().update(upSql.toString(), obj);
        }
    }

    public int updateUserPreparedStatementCreator(User user) {
        return this.getJdbcTemplate().update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(UPDATE_USERNAME_BY_ID);
                ps.setString(1, user.getName());
                ps.setLong(2, user.getId());
                return ps;
            }
        });
    }

    public int updateUserPreparedStatementSetter(User user) {
        return this.getJdbcTemplate().update(UPDATE_USERNAME_BY_ID, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, user.getName());
                ps.setLong(2, user.getId());
            }
        });
    }

    @Override
    public int deleteUser(User user) {
        return 0;
    }

    @Override
    public User getUser(Long managerId) {
        final List<User> userList = this.getJdbcTemplate().query(
                "select id,name,account,password,mobile,email,created,status from user where id = ?",new Object[]{managerId}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                return new User.Builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .mobile(resultSet.getString("mobile"))
                        .password(resultSet.getString("password"))
                        .account(resultSet.getString("account"))
                        .email(resultSet.getString("email"))
                        .status(User.Status.get(resultSet.getInt("status")))
                        .build();
            }
        });
        return  userList.isEmpty() ? null : userList.get(0);
    }

    @Override
    public User getUserByAccount(String account) {
        StringBuffer sql = new StringBuffer();

        // 20190709manager系统权限改造
        /*sql.append("select u.id,u.name,u.account,u.password,u.email,u.mobile,u.created,u.status,st.id as storeId,st.store_type as storeType ");
        sql.append("from user u left join store st on st.manager_id=u.id ");
        sql.append("where account = ? and u.status !=2 and u.status !=3 and st.status != 2 and st.status != 3");*/
        sql.append("SELECT u.id,u.name,u.account,u.password,u.email,u.mobile,u.created,u.STATUS,u.ACCOUNT_TYPE as accountType,u.BIZ_TYPE as bizType,u.ACCOUNT_ROLE AS accountRole,st.id AS storeId,st.store_type AS storeType ");
        sql.append("FROM USER u LEFT JOIN store st ON (st.manager_id=u.id OR u.store_id = st.id) and st.status != 3 ");

        // sql.append("where account = ? and u.status !=2 and u.status !=3 and st.status != 2 and st.status != 3");
        // 商户下架和欠费停服务状态manager cms可用的。
        sql.append("where account = ? and u.status !=2 and u.status !=3");

        logger.info("根据用户账号查询用户信息SQL::" + sql.toString());
        final List<User> userList = this.getJdbcTemplate().query(
                sql.toString(),
                new Object[]{account},
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new User.Builder()
                                .id(resultSet.getLong("id"))
                                .name(resultSet.getString("name"))
                                .account(resultSet.getString("account"))
                                .mobile(resultSet.getString("mobile"))
                                .password(resultSet.getString("password"))
                                .email(resultSet.getString("email"))
                                .status(User.Status.get(resultSet.getInt("status")))
                                .storeId(resultSet.getLong("storeId"))
                                .storeType(resultSet.getInt("storeType"))
                                .accountType(resultSet.getInt("accountType"))
                                .bizType(resultSet.getInt("bizType"))
                                .accountRole(resultSet.getInt("accountRole"))
                                .build();
                    }
                });
        return userList.isEmpty() ? null : userList.get(0);
    }

    /**
     * 根据ID获取用户
     *
     * @param managerId
     * @return UserSession
     */
    public User getUserByManagerId(String managerId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT u.id,u.name,u.account,u.password,u.email,u.mobile,u.created,u.STATUS,u.ACCOUNT_TYPE as accountType,u.BIZ_TYPE as bizType,u.ACCOUNT_ROLE AS accountRole,st.id AS storeId,st.store_type AS storeType ");
        sql.append("FROM USER u JOIN store st ON st.manager_id=u.id OR u.store_id = st.id ");
        sql.append("where u.id = ? and u.status !=2 and u.status !=3 and st.status != 2 and st.status != 3");
        logger.info("根据用户账号查询用户信息SQL::" + sql.toString());
        final List<User> userList = this.getJdbcTemplate().query(
                sql.toString(),
                new Object[]{managerId},
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new User.Builder()
                                .id(resultSet.getLong("id"))
                                .name(resultSet.getString("name"))
                                .account(resultSet.getString("account"))
                                .mobile(resultSet.getString("mobile"))
                                .password(resultSet.getString("password"))
                                .email(resultSet.getString("email"))
                                .status(User.Status.get(resultSet.getInt("status")))
                                .storeId(resultSet.getLong("storeId"))
                                .storeType(resultSet.getInt("storeType"))
                                .accountType(resultSet.getInt("accountType"))
                                .bizType(resultSet.getInt("bizType"))
                                .accountRole(resultSet.getInt("accountRole"))
                                .build();
                    }
                });
        return userList.isEmpty() ? null : userList.get(0);
    }

    @Override
    public void deleteUser(Long id) {
        this.getJdbcTemplate().update("delete from user where id = ?",new Object[]{id});
    }

    /**
     * 用户状态变更-适应用于Admin端和Manager端的门店用户管理
     *
     * @param userStatus
     * @return
     */
    @Override
    public int updateUserStatus(UpdateUserStatus userStatus) {
        this.getJdbcTemplate().batchUpdate("update user set status = ? where id=? ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Long id = userStatus.getUserIds().get(i);
                preparedStatement.setInt(1,userStatus.getStatus());
                preparedStatement.setLong(2,id);
            }

            @Override
            public int getBatchSize() {
                return userStatus.getUserIds().size();
            }
        });
        return userStatus.getUserIds().size();
    }

    @Override
    public int delUserByAccount(String account) {
        Object[] obj = {account};
        String sql = "update user set status = 3 where account=? ";
        return this.getJdbcTemplate().update(sql, obj);

    }

    /**
     * 根据账号获取用户
     *
     * @param account
     * @return
     */
    public User checkAccountIsExist(String account){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT u.id,u.name,u.account,u.password,u.email,u.mobile,u.created,u.STATUS,u.ACCOUNT_TYPE as accountType,u.BIZ_TYPE as bizType,u.ACCOUNT_ROLE AS accountRole,u.STORE_ID AS storeId ");
        sql.append("FROM USER u ");
        sql.append("where u.account = ? and u.status !=3");
        logger.info("根据用户账号查询用户信息SQL::" + sql.toString());
        final List<User> userList = this.getJdbcTemplate().query(
                sql.toString(),
                new Object[]{account},
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new User.Builder()
                                .id(resultSet.getLong("id"))
                                .name(resultSet.getString("name"))
                                .account(resultSet.getString("account"))
                                .mobile(resultSet.getString("mobile"))
                                .password(resultSet.getString("password"))
                                .email(resultSet.getString("email"))
                                .status(User.Status.get(resultSet.getInt("status")))
                                .storeId(resultSet.getLong("storeId"))
                                .accountType(resultSet.getInt("accountType"))
                                .bizType(resultSet.getInt("bizType"))
                                .accountRole(resultSet.getInt("accountRole"))
                                .build();
                    }
                });
        return userList.isEmpty() ? null : userList.get(0);
    }

    //  Manager端-门店用户信息
    private final static String INSERT_USER_IN_MANAGER
            = "insert into user(id, name, account, password, mobile, email, created, status, ACCOUNT_TYPE, BIZ_TYPE, ACCOUNT_ROLE, STORE_ID) values (?, ?, ?, ?, ?,?, now(),?,?,?,?,?)";

    /**
     * Manager端-门店用户信息
     *
     * @param user
     * @return
     */
    public int insertUserInManager(User user) {
        Object[] obj = {user.getId(), user.getName(), user.getAccount(),
                user.getPassword(), user.getMobile(), user.getEmail(), user.getStatus().code(),
                user.getAccountType(), user.getBizType(), user.getAccountRole(), user.getStoreId()};
        return this.getJdbcTemplate().update(INSERT_USER_IN_MANAGER, obj);
    }

    // Manager端-更新用户信息
    private final static String UPDATE_USERNAME_BY_ID_IN_MANAGER
            = "update user set name = ?,account=?, password=?, mobile=?,email=?,ACCOUNT_TYPE=?,BIZ_TYPE=?,ACCOUNT_ROLE=?,STORE_ID=? where id = ?";

    /**
     * Manager端-更新用户信息
     *
     * @param user
     * @return
     */
    public int updateUserInManager(User user) {
        Object[] obj = {user.getName(), user.getAccount(),
                user.getPassword(), user.getMobile(), user.getEmail(),
                user.getAccountType(), user.getBizType(), user.getAccountRole(), user.getStoreId(),
                user.getId()};
        return this.getJdbcTemplate().update(UPDATE_USERNAME_BY_ID_IN_MANAGER, obj);
    }

    /**
     * 修改密码
     *
     * @param account
     * @param password
     * @return
     */
    @Override
    public int updateUser(String account, String password) {
        Object[] obj = {password, account};
        return this.getJdbcTemplate().update("update user set password=? where account = ?", obj);
    }
}
