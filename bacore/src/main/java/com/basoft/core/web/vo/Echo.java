package com.basoft.core.web.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

public class Echo<T> {
    private T biz_data;
    private int status = 0;
    private String message = "";
    private String messageKey = "";
    private StackTraceElement[] stacktrace;


    public Echo() {
        this.biz_data = null;
        this.status = 0;
    }

    public Echo(T biz_data) {
        this.biz_data = biz_data;
        this.status = 0;
    }

    public Echo(T biz_data, int status) {
        this.biz_data = biz_data;
        this.status = status;
    }

    public Echo(T biz_data, int status, String message) {
        this.biz_data = biz_data;
        this.status = status;
        this.message = message;
    }

    public Echo(T biz_data, int status, String message, StackTraceElement[] stacktrace) {
        this.biz_data = biz_data;
        this.status = status;
        this.message = message;
        this.stacktrace = stacktrace;
    }

    public T getBiz_data() {
        return this.biz_data;
    }

    public void setBiz_data(T biz_data) {
        this.biz_data = biz_data;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public StackTraceElement[] getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(StackTraceElement[] stacktrace) {
        this.stacktrace = stacktrace;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }
}
