package com.basoft.core.ware.wechat.domain.msg;

/**
 * 回复音乐消息
 */
public class Music {
	private String toUserName = "";
	private String fromUserName = "";
	private String createTime = "";
	private String title = "";
	private String description = "";
	private String musicUrl = "";
	private String hqMusicUrl = "";
	private String thumbMediaId = "";
	// Title 否 音乐标题
	// Description 否 音乐描述
	// MusicUrl 否 音乐链接
	// HQMusicUrl 否 高质量音乐链接，WIFI环境优先使用该链接播放音乐
	// ThumbMediaId 是 缩略图的媒体id，通过上传多媒体文件，得到的id

	public static void main(String[] args) {
		System.out.println(new Music());
	}

	public Music() {
		super();
	}

	public Music(String toUserName, String fromUserName, String createTime, String thumbMediaId) {
		super();
		this.toUserName = toUserName;
		this.fromUserName = fromUserName;
		this.createTime = createTime;
		this.thumbMediaId = thumbMediaId;
	}

	@Override
	public String toString() {
		/*<xml>
			<ToUserName><![CDATA[toUser]]></ToUserName>
			<FromUserName><![CDATA[fromUser]]></FromUserName>
			<CreateTime>12345678</CreateTime>
			<MsgType><![CDATA[music]]></MsgType>
			<Music>
				<Title><![CDATA[TITLE]]></Title>
				<Description><![CDATA[DESCRIPTION]]></Description>
				<MusicUrl><![CDATA[MUSIC_Url]]></MusicUrl>
				<HQMusicUrl><![CDATA[HQ_MUSIC_Url]]></HQMusicUrl>
				<ThumbMediaId><![CDATA[media_id]]></ThumbMediaId>
			</Music>
		</xml>*/
		
		//	参数				是否必须		描述
		//	ToUserName			是	接收方帐号（收到的OpenID）
		//	FromUserName		是	开发者微信号
		//	CreateTime			是	消息创建时间 （整型）
		//	MsgType				是	music
		//	Title				否	音乐标题
		//	Description			否	音乐描述
		//	MusicUrl			否	音乐链接
		//	HQMusicUrl			否	高质量音乐链接，WIFI环境优先使用该链接播放音乐
		//	ThumbMediaId		是	缩略图的媒体id，通过上传多媒体文件，得到的id
	
		String format 	= "<xml>\n" 											
		            + 	"<ToUserName><![CDATA[%1$s]]></ToUserName>\n" 	 
		            + 	"<FromUserName><![CDATA[%2$s]]></FromUserName>\n" 
		            + 	"<CreateTime>%3$s</CreateTime>\n" 			 
		            + 	"<MsgType><![CDATA[music]]></MsgType>\n" 		 
		            + 	"<Music>\n" 		 
		            + 		"<Title><![CDATA[%4$s]]></Title>\n" 			 
		            + 		"<Description><![CDATA[%5$s]]></Description>\n" 			 
		            + 		"<MusicUrl><![CDATA[%6$s]]></MusicUrl>\n" 			 
		            + 		"<HQMusicUrl><![CDATA[%7$s]]></HQMusicUrl>\n" 			 
		            + 		"<ThumbMediaId><![CDATA[%8$s]]></ThumbMediaId>\n" 			 
		            + 	"</Music>\n" 		 
		            + "</xml>" ;
	
		return String.format(format, toUserName, fromUserName, createTime, title, description, musicUrl, hqMusicUrl,
				thumbMediaId);
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMusicUrl() {
		return musicUrl;
	}

	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}

	public String getHqMusicUrl() {
		return hqMusicUrl;
	}

	public void setHqMusicUrl(String hqMusicUrl) {
		this.hqMusicUrl = hqMusicUrl;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}
}