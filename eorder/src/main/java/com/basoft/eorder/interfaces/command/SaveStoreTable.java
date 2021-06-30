package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.StoreTable;




/**
 * @author woonill
 * 创建保存商店餐桌信息
 */
public class SaveStoreTable implements Command {


    public static final String NAME = "saveStoreTable";

    private Long storeId;
    private Long id;

    private int number;//桌子号码
    private String tag ;
    private int maxSeat;
//    private String qrCodeImagePath;
    private int order;

    //批量新增
    private int startNum;

    private int endNum;

    private int isSilent;

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

//    public String getQrCodeImagePath() {
//        return qrCodeImagePath;
//    }
//    public void setQrCodeImagePath(String qrCodeImagePath) {
//        this.qrCodeImagePath = qrCodeImagePath;
//    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public int getMaxSeat() {
        return maxSeat;
    }

    public void setMaxSeat(int maxSeat) {
        this.maxSeat = maxSeat;
    }

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public int getEndNum() {
        return endNum;
    }

    public void setEndNum(int endNum) {
        this.endNum = endNum;
    }

    public int getIsSilent() {
        return isSilent;
    }

    public void setIsSilent(int isSilent) {
        this.isSilent = isSilent;
    }

    /**
     * @param store
     * @param sid
     * @return StoreTable
     *
     */
    StoreTable build(Store store, Long sid){
        return new StoreTable.Builder()
                .setId(sid)
                .setOrder(this.order)
                .setTag(this.tag)
                .setMaxNumberOfSeat(this.maxSeat)
                .setNumber(this.number)
//                .setQrCodeImagePath(qrcodeImage.getId())
                .setIsSilent(this.isSilent)
                .setStore(store)
                .build();
    }


}
