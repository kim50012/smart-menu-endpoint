package com.basoft.eorder.interfaces.command.admin;

import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.application.framework.Command;

public class SaveCategory implements Command {
    public static final String NAME = "saveCategory";
    private Long id;

    private int type;

    private Long parentId;

    private String name;

    private String chnName;

    private int functionType;

    private int categoryType;

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

    Category build(Category parent, Long id) {
        validateInParameter();
        return new Category.Builder()
                .id(id)
                .type(this.type)
                .name(this.name)
                .chnName(this.chnName)
                .parent(parent)
                .functionType(this.functionType)
                .categoryType(this.categoryType)
                .manageType(this.manageType)
                .build();
    }

    private void validateInParameter() {
    }
}
