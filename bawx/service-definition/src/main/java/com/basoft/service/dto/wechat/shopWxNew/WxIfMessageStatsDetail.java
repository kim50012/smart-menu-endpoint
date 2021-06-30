package com.basoft.service.dto.wechat.shopWxNew;

import lombok.Data;

@Data
public class WxIfMessageStatsDetail {
    private String refDate;

    private String refHour;

    private String msgid;

    private String title;

    private String statDate;

    private Integer targetUser;

    private Integer intPageReadUser;//图文阅读人数

    private Integer intPageReadCount;//图文阅读次数

    private Integer oriPageReadUser;//原文页阅读人数

    private Integer oriPageReadCount;//原文页阅读次数

    private Integer shareUser;//分享的人数

    private Integer shareCount;//分享的次数

    private Integer addToFavUser;//收藏的人数

    private Integer addToFavCount;//收藏的次数

    private Integer intPageFromSessionReadUser;//会话

    private Integer intPageFromSessionReadCount;

    private Integer intPageFromFeedReadUser;//朋友圈

    private Integer intPageFromFeedReadCount;//朋友圈

    private Integer intPageFromHistMsgReadUser;//历史消息

    private Integer intPageFromHistMsgReadCount;//历史消息

    private Integer intPageFromKanyikanReadUser;//看一看

    private Integer intPageFromKanyikanReadCount;//看一看

    private Integer intPageFromSouyisouReadUser;//搜一搜

    private Integer intPageFromSouyisouReadCount;//搜一搜

    private Integer intPageFromFriendsReadUser;//好友转发

    private Integer intPageFromFriendsReadCount;//好友转发

    private Integer intPageFromOtherReadUser;

    private Integer intPageFromOtherReadCount;

    private Integer feedShareFromSessionUser;

    private Integer feedShareFromSessionCnt;

    private Integer feedShareFromFeedUser;

    private Integer feedShareFromFeedCnt;

    private Integer feedShareFromOtherUser;

    private Integer feedShareFromOtherCnt;

    private Integer userSource;


}