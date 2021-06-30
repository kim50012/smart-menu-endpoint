package com.basoft.eorder.interfaces.query;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:57 2019/1/3
 **/
public class SkuStandardMapDTO {
    private Long skuId;
    private Long prdStandardId;
    private String prdStandardNameVal;
    private Long productId;
    private String chnName;
    private int baseExtend;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getPrdStandardId() {
        return prdStandardId;
    }

    public void setPrdStandardId(Long prdStandardId) {
        this.prdStandardId = prdStandardId;
    }

    public String getPrdStandardNameVal() {
        return prdStandardNameVal;
    }

    public void setPrdStandardNameVal(String prdStandardNameVal) {
        this.prdStandardNameVal = prdStandardNameVal;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public int getBaseExtend() {
        return baseExtend;
    }

    public void setBaseExtend(int baseExtend) {
        this.baseExtend = baseExtend;
    }
}
