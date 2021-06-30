package com.basoft.file.application;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FileService extends FileUpload ,FileFetcher{
    FileData getFile(String fileId);

    List<FileData> fileListOfGroup(String groupId);

    public static class FileRef{
        private String id;
        private String fullPath;
        private Map<String,Serializable> props = new HashMap<>();
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

        @Override
        public String toString() {
            return "FileRef{" +
                    "id='" + id + '\'' +
                    ", fullPath='" + fullPath + '\'' +
                    ", props=" + props +
                    ", fullName='" + fullName + '\'' +
                    ", visitFullPath='" + visitFullPath + '\'' +
                    ", file=" + file +
                    '}';
        }
    }
}
