package com.basoft.eorder.application.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpFileSerivce implements FileService {

    private String remoteServer;

    private static final ObjectMapper om = new ObjectMapper();

    HttpFileSerivce(){}

    public HttpFileSerivce(String remoteServer){
        this.remoteServer = remoteServer;
    }

    @Override
    public FileRef uploadFile(UploadFile file) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        FileRef result=null;
        try {
            String fileName = file.originalName();
            HttpPost httpPost = new HttpPost(remoteServer);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", file.contents(), ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
            builder.addTextBody("filename", fileName);// 类似浏览器表单提交，对应input的name和value
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);// 执行提交
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                String resultStr = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));

                System.out.println(resultStr);
                final HashMap hashMap = om.readValue(resultStr, HashMap.class);
                String fileId = (String) hashMap.get("id");
                String fullPath = (String) hashMap.get("fullPath");
                result = new FileRef(fileId,fullPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public List<FileRef> batchUpload(List<UploadFile> collect) {
        List<FileRef> refList = new ArrayList<>(collect.size());
        for(UploadFile file:collect){
            refList.add(uploadFile(file));
        }
        return refList;
    }

    @Override
    public FileData getFile(String fileId) {
        throw new IllegalStateException("error get file ");
    }
}
