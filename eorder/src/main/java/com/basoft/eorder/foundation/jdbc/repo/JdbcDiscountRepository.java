package com.basoft.eorder.foundation.jdbc.repo;

import com.basoft.eorder.application.base.BaseService;
import com.basoft.eorder.domain.DiscountRepository;
import com.basoft.eorder.domain.model.activity.discount.Discount;
import com.basoft.eorder.domain.model.activity.discount.DiscountDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
//@Transactional
public class JdbcDiscountRepository extends BaseRepository implements DiscountRepository {
    private BaseService baseService;

    public JdbcDiscountRepository() {
    }

    @Autowired
    public JdbcDiscountRepository(DataSource dataSource, BaseService baserService) {
        super(dataSource);
        this.baseService = baserService;
    }

    /**
     * 折扣新增(折扣和折扣明细)
     *
     * @param discount
     * @param discountDetails
     * @return
     */
    @Override
    @Transactional
    public int saveDiscount(Discount discount, List<DiscountDetail> discountDetails) {
        int insertDiscountCount = this.getJdbcTemplate().update("INSERT INTO ACTY_DISCOUNT (DISC_ID, DISC_NAME, DISC_CHANNEL, CUST_ID, " +
                        "STORE_ID, DISC_RATE, START_TIME, END_TIME, DISC_STATUS, USE_YN, CREATED_DT, CREATED_USER_ID)" +
                        " VALUES (?, ?, ?, ?, ?, ?, STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s'), STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s'), ?, ?, now(), ?)",
                new Object[]{
                        discount.getDiscId(),
                        discount.getDiscName(),
                        discount.getDiscChannel(),
                        discount.getCustId(),
                        discount.getStoreId(),
                        discount.getDiscRate(),
                        discount.getStartTime(),
                        discount.getEndTime(),
                        discount.getDiscStatus(),
                        discount.getUseYn(),
                        discount.getStoreId()
                });

        // 存在明细进行保存
        if (discountDetails != null && discountDetails.size() > 0) {
            // 批量插入折扣明细
            int[] insertDiscountDetailCount = updateDiscountDetails(discountDetails);
            //System.out.println("折扣新增数量：" + insertDiscountCount + ",折扣明细新增数量：" + insertDiscountDetailCount.length);
        }

        return insertDiscountCount;
    }

    /**
     * 折扣修改
     *
     * @param isReform 是否重做产品明细
     * @param discount
     * @param discountDetails
     * @return
     */
    @Override
    @Transactional
    public int updateDiscount(String isReform, Discount discount, List<DiscountDetail> discountDetails) {
        int updateDiscountCount = this.getJdbcTemplate().update("UPDATE ACTY_DISCOUNT SET DISC_NAME=?,DISC_CHANNEL=?,DISC_RATE=?," +
                        "START_TIME=?,END_TIME=?,DISC_STATUS=?,USE_YN=?,MODIFIED_DT=NOW(),MODIFIED_USER_ID=? " +
                        // "WHERE CUST_ID = ? AND STORE_ID = ? AND DISC_ID = ?",
                        "WHERE DISC_ID = ?",
                new Object[]{
                        discount.getDiscName(),
                        discount.getDiscChannel(),
                        discount.getDiscRate(),
                        discount.getStartTime(),
                        discount.getEndTime(),
                        discount.getDiscStatus(),
                        discount.getUseYn(),
                        discount.getStoreId(),
                        discount.getDiscId()
                });

        // 折扣活动修改则进行重构产品明细，如果是状态编号则不需要重构产品明细
        if("y".equals(isReform) || "Y".equals(isReform)){
            // 删除所有的关联明细
            this.getJdbcTemplate().update("DELETE FROM ACTY_DISCOUNT_DETAIL WHERE DISC_ID =?", discount.getDiscId());

            // 存在明细进行保存
            if (discountDetails != null && discountDetails.size() > 0) {
                // 重新插入折扣明细-批量插入
                int[] insertDiscountDetailCount = updateDiscountDetails(discountDetails);
                //System.out.println("折扣新增数量：" + updateDiscountCount + ",折扣明细新增数量：" + insertDiscountDetailCount.length);
            }
        }

        return updateDiscountCount;
    }

    /**
     * 批量插入折扣明细
     *
     * @param discountDetails
     * @return
     */
    private int[] updateDiscountDetails(List<DiscountDetail> discountDetails) {
        int[] insertDiscountDetailCount = this.getJdbcTemplate().batchUpdate(
                "INSERT INTO ACTY_DISCOUNT_DETAIL (DISC_DET_ID, DISC_ID, PROD_ID, PROD_SKU_ID," +
                        " CUST_ID, STORE_ID, DISC_PRICE, CREATED_DT, CREATED_USER_ID)" +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, now(), ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        DiscountDetail discountDetail = discountDetails.get(i);
                        ps.setLong(1, discountDetail.getDiscDetId());
                        ps.setLong(2, discountDetail.getDiscId());
                        ps.setLong(3, discountDetail.getProdId());
                        ps.setLong(4, discountDetail.getProdSkuId());
                        ps.setLong(5, discountDetail.getCustId());
                        ps.setLong(6, discountDetail.getStoreId());
                        ps.setBigDecimal(7, discountDetail.getDiscPrice());
                        //ps.setString(8, discountDetail.getCreatedDt());
                        ps.setString(8, discountDetail.getCreatedUserId());
                    }

                    @Override
                    public int getBatchSize() {
                        return discountDetails.size();
                    }
                });
        return insertDiscountDetailCount;
    }
}