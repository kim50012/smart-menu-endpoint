package com.basoft.eorder.foundation.jdbc.repo;

import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.domain.AdviceRepository;
import com.basoft.eorder.domain.model.Advice;
import com.basoft.eorder.domain.model.Review;
import com.basoft.eorder.interfaces.command.BatchUpAdviceStatus;
import com.basoft.eorder.interfaces.command.advice.BatchUpAdvImgStatus;
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
 * @Date Created in 下午2:03 2019/5/20
 **/
@Repository
@Transactional
public class JdbcAdviceRepoImpl extends BaseRepository  implements AdviceRepository{



    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UidGenerator uidGenerator;

    @Override
    public int saveAdvice(Advice advice) {
       String sql = " insert into str_advice (ADVI_ID, PLATFORM_ID, NICKNAME,"+
           " CHATHEAD, REV_PLATFORM, ADVI_TOS,"+
           " ADVI_TYPE, STORE_ID,"+
           " LINKER, LINK_PHONE, LINK_EMAIL,"+
           " ADVI_STATUS, ADVI_REPLIER,"+
           " ADVI_CONTENT,ADVI_TIME,"+
           " ADVI_REPLY_CONTENT,MODIFIED_DT,MODIFIED_USER_ID)"+
           " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,now(),?)";
       int result = this.jdbcTemplate.update(sql,
            new Object[]{
                advice.getAdviId(),
                advice.getPlatformId(),
                advice.getNickname(),
                advice.getChathead(),
                advice.getRevPlatform(),
                advice.getAdviTos(),
                advice.getAdviType(),
                advice.getStoreId(),
                advice.getLinker(),
                advice.getLinkPhone(),
                advice.getLinkEmail(),
                advice.getAdviStatus(),
                advice.getAdviReplier(),
                advice.getAdviContent(),
                advice.getAdviReplyContent(),
                advice.getModifiedUserId()
            });

        updateAdviceImages(advice);
        return result;
    }

    @Transactional
    public int updateAdviceImages(Advice advice) {

        List<Advice.AdviceImg> images = advice.getAdviceImgList();
        images.stream().map(i ->
        {i.setAdviId(advice.getAdviId()); return i; } ).collect(Collectors.toList());

        String saveImgSql = "insert into str_advice_attach (" +
            "  ADVI_ATTACH_ID" +
            ", ADVI_ID" +
            ", CONTENT_URL" +
            ", DISPLAY_ORDER" +
            ", IS_DISPLAY" +
            ", CREATED_DT" +
            ")"+
            " values(?,?,?,?,?,now())";

        this.jdbcTemplate.batchUpdate(saveImgSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Long advAttachId = uidGenerator.generate(BusinessTypeEnum.ADVICE_ATTACH);

                Advice.AdviceImg image = images.get(i);
                ps.setLong(1,advAttachId);
                ps.setLong(2,image.getAdviId());
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
    public int updateAdvice(Advice advice) {
        String upAdviceSql = "update str_advice set ADVI_REPLY_CONTENT=?,ADVI_REPLIER=?," +
            "ADVI_REPLY_TIME=now(),MODIFIED_DT=now(),MODIFIED_USER_ID=?,ADVI_STATUS=? where ADVI_ID=?";
        return this.jdbcTemplate.update(upAdviceSql,
            new Object[]{
                advice.getAdviReplyContent(),
                advice.getAdviReplier(),
                advice.getModifiedUserId(),
                advice.getAdviStatus(),
                advice.getAdviId()
            });
    }

    @Override
    public int bachUpAdviceStatus(BatchUpAdviceStatus upAdviceStatus) {
        String sql = "update str_advice set ADVI_STATUS=? where ADVI_ID=?";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                String id =  upAdviceStatus.getAdviIds().get(i);
                preparedStatement.setString(1, upAdviceStatus.getStatus());
                preparedStatement.setString(2,id);
            }

            @Override
            public int getBatchSize() {
                return upAdviceStatus.getAdviIds().size();
            }
        });

        return upAdviceStatus.getAdviIds().size();
    }

    @Override
    public int updateAdviceImages(BatchUpAdvImgStatus imgStatus) {

        String sql = "update str_advice_attach set IS_DISPLAY = ? where ADVI_ATTACH_ID=?";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                String id =  imgStatus.getAdviAttachId().get(i);
                preparedStatement.setString(1,Advice.AdviceImg.ADVICEIMG_HIDE);
                preparedStatement.setString(2,id);
            }

            @Override
            public int getBatchSize() {
                return imgStatus.getAdviAttachId().size();
            }
        });

        return imgStatus.getAdviAttachId().size();
    }
}
