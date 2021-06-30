package com.basoft.eorder.domain.model;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:14 2019/6/11
 **/
public class AdvAdvertisementDetail {
    private Long advDetId;

    private Long advId;

    private String contentName;

    private String contentUrl;

    private String targetUrl;

    private int advDetOrder;

    private String useYn;

    public Long getAdvDetId() {
        return advDetId;
    }

    public void setAdvDetId(Long advDetId) {
        this.advDetId = advDetId;
    }

    public Long getAdvId() {
        return advId;
    }

    public void setAdvId(Long advId) {
        this.advId = advId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public int getAdvDetOrder() {
        return advDetOrder;
    }

    public void setAdvDetOrder(int advDetOrder) {
        this.advDetOrder = advDetOrder;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }
}
