package com.basoft.eorder.domain.model;

import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.model.agent.Agent;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Description: 店铺管理者
 *
 * @author woonill
 * @author DongXifu
 * @Date Created in 下午3:20 2018/12/4
 */
public class User implements Serializable {
    public enum Status {
        NORMAL(0), OPEN(1), STOP(2), DELETE(3);

        private final int code;

        private Status(int code) {
            this.code = code;
        }

        public int code() {
            return code;
        }

        public static Status get(int code) {
            return
                    Arrays.asList(Status.values())
                            .stream()
                            .filter(new Predicate<Status>() {
                                @Override
                                public boolean test(Status status) {
                                    return status.code == code;
                                }
                            })
                            .findFirst()
                            .orElseGet(new Supplier<Status>() {
                                @Override
                                public Status get() {
                                    return null;
                                }
                            });
        }
    }

    private Long id;
    private String name;
    private String account;
    private String password;
    private String mobile;
    private String email;
    private Status status = Status.NORMAL;
    private Long storeId;
    private int storeType;
    // 用户的账户类型 1:门店超级管理员或称为店主/店长 2:门店的业务操作员
    private int accountType;
    // 用户的业务类型 1:餐厅 2:医院 3:购物 4:酒店
    private int bizType;
    // 用户的角色。根据实际需要进行赋值，1-超级管理员的默认值 2-门店业务操作员的默认值 2X-根据实际需要进行赋值
    private int accountRole;

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

    public String getMobile() {
        return mobile;
    }

    public Status getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public Long getStoreId() {
        return storeId;
    }

    public int getStoreType() {
        return storeType;
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

    public static Builder newUser(User manager) {
        return new Builder()
                .id(manager.id)
                .name(manager.name)
                .status(manager.status)
                .email(manager.getEmail())
                .password(manager.getPassword())
                .mobile(manager.mobile)
                .account(manager.getAccount())
                .storeId(manager.getStoreId())
                .storeType(manager.getStoreType());
    }

    public User newUserFromAgentForInsert(Long newUserId, Agent agent) {
        // 代理商类型：1-SA   2-CA
        int fromAgtType = agent.getAgtType();
        int agtRole = 3;
        if(fromAgtType == 2){
            agtRole = 4;
        }
        return new User.Builder()
                .id(newUserId)
                .name(agent.getAgtName())
                .account(agent.getAgtCode())
                .password(agent.getAgtPassword())
                .mobile(agent.getAgtMobile())
                .email(agent.getAgtEmail())
                .status(Status.NORMAL)
                // 代理商无所属门店，填入代理商ID
                .storeId(agent.getAgtId())
                .storeType(99)
                // 用户的账户类型 1:门店超级管理员或称为店主/店长 2:门店的业务操作员 3:代理商用户
                .accountType(3)
                // 用户的业务类型 1:餐厅 2:医院 3:购物 4:酒店 5:代理商
                .bizType(5)
                /*
                 * 用户的角色,根据实际需要进行赋值。
                 * 1-门店超级管理员的默认值
                 * 2-门店业务操作员的默认值 2X-门店业务操作员根据实际需要进行赋值
                 * 3-SA代理商超级管理员 3X-SA代理商操作员根据实际进行赋值
                 * 4-CA代理商超级管理员 4X-CA代理商操作员根据实际进行赋值
                 */
                .accountRole(agtRole)
                .build();
    }

    /**
     * 构建修改代理商用户的信息。修改依据为user account属性
     *
     * @param agent
     * @return
     */
    public User newUserFromAgentForUpdate(Agent agent) {
        return new User.Builder()
                .name(agent.getAgtName())
                .account(agent.getAgtCode())
                .password(agent.getAgtPassword())
                .mobile(agent.getAgtMobile())
                .email(agent.getAgtEmail())
                // 代理商无所属门店，填入代理商ID
                .storeId(agent.getAgtId())
                .build();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String account;
        private String password;
        private String mobile;
        private String email;
        private Status status;
        private Long storeId;
        private int storeType;
        private int accountType;
        private int bizType;
        private int accountRole;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder account(String account) {
            this.account = account;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder storeId(Long storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder storeType(int storeType) {
            this.storeType = storeType;
            return this;
        }

        public Builder accountType(int accountType) {
            this.accountType = accountType;
            return this;
        }

        public Builder bizType(int bizType) {
            this.bizType = bizType;
            return this;
        }

        public Builder accountRole(int accountRole) {
            this.accountRole = accountRole;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public User build() {
            User user = new User();
            user.id = this.id;
            user.name = this.name;
            user.account = this.account;
            user.mobile = this.mobile;
            user.password = this.password;
            user.email = this.email;
            user.status = this.status;
            user.storeId = this.storeId;
            user.storeType = this.storeType;
            user.accountType = this.accountType;
            user.bizType = this.bizType;
            user.accountRole = this.accountRole;
            return user;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", storeId=" + storeId +
                ", storeType=" + storeType +
                ", accountType=" + accountType +
                ", bizType=" + bizType +
                ", accountRole=" + accountRole +
                '}';
    }
}