package com.basoft.eorder.foundation.jdbc.query;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.basoft.eorder.foundation.jdbc.repo.BaseRepository;
import com.basoft.eorder.interfaces.query.HolidayDTO;
import com.basoft.eorder.interfaces.query.HolidayQuery;
import com.basoft.eorder.interfaces.query.OrderDTO;
import com.basoft.eorder.interfaces.query.OrderQuery;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:18 2019/7/24
 **/

@Component("HolidayQuery")
public class JdbcHolidayQueryImpl extends BaseRepository implements HolidayQuery {
    private Logger logger = LoggerFactory.getLogger(JdbcHolidayQueryImpl.class);

    private final static String HOLIDAY_QUERY_SELECT = ""
    		+ "select\n" + 
    		"        group_concat(de order by dd asc) as holiday\n" + 
    		"from    (\n" + 
    		"            select\n" + 
    		"                    date_format(t1.dt, '%d') as dd\n" + 
    		"                    , date_format(t1.dt, '%e') as de\n" + 
    		"                    , case when t2.working_day is null then t1.working_day else t2.working_day end as workingDay\n" + 
    		"            from    eorder.calendar t1\n" + 
    		"                    left join eorder.calendar_holiday t2\n" + 
    		"                            on t1.dt = t2.dt\n" + 
    		"                            and t2.store_id = :storeId\n" + 
    		"            where   t1.dt like concat(:dt, '%')\n" + 
    		"        ) as tt\n" + 
    		"where   tt.workingDay = 0\n" + 
    		"";


    public HolidayDTO getHolidayByMap(Map<String, Object> param) {
        StringBuilder sql = new StringBuilder(HOLIDAY_QUERY_SELECT);
        return this.queryForObject(sql.toString(),param,new BeanPropertyRowMapper<>(HolidayDTO.class));
    }

}
