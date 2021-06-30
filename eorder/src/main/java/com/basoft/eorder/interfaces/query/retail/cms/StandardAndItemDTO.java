package com.basoft.eorder.interfaces.query.retail.cms;

import lombok.Data;

/**
 * 规格和item组合名称逗号隔开
 */

@Data
public class StandardAndItemDTO {

    private Long invId;

    private Long prodId;

    private Long stdId;

    private Long itemId;

    private Long prodSkuId;

    private Long invTotal; //库存总量，一旦设置不可修改，持久性的由增加量或减少量决定该值

    private Long invSold; //库存情况下的销售总量，小于等于库存总量。

    private Long invBalance; //库存余量。零售库存关键字段，可以被调整增加，调整减少。调整时同步调整INV_TOTAL

    private int isInventory;

    private int status;

    private String stdItemNameChn;

    private String stdItemNameKor;

    private String stdItemNameEng;
}
