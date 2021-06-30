package com.basoft.eorder.interfaces.query;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @ProjectName: intellij_work
 * @Package: com.basoft.eorder.interfaces.query
 * @ClassName: StoreTable
 * @Description: TODO
 * @Author: liminzhe
 * @Date: 2018-12-13 16:21
 * @Version: 1.0
 */

public class StoreTableDTO {

    private Long id;		//
    private Long storeId;		//
    private int number;
    private String tag;
    private String numberStr;
    private String qrCodeImagePath;
    private int maxSeat;
    private int showIndex;
    private String qrcodeImageUrl;
    private String isSilent;
    private String created;		//
    private int status;		//

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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

    public String getNumberStr() {
        return numberStr;
    }

    public void setNumberStr(String numberStr) {
        this.numberStr = numberStr;
    }

    public String getQrCodeImagePath() {
        return qrCodeImagePath;
    }

    public void setQrCodeImagePath(String qrCodeImagePath) {
        this.qrCodeImagePath = qrCodeImagePath;
    }

    public int getMaxSeat() {
        return maxSeat;
    }

    public void setMaxSeat(int maxSeat) {
        this.maxSeat = maxSeat;
    }

    public int getShowIndex() { return showIndex; }

    public void setShowIndex(int showIndex) { this.showIndex = showIndex; }

    public String getQrcodeImageUrl() { return qrcodeImageUrl; }

    public void setQrcodeImageUrl(String qrcodeImageUrl) { this.qrcodeImageUrl = qrcodeImageUrl; }

    public String getIsSilent() {
        return isSilent;
    }

    public void setIsSilent(String isSilent) {
        this.isSilent = isSilent;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
