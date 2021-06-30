package com.basoft.file.application.impl;

import com.basoft.file.application.FileFetcher;
import com.basoft.file.application.FileService;
import com.basoft.file.application.FileTransfer;
import com.basoft.file.application.UploadFile;

import java.io.File;
import java.util.List;

public abstract class FileTransferAdapter implements FileTransfer ,FileFetcher {
    public String fullName(String fname,String name){
        String prefix = name.substring(name.lastIndexOf("."));
        return new StringBuilder(fname).append(prefix).toString();
    }

    public String buildFullPath(String fullName,String subPath){
        String resFullNmae = fullName.startsWith(File.separator) ? fullName.substring(1,fullName.length()-1) : fullName;
        return new StringBuilder(subPath)
                .append(resFullNmae)
                .toString();
    }

    @Override
    public FileService.FileRef uploadFile(UploadFile file){
        throw new IllegalStateException("unsported");
    }

    @Override
    public List<FileService.FileRef> batchUpload(List<UploadFile> collect){
        throw new IllegalStateException("unsported");
    }

    @Override
    public byte[] fetch(String fileFullPath){
        throw new IllegalStateException("unsported");
    }
}
