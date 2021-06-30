package com.basoft.file;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication(scanBasePackages="com.basoft.file")
@EnableTransactionManagement
@Configuration
class InnerSpringApplicationBoot extends SpringApplicationBoot{
    @Override
    protected String getConfigFileName(Environment env) {
        return "classpath:app-config-dev.json";
    }
}

public class DevAppBootstrap {





    public static void main(String...args){

        try {
            ResourceUtils.getFile("classpath:app-config-test.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(DevAppBootstrap.class.getResource("/"));


        Path file = Paths.get("./images");
        if(!file.toFile().exists()){
            file.toFile().mkdir();
        }

        InnerSpringApplicationBoot ap = new InnerSpringApplicationBoot();
        ap.run("--config=app-config-test.json");

    }
}
