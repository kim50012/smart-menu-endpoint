package com.basoft.api.utils.tencent.service.impl;

import com.basoft.api.utils.aws.core.domain.FileReference;
import com.basoft.api.utils.aws.core.domain.UploadFile;
import com.basoft.api.utils.tencent.service.TencentFileService;
import com.basoft.api.utils.tencent.service.base.core.TencentTransfer;
import lombok.extern.slf4j.Slf4j;

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

    public TencentFileServiceImpl(TencentTransfer tencentTransfer) {
        this.tencentTransfer = tencentTransfer;
    }

    @Override
    public FileReference uploadFile(UploadFile file) {
        log.debug("开始上传至腾讯云......");
        final FileReference fileRef = tencentTransfer.uploadFile(file);
        log.info("上传至腾讯云成功结束......");
        log.debug("上传至腾讯云成功信息：" + fileRef.toString());

        /*log.debug("将上传信息保存至数据库......");
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
        log.info("将上传信息保存至数据库结束......");*/
        return fileRef;
    }
}
