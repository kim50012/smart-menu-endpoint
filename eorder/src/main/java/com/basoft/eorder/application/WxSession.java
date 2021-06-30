package com.basoft.eorder.application;

import java.io.Serializable;

public class WxSession implements Serializable {
    private static final long serialVersionUID = -3879535958589683446L;
    private String token;
    private String openId;
    // 对应store_table表中的table_no字段
    private int sceneId;
    private Long storeId;
    private int storeType;
    // qrcode_id。对应store_table表中的qrcode_id字段
    private String sceneStr;
    private String nickname;
    private String headimgurl;
    private String city;
    private String country;
    private String province;

    // 是否下单即支付商店 1-是 0-不是
    private String isPayStore;

    // 业务类型 1-点餐 2-网购物 3-医美 4-酒店
    private String sessionType;

    // session产生的渠道：scan-扫码点餐 outer-店外点餐
    private String sessionChannel;

    public String getToken() {
        return token;
    }

    public Long getStoreId() {
        return storeId;
    }

    public int getStoreType(){ return storeType;}

    public String getOpenId() {
        return openId;
    }

    public int getSceneId() {
        return sceneId;
    }

    public String getSceneStr() {
        return sceneStr;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public String getIsPayStore() {
        return isPayStore;
    }

    public void setIsPayStore(String isPayStore) {
        this.isPayStore = isPayStore;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getSessionChannel() {
        return sessionChannel;
    }

    public void setSessionChannel(String sessionChannel) {
        this.sessionChannel = sessionChannel;
    }

    public final static class Builder {
        private String token;
        private String openId;
        private int sceneId;
        private Long storeId;
        private int storeType;
        private String sceneStr;
        private String nickname;
        private String headimgurl;
        private String city;
        private String province;
        private String country;

        // 是否下单即支付商店 1-是 0-不是
        private String isPayStore;

        // 业务类型 1-点餐 2-网购物 3-医美 4-酒店
        private String sessionType;

        // session产生的渠道：scan-扫码点餐 outer-店外点餐
        private String sessionChannel;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder openId(String openId) {
            this.openId = openId;
            return this;
        }

        public Builder sceneId(int sceneId) {
            this.sceneId = sceneId;
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

        public Builder sceneStr(String sceneStr) {
            this.sceneStr = sceneStr;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder headimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder province(String province) {
            this.province = province;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder isPayStore(String isPayStore) {
            this.isPayStore = isPayStore;
            return this;
        }

        public Builder sessionType(String sessionType) {
            this.sessionType = sessionType;
            return this;
        }

        public Builder sessionChannel(String sessionChannel) {
            this.sessionChannel = sessionChannel;
            return this;
        }

        public WxSession build() {
            WxSession wx = new WxSession();
            wx.token = this.token;
            wx.openId = this.openId;
            wx.sceneId = this.sceneId;
            wx.storeId = this.storeId;
            wx.storeType = this.storeType;
            wx.sceneStr = this.sceneStr;
            wx.nickname = this.nickname;
            wx.headimgurl = this.headimgurl;
            wx.city = this.city;
            wx.province = this.province;
            wx.country = this.country;
            wx.isPayStore = this.isPayStore;
            wx.sessionType = this.sessionType;
            wx.sessionChannel = this.sessionChannel;
            return wx;
        }
    }
}