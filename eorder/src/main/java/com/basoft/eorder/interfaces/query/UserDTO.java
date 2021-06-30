package com.basoft.eorder.interfaces.query;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @ProjectName: eorder
 * @Package: com.basoft.eorder.interfaces.query
 * @ClassName: UserDTO
 * @Description: TODO
 * @Author: liminzhe
 * @Date: 2018-12-11 15:36
 * @Version: 1.0
 */

public class UserDTO {

    private Long id;
    private String name;
    private String account;
    private String password;
    private Long storeId;
    private String storeName;
    private String mobile;
    private String created;
    private String email;
    private int status;

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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
