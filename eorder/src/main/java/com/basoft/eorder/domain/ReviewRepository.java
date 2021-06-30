package com.basoft.eorder.domain;

import com.basoft.eorder.domain.model.Review;
import com.basoft.eorder.interfaces.command.review.BatchUpRevImgStatus;
import com.basoft.eorder.interfaces.command.BatchUpReviewStatus;
import com.basoft.eorder.interfaces.command.TransReview;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:55 2019/5/13
 **/
public interface ReviewRepository {
    int saveReview(Review review);

    int updateReview(Review review);

    int bachUpReviewStatus(BatchUpReviewStatus upReviewStatus);

    int bachUpRevImgStatus(BatchUpRevImgStatus batchUpRevImgStatus);

    int translateReview(TransReview transReview);
}
