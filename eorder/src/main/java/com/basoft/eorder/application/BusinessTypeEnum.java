package com.basoft.eorder.application;

public enum BusinessTypeEnum {
	USER(1),
	STORE(2),
	BANNER(3),
	STORE_TABLE(4),
	PRODUCT(5),
	PRODUCT_SKU(6),
	CATEGORY(7),
	OPTION(8),
	PRODUCT_GROUP(9),
	ORDER(10),
	ORDER_ITEM(11),
	ORDER_PAY(12),
	ORDER_PAY_CANCEl(13),
	// 活动折扣
	ACTIVITY_DISCOUNT(14),
	ACTIVITY_DISCOUNT_DETAIL(15),
	// 评论
	REVIEW(16),REVIEW_ATTACH(17),

	ADVICE(18),ADVICE_ATTACH(19),
	// 收货区域
	SHIP_POINT(20),
	ADVERT(21),ADVERT_DETIL(22),
	STORE_OPTION(23),
	WX_CUST(24),
	INVENTORY_HOTEL(25),
	STORE_SETTLEMENT(26),
	AGENT(27),
	AGENT_STORE_MAP(28),
	AGENT_WX_MAP(29),
	AGENT_SETTLEMENT(30),
	STORE_ATTACH(31),
	STORE_DAILY_SETTLEMENT(32),
	BA_STORE_SETTLEMENT(33),
	BA_STORE_DAILY_SETTLEMENT(34),
	BASE_TOPIC(35),
	PRO_ALONE_STAND(36),
	PRO_ALONE_STAND_ITEM(37),
	PRO_ALONE_STAND_TEMP(38),
	INVENTORY_RETAIL(39),
	PRODUCT_ALONE_STANDARD_TEMPLATE(40),
	PRODUCT_ALONE_STANDARD_T_S(41),
	PRODUCT_ALONE_STANDARD__T_S_I(42),
	POST_STORE_SET(43),
	POST_STORE_SET_DETAIL(44),
	RETAIL_ORDER_SERVICE(45),
	null16(46),
	null17(47),
	null18(48),
	null19(49),
	null20(50); //用的时候修改名称



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