package com.basoft.service.entity.code;

public class CmCodeKey {
    private String tbNm;

    private String fdNm;

    private Integer cdId;

    public String getTbNm() {
        return tbNm;
    }

    public void setTbNm(String tbNm) {
        this.tbNm = tbNm == null ? null : tbNm.trim();
    }

    public String getFdNm() {
        return fdNm;
    }

    public void setFdNm(String fdNm) {
        this.fdNm = fdNm == null ? null : fdNm.trim();
    }

    public Integer getCdId() {
        return cdId;
    }

    public void setCdId(Integer cdId) {
        this.cdId = cdId;
    }
}