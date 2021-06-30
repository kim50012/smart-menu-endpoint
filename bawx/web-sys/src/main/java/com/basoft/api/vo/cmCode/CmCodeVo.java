package com.basoft.api.vo.cmCode;

import com.basoft.service.definition.wechat.BizConstants;
import com.basoft.service.entity.code.CmCodeWithBLOBs;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:30 2018/4/11
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmCodeVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Integer cdId;

    private String tbNm;

    private String fdNm;

    private String cdVal;

    private Byte isDelete;

    private String isDeleteStr;

    private String cdDesc;

    public CmCodeVo(CmCodeWithBLOBs data) {
        this.cdId = data.getCdId();
        this.tbNm = data.getTbNm();
        this.fdNm = data.getFdNm();
        this.cdVal = data.getCdVal();
        this.isDelete = data.getIsDelete();
        if(data.getIsDelete()==0){
            this.isDeleteStr = BizConstants.IS_DELETE_START;
        }else{
            this.isDeleteStr = BizConstants.IS_DELETE_FORBID;
        }
        this.cdDesc = data.getCdDesc();
    }
}
