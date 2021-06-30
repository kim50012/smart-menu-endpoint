package com.basoft.eorder.domain.model;

import com.basoft.eorder.application.wx.api.WechatAPI;
import com.basoft.eorder.application.wx.model.QrcodeTicket;
import org.apache.commons.lang.StringUtils;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:35 2018/12/4
 **/
public class StoreTable {
    private Store store;
    private Long id;
    private int number;//桌子号码
    private String tag;
    private String qrCodeId;
    private String qrCodeImagePath;
    private int maxNumberOfSeat;
    private String ticket;
    // table_no
    private int sceneId;
    // 序号
    private int order;

    private String showQrCodeUrl;
    // 状态  0-use  3-delete
    private int status;
    // 是否静默桌子
    private int isSilent;

    public enum Status {
        NORMAL(0), OPEN(1), CLOSED(2), DELETED(3);
        private int code;

        Status(int code) {
            this.code = code;
        }

        public int code() {
            return this.code;
        }

        public static Status valueOf(Integer value) {
            for (Status e : Status.values()) {
                if (e.code() == value) {
                    return e;
                }
            }
            return null;
        }
    }

    // private Long qrCodeImg;//二维码

    /*private Byte isDelete;
    private String createdId;
    private Date createdDt;
    private String modifiedId;
    private Date modifiedDt;*/


    public String getShowQrCodeUrl() {
        if (StringUtils.isBlank(showQrCodeUrl))
            if (StringUtils.isNotBlank(ticket))
                return WechatAPI.getInstance().getShowqrcodeUrl() + ticket;
        return showQrCodeUrl;
    }

    public void setShowQrCodeUrl(String showQrCodeUrl) {
        this.showQrCodeUrl = showQrCodeUrl;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getQrCodeImagePath() {
        return qrCodeImagePath;
    }

    public void setQrCodeImagePath(String qrCodeImagePath) {
        this.qrCodeImagePath = qrCodeImagePath;
    }

    public int getMaxNumberOfSeat() {
        return maxNumberOfSeat;
    }

    public void setMaxNumberOfSeat(int maxNumberOfSeat) {
        this.maxNumberOfSeat = maxNumberOfSeat;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public final Long id() {
        return id;
    }

    public int number() {
        return this.number;
    }

    public String qrCodeImagePath() {
        return this.qrCodeImagePath;
    }


    public int order() {
        return this.order;
    }

    public Store store() {
        return this.store;
    }

    public String tag() {
        return this.tag;
    }

    public int maxNumberOfSeat() {
        return this.maxNumberOfSeat;
    }

    public String ticket() {
        return this.ticket;
    }

    public int sceneId() {
        return this.sceneId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public int getIsSilent() {
        return isSilent;
    }

    public void setIsSilent(int isSilent) {
        this.isSilent = isSilent;
    }

    public static final class Builder {
        private Long id;
        private Store store;
        private int number;//桌子号码
        private String tag;
        private String qrCodeId;
        private String qrCodeImagePath;
        private int order;
        private int maxNumberOfSeat;
        private String ticket;
        private int sceneId;
        private int status;
        private int isSilent;

        public Builder setStore(Store store) {
            this.store = store;
            return this;
        }

        public Builder setTag(String tagStr) {
            this.tag = tagStr;
            return this;
        }


        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setNumber(int number) {
            this.number = number;
            return this;
        }

        public Builder setQrCodeImagePath(String qrCodeImagePath) {
            this.qrCodeImagePath = qrCodeImagePath;
            return this;
        }

        public Builder setQrCodeId(String qrCodeId) {
            this.qrCodeId = qrCodeId;
            return this;
        }

        public Builder setOrder(int order) {
            this.order = order;
            return this;
        }

        public Builder setMaxNumberOfSeat(int maxNumberOfSeat) {
            this.maxNumberOfSeat = maxNumberOfSeat;
            return this;
        }

        public Builder setTicket(String ticket) {
            this.ticket = ticket;
            return this;
        }

        public Builder setSceneId(int sceneId) {
            this.sceneId = sceneId;
            return this;
        }

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public Builder setIsSilent(int isSilent) {
            this.isSilent = isSilent;
            return this;
        }

        public StoreTable build() {
            StoreTable table = new StoreTable();
            table.store = this.store;
            table.order = this.order;
            table.id = this.id;
            table.number = this.number;
            table.tag = this.tag;
            table.qrCodeId = this.qrCodeId;
            table.qrCodeImagePath = this.qrCodeImagePath;
            table.maxNumberOfSeat = this.maxNumberOfSeat;
            table.ticket = this.ticket;
            table.sceneId = this.sceneId;
            table.status = this.status;
            table.isSilent = this.isSilent;
            return table;
        }
    }

    public StoreTable.Builder createStoreTable(StoreTable table) {
        return new Builder()
                .setId(table.id)
                .setNumber(table.number)
                .setTag(table.tag)
                .setQrCodeId(table.qrCodeId)
                .setQrCodeImagePath(table.qrCodeImagePath)
                .setMaxNumberOfSeat(table.maxNumberOfSeat)
                .setTicket(table.ticket)
                .setSceneId(table.sceneId)
                .setStatus(table.status)
                .setIsSilent(table.isSilent);
    }

    public StoreTable.Builder createTableByTicket(StoreTable table, QrcodeTicket ticket) {
        return new Builder()
                .setId(table.id)
                .setNumber(table.number)
                .setTag(table.tag)
                .setQrCodeId(table.qrCodeId)
                .setQrCodeImagePath(ticket.getUrl())
                .setMaxNumberOfSeat(table.maxNumberOfSeat)
                .setTicket(ticket.getTicket())
                .setSceneId(table.sceneId)
                .setStatus(table.status)
                .setIsSilent(table.isSilent);
    }
}
