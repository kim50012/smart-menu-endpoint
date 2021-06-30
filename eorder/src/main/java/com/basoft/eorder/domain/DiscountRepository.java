package com.basoft.eorder.domain;

import com.basoft.eorder.domain.model.activity.discount.Discount;
import com.basoft.eorder.domain.model.activity.discount.DiscountDetail;

import java.util.List;

/**
 * Discount折扣存储接口
 *
 * @version 1.0
 * @author Mentor
 * @since 20190515
 */
public interface DiscountRepository {
    /**
     * 折扣新增
     *
     * @param discount
     * @param discountDetails
     */
    int saveDiscount(Discount discount, List<DiscountDetail> discountDetails);

    /**
     * 折扣修改
     *
     * @param isReform 是否重做产品明细 y或Y-重做 n-不重做
     * @param discount
     * @param discountDetails
     * @return
     */
    int updateDiscount(String isReform, Discount discount, List<DiscountDetail> discountDetails);
}