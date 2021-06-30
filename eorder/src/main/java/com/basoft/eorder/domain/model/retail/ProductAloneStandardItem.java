package com.basoft.eorder.domain.model.retail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 商户零售产品规格项目表(ProductAloneStandardItem)实体类
 *
 * @author DongXifu
 * @since 2020-04-03 09:20:07
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductAloneStandardItem {

    private Long tId; // 模版id

    private Long itemId; //零售产品规格项目ID

    private Long prodId; //产品id

    private Long stdId; //零售产品规格ID

    private String itemNameChn; //产品规格项目中文名称

    private String itemNameKor; //产品规格项目韩文名称

    private String itemNameEng; //产品规格项目英文名称

    private int disOrder; //显示顺序

    private String itemImage; //规格项目图片

    private int itemStatus; //使用状态 0-停用 1-在用

    private Long invTotal; //库存

    private Date createTime; //创建时间생성시간

    private String createUser; //创建人

    private Date updateTime; //修改时间수정시간

    private String updateUser; //修改人


}