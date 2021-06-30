package com.basoft.eorder.batch.job.model.retail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetailToDoRecoverTempOrder {
    // 订单编号
    private Long transId;

    // 订单业务编号
    private Long id;

    private Long storeId;

    private Long skuId;

    private Long qty;
}