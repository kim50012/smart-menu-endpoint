package com.basoft.eorder.domain;

import com.basoft.eorder.interfaces.command.holiday.SaveHoliday;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:16 2019/7/24
 **/
public interface HolidayRepository {

	SaveHoliday saveHoliday(SaveHoliday saveHoliday);
}
