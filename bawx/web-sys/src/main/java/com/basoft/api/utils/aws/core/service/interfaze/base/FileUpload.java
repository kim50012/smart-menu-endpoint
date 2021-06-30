package com.basoft.api.utils.aws.core.service.interfaze.base;

import com.basoft.api.utils.aws.core.domain.FileReference;
import com.basoft.api.utils.aws.core.domain.UploadFile;

import java.util.List;

/**
 * AWS File Upload文件上传
 *
 * @author Mentor
 * @version 1.0
 * @since 20190605
 */
public interface FileUpload {
    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    FileReference uploadFile(UploadFile file);

    /**
     * 批量上传文件
     *
     * @param fileList
     * @return
     */
    List<FileReference> batchUpload(List<UploadFile> fileList);
}
