package com.basoft.eorder.domain.model;

public class ContactInfo {

    private String city;
    private String email;
    private  String mobile;
    private String logoUrl;
    private String detailAddr;
    private String detailAddrChn;


    public ContactInfo(String mobile, String email, String city,String logoUrl) {
        this.city = city;
        this.email = email;
        this.mobile= mobile;
        this.logoUrl=logoUrl;
    }

    public ContactInfo(String mobile, String email, String city, String detailAddr2,String detailAddrChn,String logoUrl) {
        this(mobile,email,city,logoUrl);
        this.detailAddr = detailAddr2;
        this.detailAddrChn = detailAddrChn;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public String getDetailAddrChn() {
        return detailAddrChn;
    }

}
