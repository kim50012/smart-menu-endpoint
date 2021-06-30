package com.basoft.service.param.wechat.shopWxNews;

import lombok.Data;

import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午3:50 2018/5/9
 **/
@Data
public class SenNewsForm {
	private Long shopId;

	private Long msgId;

	// 客户等级
	private Long gradeId;

	// 1-所有 2-按客户等级 3-按选择的用户
	private Integer sendType;

	private List<String> custList;

	private String custSysId;
	
    // 1-NEWS 2-IMAGE 3-TEXT 4-VIDEO 5-VOICE
	private Integer sendFileType;
	
	// 文本
	private String sendText;
	
	// 图片、音频、视频
	private String sendMediaId;
}
