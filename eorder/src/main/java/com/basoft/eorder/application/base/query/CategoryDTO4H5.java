package com.basoft.eorder.application.base.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class CategoryDTO4H5 {
    @JsonIgnore//JsonSerialize在这个就不起作用了！
    @JsonSerialize(using = ToStringSerializer.class)
    //@JsonIgnore
    private Long id;
    @JsonIgnore
    private int type;
    @JsonSerialize(using = ToStringSerializer.class)
    private String name;
    @JsonSerialize(using = ToStringSerializer.class)
    private String chnName;
    @JsonIgnore
    private int status;
    @JsonIgnore
    private int functionType;
    @JsonIgnore
    private int categoryType;
    @JsonIgnore
    private int manageType;

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

    public int getFunctionType() {
        return functionType;
    }

    public void setFunctionType(int functionType) {
        this.functionType = functionType;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public int getManageType() {
        return manageType;
    }

    public void setManageType(int manageType) {
        this.manageType = manageType;
    }
}