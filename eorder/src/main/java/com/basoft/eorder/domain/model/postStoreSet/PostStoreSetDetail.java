package com.basoft.eorder.domain.model.postStoreSet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * 商户配送设置收费明细表(PostStoreSetDetail)实体类
 *
 * @author DongXifu
 * @since 2020-04-29 13:08:47
 */

@Builder
@Data
public class PostStoreSetDetail {

    private Long pssId; //配送设置ID
    
    private Integer detailNo; //收费明细编号
    
    private BigDecimal lowerLimit; //该配送明细设置重量下限
    
    private BigDecimal upperLimit; //该配送明细设置重量上限，包含
    
    private BigDecimal chargeFee; //该重量区间收费金额，韩币

    private String createUser;

    private String updateUser;

    


}