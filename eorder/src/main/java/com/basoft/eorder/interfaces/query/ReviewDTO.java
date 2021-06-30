package com.basoft.eorder.interfaces.query;

import com.basoft.eorder.domain.model.StrReviewAttach;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description: 评论
 * @Date Created in 上午9:54 2019/5/14
 **/
@Data
public class ReviewDTO {
    public static final int REVCLASS_FIVE = 5;
    public static final int REVCLASS_ZERO = 0;

    private Long revId;

    private String platformId;//评价人平台ID。微信为OPENID

    private String nickname;//平台用户昵称。取自第三方平台，如微信、支付宝

    private String chathead;//平台用户头像URL。开发时，建议实施获取

    private String revPlatform;//第三方平台。WE-微信平台 ALI-阿里支付宝

    private Long orderId;//评价订单号

    private Long custId;//所属客户

    private Long storeId;//所属店铺

    private String storeName;//店铺名称

    private int revClass;//综合评价星级。终端用户可见。1-差 2-一般 3-还不错 4-很满意 5-强烈推荐

    private String revTag;//综合评价内容-所选标签。终端用户不可见，店铺管理类端可见。

    private String envClass;//1-差 2-一般 3-还不错 4-很满意 5-强烈推荐。终端用户不可见，店铺管理类端可见。

    private String prodClass;//1-差 2-一般 3-还不错 4-很满意 5-强烈推荐。终端用户不可见，店铺管理类端可见。

    private String serviceClass;//1-差 2-一般 3-还不错 4-很满意 5-强烈推荐。终端用户不可见，店铺管理类端可见。

    private Long averPrice;//人均菜价。此评价感觉无意义，人均菜价取决于客人点菜情况，不具备实际指导意义。

    private String revContent;//评价内容

    private String revContentCopie;//评价内容翻译

    private String isAnonymity;//是否匿名评价。1-是 0-否

    private Date revTime;//评价时间

    private String revStatus;//评价状态。1-评论人新发布；2-已审核，可以显示；3-精品评价/优质评价；4-隐藏状态（即删除状态）

    private String revReplyContent;//答复内容

    private String revReplyContentCopie;//回复内容翻译

    private String revReplier;//答复人

    private Date revReplyTime;//答复时间

    private Date modifiedDt;

    private String modifiedUserId;

    private List<StrReviewAttach> imgList;


}
