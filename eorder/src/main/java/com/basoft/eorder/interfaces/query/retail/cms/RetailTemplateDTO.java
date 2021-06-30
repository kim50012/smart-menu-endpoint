package com.basoft.eorder.interfaces.query.retail.cms;

import lombok.Data;

import java.util.List;

@Data
public class RetailTemplateDTO {
   
    private Long tId; //零售商户产品规格模板ID
   
    private Long storeId; //零售商户ID
   
    private String tNameChn; //规格模板的中文名称
   
    private String tNameKor; //规格模板的韩文名称
   
    private String tNameEng; //规格模板的英文名称
   
    private int tStatus; //模版状态

    private String desKor; //规格模板描述

    private String desChn; //规格模板描述


    private List<ProductAloneStandardDTO> aloneStandardList;




}