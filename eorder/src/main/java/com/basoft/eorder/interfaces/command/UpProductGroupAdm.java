package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.ProductGroup;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:41 2018/12/27
 **/
public class UpProductGroupAdm implements Command {

    public static final String NAME = "upProdctGroup";

    private Long id;
    private Long storeId;
    private String nameKor;
    private String nameChn;
    private int showIndex;
    private int status;


    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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

    public int getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public ProductGroup buildAdmin(UpProductGroupAdm updateProductGroup, Long id) {
        return new ProductGroup.Builder()
            .id(id)
            .storeId(this.storeId)
            .name(this.nameKor)
            .chnName(this.nameChn)
            .showIndex(this.showIndex)
            .status(this.status)
            .build();
    }
}
