package com.basoft.eorder.interfaces.command.admin;

import com.basoft.eorder.application.base.Category;
import com.basoft.eorder.application.framework.Command;
import lombok.Data;

@Data
public class UpdateCategory implements Command {

    public static final String NAME = "UpdateCategory";
    public static final String DEL_NAME = "delCategory";

    private Long id;

    private Category.Status status;


    public Category build() {
        return new Category.Builder()
                .id(this.id)
                .status(status)
                .build();
    }
}
