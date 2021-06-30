package com.basoft.api.utils.aws.core.service.impl;

import com.basoft.api.utils.aws.core.service.interfaze.FileTransfer;
import com.basoft.api.utils.aws.core.domain.FileData;
import com.basoft.api.utils.aws.core.domain.FileReference;
import com.basoft.api.utils.aws.core.domain.UploadFile;
import com.basoft.api.utils.aws.core.service.interfaze.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DefaultFileService implements FileService {
    @Autowired
    @Qualifier("awsS3FileSerivce")
    private FileTransfer fileTransfer;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    DefaultFileService() {
    }

    public DefaultFileService(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    public DefaultFileService(DataSource ds, FileTransfer fileTransfer) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.fileTransfer = fileTransfer;
    }

    @Override
    public FileReference uploadFile(UploadFile file) {
        final FileReference fileRef = fileTransfer.uploadFile(file);
        /*this.jdbcTemplate.update(
                "insert into file_mst (file_id,file_name,file_sys_name,file_original_name,file_type,file_size,file_url,write_to_url,group_id,key_url) values(?,?,?,?,?,?,?,?,?,?)",
                new Object[]{
                        fileRef.getId(),
                        file.name(),
                        fileRef.fullName(),
                        file.originalName(),
                        file.type(),
                        file.contents().length,
                        fileRef.getFullPath(),
                        fileRef.getVisitFullPath(),
                        fileRef.getUploadFile().groupId(),
                        fileRef.getUploadFile().getInFullUrl()
                });*/
        return fileRef;
    }

    @Override
    public List<FileReference> batchUpload(List<UploadFile> collect) {
        List<FileReference> fileRefs = this.fileTransfer.batchUpload(collect);
        this.jdbcTemplate.batchUpdate(
                "insert into file_mst (file_id,file_name,file_sys_name,file_original_name,file_type,file_size,file_url,write_to_url,group_id,key_url) values(?,?,?,?,?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        FileReference ref = fileRefs.get(i);
                        // UploadFile uploadFile = collect.get(i);
                        UploadFile uploadFile = ref.getUploadFile();
                        ps.setString(1, ref.getId());
                        ps.setString(2, uploadFile.name());
                        ps.setString(3, ref.fullName());
                        ps.setString(4, uploadFile.originalName());
                        ps.setString(5, uploadFile.type());
                        ps.setInt(6, uploadFile.contents().length);
                        ps.setString(7, ref.getVisitFullPath());
                        ps.setString(8, ref.getFullPath());
                        ps.setString(9, uploadFile.groupId());
                        ps.setString(10, uploadFile.getInFullUrl());
                    }

                    @Override
                    public int getBatchSize() {
                        return fileRefs.size();
                    }
                });
        return fileRefs;
    }

    protected String buildRealFullPath(String fullPath) {
        return fullPath;
    }

    @Override
    public FileData getFile(String fileId) {
        final FileData.Builder builder = this.jdbcTemplate.query(
                "select file_id,file_sys_name,file_original_name,file_type,file_url,key_url from file_mst where file_id = ?",
                new Object[]{fileId},
                new RowMapper<FileData.Builder>() {
                    @Override
                    public FileData.Builder mapRow(ResultSet resultSet, int i) throws SQLException {
                        return FileData.newBuilder()
                                .id(resultSet.getString("file_id"))
                                .name(resultSet.getString("file_sys_name"))
                                .originalName(resultSet.getString("file_original_name"))
                                .contentsType(resultSet.getString("file_type"))
                                .keyUrl(resultSet.getString("key_url"))
                                .fullUrl(resultSet.getString("file_url"));
                    }
                })
                .stream()
                .findFirst()
                .orElseGet(() -> null);

        if (builder != null) {
            return builder.payload(fetch(builder.getName())).build();
        }
        return null;
    }

    @Override
    public List<FileData> fileListOfGroup(String groupId) {
        return this.jdbcTemplate.query(
                "select file_id,file_sys_name,file_original_name,file_type,file_url,key_url from file_mst where group_id = ? ",
                new Object[]{groupId},
                new RowMapper<FileData>() {
                    @Override
                    public FileData mapRow(ResultSet resultSet, int i) throws SQLException {
                        return FileData.newBuilder()
                                .id(resultSet.getString("file_id"))
                                .name(resultSet.getString("file_sys_name"))
                                .originalName(resultSet.getString("file_original_name"))
                                .contentsType(resultSet.getString("file_type"))
                                .fullUrl(resultSet.getString("file_url"))
                                .keyUrl(resultSet.getString("key_url"))
                                .build();
                    }
                });

    }

    @Override
    public byte[] fetch(String fileFullPath) {
        return fileTransfer.fetch(fileFullPath);
    }
}
