package com.basoft.eorder.interfaces.command.advice;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BusinessTypeEnum;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.domain.AdviceRepository;
import com.basoft.eorder.domain.model.Advice;
import com.basoft.eorder.interfaces.command.BatchUpAdviceStatus;
import com.basoft.eorder.interfaces.command.review.SaveReview;
import com.basoft.eorder.util.UidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author:DongXifu
 * @Description:投诉与建议
 * @Date Created in 下午1:55 2019/5/20
 **/

@CommandHandler.AutoCommandHandler("AdviceCommandHandler")
@Transactional
public class AdviceCommandHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UidGenerator uidGenerator;
    @Autowired
    private AdviceRepository adviceRepository;




    /**
     * 新增投诉意见
     * @Param saveAdvice
     * @return int
     * @author Dong Xifu
     * @date 2019/6/4 下午1:58
     */
    @CommandHandler.AutoCommandHandler(SaveAdvice.NAME)
    @Transactional
    public int saveAdvice(SaveAdvice saveAdvice, CommandHandlerContext context){
        logger.debug("Start saveAdvice -------");
        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);

        Long advId =  uidGenerator.generate(BusinessTypeEnum.ADVICE);
        saveAdvice.setAdviId(advId);
        saveAdvice.setAdviStatus(Advice.ADVICE_STATUS_START);
        saveAdvice.setStoreId(us.getStoreId());
        Advice advice = saveAdvice.build();
        return adviceRepository.saveAdvice(advice);
    }

    /**
     * @Title 回复意见反馈
     * @Param updateAdvice
     * @return int
     * @author Dong Xifu
     * @date 2019/5/21 上午10:52
     */
    @CommandHandler.AutoCommandHandler(UpdateAdvice.NAME)
    @Transactional
    public int replyAdvice(UpdateAdvice updateAdvice, CommandHandlerContext context) {
        logger.debug("Start updateAdvice -------");
        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);

        updateAdvice.setAdviStatus(Advice.ADVICE_STATUS_RESOLVED);
       // updateAdvice.setAdviReplier(us.getAccount());
       // updateAdvice.setModifiedUserId(us.getAccount());
        Advice advice = updateAdvice.build();

        return adviceRepository.updateAdvice(advice);
    }



    /**
     * @Title 删除意见反馈
     * @Param
     * @return int
     * @author Dong Xifu
     * @date 2019/5/21 上午11:03
     */
    @CommandHandler.AutoCommandHandler(BatchUpAdviceStatus.NAME)
    @Transactional
    public int batchDelAdviceStatus(BatchUpAdviceStatus batchUpAdviceStatus) {
        logger.debug("Start updateAdvice -------");
        batchUpAdviceStatus.setStatus(Advice.ADVICE_STATUS_DEL);

        return adviceRepository.bachUpAdviceStatus(batchUpAdviceStatus);
    }


    /**
     * 删除投诉意见图片
     * @Param imgStatus
     * @return java.lang.Long
     * @author Dong Xifu
     * @date 2019/6/4 下午1:17
     */
    @CommandHandler.AutoCommandHandler(BatchUpAdvImgStatus.NAME)
    @Transactional
    public int batchUpAdviceImgStatus(BatchUpAdvImgStatus imgStatus) {
        logger.debug("Start batchUpAdviceImgStatus -------");
        imgStatus.setIsDisplay(Advice.AdviceImg.ADVICEIMG_HIDE);
        return adviceRepository.updateAdviceImages(imgStatus);
    }



}
