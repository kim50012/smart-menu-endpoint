package com.basoft.eorder.interfaces.command.admin;

import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.base.Option;
import com.basoft.eorder.application.framework.Command;
import lombok.Data;

@Data
public class UpdateOption implements Command {
    public static final String DEL_OPTION = "delOption";

    private Long id;

    private int status;

    public Option build(){

        if (this.id == null) {
            throw new BizException(ErrorCode.PARAM_MISSING);
        }

        return new Option.Builder()
                .id(id)
                .status(status)
                .build();
    }
}
