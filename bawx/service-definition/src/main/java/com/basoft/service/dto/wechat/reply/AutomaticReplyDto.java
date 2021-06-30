package com.basoft.service.dto.wechat.reply;

import com.basoft.service.entity.wechat.reply.ShopWxMessageWithBLOBs;

import lombok.Data;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:29 2018/4/2
 **/
@Data
public class AutomaticReplyDto extends ShopWxMessageWithBLOBs {
	private String keyword;

	private String sendFileTypeStr;

	private String fileUrl;

}