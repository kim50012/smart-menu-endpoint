package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;

public class BatchGenerateStoreTable implements Command {
    public static final String NAME = "saveStoreTableList";
    private Long storeId;

    //批量新增
    private int id;
    private int startNum;
    private int endNum;
    private String tag;
    private int maxSeat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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
}
