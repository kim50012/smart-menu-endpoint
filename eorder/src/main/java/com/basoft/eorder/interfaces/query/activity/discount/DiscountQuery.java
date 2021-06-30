package com.basoft.eorder.interfaces.query.activity.discount;

import com.basoft.eorder.domain.model.activity.discount.Discount;
import com.basoft.eorder.domain.model.activity.discount.DiscountDetail;

import java.util.List;
import java.util.Map;

/**
 * 折扣活动查询
 *
 * @author Mentor
 * @version 1.0
 * @since 20180522
 */
public interface DiscountQuery {
    /**
     * 查询折扣活动详情
     *
     * @param param
     * @return
     */
    Discount getDiscount(Map<String, Object> param);

    /**
     * 查询折扣活动详情的明细部分
     *
     * @param param
     * @return
     */
    List<DiscountDetail> getDiscountDetails(Map<String, Object> param);

    /**
     * 根据查询条件查询记录总数
     *
     * @param param
     * @return
     */
    int getDiscountCount(Map<String, Object> param);

    /**
     * 根据查询条件查询记录信息
     *
     * @param param
     * @return
     */
    List<Discount> getDiscountList(Map<String, Object> param);

    /**
     * 根据门店ID查询该门店的折扣产品列表
     *
     * @param param
     * @return
     */
    public List<DiscountDisplayDTO> getDiscountDisplayList(Map<String, Object> param);

    /**
     * 根据门店ID查询该门店的折扣产品列表-用于订单核价(为了提高系统性能不进行汇率计算)
     *
     * @param param
     * @return
     */
    public List<DiscountDisplayDTO> getDiscountDisplayList4OrderCheck(Map<String, Object> param);

    /**
     * 零售业务：
     * 根据活动ID和产品sku查询sku的活动详情
     *
     * @param discIdList
     * @param prodSkuIdList
     * @return
     */
    List<DiscountDetail> getDiscountDetailList(List<Long> discIdList, List<Long> prodSkuIdList);
}