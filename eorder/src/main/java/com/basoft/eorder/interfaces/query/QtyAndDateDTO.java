package com.basoft.eorder.interfaces.query;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:13 2019/2/18
 **/
public class QtyAndDateDTO {

    private int qty;

    private String date;

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
