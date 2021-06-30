package com.basoft.eorder.application.file;

import java.util.List;

public interface FileService {

    FileRef uploadFile(UploadFile file);
    List<FileRef> batchUpload(List<UploadFile> collect);
    FileData getFile(String fileId);

}
