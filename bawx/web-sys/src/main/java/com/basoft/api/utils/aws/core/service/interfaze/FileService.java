package com.basoft.api.utils.aws.core.service.interfaze;

import com.basoft.api.utils.aws.core.domain.FileData;
import com.basoft.api.utils.aws.core.service.interfaze.base.FileFetcher;
import com.basoft.api.utils.aws.core.service.interfaze.base.FileUpload;

import java.util.List;

/**
 * AWS File Service。继承接口FileUpload, FileFetcher
 *
 * @author Mentor
 * @version 1.0
 * @since 20190605
 */
public interface FileService extends FileUpload, FileFetcher {
    FileData getFile(String fileId);

    List<FileData> fileListOfGroup(String groupId);
}