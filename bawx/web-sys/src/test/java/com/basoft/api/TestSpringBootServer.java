package com.basoft.api;

public class TestSpringBootServer {

/*    @SpringBootApplication
    @Configurationls
    @ComponentScan(basePackages = { "com.basoft" })
    static class BAAwsSoftWechatApplication extends BAAwsSoftWechatApplication{


    }*/

    public static void main(String...args){


        BAAwsSoftWechatApplication.main("--server.port=8000","--spring.profiles.active=dev");

    }
}
