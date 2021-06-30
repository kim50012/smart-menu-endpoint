package com.basoft.eorder.application;

import java.util.Objects;

public class QRCodeGenerateRequest implements java.io.Serializable{


    private static final long serialVersionUID = -3774474666837525272L;

    private String sid; // context id
    private String content;

    public QRCodeGenerateRequest(String s, String s1) {

        Objects.requireNonNull(s,"Context id required");
        Objects.requireNonNull(s1,"QRCode contents required");

        this.sid = s;
        this.content = s1;
    }

    public String getSid() {
        return sid;
    }


    public String getContent() {
        return content;
    }

}
