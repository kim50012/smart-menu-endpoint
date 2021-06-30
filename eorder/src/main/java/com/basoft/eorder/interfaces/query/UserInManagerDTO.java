package com.basoft.eorder.interfaces.query;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 门店用户管理
 */
public class UserInManagerDTO {
    private Long id;
    private String name;
    private String account;
    private String password;
    private Long storeId;
    private String mobile;
    private String created;
    private String email;
    private int status;
    private int accountType;
    private int bizType;
    private int accountRole;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public Long getStoreId() {
        return storeId;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCreated() {
        return created;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}