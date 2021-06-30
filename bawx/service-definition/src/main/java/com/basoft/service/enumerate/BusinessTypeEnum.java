package com.basoft.service.enumerate;

public enum BusinessTypeEnum {
	USER(1),
	GROUP(2),
	SHOP(3),
	SHOP_WX_NEWS_HEAD(4),
	SHOP_WX_NEWS_ITEM(5),
	SHOP_WX_MESSAGE(6),
	SHOP_WX_MESSAGE_KEYWORD(7),
	SHOP_WX_MENU(8),
	SHOP_WX_FILE(9),
	MENU_MST_ID(10),
	MENU_MST(11),
	MENU_AUTH(12),
	GRADE_MST(13),
	SHOPFILE(14),
	WX_CUST(15),
	WX_RESPONSE_MESSAGE(16);

	private Integer code;

	BusinessTypeEnum(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return this.code;
	}

	public static BusinessTypeEnum valueOf(Integer value) {
		for (BusinessTypeEnum e : BusinessTypeEnum.values()) {
			if (e.getCode() == value) {
				return e;
			}
		}
		return null;
	}
}