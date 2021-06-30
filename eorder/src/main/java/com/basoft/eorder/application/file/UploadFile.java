package com.basoft.eorder.application.file;

import com.google.common.collect.ImmutableMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UploadFile implements java.io.Serializable{


    private static final long serialVersionUID = 3784609236859394265L;
    private String type;
//    private Long size;
    private String name;
    private String originalName;
    private byte[] contents;

    private Map<String,Serializable> props = new HashMap<>();


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

    public static Builder newBuild() {
        return new Builder();
    }

    public Serializable prop(String key){
        return this.props.get(key);
    }

    public Map<String,Serializable> props(){
        return ImmutableMap.copyOf(this.props);
    }


    public static final class Builder{

        private String type;
        private Long size;
        private String name;
        private String originalName;
        private byte[] contents;
        private Map<String,Serializable> props = new HashMap<>();

        public Builder type(String type){

            this.type = type;
            return this;
        }


        public Builder prop(String key,Serializable val){
            this.props.put(key,val);
            return this;
        }

        public Builder payload(byte[] contents){
            this.contents = contents;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }


        public Builder originalName(String oname){
            this.originalName = oname;
            return this;
        }

        public UploadFile build(){

            UploadFile file = new UploadFile();
            file.name = this.name;
            file.contents = this.contents;
            file.originalName = this.originalName;
            file.type = this.type;
            file.props.putAll(this.props);
            return file;

        }

    }
}
