package com.basoft.eorder.interfaces.query.inventory.hotel;

import lombok.Data;

import java.util.Date;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 5:26 PM 12/6/19
 **/
@Data
public class StoreDayPriceDTO {
    private Long sdpId;

    private Long storeId;

    private Long prodId;

    private Long prodSkuId;

    private Date invDate;

    private Long minPrice;

    private Long maxPrice;

    private Long avgPrice;

}
