package com.basoft.api.vo.wechat.shopWxNews;

import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsItemWithBLOBs;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:04 2018/4/19
 **/

@Data
public class ShopWxNewsItemDetailVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long msgId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long newsId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long mfileId;

    private String mauthor;

    private String mtitle;

    private Byte mshowCoverPic;

    private String msourceUrl;

    private String mcontent;

    private String mdigest;

    private String mcontentwechat;

    public ShopWxNewsItemDetailVo(ShopWxNewsItemWithBLOBs bloBs){
        this.msgId = bloBs.getMsgId();
        this.newsId = bloBs.getNewsId();
        this.mfileId = bloBs.getMfileId();
        this.mauthor = bloBs.getMauthor();
        this.mtitle = bloBs.getMtitle();
        this.mshowCoverPic = bloBs.getMshowCoverPic();
        this.msourceUrl = bloBs.getMsourceUrl();
        this.mcontent = bloBs.getMcontent();
        this.mdigest = bloBs.getMdigest();
        this.mcontentwechat = bloBs.getMcontentwechat();
    }
}