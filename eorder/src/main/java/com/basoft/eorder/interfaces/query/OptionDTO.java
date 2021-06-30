package com.basoft.eorder.interfaces.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.ArrayList;
import java.util.List;

public class OptionDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private String chnName;
    private int status;
    private int type;
    private List<OptionDTO> children =new ArrayList<OptionDTO>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<OptionDTO> getChildren() {
        return children;
    }

    public void setChildren(List<OptionDTO> children) {
        this.children = children;
    }



    public OptionDTO find(Long tarId){

        if(tarId != null){

            if(this.id.equals(tarId)){
                return this;
            }

            for(OptionDTO cdto :this.children){
                final OptionDTO optionDTO = cdto.find(tarId);
                if(optionDTO != null){
                    return optionDTO;
                }
            }
        }


        return null;



    }

}
