package com.basoft.eorder.interfaces.query;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class AreaDTO {

    private String areaCd;
    private String areaNm;
    private String parentCd;
    private int areaStatus;
    private int areaLvl;
    private String useYn;
    private String created;

    public String getAreaCd() {
        return areaCd;
    }

    public void setAreaCd(String areaCd) {
        this.areaCd = areaCd;
    }

    public String getAreaNm() {
        return areaNm;
    }

    public void setAreaNm(String areaNm) {
        this.areaNm = areaNm;
    }

    public String getParentCd() {
        return parentCd;
    }

    public void setParentCd(String parentCd) {
        this.parentCd = parentCd;
    }

    public int getAreaStatus() {
        return areaStatus;
    }

    public void setAreaStatus(int areaStatus) {
        this.areaStatus = areaStatus;
    }

    public int getAreaLvl() {
        return areaLvl;
    }

    public void setAreaLvl(int areaLvl) {
        this.areaLvl = areaLvl;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
