package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;
import lombok.Data;

@Data
public class UpdateProSkuStatus implements Command {

    public static final String UP_STATUS_NAME = "updateProSKuStatus";

    private Long id;

    private int status;

}
