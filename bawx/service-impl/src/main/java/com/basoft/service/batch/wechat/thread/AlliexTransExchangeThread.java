package com.basoft.service.batch.wechat.thread;

import com.basoft.core.ware.wechat.util.XmlUtils;
import com.basoft.service.definition.wechatPay.alliexTrans.WechatPayAlliexTransService;
import com.basoft.service.util.RedisUtil;
import com.basoft.service.util.SocketForHttp;
import com.basoft.service.util.WechatPayAlliexUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 同步汇率线程
 */
public class AlliexTransExchangeThread implements Runnable {
    private final transient Log logger = LogFactory.getLog(this.getClass());

    private WechatPayAlliexTransService wechatPayAlliexTransService;

    private RedisUtil redisUtil;

    /**
     * 알리엑스에 환율 interface
     *
     * @param wechatPayAlliexTransService
     */
    public AlliexTransExchangeThread(WechatPayAlliexTransService wechatPayAlliexTransService,
            RedisUtil redisUtil) {
        this.wechatPayAlliexTransService = wechatPayAlliexTransService;
        this.redisUtil = redisUtil;
    }

    @Override
    public void run() {
        logger.info("++++++++++++++++ AlliexTransExchangeThread.run() ++++++++++++++++");
        Date ifStartDate = new Date(new Date().getTime() - 5 * 60 * 1000);
        logger.info(ifStartDate);

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            logger.info("++++++++++++++++ AlliexTransExchangeThread - STEP 0100 : Get I/F Data ++++++++++++++++");
            // 알리엑스 I/F 되는 대상 데이터 조회
            map.clear();

            /*
                1、获取汇率查询的参数信息，参数查询SQL（即selectTargetList_exchange_rate_if方法执行的SQL）如下：
                select
		        '0100' as version	#버전
		        , 'EXCHANGERATE' as processtype   #처리구분 - EXCHANGERATE
		        , a.merchant_id as merchantid	#가맹점 ID
		        , date_format(adddate(sysdate(), 0), '%Y%m%d') as querydate    #조회일시(YYYYYMMDD)
                , a.merchant_id #Alliex Merchant Name
                , a.merchant_nm #Alliex Merchant ID
                , a.gateway_pw  #Alliex Gateway Password
                , a.payment_method  #Alliex Payment Method
                , a.currency    #Alliex Currency
                , a.transid_type
                from    eorder.store a
                where   a.merchant_id = 'KR19030001'
                limit 1
                ;
                从商户表查询相关参数
             */
            Map<String, Object> alliExchange = wechatPayAlliexTransService.selectTargetList_exchange_rate_if(map);

            // 2、使用汇率查询参数调用韩亚银行汇率查询接口进行汇率查询及调用后处理工作
            trasToAlliex(alliExchange);

            logger.info("++++++++++++++++ AlliexTransExchangeThread - Update Batch Run Result ++++++++++++++++");
            // BATCH Header 결과 저장 (정상처리)
            map.clear();

            // 3、将汇率信息转入汇率正式表exchange_rate，以备使用
            wechatPayAlliexTransService.insertBatchResult_exchange_rate(map);

        } catch (Exception e) {
            logger.error("++++++++++++++++ AlliexTransExchangeThread - Error : " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 1、调用韩亚银行汇率查询接口进行汇率查询，同时将查询结果存入exchange_rate_if表
     * 2、加入缓存功能：将新查询到的汇率信息更新到缓存
     *
     * @param alliPaymentData
     */
    private void trasToAlliex(Map<String, Object> alliPaymentData) {

        logger.info("++++++++++++++++ AlliexTransExchangeThread - STEP 0200 : trasToAlliex() Start ++++++++++++++++");

        String version = alliPaymentData.get("version").toString();
        String processtype = alliPaymentData.get("processtype").toString();
        String merchantid = alliPaymentData.get("merchantid").toString();
        String querydate = alliPaymentData.get("querydate").toString();
        String transid_type = alliPaymentData.get("transid_type").toString();

        Map<String, Object> map = new HashMap<String, Object>();

        try {

            Map<String, Object> params = new LinkedHashMap<>();

            params.put("Version", version);
            params.put("ProcessType", processtype);
            params.put("MerchantId", merchantid);
            params.put("QueryDate", querydate);

            logger.info("++++++++++++++++ AlliexTransExchangeThread -STEP 0210 : Request paramData : \n" + params);

            String alliexIfUrl = WechatPayAlliexUtils.ALLIEX_EXCHANGE_URL;
            if ("BA".equals(transid_type)) {
                alliexIfUrl = WechatPayAlliexUtils.ALLIEX_EXCHANGE_URL_LIVE;
            }

            String postData = SocketForHttp.requestPost(alliexIfUrl, params);
//			String postData = HttpClientUtils.requestPost(WechatPayAlliexUtils.ALLIEX_PAYMENT_URL, postStringFormat);
            logger.info("++++++++++++++++ AlliexTransExchangeThread -STEP 0211 : Result postData : \n" + postData);


            //==============================================================================================================
            //==============================================================================================================
            // TEST BY DIKIM insertBatchResult_exchange_rate_if
            //==============================================================================================================
            //==============================================================================================================
			/*
			postData = 	"<RateResponse>\n" + 
						"<Version>0100</Version>\n" + 
						"<ProcessType>EXCHANGERATE</ProcessType>\n" + 
						"<MerchantId>M000000000</MerchantId> \n" + 
						"<QueryDate>20180101</QueryDate>\n" + 
						"<ResponseCode>0000</ResponseCode>\n" + 
						"<ResponseMsg>SUCCESS</ResponseMsg>\n" + 
						"<ConversionRate>6.3814</ConversionRate>\n" + 
						"<InvertedRate>0.15966923</InvertedRate>\n" + 
						"<UsdFxrate>0.0008975</UsdFxrate>\n" + 
						"</RateResponse>";
			*/
            //==============================================================================================================
            //==============================================================================================================
            // TEST BY DIKIM
            //==============================================================================================================
            //==============================================================================================================

            //Result data xml parsing
//			Map<String,String> requestMap = new Gson().fromJson(postData, Map.class);
            Map<String, String> requestMap = XmlUtils.parseXml2Map(new String(postData.getBytes("gbk"), "utf-8"));

            String r_version = requestMap.get("Version");
            String r_processtype = requestMap.get("ProcessType");
            String r_merchantid = requestMap.get("MerchantId");
            String r_querydate = requestMap.get("QueryDate");
            String r_responsecode = requestMap.get("ResponseCode");
            String r_responsemsg = requestMap.get("ResponseMsg");
            String r_conversionrate = requestMap.get("ConversionRate");
            String r_invertedrate = requestMap.get("InvertedRate");
            String r_usdfxrate = requestMap.get("UsdFxrate");

            logger.info("++++++++++++++++ AlliexTransExchangeThread -STEP 0220 : Result Data : ");
            logger.info("r_version===============" + r_version);
            logger.info("r_processtype===========" + r_processtype);
            logger.info("r_merchantid============" + r_merchantid);
            logger.info("r_querydate=============" + r_querydate);
            logger.info("r_responsecode==========" + r_responsecode);
            logger.info("r_responsemsg===========" + r_responsemsg);
            logger.info("r_conversionrate========" + r_conversionrate);
            logger.info("r_invertedrate==========" + r_invertedrate);
            logger.info("r_usdfxrate=============" + r_usdfxrate);


            logger.info("++++++++++++++++ AlliexTransExchangeThread -STEP 0230 : Save Result Data : ++++++++++++++++");
            // I/F 결과 저장 (정상처리)
            map.clear();
            map.put("status", 2);
            map.put("version", version);
            map.put("processtype", processtype);
            map.put("merchantid", merchantid);
            map.put("querydate", querydate);
            map.put("r_version", r_version);
            map.put("r_processtype", r_processtype);
            map.put("r_merchantid", r_merchantid);
            map.put("r_querydate", r_querydate);
            map.put("r_responsecode", r_responsecode);
            map.put("r_responsemsg", r_responsemsg);
            map.put("r_conversionrate", r_conversionrate);
            map.put("r_invertedrate", r_invertedrate);
            map.put("r_usdfxrate", r_usdfxrate);
            try {
                wechatPayAlliexTransService.insertBatchResult_exchange_rate_if(map);
            } catch (Exception e) {
                logger.error("++++++++++++++++ AlliexTransExchangeThread - STEP 0231 trasToAlliex Update Error : " + e.getMessage());
            }

            // 将汇率放入缓存-START 20200420
            try {
                if(StringUtils.isNotBlank(r_conversionrate)){
                    BigDecimal krwCnyRate = new BigDecimal(r_conversionrate);
                    BigDecimal krwUsdRate = new BigDecimal(r_usdfxrate);
                    // 新增或者更新汇率（韩币和人民币之间）
                    redisUtil.set("EXCHANGE_RATE_KRW_CNY_CACHE", krwCnyRate);
                    // 新增或者更新汇率（韩币和美元之间）
                    redisUtil.set("EXCHANGE_RATE_KRW_USD_CACHE", krwUsdRate);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("++++++++++++++++ AlliexTransExchangeThread - STEP EXCHAGE_RATE_KRW_CNY_REDIS Update Error : " + e.getMessage());
            }
            // 将汇率放入缓存-END 20200420
        } catch (Exception e) {
            logger.error("++++++++++++++++ AlliexTransExchangeThread - STEP 0231 trasToAlliex Error : " + e.getMessage());
            // I/F 결과 저장 (오류처리)
            map.clear();
            map.put("status", 9);
            map.put("version", version);
            map.put("processtype", processtype);
            map.put("merchantid", merchantid);
            map.put("querydate", querydate);
            map.put("r_responsemsg", "trasToAlliex Batch job Error");
            wechatPayAlliexTransService.insertBatchResult_exchange_rate_if(map);
            e.printStackTrace();
        }
    }


}
