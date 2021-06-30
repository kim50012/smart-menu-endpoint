package com.basoft.core.ware.wechat.domain.msg;

/**
 * 回复图片消息
 */
public class Image {
	private Long id;
	private Integer fileId;
	private String sysId = "";
	private String msgId = "";
	private String toUserName = "";
	private String fromUserName = "";
	private String createTime = "";
	private String msgType = "";
	private String mediaId = "";
	private String picUrl = "";

	public Image() {
		super();
	}

	public Image(String toUserName, String fromUserName, String createTime, String mediaId) {
		super();
		this.toUserName = toUserName;
		this.fromUserName = fromUserName;
		this.createTime = createTime;
		this.mediaId = mediaId;
	}

	/**
	 * @param sysId
	 * @param msgId
	 * @param fromUserName
	 * @param toUserName
	 * @param createTime
	 * @param msgType
	 * @param mediaId
	 * @param picUrl
	 */
	public Image(String sysId, String msgId, String fromUserName, String toUserName, String createTime, String msgType,
			String mediaId, String picUrl) {
		super();
		this.sysId = sysId;
		this.msgId = msgId;
		this.fromUserName = fromUserName;
		this.toUserName = toUserName;
		this.createTime = createTime;
		this.msgType = msgType;
		this.mediaId = mediaId;
		this.picUrl = picUrl;
	}

	public static void main(String[] args) {
		System.out.println(new Image());
	}

	@Override
	public String toString() {
		 /*<xml>
		 <ToUserName><![CDATA[toUser]]></ToUserName>
		 <FromUserName><![CDATA[fromUser]]></FromUserName>
		 <CreateTime>12345678</CreateTime>
		 <MsgType><![CDATA[image]]></MsgType>
		 <Image>
		 <MediaId><![CDATA[media_id]]></MediaId>
		 </Image>
		 </xml>*/
		// 参数 是否必须 描述
		// ToUserName 是 接收方帐号（收到的OpenID）
		// FromUserName 是 开发者微信号
		// CreateTime 是 消息创建时间 （整型）
		// MsgType 是 image
		// MediaId 是 通过上传多媒体文件，得到的id。

		String format = "<xml>\n" + "<ToUserName><![CDATA[%1$s]]></ToUserName>\n"
				+ "<FromUserName><![CDATA[%2$s]]></FromUserName>\n" + "<CreateTime>%3$s</CreateTime>\n"
				+ "<MsgType><![CDATA[image]]></MsgType>\n" + "<Image>\n" + "<MediaId><![CDATA[%4$s]]></MediaId>\n"
				+ "</Image>\n" + "</xml>";

		return String.format(format, toUserName, fromUserName, createTime, mediaId);
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
}