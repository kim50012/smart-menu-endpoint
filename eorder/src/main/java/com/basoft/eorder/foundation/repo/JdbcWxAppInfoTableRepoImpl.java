package com.basoft.eorder.foundation.repo;

import com.basoft.eorder.domain.WxAppInfoTableRepository;
import com.basoft.eorder.domain.model.WxAppInfoTable;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:26 2018/12/13
 **/

@Repository
@Transactional
public class JdbcWxAppInfoTableRepoImpl extends BaseRepository implements WxAppInfoTableRepository {

    @Override
    public WxAppInfoTable getWxAppInfoTable(String sysId) {
        String tableSql = "select sys_id ,shop_id ,original_app_id ,comp_nm ,app_id ,app_secret ,url ,token ,encording_aes_key from bawechat.wx_app_info where sys_id=?";
        final List<WxAppInfoTable> tableList = this.getJdbcTemplate().query(tableSql,new Object[]{sysId}, new RowMapper<WxAppInfoTable>() {
            @Override
            public WxAppInfoTable mapRow(ResultSet resultSet, int i) throws SQLException {
                return new WxAppInfoTable.Builder()
                		.setAppId(resultSet.getString("app_id"))
                		.setAppSecret(resultSet.getString("app_secret"))
                		.setCompNm(resultSet.getString("comp_nm"))
                		.setEncordingAesKey(resultSet.getString("encording_aes_key"))
                		.setOriginalAppId(resultSet.getString("original_app_id"))
                		.setShopId(resultSet.getLong("shop_id"))
                		.setSysId(resultSet.getString("sys_id"))
                		.setToken(resultSet.getString("token"))
                		.setUrl(resultSet.getString("url"))
                        .build();
            }
        });
        return  tableList.isEmpty() ? null : tableList.get(0);
    }


}
