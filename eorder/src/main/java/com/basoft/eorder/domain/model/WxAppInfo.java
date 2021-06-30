package com.basoft.eorder.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

/**
 * @Author:DongXifu
 * @Description: 店铺
 * @Date Created in 下午3:03 2018/12/4
 **/
public class WxAppInfo {

    @JsonSerialize
    private String sysId;
    private Long shopId;

    private String originalAppId;
    private String compNm;
    private String appId;
    private String appSecret;
    private String url;
    private String token;
    private String encordingAesKey;

    public String sysId() {
    	return sysId;
    }
    public Long shopId() {
        return shopId;
    }
    
    public String originalAppId() {
    	return originalAppId;
    }
    public String compNm() {
    	return compNm;
    }
    public String appId() {
    	return appId;
    }
    public String appSecret() {
    	return appSecret;
    }
    public String url() {
    	return url;
    }
    public String token() {
    	return token;
    }
    public String encordingAesKey() {
    	return encordingAesKey;
    }

    public static final class Builder{

        private String sysId;
        private Long shopId;

        private String originalAppId;
        private String compNm;
        private String appId;
        private String appSecret;
        private String url;
        private String token;
        private String encordingAesKey;


        public Builder sysId(String sysId){
            this.sysId = sysId;
            return this;
        }
        public Builder shopId(Long shopId){
            this.shopId = shopId;
            return this;
        }
        public Builder originalAppId(String originalAppId){
            this.originalAppId = originalAppId;
            return this;
        }
        public Builder compNm(String compNm){
            this.compNm = compNm;
            return this;
        }
        public Builder appId(String appId){
            this.appId = appId;
            return this;
        }
        public Builder appSecret(String appSecret){
            this.appSecret = appSecret;
            return this;
        }
        public Builder url(String url){
            this.url = url;
            return this;
        }
        public Builder token(String token){
            this.token = token;
            return this;
        }
        public Builder encordingAesKey(String encordingAesKey){
            this.encordingAesKey = encordingAesKey;
            return this;
        }


        public WxAppInfo build(){
            WxAppInfo wxAppInfo = new WxAppInfo();
            wxAppInfo.sysId = this.sysId;
            wxAppInfo.shopId = this.shopId;
            wxAppInfo.originalAppId = this.originalAppId;
            wxAppInfo.compNm = this.compNm;
            wxAppInfo.appId = this.appId;
            wxAppInfo.appSecret = this.appSecret;
            wxAppInfo.url = this.url;
            wxAppInfo.token = this.token;
            wxAppInfo.encordingAesKey = this.encordingAesKey;
            return wxAppInfo;
        }

    }

}
