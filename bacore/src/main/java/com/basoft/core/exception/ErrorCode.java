package com.basoft.core.exception;

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
	LOGIN_ERROR(200001, "用户名或密码错误！"),

	FOLLOW_ERROR(300001, "关注不能设为关键字"),

	/*********start****素材保存验证***********/
	MTITLE_NOT_NULL(400001, "标题不能为空"),
	MCONTENT_NOT_NULL(400001, "内容不能为空"),
	MFILE_ID_NOT_NULL(400001, "图片不能为空"),
	/*********end****素材保存验证***********/

	/*****start***发送素材验证******/
	CUST_SYSID_LIST_NULL(500001,"请选择用户"),
    GRADEID_NOT_NULL(500002,"请选择等级"),
	MSG_SEND_TYPE_NULL(500003,"请选择类型"),
	SEND_USER_LSIT(500004,"发送对象列表为空!"),
	/*****end***发送素材验证********/
	
	/*******时间模糊查询验证******/
	START_TIME_NULL(600001,"开始时间不能为空"),
	END_TIME_NULL(600002,"结束时间不能为空"),
	TIME_NULL(600003,"请选择开始或结束时间");
	/*******时间模糊查询验证******/

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