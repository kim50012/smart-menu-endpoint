package com.basoft.eorder.domain.model;

public class CompanyInfo {

    private String ceo;//法人
    private String scope;//经营范围
    private String shopHour;//营业时间

    public CompanyInfo(String ceo, String bizScope,String shopHour) {

        this.ceo = ceo;
        this.scope = bizScope;
        this.shopHour = shopHour;
    }


    public String getCeo() {
        return ceo;
    }

    public String getScope() {
        return scope;
    }

    public String getShopHour() {
        return shopHour;
    }
}
