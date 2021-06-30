package com.basoft.eorder.interfaces.query.retail.cms;

import com.basoft.eorder.interfaces.query.OrderDTO;
import com.basoft.eorder.interfaces.query.OrderItemDTO;

import java.util.List;
import java.util.Map;

/**
 * 商户零售产品规格表(ProductAloneStandard)表数据库访问层
 *
 * @author DongXifu
 * @since 2020-04-10 17:44:14
 */
public interface RetailQuery {

    int getRetailOrderCount(Map<String, Object> param);

    List<OrderDTO> getRetailOrderList(Map<String, Object> param);

    List<OrderItemDTO> getOrderItemListByMap(Map<String, Object> param);

    /**
     * 查询产品下的standard和item
     * @param param
     * @return
     */
    List<ProductAloneStandardDTO> getProductStandardListByMap(Map<String, Object> param);

    /**
     * 查询规格和standard关系
     * @param param
     * @return
     */
    List<ProSkuItemNameDTO> getSkuStandardList(Map<String, Object> param);

    /**
     * 有规格情况下设置规格和standard组合
     * standard和item组合逗号隔开
     *
     * @param productIdList
     * @return
     */
    List<StandardAndItemDTO> getStandardAndItemList(Long ...productIdList);

    /**
     * 没有没有规格情况下设置规格和standard组合
     * standard和item组合逗号隔开
     *
     * @param productIdList
     * @return
     */
    List<StandardAndItemDTO> getNoStandardAndItemList(Long ...productIdList);

    /**
     * 查询订单状态数量列表
     *
     * @param param
     * @return
     */
    List<RetailOrderStatusDTO> getRetailOrderStatusCountList(Map<String, Object> param);

    List<RetailOrderStatusDTO> getRetailServOrderStatusCountList(Map<String, Object> param);
}