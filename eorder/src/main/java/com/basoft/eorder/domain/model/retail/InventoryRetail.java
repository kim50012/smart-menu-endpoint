package com.basoft.eorder.domain.model.retail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;


/**
 * 零售产品库存表(InventoryRetail)实体类
 *
 * @author DongXifu
 * @since 2020-04-07 17:46:48
 */
@AllArgsConstructor
@Builder
@Data
public class InventoryRetail  {

    public InventoryRetail() {

    }

    public InventoryRetail(Long invId,Long storeId,Long prodId,Long prodSkuId,Long invTotal,Long invSold,Long invBalance,String createUser) {
        this.invId = invId;
        this.storeId = storeId;
        this.prodId = prodId;
        this.prodSkuId = prodSkuId;
        this.invTotal = invTotal;
        this.invSold = invSold;
        this.invBalance = invBalance;
        this.createUser = createUser;
    }


    private Long invId;
    
    /**
     * 零售商户ID
     */
    private Long storeId;
    
    /**
     * 零售商户产品
     */
    private Long prodId;
    
    /**
     * 零售商户产品SKU
     */
    private Long prodSkuId;
    
    /**
     * 库存总量，一旦设置不可修改，持久性的由增加量或减少量决定该值
     */
    private Long invTotal;
    
    /**
     * 库存情况下的销售总量，小于等于库存总量。
     */
    private Long invSold;
    
    /**
     * 库存余量。零售库存关键字段，可以被调整增加，调整减少。调整时同步调整INV_TOTAL
     */
    private Long invBalance;
    
    /**
     * 创建时间생성시간
     */
    private Date createTime;
    
    /**
     * 创建人
     */
    private String createUser;
    
    /**
     * 修改时间수정시간
     */
    private Date updateTime;
    
    /**
     * 修改人
     */
    private String updateUser;



}