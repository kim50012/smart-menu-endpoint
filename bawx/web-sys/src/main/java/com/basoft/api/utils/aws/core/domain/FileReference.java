package com.basoft.api.utils.aws.core.domain;

import com.basoft.api.utils.aws.core.service.interfaze.base.FileFetcher;
import com.basoft.api.utils.aws.core.service.interfaze.base.FileUpload;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AWS File Service。继承接口FileUpload, FileFetcher
 *
 * @author Mentor
 * @version 1.0
 * @since 20190605
 */
public class FileReference {
    private String id;
    private String fullPath;
    private Map<String, Serializable> props = new HashMap<>();
    private String fullName;
    private String visitFullPath;
    private UploadFile file;

    public FileReference(String fileId, String fullFilePath, Map<String, Serializable> props, String fullName, UploadFile file) {
        this(fileId, fullFilePath, fullName, fullFilePath, props, file);
    }

    public FileReference(String fileId, String fullFilePath, String fullName2, UploadFile file) {
        this(fileId, fullFilePath, fullName2, fullFilePath, null, file);
    }

    public FileReference(String fileId,
                         String writeToUrl,
                         String fullName,
                         String visitFullPath,
                         Map<String, Serializable> props, UploadFile file) {
        this.id = fileId;
        this.fullPath = writeToUrl;
        this.fullName = fullName;
        this.visitFullPath = visitFullPath;
        this.file = file;
        if (props != null) {
            this.props.putAll(props);
        }
    }

    public String fullName() {
        return this.fullName;
    }

    public String getId() {
        return id;
    }

    public String getFullPath() {
        return fullPath;
    }

    public String getVisitFullPath() {
        return this.visitFullPath;
    }

    public Map<String, Serializable> props() {
        return Collections.unmodifiableMap(props);
    }

    public Serializable prop(String key) {
        return this.props.get(key);
    }

    public UploadFile getUploadFile() {
        return this.file;
    }
}