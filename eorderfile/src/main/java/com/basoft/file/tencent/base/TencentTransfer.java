package com.basoft.file.tencent.base;

import com.basoft.file.application.AppConfigure;
import com.basoft.file.application.FileService;
import com.basoft.file.application.UploadFile;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 腾讯云存储核心组件
 *
 * @author Mentor
 * @version 1.0
 * @since 20190724
 */
@Slf4j
public class TencentTransfer {
    private static final String TENCENT_CLOUD_STORAGE_SERVICE = "tencentCloudStorageService";

    // bucket名需包含appid
    private String bucketName;

    private String rootKey;

    private String httpTencentRootPath;

    private String visitFullURL;

    private COSClient cosClient;

    /**
     * 构造方法：初始化cosClient对象
     *
     * @param appConfigure
     * @param cosClient
     */
    public TencentTransfer(AppConfigure appConfigure, COSClient cosClient) {
        log.info("Starting Initialize TencentTransfer......");

        final Map<String, Object> tencentCloudStorageConfig = (Map<String, Object>) appConfigure.getObject(TENCENT_CLOUD_STORAGE_SERVICE).orElseThrow(() -> new IllegalStateException("error"));
        Map<String, Object> configPros = (Map<String, Object>) tencentCloudStorageConfig.get("props");
        this.bucketName = (String) configPros.get("bucketName");
        this.rootKey = (String) configPros.get("rootKey");
        String region = (String) configPros.get("region");

        this.httpTencentRootPath = new StringBuilder("https://cos.")
                .append(region)
                .append(".")
                .append("myqcloud.com/")
                .append(this.bucketName).append("/")
                .toString();
        log.info("Tencent Full Path:[httpTencentRootPath]:::" + httpTencentRootPath);

        // https://{bucketName}.cos.{region}.myqcloud.com
        this.visitFullURL = (String) configPros.get("visitFullURL");
        this.visitFullURL = this.visitFullURL.replace("{bucketName}", bucketName).replace("{region}", region);
        this.visitFullURL = StringUtils.isEmpty(this.visitFullURL) ? httpTencentRootPath : this.visitFullURL;
        log.info("Tencent Full URL:[visitFullURL]:::" + visitFullURL);

        this.cosClient = cosClient;
        log.info("Initialize TencentTransfer End......");
    }

    /**
     * 从输入流进行读取并上传到COS
     *
     * @param file
     */
    public FileService.FileRef uploadFile(UploadFile file) {
        String fileUUID = UUID.randomUUID().toString();
        String fileUUIDName = fileUUIDName(fileUUID, file.originalName());
        String key = buildFilePath(this.rootKey, fileUUIDName);
        log.info("上传文件Key>>><<<<:::" + key);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
        objectMetadata.setContentLength(file.contents().length);

        // 默认下载时根据cos路径key的后缀返回响应的contentType, 上传时设置contentType会覆盖默认值
        objectMetadata.setContentType(file.type());
        objectMetadata.setContentEncoding("utf-8");

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(file.contents()), objectMetadata);

        // 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
        putObjectRequest.setStorageClass(StorageClass.Standard);

        try {
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            // putObjectResult会返回文件的etag
            //String etag = putObjectResult.getETag();

            String writeToUrl = new StringBuilder(this.httpTencentRootPath).append(key).toString();
            String visitFullURLPath = new StringBuilder(this.visitFullURL).append("/").append(key).toString();

            return new FileService.FileRef(fileUUID, writeToUrl, fileUUIDName, visitFullURLPath, file.props(), file);
        } catch (CosServiceException e) {
            // e.printStackTrace();
            log.error("上传文件至腾讯云CosServiceException错误:::" + e.getMessage());
            log.error("\n上传文件至腾讯云CosServiceException错误，异常栈\n", e);
            throw new IllegalStateException(e);
        } catch (CosClientException e) {
            // e.printStackTrace();
            log.error("上传文件至腾讯云CosClientException错误:::" + e.getMessage());
            log.error("\n上传文件至腾讯云CosClientException错误，异常栈\n", e);
            throw new IllegalStateException(e);
        }
    }

    /**
     * 生成文件的UUID名称
     *
     * @param fileUUID
     * @param originalName
     * @return
     */
    private String fileUUIDName(String fileUUID, String originalName) {
        String prefix = originalName.substring(originalName.lastIndexOf("."));
        return new StringBuilder(fileUUID).append(prefix).toString();
    }

    /**
     * 腾讯云存储虚拟目录
     *
     * @param rootPath
     * @param name
     * @return
     */
    private String buildFilePath(String rootPath, String name) {
        // 如果rootPath值为/则不设置虚拟目录
        if ("/".startsWith(rootPath)) {
            return name;
        }
        return rootPath + "/" + name;
    }

    /**
     * 批量上传文件
     *
     * @param files
     * @return
     */
    public List<FileService.FileRef> batchUpload(List<UploadFile> files) {
        log.info("批量上传文件至腾讯云:::START");
        List<FileService.FileRef> refList = new ArrayList<FileService.FileRef>(files.size());
        for (UploadFile file : files) {
            try {
                final FileService.FileRef fileRef = this.uploadFile(file);
                refList.add(fileRef);
            } catch (Throwable e) {
                log.error("批量上传文件至腾讯云错误:::" + e.getMessage());
                log.error("\n批量上传文件至腾讯云错误，异常栈\n", e);
            }
        }
        log.info("批量上传文件至腾讯云:::END");
        return refList;
    }

    /**
     *从腾讯云获取文件
     *
     * @param fileName
     * @return byte[]
     */
    public byte[] fetchObject(String fileName) {
        // file key
        String key = buildFilePath(this.rootKey, fileName);
        log.info("从腾讯云获取文件，key is:::" + key);

        // GetObjectRequest
        GetObjectRequest getObjectRequest = new GetObjectRequest(this.bucketName, key);

        // 获取COSObject
        COSObject cosObject = cosClient.getObject(getObjectRequest);

        // 获取COSObjectInputStream流
        COSObjectInputStream cosObjectInput = cosObject.getObjectContent();

        // 转化为byte数组
        try {
            return org.apache.commons.io.IOUtils.toByteArray(cosObjectInput);
        } catch (IOException e) {
            log.error("从腾讯云获取文件时流处理错误:::" + e.getMessage());
            log.error("从腾讯云获取文件时流处理错误，异常栈\n", e);
            return null;
        }
    }
}