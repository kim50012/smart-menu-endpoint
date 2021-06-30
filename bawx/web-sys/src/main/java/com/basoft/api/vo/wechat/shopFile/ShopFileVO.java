package com.basoft.api.vo.wechat.shopFile;

import com.basoft.service.dto.shop.ShopFileDto;
import com.basoft.service.entity.shop.ShopFile;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:42 2018/4/10
 **/

@Data
public class ShopFileVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long fileId;

    private String fileNm;

    private String fileSysNm;

    private Byte fileType;

    private String fileTypeStr;

    private Long fileSize;

    private String fullUrl;

    private String mediaId;

    private Byte isUse;

    private String isUseStr;

    private Date createdDt;

    public ShopFileVO(ShopFileDto dto) {
        this.fileId = dto.getFileId();
        this.fileNm = dto.getFileNm();
        this.fileSysNm = dto.getFileSysNm();
        this.fileType = dto.getFileType();
        this.fileTypeStr = dto.getFileTypeStr();
        this.fileSize = dto.getFileSize();
        this.fullUrl = dto.getFullUrl();
        this.mediaId = dto.getMediaId();
        this.isUse = dto.getIsUse();
        this.isUseStr = dto.getIsUseStr();
        this.createdDt = dto.getCreatedDt();
    }

    public ShopFileVO(ShopFile entity) {
        this.fileId = entity.getFileId();
        this.fileNm = entity.getFileNm();
        this.fileSysNm = entity.getFileSysNm();
        this.fileType = entity.getFileType();
        this.fileSize = entity.getFileSize();
        this.fullUrl = entity.getFullUrl();
        this.mediaId = entity.getMediaId();
        this.isUse = entity.getIsUse();
        this.createdDt = entity.getCreatedDt();
    }
}
