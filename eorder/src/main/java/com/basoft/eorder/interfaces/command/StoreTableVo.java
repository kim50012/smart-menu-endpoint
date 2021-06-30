package com.basoft.eorder.interfaces.command;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:02 2018/12/13
 **/

public class StoreTableVo {

    private Long storeId;
    private String storeNm;
    private String description;
    private int tableNm;
    private String qrCodeImagePath;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreNm() {
        return storeNm;
    }

    public void setStoreNm(String storeNm) {
        this.storeNm = storeNm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTableNm() {
        return tableNm;
    }

    public void setTableNm(int tableNm) {
        this.tableNm = tableNm;
    }

    public String getQrCodeImagePath() {
        return qrCodeImagePath;
    }

    public void setQrCodeImagePath(String qrCodeImagePath) {
        this.qrCodeImagePath = qrCodeImagePath;
    }
}
