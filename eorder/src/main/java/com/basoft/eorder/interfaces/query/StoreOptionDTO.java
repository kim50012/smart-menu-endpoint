package com.basoft.eorder.interfaces.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:41 2019/7/8
 **/
public class StoreOptionDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long pcId;
    private Long storeId;
    private String name;
    private String chnName;
    private int status;
    private int showIndex;
    private int baseExtend;
    private String description;
    private List<StoreOptionDTO> children =new ArrayList<StoreOptionDTO>();


    public Long getPcId() {
        return pcId;
    }

    public void setPcId(Long pcId) {
        this.pcId = pcId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBaseExtend() {
        return baseExtend;
    }

    public void setBaseExtend(int baseExtend) {
        this.baseExtend = baseExtend;
    }

    public List<StoreOptionDTO> getChildren() {
        return children;
    }

    public void setChildren(List<StoreOptionDTO> children) {
        this.children = children;
    }

    public StoreOptionDTO find(Long tarId){

        if(tarId != null){

            if(this.pcId.equals(tarId)){
                return this;
            }

            for(StoreOptionDTO cdto :this.children){
                final StoreOptionDTO optionDTO = cdto.find(tarId);
                if(optionDTO != null){
                    return optionDTO;
                }
            }
        }


        return null;



    }
}
