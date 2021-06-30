package com.basoft.core.ware.wechat.exception;

public class WeixinPayUnifiedOrderException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public static final String NOAUTH 					= "NOAUTH";
	public static final String NOTENOUGH 				= "NOTENOUGH";
	public static final String ORDERPAID 				= "ORDERPAID";
	public static final String ORDERCLOSED 				= "ORDERCLOSED";
	public static final String SYSTEMERROR 				= "SYSTEMERROR";
	public static final String APPID_NOT_EXIST 			= "APPID_NOT_EXIST";
	public static final String MCHID_NOT_EXIST 			= "MCHID_NOT_EXIST";
	public static final String APPID_MCHID_NOT_MATCH 	= "APPID_MCHID_NOT_MATCH";
	public static final String LACK_PARAMS 				= "LACK_PARAMS";
	public static final String OUT_TRADE_NO_USED 		= "OUT_TRADE_NO_USED";
	public static final String SIGNERROR 				= "SIGNERROR";
	public static final String XML_FORMAT_ERROR 		= "XML_FORMAT_ERROR";
	public static final String REQUIRE_POST_METHOD 		= "REQUIRE_POST_METHOD";
	public static final String POST_DATA_EMPTY 			= "POST_DATA_EMPTY";
	public static final String NOT_UTF8 				= "NOT_UTF8";
	
	@SuppressWarnings("unused")
	private String code;

	private String reason;

	private String solution;

	private String getReason() {
		return reason;
	}

	private String getSolution() {
		return solution;
	}

	public WeixinPayUnifiedOrderException(String code) {
		super(code);
		this.code = code;
		setMessage(code);
	}

	@Override
	public String getMessage() {
		String messge = "生成微信预支付交易单失败!" + "<br>原因:" + getReason() + "<br>解决方案:" + getSolution();
		return messge;
	}

	private void setMessage(String code) {
		switch (code) {
		case NOAUTH:
			reason = "商户未开通此接口权限";
			solution = "请商户前往申请此接口权限";
			break;
		case NOTENOUGH:
			reason = "用户帐号余额不足";
			solution = "用户帐号余额不足，请用户充值或更换支付卡后再支付";
			break;
		case ORDERPAID:
			reason = "商户订单已支付，无需重复操作";
			solution = "商户订单已支付，无需更多操作";
			break;
		case ORDERCLOSED:
			reason = "当前订单已关闭，无法支付";
			solution = "当前订单已关闭，请重新下单";
			break;
		case SYSTEMERROR:
			reason = "系统超时";
			solution = "系统异常，请用相同参数重新调用";
			break;
		case APPID_NOT_EXIST:
			reason = "参数中缺少APPID";
			solution = "请检查APPID是否正确";
			break;
		case MCHID_NOT_EXIST:
			reason = "参数中缺少MCHID";
			solution = "请检查MCHID是否正确";
			break;
		case APPID_MCHID_NOT_MATCH:
			reason = "appid和mch_id不匹配";
			solution = "请确认appid和mch_id是否匹配";
			break;
		case LACK_PARAMS:
			reason = "缺少必要的请求参数";
			solution = "请检查参数是否齐全";
			break;
		case OUT_TRADE_NO_USED:
			reason = "同一笔交易不能多次提交";
			solution = "请核实商户订单号是否重复提交";
			break;
		case SIGNERROR:
			reason = "参数签名结果不正确";
			solution = "请检查签名参数和方法是否都符合签名算法要求";
			break;
		case XML_FORMAT_ERROR:
			reason = "XML格式错误";
			solution = "请检查XML参数格式是否正确";
			break;
		case REQUIRE_POST_METHOD:
			reason = "未使用post传递参数";
			solution = "请检查请求参数是否通过post方法提交";
			break;
		case POST_DATA_EMPTY:
			reason = "post数据不能为空";
			solution = "请检查post数据是否为空";
			break;
		case NOT_UTF8:
			reason = "未使用指定编码格式";
			solution = "请使用NOT_UTF8编码格式";
			break;
		default:
			reason = "";
			solution = "";
			break;
		}
	}
}