package com.basoft.api.vo.wechat.shopWxNews;

import com.basoft.service.dto.wechat.shopWxNew.ShopWxNewDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:48 2018/4/16
 **/

@Data
public class ShopWxNewsVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long msgId;

    private String msgNm;

    private Date createdDt;

    private String newsType;

    private String newsTypeStr;

    private String mtitle;

    private String fullUrl;

    private Integer mshowCoverPic;

    private String msourceUrl;

    private String mdigest;

    private Integer readCnt;



    public ShopWxNewsVo(ShopWxNewDto dto){
        this.msgId = dto.getMsgId();
        this.msgNm = dto.getMsgNm();
        this.createdDt = dto.getCreatedDt();
        this.newsType = dto.getNewsType();
        this.newsTypeStr = dto.getNewsTypeStr();
        this.mtitle = dto.getMtitle();
        this.fullUrl = dto.getFullUrl();
        this.mshowCoverPic = dto.getMshowCoverPic();
        this.msourceUrl = dto.getMsourceUrl();
        this.mdigest = dto.getMdigest();
        this.readCnt = dto.getReadCnt();
    }

}
