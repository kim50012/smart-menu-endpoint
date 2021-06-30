package com.basoft.eorder.foundation.jdbc.query;

import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.BannerDTO;
import com.basoft.eorder.interfaces.query.BannerQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/**
 * @ProjectName: intellij_work
 * @Package: com.basoft.eorder.foundation.jdbc.query
 * @ClassName: JdbcBannerQueryImpl
 * @Description: TODO
 * @Author: liminzhe
 * @Date: 2018-12-13 14:18
 * @Version: 1.0
 */
@Component("Banner")
public class JdbcBannerQueryImpl extends BaseRepository implements BannerQuery {

    private static final String BANNER_BY_ID = "select b.id as id, b.name as name, b.image_path as imagePath, b.created as created, b.store_id as storeId, b.show_index as showIndex from banners b where id = :id";

//    private static final String BANNER_COUNT_SELECT = "select count(*) \n";

    private static final String BANNER_LIST_SELECT = "select b.id as id, b.name as name, b.image_path as imagePath," +
                        "b.status, b.created as created, b.store_id as storeId, b.show_index as showIndex \n";


    private static final String BANNER_FROM = "from banners b \n where 1 = 1 \n";

//    private static final String LIMIT = "limit :page, :size";

    @Override
    public BannerDTO getBannerById(Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        return this.queryForObject(BANNER_BY_ID, map, new BeanPropertyRowMapper<BannerDTO>(BannerDTO.class));
    }

    @Override
    public List<BannerDTO> getBannerListByMap(Map<String, Object> param) {

        String name = Objects.toString(param.get("name"), null);
        String status = Objects.toString(param.get("status"), null);
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
       // String showIndex = Objects.toString(Objects.toString(param.get("showIndex"), null));
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));

        StringBuilder query = new StringBuilder(BANNER_LIST_SELECT + BANNER_FROM);

        if (StringUtils.isNotBlank(name)) {
            query.append(" and b.name = :name \n");
        }


        if (StringUtils.isNotBlank(status)) {
            query.append(" and b.status = :status \n");
        }

        if (storeId > 0) {
            query.append(" and b.store_id = :storeId \n");
        }
//                .append(this.getQueryConditions(param))
        query.append("order by if(b.show_index < 1, 9999 , show_index) \n");


        if (page >= 0 && size > 0) {
            int resPage = page;
            if(page > 0){
                resPage = (resPage-1)*10;
                param.put("page",resPage);
            }
            query.append(LIMIT);
        }

        return this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<BannerDTO>(BannerDTO.class));
    }

    @Override
    public int getBannerCount(Map<String, Object> param) {

        String name = Objects.toString(param.get("name"), null);
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));

        StringBuilder query = new StringBuilder(SELECT_COUNT + BANNER_FROM);

//                .append(this.getQueryConditions(map))
        if (StringUtils.isNotBlank(name)) {
            query.append(" and b.name = :name \n");
        }

        if (storeId > 0) {
            query.append(" and b.store_id = :storeId \n");
        }



        Integer count = this.getNamedParameterJdbcTemplate().queryForObject(query.toString(), param, Integer.class);
        return count.intValue();
    }
}
