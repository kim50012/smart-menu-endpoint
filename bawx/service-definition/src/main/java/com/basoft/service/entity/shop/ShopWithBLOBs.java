package com.basoft.service.entity.shop;

public class ShopWithBLOBs extends Shop {
    private String legalPersonInrtoduce;

    private String descIntroduction;

    private String descBeforeSales;

    private String descAfterSales;

    private String descWorkingtime;

    public String getLegalPersonInrtoduce() {
        return legalPersonInrtoduce;
    }

    public void setLegalPersonInrtoduce(String legalPersonInrtoduce) {
        this.legalPersonInrtoduce = legalPersonInrtoduce == null ? null : legalPersonInrtoduce.trim();
    }

    public String getDescIntroduction() {
        return descIntroduction;
    }

    public void setDescIntroduction(String descIntroduction) {
        this.descIntroduction = descIntroduction == null ? null : descIntroduction.trim();
    }

    public String getDescBeforeSales() {
        return descBeforeSales;
    }

    public void setDescBeforeSales(String descBeforeSales) {
        this.descBeforeSales = descBeforeSales == null ? null : descBeforeSales.trim();
    }

    public String getDescAfterSales() {
        return descAfterSales;
    }

    public void setDescAfterSales(String descAfterSales) {
        this.descAfterSales = descAfterSales == null ? null : descAfterSales.trim();
    }

    public String getDescWorkingtime() {
        return descWorkingtime;
    }

    public void setDescWorkingtime(String descWorkingtime) {
        this.descWorkingtime = descWorkingtime == null ? null : descWorkingtime.trim();
    }
}