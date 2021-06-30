package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午10:35 2018/12/31
 **/
public class UpadateProductName implements Command {
    public static final String NAME="updateProductName";

    private Long id;
    private Long storeId;
    private String nameKor;
    private String nameChn;
    private String desKor;//韩文说明
    private String desChn;//中文说明

    private String detailDesc;
    private String detailChnDesc;
    private int status;


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

    public static String getNAME() {
        return NAME;
    }

    public String getNameKor() {
        return nameKor;
    }

    public void setNameKor(String nameKor) {
        this.nameKor = nameKor;
    }

    public String getNameChn() {
        return nameChn;
    }

    public void setNameChn(String nameChn) {
        this.nameChn = nameChn;
    }

    public String getDesKor() {
        return desKor;
    }

    public void setDesKor(String desKor) {
        this.desKor = desKor;
    }

    public String getDesChn() {
        return desChn;
    }

    public void setDesChn(String desChn) {
        this.desChn = desChn;
    }

    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    public String getDetailChnDesc() {
        return detailChnDesc;
    }

    public void setDetailChnDesc(String detailChnDesc) {
        this.detailChnDesc = detailChnDesc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
