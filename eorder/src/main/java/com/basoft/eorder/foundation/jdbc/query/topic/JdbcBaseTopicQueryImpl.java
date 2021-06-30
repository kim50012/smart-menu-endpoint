package com.basoft.eorder.foundation.jdbc.query.topic;

import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.model.topic.BaseTopic;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.topic.BaseTopicDTO;
import com.basoft.eorder.interfaces.query.topic.BaseTopicQuery;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component("BaseTopicQuery")
public class JdbcBaseTopicQueryImpl extends BaseRepository implements BaseTopicQuery {

     private final static String BaseTopic_SELECT = "select t.TP_ID" +
        ",t.TP_BIZ_TYPE "+
        ",t.TP_FUNC_TYPE "+
        ",t.TP_NAME "+
        ",t.TP_NAME_FOREI "+
        ",t.TP_LOGO_SID "+
        ",t.TP_LOGO_URL "+
        ",t.TP_DIS_TYPE "+
        ",t.TP_URL_TYPE "+
        ",t.TP_URL "+
        ",t.TP_ORDER"+
        ",t.TP_STATUS "+
        ",t.CREATE_TIME "+
        ",t.CREATE_USER "+
        ",t.UPDATE_TIME "+
        ",t.UPDATE_USER" ;
     private final static String BaseTopic_FROM =" from base_topic t  where 1=1 ";

     @Override
     public int getBaseTopicCount(Map<String, Object> param){
          StringBuilder sql = new StringBuilder("select count(1)"+BaseTopic_FROM);
          getbaseTopicCondition(sql,param);
          return this.getNamedParameterJdbcTemplate().queryForObject(sql.toString(), param, Integer.class);
     }

     @Override
     public List<BaseTopicDTO> getBaseTopicListByMap(Map<String, Object> param) {
         StringBuilder sql = new StringBuilder(BaseTopic_SELECT + BaseTopic_FROM);
         getbaseTopicCondition(sql, param);
         orderByAndPage(param, sql, " order by t.TP_FUNC_TYPE asc, t.TP_ORDER asc");
         List<BaseTopicDTO> baseTopicDtoList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(BaseTopicDTO.class));
         return baseTopicDtoList;
     }

     @Override
     public BaseTopicDTO getBaseTopicDto(Map<String, Object> param) {
          StringBuilder sql = new StringBuilder();
          sql.append(BaseTopic_SELECT+BaseTopic_FROM);
          getbaseTopicCondition(sql, param);
          BaseTopicDTO dto =  this.queryForObject(sql.toString(),param,new BeanPropertyRowMapper<>(BaseTopicDTO.class));
          return dto;
     }

     @Override
     public BaseTopic getBaseTopic(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder();
        sql.append(BaseTopic_SELECT+BaseTopic_FROM);
        getbaseTopicCondition(sql, param);
        return  this.queryForObject(sql.toString(),param,new BeanPropertyRowMapper<>(BaseTopic.class));
     }

    private StringBuilder  getbaseTopicCondition(StringBuilder sql, Map<String, Object> param){
        String tpId = Objects.toString(param.get("tpId"), null);
        String tpBizType = Objects.toString(param.get("tpBizType"), null);
        String tpFuncType = Objects.toString(param.get("tpFuncType"), null);
        String tpDisType = Objects.toString(param.get("tpDiType"), null);
        String tpName = Objects.toString(param.get("tpName"), null);
        String tpNameForei = Objects.toString(param.get("tpNameForei"), null);
        String tpUrlType = Objects.toString(param.get("tpUrlType"), null);
        String tpStatus = Objects.toString(param.get("tpStatus"), null);

        if(StringUtils.isNotBlank(tpId)){
            sql.append(" and TP_ID = :tpId");
        }
        if(StringUtils.isNotBlank(tpBizType)){
            sql.append(" and TP_BIZ_TYPE = :tpBizType");
        }
        if(StringUtils.isNotBlank(tpFuncType)){
            sql.append(" and TP_FUNC_TYPE = :tpFuncType");
        }
        if(StringUtils.isNotBlank(tpDisType)){
            sql.append(" and TP_DIS_TYPE = :tpDisType");
        }
        if(StringUtils.isNotBlank(tpName)){
            sql.append(" and TP_NAME = :tpName");
        }
        if(StringUtils.isNotBlank(tpNameForei)){
            sql.append(" and TP_NAME_FOREI = :tpNameForei");
        }
        if(StringUtils.isNotBlank(tpUrlType)){
            sql.append(" and TP_URL_TYPE = :tpUrlType");
        }
        if(StringUtils.isNotBlank(tpUrlType)){
            sql.append(" and  TP_FUNC_TYPE  = :tpFuncType");
        }
        if(StringUtils.isNotBlank(tpStatus)){
            sql.append(" and TP_STATUS = :tpStatus");
        }
        sql.append(" and TP_STATUS !=3");

        return sql;
     }

    /**
     * 获取餐厅的全部主题列表
     *
     * @return
     */
    public List<BaseTopicDTO> getRestaurantBaseTopicList() {
        StringBuilder sql = new StringBuilder(BaseTopic_SELECT + BaseTopic_FROM);
        Map<String, Object> param = Maps.newHashMap();
        param.put("tpBizType", CommonConstants.BIZ_ORDERING_STRING);
        param.put("tpStatus", "1");
        getbaseTopicCondition(sql, param);
        orderByAndPage(param, sql, " order by t.TP_FUNC_TYPE asc, t.TP_ORDER asc");
        List<BaseTopicDTO> baseTopicDtoList = this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(BaseTopicDTO.class));
        return baseTopicDtoList;
    }
}

