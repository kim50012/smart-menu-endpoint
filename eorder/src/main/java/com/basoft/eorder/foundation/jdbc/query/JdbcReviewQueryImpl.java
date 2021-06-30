package com.basoft.eorder.foundation.jdbc.query;

import com.basoft.eorder.domain.model.Review;
import com.basoft.eorder.domain.model.StrReviewAttach;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.ReviewDTO;
import com.basoft.eorder.interfaces.query.ReviewQuery;
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
 * @Date Created in 上午10:14 2019/5/14
 **/
@Component("ReviewQuery")
public class JdbcReviewQueryImpl extends BaseRepository implements ReviewQuery{
    private final static String REVIEW_SELECT = "select " +
        " r.REV_ID, r.PLATFORM_ID, r.NICKNAME, r.CHATHEAD, r.REV_PLATFORM, r.ORDER_ID, r.CUST_ID, r.STORE_ID, \n" +
        " r.REV_CLASS, r.REV_TAG, r.ENV_CLASS, r.PROD_CLASS, r.SERVICE_CLASS, r.AVER_PRICE, r.IS_ANONYMITY, \n" +
        " r.REV_TIME, r.REV_STATUS, r.REV_REPLIER, r.REV_REPLY_TIME, r.MODIFIED_DT, r.MODIFIED_USER_ID, " +
        " r.REV_CONTENT,r.REV_CONTENT_COPIE, r.REV_REPLY_CONTENT,REV_REPLY_CONTENT_COPIE,s.name as storeName ";

    private final static String REVIEW_FROM =" from str_review r " +
        " left join store s on r.STORE_ID=s.id  where 1=1 ";

    @Override
    public ReviewDTO getReviewDto(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(REVIEW_SELECT+REVIEW_FROM);

        reviewCondition(sql, param,false);
        ReviewDTO dto =  this.queryForObject(sql.toString(),param,new BeanPropertyRowMapper<>(ReviewDTO.class));
        List<ReviewDTO> dtoList = new LinkedList<>();
        dtoList.add(dto);
        return getRevImage(dtoList).get(0);

    }

    @Override
    public Review getReview(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(REVIEW_SELECT+REVIEW_FROM);

        reviewCondition(sql, param,false);
        return  this.queryForObject(sql.toString(),param,new BeanPropertyRowMapper<>(Review.class));
    }

    private List<ReviewDTO> getRevImage(List<ReviewDTO> reviewList) {

        final List<Long> collect = reviewList.stream().map(ReviewDTO::getRevId).collect(Collectors.toList());
        final Map<Long, List<StrReviewAttach>> group = getRevtWithImage(collect.toArray(new Long[collect.size()]));
        if (group != null&&group.size()>0) {
            reviewList.forEach(r -> r.setImgList(group.get(r.getRevId())));
        }else{
            reviewList.forEach(r -> r.setImgList(new LinkedList<>()));
        }
        return reviewList;
    }


    private Map<Long,List<StrReviewAttach>> getRevtWithImage(Long ... ids) {

        Map<String, Object> param = Maps.newHashMap();
        param.put("revIds", Arrays.asList(ids));
        List<StrReviewAttach> pidList = getRevImageListByMap(param);
        return  pidList.stream().collect(Collectors.groupingBy(StrReviewAttach::getRevId));
    }


    public List<StrReviewAttach> getRevImageListByMap(Map<String, Object> param) {

        StringBuilder sql = new StringBuilder("select REV_ATTACH_ID, REV_ID, CONTENT_ID, CONTENT_URL, DISPLAY_ORDER, IS_DISPLAY, CREATED_DT, \n" +
            "    CREATED_USER_ID, MODIFIED_DT, MODIFIED_USER_ID from STR_REVIEW_ATTACH where 1 = 1 \n");
        String revIds = Objects.toString(param.get("revIds"), null);
        if (StringUtils.isNotBlank(revIds)) {
            sql.append(" and REV_ID in (:revIds)");
        }
        sql.append(" and IS_DISPLAY = 1");
        sql.append(" order by DISPLAY_ORDER asc");

        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(StrReviewAttach.class));
    }



    @Override
    public int getReviewCount(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1)");
        sql.append(REVIEW_FROM);
        reviewCondition(sql, param,false);
        return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public List<ReviewDTO> getReviewList(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(REVIEW_SELECT+REVIEW_FROM);
        reviewCondition(sql, param,true);

        List<ReviewDTO> revList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(ReviewDTO.class));
        if (revList != null && revList.size() > 0) {
            revList = getRevImage(revList);
        }
        return revList;
    }

    private StringBuilder reviewCondition(StringBuilder sql,Map<String, Object> param,Boolean isLimit){
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        long revId = NumberUtils.toLong(Objects.toString(param.get("revId"), null));
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        String storeType = Objects.toString(Objects.toString(param.get("storeType"), ""));
        String platformId = Objects.toString(param.get("platformId"), null);
        String orderId = Objects.toString(param.get("orderId"), null);
        String startTime = Objects.toString(param.get("startTime"), null);
        String endTime = Objects.toString(param.get("endTime"), null);
        String nickname = Objects.toString(param.get("nickname"), null);
        String storeName = Objects.toString(param.get("storeName"), null);

        if (storeId>0) {
            sql.append(" and r.STORE_ID = :storeId \n");
        }
        if (revId>0) {
            sql.append(" and r.REV_ID = :revId \n");
        }
        if (StringUtils.isNotBlank(platformId)) {
            sql.append(" and r.PLATFORM_ID = :platformId \n");
        }
        if (StringUtils.isNotBlank(storeType)) {
            sql.append(" and s.store_type = :storeType \n");
        }
        if (StringUtils.isNotBlank(nickname)) {
            sql.append(" and r.nickname = :nickname \n");
        }
        if (StringUtils.isNotBlank(storeName)) {
            sql.append(" and s.name like '%' :storeName '%'\n");
        }
        if (StringUtils.isNotBlank(orderId)) {
            sql.append(" and r.order_id = :orderId \n");
        }
            sql.append(" and r.rev_status != 4\n");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql.append(" and date_format(r.REV_TIME, '%Y-%m-%d') >= :startTime and date_format(r.REV_TIME, '%Y-%m-%d')<=:endTime ");
        } else if (StringUtils.isNotBlank(endTime)) {
            sql.append(" and date_format(r.REV_TIME, '%Y-%m-%d') <= :endTime");
        }
        sql.append(" order by r.rev_time desc ");
        appendPage(page, size,sql, param, isLimit);
        return sql;

    }

}


