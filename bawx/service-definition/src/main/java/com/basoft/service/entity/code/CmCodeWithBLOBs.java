package com.basoft.service.entity.code;

public class CmCodeWithBLOBs extends CmCode {
    private String cdVal;

    private String cdDesc;

    public String getCdVal() {
        return cdVal;
    }

    public void setCdVal(String cdVal) {
        this.cdVal = cdVal == null ? null : cdVal.trim();
    }

    public String getCdDesc() {
        return cdDesc;
    }

    public void setCdDesc(String cdDesc) {
        this.cdDesc = cdDesc == null ? null : cdDesc.trim();
    }
}