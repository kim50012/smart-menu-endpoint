package com.basoft.core.ware.wechat.domain.user;

import com.basoft.core.ware.wechat.domain.WeixinReturn;

/**
 * 获取用户列表 - response
 */
public class UserListReturn extends WeixinReturn {

	/**
	 * 关注该公众账号的总用户数
	 */
	private int total;

	/**
	 * 拉取的OPENID个数，最大值为10000
	 */
	private int count;

	/**
	 * 列表数据，OPENID的列表
	 */
	private Data data;

	/**
	 * 拉取列表的后一个用户的OPENID
	 */
	private String next_openid;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getNext_openid() {
		return next_openid;
	}

	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}

	@Override
	public String toString() {
		return "UserListReturn [total=" + total + ", count=" + count + ", data=" + data + ", next_openid=" + next_openid
				+ ", getData()=" + getData() + ", errcode=" + getErrcode() + ", errmsg=" + getErrmsg() + "]";
	}
}