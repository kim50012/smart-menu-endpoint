package com.basoft.eorder.foundation.jdbc.repo;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.wx.model.WxPayCloseResp;
import com.basoft.eorder.batch.lock.RedissonUtil;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.InventoryHotelRepository;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.model.inventory.hotel.InventoryHotel;
import com.basoft.eorder.util.DateUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class JdbcInventoryHotelRepoImpl extends BaseRepository implements InventoryHotelRepository {
    @Autowired
    private RedissonUtil redissonUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    StoreRepository storeRepository;

    final private static String saveInventoryHotelSql = "insert  into inventory_hotel" +
        "(       " +
        " INV_ID," +
        " STORE_ID," +
        " PROD_ID," +
        " PROD_SKU_ID," +
        " PRICE," +
        " DIS_PRICE," +
        " INV_YEAR," +
        " INV_MONTH," +
        " INV_DAY," +
        " INV_DATE," +
        " IS_OPENING," +
        " INV_TOTAL," +
        " INV_USED," +
        " INV_BALANCE," +
        " CREATE_TIME," +
        " CREATE_USER )" +
        "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?)";

    @Override
    @Transactional
    public int saveInventoryHotel(InventoryHotel inventoryHotel) {
        int result = this.jdbcTemplate.update(saveInventoryHotelSql,
            new Object[]{
                inventoryHotel.getInvId(),
                inventoryHotel.getStoreId(),
                inventoryHotel.getProdId(),
                inventoryHotel.getProdSkuId(),
                inventoryHotel.getPrice(),
                inventoryHotel.getDisPrice(),
                inventoryHotel.getInvYear(),
                inventoryHotel.getInvMonth(),
                inventoryHotel.getInvDay(),
                inventoryHotel.getInvDate(),
                inventoryHotel.getIsOpening(),
                inventoryHotel.getInvTotal(),
                inventoryHotel.getInvUsed(),
                inventoryHotel.getInvBalance(),
                inventoryHotel.getCreateUser(),
            });
        return result;
    }

    @Override
    public int saveInventoryHotels(List<InventoryHotel> inventoryHotels) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert  into inventory_hotel(\n");
        sql.append(" INV_ID,");
        sql.append(" STORE_ID,");
        sql.append(" PROD_ID,");
        sql.append(" PROD_SKU_ID,\n");
       // if (inventoryHotels.get(0).getPrice() != null)
            sql.append("PRICE,");
       // if (inventoryHotels.get(0).getDisPrice() != null)
            sql.append(" DIS_PRICE,");
        sql.append(" PRICE_SETTLE,");
        sql.append(" DIS_PRICE_SETTLE,");
        sql.append(" INV_YEAR,");
        sql.append(" INV_MONTH,");
        sql.append(" INV_DAY,");
        sql.append(" INV_DATE,\n");
        if (inventoryHotels.get(0).getIsOpening() != null)
            sql.append(" IS_OPENING,\n");
        if (inventoryHotels.get(0).getInvTotal() != null)
            sql.append(" INV_TOTAL,\n");
        //if (inventoryHotels.get(0).getInvUsed() != null)
            sql.append(" INV_USED,\n");
        //if (inventoryHotels.get(0).getInvBalance() != null)
            sql.append(" INV_BALANCE,");
        sql.append(" CREATE_TIME,");
        sql.append(" CREATE_USER )");
        sql.append("values (");
        sql.append("?,?,?,?,");
        //if (inventoryHotels.get(0).getPrice() != null)
            sql.append("?,");
        //if (inventoryHotels.get(0).getDisPrice() != null)
            sql.append("?,");
        sql.append("?,?,?,?,?,?,");
        if (inventoryHotels.get(0).getIsOpening() != null)
            sql.append("?,");
        if (inventoryHotels.get(0).getInvTotal() != null)
            sql.append("?,");
       // if (inventoryHotels.get(0).getInvUsed() != null)
            sql.append("?,");
       // if (inventoryHotels.get(0).getInvBalance() != null)
            sql.append("?,");
        sql.append("now(),");
        sql.append("?)");

        final int[] ints = this.getJdbcTemplate().batchUpdate(sql.toString(),
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    InventoryHotel hotels = inventoryHotels.get(i);
                    int k = 1;
                    preparedStatement.setLong(k++, hotels.getInvId());
                    preparedStatement.setLong(k++, hotels.getStoreId());
                    preparedStatement.setLong(k++, hotels.getProdId());
                    preparedStatement.setLong(k++, hotels.getProdSkuId());
                    if (inventoryHotels.get(0).getPrice() == null){
                        preparedStatement.setBigDecimal(k++, new BigDecimal(-1));
                    }else {
                        preparedStatement.setBigDecimal(k++, hotels.getPrice());
                    }
                    if (inventoryHotels.get(0).getDisPrice() == null){
                        preparedStatement.setBigDecimal(k++, new BigDecimal(-1));
                    }else {
                        preparedStatement.setBigDecimal(k++, hotels.getDisPrice());

                    }
                    if (inventoryHotels.get(0).getPriceSettle() == null) {
                        preparedStatement.setBigDecimal(k++, new BigDecimal(-1));
                    }else {
                        preparedStatement.setBigDecimal(k++, hotels.getPriceSettle());
                    }

                    if (inventoryHotels.get(0).getDisPriceSettle() == null) {
                        preparedStatement.setBigDecimal(k++, new BigDecimal(-1));
                    }else {
                        preparedStatement.setBigDecimal(k++, hotels.getDisPriceSettle());
                    }
                    preparedStatement.setInt(k++, hotels.getInvYear());
                    preparedStatement.setInt(k++, hotels.getInvMonth());
                    preparedStatement.setInt(k++, hotels.getInvDay());
                    preparedStatement.setString(k++, hotels.getInvDate());
                    if (inventoryHotels.get(0).getIsOpening() != null)
                        preparedStatement.setInt(k++, hotels.getIsOpening());
                    if (inventoryHotels.get(0).getInvTotal() != null)
                        preparedStatement.setInt(k++, hotels.getInvTotal());
                    if (inventoryHotels.get(0).getInvUsed() == null){
                        preparedStatement.setInt(k++, 0);
                    }else {
                        preparedStatement.setInt(k++, hotels.getInvUsed());
                    }
                    if (inventoryHotels.get(0).getInvBalance() == null) {
                        preparedStatement.setInt(k++, 0);
                    } else {
                        preparedStatement.setInt(k++, hotels.getInvBalance());
                    }
                    preparedStatement.setString(k++, hotels.getCreateUser());
                }

                @Override
                public int getBatchSize() {
                    return inventoryHotels.size();
                }
            });

        return inventoryHotels.size();
    }

    @Override
    public int upInventoryHotels(List<InventoryHotel> inventoryHotels) {
        StringBuilder upSql = new StringBuilder();
        upSql.append("update inventory_hotel set \n");
        if (inventoryHotels.get(0).getPrice() != null) {
            upSql.append(" price = ?,\n");
        }

        if (inventoryHotels.get(0).getDisPrice() != null) {
            upSql.append(" dis_price = ?,\n");
        }

        if (inventoryHotels.get(0).getPriceSettle() != null) {
            upSql.append(" PRICE_SETTLE = ?,\n");
        }

        if (inventoryHotels.get(0).getDisPriceSettle() != null) {
            upSql.append(" DIS_PRICE_SETTLE = ?,\n");
        }

        if (inventoryHotels.get(0).getInvTotal() != null) {
            upSql.append(" INV_TOTAL = ?,\n");
        }
        if (inventoryHotels.get(0).getIsOpening() != null) {
            upSql.append(" IS_OPENING=?,\n");
        }
        upSql.append(" UPDATE_TIME = now(),UPDATE_USER=? \n" +
            "where STORE_ID = ? and INV_DATE = ? and PROD_ID=? and PROD_SKU_ID=? ");
        final int[] ints = this.getJdbcTemplate().batchUpdate(upSql.toString(),
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    InventoryHotel hotels = inventoryHotels.get(i);
                    int k = 1;
                    if (inventoryHotels.get(0).getPrice() != null) {
                        preparedStatement.setBigDecimal(k++, hotels.getPrice());
                    }
                    if (inventoryHotels.get(0).getDisPrice() != null) {
                        preparedStatement.setBigDecimal(k++, hotels.getDisPrice());
                    }
                    if (inventoryHotels.get(0).getPriceSettle() != null) {
                        preparedStatement.setBigDecimal(k++, hotels.getPriceSettle());
                    }
                    if (inventoryHotels.get(0).getDisPriceSettle() != null) {
                        preparedStatement.setBigDecimal(k++, hotels.getDisPriceSettle());
                    }
                    if (inventoryHotels.get(0).getInvTotal() != null) {
                        preparedStatement.setInt(k++, hotels.getInvTotal());
                    }
                    if (inventoryHotels.get(0).getIsOpening() != null) {
                        preparedStatement.setInt(k++, hotels.getIsOpening());
                    }
                    preparedStatement.setString(k++, hotels.getUpdateUser());
                    preparedStatement.setLong(k++, hotels.getStoreId());
                    preparedStatement.setString(k++, hotels.getInvDate());
                    preparedStatement.setLong(k++, hotels.getProdId());
                    preparedStatement.setLong(k++, hotels.getProdSkuId());
                }

                @Override
                public int getBatchSize() {
                    return inventoryHotels.size();
                }
            });

        return inventoryHotels.size();
    }

    @Override
    public int BatchUpdateInventHotelStatus(List<InventoryHotel> inventoryHotels) {
        String updateSql = "update inventory_hotel set IS_OPENING = ?,UPDATE_USER = ?,UPDATE_TIME = now() " +
            "where INV_DATE= ? and PROD_ID = ? and PROD_SKU_ID = ?";
        final int[] ints = this.getJdbcTemplate().batchUpdate(updateSql,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    InventoryHotel hotels = inventoryHotels.get(i);
                    preparedStatement.setInt(1, hotels.getIsOpening());
                    preparedStatement.setString(2, hotels.getUpdateUser());
                    preparedStatement.setString(3, hotels.getInvDate());
                    preparedStatement.setLong(4, hotels.getProdId());
                    preparedStatement.setLong(5, hotels.getProdSkuId());
                }

                @Override
                public int getBatchSize() {
                    return inventoryHotels.size();
                }
            });

        return inventoryHotels.size();
    }

    /**
     * 恢复酒店库存，恢复数量1
     *
     * @param transId
     * @param storeId
     * @param prodSkuId
     * @param fromDate
     * @param toDate
     * @param recoverType
     */
    @Transactional
    public void recoverHotelInventory(Long transId, Long storeId, Long prodSkuId, String fromDate,
                                      String toDate, String recoverType, AppConfigure appConfigure) {
        // 1、关闭订单，如果为未支付订单库存的恢复，需要先关闭订单，然后进行库存恢复
        if (CommonConstants.HOTEL_INVENTORY_RECOVER_AUTO.equals(recoverType)) {
            //注释关闭订单功能1 Store store = storeRepository.getStore(storeId);
            WxPayCloseResp resp = null;
            try {
                //注释关闭订单功能2 resp = WechatPayAPI.closeOrderRight(String.valueOf(transId), store, appConfigure.get(AppConfigure.BASOFT_WECHAT_CERT_PATH));

                if (resp != null) {
                    if ("SUCCESS".equals(resp.getReturn_code())) {
                        if ("SUCCESS".equals(resp.getResult_code())) {
                            log.info("【酒店库存恢复】订单{}关闭订单成功", transId);
                        } else {
                            // 退款异常，直接返回，不恢复库存
                            return;
                        }
                    } else {
                        // 退款异常，直接返回，不恢复库存
                        return;
                    }
                } else {
                    // 退款异常，直接返回，不恢复库存
                    //注释关闭订单功能3 return;
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                // 退款异常，直接返回，不恢复库存
                return;
            }
        }
        // 退款不需要关闭订单
        else {
            // nothing to do
        }

        // 2、恢复库存
        try {
            // findDataAll抛出转换异常
            List<String> dateList = DateUtil.findDataAll(fromDate, toDate, 1);



            if (dateList == null || dateList.size() == 0) {
                log.info("【酒店库存恢复】【{}】无库存恢复的日期##############################@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", transId);
            } else {
                // 库存削减不恢复最后一天toDate的。
                dateList.remove(dateList.size() - 1);
                log.info("【酒店库存恢复】【{}】需要恢复库存的日期>>> " + "【" + dateList.size() + "】" + dateList, transId);



                // 库存恢复SQL
                // String sql = "UPDATE INVENTORY_HOTEL SET INV_USED = INV_USED - 1, UPDATE_TIME = NOW(), UPDATE_USER = 'recoverBatch' WHERE STORE_ID = ? AND PROD_SKU_ID = ? AND DATE_FORMAT(INV_DATE,'%Y-%m-%d') = ? AND INV_USED > 0";
                StringBuilder sql = new StringBuilder("UPDATE INVENTORY_HOTEL SET INV_USED = INV_USED - 1, UPDATE_TIME = NOW(), UPDATE_USER =");
                if (CommonConstants.HOTEL_INVENTORY_RECOVER_AUTO.equals(recoverType)) {
                    sql.append("'recoverBatch'");
                } else {
                    sql.append("'refundBatch'");
                }
                sql.append("WHERE STORE_ID = ? AND PROD_SKU_ID = ? AND DATE_FORMAT(INV_DATE,'%Y-%m-%d') = ? AND INV_USED > 0");



                // 库存恢复---------------------------------------------------------------------start
                StringBuilder hotelLockKey = new StringBuilder(CommonConstants.HOTEL_INVENTORY_LOCK).append(storeId);
                // 获取库存恢复锁，并且防止死锁。60秒后自动释放。
                RLock lock = redissonUtil.getFairLock(hotelLockKey.toString());
                lock.lock(60, TimeUnit.SECONDS);

                try {
                    // 批量更新庫存
                    this.getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                            preparedStatement.setLong(1, storeId);
                            preparedStatement.setLong(2, prodSkuId);
                            preparedStatement.setString(3, dateList.get(i));
                        }

                        @Override
                        public int getBatchSize() {
                            return dateList.size();
                        }
                    });

                    // 更新恢復狀態
                    if (CommonConstants.HOTEL_INVENTORY_RECOVER_AUTO.equals(recoverType)) {
                        Map<String, Object> param = Maps.newHashMap();
                        param.put("transId", transId);
                        this.getNamedParameterJdbcTemplate().update("UPDATE order_temp SET IS_RECOVER = 1 WHERE trans_id = :transId", param);
                    }
                } finally {
                    // lock.unlock();
                    if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
                        lock.unlock();
                        log.info("【酒店库存恢复】【{}】恢复库存的锁释放完毕", transId);
                    }
                }
                // 库存恢复---------------------------------------------------------------------end
            }
        } catch (ParseException e) {
            log.error("【酒店库存恢复】库存恢复时对日期转换异常>>> " + e.getMessage(), e);
        }
    }
}