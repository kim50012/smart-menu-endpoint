package com.basoft.api.vo.wechat.shopWxNews;

import com.basoft.service.dto.wechat.shopWxNew.NewsStatsDataDto;
import lombok.Data;

import java.util.Date;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午8:08 2018/5/15
 **/
@Data
public class NewsStatsDataVo {

    private Date refDate; //时间

    private String title;

    private int sendTargetCnt;//送达人数

    private int intPageReadUser; //图文页阅读人数;

    private int shareUser;//分享的人数


    public NewsStatsDataVo(NewsStatsDataDto dto){
        this.refDate = dto.getRefDate();
        this.title = dto.getTitle();
        this.sendTargetCnt = dto.getSendTargetCnt();
        this.intPageReadUser = dto.getIntPageReadUser();
        this.shareUser = dto.getShareUser();
    }

}
