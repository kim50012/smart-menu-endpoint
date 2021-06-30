package com.basoft.eorder.application.wx.model;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class WxPayCloseResp extends WxResp {
    private String result_msg;          //业务结果描述

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
