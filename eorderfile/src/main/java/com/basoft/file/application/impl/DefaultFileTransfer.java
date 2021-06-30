package com.basoft.file.application.impl;

import com.basoft.file.application.FileTransfer;
import com.basoft.file.application.UploadFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.basoft.file.application.FileService.FileRef;

public class DefaultFileTransfer extends FileTransferAdapter implements FileTransfer {


    private String uploadPath;
    private String visitRoot;


    DefaultFileTransfer(){}

    public DefaultFileTransfer(String uploadPath,String visitRoot){
        this.uploadPath = uploadPath;
        this.visitRoot = visitRoot;
    }



    protected void writeFile(String fullFilePath,byte[] payload){

        try {
            String fileFullPath = buildRealFullPath(fullFilePath);
            Files.write(Paths.get(fileFullPath),payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public FileRef uploadFile(UploadFile file) {

        String fileId = UUID.randomUUID().toString();
        String fullName =fullName(fileId,file.originalName());
//        String fullFilePath = buildFullPath(fullName,this.subPath);
        String fullFilePath = new StringBuilder(this.visitRoot).append(fullName).toString();

        writeFile(fullName,file.contents());
        return new FileRef(fileId,fullFilePath,fullName,file);
    }

    FileRef toFileRef(UploadFile file){

        String fileId = UUID.randomUUID().toString();
        String fullName =fullName(fileId,file.originalName());
        String fullFilePath = buildFullPath(fullName,this.visitRoot);
        return new FileRef(fileId,fullFilePath,file.props(),fullName,file);
    }

    @Override
    public List<FileRef> batchUpload(List<UploadFile> collect) {

        List<FileRef>  fileRefs = new ArrayList<>(collect.size());
        for(UploadFile file:collect){
            final FileRef fileRef = toFileRef(file);
            writeFile(fileRef.getFullPath(),file.contents());//write to disk
            fileRefs.add(fileRef);
        }
        return fileRefs;
    }


    @Override
    public byte[] fetch(String fileFullPath) {

        String resFilePath = buildRealFullPath(fileFullPath);

        Path file = Paths.get(resFilePath);
        try {
            return Files.readAllBytes(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    public static String getRootPath(String inRootPath){
        String[] sp = inRootPath.split(File.separator);
        return new StringBuilder(File.separator)+sp[sp.length-1];
    }

    public static String getSubPath(String spec,String inRootPath){
        String[] sp = inRootPath.split(File.separator);
        StringBuilder sbb = new StringBuilder();
        int _startIndex = 0;
        boolean _start = false;
        for(int i = 0;i<sp.length;i++){
            String str = sp[i];
            if(spec.equalsIgnoreCase(str) && !_start){
                _start = true;
                _startIndex=i+1;
            }
            if(i >= _startIndex && _start){
                sbb = sbb.append(File.separator).append(str);
            }

        }
        return sbb.toString();
    }






    public final String buildRealFullPath(String fullPath){
        StringBuilder fileFullPath =  new StringBuilder(this.uploadPath);
        String[] strArray = fullPath.split(File.separator);
        for(int i = 0;i < strArray.length;i++){
            fileFullPath = fileFullPath.append(File.separator).append(strArray[i]);

        }
        return fileFullPath.toString();

    }


    public final static class DefaultFileTransferFactory implements FileTransfer.FileTransferFactory {

        public static final String UPLOAD_ROOT_PATH_PROP = "uploadRootPath";
        public static final String SUB_PATH_PROP = "visitPath";

        @Override
        public FileTransfer of(Map<String, Object> envMap) {

            String uploadRootPath = (String)envMap.get(UPLOAD_ROOT_PATH_PROP);
            String subPath = (String) envMap.get(SUB_PATH_PROP);
            DefaultFileTransfer dfd = new DefaultFileTransfer(uploadRootPath,subPath);

            return dfd;
        }





    }

}
