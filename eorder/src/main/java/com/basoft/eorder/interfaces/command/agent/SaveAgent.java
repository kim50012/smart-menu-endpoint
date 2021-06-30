package com.basoft.eorder.interfaces.command.agent;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.agent.Agent;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Date;

public class SaveAgent implements Command {

    public static final String SaveAgent = "saveAgent";
    public static final String UpAgentStatus = "upAgentStatus";
    public static final String RenewAgt = "renewAgt";

    //columns START
    private Long agtId;  //agtId  db_column: AGT_ID

    private Integer agtType;  //代理商类型：1-SA   2-CA  db_column: AGT_TYPE

    private String agtName;  //代理商名称  db_column: AGT_NAME

    private String agtCode;  //编码  db_column: AGT_CODE

    private String agtAccount;  //账号  db_column: AGT_ACCOUNT

    private Double caChargeRate;  //手续费百分比-CA才有用  db_column: CA_CHARGE_RATE

    private String agtPassword;  //密码  db_column: AGT_PASSWORD

    private String agtMobile;  //手机号  db_column: AGT_MOBLE

    private String agtEmail;  //邮件  db_column: AGT_EMAIl

    private String agtBankCode;  //银行账号  db_column: AGT_BANK_CODE

    private String agtQrcodeUrl;  //代理商二维码地址  db_column: AGT_QRCODE_URL

    private String contractSt;  //合同起始时间

    private String contractEd;  //合同结束时间

    private Integer status;  //状态：1-正常   2-禁用   3-删除  db_column: STATUS

    private Date createTime;  //创建时间  db_column: CREATE_TIME

    private String createUser;  //创建人  db_column: CREATE_USER

    private Date updateTime;  //修改时间  db_column: UPDATE_TIME

    private String modifiedUserId;  //修改人  db_column: MODIFIED_USER_ID

    //columns END

    public Long getAgtId() {
        return agtId;
    }

    public void setAgtId(Long agtId) {
        this.agtId = agtId;
    }

    public Integer getAgtType() {
        return agtType;
    }

    public void setAgtType(Integer agtType) {
        this.agtType = agtType;
    }

    public String getAgtName() {
        return agtName;
    }

    public void setAgtName(String agtName) {
        this.agtName = agtName;
    }

    public String getAgtCode() {
        return agtCode;
    }

    public void setAgtCode(String agtCode) {
        this.agtCode = agtCode;
    }

    public String getAgtAccount() {
        return agtAccount;
    }

    public void setAgtAccount(String agtAccount) {
        this.agtAccount = agtAccount;
    }

    public Double getCaChargeRate() {
        return caChargeRate;
    }

    public void setCaChargeRate(Double caChargeRate) {
        this.caChargeRate = caChargeRate;
    }

    public String getAgtPassword() {
        return agtPassword;
    }

    public void setAgtPassword(String agtPassword) {
        this.agtPassword = agtPassword;
    }

    public String getAgtMobile() {
        return agtMobile;
    }

    public void setAgtMobile(String agtMobile) {
        this.agtMobile = agtMobile;
    }

    public String getAgtEmail() {
        return agtEmail;
    }

    public void setAgtEmail(String agtEmail) {
        this.agtEmail = agtEmail;
    }

    public String getAgtBankCode() {
        return agtBankCode;
    }

    public void setAgtBankCode(String agtBankCode) {
        this.agtBankCode = agtBankCode;
    }

    public String getAgtQrcodeUrl() {
        return agtQrcodeUrl;
    }

    public void setAgtQrcodeUrl(String agtQrcodeUrl) {
        this.agtQrcodeUrl = agtQrcodeUrl;
    }

    public String getContractSt() {
        return contractSt;
    }

    public void setContractSt(String contractSt) {
        this.contractSt = contractSt;
    }

    public String getContractEd() {
        return contractEd;
    }

    public void setContractEd(String contractEd) {
        this.contractEd = contractEd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getModifiedUserId() {
        return modifiedUserId;
    }

    public void setModifiedUserId(String modifiedUserId) {
        this.modifiedUserId = modifiedUserId;
    }

    public Agent buildBase(Long agtId,String agtCode){
        return new Agent.Builder()
                .agtId(agtId)
                .agtType(agtType)
                .agtName(agtName)
                .agtCode(agtCode)
                .agtAccount(agtAccount)
                .caChargeRate(caChargeRate)
                .agtPassword(agtPassword)
                .agtMobile(agtMobile)
                .agtEmail(agtEmail)
                .agtBankCode(agtBankCode)
                .agtQrcodeUrl(agtQrcodeUrl)
                .contractSt(contractSt)
                .contractEd(contractEd)
                .status(status)
                .createTime(createTime)
                .createUser(createUser)
                .updateTime(updateTime)
                .modifiedUserId(modifiedUserId)
                .build();
    }

    public Agent build(Long agtId, String agtCode) {
        if (StringUtils.isNotBlank(agtPassword)) {
            String password = BCrypt.hashpw(this.getAgtPassword(), BCrypt.gensalt());
            this.setAgtPassword(password);
        }
        return  buildBase(agtId,agtCode);
    }

}

