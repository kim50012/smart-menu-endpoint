package com.basoft.eorder.domain.model.retail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


/**
 * 商户零售产品规格表(ProductAloneStandard)实体类
 *
 * @author DongXifu
 * @since 2020-04-02 18:52:07
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductAloneStandard {

    private List<ProductAloneStandardItem> standardItemList; //规格项

    private List<ProductSkuAloneStandard> skuStandardList; //规格和standard关系

    private Long tId; // 模版id

    private Long stdId; //零售产品规格ID

    private Long storeId; //零售商户ID

    private Long prodId; //零售商户产品ID

    private String stdNameChn; //产品规格中文名称

    private String stdNameKor; //产品规格韩文名称

    private String stdNameEng; //产品规格英文名称

    private int disOrder; //显示顺序

    private String stdImage; //规格图片

    protected int stdStatus; //使用状态 0-停用 1-在用

    private Date createTime; //创建时间생성시간

    private String createUser; //创建人

    private Date updateTime; //修改时间수정시간

    private String updateUser; //修改人


}