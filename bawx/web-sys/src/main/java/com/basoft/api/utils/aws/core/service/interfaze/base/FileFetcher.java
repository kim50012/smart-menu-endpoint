package com.basoft.api.utils.aws.core.service.interfaze.base;

/**
 * AWS File Fetcher文件拉取
 *
 * @author Mentor
 * @version 1.0
 * @since 20190605
 */
public interface FileFetcher {

    /**
     * 获取文件
     *
     * @param fileFullPath
     * @return
     */
    byte[] fetch(String fileFullPath);
}
