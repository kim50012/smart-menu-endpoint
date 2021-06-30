package com.basoft.eorder.foundation.jdbc.query.post;

import com.basoft.eorder.domain.model.post.CustPostAddress;
import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.post.CustPostAddressQuery;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JdbcCustPostAddressQueryImpl extends BaseRepository implements CustPostAddressQuery {
    /**
     * 获取配送地址列表
     *
     * @param wxOpenId
     * @param queryType a-查询所有状态的配送地址 p-查询可用的配送地址 d-查询默认的配送地址
     * @return
     */
    @Override
    public List<CustPostAddress> getPostAddressList(String wxOpenId, String queryType) {
        StringBuilder sql = new StringBuilder(1024);
        sql.append("SELECT")
                .append(" ADDR_ID,CUST_ID,WX_OPEN_ID,ADDR_NAME,COUNTRY_CHN,COUNTRY_KOR,COUNTRY_ENG,COUNTRY_CODE,CONSIGNEE")
                .append(" ,MOBILE_COUNTRY_CODE,MOBILE_NO,ALTERNATE_MOBILE_COUNTRY_CODE,ALTERNATE_MOBILE_NO,REGION_NAME_CHN,REGION_NAME_KOR,REGION_NAME_ENG")
                .append(" ,REGION_ONE_NAME_CHN,REGION_ONE_NAME_KOR,REGION_ONE_NAME_ENG,REGION_ONE_CODE")
                .append(" ,REGION_TWO_NAME_CHN,REGION_TWO_NAME_KOR,REGION_TWO_NAME_ENG,REGION_TWO_CODE")
                .append(" ,REGION_THREE_NAME_CHN,REGION_THREE_NAME_KOR,REGION_THREE_NAME_ENG,REGION_THREE_CODE")
                .append(" ,REGION_FOUR_NAME_CHN,REGION_FOUR_NAME_KOR,REGION_FOUR_NAME_ENG,REGION_FOUR_CODE")
                .append(" ,REGION_FIVE_NAME_CHN,REGION_FIVE_NAME_KOR,REGION_FIVE_NAME_ENG,REGION_FIVE_CODE")
                .append(" ,ADDR_DETAIL_CHN,ADDR_DETAIL_KOR,ADDR_DETAIL_ENG")
                .append(" ,ADDR_POSTCODE,EMAIL,TAG,IS_DEFAULT,STATUS,STATUS_TIME,CREATE_TIME,CREATE_USER,UPDATE_TIME")
                .append(" FROM POST_WX_CUST_ADDRESS WHERE")
                .append(" WX_OPEN_ID = :wxOpenId");
        if ("a".equals(queryType)) {
            // nothing to do
        } else if ("p".equals(queryType)) {
            sql.append(" AND STATUS = 1");
        } else if ("d".equals(queryType)) {
            sql.append(" AND STATUS = 1 AND IS_DEFAULT = 1");
        }
        sql.append(" ORDER BY IS_DEFAULT DESC");

        Map<String, String> param = Maps.newHashMap();
        param.put("wxOpenId", wxOpenId);
        return this.getNamedParameterJdbcTemplate().query(sql.toString(), param, new BeanPropertyRowMapper<>(CustPostAddress.class));
    }
}


