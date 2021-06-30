package com.basoft.eorder.interfaces.command.admin;

import com.basoft.eorder.application.framework.Command;

public class StopCategory implements Command {


    public static final String NAME = "stopCategory";
    private Long[] ids;

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }
}
