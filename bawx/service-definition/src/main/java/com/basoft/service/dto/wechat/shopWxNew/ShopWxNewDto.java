package com.basoft.service.dto.wechat.shopWxNew;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description: 素材查询列表
 * @Date Created in 下午4:48 2018/4/16
 **/

@Data
public class ShopWxNewDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long msgId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long newsId;

    private Integer menuOpId;

    private Integer mshowCoverPic;

    private String msgNm;

    private String wxMsgId;

    private String wxMsgErr;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long wxMsgDataId;


    private Date createdDt;

    private String mtitle;

    private String mauthor;

    private String mcontent;

    private String fullUrl;

    private String newsType;

    private String newsTypeStr;

    private Integer readCnt;

    private String mdigest;

    private String msourceUrl;

    private  ShopWxNewDto dto;//头部


    List<ShopWxNewDto> ShopWxNewsItemChild;
}
