package com.basoft.eorder.interfaces.query.retail.api;

import com.basoft.eorder.interfaces.query.retail.cms.ProductAloneStandardItemDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductAloneStandardInfoVO {

    private Long stdId;

    private Long storeId;//零售商户ID

    private Long prodId;//零售商户产品ID

    private String stdNameChn;//产品规格中文名称

    private String stdNameKor;//产品规格韩文名称

    private String stdNameEng;// 产品规格英文名称

    private Object disOrder;//显示顺序

    private String stdImage;//规格图片

    private Object stdStatus;//使用状态 0-停用 1-在用

    List<ProductAloneStandardItemDTO> standardItemList;

    //规格项ID@规格中文名称, 规格项ID@规格中文名称,规格项ID@规格中文名称...
    private String productAloneStandardItemInfo;
}