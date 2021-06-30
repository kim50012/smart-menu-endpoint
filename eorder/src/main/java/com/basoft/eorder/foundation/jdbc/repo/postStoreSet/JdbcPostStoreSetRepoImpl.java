package com.basoft.eorder.foundation.jdbc.repo.postStoreSet;


import com.basoft.eorder.domain.model.postStoreSet.PostStoreSet;
import com.basoft.eorder.domain.model.postStoreSet.PostStoreSetDetail;
import com.basoft.eorder.domain.postStoreSet.PostStoreSetRepository;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 商户配送设置表数据库访问层
 *
 * @author DongXifu
 * @since 2020-04-29 10:51:44
 */
@Repository
public class JdbcPostStoreSetRepoImpl extends BaseRepository implements PostStoreSetRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Long insertPostStoreSet(PostStoreSet postStoreSet) {
        String savePostStoreSetSql = "insert  into post_store_set" +
                "(PSS_ID,STORE_ID,SET_NAME_CHN,SET_NAME_KOR,SET_NAME_ENG,TARGET_COUNTRY_NAME,TARGET_COUNTRY_CODE,IS_FREE,FREE_AMOUNT,POST_COM_NAME,POST_COM_CODE,SET_RULE,STATUS,STATUS_TIME,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER)" +
                "values" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(savePostStoreSetSql,
                new Object[]{
                        postStoreSet.getPssId(),
                        postStoreSet.getStoreId(),
                        postStoreSet.getSetNameChn(),
                        postStoreSet.getSetNameKor(),
                        postStoreSet.getSetNameEng(),
                        postStoreSet.getTargetCountryName(),
                        postStoreSet.getTargetCountryCode(),
                        postStoreSet.getIsFree(),
                        postStoreSet.getFreeAmount(),
                        postStoreSet.getPostComName(),
                        postStoreSet.getPostComCode(),
                        postStoreSet.getSetRule(),
                        1,
                        postStoreSet.getStatusTime(),
                        postStoreSet.getCreateTime(),
                        postStoreSet.getCreateUser(),
                        postStoreSet.getUpdateTime(),
                        postStoreSet.getUpdateUser()                 
                });


        insertPostStoreSetDetail(postStoreSet.getPostStoreSetDetailList());
        return postStoreSet.getPssId();
    }


    /**
     * 新增商户配送设置收费明细
     *
     * @param postStoreSetDetails
     * @return
     */
    public  int  insertPostStoreSetDetail(List<PostStoreSetDetail> postStoreSetDetails) {
        String savePostStoreSetDetailSql = "insert  into post_store_set_detail" +
                "(PSS_ID,DETAIL_NO,LOWER_LIMIT,UPPER_LIMIT,CHARGE_FEE,CREATE_USER)" +
                "values" +
                "(?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.batchUpdate(savePostStoreSetDetailSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PostStoreSetDetail postStoreSetDetail = postStoreSetDetails.get(i);
                int num = 1;
                ps.setLong(num++, postStoreSetDetail.getPssId());
                ps.setLong(num++, postStoreSetDetail.getDetailNo());
                ps.setBigDecimal(num++, postStoreSetDetail.getLowerLimit());
                ps.setBigDecimal(num++, postStoreSetDetail.getUpperLimit());
                ps.setBigDecimal(num++, postStoreSetDetail.getChargeFee());
                ps.setString(num++,  postStoreSetDetail.getCreateUser());
            }

            @Override
            public int getBatchSize() {
                return postStoreSetDetails.size();
            }
        });

        return 0 ;
    }


    @Override
    public Long updatePostStoreSet(PostStoreSet postStoreSet) {
        String upPostStoreSetSql = "update post_store_set set " +
                "SET_NAME_CHN = ?," +
                "SET_NAME_KOR = ?," +
                "SET_NAME_ENG = ?," +
                "TARGET_COUNTRY_NAME = ?," +
                "TARGET_COUNTRY_CODE = ?,\n" +
                "IS_FREE = ?," +
                "FREE_AMOUNT = ?," +
                "POST_COM_NAME = ?," +
                "POST_COM_CODE = ?,\n" +
                "SET_RULE = ?," +
                "STATUS = ?," +
                "STATUS_TIME = ?," +
                "UPDATE_USER = ?\n" +
                "where PSS_ID = ?";

        this.jdbcTemplate.update(upPostStoreSetSql,
                new Object[]{
                        postStoreSet.getSetNameChn(),
                        postStoreSet.getSetNameKor(),
                        postStoreSet.getSetNameEng(),
                        postStoreSet.getTargetCountryName(),
                        postStoreSet.getTargetCountryCode(),
                        postStoreSet.getIsFree(),
                        postStoreSet.getFreeAmount(),
                        postStoreSet.getPostComName(),
                        postStoreSet.getPostComCode(),
                        postStoreSet.getSetRule(),
                        postStoreSet.getStatus(),
                        postStoreSet.getStatusTime(),
                        postStoreSet.getUpdateUser(),
                        postStoreSet.getPssId()
                });

        this.jdbcTemplate.update("delete from post_store_set_detail where PSS_ID = ?",postStoreSet.getPssId());
        insertPostStoreSetDetail(postStoreSet.getPostStoreSetDetailList());

        return postStoreSet.getPssId();
    }
    
    @Override
    public int updatePostStoreSetStatus(List<PostStoreSet> postStoreSetList,int status) {
        String upStaSql = "update post_store_set set status=?\n" +
                "where PSS_ID = ?";

        this.jdbcTemplate.batchUpdate(upStaSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PostStoreSet postStoreSet = postStoreSetList.get(i);
                ps.setInt(1, status);
                ps.setLong(2, postStoreSet.getPssId());
            }

            @Override
            public int getBatchSize() {
                return postStoreSetList.size();
            }
        });


        return 0;
    }

}