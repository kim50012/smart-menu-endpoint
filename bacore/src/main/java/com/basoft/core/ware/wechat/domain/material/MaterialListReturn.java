package com.basoft.core.ware.wechat.domain.material;

import java.util.List;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 获取素材列表-返回数据封装（图片、语音、视频）
 */
public class MaterialListReturn extends WeixinReturn {
	/**
	 * 素材列表
	 */
	private List<Material> item;

	/**
	 * 该类型的素材的总数
	 */
	private int total_count;

	/**
	 * 本次调用获取的素材的数量
	 */
	private int item_count;

	public List<Material> getItem() {
		return item;
	}

	public void setItem(List<Material> item) {
		this.item = item;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public int getItem_count() {
		return item_count;
	}

	public void setItem_count(int item_count) {
		this.item_count = item_count;
	}

	@Override
	public String toString() {
		return "MaterialListReturn [item=" + item + ", total_count=" + total_count + ", item_count=" + item_count + "]";
	}
}
