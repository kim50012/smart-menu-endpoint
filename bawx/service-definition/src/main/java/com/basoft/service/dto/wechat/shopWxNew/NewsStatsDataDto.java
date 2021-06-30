package com.basoft.service.dto.wechat.shopWxNew;

import lombok.Data;

import java.util.Date;

/**
 * @Author:DongXifu
 * @Description: 素材发送统计
 * @Date Created in 下午6:57 2018/5/15
 **/
@Data
public class NewsStatsDataDto {

    private Date refDate;

    private String title;

    private Long msgId;

    private Long newsId;

    private int sendTargetCnt;//送达总人数

    private int intPageReadUser; //图文页阅读总人数;

    private int intPageReadCount; //图文页的阅读次数;

    private int oriPageReadUser;//原文页阅读总人数

    private int oriPageReadCount;//原文页的阅读次数

    private int shareUser;//分享的人数

    private int shareCount;//分享的次数

    private int addToFavUser;//收藏的人数

    private int addToFavCount;//收藏的次数

}
