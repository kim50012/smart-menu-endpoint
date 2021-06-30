package com.basoft.file.application;

import java.util.List;

public interface FileUpload {
    FileService.FileRef uploadFile(UploadFile file);

    List<FileService.FileRef> batchUpload(List<UploadFile> collect);
}
