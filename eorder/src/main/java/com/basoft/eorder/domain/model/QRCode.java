package com.basoft.eorder.domain.model;


public class QRCode {

    private String qrcodeId;		//qrcode pk
    private String actionName;		//QR_SCENE:临时的整型参数值，QR_STR_SCENE:临时的字符串参数值，QR_LIMIT_SCENE:永久的整型参数值，QR_LIMIT_STR_SCENE:永久的字符串参数值;QR_SCENE:임시용 int형 파라미터，QR_STR_SCENE:임시용 varchar형 파라미터，QR_LIMIT_SCENE:int형 파라미터，QR_LIMIT_STR_SCENE:varchar형 파라미터;
    private int expireSeconds;		//不能超过2592000 30天;최대 2592000 30일
    private String expireDts;		//有效时间;유효시간
    private int sceneId;		//临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）;임시용 QR코드시 0이 아닌 32자라int형값, 영구용 QR코드시 최대치는 100000(1--100000 적용가능)
    private String sceneStr;		//字符串类型，长度限制为1到64;varchar형, 길이는 1-64
    private String qrcodeTicket;		//二维码ticket;QR코드 ticket
    private String qrcodeUrl;		//二维码Url;QR 코드 Url
    private long storeId;

    public String getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(String qrcodeId) {
        this.qrcodeId = qrcodeId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getExpireDts() {
        return expireDts;
    }

    public void setExpireDts(String expireDts) {
        this.expireDts = expireDts;
    }

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneStr() {
        return sceneStr;
    }

    public void setSceneStr(String sceneStr) {
        this.sceneStr = sceneStr;
    }

    public String getQrcodeTicket() {
        return qrcodeTicket;
    }

    public void setQrcodeTicket(String qrcodeTicket) {
        this.qrcodeTicket = qrcodeTicket;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public final static class Builder {
        private String qrcodeId;		//qrcode pk
        private String actionName;		//QR_SCENE:临时的整型参数值，QR_STR_SCENE:临时的字符串参数值，QR_LIMIT_SCENE:永久的整型参数值，QR_LIMIT_STR_SCENE:永久的字符串参数值;QR_SCENE:임시용 int형 파라미터，QR_STR_SCENE:임시용 varchar형 파라미터，QR_LIMIT_SCENE:int형 파라미터，QR_LIMIT_STR_SCENE:varchar형 파라미터;
        private int expireSeconds;		//不能超过2592000 30天;최대 2592000 30일
        private String expireDts;		//有效时间;유효시간
        private int sceneId;		//临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）;임시용 QR코드시 0이 아닌 32자라int형값, 영구용 QR코드시 최대치는 100000(1--100000 적용가능)
        private String sceneStr;		//字符串类型，长度限制为1到64;varchar형, 길이는 1-64
        private String qrcodeTicket;		//二维码ticket;QR코드 ticket
        private String qrcodeUrl;		//二维码Url;QR 코드 Url
        private long storeId;

        public Builder qrcodeId(String qrcodeId) {
            this.qrcodeId = qrcodeId;
            return this;
        }
        public Builder actionName(String actionName) {
            this.actionName = actionName;
            return this;
        }
        public Builder expireSeconds(int expireSeconds) {
            this.expireSeconds = expireSeconds;
            return this;
        }
        public Builder expireDts(String expireDts) {
            this.expireDts = expireDts;
            return this;
        }
        public Builder sceneId(int sceneId) {
            this.sceneId = sceneId;
            return this;
        }
        public Builder sceneStr(String sceneStr) {
            this.sceneStr = sceneStr;
            return this;
        }
        public Builder qrcodeTicket(String qrcodeTicket) {
            this.qrcodeTicket = qrcodeTicket;
            return this;
        }
        public Builder qrcodeUrl(String qrcodeUrl) {
            this.qrcodeUrl = qrcodeUrl;
            return this;
        }
        public Builder storeId(long storeId) {
            this.storeId = storeId;
            return this;
        }

        public QRCode build() {
            QRCode code = new QRCode();

            code.qrcodeId = this.qrcodeId;
            code.actionName = this.actionName;
            code.expireSeconds = this.expireSeconds;
            code.expireDts = this.expireDts;
            code.sceneId = this.sceneId;
            code.sceneStr = this.sceneStr;
            code.qrcodeTicket = this.qrcodeTicket;
            code.qrcodeUrl = this.qrcodeUrl;
            code.storeId = this.storeId;

            return code;
        }

    }
}
