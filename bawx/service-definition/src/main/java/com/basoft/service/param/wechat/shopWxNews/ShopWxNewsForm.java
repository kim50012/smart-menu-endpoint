package com.basoft.service.param.wechat.shopWxNews;

import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsHead;
import com.basoft.service.entity.wechat.shopWxNews.ShopWxNewsItemWithBLOBs;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午11:08 2018/4/16
 **/

@Data
public class ShopWxNewsForm {
    @NotNull(message = "msgId不能为空")
    private Long msgId;

    private Long shopId;

    private String userId;

    private ShopWxNewsHead shopWxNewHead;

    private ShopWxNewsItemWithBLOBs shopWxNewsItemWithBLOBs;

    private List<ShopWxNewsItemWithBLOBs> shopWxNewsChild;
}
