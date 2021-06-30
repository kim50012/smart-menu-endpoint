package com.basoft.eorder.foundation.jdbc.query;

import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.StoreTableDTO;
import com.basoft.eorder.interfaces.query.StoreTableQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ProjectName: intellij_work
 * @Package: com.basoft.eorder.foundation.jdbc.query
 * @ClassName: JdbcStoreTableQueryImpl
 * @Description: TODO
 * @Author: liminzhe
 * @Date: 2018-12-13 16:37
 * @Version: 1.0
 */
@Component("StoreTableQuery")
public class JdbcStoreTableQueryImpl extends BaseRepository implements StoreTableQuery {
    private static final String STORE_TABLE_BY_ID = "select st.id as id, st.store_id as storeId, st.number as number, st.show_index as showIndex, st.qrcode_image_url as qrcodeImageUrl,st.IS_SILENT as isSilent, st.created as created,st.status from store_table st where 1=1 ";

    private static final String STORE_TABLE_LIST_SELECT = "select st.id as id, st.store_id as storeId, st.number as number,CONCAT(tag,'-',number)as numberStr," +
            "st.max_number_seat as maxSeat,st.tag as tag,st.show_index as showIndex," +
            "st.qrcode_image_url as qrcodeImageUrl,st.IS_SILENT as isSilent, st.created as created \n";

    private static final String STORE_TABLE_FORM = "from store_table st \n where 1 = 1  \n";

    JdbcStoreTableQueryImpl() {
    }

    @Autowired
    public JdbcStoreTableQueryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public StoreTableDTO getStoreTableByMap(Map<String, Object> param) {
        StringBuilder query = new StringBuilder(STORE_TABLE_BY_ID);
        long id = NumberUtils.toLong(Objects.toString(param.get("id"), null));
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        String isSilent = Objects.toString(param.get("isSilent"), null);
        String status = Objects.toString(param.get("status"), null);

        if (id > 0) {
            query.append(" and st.id = :id \n");

        }

        if (storeId > 0) {
            query.append(" and st.store_id = :storeId");

        }

        if (StringUtils.isNotBlank(isSilent)) {
            query.append(" and IS_SILENT = :isSilent");

        }
        if (StringUtils.isNotBlank(status)) {
            query.append(" and status = :status");

        }

        return this.queryForObject(query.toString(), param, new BeanPropertyRowMapper<>(StoreTableDTO.class));
    }

    @Override
    public StoreTableDTO getStoreTableById(long id) {
        String sql = STORE_TABLE_BY_ID + " and id = :id";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return this.queryForObject(sql, map, new BeanPropertyRowMapper<>(StoreTableDTO.class));
    }

    @Override
    public int getStoreTableCount(Map<String, Object> param) {
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        String isSilent = Objects.toString(param.get("isSilent"), null);
        String status = Objects.toString(param.get("status"), null);

        StringBuilder query = new StringBuilder(SELECT_COUNT + STORE_TABLE_FORM);
        if (storeId > 0) {
            query.append(" and st.store_id = :storeId \n");
        }
        if (StringUtils.isNotBlank(isSilent)) {
            query.append(" and st.IS_SILENT = :isSilent ");
        }
        if (StringUtils.isNotBlank(status)) {
            query.append(" and st.status = :status ");
        }

        Integer count = this.getNamedParameterJdbcTemplate().queryForObject(query.toString(), param, Integer.class);
        return count.intValue();
    }

    @Override
    public List<StoreTableDTO> getStoreTableListByMap(Map<String, Object> param) {
        long storeId = NumberUtils.toLong(Objects.toString(param.get("storeId"), null));
        int page = NumberUtils.toInt(Objects.toString(param.get("page"), null));
        int size = NumberUtils.toInt(Objects.toString(param.get("size"), null));
        List<Long> ids = param.get("ids") == null ? null : (List<Long>) param.get("ids");

        StringBuilder query = new StringBuilder(STORE_TABLE_LIST_SELECT + STORE_TABLE_FORM);
        if (storeId > 0) {
            query.append(" and st.store_id = :storeId \n");
        }

        if (ids != null && ids.size() > 0) {
            query.append(" and st.id in (:ids) \n");
        }

        query.append(" and status !=3 ");

        query.append("ORDER BY tag  asc, number asc \n");

        if (page >= 0 && size > 0) {
            int resPage = page;
            if (page > 0) {
                resPage = (resPage - 1) * 10;
                param.put("page", resPage);
            }
            query.append(LIMIT);
        }

        return this.getNamedParameterJdbcTemplate().query(query.toString(), param, new BeanPropertyRowMapper<>(StoreTableDTO.class));
    }
}
