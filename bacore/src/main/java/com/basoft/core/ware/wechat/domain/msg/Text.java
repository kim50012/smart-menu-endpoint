package com.basoft.core.ware.wechat.domain.msg;

/**
 * 回复文本消息
 */
public class Text {
	private Long id;
	private String sysId = "";
	private String msgId = "";
	private String fromUserName = "";
	private String toUserName = "";
	private String createTime = "";
	private String msgType = "";
	private String content = "";

	public Text() {
		super();
	}

	/**
	 * @param sysId
	 * @param msgId
	 * @param fromUserName
	 * @param toUserName
	 * @param createTime
	 * @param msgType
	 * @param content
	 * @param creataDate
	 */
	public Text(String sysId, String msgId, String fromUserName, String toUserName, String createTime, String msgType,
			String content) {
		super();
		this.sysId = sysId;
		this.msgId = msgId;
		this.fromUserName = fromUserName;
		this.toUserName = toUserName;
		this.createTime = createTime;
		this.msgType = msgType;
		this.content = content;
	}

	public static void main(String[] args) {
		System.out.println(new Text());
	}

	@Override
	public String toString() {
		/*<xml>
		<ToUserName><![CDATA[toUser]]></ToUserName>
		<FromUserName><![CDATA[fromUser]]></FromUserName>
		<CreateTime>12345678</CreateTime>
		<MsgType><![CDATA[text]]></MsgType>
		<Content><![CDATA[你好]]></Content>
		</xml>*/
		// 参数 是否必须 描述
		// ToUserName 是 接收方帐号（收到的OpenID）
		// FromUserName 是 开发者微信号
		// CreateTime 是 消息创建时间 （整型）
		// MsgType 是 text
		// Content 是 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
		
		 String format 	= "<xml>\n" 											
			            + 	"<ToUserName><![CDATA[%1$s]]></ToUserName>\n" 	 
			            + 	"<FromUserName><![CDATA[%2$s]]></FromUserName>\n" 
			            + 	"<CreateTime>%3$s</CreateTime>\n" 			 
			            + 	"<MsgType><![CDATA[text]]></MsgType>\n" 		 
			            + 	"<Content><![CDATA[%4$s]]></Content>\n" 			 
			            + "</xml>" ;
		return String.format( format,toUserName, fromUserName,  createTime, content);
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}