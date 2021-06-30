package com.basoft.eorder.domain.model.retail.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * PRODUCT_ALONE_STANDARD_TEMPLATE零售商户产品规格模板表(ProductAloneStandardTemplate)实体类
 *
 * @author DongXifu
 * @since 2020-04-16 13:13:53
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductAloneStandardTemplate {

    @JsonProperty("tId")
    private Long tId; //零售商户产品规格模板ID
    
    private Long storeId; //零售商户ID

    private String tNameChn; //规格模板的中文名称
    
    private String tNameKor; //规格模板的韩文名称
    
    private String tNameEng; //规格模板的英文名称

    @JsonProperty("tStatus")
    private int tStatus; // 0停用 1 正常 2 删除

    private String desKor; //模版描述

    private String desChn;

    private Date createTime; //创建时间생성시간
    
    private String createUser; //创建人
    
    private Date updateTime; //修改时间수정시간
    
    private String updateUser; //修改人
    


}