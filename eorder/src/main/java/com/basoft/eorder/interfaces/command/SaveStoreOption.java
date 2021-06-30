package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.base.Option;
import com.basoft.eorder.application.framework.Command;
import com.basoft.eorder.domain.model.StoreOption;

public class SaveStoreOption implements Command {

    public static final String NAME = "saveStoreOption";
   // NORMAL(0),OPEN(1),CLOSED(2),DELETED(3);
    private Long pcId;
    private Long storeId;
    private int type;
    private Long parentId;
    private String name;
    private String chnName;
    private int showIndex;
    private int baseExtend;
    private String description;


    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public Long getPcId() {
        return pcId;
    }

    public void setPcId(Long pcId) {
        this.pcId = pcId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBaseExtend() {
        return baseExtend;
    }

    public void setBaseExtend(int baseExtend) {
        this.baseExtend = baseExtend;
    }

    public StoreOption build(StoreOption parentOption, Long newId, Long storeId){

        if(parentOption == null){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        if(newId == null){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        return new StoreOption.Builder()
                .pcId(newId)
                .storeId(storeId)
                .name(this.name)
                .chnName(this.chnName)
                .parent(parentOption)
                .type(type)
                .showIndex(showIndex)
                .description(description)
                .baseExtend(baseExtend)
                .build();
    }


}
