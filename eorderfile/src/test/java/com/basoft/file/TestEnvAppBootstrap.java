package com.basoft.file;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestEnvAppBootstrap {


    public static void main(String...args){




//        System.out.println(DevAppBootstrap.class.getResource("/"));
        Path file = Paths.get("./images");
        if(!file.toFile().exists()){
            file.toFile().mkdir();
        }


        new SpringApplicationBoot().run("--config=app-config-test.json");



    }
}
