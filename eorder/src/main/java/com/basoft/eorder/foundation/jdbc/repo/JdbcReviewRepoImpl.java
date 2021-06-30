package com.basoft.eorder.foundation.jdbc.repo;

import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.domain.ReviewRepository;
import com.basoft.eorder.domain.model.Review;
import com.basoft.eorder.interfaces.command.review.BatchUpRevImgStatus;
import com.basoft.eorder.interfaces.command.BatchUpReviewStatus;
import com.basoft.eorder.interfaces.command.TransReview;
import com.basoft.eorder.util.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:57 2019/5/13
 **/

@Repository
@Transactional
public class JdbcReviewRepoImpl extends BaseRepository implements ReviewRepository{

    @Autowired
    private UidGenerator uidGenerator;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    @Transactional
    public int saveReview(Review review) {
        String saveReviewSql="insert into str_review ( " +
            " REV_ID" +
            ", PLATFORM_ID" +
            ", NICKNAME" +
            ", CHATHEAD" +
            ", REV_PLATFORM " +
            ", ORDER_ID" +
            ", CUST_ID" +
            ", STORE_ID" +
            ", REV_CLASS" +
            ", REV_TAG" +
            ", ENV_CLASS" +
            ", PROD_CLASS" +
            ", SERVICE_CLASS" +
            ", AVER_PRICE" +
            ", IS_ANONYMITY" +
            ", REV_TIME" +
            ", REV_STATUS" +
            ", REV_REPLIER" +
            ", REV_REPLY_TIME" +
            ", MODIFIED_USER_ID" +
            ", MODIFIED_DT" +
            ", REV_CONTENT" +
            ", REV_REPLY_CONTENT"+
            ")"+
            " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,now(),?,?)";

       int result =  this.jdbcTemplate.update(saveReviewSql,
            new Object[]{
                review.getRevId(),
                review.getPlatformId(),
                review.getNickname(),
                review.getChathead(),
                review.getRevPlatform(),
                review.getOrderId(),
                review.getCustId(),
                review.getStoreId(),
                review.getRevClass(),
                review.getRevTag(),
                review.getEnvClass(),
                review.getProdClass(),
                review.getServiceClass(),
                review.getAverPrice(),
                review.getIsAnonymity(),
                review.getRevStatus(),
                review.getRevReplier(),
                review.getRevReplyTime(),
                review.getModifiedUserId(),
                review.getRevContent(),
                 review.getRevReplyContent()
            });

          updateReviewImages(review);
        return result;
    }


    @Override
    public int updateReview(Review review) {
        String upReviewSql = "update str_review set REV_REPLY_CONTENT=?,REV_REPLIER=?,REV_REPLY_TIME= now(),REV_STATUS=?," +
            "MODIFIED_USER_ID=?,MODIFIED_DT= now()" +
            " where REV_ID=?";
        return this.jdbcTemplate.update(upReviewSql,
            new Object[]{
                review.getRevReplyContent(),
                review.getRevReplier(),
                review.getRevStatus(),
                review.getModifiedUserId(),
                review.getRevId()
            }
            );
    }

    @Override
    @Transactional
    public int bachUpReviewStatus(BatchUpReviewStatus upReviewStatus) {
        String sql = "update str_review set REV_STATUS = ? where rev_id=?";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
               String id =  upReviewStatus.getRevIds().get(i);
                preparedStatement.setString(1,Review.REV_STATUS_DEL);
                preparedStatement.setString(2,id);
            }

            @Override
            public int getBatchSize() {
                return upReviewStatus.getRevIds().size();
            }
        });

        bachUpRevImgStatusByRevId(upReviewStatus);

        return upReviewStatus.getRevIds().size();
    }


    @Transactional
    public int updateReviewImages(Review review) {

        List<Review.RevImg> images = review.getRevImageList();
        images.stream().map(i ->
        {i.setRevId(review.getRevId()); return i; } ).collect(Collectors.toList());

        String saveImgSql = "insert into str_review_attach (" +
            "  REV_ATTACH_ID" +
            ", REV_ID" +
            ", CONTENT_URL" +
            ", DISPLAY_ORDER" +
            ", IS_DISPLAY" +
            ", CREATED_DT" +
            ")"+
            " values(?,?,?,?,?,now())";


        //this.jdbcTemplate.update("delete from str_review_attach where REV_ATTACH_ID = ?",review.getRevId());
        this.jdbcTemplate.batchUpdate(saveImgSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
               Long revAttachId = uidGenerator.generate(BusinessTypeEnum.REVIEW_ATTACH);

                Review.RevImg image = images.get(i);
                ps.setLong(1,revAttachId);
                ps.setLong(2,image.getRevId());
                ps.setString(3,image.getContentUrl());
                ps.setInt(4,image.getDisplayOrder());
                ps.setString(5,"1");
            }

            @Override
            public int getBatchSize() {
                return images.size();
            }
        });
        return images.size();
    }

    @Override
    public int bachUpRevImgStatus(BatchUpRevImgStatus batchUpRevImgStatus) {
        String sql = "update str_review_attach set IS_DISPLAY = ? where REV_ATTACH_ID=?";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                String id =  batchUpRevImgStatus.getRevAttachId().get(i);
                preparedStatement.setString(1,Review.RevImg.REVIMG_HIDE);
                preparedStatement.setString(2,id);
            }

            @Override
            public int getBatchSize() {
                return batchUpRevImgStatus.getRevAttachId().size();
            }
        });

        return batchUpRevImgStatus.getRevAttachId().size();
    }



    /**
     * @Title 根据revId删除附件
     * @Param
     * @return int
     * @author Dong Xifu
     * @date 2019/5/27 下午4:27
     */
    private void bachUpRevImgStatusByRevId(BatchUpReviewStatus reviewStatus) {
        String sql = "update str_review_attach set IS_DISPLAY = ?  where REV_ID = ?";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                String id =  reviewStatus.getRevIds().get(i);
                preparedStatement.setString(1,Review.RevImg.REVIMG_HIDE);
                preparedStatement.setString(2,id);
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        });

    }


    @Override
    public int translateReview(TransReview transReview) {
        String sql = "update str_review set REV_CONTENT_COPIE = ?, REV_REPLY_CONTENT_COPIE = ?  where rev_id = ? ";
        return this.jdbcTemplate.update(sql,
            new Object[]{
                transReview.getRevContentCopie(),
                transReview.getRevReplyContentCopie(),
                transReview.getRevId()
            }
        );
    }
}
