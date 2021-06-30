package com.basoft.api.vo.wechat.reply;

import com.basoft.core.ware.wechat.domain.customservice.GetkflistReturn;
import com.basoft.core.ware.wechat.domain.customservice.Kflist;
import com.basoft.service.dto.wechat.reply.AutomaticReplyDto;
import com.basoft.service.entity.wechat.reply.ShopMessageKeyword;
import com.basoft.service.entity.wechat.reply.ShopWxMessageWithBLOBs;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午4:33 2018/4/2
 **/
@Data
public class ReplyVO {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long messageId;

	private String keyword;

	private Integer sendFileType;

	private String sendFileTypeStr;

	private String msgTitle;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long materialFileId;

	private String sendMsgBody;

	private String fileUrl;// 图片地址

	private Date createdDt;

	private String materialFileWxId;

	private String callcenterId;

	private String kfNick;

	private List<ShopMessageKeyword> keywordList;

	private List<Kflist> kf_list = new ArrayList<Kflist>();
	
	// 查询回复列表
	public ReplyVO(AutomaticReplyDto dto) {
		this.shopId = dto.getShopId();
		this.messageId = dto.getMessageId();
		this.keyword = dto.getKeyword();
		this.sendFileType = dto.getSendFileType();
		this.sendFileTypeStr = dto.getSendFileTypeStr();
		this.msgTitle = dto.getMsgTitle();
		this.sendMsgBody = dto.getSendMsgBody();
		this.createdDt = dto.getCreatedDt();
		this.materialFileId = dto.getMaterialFileId();
		this.fileUrl = dto.getFileUrl();
		this.callcenterId = dto.getCallcenterId();
		this.kfNick = dto.getKfNick();
	}

	// 查询回显
	public ReplyVO(AutomaticReplyDto dto, List<ShopMessageKeyword> keywordList) {
		this.shopId = dto.getShopId();
		this.messageId = dto.getMessageId();
		this.keyword = dto.getKeyword();
		this.sendFileType = dto.getSendFileType();
		this.msgTitle = dto.getMsgTitle();
		this.sendMsgBody = dto.getSendMsgBody();
		this.createdDt = dto.getCreatedDt();
		this.fileUrl = dto.getFileUrl();
		this.materialFileId = dto.getMaterialFileId();
		this.materialFileWxId = dto.getMaterialFileWxId();
		this.callcenterId = dto.getCallcenterId();
		this.kfNick = dto.getKfNick();
		if (keywordList != null && keywordList.size() > 0)
			this.keywordList = keywordList;
	}

	public ReplyVO(ShopWxMessageWithBLOBs bloBs, String fullUrl) {
		this.shopId = bloBs.getShopId();
		this.messageId = bloBs.getMessageId();
		this.sendFileType = bloBs.getSendFileType();
		this.msgTitle = bloBs.getMsgTitle();
		this.sendMsgBody = bloBs.getSendMsgBody();
		this.createdDt = bloBs.getCreatedDt();
		this.fileUrl = fullUrl;
		this.materialFileId = bloBs.getMaterialFileId();
		this.materialFileWxId = bloBs.getMaterialFileWxId();
	}

	public ReplyVO() {
		this.shopId = null;
		this.messageId = 0L;
		this.sendFileType = null;
		this.msgTitle = "";
		this.sendMsgBody = null;
		this.createdDt = null;
		this.fileUrl = null;
		this.materialFileId = null;
		this.materialFileWxId = null;
	}

	public ReplyVO(GetkflistReturn dto) {
		this.kf_list = dto.getKf_list();
	}
}
