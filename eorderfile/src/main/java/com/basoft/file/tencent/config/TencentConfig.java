package com.basoft.file.tencent.config;

import com.basoft.file.application.AppConfigure;
import com.basoft.file.tencent.base.TencentTransfer;
import com.basoft.file.tencent.base.service.TencentFileService;
import com.basoft.file.tencent.base.service.impl.TencentFileServiceImpl;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 腾讯云存储组件实例化
 *
 * @author Mentor
 * @version 1.0
 * @since 20190724
 */
@Configuration
public class TencentConfig {
    // AWS File Service Config
    private static final String TENCENT_CLOUD_STORAGE_SERVICE = "tencentCloudStorageService";

    // @Autowired
    // private AppConfigure appConfigure;

    /**
     * COSClient Instantiation
     *
     * @return
     */
    @Bean
    public COSClient cosClient(AppConfigure appConfigure) {
        final Map<String, Object> tencentCloudStorageConfig = (Map<String, Object>) appConfigure.getObject(TENCENT_CLOUD_STORAGE_SERVICE).orElseThrow(() -> new IllegalStateException("error"));
        Map<String, Object> configPros = (Map<String, Object>) tencentCloudStorageConfig.get("props");

        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials((String) configPros.get("accessKey"), (String) configPros.get("secretKey"));

        // 2 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region((String) configPros.get("region")));

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
    public TencentTransfer createTencentTransfer(AppConfigure appConfigure, COSClient cosClient) {
        return new TencentTransfer(appConfigure, cosClient);
    }

    /**
     * TencentFileService Instantiation
     *
     * @param tencentTransfer
     * @param dataSource
     * @return
     */
    @Bean("tencentFileService")
    public TencentFileService createTencentFileService(TencentTransfer tencentTransfer, DataSource dataSource) {
        return new TencentFileServiceImpl(tencentTransfer, dataSource);
    }
}