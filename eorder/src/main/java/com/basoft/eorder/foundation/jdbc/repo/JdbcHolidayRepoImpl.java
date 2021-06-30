package com.basoft.eorder.foundation.jdbc.repo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.basoft.eorder.domain.HolidayRepository;
import com.basoft.eorder.domain.model.HolidayItem;
import com.basoft.eorder.interfaces.command.holiday.SaveHoliday;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:17 2019/7/24
 **/
@Repository
@Transactional
public class JdbcHolidayRepoImpl  extends BaseRepository implements HolidayRepository {

    @Override
    @Transactional
    public SaveHoliday saveHoliday(SaveHoliday saveHoliday) {

    	// reset holiday
    	updateHolidayReset(saveHoliday);
    	
        // insert order items to temp
        List<HolidayItem> itemList = saveHoliday.getHolidayItem();
        if (itemList != null && itemList.size() > 0) {
        	Long storeId = saveHoliday.getStoreId();
        	String cmt = saveHoliday.getCmt();
            List<HolidayItem> newItemList = itemList.stream().map(item -> {
            	HolidayItem holidayItem = item.createNewHolidayItem(storeId, cmt, item).build();
                return holidayItem;
            }).collect(Collectors.toList());
            saveHolidayItem(newItemList);
        }
        return saveHoliday;
    }

    public void updateHolidayReset(SaveHoliday saveHoliday) {

        this.getNamedParameterJdbcTemplate().update(""
        		+ "insert into eorder.calendar_holiday\n" + 
        		"    (\n" + 
        		"        store_id\n" + 
        		"        ,dt\n" + 
        		"        ,yyyy\n" + 
        		"        ,mm\n" + 
        		"        ,dd\n" + 
        		"        ,week_of_days\n" + 
        		"        ,working_day\n" + 
        		"        ,cmt\n" + 
        		"    )\n" + 
        		"select\n" + 
        		"    :storeId\n" + 
        		"    , t1.dt\n" + 
        		"    , t1.yyyy\n" + 
        		"    , t1.mm\n" + 
        		"    , t1.dd\n" + 
        		"    , t1.week_of_days\n" + 
        		"    , 1\n" + 
        		"    , cmt\n" + 
        		"from    eorder.calendar t1\n" + 
        		"where   t1.dt like concat(:dt, '%')\n" + 
        		"on duplicate key\n" + 
        		"    update\n" + 
        		"        yyyy = t1.yyyy\n" + 
        		"        ,mm = t1.mm\n" + 
        		"        ,dd = t1.dd\n" + 
        		"        ,week_of_days = t1.week_of_days\n" + 
        		"        ,working_day = 1\n" + 
        		"        ,cmt = t1.cmt\n" + 
        		";"
        		+ "", new BeanPropertySqlParameterSource(saveHoliday));
    }
    
    
    public void saveHolidayItem(List<HolidayItem> holidayItem) {

        this.getNamedParameterJdbcTemplate().batchUpdate(""
        		+ "insert into eorder.calendar_holiday\n" + 
        		"    (\n" + 
        		"        store_id\n" + 
        		"        ,dt\n" + 
        		"        ,yyyy\n" + 
        		"        ,mm\n" + 
        		"        ,dd\n" + 
        		"        ,week_of_days\n" + 
        		"        ,working_day\n" + 
        		"        ,cmt\n" + 
        		"    )\n" + 
        		"    values \n" + 
        		"    (\n" + 
        		"        :storeId\n" + 
        		"        ,:dt\n" + 
        		"        ,date_format(:dt, '%Y')\n" + 
        		"        ,date_format(:dt, '%m')\n" + 
        		"        ,date_format(:dt, '%d')\n" + 
        		"        ,dayofweek(:dt)\n" + 
        		"        ,0\n" + 
        		"        ,:cmt\n" + 
        		"    )\n" + 
        		"on duplicate key\n" + 
        		"    update\n" + 
        		"        yyyy = date_format(:dt, '%Y')\n" + 
        		"        ,mm = date_format(:dt, '%m')\n" + 
        		"        ,dd = date_format(:dt, '%d')\n" + 
        		"        ,week_of_days = dayofweek(:dt)\n" + 
        		"        ,working_day = 0\n" + 
        		"        ,cmt = :cmt"
        		+ "", SqlParameterSourceUtils.createBatch(holidayItem.toArray()));
    }
}
