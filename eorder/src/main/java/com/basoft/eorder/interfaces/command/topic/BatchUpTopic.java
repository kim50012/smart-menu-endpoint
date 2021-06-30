package com.basoft.eorder.interfaces.command.topic;

import com.basoft.eorder.application.framework.Command;
import lombok.Data;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 3:49 PM 1/14/20
 **/

@Data
public class BatchUpTopic implements Command {
    public static final String NAME = "batchUpTopicStatus";

    private List<String> tpIds;  //主题编号  db_column: TP_ID

    private int tpStatus;

}
