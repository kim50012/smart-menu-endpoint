package com.basoft.service.entity.code;

import java.util.Date;

public class CmCode extends CmCodeKey {
    private Byte isDelete;

    private Date createdDt;

    private Date modifyDt;

    private String cdTp;

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getModifyDt() {
        return modifyDt;
    }

    public void setModifyDt(Date modifyDt) {
        this.modifyDt = modifyDt;
    }

    public String getCdTp() {
        return cdTp;
    }

    public void setCdTp(String cdTp) {
        this.cdTp = cdTp == null ? null : cdTp.trim();
    }
}