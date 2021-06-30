package com.basoft.file;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

public class FileTransformTest {


    public static void main(String...args){


        String img = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQHd7jwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAybDNiV1pncHBjeUMxdDVVVmhzY18AAgRFa1JcAwQAjScA";

        URI uri = URI.create(img);
        System.out.println(uri.getQuery().split("=")[0]);
        System.out.println(uri.getQuery().split("=")[1]);

    }


    static byte[] payload(String img){

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpPost = new HttpGet(img);


        try {
            final CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            final HttpEntity entity = httpResponse.getEntity();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            entity.writeTo(baos);

            System.out.println(baos.size());
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }
}
