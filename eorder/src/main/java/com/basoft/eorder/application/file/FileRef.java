package com.basoft.eorder.application.file;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FileRef{

    private String id;
    private String fullPath;
    private Map<String,Serializable> props = new HashMap<>();
    private String fullName;

    public FileRef(String fileId, String fullFilePath) {
        this.id = fileId;
        this.fullPath = fullFilePath;
    }
    public FileRef(String fileId, String fullFilePath, Map<String, Serializable> props, String fullName) {
        this.id = fileId;
        this.fullPath = fullFilePath;

        if(props != null && !props.isEmpty()){
            this.props.putAll(props);
        }

        this.fullName = fullName;


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

    public Map<String,Serializable> props(){
        return Collections.unmodifiableMap(props);

    }

    public Serializable prop(String key) {
        return this.props.get(key);
    }
}