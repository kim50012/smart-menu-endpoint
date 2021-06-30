package com.basoft.eorder.interfaces.command.advert;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.domain.AdvertRepository;
import com.basoft.eorder.domain.model.Advertisement;
import com.basoft.eorder.interfaces.query.AdvertQuery;
import com.basoft.eorder.util.UidGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author:DongXifu
 * @Description: 广告
 * @Date Created in 下午2:25 2019/6/1
 **/
@CommandHandler.AutoCommandHandler("AdvertCommandHandler")
@Transactional
public class AdvertCommandHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdvertRepository advertRepository;
    @Autowired
    private AdvertQuery advertQuery;
    @Autowired
    private UidGenerator uidGenerator;


    /**
     * 新增或修改广告
     * @Param context
     * @Param saveReview
     * @return int
     * @author Dong Xifu
     * @date 2019/6/3 上午10:54
     */
    @CommandHandler.AutoCommandHandler(SaveAdvert.NAME)
    @Transactional
    public Long saveAdvert(SaveAdvert saveAdvert){

        logger.debug("Start saveAdvert -------");
        String advId = saveAdvert.getAdvId();
        if (StringUtils.isNotBlank(advId)) {
            Advertisement advert = saveAdvert.build();
            advertRepository.updateAdvert(advert);
        }else{
            advId =  String.valueOf(uidGenerator.generate(BusinessTypeEnum.REVIEW));
            saveAdvert.setAdvId(advId);
            Advertisement advert = saveAdvert.build();

            advertRepository.saveAdvert(advert);
        }
        return Long.valueOf(saveAdvert.getAdvId());
    }


    /**
     * 修改广告状态
     * @Param upStatusAdvert
     * @return java.lang.Long
     * @author Dong Xifu
     * @date 2019/6/10 上午10:49
     */
    @CommandHandler.AutoCommandHandler(BatchUpStatusAdvert.NAME)
    @Transactional
    public int batchUpStatusAdvert(BatchUpStatusAdvert upStatusAdvert) {
       return advertRepository.upStatusAdvert(upStatusAdvert);
    }

}
