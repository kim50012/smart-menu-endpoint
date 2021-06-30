package com.basoft.eorder.application;

public class UserSession implements java.io.Serializable {
    private static final long serialVersionUID = -3819311916353952687L;
    private String token;
    private Long userId;
    private Long storeId;
    private int storeType;
    private String name;
    private String account;
    private String mobile;
    private String email;
    private String password;
    // 用户的账户类型 1:门店超级管理员或称为店主/店长 2:门店的业务操作员
    private int accountType;
    // 用户的业务类型 1:餐厅 2:医院 3:购物 4:酒店
    private int bizType;
    // 用户的角色。根据实际需要进行赋值，1-超级管理员的默认值 2-门店业务操作员的默认值 2X-根据实际需要进行赋值
    private int accountRole;
    // 代理商ID
    private Long agentId;

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
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

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public static final class Builder {
        private String token;
        private Long userId;
        private Long storeId;
        private int storeType;
        private String name;
        private String account;
        private String password;
        private String mobile;
        private String email;
        private int accountType;
        private int bizType;
        private int accountRole;
        private Long agentId;

        public Builder token(String to) {
            this.token = to;
            return this;
        }

        public Builder id(Long id) {
            this.userId = id;
            return this;
        }

        public Builder storeId(Long storeId) {
            this.storeId = storeId;
            return this;
        }

        public Builder name(String name2) {
            this.name = name2;
            return this;
        }

        public Builder account(String account) {
            this.account = account;
            return this;
        }

        public Builder password(String pass) {
            this.password = pass;
            return this;
        }

        public Builder mobile(String mb) {
            this.mobile = mb;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
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

        public Builder agentId(Long agentId) {
            this.agentId = agentId;
            return this;
        }

        public UserSession build() {
            UserSession session = new UserSession();
            session.userId = this.userId;
            session.storeId = this.storeId;
            session.name = this.name;
            session.account = this.account;
            session.password = this.password;
            session.email = this.email;
            session.mobile = this.mobile;
            session.token = this.token;
            session.storeType = this.storeType;
            session.accountType = this.accountType;
            session.bizType = this.bizType;
            session.accountRole = this.accountRole;
            session.agentId = this.agentId;
            return session;
        }
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "token='" + token + '\'' +
                ", userId=" + userId +
                ", storeId=" + storeId +
                ", storeType=" + storeType +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", accountType=" + accountType +
                ", bizType=" + bizType +
                ", accountRole=" + accountRole +
                ", agentId=" + agentId +
                '}';
    }
}
