package com.basoft.eorder.domain.model.agent;

public class QRCodeAgent {
    //qrcode pk
    private String agentId;

    //QR_SCENE:临时的整型参数值，QR_STR_SCENE:临时的字符串参数值，QR_LIMIT_SCENE:永久的整型参数值，QR_LIMIT_STR_SCENE:永久的字符串参数值;QR_SCENE:임시용 int형 파라미터，QR_STR_SCENE:임시용 varchar형 파라미터，QR_LIMIT_SCENE:int형 파라미터，QR_LIMIT_STR_SCENE:varchar형 파라미터;
    private String actionName;

    //不能超过2592000 30天;최대 2592000 30일
    private int expireSeconds;

    //有效时间;유효시간
    private String expireDts;

    //临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）;임시용 QR코드시 0이 아닌 32자라int형값, 영구용 QR코드시 최대치는 100000(1--100000 적용가능)
    private int sceneId;

    //字符串类型，长度限制为1到64;varchar형, 길이는 1-64
    private String sceneStr;

    //二维码ticket;QR코드 ticket
    private String qrcodeTicket;

    //二维码Url;QR 코드 Url
    private String qrcodeUrl;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
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

    public final static class Builder {
        private String agentId;
        private String actionName;
        private int expireSeconds;
        private String expireDts;
        private int sceneId;
        private String sceneStr;
        private String qrcodeTicket;
        private String qrcodeUrl;

        public Builder agentId(String agentId) {
            this.agentId = agentId;
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

        public QRCodeAgent build() {
            QRCodeAgent code = new QRCodeAgent();
            code.agentId = this.agentId;
            code.actionName = this.actionName;
            code.expireSeconds = this.expireSeconds;
            code.expireDts = this.expireDts;
            code.sceneId = this.sceneId;
            code.sceneStr = this.sceneStr;
            code.qrcodeTicket = this.qrcodeTicket;
            code.qrcodeUrl = this.qrcodeUrl;
            return code;
        }
    }

    @Override
    public String toString() {
        return "QRCodeAgent{" +
                "agentId='" + agentId + '\'' +
                ", actionName='" + actionName + '\'' +
                ", expireSeconds=" + expireSeconds +
                ", expireDts='" + expireDts + '\'' +
                ", sceneId=" + sceneId +
                ", sceneStr='" + sceneStr + '\'' +
                ", qrcodeTicket='" + qrcodeTicket + '\'' +
                ", qrcodeUrl='" + qrcodeUrl + '\'' +
                '}';
    }
}
