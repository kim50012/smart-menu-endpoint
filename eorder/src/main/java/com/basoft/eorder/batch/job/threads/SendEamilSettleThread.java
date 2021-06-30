package com.basoft.eorder.batch.job.threads;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.interfaces.query.SettleDTO;
import com.basoft.eorder.util.MailUtil;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 3:11 下午 2019/11/22
 **/
@Slf4j
public class SendEamilSettleThread implements Runnable {
    private AppConfigure appConfigure;
    private SettleDTO settleDTO;
    private String emailTile;

    public SendEamilSettleThread(AppConfigure appConfigure,SettleDTO settleDTO,String emailTile) {
        this.appConfigure = appConfigure;
        this.settleDTO = settleDTO;
        this.emailTile = emailTile;
    }
    @Override
    public void run() {
        MailUtil util = new MailUtil();
        try {
            util.sendMail(settleDTO.getEmail(),emailTile,util.storeSettlehtml(settleDTO),appConfigure);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
