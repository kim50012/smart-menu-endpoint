package com.basoft.eorder.domain.model.agent;

import java.util.Date;

public class Agent {
    public static final String SA = "SA";
    public static final String CA = "CA";
    public static final int SA_TP = 1;
    public static final int CA_TP = 2;
    //columns START
    private Long agtId;  //agtId

    private Integer agtType;  //代理商类型：1-SA   2-CA

    private String agtName;  //代理商名称

    private String agtCode;  //编码

    private String agtAccount;  //账号

    private Double caChargeRate;  //手续费百分比-CA才有用

    private String agtPassword;  //密码

    private String agtMobile;  //手机号

    private String agtEmail;  //邮件

    private String agtBankCode;  //银行账号

    private String agtQrcodeUrl;  //代理商二维码地址

    private String agtTicket;

    private String agtQrcodeId;

    private Integer status;  //状态：1-正常   2-禁用   3-删除

    private String contractSt;  //合同起始时间

    private String contractEd;  //合同结束时间

    private Date createTime;  //创建时间

    private String createUser;  //创建人

    private Date updateTime;  //修改时间

    private String modifiedUserId;  //修改人
    //columns END

    public Long getAgtId() {
        return agtId;
    }

    public Integer getAgtType() {
        return agtType;
    }

    public String getAgtName() {
        return agtName;
    }

    public String getAgtCode() {
        return agtCode;
    }

    public String getAgtAccount() {
        return agtAccount;
    }

    public Double getCaChargeRate() {
        return caChargeRate;
    }

    public String getAgtPassword() {
        return agtPassword;
    }

    public String getAgtMobile() {
        return agtMobile;
    }

    public String getAgtEmail() {
        return agtEmail;
    }

    public String getAgtBankCode() {
        return agtBankCode;
    }

    public String getAgtQrcodeUrl() {
        return agtQrcodeUrl;
    }

    public String getAgtTicket() {
        return agtTicket;
    }

    public String getAgtQrcodeId() {
        return agtQrcodeId;
    }

    public Integer getStatus() {
        return status;
    }

    public String getContractSt() {
        return contractSt;
    }

    public String getContractEd() {
        return contractEd;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public String getModifiedUserId() {
        return modifiedUserId;
    }

    public void setAgtId(Long agtId) {
        this.agtId = agtId;
    }

    public void setAgtType(Integer agtType) {
        this.agtType = agtType;
    }

    public void setAgtName(String agtName) {
        this.agtName = agtName;
    }

    public void setAgtCode(String agtCode) {
        this.agtCode = agtCode;
    }

    public void setAgtAccount(String agtAccount) {
        this.agtAccount = agtAccount;
    }

    public void setCaChargeRate(Double caChargeRate) {
        this.caChargeRate = caChargeRate;
    }

    public void setAgtPassword(String agtPassword) {
        this.agtPassword = agtPassword;
    }

    public void setAgtMobile(String agtMobile) {
        this.agtMobile = agtMobile;
    }

    public void setAgtEmail(String agtEmail) {
        this.agtEmail = agtEmail;
    }

    public void setAgtBankCode(String agtBankCode) {
        this.agtBankCode = agtBankCode;
    }

    public void setAgtQrcodeUrl(String agtQrcodeUrl) {
        this.agtQrcodeUrl = agtQrcodeUrl;
    }

    public void setAgtTicket(String agtTicket) {
        this.agtTicket = agtTicket;
    }

    public void setAgtQrcodeId(String agtQrcodeId) {
        this.agtQrcodeId = agtQrcodeId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setContractSt(String contractSt) {
        this.contractSt = contractSt;
    }

    public void setContractEd(String contractEd) {
        this.contractEd = contractEd;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setModifiedUserId(String modifiedUserId) {
        this.modifiedUserId = modifiedUserId;
    }

    public static final class Builder {
        //columns START
        private Long agtId;  //agtId

        private Integer agtType;  //代理商类型：1-SA   2-CA

        private String agtName;  //代理商名称

        private String agtCode;  //编码

        private String agtAccount;  //账号

        private Double caChargeRate;  //手续费百分比-CA才有用

        private String agtPassword;  //密码

        private String agtMoble;  //手机号

        private String agtEmail;  //邮件

        private String agtBankCode;  //银行账号

        private String agtQrcodeUrl;  //代理商二维码地址

        private String agtTicket;

        private String agtQrcodeId;

        private String contractSt;  //合同起始时间

        private String contractEd;  //合同结束时间

        private Integer status;  //状态：1-正常   2-禁用   3-删除

        private Date createTime;  //创建时间

        private String createUser;  //创建人

        private Date updateTime;  //修改时间

        private String modifiedUserId;  //修改人

        //columns END
        public Builder agtId(Long agtId) {
            this.agtId = agtId;
            return this;
        }

        public Builder agtType(Integer agtType) {
            this.agtType = agtType;
            return this;
        }

        public Builder agtName(String agtName) {
            this.agtName = agtName;
            return this;
        }

        public Builder agtCode(String agtCode) {
            this.agtCode = agtCode;
            return this;
        }

        public Builder agtAccount(String agtAccount) {
            this.agtAccount = agtAccount;
            return this;
        }

        public Builder caChargeRate(Double caChargeRate) {
            this.caChargeRate = caChargeRate;
            return this;
        }

        public Builder agtPassword(String agtPassword) {
            this.agtPassword = agtPassword;
            return this;
        }

        public Builder agtMobile(String agtMoble) {
            this.agtMoble = agtMoble;
            return this;
        }

        public Builder agtEmail(String agtEmail) {
            this.agtEmail = agtEmail;
            return this;
        }

        public Builder agtBankCode(String agtBankCode) {
            this.agtBankCode = agtBankCode;
            return this;
        }

        public Builder agtQrcodeUrl(String agtQrcodeUrl) {
            this.agtQrcodeUrl = agtQrcodeUrl;
            return this;
        }

        public Builder agtTicket(String agtTicket) {
            this.agtTicket = agtTicket;
            return this;
        }

        public Builder agtQrcodeId(String agtQrcodeId) {
            this.agtQrcodeId = agtQrcodeId;
            return this;
        }


        public Builder contractSt(String contractSt) {
            this.contractSt = contractSt;
            return this;
        }

        public Builder contractEd(String contractEd) {
            this.contractEd = contractEd;
            return this;
        }

        public Builder status(Integer status) {
            this.status = status;
            return this;
        }

        public Builder createTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder createUser(String createUser) {
            this.createUser = createUser;
            return this;
        }

        public Builder updateTime(Date updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder modifiedUserId(String modifiedUserId) {
            this.modifiedUserId = modifiedUserId;
            return this;
        }

        public Agent build() {
            Agent agent = new Agent();
            agent.agtId = this.agtId;
            agent.agtType = this.agtType;
            agent.agtName = this.agtName;
            agent.agtCode = this.agtCode;
            agent.agtAccount = this.agtAccount;
            agent.caChargeRate = this.caChargeRate;
            agent.agtPassword = this.agtPassword;
            agent.agtMobile = this.agtMoble;
            agent.agtEmail = this.agtEmail;
            agent.agtBankCode = this.agtBankCode;
            agent.agtQrcodeUrl = this.agtQrcodeUrl;
            agent.agtTicket = this.agtTicket;
            agent.agtQrcodeId = this.agtQrcodeId;
            agent.contractSt = this.contractSt;
            agent.contractEd = this.contractEd;
            agent.status = this.status;
            agent.createTime = this.createTime;
            agent.createUser = this.createUser;
            agent.updateTime = this.updateTime;
            agent.modifiedUserId = this.modifiedUserId;
            return agent;
        }
    }

    @Override
    public String toString() {
        return "Agent{" +
                "agtId=" + agtId +
                ", agtType=" + agtType +
                ", agtName='" + agtName + '\'' +
                ", agtCode='" + agtCode + '\'' +
                ", agtAccount='" + agtAccount + '\'' +
                ", caChargeRate=" + caChargeRate +
                ", agtPassword='" + agtPassword + '\'' +
                ", agtMobile='" + agtMobile + '\'' +
                ", agtEmail='" + agtEmail + '\'' +
                ", agtBankCode='" + agtBankCode + '\'' +
                ", agtQrcodeUrl='" + agtQrcodeUrl + '\'' +
                ", agtTicket='" + agtTicket + '\'' +
                ", agtQrcodeId='" + agtQrcodeId + '\'' +
                ", status=" + status +
                ", contractSt=" + contractSt +
                ", contractEd=" + contractEd +
                ", createTime=" + createTime +
                ", createUser='" + createUser + '\'' +
                ", updateTime=" + updateTime +
                ", modifiedUserId='" + modifiedUserId + '\'' +
                '}';
    }

    public static void main(String[] args) {
        String s = "2019-09-15 23:59:59";
        String s1 = "2019-09-30";
        String s4 = "2019-10-01";

        String s2 = "2019-09-14";
        String s3 = "2019-09-10";

        String s5 = "2019-09-15";
        System.out.println(s.substring(0,10));
        System.out.println(s.substring(0,10).compareTo(s1));// -2 前小于后
        System.out.println(s.substring(0,10).compareTo(s2));//1 前大于后
        System.out.println(s.substring(0,10).compareTo(s3));//4 前大于后
        System.out.println(s.substring(0,10).compareTo(s4));//-1 前小于后
        System.out.println(s.substring(0,10).compareTo(s5));//0 前等于后
    }
}

