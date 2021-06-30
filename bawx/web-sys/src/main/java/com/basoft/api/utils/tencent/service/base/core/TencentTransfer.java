package com.basoft.api.utils.tencent.service.base.core;

import com.basoft.api.utils.aws.core.domain.FileReference;
import com.basoft.api.utils.aws.core.domain.UploadFile;
import com.basoft.api.utils.tencent.config.TencentConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
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
    private TencentConfig tencentConfig;

    // bucket名需包含appid
    private String bucketName;

    private String rootKey;

    private String httpTencentRootPath;

    private String visitFullURL;

    private COSClient cosClient;

    public TencentTransfer(TencentConfig tencentConfig, COSClient cosClient) {
//        log.info("Starting Initialize TencentTransfer......");
        this.bucketName = tencentConfig.getBucketName();
        this.rootKey = tencentConfig.getRootKey();
        String region = tencentConfig.getRegion();

        this.httpTencentRootPath = new StringBuilder("https://cos.")
                .append(region)
                .append(".")
                .append("myqcloud.com/")
                .append(this.bucketName).append("/")
                .toString();
//        log.info("Tencent Full Path:[httpTencentRootPath]:::" + httpTencentRootPath);

        // https://{bucketName}.cos.{region}.myqcloud.com
        this.visitFullURL = tencentConfig.getVisitFullURL();
        this.visitFullURL = this.visitFullURL.replace("{bucketName}", bucketName).replace("{region}", region);
        this.visitFullURL = StringUtils.isEmpty(this.visitFullURL) ? httpTencentRootPath : this.visitFullURL;
//        log.info("Tencent Full URL:[visitFullURL]:::" + visitFullURL);

        this.cosClient = cosClient;
//        log.info("Initialize TencentTransfer End......");
    }

    /**
     * 从输入流进行读取并上传到COS
     *
     * @param file
     */
    public FileReference uploadFile(UploadFile file) {
        String fileUUID = UUID.randomUUID().toString();
        String fileUUIDName = fileUUIDName(fileUUID, file.originalName());
        String key = buildFilePath(this.rootKey, fileUUIDName);
//        log.info("上传文件Key>>><<<<:::" + key);

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

            return new FileReference(fileUUID, writeToUrl, fileUUIDName, visitFullURLPath, file.props(), file);
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
}