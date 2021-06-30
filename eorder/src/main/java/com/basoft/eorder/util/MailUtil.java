package com.basoft.eorder.util;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:20 2019/8/26
 **/

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.interfaces.query.SettleDTO;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;


@Component
public class MailUtil {


    private String HOST = "";
    private static final Integer PORT = 25;
    private static final String USERNAME = "18513584792@163.com";
    private static final String PASSWORD = "dong123450";
    private static final String emailForm = "18513584792@163.com";
    private static final String timeout = "25000";
    private static final String personal = "董锡福";
    private static final String subject = "你好";
    private static final String html = "您的邮箱验证码为：";
    // private  JavaMailSenderImpl mailSender = createMailSender();


    /**
     * 邮件发送器
     *
     * @return 配置好的工具
     */
    private JavaMailSenderImpl createMailSender(AppConfigure appConfigure) {
        Map<String, Object> mailConfig = appConfigure.getObject("mail-config").map((o) -> (Map<String, Object>) o).orElseGet(() -> null);
        String mailHost = (String) mailConfig.get("mailHost");
        String mailPort = (String) mailConfig.get("mailPort");
        String mailUsername = (String) mailConfig.get("mailUsername");
        String mailPassword = (String) mailConfig.get("mailPassword");

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(mailHost);
        sender.setPort(Integer.valueOf(mailPort));
        sender.setUsername(mailUsername);
        sender.setPassword(mailPassword);
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", timeout);
        p.setProperty("mail.smtp.auth", "false");
        sender.setJavaMailProperties(p);
        return sender;
    }

    /**
     * 发送邮件
     *
     * @param to      接受人
     * @param content 发送内容
     * @throws MessagingException           异常
     * @throws UnsupportedEncodingException 异常
     */
    public void sendMail(String to, String subject, String content, AppConfigure appConfigure) throws MessagingException, UnsupportedEncodingException {
        JavaMailSenderImpl mailSender = createMailSender(appConfigure);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Map<String, Object> mailConfig = appConfigure.getObject("mail-config").map((o) -> (Map<String, Object>) o).orElseGet(() -> null);
        String mailFrom = (String) mailConfig.get("mailFrom");
        String personal = (String) mailConfig.get("personal");

        String html = content;

        messageHelper.setFrom(mailFrom, personal);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);

        // messageHelper.addAttachment("", new File(""));//附件
        mailSender.send(mimeMessage);
    }

    public String storeSettlehtml(SettleDTO dto) {
        StringBuilder html = new StringBuilder();
        html.append("<html>\n" +
            "<body style='padding:0; margin:0; border:none; line-height:1em; word-break:normal;white-space:normal;'>\n" +
            "<div style='width:100%;'>\n" +
            "<style> .imgdiv img {height:26px; position:absolute; top:50%; left:50%; transform: translate(-50%,-50%)} </style>\n" +
            "<div class='imgdiv' style='width:100%; height:110px; position:relative; background:#38a75c; margin-bottom:40px;'>\n" +
            "<img src='http://admin.batechkorea.com/bawx/res/images/logo/1/539548684556959751/2019/11/22/logo_1_539548684556959751_20191122145244630_91474963.jpg'>\n" +
            "</div>\n" +
            "<div style='width:480px; margin:0 auto;'>\n" +
            "<div style='margin-bottom:20px;'>\n" +
            "<p style='text-align:center;'>\n" +
            "<span style='color:#38a75c; font-weight:bold; vertical-align: middle; font-size:16px; padding-right:10px;'>" + dto.getClosingMonths() + "</span><!--月份-->\n" +
            "<span style='color:#38a75c; font-weight:bold; vertical-align: middle; font-size:16px; padding-right:10px;'>총 서비스 이용료</span>\n" +
            "<span style='color:#38a75c; font-weight:bold; font-size:26px;'>" + StringUtil.decimalStr(Long.valueOf(dto.getFinalFee())) + "원</span>\n" +
            "</p>\n" +
            "</div>\n" +
            "<p style='text-align:center;'>\n" +
            "<span id= 'storeNm' style='color:#9494a5; font-size:14px;'>" + dto.getStoreNm() + "</span> <!--商店名-->\n" +
            "<span style='color:#9494a5; font-size:14px;'> 점주님, BA Place 플랫폼을 이용해 주셔서 감사합니다.</span>\n" +
            "</p>\n" +
            "<div style='margin-top:80px;'>\n" +
            "<p style='font-size:20px; font-weight:bold; color:#000; border-bottom:1px solid #000; padding-bottom:14px;margin-bottom:30px'>이용내역</p>\n" +
            "<p style='margin-bottom:15px;'>\n" +
            "<span style='font-size:14px; color:#000; display:inline-block'>거래날짜 : </span> \n" +
            "<span style='font-size:14px; color:#000; display:inline-block; text-align:right; float:right'>" + dto.getPayFrDt() + " - " + dto.getPayToDt() + "</span><!--交易日期-->\n" +
            "</p>\n" +
            "<p style='margin-bottom:15px;'>\n" +
            "<span style='font-size:14px; color:#000; display:inline-block'>거래금액 : </span>\n" +
            "<span style='font-size:14px; color:#000; display:inline-block; text-align:right; float:right'>" + StringUtil.decimalStr(dto.getAmount().longValue()) + "원</span><!--交易金额-->\n" +
            "</p>\n" +
            "<p style='margin-bottom:15px;'>\n" +
            "<span style='font-size:14px; color:#000; display:inline-block'>거래건수 : </span>\n" +
            "<span style='font-size:14px; color:#000; display:inline-block; text-align:right; float:right'>" + dto.getPayCnt() + "건</span><!--交易数量-->\n" +
            "</p>\n" +
            "<p style='border-top:1px solid #ededed; margin-top:30px; margin-bottom:30px;'></p>\n" +
            "<p style='margin-bottom:15px;'>\n");
        html.append("<span style='font-size:14px; color:#000; display:inline-block'>");
        if ("1".equals(dto.getSettleType())) {
            dto.setSettleTypeStr("서비스 이용료 (영업매출" + dto.getChargeRate() + "%)");
        } else if ("2".equals(dto.getSettleType())) {
            dto.setSettleTypeStr("서비스 이용료(영업매출 " + dto.getChargeRate() + "% + 최소이용료)");
        } else if ("3".equals(dto.getSettleType())) {
            dto.setSettleTypeStr("서비스 이용료 ( 월정액 )");
        }
        html.append(" " + dto.getSettleTypeStr() + " ");
        html.append("</span> <!--服务费百分比-->\n");
        html.append("<span style='font-size:14px; color:#38a75c; display:inline-block; text-align:right; float:right; font-weight:bold'>" +StringUtil.decimalStr(Long.valueOf(dto.getFinalFee()))  + "원</span><!--平台服务费-->\n" +
            "</p>\n" +
            "<p style='border-top:1px solid #ededed; margin-top:30px; margin-bottom:20px;'></p>\n" +
            "<div style='text-align:right; margin-bottom:20px;'>\n" +
            "<button style='outline: none; box-shadow: none; border: 0; font-size:14px; padding:5px 20px; background:#38a75c; color:#fff'>\n" +
            "<a style='text-decoration: unset; color:#fff;' href='http://manager.bacommerce.co.kr'>상세내역</a>\n" +
            "</button>\n" +
            "</div>\n" +
            "<p style='margin-top:30px;'>\n" +
            "<span style='color:#9494a5; font-size:14px;'>*상세내역 버튼을 클릭하시면 발급 받으신 아이디로 CMS 로그인 하셔서 상세 내역 조회가 가능합니다.</span>\n" +
            "</p>\n" +
            "</div>\n" +
            "<div style='margin-top:80px;'>\n" +
            "<p style='font-size:20px; font-weight:bold; color:#000; border-bottom:1px solid #000; padding-bottom:14px;margin-bottom:30px'>고객센터</p>\n" +
            "<p><span style='color:#9494a5; font-size:14px;'>본 메일은 발신전용으로 회신이 불가 합니다. 문의 사항은 help@bacommerce.co.kr 메일을 이용해 주세요</span></p>\n" +
            "</div>\n" +
            "<div style='margin-top:80px;'>\n" +
            "<p style='font-size:10px; color:#bbb; padding-top:20px; border-top:1px solid #dedede;'>회사명: 주식회사 비에이커머스 | 대표 : 김기찬 | 사업자등록번호 : 123-324325-23423 | '개인정보책임담당자 : 김기찬 | 주소 : 제주시 도령로 102 | 통신판매번호 : 123-12312312 | 전화 : 064-1234-4567 | E-mail : help@bacommerce.co.kr</p>\n" +
            "</div>\n" +
            "</div>\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>\n");

        return html.toString();
    }


    public void sendMailTest(String to, String subject, String content, AppConfigure appConfigure) throws MessagingException, UnsupportedEncodingException {
        JavaMailSenderImpl mailSender = createMailSender(appConfigure);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Map<String, Object> mailConfig = appConfigure.getObject("mail-config").map((o) -> (Map<String, Object>) o).orElseGet(() -> null);
        String mailFrom = (String) mailConfig.get("mailFrom");
        String personal = (String) mailConfig.get("personal");

        String html = content;

        messageHelper.setFrom(mailFrom, personal);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);

        // messageHelper.addAttachment("", new File(""));//附件
        mailSender.send(mimeMessage);
    }

}