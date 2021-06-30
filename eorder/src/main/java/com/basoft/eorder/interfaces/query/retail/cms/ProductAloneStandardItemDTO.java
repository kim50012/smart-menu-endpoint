package com.basoft.eorder.interfaces.query.retail.cms;

import lombok.Data;

import java.util.Date;

@Data
public class ProductAloneStandardItemDTO {
    private Long itemId; //零售产品规格项目ID

    private Long stdId; //零售产品规格ID

    private String itemNameChn; //产品规格项目中文名称

    private String itemNameKor; //产品规格项目韩文名称

    private String itemNameEng; //产品规格项目英文名称

    private int disOrder; //显示顺序

    private String itemImage; //规格项目图片

    private int itemStatus; //使用状态 0-停用 1-在用

    private Date createTime; //创建时间

    private String createUser; //创建人

    private Date updateTime; //修改时间

    private String updateUser; //修改人
}