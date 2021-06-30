package com.basoft.eorder.foundation.jdbc.query;

import com.basoft.eorder.domain.model.Advice;
import com.basoft.eorder.domain.model.Review;
import com.basoft.eorder.domain.model.StrAdviceAttach;
import com.basoft.eorder.domain.model.StrReviewAttach;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.AdviceDTO;
import com.basoft.eorder.interfaces.query.AdviceQuery;
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
 * @Date Created in 下午4:26 2019/5/20
 **/

@Component("AdviceQuery")
public class JdbcAdviceQueryImpl extends BaseRepository implements AdviceQuery{

    private static final String ADVICE_SELECT = "select  SA.ADVI_ID, SA.PLATFORM_ID, SA.NICKNAME, SA.CHATHEAD," +
        " SA.REV_PLATFORM, SA.ADVI_TOS, SA.ADVI_TYPE, SA.CUST_ID, SA.STORE_ID, SA.LINKER, " +
        " SA.LINK_PHONE, SA.LINK_EMAIL, SA.ADVI_STATUS,SA.ADVI_REPLIER, SA.ADVI_REPLY_TIME, " +
        " SA.MODIFIED_DT, SA.MODIFIED_USER_ID,SA.ADVI_TIME,SA.ADVI_CONTENT,SA.ADVI_REPLY_CONTENT,S.name as storeName ";


    private static final String ADVICE_FROM  = " FROM STR_ADVICE SA LEFT JOIN STORE S ON SA.STORE_ID=S.ID where 1=1 ";

    @Override
    public Advice getAdvice(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(ADVICE_SELECT + ADVICE_FROM);
        adviceCondtion(sql, param, false);
        return this.queryForObject(sql.toString(),param,new BeanPropertyRowMapper<>(Advice.class));

    }

    @Override
    public AdviceDTO getAdviceDto(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(ADVICE_SELECT + ADVICE_FROM);
        adviceCondtion(sql, param, false);

        AdviceDTO dto = this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(AdviceDTO.class));
        if (dto != null) {
            List<AdviceDTO> dtoList = new LinkedList<>();
            dtoList.add(dto);
            getAdvImage(dtoList);
        }

        return dto;
    }

    @Override
    public int getAdviceCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1)");
        sql.append(ADVICE_FROM);
        adviceCondtion(sql, param, false);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<AdviceDTO> getAdviceList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(ADVICE_SELECT + ADVICE_FROM);
        adviceCondtion(sql, param, true);
        List<AdviceDTO> adviceList =  this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(AdviceDTO.class));
        if (adviceList != null && adviceList.size() > 0) {
            getAdvImage(adviceList);
        }
        return adviceList;
    }





    private List<AdviceDTO> getAdvImage(List<AdviceDTO> adviceList) {

        final List<Long> collect = adviceList.stream().map(AdviceDTO::getAdviId).collect(Collectors.toList());
        final Map<Long, List<StrAdviceAttach>> group = getAdviceWithImage(collect.toArray(new Long[collect.size()]));
        if (group != null&&group.size()>0) {
            adviceList.forEach(r -> r.setImgList(group.get(r.getAdviId())));
        }else{
            adviceList.forEach(r -> r.setImgList(new LinkedList<>()));
        }
        return adviceList;
    }


    private Map<Long,List<StrAdviceAttach>> getAdviceWithImage(Long ... ids) {

        Map<String, Object> param = Maps.newHashMap();
        param.put("advIds", Arrays.asList(ids));
        List<StrAdviceAttach> imgList = getAdvImageListByMap(param);
        return  imgList.stream().collect(Collectors.groupingBy(StrAdviceAttach::getAdviId));
    }


    public List<StrAdviceAttach> getAdvImageListByMap(Map<String, Object> param) {

        StringBuilder sql = new StringBuilder("select ADVI_ATTACH_ID as adviAttachId, ADVI_ID as adviId, CONTENT_ID as contentId," +
            " CONTENT_URL as contentUrl, DISPLAY_ORDER as displayOrder, IS_DISPLAY as isDisplay, CREATED_DT, \n" +
            " CREATED_USER_ID, MODIFIED_DT, MODIFIED_USER_ID from STR_ADVICE_ATTACH where 1 = 1 \n");
        String advIds = Objects.toString(param.get("advIds"), null);
        if (StringUtils.isNotBlank(advIds)) {
            sql.append(" and ADVI_ID in (:advIds)");
        }
        sql.append(" and IS_DISPLAY = 1");
        sql.append(" order by DISPLAY_ORDER asc");

        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(StrAdviceAttach.class));
    }








    private StringBuilder adviceCondtion(StringBuilder sql, Map<String, Object> param, boolean isLimit) {
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        long adviId = NumberUtils.toLong(Objects.toString(param.get("adviId"), null));
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        String platformId = Objects.toString(param.get("platformId"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String nickname = Objects.toString(param.get("nickname"), null);
        String storeName = Objects.toString(param.get("storeName"), null);
        String adviTos = Objects.toString(param.get("adviTos"), null);
        String adviType = Objects.toString(param.get("adviType"), null);


        if (storeId>0) {
            sql.append(" and SA.STORE_ID = :storeId \n");
        }
        if (adviId>0) {
            sql.append(" and SA.ADVI_ID = :adviId \n");
        }
        if (StringUtils.isNotBlank(platformId)) {
            sql.append(" and SA.PLATFORM_ID = :platformId \n");
        }
        if (StringUtils.isNotBlank(nickname)) {
            sql.append(" and SA.nickname = :nickname \n");
        }
        if (StringUtils.isNotBlank(storeName)) {
            sql.append(" and s.name = :storeName \n");
        }
        if (StringUtils.isNotBlank(adviTos)) {
            sql.append(" and SA.ADVI_TOS = :adviTos \n");
        }
        if (StringUtils.isNotBlank(adviType)) {
            sql.append(" and SA.ADVI_TYPE = :adviType \n");
        }

        sql.append(" and SA.ADVI_STATUS != 3\n");
        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            sql.append(" and SA.ADVI_TIME >= :startTime and SA.ADVI_TIME<=:endTime ");
        }
        sql.append(" order by SA.ADVI_TIME desc ");
        appendPage(page, size,sql, param, isLimit);
        return sql;

    }
}
