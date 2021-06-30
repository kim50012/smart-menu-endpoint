package com.basoft.eorder;

import com.basoft.eorder.application.AppConfigure;
import org.springframework.context.ConfigurableApplicationContext;

public class TestServerBoot {



    public static void main(String...args){
/*
        String rootPath = "--"+AppConfigure.BASOFT_UPLOAD_PATH+"=/Users/a/Downloads/project/bawechat/eorder/images-test";
        String appUrl = "--"+AppConfigure.BASOFT_APP_URL_PROP+"=http://localhost:8004/eorder";
        new InnerSpringEOrderApplicationBoot().run(rootPath,appUrl);
*/


        final ConfigurableApplicationContext run = new InnerSpringEOrderApplicationBoot().run(
                "--server.port=9000",
                "--spring.profiles.active=local",
                "--config=conf/app-config-local.json");

        final AppConfigure appConfigure = run.getBean(AppConfigure.class);




        String str =  new StringBuilder("redirect:")
                .append(appConfigure.get(AppConfigure.BASOFT_WECHAT_INIT_ROOT))
                .append("?")
                .append("token")
                .append("=")
                .append("123").toString();


        System.out.println(str);

        //String token = "22_qlfSVi-kQO6gv1DwkhEYJx8m8xoVXbrDuwkcCxAhLigWcsm2KIbCyCJYIvq-Kxa0GYjjeAqxj-3JI_L8rnKX4c-OvxwEpTHLvHVrpRGg9kCwwerIlEBtaaZAb-McZ1t-XxElNHoqcH14d1I2JMEbACAYBT";

        //RedisUtil redisUtil = run.getBean(RedisUtil.class);
       // redisUtil.set("wx_token",token,7200);




/*        RedisUtil ru = run.getBean(RedisUtil.class);

        boolean res = ru.set(
                "wx_token",
                "17_eT5Lr-CRLE2ejLWeaDbfhfjSUz5FQAH8qj3DebFvXsKF6wkYqN9-uaHRtx_1HzzozeVGwlt_pV1Z6FuOnW2Qts3Z8yj6-BGGvty5KqDnu81ykcBbnMVErg0iKy4d6HZyJqwRdY6xEoB4f1qXITBhAGAALT",
                60*60);
                System.out.println(res);


        System.out.println(ru.get("wx_token"));*/



    }
}
