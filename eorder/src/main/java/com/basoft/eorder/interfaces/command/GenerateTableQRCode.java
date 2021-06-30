package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;

import java.util.List;

public class GenerateTableQRCode implements Command {
    public static final String NAME = "generateTableQRCode";
    private List<Long> tableIds;

    public List<Long> getTableIds() {
        return tableIds;
    }

    public void setTableIds(List<Long> tableIds) {
        this.tableIds = tableIds;
    }
}
