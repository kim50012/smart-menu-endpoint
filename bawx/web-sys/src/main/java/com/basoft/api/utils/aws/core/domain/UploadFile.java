package com.basoft.api.utils.aws.core.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * AWS Upload封装的文件上传对象
 *
 * @version 1.0
 * @since 20190605
 */
public class UploadFile implements Serializable {
    private static final long serialVersionUID = 3784609236859394265L;
    private String type;
    // private Long size;
    private String name;
    private String originalName;
    private byte[] contents;

    private Map<String, Serializable> props = new HashMap<>();
    private String groupId;
    private String inFullUrl;

    public String type() {
        return type;
    }

    public String name() {
        return name;
    }

    public String originalName() {
        return originalName;
    }

    public byte[] contents() {
        return contents;
    }

    public String getInFullUrl() {
        return inFullUrl;
    }

    public static Builder newBuild() {
        return new Builder();
    }

    public Serializable prop(String key) {
        return this.props.get(key);
    }

    public String groupId() {
        return groupId;
    }

    public Map<String, Serializable> props() {
        return Collections.unmodifiableMap(this.props);
    }

    public static final class Builder {
        private String type;
        private Long size;
        private String name;
        private String originalName;
        private byte[] contents;
        private String groupId;
        private Map<String, Serializable> props = new HashMap<>();

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder prop(String key, Serializable val) {
            this.props.put(key, val);
            return this;
        }

        public Builder payload(byte[] contents) {
            this.contents = contents;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder originalName(String oname) {
            this.originalName = oname;
            return this;
        }

        public Builder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        private String fullUrl;

        public Builder fullUrl(String fullUrl2) {
            this.fullUrl = fullUrl2;
            return this;
        }

        public UploadFile build() {
            UploadFile file = new UploadFile();
            file.inFullUrl = this.fullUrl;
            file.name = this.name;
            file.contents = this.contents;
            file.originalName = this.originalName;
            file.type = this.type;
            file.groupId = this.groupId;
            file.props.putAll(this.props);
            return file;
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "type='" + type + '\'' +
                    ", size=" + size +
                    ", name='" + name + '\'' +
                    ", originalName='" + originalName + '\'' +
                    ", contents=" + Arrays.toString(contents) +
                    ", groupId='" + groupId + '\'' +
                    ", props=" + props +
                    ", fullUrl='" + fullUrl + '\'' +
                    '}';
        }
    }
}