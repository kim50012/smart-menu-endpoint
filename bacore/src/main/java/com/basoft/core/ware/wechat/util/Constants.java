package com.basoft.core.ware.wechat.util;

public class Constants {
	public static class LogType {
		public static final String WX_SERVER_IF = "WX_SERVER_IF"; // 与微信公众账号交互消息

		public static final String SESSION_PAGE = "SESSION_PAGE"; // 创建session

		public static final String SHARE_SETTIONG = "SHARE_SETTIONG";// 设置共享信息
	}
	
	public static class MsgType {
		public static final String EVENT = "event";

		public static final String TEXT = "text";

		public static final String IMAGE = "image";

		public static final String VOICE = "voice";

		public static final String VIDEO = "video";

		public static final String LOCATION = "location";

		public static final String LINK = "link";

		public static final String SHORTVIDEO = "shortvideo";
	}
	
	public static class Event {
		// 关注
		public static final String SUBSCRIBE = "subscribe";

		// 取消关注
		public static final String UNSUBSCRIBE = "unsubscribe";

		// 用户已关注时的事件推送
		public static final String SCAN = "SCAN";

		// 上报地理位置事件
		public static final String LOCATION = "LOCATION";

		// 自定义菜单事件-点击菜单拉取消息时的事件推送
		// 用户点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event 的结构给开发者（参考消息接口指南），
		// 并且带上按钮中开发者填写的key值，开发者可以通过自定义的key值与用户进行交互；
		public static final String CLICK = "CLICK";

		// 自定义菜单事件-点击菜单跳转链接时的事件推送(跳转URL)
		// 用户点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的网页URL，可与网页授权获取用户基本信息接口结合，获得用户基本信息。
		public static final String VIEW = "VIEW";

		// 自定义菜单事件-scancode_push：扫码推事件
		// 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL），且会将扫码的结果传给开发者，开发者可以下发消息。
		public static final String SCANCODE_PUSH = "scancode_push";

		// 自定义菜单事件-scancode_waitmsg：扫码推事件且弹出“消息接收中”提示框
		// 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后，将扫码的结果传给开发者，同时收起扫一扫工具，然后弹出“消息接收中”提示框，随后可能会收到开发者下发的消息。
		public static final String SCANCODE_WAITMSG = "scancode_waitmsg";

		/// 自定义菜单事件-pic_sysphoto：弹出系统拍照发图
		// 用户点击按钮后，微信客户端将调起系统相机，完成拍照操作后，会将拍摄的相片发送给开发者，并推送事件给开发者，同时收起系统相机，随后可能会收到开发者下发的消息。
		public static final String PIC_SYSPHOTO = "pic_sysphoto";

		// 自定义菜单事件-pic_photo_or_album：弹出拍照或者相册发图
		// 用户点击按钮后，微信客户端将弹出选择器供用户选择“拍照”或者“从手机相册选择”。用户选择后即走其他两种流程。
		public static final String PIC_PHOTO_OR_ALBUM = "pic_photo_or_album";

		// 自定义菜单事件-pic_weixin：弹出微信相册发图器
		// 用户点击按钮后，微信客户端将调起微信相册，完成选择操作后，将选择的相片发送给开发者的服务器，并推送事件给开发者，同时收起相册，随后可能会收到开发者下发的消息。
		public static final String PIC_WEIXIN = "pic_weixin";

		// 自定义菜单事件-location_select：弹出地理位置选择器
		// 用户点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，将选择的地理位置发送给开发者的服务器，同时收起位置选择工具，随后可能会收到开发者下发的消息
		public static final String LOCATION_SELECT = "location_select";
        
		/**
		 * 多客服会话控制-接入会话
		 */
		public static final String KF_CREATE_SESSION = "kf_create_session";

		/**
		 * 多客服会话控制-关闭会话
		 */
		public static final String KF_CLOSE_SESSION = "kf_close_session";

		/**
		 * 多客服会话控制-转接会话
		 */
		public static final String KF_SWITCH_SESSION = "kf_switch_session";

		/**
		 * 由于群发任务提交后，群发任务可能在一定时间后才完成，因此，群发接口调用时， 仅会给出群发任务是否提交成功的提示，若群发任务提交成功，则在群发任务结束时，
		 * 会向开发者在公众平台填写的开发者URL（callback URL）推送事件。
		 */
		public static final String MASSSENDJOBFINISH = "MASSSENDJOBFINISH";

		/**
		 * 在模版消息发送任务完成后，微信服务器会将是否送达成功作为通知，发送到开发者中心中填写的服务器配置地址中
		 */
		public static final String TEMPLATESENDJOBFINISH = "TEMPLATESENDJOBFINISH";
	}

	public static class UserSubscribeType {
		public static final int SUBSCRIBE = 1; // 表此用户关注该公众号

		public static final int UN_SUBSCRIBE = 0;// 表此用户没有关注该公众号
	}

	/**
	 * 二维码类型
	 */
	public static class QrcodeType {
		public static final String QR_SCENE = "QR_SCENE"; // 临时

		public static final String QR_LIMIT_SCENE = "QR_LIMIT_SCENE";// 永久
	}
	
	/**
	 * 菜单类型
	 * 注意：media_id，view_limited类型是专门给第三方平台旗下未微信认证（具体而言，是资质认证未通过）
	 * 的订阅号准备的事件类型，它们是没有事件推送的，能力相对受限，其他类型的公众号不必使用。
	 */
	public static class MenuType {
		// 共同类型
		/**
		 * <h3>view：跳转URL</h3>
		 * 用户点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的网页URL，可与网页授权获取用户基本信息接口结合，获得用户基本信息。
		 */
		public static final String VIEW = "view";

		// 在公众平台官网通过网站功能发布菜单类型
		/**
		 * <h3>text：返回文本</h3>
		 */
		public static final String TEXT = "text";

		/**
		 * <h3>img：返回图片</h3>
		 */
		public static final String IMG = "img";

		/**
		 * <h3>photo：</h3>
		 */
		public static final String PHOTO = "photo";

		/**
		 * <h3>video：视频</h3>
		 */
		public static final String VIDEO = "video";

		/**
		 * <h3>voice：语音</h3>
		 */
		public static final String VOICE = "voice";

		/**
		 * <h3>news：图文消息</h3>
		 */
		public static final String NEWS = "news";

		// 自定义菜单创建接口
		/**
		 * <h3>click：点击推事件</h3> 用户点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event
		 * 的结构给开发者（参考消息接口指南）， 并且带上按钮中开发者填写的key值，开发者可以通过自定义的key值与用户进行交互；
		 */
		public static final String CLICK = "click";

		/**
		 * <h3>scancode_push：扫码推事件</h3>
		 * 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL）， 且会将扫码的结果传给开发者，开发者可以下发消息。
		 */
		public static final String SACNCODE_PUSH = "scancode_push";

		/**
		 * <h3>scancode_waitmsg：扫码推事件且弹出“消息接收中”提示框</h3>
		 * 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后，将扫码的结果传给开发者，同时收起扫一扫工具，
		 * 然后弹出“消息接收中”提示框，随后可能会收到开发者下发的消息。
		 */
		public static final String SACNCODE_WAITMSG = "scancode_waitmsg";

		/**
		 * <h3>pic_sysphoto：弹出系统拍照发图</h3>
		 * 用户点击按钮后，微信客户端将调起系统相机，完成拍照操作后，会将拍摄的相片发送给开发者，并推送事件给开发者，
		 * 同时收起系统相机，随后可能会收到开发者下发的消息。
		 */
		public static final String PIC_SYSPHOTO = "pic_sysphoto";

		/**
		 * <h3>pic_photo_or_album：弹出拍照或者相册发图</h3>
		 * 用户点击按钮后，微信客户端将弹出选择器供用户选择“拍照”或者“从手机相册选择”。用户选择后即走其他两种流程。
		 */
		public static final String PIC_PHOTO_OR_ALBUM = "pic_photo_or_album";

		/**
		 * <h3>pic_weixin：弹出微信相册发图器</h3>
		 * 用户点击按钮后，微信客户端将调起微信相册，完成选择操作后，将选择的相片发送给开发者的服务器，并推送事件给开发者，
		 * 同时收起相册，随后可能会收到开发者下发的消息。
		 */
		public static final String PIC_WEIXIN = "pic_weixin";

		/**
		 * <h3>location_select：弹出地理位置选择器</h3>
		 * 用户点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，将选择的地理位置发送给开发者的服务器，
		 * 同时收起位置选择工具，随后可能会收到开发者下发的消息。
		 */
		public static final String LOCATION_SELECT = "location_select";

		/**
		 * <h3>media_id：下发消息（除文本消息）</h3>
		 * 用户点击media_id类型按钮后，微信服务器会将开发者填写的永久素材id对应的素材下发给用户，永久素材类型可以是图片、音频、视频、图文消息。
		 * 请注意：永久素材id必须是在“素材管理/新增永久素材”接口上传后获得的合法id。
		 */
		public static final String MEDIA_ID = "media_id";

		/**
		 * <h3>view_limited：跳转图文消息URL</h3>
		 * 用户点击view_limited类型按钮后，微信客户端将打开开发者在按钮中填写的永久素材id对应的图文消息URL，永久素材类型只支持图文消息。
		 * 请注意：永久素材id必须是在“素材管理/新增永久素材”接口上传后获得的合法id。
		 */
		public static final String VIEW_LIMITED = "view_limited";
    }
	
}
