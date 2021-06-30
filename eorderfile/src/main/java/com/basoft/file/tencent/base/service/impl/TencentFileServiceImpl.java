package com.basoft.file.tencent.base.service.impl;

import com.basoft.file.application.FileData;
import com.basoft.file.application.FileService;
import com.basoft.file.application.UploadFile;
import com.basoft.file.tencent.base.TencentTransfer;
import com.basoft.file.tencent.base.service.TencentFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * 腾讯云存储Service组件
 *
 * @author Mentor
 * @version 1.0
 * @since 20190724
 */
@Slf4j
public class TencentFileServiceImpl implements TencentFileService {
    private TencentTransfer tencentTransfer;

    private JdbcTemplate jdbcTemplate;

    public TencentFileServiceImpl(TencentTransfer tencentTransfer, DataSource dataSource) {
        this.tencentTransfer = tencentTransfer;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 上传文件（图片，视频等）到腾讯云存储COS，并且将上传信息保存到DB
     *
     * @param file
     * @return
     */
    @Override
    public FileService.FileRef uploadFile(UploadFile file) {
        log.debug("开始上传至腾讯云......");
        final FileService.FileRef fileRef = tencentTransfer.uploadFile(file);
        log.info("上传至腾讯云成功结束......");
        log.debug("上传至腾讯云成功信息：" + fileRef.toString());

        log.debug("将上传信息保存至数据库......");
        this.jdbcTemplate.update(
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
                });
        log.info("将上传信息保存至数据库结束......");
        return fileRef;
    }

    /**
     * 获取腾讯云文件
     *
     * @param fileId
     * @return
     */
    public FileData getFile(String fileId){
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

        if(builder != null){
            return builder.payload(fetch(builder.getName())).build();
        }
        return null;
    }

    /**
     * 从腾讯云存储获取文件内容byte数组
     *
     * @param fileName
     * @return
     */
    private byte[] fetch(String fileName) {
        log.info("TencentFileService[fetch]根据COS File Key Get Object from Tencent COS Storage!!!");
        byte[] fileContent = tencentTransfer.fetchObject(fileName);
        if (fileContent == null) {
            throw new IllegalArgumentException("NOT FOUND FILE BY:" + fileName);
        }
        log.info("TencentFileService[fetch]根据COS File Key Get Object from Tencent COS Storage FINISH!!!");
        return fileContent;
    }

    /**
     * 批量上传文件到腾讯云
     *
     * @param files
     * @return
     */
    public List<FileService.FileRef> batchUpload(List<UploadFile> files) {
        // 批量上传文件到腾讯云
        List<FileService.FileRef> fileRefs = tencentTransfer.batchUpload(files);

        // 保存上传的文件信息到file_mst表
        this.jdbcTemplate.batchUpdate(
                "insert into file_mst (file_id,file_name,file_sys_name,file_original_name,file_type,file_size,file_url,write_to_url,group_id,key_url) values(?,?,?,?,?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        FileService.FileRef ref = fileRefs.get(i);
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

    /**
     * 根据组ID获取组内文件信息列表
     *
     * @param groupId
     * @return
     */
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

    /**
     * 根据代理商ID查询代理商二维码文件信息
     *
     * @param groupId
     * @return
     */
    public FileData fileOfAgent(String groupId) {
        List<FileData> fileDateList = this.jdbcTemplate.query(
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
        if (fileDateList != null && fileDateList.size() > 0) {
            return fileDateList.get(0);
        } else {
            return null;
        }
    }
}
