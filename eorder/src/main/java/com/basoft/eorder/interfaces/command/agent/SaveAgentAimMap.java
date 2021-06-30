package com.basoft.eorder.interfaces.command.agent;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.agent.AgentAimMap;
import java.sql.Date;
import java.util.List;

public class SaveAgentAimMap implements Command {

    public static final String SaveAgentAimMap = "saveAgentAimMap";


    //columns START
    private List<AgentAimMap> aimMapList;

    private Long id;  //id  db_column: ID

    private Long agtId;  //代理商id  db_column: AGT_ID

    private String storeId;  //店铺id  db_column: STORE_ID

    private String openId;  //openid  db_column: open_id

    private Integer status;  //状态：1-正常 2禁用 3 删除  db_column: STATUS

    private Integer agtType;  //代理商类型 1-SA  2-CA  db_column: AGENT_TYPE

    private String agtRate;  //手续费百分比  db_column: CHARGE_RATE

    private String agtPercent;

    private Date contractSt;  //合同起始时间  db_column: CONTRACT_ST

    private Date contractEd;  //合同结束时间  db_column: CONTRACT_ED

    private Date createTime;  //创建时间  db_column: CREATE_TIME

    private Date updateTime;  //updateTime  db_column: UPDATE_TIME

    //columns END

    public List<AgentAimMap> getAimMapList() {
        return aimMapList;
    }

    public void setAimMapList(List<AgentAimMap> aimMapList) {
        this.aimMapList = aimMapList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgtId() {
        return agtId;
    }

    public void setAgtId(Long agtId) {
        this.agtId = agtId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAgtType() {
        return agtType;
    }

    public void setAgtType(Integer agtType) {
        this.agtType = agtType;
    }

    public String getAgtRate() {
        return agtRate;
    }

    public void setAgtRate(String agtRate) {
        this.agtRate = agtRate;
    }

    public String getAgtPercent() {
        return agtPercent;
    }

    public void setAgtPercent(String agtPercent) {
        this.agtPercent = agtPercent;
    }

    public Date getContractSt() {
        return contractSt;
    }

    public void setContractSt(Date contractSt) {
        this.contractSt = contractSt;
    }

    public Date getContractEd() {
        return contractEd;
    }

    public void setContractEd(Date contractEd) {
        this.contractEd = contractEd;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public AgentAimMap build(Store s){
        return new AgentAimMap.Builder()
                .id(id)
                .agtId(agtId)
                .storeId(storeId)
                .openId(openId)
                .status(status)
                .agtType(agtType)
                .agtChargeRate(agtRate)
                .agtChargePercent(agtPercent)
                .contractSt(contractSt)
                .contractEd(contractEd)
                .createTime(createTime)
                .updateTime(updateTime)
                .aimMapList(aimMapList,s)
                .build();
    }
}

