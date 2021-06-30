package com.basoft.service.param.wechat.shopWxNews;

import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsItemWithBLOBs;
import lombok.Data;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:预览form
 * @Date Created in 下午2:21 2018/5/10
 **/
@Data
public class PreviewNewsForm {

    private Long msgId;

    private String openId;

    List<ShopWxNewsItemWithBLOBs> newsItemList;

    private int isForever;
}
