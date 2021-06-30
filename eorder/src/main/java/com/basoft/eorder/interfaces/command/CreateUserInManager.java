package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;

public class CreateUserInManager implements Command {
    public static final String NAME = "saveUserInManager";

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private String account;
    private String password;
    private String mobile;
    private String email;
    private String newPassword;
    private int accountType;
    private int bizType;
    private int accountRole;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public int getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(int accountRole) {
        this.accountRole = accountRole;
    }

    private User build(Long newId, Long storeId) {
        validate(newId);
        return new User.Builder()
                .id(newId)
                .email(this.email)
                .account(this.account)
                .name(this.name)
                .mobile(this.mobile)
                .password(this.password)
                .status(User.Status.NORMAL)
                .accountType(this.accountType)
                .bizType(this.bizType)
                .accountRole(this.accountRole)
                .storeId(storeId)
                .build();
    }

    private void validate(Long newId) {
        if (newId == null) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        if (StringUtils.isEmpty(this.account)) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        if (StringUtils.isEmpty(mobile)) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        if (StringUtils.isEmpty(this.password)) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        if (this.accountType == 0) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        if (this.bizType == 0) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        if (this.accountRole == 0) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }
    }

    public User buildForNew(Long newId, Long storeId) {
        if (this.getPassword() != null && this.getPassword().length() > 0) {
            String resPasswd = BCrypt.hashpw(this.getPassword(), BCrypt.gensalt());
            this.setPassword(resPasswd);
        }
        return build(newId, storeId);
    }

    /**
     * 更新时用此函数生成，主要处理 newPassword 的部分
     *
     * @param newId
     * @return
     */
    public User buildForUpdate(Long newId, Long storeId) {
        //更新操作时将会有 newPassword 字段，如果此字段不是为空的话，将会加密之后替换到 password字段上去
        if (this.getNewPassword() != null && this.getNewPassword().length() > 0) {
            String resPasswd = BCrypt.hashpw(this.getNewPassword(), BCrypt.gensalt());
            this.setPassword(resPasswd);
        }
        return build(newId, storeId);
    }
}