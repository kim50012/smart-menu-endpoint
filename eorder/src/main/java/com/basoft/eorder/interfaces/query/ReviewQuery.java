package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.domain.model.Review;

import java.util.List;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:10 2019/5/14
 **/
public interface ReviewQuery {

    ReviewDTO getReviewDto(Map<String, Object> param);

    Review getReview(Map<String, Object> param);

    int getReviewCount(Map<String, Object> param);

    List<ReviewDTO> getReviewList(Map<String, Object> param);
}
