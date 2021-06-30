package com.basoft.eorder.foundation.repo;

import com.basoft.eorder.domain.StoreTableRepository;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.StoreTable;
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
public class JdbcStoreTableRepoImpl extends BaseRepository implements StoreTableRepository {
    @Override
    public StoreTable getStoreTable(Long tableId) {
        String tableSql = "select * from store_table where id=?";
        final List<StoreTable> tableList = this.getJdbcTemplate().query(tableSql,new Object[]{tableId}, new RowMapper<StoreTable>() {
            @Override
            public StoreTable mapRow(ResultSet resultSet, int i) throws SQLException {
                return new StoreTable.Builder()
                        .setId(resultSet.getLong("id"))
                        .setNumber(resultSet.getInt("number"))
                        .setQrCodeImagePath(resultSet.getString("qrcode_image_url"))
                        .setSceneId(resultSet.getInt("table_no"))
                        .setTicket(resultSet.getString("ticket"))
                        .setStore(new Store.Builder().id(resultSet.getLong("store_id")).build())
                        .setOrder(resultSet.getInt("number"))
                        .setQrCodeId(resultSet.getString("qrcode_id"))	//added by dikim 20190215
                        .build();
            }
        });
        return  tableList.isEmpty() ? null : tableList.get(0);
    }

    /**
     *获取门店的静默桌号
     *
     * @param storeId
     * @return
     */
    public StoreTable getSilentStoreTable(Long storeId){
        String tableSql = "select * from store_table where store_id=? and is_silent ='1' and status = '0'";
        final List<StoreTable> tableList = this.getJdbcTemplate().query(tableSql,new Object[]{storeId}, new RowMapper<StoreTable>() {
            @Override
            public StoreTable mapRow(ResultSet resultSet, int i) throws SQLException {
                return new StoreTable.Builder()
                        .setId(resultSet.getLong("id"))
                        .setNumber(resultSet.getInt("number"))
                        .setQrCodeImagePath(resultSet.getString("qrcode_image_url"))
                        .setSceneId(resultSet.getInt("table_no"))
                        .setTicket(resultSet.getString("ticket"))
                        .setStore(new Store.Builder().id(resultSet.getLong("store_id")).build())
                        .setOrder(resultSet.getInt("number"))
                        .setQrCodeId(resultSet.getString("qrcode_id"))	//added by dikim 20190215
                        .build();
            }
        });
        return  tableList.isEmpty() ? null : tableList.get(0);
    }
}
