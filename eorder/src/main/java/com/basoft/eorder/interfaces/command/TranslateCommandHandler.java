package com.basoft.eorder.interfaces.command;

import com.basoft.eorder.application.framework.CommandHandler;
import com.basoft.eorder.util.TranslateUtil;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 2:40 下午 2019/11/19
 **/
@CommandHandler.AutoCommandHandler("TranslateCommandHandler")
public class TranslateCommandHandler {


    /**
     * 翻译(汉译韩或者韩译汉)
     *
     * @param translate
     * @return
     */
    @CommandHandler.AutoCommandHandler(SaveTranslate.TRANS_CMT)
    public SaveTranslate translate(SaveTranslate translate) {
        try {
            translate.setResult(TranslateUtil.translateTest(translate.getContent()));
        } catch (Exception e) {
            e.getMessage();
        }
        return translate;
    }
}
