package com.basoft.eorder.foundation.jdbc.query;

import com.basoft.eorder.domain.model.AdvAdvertisementDetail;
import com.basoft.eorder.domain.model.Advertisement;
import com.basoft.eorder.domain.model.StrReviewAttach;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.AdvertDTO;
import com.basoft.eorder.interfaces.query.AdvertQuery;
import com.basoft.eorder.interfaces.query.ReviewDTO;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:47 2019/6/1
 **/
@Component("AdvertQuery")
public class JdbcAdvertQueryImpl extends BaseRepository implements AdvertQuery{
   public static final String ADVERT_COLUMN = " ADV_ID, ADV_NAME, ADV_CHANNEL, ADV_LOCATION, ADV_TYPE, ADV_CONTENT, ADV_IMAGE_ID, \n" +
        "    ADV_IMAGE_URL, ADV_VIDEO_ID, ADV_VIDEO_URL, ADV_URL, START_TIME, END_TIME, ADV_SECONDS, \n" +
        "    ADV_DISPLAY_DAYS, ADV_HEIGHT, ADV_WIDTH, ADV_TOP, ADV_LEFT, ADV_DESC, ADV_STATUS, \n" +
        "    USE_YN, CREATED_DT, CREATED_USER_ID, MODIFIED_DT, MODIFIED_USER_ID ";

    @Override
    public Advertisement getAdvert(Long advertId) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", advertId);
        StringBuilder query = new StringBuilder();
        query.append("select "+ADVERT_COLUMN+" from adv_advertisement where ADV_ID = :id");

        return this.queryForObject(query.toString(),param,new BeanPropertyRowMapper<>(Advertisement.class));

    }

    @Override
    public Advertisement getAdvert(Map<String, Object> param) {
        StringBuilder query = new StringBuilder();
        query.append("select "+ADVERT_COLUMN+" from adv_advertisement where 1=1 ");
        advertCondition(query,param,false);
        return this.queryForObject(query.toString(),param,new BeanPropertyRowMapper<>(Advertisement.class));

    }

    @Override
    public AdvertDTO getAdvertDto(Map<String, Object> param) {
        StringBuilder query = new StringBuilder();
        query.append("select "+ADVERT_COLUMN+" from adv_advertisement where 1=1 ");

        advertCondition(query,param,false);
        return this.queryForObject(query.toString(),param,new BeanPropertyRowMapper<>(AdvertDTO.class));
    }

    @Override
    public int getAdvertCount(Map<String, Object> param) {
        StringBuilder query = new StringBuilder();

        query.append("select count(1) from adv_advertisement where 1=1 ");
        advertCondition(query, param, false);
        return this.getNamedParameterJdbcTemplate().queryForObject(query.toString(), param, Integer.class);
    }

    @Override
    public List<AdvertDTO> getAdvertList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("select "+ADVERT_COLUMN+" from adv_advertisement where 1=1 ");
        advertCondition(sql,param,true);
        List<AdvertDTO> dtoList =  this.getNamedParameterJdbcTemplate().query(sql.toString(),param, new BeanPropertyRowMapper<>(AdvertDTO.class));
        if (dtoList != null && dtoList.size() > 0) {
            getAdvDetail(dtoList);
        }
        return dtoList;
    }

    /**
     * 向广告里插入图片或内容
     *
     * @Param advertList
     * @return java.util.List<com.basoft.eorder.interfaces.query.AdvertDTO>
     * @author Dong Xifu
     * @date 2019/6/11 下午3:35
     */
    private List<AdvertDTO> getAdvDetail(List<AdvertDTO> advertList) {
        final List<Long> collect = advertList.stream().map(AdvertDTO::getAdvId).collect(Collectors.toList());
        final Map<Long, List<AdvAdvertisementDetail>> group = getAdverttWithDetail(collect.toArray(new Long[collect.size()]));
        if (group != null&&group.size()>0) {
            advertList.forEach(r -> r.setDetailList(group.get(r.getAdvId())));
        }else{
            advertList.forEach(r -> r.setDetailList(new LinkedList<>()));
        }
        return advertList;
    }

    private Map<Long,List<AdvAdvertisementDetail>> getAdverttWithDetail(Long ... ids) {

        Map<String, Object> param = Maps.newHashMap();
        param.put("advIds", Arrays.asList(ids));
        List<AdvAdvertisementDetail> dtlList = getadvDtlListByMap(param);
        return  dtlList.stream().collect(Collectors.groupingBy(AdvAdvertisementDetail::getAdvId));
    }

    public List<AdvAdvertisementDetail> getadvDtlListByMap(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder("select ADV_DET_ID as advDetId, ADV_ID as advId, CONTENT_NAME, CONTENT_ID, CONTENT_URL, TARGET_URL, START_TIME, \n" +
            "    END_TIME, ADV_DET_ORDER, USE_YN ,CREATED_DT from adv_advertisement_detail where 1 = 1 \n");
        String advIds = Objects.toString(param.get("advIds"), null);
        if (StringUtils.isNotBlank(advIds)) {
            sql.append(" and ADV_ID in (:advIds)");
        }
        sql.append(" and USE_YN = 1");
        sql.append(" order by ADV_DET_ORDER asc");

        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(AdvAdvertisementDetail.class));
    }

    private StringBuilder advertCondition(StringBuilder sql,Map<String, Object> param,Boolean isLimit) {
        String advId = Objects.toString(param.get("advId"), null);
        String advName = Objects.toString(param.get("advName"), null);
        String advType = Objects.toString(param.get("advType"), null);
        String advStatus = Objects.toString(param.get("advStatus"), null);
        String useYn = Objects.toString(param.get("useYn"), null);
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));

        if (StringUtils.isNotBlank(advId)) {
            sql.append(" and adv_id = :advId");
        }
        if (StringUtils.isNotBlank(advName)) {
            sql.append(" and adv_name = :advName");
        }
        if (StringUtils.isNotBlank(advType)) {
            sql.append(" and adv_type = :advType");
        }
        if (StringUtils.isNotBlank(advStatus)) {
            sql.append(" and ADV_STATUS = :advStatus");
        }
            sql.append(" and USE_YN != 0");
        sql.append(" order by CREATED_DT desc ");
        appendPage(page, size,sql, param, isLimit);
        return  sql;
    }
}
