package com.basoft.api.utils.aws.core.service.impl.transfer.adapter;

import java.io.File;

public class FileTransferAdapter {
    public String fullName(String fname, String name) {
        String prefix = name.substring(name.lastIndexOf("."));
        return new StringBuilder(fname).append(prefix).toString();
    }

    public String buildFullPath(String fullName, String subPath) {
        String resFullNmae = fullName.startsWith(File.separator) ? fullName.substring(1, fullName.length() - 1) : fullName;
        return new StringBuilder(subPath)
                .append(resFullNmae)
                .toString();
    }
}
