package com.basoft.api.utils.tencent.config;

import com.basoft.api.utils.tencent.service.TencentFileService;
import com.basoft.api.utils.tencent.service.base.core.TencentTransfer;
import com.basoft.api.utils.tencent.service.impl.TencentFileServiceImpl;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云存储组件实例化
 *
 * @author Mentor
 * @version 1.0
 * @since 20190724
 */
@Data
@Configuration
public class TencentConfig {
    @Value("${tencentcloud.file.accessKey}")
    public String accessKey;

    @Value("${tencentcloud.file.secretKey}")
    public String secretKey;

    @Value("${tencentcloud.file.region}")
    public String region;

    @Value("${tencentcloud.file.bucketName}")
    public String bucketName;

    @Value("${tencentcloud.file.rootKey}")
    public String rootKey;

    @Value("${tencentcloud.file.visitFullURL}")
    public String visitFullURL;

    /**
     * COSClient Instantiation
     *
     * @return
     */
    @Bean
    public COSClient cosClient() {
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);

        // 2 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(region));

        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);

        return cosclient;
    }

    /**
     * TencentTransfer Instantiation
     *
     * @param cosClient
     * @return
     */
    @Bean
    public TencentTransfer createTencentTransfer(COSClient cosClient) {
        return new TencentTransfer(this, cosClient);
    }

    /**
     * TencentFileService Instantiation
     *
     * @param tencentTransfer
     * @return
     */
    @Bean("tencentFileService")
    public TencentFileService createTencentFileService(TencentTransfer tencentTransfer) {
        return new TencentFileServiceImpl(tencentTransfer);
    }
}