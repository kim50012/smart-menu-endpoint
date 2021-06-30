package com.basoft.eorder.application.wx.http;

import com.basoft.eorder.application.wx.model.wxuser.WeixinReturn;

public class WeixinErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer errcode;

    private String errmsg;

    public WeixinErrorException(Integer errcode, String errmsg) {
        super(errmsg);
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public WeixinErrorException(WeixinReturn weixinReturn) {
        super();
        this.errcode = weixinReturn.getErrcode();
        this.errmsg = weixinReturn.getErrmsg();
    }

    public WeixinErrorException(String errmsg) {
        super();
        this.errcode = null;
        this.errmsg = errmsg;
    }

    @Override
    public String getMessage() {
        String message = "";
        if (errcode != null) {
            String desc = WechatErrorCode.getDesc(errcode);
            message = "微信接口调用失败!\n" + desc + "\n[" + errcode + "]" + errmsg;
        } else {
            message = "微信接口调用失败!\n" + errmsg;
        }
        return message;
    }
}