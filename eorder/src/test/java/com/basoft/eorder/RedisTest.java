package com.basoft.eorder;

import com.basoft.eorder.application.file.FileRef;
import com.basoft.eorder.application.file.HttpFileSerivce;
import com.basoft.eorder.application.file.UploadFile;
import com.basoft.eorder.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import redis.clients.jedis.JedisShardInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class RedisTest {


    final static RedisConnectionFactory newRedisConnectionFactory() {
        System.out.println("\n\n\n start jedisConnectioNFactory");
        JedisShardInfo jsi = new JedisShardInfo("127.0.0.1");
        JedisConnectionFactory jcf = new JedisConnectionFactory(jsi);
        return jcf;
    }

//    @Test
    public void testUploadFile(){


        File file = Paths.get("/Users/woonill/woonill/workspace/ba-soft-pro/bawechat/eorder/src/test/pn1.jpeg").toFile();
        System.out.println(file.exists());
        try {
            UploadFile uploadFile = UploadFile.newBuild().name("file.png").originalName(file.getName())
                    .payload(Files.readAllBytes(file.toPath()))
                    .type("image/jepg")
                    .build();
            HttpFileSerivce hfs = new HttpFileSerivce("http://localhost:8083/file/api/v1/upload");
            final FileRef fileRef = hfs.uploadFile(uploadFile);
            System.out.println(fileRef.getFullPath());

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static void testRedis(){
        final RedisConnectionFactory rcf = newRedisConnectionFactory();
//        JedisConnectionFactory jcf = new JedisConnectionFactory();
        RedisTemplate<String,Object> tt = new RedisTemplate<>();
        tt.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        tt.setConnectionFactory(rcf);
        tt.afterPropertiesSet();


        RedisUtil ru =  new RedisUtil(tt);
        final boolean bawx_cms_token = ru.hasKey("CMS_USERS");
        System.out.println(bawx_cms_token);


        final Object bawx_cms_token1 = ru.hasKey("BAWX_CMS_TOKEN");
        System.out.println(bawx_cms_token1);


        final Object bawx_cms_token2 = ru.hget("BAWX_CMS_TOKEN", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InJvb3QifQ.s20zlMcmydArPzU03LBiS3On0yFA_Gm2ZIfY7K0EQmE");

        try {
            Map<String,Object> jsonMap = new ObjectMapper().readValue((String)bawx_cms_token2,HashMap.class);
            System.out.println(bawx_cms_token2);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
