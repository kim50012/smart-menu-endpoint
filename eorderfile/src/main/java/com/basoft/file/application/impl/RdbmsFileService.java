/*
package com.basoft.file.application.impl;

import com.basoft.file.application.FileData;
import com.basoft.file.application.FileService;
import com.basoft.file.application.FileTransfer;
import com.basoft.file.application.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class RdbmsFileService implements FileService {


    private DataSource ds;
    private JdbcTemplate jdbcTemplate;
    private FileTransfer fileUploadService;

    RdbmsFileService(){}

    @Autowired
    public RdbmsFileService(DataSource ds,FileTransfer fus){
        this.ds = ds;
        this.fileUploadService = fus;
    }

    @Override
    public FileData getFile(String fileId) {

        final FileData.Builder builder = this.jdbcTemplate.query(
                "select * from file_mst where file_id = ?",
                new Object[]{fileId},
                new RowMapper<FileData.Builder>() {
                    @Override
                    public FileData.Builder mapRow(ResultSet resultSet, int i) throws SQLException {
                        return FileData.newBuilder()
                                .id(resultSet.getString("file_id"))
                                .name(resultSet.getString("file_name"))
                                .originalName(resultSet.getString("file_original_name"))
                                .contentsType(resultSet.getString("file_type"))
                                .fullUrl(resultSet.getString("file_url"));
                    }
                })
                .stream()
                .findFirst()
                .orElseGet(() -> null);

        return null;
    }

    @Override
    public FileRef uploadFile(UploadFile file) {

        String fileId = UUID.randomUUID().toString();
*/
/*
        String fullName =fullName(fileId,file.originalName());
        String fullFilePath = buildFullPath(fullName);
        *//*

        FileRef ref =  fileUploadService.uploadFile(file);
        //            file_id,file_name,file_sys_name,file_type,file_size,file_url,is_use,modified_dt,created_dt
        this.jdbcTemplate.update(
                "insert into file_mst (file_id,file_name,file_sys_name,file_original_name,file_type,file_size,file_url) values(?,?,?,?,?,?,?)",
                new Object[]{
                        fileId,
                        file.name(),
                        ref.fullName(),
                        file.originalName(),
                        file.type(),
                        file.contents().length,
                        ref.getFullPath()
                });

        return ref;

    }

    @Override
    public List<FileRef> batchUpload(List<UploadFile> collect) {
        return fileUploadService.batchUpload(collect);
    }
}
*/
