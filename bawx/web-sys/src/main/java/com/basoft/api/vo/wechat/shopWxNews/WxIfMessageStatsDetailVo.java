package com.basoft.api.vo.wechat.shopWxNews;

import com.basoft.service.dto.wechat.shopWxNew.WxIfMessageStatsDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午5:15 2018/5/20
 **/
@Data
public class WxIfMessageStatsDetailVo {
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private String refDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private String statDate;

    private String refHour;

    private int intPageReadUser;//图文页（点击群发图文卡片进入的页面）的阅读人数

    private int intPageReadCount;//图文页的阅读次数

    private int oriPageReadUser;//原文页（点击图文页“阅读原文”进入的页面）的阅读人数，无原文页时此处数据为0

    private int oriPageReadCount;//原文页的阅读次数

    private int intPageFromSessionReadUser;//公众号会话阅读人数

    private int intPageFromSessionReadCount;//公众号会话阅读次数

    private int intPageFromFeedReadUser;//朋友圈阅读人数

    private int intPageFromFeedReadCount;//朋友圈阅读次数

    private Integer intPageFromHistMsgReadUser;//历史消息

    private Integer intPageFromHistMsgReadCount;//历史消息

    private Integer intPageFromKanyikanReadUser;//看一看

    private Integer intPageFromKanyikanReadCount;//看一看

    private Integer intPageFromSouyisouReadUser;//搜一搜

    private Integer intPageFromSouyisouReadCount;//搜一搜

    private int intPageFromFriendsReadUser;//好友转发阅读人数

    private int intPageFromFriendsReadCount;//好友转发阅读次数

    private int intPageFromOtherReadUser;// 其他场景阅读人数

    private int intPageFromOtherReadCount;//其他场景阅读次数

    private int addToFavUser;//收藏人数

    private int addToFavCount;//收藏次数
    private int shareUser;//分享人数
    private int shareCount;//分享次数

    public WxIfMessageStatsDetailVo(WxIfMessageStatsDetail entity){
        this.refDate = entity.getRefDate();
        this.statDate = entity.getStatDate();
        this.refHour = entity.getRefHour();
        this.intPageReadUser = entity.getIntPageReadUser();
        this.intPageReadCount = entity.getIntPageReadCount();
        this.oriPageReadUser = entity.getOriPageReadUser();
        this.oriPageReadCount = entity.getOriPageReadCount();
        this.intPageFromSessionReadUser = entity.getIntPageFromSessionReadUser();
        this.intPageFromSessionReadCount = entity.getIntPageFromSessionReadCount();
        this.intPageFromHistMsgReadUser = entity.getIntPageFromHistMsgReadUser();
        this.intPageFromHistMsgReadCount = entity.getIntPageFromHistMsgReadCount();
        this.intPageFromSouyisouReadUser = entity.getIntPageFromSouyisouReadUser();
        this.intPageFromSouyisouReadCount = entity.getIntPageFromSouyisouReadCount();
        this.intPageFromKanyikanReadUser = entity.getIntPageFromKanyikanReadUser();
        this.intPageFromKanyikanReadCount = entity.getIntPageFromKanyikanReadCount();
        this.intPageFromFeedReadUser = entity.getIntPageFromFeedReadUser();
        this.intPageFromFeedReadCount = entity.getIntPageFromFeedReadCount();
        this.intPageFromFriendsReadUser = entity.getIntPageFromFriendsReadUser();
        this.intPageFromFriendsReadCount = entity.getIntPageFromFriendsReadCount();
        this.intPageFromOtherReadUser = entity.getIntPageFromOtherReadUser();
        this.intPageFromOtherReadCount = entity.getIntPageFromOtherReadCount();
        this.addToFavUser = entity.getAddToFavUser();
        this.addToFavCount = entity.getAddToFavCount();
        this.shareUser = entity.getShareUser();
        this.shareCount = entity.getShareCount();
    }
}
