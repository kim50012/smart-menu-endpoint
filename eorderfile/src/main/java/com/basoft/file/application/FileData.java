package com.basoft.file.application;

import java.util.Arrays;

public class FileData extends FileService.FileRef {
    private String name;
    private String originalName;
    private String type;
    private int size;
    private byte[] payload;
    private String keyUrl;

    public FileData(String fileId, String fullFilePath, String fullName) {
        super(fileId, fullFilePath, fullName, null);
    }

    public String name() {
        return name;
    }

    public String originalName() {
        return originalName;
    }

    public String type() {
        return type;
    }

    public int size() {
        return size;
    }

    public byte[] payload() {
        return payload;
    }

    public String getKeyUrl() {
        return keyUrl;
    }

    public static final Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String id;
        private String name;
        private String originalName;
        private String type;
        private int size;
        private String fullUrl;
        private byte[] payload;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder originalName(String originalName) {
            this.originalName = originalName;
            return this;
        }

        public Builder contentsType(String type) {
            this.type = type;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder fullUrl(String url) {
            this.fullUrl = url;
            return this;
        }

        public Builder payload(byte[] payload) {
            this.payload = payload;
            return this;
        }

        private String keyUrl;

        public Builder keyUrl(String key_url) {
            this.keyUrl = key_url;
            return this;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getOriginalName() {
            return originalName;
        }

        public String getType() {
            return type;
        }

        public String getFullUrl() {
            return fullUrl;
        }

        public FileData build() {
            FileData fd = new FileData(id, fullUrl, name);
            fd.name = this.name;
            fd.originalName = this.originalName;
            fd.size = this.size;
            fd.type = this.type;
            fd.payload = this.payload;
            fd.keyUrl = this.keyUrl;
            return fd;
        }
    }

    @Override
    public String toString() {
        return "FileData{" +
                "name='" + name + '\'' +
                ", originalName='" + originalName + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", payload=" + Arrays.toString(payload) +
                ", keyUrl='" + keyUrl + '\'' +
                '}';
    }
}
