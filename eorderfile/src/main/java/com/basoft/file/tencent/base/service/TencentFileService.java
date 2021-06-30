package com.basoft.file.tencent.base.service;

import com.basoft.file.application.FileData;
import com.basoft.file.application.FileService;
import com.basoft.file.application.UploadFile;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 腾讯云存储Service接口
 *
 * @author Mentor
 * @version 1.0
 * @since 20190724
 */
public interface TencentFileService {
    /**
     * 上传文件（图片，视频等）到腾讯云存储COS，并且将上传信息保存到DB
     *
     * @param file
     * @return
     */
    FileService.FileRef uploadFile(UploadFile file);

    /**
     * 获取腾讯云文件
     *
     * @param fileId
     * @return
     */
    FileData getFile(String fileId);

    /**
     * 批量上传文件到腾讯云
     *
     * @param files
     * @return
     */
    List<FileService.FileRef> batchUpload(List<UploadFile> files);

    List<FileData> fileListOfGroup(String groupId);

    /**
     * 根据代理商ID查询代理商二维码文件信息
     *
     * @param groupId
     * @return
     */
    FileData fileOfAgent(String groupId);

    public static class FileRef{
        private String id;
        private String fullPath;
        private Map<String, Serializable> props = new HashMap<>();
        private String fullName;
        private String visitFullPath;
        private UploadFile file;

        public FileRef(String fileId, String fullFilePath, Map<String, Serializable> props, String fullName,UploadFile file) {
            this(fileId,fullFilePath,fullName,fullFilePath,props,file);
        }

        public FileRef(String fileId, String fullFilePath,String fullName2,UploadFile file) {
            this(fileId,fullFilePath,fullName2,fullFilePath,null,file);
        }

        public FileRef(String fileId,
                       String writeToUrl,
                       String fullName,
                       String visitFullPath,
                       Map<String,Serializable> props,UploadFile file) {
            this.id = fileId;
            this.fullPath = writeToUrl;
            this.fullName = fullName;
            this.visitFullPath = visitFullPath;
            this.file = file;
            if(props != null){
                this.props.putAll(props);
            }
        }

        public String fullName(){
            return this.fullName;
        }

        public String getId() {
            return id;
        }

        public String getFullPath() {
            return fullPath;
        }

        public String getVisitFullPath(){
            return this.visitFullPath;
        }

        public Map<String,Serializable> props(){
            return Collections.unmodifiableMap(props);
        }

        public Serializable prop(String key) {
            return this.props.get(key);
        }

        public UploadFile getUploadFile() {
            return this.file;
        }
    }
}
