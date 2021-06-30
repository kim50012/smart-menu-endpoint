package com.basoft.eorder.domain.excel;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午6:04 2019/8/22
 **/
public class BusClick {

    @ExcelColumn(valueKor = "cityCode",col = 1)
    private String cityCode;

    @ExcelColumn(valueKor = "markId", col = 2)
    private String markId;

    @ExcelColumn(valueKor = "toaluv", col = 3)
    private String toaluv;

    @ExcelColumn(valueKor = "date", col = 4)
    private String date;

    @ExcelColumn(valueKor = "clientVer", col = 5)
    private String clientVer;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getMarkId() {
        return markId;
    }

    public void setMarkId(String markId) {
        this.markId = markId;
    }

    public String getToaluv() {
        return toaluv;
    }

    public void setToaluv(String toaluv) {
        this.toaluv = toaluv;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClientVer() {
        return clientVer;
    }

    public void setClientVer(String clientVer) {
        this.clientVer = clientVer;
    }
}
