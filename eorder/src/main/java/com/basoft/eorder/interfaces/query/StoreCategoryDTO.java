package com.basoft.eorder.interfaces.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

public class StoreCategoryDTO {



    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private String chnName;
    private int status;

    private List<StoreCategoryDTO> children;

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

    public List<StoreCategoryDTO> getChildren() {
        return children;
    }

    public void setChildren(List<StoreCategoryDTO> children) {
        this.children = children;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
