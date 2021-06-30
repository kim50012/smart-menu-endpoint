package com.basoft.eorder.domain.model;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:07 2018/12/24
 **/
public class StoreTableBatch {
    private Long storeId;
    private Long id;

    private int number;//桌子号码
    private String tag;
    private String qrCodeImagePath;
    private int maxNumberOfSeat;

    private int order;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
