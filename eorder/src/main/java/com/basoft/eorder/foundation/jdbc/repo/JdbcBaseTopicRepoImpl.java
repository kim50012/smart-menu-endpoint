package com.basoft.eorder.foundation.jdbc.repo;

import com.basoft.eorder.domain.BaseTopicRepository;
import com.basoft.eorder.domain.model.topic.BaseTopic;
import com.basoft.eorder.interfaces.command.topic.BatchUpTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
@Transactional
public class JdbcBaseTopicRepoImpl extends BaseRepository implements BaseTopicRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public Long insertBaseTopic(BaseTopic baseTopic) {
        String saveBaseTopicSql = "insert  into base_topic" +
            "(TP_ID,TP_BIZ_TYPE,TP_FUNC_TYPE,TP_NAME,TP_NAME_FOREI,TP_LOGO_SID,TP_LOGO_URL,TP_URL_TYPE,TP_URL,TP_DIS_TYPE,TP_ORDER,TP_STATUS,CREATE_TIME,CREATE_USER)" +
            "values" +
            "(?,?,?,?,?,?,?,?,?,?,?,1,now(),'ba')";

        int result = this.jdbcTemplate.update(saveBaseTopicSql,
            new Object[]{
                baseTopic.getTpId(),
                baseTopic.getTpBizType(),
                baseTopic.getTpFuncType(),
                baseTopic.getTpName(),
                baseTopic.getTpNameForei(),
                baseTopic.getTpLogoSid(),
                baseTopic.getTpLogoUrl(),
                baseTopic.getTpUrlType(),
                baseTopic.getTpUrl(),
                baseTopic.getTpDisType(),
                baseTopic.getTpOrder()
            });

        return baseTopic.getTpId();
    }

    @Override
    public Long updateBaseTopic(BaseTopic baseTopic) {
        String updateBaseTopicSql = "update base_topic set " +
            "TP_ID=?,TP_BIZ_TYPE=?,TP_FUNC_TYPE=?,TP_NAME=?,TP_NAME_FOREI=?,TP_LOGO_SID=?,TP_LOGO_URL=?,TP_DIS_TYPE=?,TP_ORDER=?,TP_URL_TYPE=?,TP_URL=?,UPDATE_TIME=now(),UPDATE_USER='ba' " +
            "where TP_ID=?";

        this.jdbcTemplate.update(updateBaseTopicSql,
            new Object[]{
                baseTopic.getTpId(),
                baseTopic.getTpBizType(),
                baseTopic.getTpFuncType(),
                baseTopic.getTpName(),
                baseTopic.getTpNameForei(),
                baseTopic.getTpLogoSid(),
                baseTopic.getTpLogoUrl(),
                baseTopic.getTpDisType(),
                baseTopic.getTpOrder(),
                baseTopic.getTpUrlType(),
                baseTopic.getTpUrl(),
                baseTopic.getTpId()
            });

        return baseTopic.getTpId();

    }

    @Override
    public int updateBaseTopicStatus(BatchUpTopic batchUpTopic) {
        String upStaSql = "update base_topic set TP_STATUS=?\n" +
            "where TP_ID=?";
        this.getJdbcTemplate().batchUpdate(upStaSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                String id = batchUpTopic.getTpIds().get(i);
                preparedStatement.setInt(1, batchUpTopic.getTpStatus());
                preparedStatement.setLong(2,Long.valueOf(id));
            }

            @Override
            public int getBatchSize() {
                return batchUpTopic.getTpIds().size();
            }
        });

        return batchUpTopic.getTpIds().size();
    }
}



