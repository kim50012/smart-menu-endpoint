package com.basoft.eorder.interfaces.query.retail.cms;

import lombok.Data;

@Data
public class ProSkuItemNameDTO {

    private Long productSkuId;
    private Long standardId;
    private Long standardItemId;
    private int disOrder;
    private String itemNameChn;
    private String itemNameKor;
    private String itemNameEng;
}
