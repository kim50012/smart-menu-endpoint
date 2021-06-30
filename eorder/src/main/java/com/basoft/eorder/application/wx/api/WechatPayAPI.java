package com.basoft.eorder.application.wx.api;

import com.basoft.eorder.application.wx.model.PayReq;
import com.basoft.eorder.application.wx.model.WxPayCloseResp;
import com.basoft.eorder.application.wx.model.WxPayQueryResp;
import com.basoft.eorder.application.wx.model.WxPayRefundResult;
import com.basoft.eorder.application.wx.model.WxPayResp;
import com.basoft.eorder.application.wx.model.WxPayResult;
import com.basoft.eorder.application.wx.model.WxResp;
import com.basoft.eorder.application.wx.sdk.WXPay;
import com.basoft.eorder.application.wx.sdk.WXPayConfigImpl;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.util.BeanUtil;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WechatPayAPI {
    private static Logger logger = LoggerFactory.getLogger(WechatPayAPI.class);

    // public final static String NOTIFY_URL = "http://bacommerce.co.kr/eorder/wechat/api/v1/order_pay";

    // private final static String APP_ID = "wx169f9463dac237ee";
    // private final static String MCH_ID = "1413386802";	//Vendor ID
    // private final static String SUB_APPID = "wx7202c9b1660cc48d";
    // private final static String SUB_MCH_ID = "272399448";
    // private final static String FEE_TYPE_KRW = "KRW";
    // public final static String KEY = "xmd1820gd3fjbj1837soeif5620fjtuf";

    public final static String MD5 = "MD5";
    public final static String HMACSHA256 = "HMAC-SHA256";
    private final static String FEE_TYPE_USD = "USD";
    private final static String MCC_CODE = "{\"goods_detail\":[{\"wxpay_goods_id\":\"4816\"}]}";

    public static WXPay getWXPay() {
        try {
            return new WXPay(WXPayConfigImpl.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("微信配置初始化错误", e);
        }
        return null;
    }

    private static class ApiInstance {
        private static WechatPayAPI holder = new WechatPayAPI();
    }

    public static WechatPayAPI instance() {
        return ApiInstance.holder;
    }

    /**
     * 统一下单
     *
     * @param orderNo  商户订单号
     * @param amount   金额
     * @param describe 商品描述
     * @param UserIp   用户端实际ip
     * @param attach   订单附加信息   (业务需要数据，自定义的)
     * @param openid   User Open ID
     * @return 返回整理的数据模型
     */
    public static WxPayResp unifiedOrder(Store store, String orderNo, String amount, String describe, String UserIp, String attach, String openid, String wechatNotifyUrl, String cert, Long transId) throws Exception {
        String appid = store.appid();
        String mchId = store.mchId();
        String subAppid = store.subAppid();
        String subMchid = store.subMchid();
        String apiKey = store.apiKey();
        String alliexTransId = (store.transidType() + transId + "");
        // String currency = store.currency();
        String mccCode = store.paymentMethod();

        PayReq req = new PayReq();
        req.setAppid(appid);
        req.setMch_id(mchId);
        req.setSub_appid(subAppid);
        req.setSub_mch_id(subMchid);
        req.setDevice_info(orderNo);
        req.setBody(describe);
        req.setDetail(mccCode);
        req.setAttach(attach);
        req.setOut_trade_no(alliexTransId);
        req.setFee_type(FEE_TYPE_USD);
        req.setTotal_fee(amount);
        req.setSpbill_create_ip(UserIp);
        // req.setTime_start("");
        // req.setTime_expire("");
        // req.setGoods_tag(describe);

        // 指定订单支付有效时间-start
        //req.setTime_start(DateUtil.getFormatStr(LocalDateTime.now()));
        //req.setTime_expire(DateUtil.getNowTimeOffset("MI",3,"yyyyMMddHHmmss"));
        // 指定订单支付有效时间-end

        req.setNotify_url(wechatNotifyUrl);
        req.setTrade_type("JSAPI");
        req.setProduct_id(orderNo);
        // req.setOpenid("");
        req.setSub_openid(openid);
        req.setSign_type(HMACSHA256);
        logger.info("微信支付请求参数：：" + req);
        return unifiedOrder(req, apiKey, mchId, cert);
    }

    public static WxPayResp unifiedOrder(PayReq req, String key, String mchId, String cert) throws Exception {
        Map<String, String> dataMap = (Map<String, String>) BeanUtil.apacheObjectToMap(req);
        return unifiedOrder(dataMap, key, mchId, cert);
    }

    /**
     * 统一下单
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static WxPayResp unifiedOrder(Map<String, String> data, String key, String mchId, String cert) throws Exception {
        // data.put("sub_appid", SUB_APPID);
        // data.put("sub_mch_id", SUB_MCH_ID);
        Map<String, String> respMap = getWXPay().unifiedOrder(data, key, mchId, cert);
        return BeanUtil.reflectMapToObject((Map) respMap, WxPayResp.class);
    }

    /**
     * 订单查询
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static WxPayQueryResp orderQuery(Store store, Map<String, String> data, String cert) throws Exception {
        String appid = store.appid();
        String mchId = store.mchId();
        String subAppid = store.subAppid();
        String subMchid = store.subMchid();
        String apiKey = store.apiKey();
        // String currency = store.currency();

        data.put("appid", appid);
        data.put("mch_id", mchId);
        data.put("sub_appid", subAppid);
        data.put("sub_mch_id", subMchid);
        data.put("out_trade_no", data.get("id"));
        data.put("sign_type", HMACSHA256);

        Map<String, String> respMap = getWXPay().orderQuery(data, apiKey, mchId, cert);
        return BeanUtil.reflectMapToObject((Map) respMap, WxPayQueryResp.class);
    }

    /**
     * 订单查询
     *
     * @param store
     * @param orderNo
     * @param amount
     * @param describe
     * @param UserIp
     * @param attach
     * @param refundId
     * @param cert
     * @return
     * @throws Exception
     */
    public static WxPayResult queryOrder(Store store, String orderNo, String amount, String describe, String UserIp, String attach, String refundId, String cert) throws Exception {
        String appid = store.appid();
        String mchId = store.mchId();
        String subAppid = store.subAppid();
        String subMchid = store.subMchid();
        String apiKey = store.apiKey();

        Map<String, String> data = Maps.newHashMap();
        data.put("appid", appid);
        data.put("mch_id", mchId);
        data.put("sub_appid", subAppid);
        data.put("sub_mch_id", subMchid);
        data.put("out_trade_no", orderNo);

        Map<String, String> respMap = getWXPay().orderQuery(data, apiKey, mchId, cert);
        return BeanUtil.reflectMapToObject((Map) respMap, WxPayResult.class);
    }

    /**
     * 订单查询
     *
     * @param orderId
     * @param cert
     * @return
     * @throws Exception
     */
    public static WxPayQueryResp orderQuery(String orderId, String cert) throws Exception {
        Map<String, String> data = Maps.newHashMap();
        data.put("out_trade_no", orderId);
        Store store = null;
        return orderQuery(store, data, cert);
    }

    /**
     * 撤销订单
     *
     * @param orderId
     * @param key
     * @param mchId
     * @param cert
     * @return
     * @throws Exception
     */
    public static WxResp reverse(String orderId, String key, String mchId, String cert) throws Exception {
        HashMap<String, String> data = Maps.newHashMap();
        data.put("out_trade_no", orderId);
        return reverse(data, key, mchId, cert);
    }

    /**
     * 撤销订单
     *
     * @param dataMap
     * @param key
     * @param mchId
     * @param cert
     * @return
     * @throws Exception
     */
    public static WxResp reverse(Map<String, String> dataMap, String key, String mchId, String cert) throws Exception {
        Map<String, String> respMap = getWXPay().reverse(dataMap, key, mchId, cert);
        return BeanUtil.reflectMapToObject((Map) respMap, WxResp.class);
    }

    /**
     * 关闭订单-不可用
     *
     * @param orderId
     * @param key
     * @param mchId
     * @param cert
     * @return
     * @throws Exception
     */
    public static WxPayCloseResp closeOrder(String orderId, String key, String mchId, String cert) throws Exception {
        Map<String, String> data = Maps.newHashMap();
        data.put("out_trade_no", orderId);
        Map<String, String> respMap = getWXPay().closeOrder(data, key, mchId, cert);
        return BeanUtil.reflectMapToObject((Map) respMap, WxPayCloseResp.class);
    }

    /**
     * 关闭订单-v2修正：20191230
     *
     * @param orderId
     * @param store
     * @param cert
     * @return
     * @throws Exception
     */
    public static WxPayCloseResp closeOrderRight(String orderId, Store store, String cert) throws Exception {
        Map<String, String> data = Maps.newHashMap();
        data.put("out_trade_no", orderId);
        data.put("appid", store.appid());
        data.put("mch_id", store.mchId());
        data.put("sub_appid", store.subAppid());
        data.put("sub_mch_id", store.subMchid());
        Map<String, String> respMap = getWXPay().closeOrder(data, store.apiKey(), store.mchId(), cert);
        return BeanUtil.reflectMapToObjectWithSuperClass((Map) respMap, WxPayCloseResp.class);
    }

    public static void main(String[] args) throws Exception{
        Map<String, String> respMap = new HashMap<String,String>();
        respMap.put("nonce_str", "asdfasdf");
        respMap.put("appid", "asdfaseeeeeeeeeeeeeee");
        respMap.put("sign", "1423669a33453adfasdf");
        respMap.put("return_msg", "ok");
        respMap.put("result_code", "SUCCESS");
        respMap.put("mch_id", "1423669");
        respMap.put("sub_mch_id", "23asdf");
        respMap.put("sub_appid", "23asdf3333");
        respMap.put("return_code", "SUCCESS");
        WxPayCloseResp wx =  BeanUtil.reflectMapToObjectWithSuperClass((Map) respMap, WxPayCloseResp.class);
        System.out.println(wx);
    }

    /**
     * 申请退款
     *
     * @param store
     * @param alliexTransId
     * @param amount
     * @param describe
     * @param UserIp
     * @param attach
     * @param openid
     * @param cert
     * @param cancelId
     * @return
     * @throws Exception
     */
    public static WxPayRefundResult refund(Store store, String alliexTransId, String amount, String describe, String UserIp, String attach, String openid, String cert, Long cancelId) throws Exception {
        //String transactionId, String outTradeNo, String outRefundNo, int totalFee, int refundFee

        String appid = store.appid();
        String mchId = store.mchId();
        String subAppid = store.subAppid();
        String subMchid = store.subMchid();
        String apiKey = store.apiKey();
        // String currency = store.currency();
        String outTradeNo = (store.transidType() + alliexTransId + "");
        String outRefundNo = (store.transidType() + cancelId + "");

        Map<String, String> data = Maps.newHashMap();
        data.put("appid", appid);
        data.put("mch_id", mchId);
        data.put("sub_appid", subAppid);
        data.put("sub_mch_id", subMchid);
        data.put("out_trade_no", outTradeNo);
        data.put("out_refund_no", outRefundNo);
        data.put("total_fee", amount);
        data.put("refund_fee", amount);
        data.put("refund_fee_type", FEE_TYPE_USD);
        data.put("refund_desc", describe);

        /*data.put("appid", appid);
        data.put("mch_id", mchId);
        data.put("sub_appid", subAppid);
        data.put("sub_mch_id", subMchid);
        data.put("out_trade_no", orderNo);
        data.put("out_refund_no", orderNo);
        data.put("total_fee", "20");
        data.put("refund_fee", "20");
        data.put("refund_fee_type", "USD");
        data.put("refund_desc", describe);*/

        return refund(data, apiKey, mchId, cert);
    }

    /**
     * 申请部分退款
     *
     * @param store
     * @param alliexTransId
     * @param amount
     * @param describe
     * @param UserIp
     * @param attach
     * @param openid
     * @param cert
     * @param cancelId
     * @return
     * @throws Exception
     * @created in 20200521
     */
    public static WxPayRefundResult partRefund(Store store, String alliexTransId, String amount
            , String refundAmount, String describe, String UserIp, String attach, String openid
            , String cert, Long cancelId) throws Exception {
        String appid = store.appid();

        String mchId = store.mchId();

        String subAppid = store.subAppid();

        String subMchid = store.subMchid();

        String apiKey = store.apiKey();

        // String currency = store.currency();

        /*
         * 商户订单号
         * 	out_trade_no
         * 二选一
         * 	String(32)
         * 	1217752501201407033233368018
         *  商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
         * transaction_id、out_trade_no二选一，如果同时存在优先级：transaction_id> out_trade_no
         */
        String outTradeNo = (store.transidType() + alliexTransId + "");

        /**
         * 商户退款单号
         * out_refund_no
         * 是
         * String(64)
         * 1217752501201407033233368018
         * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
         */
        String outRefundNo = (store.transidType() + cancelId + "");

        Map<String, String> data = Maps.newHashMap();
        data.put("appid", appid);
        data.put("mch_id", mchId);
        data.put("sub_appid", subAppid);
        data.put("sub_mch_id", subMchid);
        data.put("out_trade_no", outTradeNo);
        data.put("out_refund_no", outRefundNo);
        data.put("total_fee", amount);
        data.put("refund_fee", refundAmount);
        data.put("refund_fee_type", FEE_TYPE_USD);
        data.put("refund_desc", describe);

        /*data.put("appid", appid);
        data.put("mch_id", mchId);
        data.put("sub_appid", subAppid);
        data.put("sub_mch_id", subMchid);
        data.put("out_trade_no", orderNo);
        data.put("out_refund_no", orderNo);
        data.put("total_fee", "20");
        data.put("refund_fee", "20");
        data.put("refund_fee_type", "USD");
        data.put("refund_desc", describe);*/

        return refund(data, apiKey, mchId, cert);
    }

    /**
     * 申请退款
     *
     * @param dataMap
     * @param key
     * @param mchId
     * @param cert
     * @return
     * @throws Exception
     */
    public static WxPayRefundResult refund(Map<String, String> dataMap, String key, String mchId, String cert) throws Exception {
        Map<String, String> respMap = getWXPay().refund(dataMap, key, mchId, cert);
        return BeanUtil.reflectMapToObject((Map) respMap, WxPayRefundResult.class);
    }

    /**
     * 退款查询
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static WxPayRefundResult refundQuery(Map<String, String> data, String key, String mchId, String cert) throws Exception {
        Map<String, String> respMap = getWXPay().refundQuery(data, key, mchId, cert);
        return BeanUtil.reflectMapToObject((Map) respMap, WxPayRefundResult.class);
    }

    /**
     * 退款查询
     *
     * @param store
     * @param alliexTransId
     * @param amount
     * @param describe
     * @param UserIp
     * @param attach
     * @param refundId
     * @param cert
     * @param cancelId
     * @return
     * @throws Exception
     */
    public static WxPayRefundResult refundQuery(Store store, String alliexTransId, String amount, String describe, String UserIp, String attach, String refundId, String cert, Long cancelId) throws Exception {
        Map<String, String> data = Maps.newHashMap();

        String appid = store.appid();
        String mchId = store.mchId();
        String subAppid = store.subAppid();
        String subMchid = store.subMchid();
        String apiKey = store.apiKey();
        String outTradeNo = (store.transidType() + alliexTransId + "");
        String outRefundNo = (store.transidType() + cancelId + "");

        data.put("appid", appid);
        data.put("mch_id", mchId);
        data.put("sub_appid", subAppid);
        data.put("sub_mch_id", subMchid);
        data.put("out_trade_no", outTradeNo);
        data.put("out_refund_no", outRefundNo);
        data.put("refund_id", refundId);

        return refundQuery(data, apiKey, mchId, cert);
    }

    /**
     * 将map转成用户端用的封装体
     *
     * @param map map
     * @return 用户端用的封装体
     */
    private WxResp parsePayResp(Map<String, String> map) {
        WxResp response = new WxResp();
        response.setAppid(map.get("appid"));
        // response.setCode_url(map.get("code_url"));
        response.setMch_id(map.get("mch_id"));
        response.setNonce_str(map.get("nonce_str"));
        // response.setPrepay_id(map.get("prepay_id"));
        response.setResult_code(map.get("result_code"));
        response.setReturn_msg(map.get("return_msg"));
        response.setReturn_code(map.get("return_code"));
        // response.setPack("Sign=WXPay");
        //坑  todo 超级坑

        String substring = System.currentTimeMillis() / 1000 + "";
        // response.setTime(substring);
        //坑！！！！！！！！！！
        //sgin（签名），不是拿微信返回的sgin，而是自己再签一次，返回给客户端
        //注意：key不能是大写
        Map<String, String> params = new HashMap<>();
        // params.put("appid", APP_ID);
        // params.put("partnerid", MCH_ID);
        params.put("prepayid", map.get("prepay_id"));
        params.put("package", "Sign=WXPay");
        params.put("noncestr", map.get("nonce_str"));
        params.put("timestamp", substring);
        try {
            // String sgin = WXPayUtil.generateSignature(params, KEY);
            // response.setSign(sgin);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        response.setErr_code_des(map.get("err_code_des"));
        return response;
    }

    /**
     * 是否成功接收微信支付回调
     * 用于回复微信，否则微信回默认为商户后端没有收到回调
     *
     * @return
     */
    public static String returnWXPayVerifyMsg() {
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }

    /**
     * 是否成功接收微信支付回调 Error（签名核查错误）
     * 用于回复微信，否则微信回默认为商户后端没有收到回调
     *
     * @return
     */
    public static String returnWXPayVerifyMsgError() {
        return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[SIGN ERROR]]></return_msg></xml>";
    }

    /**
     * 是否成功接收微信支付回调 Error(数据存储处理错误)
     * 用于回复微信，否则微信回默认为商户后端没有收到回调
     *
     * @return
     */
    public static String returnWXPayVerifyMsgDealError() {
        return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[DATA DEAL ERROR]]></return_msg></xml>";
    }
}
