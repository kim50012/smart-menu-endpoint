package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.Command;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 2:55 下午 2019/11/19
 **/
@Data
public class SaveTranslate implements Command {
    public static final String TRANS_CMT = "translate";


    private String content;
    private String result;


}
