package com.basoft.eorder.interfaces.command.holiday;

import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.wx.model.BaseResult;
import com.basoft.eorder.domain.HolidayRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:14 2019/7/24
 **/
@CommandHandler.AutoCommandHandler("HolidayCommandHandler")
@Transactional
public class HolidayCommandHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private HolidayRepository holidayRepository;

    @Autowired
    public HolidayCommandHandler(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    /**
     * SaveHoliday
     *
     * @param saveHoliday
     * @return int
     * @author dikim
     * @date 2019/5/22
     */
    @CommandHandler.AutoCommandHandler(SaveHoliday.NAME)
    @Transactional
    public BaseResult saveHoliday(SaveHoliday saveHoliday) {

        BaseResult baseResult = null;
        baseResult = new BaseResult();
        
    	SaveHoliday rSaveHoliday = holidayRepository.saveHoliday(saveHoliday);

        baseResult.setReturn_code("SUCCESS");
        baseResult.setReturn_msg("Save holiday is success!");
        
        return baseResult;
    }
}
