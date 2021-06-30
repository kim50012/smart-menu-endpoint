package com.basoft.eorder.interfaces.query.postStoreSet;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PostStoreSetDetailDTO {
   
    private Long pssId; //配送设置ID
   
    private Integer detailNo; //收费明细编号
   
    private BigDecimal lowerLimit; //该配送明细设置重量下限
   
    private BigDecimal upperLimit; //该配送明细设置重量上限，包含
   
    private BigDecimal chargeFee; //该重量区间收费金额，韩币
   
}