package com.basoft.service.entity.user;

import java.util.List;

public class UserDTO extends User {
	// 用户类型 S-超级管理员 A-管理员 N-普通员工
	private String userType;

	// 微信公众号列表
	private List<Long> shopList;

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<Long> getShopList() {
		return shopList;
	}

	public void setShopList(List<Long> shopList) {
		this.shopList = shopList;
	}
}