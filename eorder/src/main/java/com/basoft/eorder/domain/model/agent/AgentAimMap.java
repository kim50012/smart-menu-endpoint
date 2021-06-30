package com.basoft.eorder.domain.model.agent;

import com.basoft.eorder.domain.model.Store;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;


public class AgentAimMap {

    public static final double defaultRate = 0.33;
    //columns START
    private Long id;  //id

    private Long agtId;  //代理商id

    private String storeId;  //店铺id

    private String openId;  //微信用户id

    private Integer status;  //状态：1-正常 2禁用 3 删除

    private Integer agtType;  //代理商类型 1-SA  2-CA

    private String agtRate;  //手续费占支付金额

    private String agtPercent;  //手续费占店铺手续费百分比

    private Date contractSt;  //合同起始时间

    private Date contractEd;  //合同结束时间

    private Date createTime;  //创建时间

    private Date updateTime;  //updateTime

    private List<AgentAimMap> aimMapList;

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

    public List<AgentAimMap> getAimMapList() {
        return aimMapList;
    }

    public void setAimMapList(List<AgentAimMap> aimMapList) {
        this.aimMapList = aimMapList;
    }

    //columns END

    public static final class Builder {
        //columns START
        private Long id;  //id

        private Long agtId;  //代理商id

        private String storeId;  //店铺id

        private String openId;  //微信用户id

        private Integer status;  //状态：1-正常 2禁用 3 删除

        private Integer agtType;  //代理商类型 1-SA  2-CA

        private String agtChargeRate;  //手续费占支付金额

        private String agtChargePercent;  //手续费占店铺手续费百分比

        private Date contractSt;  //合同起始时间

        private Date contractEd;  //合同结束时间

        private Date createTime;  //创建时间

        private Date updateTime;  //updateTime

        private List<AgentAimMap> aimMapList;

        //columns END
        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder agtId(Long agtId) {
            this.agtId = agtId;
            return this;
        }

        public Builder storeId(String storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder openId(String openId) {
            this.openId = openId;
            return this;
        }

        public Builder status(Integer status) {
            this.status = status;
            return this;
        }

        public Builder agtType(Integer agtType) {
            this.agtType = agtType;
            return this;
        }

        public Builder agtChargeRate(String agtChargeRate) {
            this.agtChargeRate = agtChargeRate;
            return this;
        }

        public Builder agtChargePercent(String agtChargePercent) {
            this.agtChargePercent = agtChargePercent;
            return this;
        }

        public Builder contractSt(Date contractSt) {
            this.contractSt = contractSt;
            return this;
        }

        public Builder contractEd(Date contractEd) {
            this.contractEd = contractEd;
            return this;
        }

        public Builder createTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder updateTime(Date updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder aimMapList(List<AgentAimMap> aimMapList, Store s) {
            this.aimMapList = aimMapList;
            this.aimMapList.stream().map(i ->
            {
                i.setStoreId(this.storeId);
                i.setAgtRate(setChargeRate(i.getAgtPercent(),s.chargeRate()));
                return i;
            })
                .collect(Collectors.toList());


            return this;
        }

        private String setChargeRate(String agtPercent, double storeRate) {
            Double value = Double.valueOf(agtPercent);
            value = value / 100 * storeRate / 100;//所占百分比乘店铺手续费百分比
            return String.valueOf(value);
        }

        public AgentAimMap build() {
            AgentAimMap agentAimMap = new AgentAimMap();
            agentAimMap.id = this.id;
            agentAimMap.agtId = this.agtId;
            agentAimMap.storeId = this.storeId;
            agentAimMap.openId = this.openId;
            agentAimMap.status = this.status;
            agentAimMap.agtType = this.agtType;
            agentAimMap.agtRate = this.agtChargeRate;
            agentAimMap.agtPercent = this.agtChargePercent;
            agentAimMap.contractSt = this.contractSt;
            agentAimMap.contractEd = this.contractEd;
            agentAimMap.createTime = this.createTime;
            agentAimMap.updateTime = this.updateTime;
            agentAimMap.aimMapList = this.aimMapList;
            return agentAimMap;
        }
    }
}

