package com.basoft.eorder.foundation.jdbc.query.postStoreSet;

import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.postStoreSet.PostStoreSetDTO;
import com.basoft.eorder.interfaces.query.postStoreSet.PostStoreSetDetailDTO;
import com.basoft.eorder.interfaces.query.postStoreSet.PostStoreSetQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商户配送设置表查询
 *
 * @author DongXifu
 * @since 2020-04-29 10:49:09
 */
@Component("PostStoreSetQuery")
public class JdbcPostStoreSetQueryImpl extends BaseRepository implements PostStoreSetQuery {

     private final static String POST_STORE_SET_SELECT = "SELECT PSS_ID,STORE_ID,SET_NAME_CHN,SET_NAME_KOR,SET_NAME_ENG,TARGET_COUNTRY_NAME,TARGET_COUNTRY_CODE,IS_FREE,FREE_AMOUNT,POST_COM_NAME,POST_COM_CODE,SET_RULE,STATUS\n" +
             ",STATUS_TIME,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER\n";
     private final static String POST_STORE_SET_FROM = " FROM post_store_set R  WHERE 1=1\n";

    private final static String POST_STORE_SET_DETAIL_SELECT = "SELECT PSS_ID,DETAIL_NO,LOWER_LIMIT,UPPER_LIMIT,CHARGE_FEE,CREATE_TIME,CREATE_USER,UPDATE_TIME,UPDATE_USER\n";
    private final static String POST_STORE_SET_DETAIL_FROM = " FROM post_store_set_detail R  WHERE 1=1\n";

     @Override
     public int getPostStoreSetCount(Map<String, Object> param) {
         StringBuilder sql = new StringBuilder("select count(1)" + POST_STORE_SET_FROM);
         getPostStoreSetCondition(sql, param);
         return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
     }

     @Override
     public List<PostStoreSetDTO> getPostStoreSetListByMap(Map<String, Object> param) {
         StringBuilder sql = new StringBuilder(POST_STORE_SET_SELECT + POST_STORE_SET_FROM);
         getPostStoreSetCondition(sql, param);
         orderByAndPage(param, sql, " ORDER BY CREATE_TIME desc");

         List<PostStoreSetDTO> postStoreSetDtoList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(PostStoreSetDTO.class));

         if (postStoreSetDtoList != null && postStoreSetDtoList.size() > 0) {
             List<Long> postIds = postStoreSetDtoList.stream().map(p -> p.getPssId()).collect(Collectors.toList());
             List<PostStoreSetDetailDTO> detailList = getPostStoreSetDetailListByMap(postIds.toArray(new Long[postIds.size()]));
             Map<Long, List<PostStoreSetDetailDTO>> collect = detailList.stream().collect(Collectors.groupingBy(PostStoreSetDetailDTO::getPssId));
             postStoreSetDtoList.forEach(p->{
                 p.setDetailList(collect.get(p.getPssId()));
             });

         }

         return postStoreSetDtoList;
     }


    @Override
     public PostStoreSetDTO getPostStoreSetDto(Map<String, Object> param) {
         StringBuilder sql = new StringBuilder();
         sql.append(POST_STORE_SET_SELECT + POST_STORE_SET_FROM);
         getPostStoreSetCondition(sql, param);
         PostStoreSetDTO dto = this.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(PostStoreSetDTO.class));
         List<PostStoreSetDetailDTO> detailList = getPostStoreSetDetailListByMap(param);
         dto.setDetailList(detailList);

         return dto;
     }

    /**
     * 根据pssid列表 获取设置详情列表
     *
     * @param pssIds
     * @return
     */
    private List<PostStoreSetDetailDTO> getPostStoreSetDetailListByMap(Long ...pssIds) {
        Map<String, Object> param = new HashMap<>();
        param.put("pssIds", Arrays.asList(pssIds));
        StringBuilder sql = new StringBuilder(POST_STORE_SET_DETAIL_SELECT + POST_STORE_SET_DETAIL_FROM);
        sql.append("and pss_id in(:pssIds)");
        List<PostStoreSetDetailDTO> postStoreSetDetailDtoList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(PostStoreSetDetailDTO.class));
        return postStoreSetDetailDtoList;
    }

    /**
     * 地址设置详情
     *
     * @param param
     * @return
     */
    public List<PostStoreSetDetailDTO> getPostStoreSetDetailListByMap(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder(POST_STORE_SET_DETAIL_SELECT + POST_STORE_SET_DETAIL_FROM);
        sql.append(" and PSS_ID = :pssId\n");
        sql.append(" order by DETAIL_NO asc");
        List<PostStoreSetDetailDTO> postStoreSetDetailDtoList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(PostStoreSetDetailDTO.class));
        return postStoreSetDetailDtoList;
    }

     private StringBuilder getPostStoreSetCondition(StringBuilder sql,Map<String, Object> param){

         String storeId = Objects.toString(param.get("storeId"), null);
         if (StringUtils.isNotBlank(storeId)) {
             sql.append(" and STORE_ID  = :storeId");
         }

         String pssId = Objects.toString(param.get("pssId"), null);
         if (StringUtils.isNotBlank(pssId)) {
             sql.append(" and PSS_ID  = :pssId");
         }

         String setNameChn = Objects.toString(param.get("setNameChn"), null);
         if (StringUtils.isNotBlank(setNameChn)) {
             sql.append(" and (SET_NAME_CHN like '%' :name '%' or SET_NAME_KOR like '%' :name '%')    ");
         }

         String targetCountryName = Objects.toString(param.get("targetCountryName"), null);
         if (StringUtils.isNotBlank(targetCountryName)) {
             sql.append(" and TARGET_COUNTRY_NAME  = :targetCountryName");
         }
                  
         String targetCountryCode = Objects.toString(param.get("targetCountryCode"), null);
         if (StringUtils.isNotBlank(targetCountryCode)) {
             sql.append(" and TARGET_COUNTRY_CODE  = :targetCountryCode");
         }
                  
         String isFree = Objects.toString(param.get("isFree"), null);
         if (StringUtils.isNotBlank(isFree)) {
             sql.append(" and IS_FREE  = :isFree");
         }
                  
         String freeAmount = Objects.toString(param.get("freeAmount"), null);
         if (StringUtils.isNotBlank(freeAmount)) {
             sql.append(" and FREE_AMOUNT  = :freeAmount");
         }
                  
         String postComName = Objects.toString(param.get("postComName"), null);
         if (StringUtils.isNotBlank(postComName)) {
             sql.append(" and POST_COM_NAME  = :postComName");
         }
                  
         String postComCode = Objects.toString(param.get("postComCode"), null);
         if (StringUtils.isNotBlank(postComCode)) {
             sql.append(" and POST_COM_CODE  = :postComCode");
         }
                  
         String setRule = Objects.toString(param.get("setRule"), null);
         if (StringUtils.isNotBlank(setRule)) {
             sql.append(" and SET_RULE  = :setRule");
         }
                  
         String statusTime = Objects.toString(param.get("statusTime"), null);
         if (StringUtils.isNotBlank(statusTime)) {
             sql.append(" and STATUS_TIME  = :statusTime");
         }

         String status = Objects.toString(param.get("status"), null);
         if (StringUtils.isNotBlank(status)) {
             sql.append(" and status  = :status");
         }
         sql.append(" and status != 2");

         return sql;
     }
}