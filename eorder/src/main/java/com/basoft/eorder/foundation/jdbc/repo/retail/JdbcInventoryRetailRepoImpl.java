package com.basoft.eorder.foundation.jdbc.repo.retail;

import com.basoft.eorder.batch.job.model.retail.RetailToDoRecoverTempOrder;
//import com.basoft.eorder.batch.lock.RedissonUtil;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.model.retail.InventoryRetail;
import com.basoft.eorder.domain.retail.InventoryRetailRepository;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.command.retail.SaveInventoryRetail;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 零售产品库存表数据库访问层
 *
 * @author DongXifu
 * @since 2020-04-07 17:51:23
 */
@Slf4j
@Repository
public class JdbcInventoryRetailRepoImpl extends BaseRepository implements InventoryRetailRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @Autowired
//    private RedissonUtil redissonUtil;

    @Override
    public Long insertInventoryRetail(InventoryRetail inventoryRetail) {
        String saveInventoryRetailSql = "insert  into inventory_retail" +
                "(STORE_ID,PROD_ID,PROD_SKU_ID,INV_TOTAL,INV_SOLD,INV_BALANCE,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER)" +
                "values" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(saveInventoryRetailSql,
                new Object[]{
                        inventoryRetail.getStoreId(),
                        inventoryRetail.getProdId(),
                        inventoryRetail.getProdSkuId(),
                        inventoryRetail.getInvTotal(),
                        inventoryRetail.getInvSold(),
                        inventoryRetail.getInvBalance(),
                        inventoryRetail.getCreateTime(),
                        inventoryRetail.getCreateUser(),
                        inventoryRetail.getUpdateTime(),
                        inventoryRetail.getUpdateUser()                 
                });

        return inventoryRetail.getInvId();
    }

    @Override
    public Long saveInventoryRetail(SaveInventoryRetail saveInventoryRetail) {

        String upSkuInventory = "update product_sku set is_inventory = ? where id = ? ";
        this.jdbcTemplate.update(upSkuInventory,
                new Object[]{
                        saveInventoryRetail.getIsInventory(),
                        saveInventoryRetail.getProdSkuId()
                });


        String saveInventoryRetailSql = "insert  into inventory_retail" +
                "(STORE_ID,PROD_ID,PROD_SKU_ID,INV_TOTAL,INV_SOLD,INV_BALANCE,CREATE_TIME,CREATE_USER)" +
                "values" +
                "(?, ?, ?, ?, 0, ?, now(), ?)\n"+
                "on duplicate key\n"+
                "update \n"+
                "INV_TOTAL=INV_TOTAL+?,INV_BALANCE = INV_BALANCE+? ,UPDATE_TIME = now(),UPDATE_USER = ?";

        this.jdbcTemplate.update(saveInventoryRetailSql,
                new Object[]{
                        saveInventoryRetail.getStoreId(),
                        saveInventoryRetail.getProdId(),
                        saveInventoryRetail.getProdSkuId(),
                        saveInventoryRetail.getInvTotal(),
                        saveInventoryRetail.getInvTotal(),
                        saveInventoryRetail.getAcoount(),
                        saveInventoryRetail.getNum(),
                        saveInventoryRetail.getNum(),
                        saveInventoryRetail.getAcoount()
                });

        return saveInventoryRetail.getProdId();
    }

    @Override
    public Long saveInventoryRetails(List<InventoryRetail> inventoryRetails) {

        String saveInventoryRetailSql = "insert  into inventory_retail" +
                "(STORE_ID,PROD_ID,PROD_SKU_ID,INV_TOTAL,INV_SOLD,INV_BALANCE,CREATE_TIME,CREATE_USER)" +
                "values" +
                "(?, ?, ?, ?, 0, ?, now(), ?)\n"+
                "on duplicate key\n"+
                "update \n"+
                "UPDATE_TIME = now(),UPDATE_USER = ?";

        this.jdbcTemplate.batchUpdate(saveInventoryRetailSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                InventoryRetail inventoryRetail = inventoryRetails.get(i);
                int num = 1;
                ps.setLong(num++, inventoryRetail.getStoreId());
                ps.setLong(num++, inventoryRetail.getProdId());
                ps.setLong(num++, inventoryRetail.getProdSkuId());
                ps.setLong(num++, inventoryRetail.getInvTotal());
                ps.setLong(num++, inventoryRetail.getInvBalance());
                ps.setString(num++, inventoryRetail.getCreateUser());
                ps.setString(num++, inventoryRetail.getUpdateUser());
            }

            @Override
            public int getBatchSize() {
                return inventoryRetails.size();
            }
        });

        return null;
    }

    @Override
    public Long updateInventoryRetail(InventoryRetail inventoryRetail) {
        String upInventoryRetailSql = "update inventory_retail set " +
                "STORE_ID = ?," +
                "PROD_ID = ?," +
                "PROD_SKU_ID = ?," +
                "INV_TOTAL = ?," +
                "INV_SOLD = ?," +
                "INV_BALANCE = ?," +
                "CREATE_TIME = ?," +
                "CREATE_USER = ?," +
                "UPDATE_TIME = ?," +
                "UPDATE_USER = ?," +
                "where INV_ID = invId";

        this.jdbcTemplate.update(upInventoryRetailSql,
                new Object[]{
                        inventoryRetail.getStoreId(),
                        inventoryRetail.getProdId(),
                        inventoryRetail.getProdSkuId(),
                        inventoryRetail.getInvTotal(),
                        inventoryRetail.getInvSold(),
                        inventoryRetail.getInvBalance(),
                        inventoryRetail.getCreateTime(),
                        inventoryRetail.getCreateUser(),
                        inventoryRetail.getUpdateTime(),
                        inventoryRetail.getUpdateUser()                  
                });

        return inventoryRetail.getInvId();
    }
    
    @Override
    public Long updateInventoryRetailStatus(InventoryRetail inventoryRetail) {
        String upStaSql = "update inventory_retail set " +
                "where INV_ID = ?";
        this.jdbcTemplate.update(upStaSql,
                new Object[]{
                        inventoryRetail.getInvId(),
                });
        return inventoryRetail.getInvId();
    }

    /**
     * 零售业务：库存恢复
     *
     * @param orderTempList
     * @param recoverType
     */
    public void recoverRetailInventory(List<RetailToDoRecoverTempOrder> orderTempList, String recoverType) {
        if (orderTempList == null || orderTempList.size() == 0) {
            return;
        }

        // 商户ID
        Long storeId = orderTempList.get(0).getStoreId();
        // 订单ID
        Long transId = orderTempList.get(0).getTransId();

        // 库存恢复SQL
        StringBuilder sql = new StringBuilder("UPDATE INVENTORY_RETAIL SET INV_BALANCE = INV_BALANCE + ?, INV_SOLD = INV_SOLD - ?, UPDATE_TIME = NOW(), UPDATE_USER = ? WHERE STORE_ID = ? AND PROD_SKU_ID = ?");

        StringBuilder hotelLockKey = new StringBuilder(CommonConstants.RETAIL_INVENTORY_LOCK).append(storeId);
        // 获取库存恢复锁，并且防止死锁。60秒后自动释放。
//        RLock lock = redissonUtil.getFairLock(hotelLockKey.toString());
//        lock.lock(60, TimeUnit.SECONDS);

        try {
            // 批量更新庫存
            this.getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    preparedStatement.setLong(1, orderTempList.get(i).getQty());
                    preparedStatement.setLong(2, orderTempList.get(i).getQty());
                    if (CommonConstants.HOTEL_INVENTORY_RECOVER_AUTO.equals(recoverType)) {
                        preparedStatement.setString(3, "recoverBatch");
                    } else {
                        preparedStatement.setString(3, "refundBatch");
                    }
                    preparedStatement.setLong(4, storeId);
                    preparedStatement.setLong(5, orderTempList.get(i).getSkuId());
                }

                @Override
                public int getBatchSize() {
                    return orderTempList.size();
                }
            });

            // 定时任务恢复库存后，对订单的恢復狀態进行更新
            if (CommonConstants.HOTEL_INVENTORY_RECOVER_AUTO.equals(recoverType)) {
                Map<String, Object> param = Maps.newHashMap();
                param.put("transId", transId);
                this.getNamedParameterJdbcTemplate().update("UPDATE order_temp SET IS_RECOVER = 1 WHERE trans_id = :transId", param);
            }
        } finally {
            // lock.unlock();
//            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//                log.info("【零售业务产品库存恢复】【{}】恢复库存的锁释放完毕", transId);
//            }
        }
    }
}