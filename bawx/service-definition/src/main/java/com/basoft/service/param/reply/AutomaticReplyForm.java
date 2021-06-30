package com.basoft.service.param.reply;

import com.basoft.service.entity.wechat.reply.ShopMessageKeyword;
import com.basoft.service.param.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:05 2018/4/3
 **/

@Data
public class AutomaticReplyForm extends BaseForm{

    @NotNull(message = "messageId不能为空")
    private Long messageId;

    private Long shopId;

    private String msgGroup;

    @NotNull(message = "消息类型不能为空")
    private Integer sendFileType;

    private Integer sendType;

    private Integer sendTypeId;

    private Date sendTime;

    private Long materialFileId;

    private Long keywordId;

    private String msgTitle;

    private String sendMsgBody;

    private String materialFileWxId;
    
    private String callcenterId;

    private String kfNick;

    private List<ShopMessageKeyword> keywordList;


}
