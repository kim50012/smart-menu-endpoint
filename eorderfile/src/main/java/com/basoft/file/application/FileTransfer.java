package com.basoft.file.application;

import java.util.Map;

public interface FileTransfer extends FileUpload,FileFetcher{

/*
    FileService.FileRef uploadFile(UploadFile file);
    List<FileService.FileRef> batchUpload(List<UploadFile> collect);
*/


    public interface FileTransferFactory{

        public static final String CONFIG_PROP = "fileServiceFactory";

        FileTransfer of(Map<String,Object> envMap);

    }
}
