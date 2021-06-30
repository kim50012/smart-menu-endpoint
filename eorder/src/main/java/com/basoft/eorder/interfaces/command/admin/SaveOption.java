package com.basoft.eorder.interfaces.command.admin;

import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.base.Option;
import com.basoft.eorder.application.framework.Command;

public class SaveOption implements Command {

    public static final String NAME = "saveOption";

    private Long id;
    private int type;
    private Long parentId;
    private String name;
    private String chnName;
    private int showIndex;
    private String description;


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Option build(Option parentOption, Long newId){

        if(parentOption == null){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        if(newId == null){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        return new Option.Builder()
                .id(newId)
                .name(this.name)
                .chnName(this.chnName)
                .parent(parentOption)
                .type(type)
                .build();
    }


}
