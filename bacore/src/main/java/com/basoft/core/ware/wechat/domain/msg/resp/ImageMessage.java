package com.basoft.core.ware.wechat.domain.msg.resp;

/**
 * 回复图片消息
 */
public class ImageMessage extends BaseMessage {
	// 图片
	private Image image;

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}