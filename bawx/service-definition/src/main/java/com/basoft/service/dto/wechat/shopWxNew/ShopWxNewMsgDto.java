package com.basoft.service.dto.wechat.shopWxNew;

import lombok.Data;

import java.util.Date;

/**
 * @Author:DongXifu
 * @Description: 素材查询列表
 * @Date Created in 下午4:48 2018/4/16
 **/

@Data
public class ShopWxNewMsgDto {

    private Integer msgId;

    private Integer newsId;

    private Integer mshowCoverPic;

    private String msgNm;

    private Date createdDt;

    private String mtitle;

    private String mauthor;

    private String mcontent;

    private String fullUrl;

    private String newsType;

    private String newsTypeStr;
}
