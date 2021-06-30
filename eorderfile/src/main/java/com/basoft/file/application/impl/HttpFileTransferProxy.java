package com.basoft.file.application.impl;

import com.basoft.file.application.FileFetcher;
import com.basoft.file.application.FileTransfer;
import com.basoft.file.application.UploadFile;
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
import java.util.Map;

import static com.basoft.file.application.FileService.FileRef;

public class HttpFileTransferProxy extends FileTransferAdapter implements FileTransfer,FileFetcher {


    private String remoteHostAddr;
//    private String remoteReadHostAddr;
    private static final ObjectMapper om = new ObjectMapper();


    HttpFileTransferProxy(){
    }

    public HttpFileTransferProxy(String str){
        this.remoteHostAddr = str;
    }


    @Override
    public FileRef uploadFile(UploadFile file) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        FileRef result=null;
        try {
            String fileName = file.originalName();
            HttpPost httpPost = new HttpPost(remoteHostAddr);
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
                final HashMap hashMap = om.readValue(resultStr, HashMap.class);
                String fileId = (String) hashMap.get("id");
                String fullPath = (String) hashMap.get("fullPath");
                String fileUri = (String)hashMap.get("fileUri");

                result = new FileRef(fileId,fullPath,fileUri,file);
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

/*    @Override
    public byte[] fetch(String fileFullPath){
        throw new IllegalStateException("unsported");
    }*/




    public static final class Factory implements FileTransferFactory{

        @Override
        public FileTransfer of(Map<String, Object> envMap) {

            String remoteAddress = (String) envMap.get("remoteAddr");
            HttpFileTransferProxy hftp = new HttpFileTransferProxy(remoteAddress);
            return hftp;
        }
    }
}
