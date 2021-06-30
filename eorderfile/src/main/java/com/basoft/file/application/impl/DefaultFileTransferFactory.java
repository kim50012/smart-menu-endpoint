/*
package com.basoft.file.application.impl;

import com.basoft.file.application.FileTransfer;

import java.util.Map;

public final class DefaultFileTransferFactory implements FileTransfer.FileTransferFactory {

    public static final String UPLOAD_ROOT_PATH_PROP = "uploadRootPath";
    public static final String SUB_PATH_PROP = "subPath";

    @Override
    public FileTransfer of(Map<String, Object> envMap) {

        String uploadRootPath = (String)envMap.get(UPLOAD_ROOT_PATH_PROP);
        String subPath = (String) envMap.get(SUB_PATH_PROP);
        DefaultFileTransfer dfd = new DefaultFileTransfer(uploadRootPath,subPath);

        return dfd;
    }





}
*/
