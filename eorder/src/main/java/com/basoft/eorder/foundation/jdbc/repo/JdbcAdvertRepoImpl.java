package com.basoft.eorder.foundation.jdbc.repo;

import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.domain.AdvertRepository;
import com.basoft.eorder.domain.model.Advertisement;
import com.basoft.eorder.domain.model.Advice;
import com.basoft.eorder.domain.model.Review;
import com.basoft.eorder.interfaces.command.advert.BatchUpStatusAdvert;
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
 * @Description: 广告
 * @Date Created in 下午2:36 2019/6/1
 **/
@Repository
@Transactional
public class JdbcAdvertRepoImpl extends BaseRepository implements AdvertRepository{

    @Autowired
    private UidGenerator uidGenerator;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public int saveAdvert(Advertisement ad) {
        String saveSql = " insert into adv_advertisement("+
            "  ADV_ID" +
            ", ADV_NAME" +
            ", ADV_CHANNEL" +
            ", ADV_LOCATION " +
            ", ADV_TYPE" +
            ", ADV_CONTENT" +
            ", ADV_IMAGE_URL" +
            ", ADV_URL" +
            ", START_TIME" +
            ", END_TIME" +
            ", ADV_SECONDS" +
            ", ADV_HEIGHT" +
            ", ADV_WIDTH" +
            ", ADV_TOP" +
            ", ADV_LEFT" +
            ", ADV_DESC" +
            ", ADV_STATUS" +
            ", USE_YN" +
            ", CREATED_DT" +
            ")" +
            " values "+
            " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
        int result =  this.jdbcTemplate.update(saveSql,new Object[]{
             ad.getAdvId()
            ,ad.getAdvName()
            ,ad.getAdvChannel()
            ,ad.getAdvLocation()
            ,ad.getAdvType()
            ,ad.getAdvContent()
            ,ad.getAdvImageUrl()
            ,ad.getAdvUrl()
            ,ad.getStartTime()
            ,ad.getEndTime()
            ,ad.getAdvSeconds()
            ,ad.getAdvHeight()
            ,ad.getAdvWidth()
            ,ad.getAdvTop()
            ,ad.getAdvLeft()
            ,ad.getAdvDesc()
            ,Advertisement.START
            ,Advertisement.START
        });


        updateAdvertImages(ad);

        return result;
    }

    private int delAdvertDetail(Long advId) {
       return this.jdbcTemplate.update("delete from adv_advertisement_detail where ADV_ID = ?",advId);
    }


    public int updateAdvertImages(Advertisement advert) {

        List<Advertisement.AdvertDetail> detailList = advert.getDetailsList();
        detailList.stream().map(i ->
        {i.setAdvId(advert.getAdvId()); return i; } ).collect(Collectors.toList());

        String saveImgSql = "insert into adv_advertisement_detail (" +
            "  ADV_DET_ID" +
            ", ADV_ID" +
            ", CONTENT_NAME" +
            ", CONTENT_URL" +
            ", TARGET_URL" +
            ", ADV_DET_ORDER" +
            ", USE_YN" +
            ", CREATED_DT" +
            ")"+
            " values(?,?,?,?,?,?,1,now())";

        this.jdbcTemplate.batchUpdate(saveImgSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Long advDetId = uidGenerator.generate(BusinessTypeEnum.ADVERT);

                Advertisement.AdvertDetail detail = detailList.get(i);
                ps.setLong(1,advDetId);
                ps.setLong(2,detail.getAdvId());
                ps.setString(3,detail.getContentName());
                ps.setString(4,detail.getContentUrl());
                ps.setString(5,detail.getTargetUrl());
                ps.setInt(6,detail.getAdvDetOrder());
            }
            @Override
            public int getBatchSize() {
                return detailList.size();
            }
        });

        return detailList.size();
    }



    @Override
    public Long updateAdvert(Advertisement advert) {
        String upSql = "update adv_advertisement set ADV_NAME=?, ADV_CHANNEL=?," +
            "ADV_LOCATION=?,ADV_TYPE=?,ADV_CONTENT=?,ADV_IMAGE_URL=?,ADV_URL=?," +
            "ADV_SECONDS=?, ADV_HEIGHT=?, ADV_WIDTH=?, ADV_TOP=?,ADV_LEFT=?, ADV_DESC=?," +
            "ADV_STATUS=?,USE_YN=? where ADV_ID=? ";
        this.jdbcTemplate.update(upSql,new Object[]{
            advert.getAdvName(),
            advert.getAdvChannel(),
            advert.getAdvLocation(),
            advert.getAdvType(),
            advert.getAdvContent(),
            advert.getAdvImageUrl(),
            advert.getAdvUrl(),
            advert.getAdvSeconds(),
            advert.getAdvHeight(),
            advert.getAdvWidth(),
            advert.getAdvTop(),
            advert.getAdvLeft(),
            advert.getAdvDesc(),
            advert.getAdvStatus(),
            advert.getUseYn(),
            advert.getAdvId()
        });

        int dtl =  delAdvertDetail(advert.getAdvId());
        if(dtl>=0)
            updateAdvertImages(advert);

        return advert.getAdvId();
    }


    @Override
    public int upStatusAdvert(BatchUpStatusAdvert upStatusAdvert) {
        String sql = "update adv_advertisement set ADV_STATUS = ?,USE_YN = ?,MODIFIED_DT = now() where ADV_ID=?";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                String id = upStatusAdvert.getAdvIds().get(i);
                preparedStatement.setString(1, upStatusAdvert.getAdvStatus());
                preparedStatement.setString(2,upStatusAdvert.getUseYn());
                preparedStatement.setString(3,id);
            }

            @Override
            public int getBatchSize() {
                return upStatusAdvert.getAdvIds().size();
            }
        });

        return upStatusAdvert.getAdvIds().size();
    }
}
