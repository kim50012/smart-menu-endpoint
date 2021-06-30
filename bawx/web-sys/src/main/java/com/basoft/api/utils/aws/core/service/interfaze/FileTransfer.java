package com.basoft.api.utils.aws.core.service.interfaze;

import com.basoft.api.utils.aws.core.service.interfaze.base.FileFetcher;
import com.basoft.api.utils.aws.core.service.interfaze.base.FileUpload;

import java.util.Map;

/**
 * AWS File Upload Transfer
 *
 * @author Mentor
 * @version 1.0
 * @since 20190605
 */
public interface FileTransfer extends FileUpload, FileFetcher {
    public interface FileTransferFactory {
        public static final String CONFIG_PROP = "fileServiceFactory";

        FileTransfer of(Map<String, Object> envMap);
    }
}
