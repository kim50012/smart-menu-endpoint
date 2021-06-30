package com.basoft.api.utils.tencent.service;

import com.basoft.api.utils.aws.core.domain.FileReference;
import com.basoft.api.utils.aws.core.domain.UploadFile;

/**
 * 腾讯云存储Service接口
 *
 * @author Mentor
 * @version 1.0
 * @since 20190724
 */
public interface TencentFileService {
    FileReference uploadFile(UploadFile file);
}
