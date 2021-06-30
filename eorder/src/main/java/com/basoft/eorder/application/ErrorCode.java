package com.basoft.eorder.application;

import org.apache.commons.lang3.StringUtils;

/**
 * ClassName: ErrorCode ErrorCode 异常枚举信息<br/>
 */
public enum ErrorCode {
	SYS_ERROR(100000, "系统繁忙，请稍后再试"),
	SYS_ERROR_EN_US(100000, "System is busy，please try again after a moment"),

	SYS_TIMEOUT(99999, "系统过期，请重新登录"),
	SYS_TIMEOUT_EN_US(99999, "System time out,please login again"),

	PARAM_INVALID(100001, "参数错误"),
	PARAM_INVALID_EN_US(100001, "Parameter Error"),

	PARAM_MISSING(100002, "参数缺失"),
	PARAM_MISSING_EN_US(100002, "Parameter lost"),

	SYS_EMPTY(100003, "无数据"),
	SYS_EMPTY_EN_US(100003, "No data"),
	
	SYS_UPLOAD_FAIL(100004, "上传失败"),
	SYS_UPLOAD_FAIL_EN_US(100003, "Upload failure"),

	SYS_DUPLICATE(100004, "重复定义"),
	SYS_DUPLICATE_EN_US(100004, "Duplicate define"),
	
	BIZ_EXCEPTION(400000, "业务异常"),
	BIZ_EXCEPTION_EN_US(400000, "业务异常"),
	BIZ_EXCEPTION_KO_KR(400000, "业务异常"),

	LOGIN_INVALID(200001, "登陆信息无效！"),
	LOGIN_ERROR(200002, "用户名或密码错误！"),
	LOGIN_USER_NULL(200003, "没有该用户！"),
	LOGIN_USER_EXIT(200004, "已有该用户！"),
	AGENT_LOGIN_USER_EXIT(200005, "创建代理商登录账号错误(原因:登录账号重复)！"),
	LOGIN_AGENT_USER_NULL(200006, "该账号关联的代理商信息不存在！"),

	STORE_NULL(300001,"该店铺不存在"),
	STORE_NULL_USER(300002,"无关联商户或关联商户已关闭"),
	STORE_MANAGER_EXIT(300003,"该账号已被占用"),
	STORE_LONGITUDE_WRONG(300004,"纬度有误"),
	STORE_LATITUDE_WRONG(300005,"经度有误"),
	STORE_PAYINFO_WRONG(300006,"该店铺不能支付"),


	STORE_TABLE_EXIT(400001,"该餐桌已存在"),
	STORE_TABLE_FULL(400002,"餐桌数量已达上限"),
	PRODUCT_IS_USE(500001,"有产品再使用此分类"),
	CATEGORY_HAS_CHILD(500002, "有子分类不能删除"),
	OPTION_HAS_CHILD(500003, "有子规格不能删除"),


	
	/*******时间模糊查询验证******/
	START_TIME_NULL(600001,"开始时间不能为空"),
	END_TIME_NULL(600002,"结束时间不能为空"),
	TIME_NULL(600003,"请选择开始或结束时间"),
	/*******时间模糊查询验证******/

	/** 微信相关 **/
	WECHAT_SNS_CODE_INVALID(800001, "微信授权编号错误"),
	WECHAT_RECEIVE_SGIN_NOT_VOILD(800002, "NOT A VALID SIGN"),

	/*产品评价相关*/
	WECHAT_REVIEW_EXIT(800003,"只能评论一次"),

	/*酒店库存相关*/
	INVENTHOTEL_NUMBER_WRONG(900001,"库存不能小于已使用"),
	INVENTHOTEL_INSUFFICIENT(900002,"您手慢了，该房间已被订完！"),
	INVENTHOTEL_UPDATE_FAILURE(900003,"库存更新失败！"),

	/*代理商相关 */
	AGENT_STORE_ISBIND(1000003,"该店铺已被绑定"),
	AGENT_TYPE_NULL(1000004,"代理商类型不能为空"),

	/*订单相关*/
	ORDER_COMPLETE_CANCELD(1100001,"已退款订单不能执行该操作"),

	/*零售业务*/
	RETAIL_DISCOUNT_END(1200001,"手慢了，您要购买商品的折扣活动结束啦，请重新下单吧！"),
	RETAIL_INVENT_INSUFFICIENT(1200002,"您要购买的商品库存不足！"),
	RETAIL_INVENT_UPDATE_FAILURE(1200003,"库存更新失败！"),
	RETAIL_CHANGESTATUS_NOT_ZERO(1200004,"订单状态已改变不能发货"),
	AUDIT_REFUND_AMOUNT_INVALID(1200005,"退款金额不合法，应该小于等于订单支付金额");


	private int code;

	private String msg;

	ErrorCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	/**
	 * 根据信息获取code 不存在返回-1
	 *
	 * @param msg
	 * @return
	 */
	public static int getCode(String msg) {
		for (ErrorCode errorCode : ErrorCode.values()) {
			if (StringUtils.equals(msg, errorCode.getMsg())) {
				return errorCode.getCode();
			}
		}
		return -1;
	}

	/**
	 * 根据code获取信息值 不存在返回null
	 *
	 * @param code
	 * @return
	 */
	public static String getMsg(int code) {
		for (ErrorCode errorCode : ErrorCode.values()) {
			if (code == errorCode.getCode()) {
				return errorCode.getMsg();
			}
		}
		return null;
	}

	public static ErrorCode getByCode(int code) {
		for (ErrorCode errorCode : ErrorCode.values()) {
			if (code == errorCode.getCode()) {
				return errorCode;
			}
		}
		return null;
	}
}