package com.basoft.eorder.interfaces.command.review;

import com.basoft.eorder.application.*;
import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.application.framework.CommandHandlerContext;
import com.basoft.eorder.application.framework.QueryHandler;
import com.basoft.eorder.domain.ReviewRepository;
import com.basoft.eorder.domain.model.Review;
import com.basoft.eorder.interfaces.command.BatchUpReviewStatus;
import com.basoft.eorder.interfaces.command.TransReview;
import com.basoft.eorder.interfaces.query.ReviewQuery;
import com.basoft.eorder.util.TranslateUtil;
import com.basoft.eorder.util.UidGenerator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:18 2019/5/13
 **/

@CommandHandler.AutoCommandHandler("ReviewCommandHandler")
@Transactional
public class ReviewCommandHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewQuery reviewQuery;
    @Autowired
    private UidGenerator uidGenerator;



    /**
     * @Title 新建评论
     * @Param
     * @return int
     * @author Dong Xifu
     * @date 2019/5/13 下午6:13
     */
    @CommandHandler.AutoCommandHandler(SaveReview.NAME)
    @Transactional
    public int saveReview(SaveReview saveReview, CommandHandlerContext context){
        logger.debug("Start saveReview -------");

        Long revId =  uidGenerator.generate(BusinessTypeEnum.REVIEW);
        saveReview.setRevId(revId);
        Review review = saveReview.build();
        return reviewRepository.saveReview(review);
    }

    /**
     * @Title 回复评论
     * @Param
     * @return int
     * @author Dong Xifu
     * @date 2019/5/14 上午9:40
     */
    @CommandHandler.AutoCommandHandler(UpdateReview.REPLY_NAME)
    @Transactional
    public int replyReview( UpdateReview updateReview, CommandHandlerContext context){
        logger.debug("Start updateReview -------");
        if (updateReview.getRevId()==null||updateReview.getRevId()<1){
            throw new BizException(ErrorCode.PARAM_MISSING);
        }

        UserSession us = (UserSession) context.props().get(AppConfigure.BASOFT_USER_SESSION_PROP);

        Map<String, Object> param = new HashMap<>();
        param.put("revId",updateReview.getRevId());

        Review review = reviewQuery.getReview(param);
        if(review==null){
            throw new BizException(ErrorCode.SYS_EMPTY);
        }
        updateReview.setRevStatus(Review.REV_STATUS_REPLYED);
        updateReview.setModifiedUserId(us.getAccount());

        review = updateReview.build(review);

        return reviewRepository.updateReview(review);

    }


    @CommandHandler.AutoCommandHandler(TransReview.NAME)
    @Transactional
    public String translateReview(TransReview transReview){

        System.out.println("translateReview -------");
        Map<String, Object> param = new HashMap<>();
        param.put("revId",transReview.getRevId());
        Review review = reviewQuery.getReview(param);
        if (StringUtils.isNotBlank(transReview.getRevContent())) {
            if(StringUtils.isNotBlank(review.getRevContentCopie())){
                return review.getRevContentCopie();
            }else{
                String revCompie =  TranslateUtil.translateTest(transReview.getRevContent());
                transReview.setRevContentCopie(revCompie);
                transReview.setRevReplyContentCopie(review.getRevReplyContentCopie());
                reviewRepository.translateReview(transReview);
                return revCompie;
            }
        } else if (StringUtils.isNotBlank(transReview.getRevReplyContent())) {
            if (StringUtils.isNotBlank(review.getRevReplyContentCopie())) {
                return review.getRevReplyContentCopie();
            } else {
                String replyCompie =  TranslateUtil.translateTest(transReview.getRevReplyContent());
                transReview.setRevContentCopie(review.getRevContentCopie());
                transReview.setRevReplyContentCopie(replyCompie);
                reviewRepository.translateReview(transReview);
                return replyCompie;
            }
        }

        return "";
    }


    /**
     * @Title 删除评论
     * @Param
     * @return int
     * @author Dong Xifu
     * @date 2019/5/14 下午2:28
     */
    @CommandHandler.AutoCommandHandler(BatchUpReviewStatus.NAME)
    public int batchUpReviewStatus(BatchUpReviewStatus updateReview){
        if(updateReview.getRevIds().size()<1){
            new BizException(ErrorCode.PARAM_MISSING);
        }

        return reviewRepository.bachUpReviewStatus(updateReview);
    }


    /**
     * @Title 删除评论图片
     * @Param
     * @return int
     * @author Dong Xifu
     * @date 2019/5/27 下午4:22
     */
    @CommandHandler.AutoCommandHandler(BatchUpRevImgStatus.NAME)
    public int batchUpRevImgStatus(BatchUpRevImgStatus batchUpRevImgStatus) {
        if(batchUpRevImgStatus.getRevAttachId().size()<1){
            new BizException(ErrorCode.PARAM_MISSING);
        }

        return reviewRepository.bachUpRevImgStatus(batchUpRevImgStatus);
    }
}

