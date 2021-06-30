package com.basoft.service.dto.customer;

import com.basoft.service.entity.wechat.reply.ShopWxMessageWithBLOBs;
import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:37 2018/5/7
 **/
@Data
public class CustMsgDto extends ShopWxMessageWithBLOBs{

    private String custName;

    private String sendFileTypeStr;
}
