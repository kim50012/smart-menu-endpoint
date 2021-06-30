package com.basoft.eorder.interfaces.controller.h5;

import com.basoft.eorder.application.*;
import com.basoft.eorder.application.framework.*;
import com.basoft.eorder.application.wx.api.WechatAPI;
import com.basoft.eorder.application.wx.api.WechatPayAPI;
import com.basoft.eorder.application.wx.http.HttpClientUtils;
import com.basoft.eorder.application.wx.model.*;
import com.basoft.eorder.application.wx.model.wxuser.WXUser;
import com.basoft.eorder.application.wx.sdk.WXPay;
import com.basoft.eorder.application.wx.sdk.WXPayUtil;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.*;
import com.basoft.eorder.domain.model.*;
import com.basoft.eorder.interfaces.command.BatchUpAdviceStatus;
import com.basoft.eorder.interfaces.command.BatchUpReviewStatus;
import com.basoft.eorder.interfaces.command.TransReview;
import com.basoft.eorder.interfaces.command.advice.SaveAdvice;
import com.basoft.eorder.interfaces.command.review.SaveReview;
import com.basoft.eorder.interfaces.controller.CQRSAbstractController;
import com.basoft.eorder.interfaces.query.*;
import com.basoft.eorder.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:39 2018/12/13
 **/
@Slf4j
@Controller
@RequestMapping("/wechat/api/v1")
@ResponseBody
public class SpringWechatController extends CQRSAbstractController {
    private StoreTableRepository storeTableRepository;
    private WxAppInfoTableRepository wxAppInfoTableRepository;
    private RedisUtil redisUtil;
    private OrderService orderService;
    private OrderRepository orderRepository;
    private StoreRepository storeRepository;
    private StoreQuery storeQuery;
    private AppConfigure appConfigure;
    private UidGenerator uidGenerator;
    private EventProducer eventProducer;
    private ReviewRepository reviewRepository;
    private AdviceRepository adviceRepository;
    private ReviewQuery reviewQuery;
    private AdviceQuery adviceQuery;
    private AdvertQuery advertQuery;
    private WxUserQuery wxUserQuery;
    private ObjectMapper payloadMapper = new ObjectMapper();
    private DatabaseHealthCheckRepository databaseHealthCheckRepository;
    private ProductQuery productQuery;

    @Autowired
    public SpringWechatController(
            QueryHandler queryHandler,
            CommandHandleEngine handleEngine,
            StoreTableRepository storeTableRepository,
            WxAppInfoTableRepository wxAppInfoTableRepository,
            StoreRepository storeRepository,
            StoreQuery storeQuery,
            OrderService orderService,
            OrderRepository orderRepository,
            RedisUtil redisUtil,
            AppConfigure appConfigure,
            UidGenerator uidGenerator,
            ReviewRepository reviewRepository,
            AdviceRepository adviceRepository,
            ReviewQuery reviewQuery,
            AdviceQuery adviceQuery,
            AdvertQuery advertQuery,
            WxUserQuery wxUserQuery,
            EventProducer eventProducer,
            DatabaseHealthCheckRepository databaseHealthCheckRepository,
            ProductQuery productQuery) {
        super(queryHandler, handleEngine);
        this.storeTableRepository = storeTableRepository;
        this.wxAppInfoTableRepository = wxAppInfoTableRepository;
        this.storeRepository = storeRepository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.redisUtil = redisUtil;
        this.appConfigure = appConfigure;
        this.uidGenerator = uidGenerator;
        this.reviewRepository = reviewRepository;
        this.adviceRepository = adviceRepository;
        this.reviewQuery = reviewQuery;
        this.storeQuery = storeQuery;
        this.adviceQuery = adviceQuery;
        this.advertQuery = advertQuery;
        this.wxUserQuery = wxUserQuery;
        this.eventProducer = eventProducer;
        this.databaseHealthCheckRepository = databaseHealthCheckRepository;
        this.productQuery = productQuery;
    }

    /**
     * 系统健康性检查接口
     * 附带检查Database
     *
     * @return
     */
    @RequestMapping(value = "/health", method = RequestMethod.GET)
    @ResponseBody
    public String healthCheck(HttpServletRequest request, HttpServletResponse response) {
        String checkRedis = request.getParameter("cr");
        logger.debug("【<><><>】Health Checked!checkRedis=【" + checkRedis + "】");
        if (checkRedis != null && "20190723CheckRedis".equals(checkRedis)) {
            boolean isSet = redisUtil.set("20190723CheckRedis", "Gaga From Moon~", 5);
            if (isSet) {
                return "Interface and Redis is all ok! Success info::" + redisUtil.get("20190723CheckRedis");
            } else {
                return "Interface is OK,but redis is bad!";
            }
        }

        // 减少健康检查日志输出，整点0分、20分、40分输出
        int minute = DateUtil.getMinute(new Date());
        if(minute == 0 || minute == 20 || minute == 40){
            logger.info("【<><><>】Interface,it's health~");
        }

        return "Interface,it's health~";
    }

    /**
     * 系统健康性检查接口
     * 附带检查Redis
     *
     * @return
     */
    @RequestMapping(value = "/healthdb", method = RequestMethod.GET)
    @ResponseBody
    public String healthCheckDB(HttpServletRequest request, HttpServletResponse response) {
        String checkDatabase = request.getParameter("cd");
        logger.debug("【<><><>】Health Checked!checkDatabase=【" + checkDatabase + "】");
        if (checkDatabase != null && "20190723CheckDB".equals(checkDatabase)) {
            // 查询数据库
            try {
                String result = databaseHealthCheckRepository.getData();
                return "Interface and Database is all ok! Success info::" + result;
            } catch (Exception e) {
                log.error("Check Database Error", e);
                return "Interface is OK,but Database is bad!";
            }
        }
        return "Interface,it's health~";
    }

    /**
     * ::扫码点餐登录
     * 扫码点餐，微信公众平台回调该方法实现用户登录
     *
     * @param id 餐桌ID
     * @param code 微信公众平台响应Code码
     * @param qrTime
     * @param response
     */
    @RequestMapping(value = "/initbasoft/{id}")
    @ResponseBody
    public void initBaSoft(@PathVariable("id") Long id, @RequestParam("code") String code,
                           @RequestParam("qrTime") String qrTime, HttpServletResponse response) {
        if (id == null || id == 0 || StringUtils.isBlank(code))
            throw new BizException(ErrorCode.PARAM_INVALID);

        logger.info("====================response code : [" + code + "] =============================");
        logger.info("------ ID:" + id);

        String sysId = "1";    //hard cording Sys id by dikim
        WxAppInfoTable wt = wxAppInfoTableRepository.getWxAppInfoTable(sysId);

        // 获取微信用户的openid等信息
        SnsToken snsToken = WechatAPI.getInstance().oauth2AccessToken(wt.getAppId(), wt.getAppSecret(), code);
        String test_openid = "o4wjI05beq8lJFkiYuJN2Rco8KnY";

        // added by dikim for test 20190225
        if (code.equals(test_openid)) {
            logger.error("<<<<<<<<< TEST LOGIN >>>>>>>>>>");
        } else {
            if (snsToken == null || StringUtils.isBlank(snsToken.getOpenid())) {
                logger.error("<<<<<<<<< wechat sns result is invalid snsToken is null >>>>>>>>>>");
                throw new BizException(ErrorCode.WECHAT_SNS_CODE_INVALID);
            }
        }

        logger.info("==============>> snsToken : [" + snsToken.toString() + "] ==========");

        // 根据桌子编号ID查询桌子信息
        StoreTable storeTable = storeTableRepository.getStoreTable(id);
        if(storeTable == null){
            throw new BizException(ErrorCode.STORE_NULL);
        }
        logger.info("Get StoreTable:" + storeTable);

        // 查询门店信息
        Store store = storeRepository.getStore(storeTable.getStore().id());
        if(store == null){
            throw new BizException(ErrorCode.STORE_NULL);
        }
        logger.info("Get Store:" + store);
        int isPaySet = store.isPaySet();

        String token = StringUtils.replace(Objects.toString(UUID.randomUUID(), null), "-", "");
        logger.info("Token:" + token);
        /*if (StringUtils.isBlank(token) || st == null || st.getSceneId() <= 0 || st.getStore().id() <=0){
            logger.info("Token Error");
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }*/


        logger.info("Get StoreTable:" + storeTable);
        int scenId = storeTable == null ? 0 : storeTable.getSceneId();
        String qrCodeId = storeTable == null ? "0" : storeTable.getQrCodeId();
        Long storeId = storeTable == null ? 0L : storeTable.getStore().id();

        logger.info("▶▶▶▶▶▶▶▶▶▶ Session Info ◀◀◀◀◀◀◀◀◀◀");
        logger.info("openid :" + snsToken.getOpenid());
        logger.info("sceneId :" + scenId);
        logger.info("sceneStr(st.getQrCodeId()) :" + qrCodeId);
        logger.info("storeId :" + storeId);
        logger.info("token :" + token);
        String openid = snsToken.getOpenid();

        // added by dikim for test 20190225
        if (code.equals(test_openid)) {
            openid = code;
            token = code;
        }

        WxUserDTO wxUserDTO = getWxUser(openid, String.valueOf(wt.getShopId()));

        WxSession wx = new WxSession.Builder()
                .openId(openid)
                .sceneId(scenId)
                .sceneStr(qrCodeId)
                .storeId(storeId)
                .storeType(store.storeType())
                .token(token)
                .headimgurl(wxUserDTO.getHeadimgurl())
                .nickname(wxUserDTO.getNickname())
                .city(wxUserDTO.getCity())
                .province(wxUserDTO.getProvince())
                .country(wxUserDTO.getCountry())
                .isPayStore(String.valueOf(isPaySet))
                //.sessionType("1")
                .sessionType(String.valueOf(store.storeType()))
                .sessionChannel("scan")
                .build();

        String redirectUrl = new StringBuilder(appConfigure.get(AppConfigure.BASOFT_WECHAT_INIT_ROOT))
                .append("?")
                .append("sid")
                .append("=")
                .append(token).toString();

        // by dikim checking qr code scan time expired start
        /*
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date qrDate = null;
        try {
            qrDate = formatter.parse(qrTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        Date date = c.getTime();

        if (qrDate.getTime() < date.getTime()) {
            redirectUrl = new StringBuilder(redirectUrl)
                    .append("&qrExpired=true").toString();
            logger.error("<<<<<<<<< expired QR Code >>>>>>>>>>");
        } else {
            redisUtil.hset(AppConfigure.BASOFT_H5_SESSION, token, wx, AppConfigure.TIME_HALF_HOUR);
            logger.info("Success");
        }
        */
        redisUtil.hset(AppConfigure.BASOFT_H5_SESSION, token, wx, AppConfigure.TIME_HALF_HOUR);
        logger.info("Success");
        // by dikim checking qr code scan time expired end

        try {
            logger.info("Redirect to url:" + redirectUrl);
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ::自助点餐登录(店外点餐登录)
     * 点击“进店”按钮，执行该逻辑，入参门店ID和openid，返回token、是否
     *
     * 扩展至酒店下单、医美预约和购物
     *
     * @param storeId
     * @param openId
     * @param response
     * @return Map<String,String>
     *    wechatEndpointToken 微信H5端用户的操作token
     *    isOpening 是否营业中 1-营业 0-打烊
     */
    @RequestMapping(value = "/initbasoftss/{storeid}/{openid}")
    @ResponseBody
    public Map<String, String> initBaSoftSelfService(@PathVariable("storeid") Long storeId,
                                      @PathVariable("openid") String openId, HttpServletResponse response) {
        if (storeId == null || storeId == 0 || StringUtils.isBlank(openId))
            throw new BizException(ErrorCode.PARAM_INVALID);

        logger.info("====================storeId : [" + storeId + "] =============================");
        logger.info("====================openId : [" + openId + "] =============================");

        StoreTable st = storeTableRepository.getSilentStoreTable(storeId);
        logger.info("Get StoreTable:" + st);
        // 如果该门店没有静默桌号则抛出错误
        if(st == null || StringUtils.isBlank(st.getQrCodeId())){
            throw new BizException(ErrorCode.PARAM_INVALID,"该店铺暂不支持店外点餐");
        }
        logger.info("Get SilentStoreTable:" + st);
        int scenId = st == null ? 0 : st.getSceneId();
        String qrCodeId = st == null ? "0" : st.getQrCodeId();

        // 获取门店信息
        Store store = storeRepository.getStore(storeId);
        if(store == null){
            throw new BizException(ErrorCode.STORE_NULL);
        }
        logger.info("Get Store:" + store);
        // 获取当前门店营业状态 1-营业 0-打烊
        int isOpening = StoreUtil.ifOpening(store);

        // 生成wechat endpoint token
        String wechatEndpointToken = StringUtils.replace(Objects.toString(UUID.randomUUID(), null), "-", "");
        logger.info("WechatEndpointToken:" + wechatEndpointToken);


        logger.info("▶▶▶▶▶▶▶▶▶▶ Session Info ◀◀◀◀◀◀◀◀◀◀");
        logger.info("openid :" + openId);
        logger.info("sceneId :" + scenId);
        logger.info("sceneStr(st.getQrCodeId()) :" + qrCodeId);
        logger.info("storeId :" + storeId);
        logger.info("WechatEndpointToken :" + wechatEndpointToken);

        WxUserDTO wxUserDTO = getWxUser(openId, null);

        WxSession wx = new WxSession.Builder()
                .openId(openId)
                .sceneId(scenId)
                .sceneStr(qrCodeId)
                .storeId(storeId)
                .token(wechatEndpointToken)
                .headimgurl(wxUserDTO.getHeadimgurl())
                .nickname(wxUserDTO.getNickname())
                .city(wxUserDTO.getCity())
                .province(wxUserDTO.getProvince())
                .country(wxUserDTO.getCountry())
                //.sessionType("1")
                .sessionType(String.valueOf(store.storeType()))
                .sessionChannel("outer")
                .build();

        redisUtil.hset(AppConfigure.BASOFT_H5_SESSION, wechatEndpointToken, wx, AppConfigure.TIME_HALF_HOUR);
        logger.info("Success");

        // return wechatEndpointToken;

        Map<String, String> returnObj = new HashMap<>();
        returnObj.put("wechatEndpointToken", wechatEndpointToken);
        returnObj.put("isOpening", "" + isOpening);
        return returnObj;
    }

    @RequestMapping(value = "/menuAuth", method = RequestMethod.GET)
    public void menuRoute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String code = req.getParameter("code");
        logger.info("WxToken:" + code);

        String sysId = "1";    //hard cording Sys id by dikim

        WxAppInfoTable wt = wxAppInfoTableRepository.getWxAppInfoTable(sysId);
        SnsToken snsToken = WechatAPI.getInstance().oauth2AccessToken(wt.getAppId(), wt.getAppSecret(), code);
        if (snsToken == null || StringUtils.isBlank(snsToken.getOpenid())) {
            logger.info("snsToken null");
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "wechat auth");
            return;
        }

        WxSession wxSession = new WxSession.Builder()
                .token(snsToken.getAccess_token())
                .openId(snsToken.getOpenid())
                .build();
        logger.info("Menu Auth Wechat User Info::::" + wxSession);

        String sid = UUID.randomUUID().toString();
        redisUtil.hset(AppConfigure.BASOFT_H5_SESSION, sid, wxSession, AppConfigure.TIME_HALF_HOUR);

        logger.info("openId================"+wxSession.getOpenId());
        /*WxUserDTO wxUserDTO = new WxUserDTO();//关注信息Xifu
        try {
             wxUserDTO = getWxUser(wxSession.getOpenId(), null);
        } catch (Exception e) {
            wxUserDTO.setSubscribe(0);
        }*/

        String redirectUrl = new StringBuilder("http://")
                .append(req.getHeader("host"))
                .append(appConfigure.get(AppConfigure.BASOFT_WECHAT_MENU_REDIRECT_ROOT))
                .append("?")
                .append("sid=")
                .append(sid)
                .append("&")
                .append("redirect=")
                .append(req.getParameter("qpa"))
                .append("?sl=")
                .append(randomNum(1000000,9999999))
                /*.append("&isSub=")//是否关注 20191204 Xifu
                .append(wxUserDTO.getSubscribe())*/
                .toString();

        logger.info("Menu redirect to:" + redirectUrl);
        res.sendRedirect(redirectUrl);
    }

    /**
     * 店铺首页前调用，在/initbasoft(扫码点餐)和/initbasoftss（店外自助点餐）后调用
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/init")
    @ResponseBody
    public Map<String, String> initWechatEnv(HttpServletRequest request, HttpServletResponse response) {
        String sid = request.getParameter("sid");
        logger.info("sid=========="+sid);
        Objects.requireNonNull(sid);

        WxSession wxSession = (WxSession) redisUtil.hget(AppConfigure.BASOFT_H5_SESSION, sid);

        if (wxSession == null) {
            logger.warn("error:" + sid);
            throw new BizException(ErrorCode.SYS_TIMEOUT);
        }

        Cookie cookie = new Cookie(AppConfigure.BASOFT_H5_SESSION, sid);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 10 * 60);

        response.addCookie(cookie);

        Map<String, String> returnObj = new HashMap<>();
        returnObj.put("store_id", "" + wxSession.getStoreId());
        returnObj.put("store_type", "" + wxSession.getSessionType());
        returnObj.put("open_id", "" + wxSession.getOpenId());
        return returnObj;
    }

    private <T> String resultReceiveWechat(Object object, Class<T> clazz) throws Exception {
        Map<String, String> resultMap = (Map<String, String>) BeanUtil.apacheObjectToMap((T) object);
        return WXPayUtil.mapToXml(resultMap);
    }

    private String getCertStream() {
        String certPath = appConfigure.get(AppConfigure.BASOFT_WECHAT_CERT_PATH);
        /*byte[] certData;
        InputStream certStream = null;
        File file;
		try {
			file = new ClassPathResource(certPath).getFile();
	        certStream = new FileInputStream(file);
	        certData = new byte[(int) file.length()];
	        certStream.read(certData);
	        certStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
        return certPath;
    }

    /**
     * 根据扫码指定的店铺获取该店铺的菜谱信息
     *
     * @param request
     * @return
     */
    @Deprecated // 该接口已经停用了，全部改用/maindisc接口（含折扣信息）
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    @ResponseBody
    public Object mainDataList(HttpServletRequest request) {
        WxSession session = (WxSession) request.getAttribute(AppConfigure.BASOFT_WX_SESSION_PROP);
        long sid = session.getStoreId();
        String query = "{ProductGroups(storeId:" + sid + "){dataList{id, nameKor, nameChn, storeId, status, showIndex, created}}, ProductGroupMaps(storeId:" + sid + ",status:\"" + 1 + "\"){id, storeId, categoryId, nameKor, nameChn,detailDesc,detailChnDesc,created,desChn,desKor, fileId, fileName, fileSysName, fileType, fileUrl, fileSize, fileOriginalName, productId, prdGroupId, showIndex, mainUrl, subImageUrl,recommend, psdList{id, nameKor, nameChn, productId, priceKor, priceChn, useDefault, created}}}";
        Map map = (Map) getQueryHandler().handle(query);
        if (map == null) {
            return null;
        }

        List<Map> groups = (List<Map>) ((Map) map.get("ProductGroups")).get("dataList");
        List<Map> products = ((List<Map>) map.get("ProductGroupMaps"));

        Map<Long, List<Map>> groupMap = products.stream().collect(Collectors.groupingBy(product ->
                NumberUtils.toLong(Objects.toString(product.get("prdGroupId"), null))
        ));

        List<Map> groupList = groups
                .stream()
                .peek(group -> {
                    Long id = NumberUtils.toLong(Objects.toString(group.get("id"), null));
                    List<Map> mapList = groupMap.get(id);
                    if (mapList != null && mapList.size() > 0) {
                        if (mapList.size() > 0)
                            group.put("products", mapList);
                    }
                })
                .collect(Collectors.toList());

        List<Map> hotProducts = products.stream().filter((hotMap) -> {
            Integer recommend = (Integer) hotMap.get("recommend");
            return recommend != null && recommend == 1;
        }).collect(Collectors.toList());

        if (0 < hotProducts.size()) {
            //Map hotGroup = hotOfGroup(NumberUtils.toLong("1")).toMap();
            Map hotGroup = hotOfGroupMap(sid);
            hotGroup.put("products", hotProducts);
            groupList.add(0, hotGroup);
            // return Lists.asList(hotGroup, groupList.toArray());
        }

        return groupList;
    }

    /**
     * 根据扫码指定的店铺获取该店铺的菜谱信息(包含折扣信息)
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/maindisc", method = RequestMethod.GET)
    @ResponseBody
    public Object mainDataListWithDiscount(HttpServletRequest request) {
        WxSession wxSession = (WxSession) request.getAttribute(AppConfigure.BASOFT_WX_SESSION_PROP);
        // 门店ID
        long storeId = wxSession.getStoreId();

        // 查询折扣活动的产品列表
        List<LinkedHashMap<String,Object>> discountDisplayList = null;
        String queryDiscount = "{DiscountsDisplay(storeId:" + storeId + "){discId, discName, discChannel, custId, storeId, discRate, startTime,endTime,discDetId,prodId,prodSkuId,discPrice,discPriceChn}}";
        Map disCountMap = (Map) getQueryHandler().handle(queryDiscount);
        if (disCountMap == null) {
            return null;
        } else {
            discountDisplayList = (List<LinkedHashMap<String,Object>>) disCountMap.get("DiscountsDisplay");
        }

        String query = "{ProductGroups(storeId:" + storeId + "){dataList{id, nameKor, nameChn, storeId, status, showIndex, created}}, ProductGroupMaps(storeId:" + storeId + ",status:\"" + 1 + "\"){id, storeId, categoryId, nameKor, nameChn,detailDesc,detailChnDesc,created,desChn,desKor, fileId, fileName, fileSysName, fileType, fileUrl, fileSize, fileOriginalName, productId, prdGroupId, showIndex, mainUrl, subImageUrl,recommend, psdList{id, nameKor, nameChn, productId, priceKor, priceChn, useDefault, created}}}";
        Map map = (Map) getQueryHandler().handle(query);
        if (map == null) {
            return null;
        }

        // 产品组列表
        List<Map> groups = (List<Map>) ((Map) map.get("ProductGroups")).get("dataList");
        // 产品列表
        List<Map> products = ((List<Map>) map.get("ProductGroupMaps"));

        // !!!将折扣活动产品信息融入产品列表-start
        // 对折扣信息加工成Map<Long, LinkedHashMap<String, Object>>即skuid:sku Discount LinkedHashMap
        Map<Long, LinkedHashMap<String, Object>> discountDisplayMap = new HashMap<Long, LinkedHashMap<String, Object>>(discountDisplayList.size());
        discountDisplayList.forEach(discountDisplayLinkedHashMap -> {
            discountDisplayMap.put((Long) discountDisplayLinkedHashMap.get("prodSkuId"), discountDisplayLinkedHashMap);
        });
        // 产品循环：第一步循环产品列表，其中取出该产品的sku列表。第二步循环sku列表，根据skuId去折扣集合里获取折扣，有折扣则处理，否则不处理
        products.stream().forEach(productMap -> {
            // 取出产品SKU列表
            List<Map> prodSkuList = (List<Map>) productMap.get("psdList");
            // 产品SKU列表循环
            prodSkuList.stream().forEach(psdMap -> {
                // 获取prod sku id
                Long prodSkuId = (Long) psdMap.get("id");
                // 根据prod sku id去折扣集合中获取折扣信息
                LinkedHashMap<String, Object> prodSkuDiscount = discountDisplayMap.get(prodSkuId);
                // 折扣信息存在，则将折扣信息注入产品的sku列表中。
                if (prodSkuDiscount != null && prodSkuDiscount.size() > 0) {
                    psdMap.put("discId", prodSkuDiscount.get("discId"));
                    psdMap.put("discPrice", prodSkuDiscount.get("discPrice"));
                    psdMap.put("discPriceChn", prodSkuDiscount.get("discPriceChn"));
                }
            });
        });
        // !!!将折扣活动产品信息融入产品列表-end

        // 产品列表按照产品组ID进行分组
        Map<Long, List<Map>> groupMap = products.stream().collect(Collectors.groupingBy(product ->
                NumberUtils.toLong(Objects.toString(product.get("prdGroupId"), null))
        ));

        // 将产品列表放入产品组中形成新的产品组groupList（其中每个分组包含自己组的产品列表）
        List<Map> groupList = groups
                .stream()
                .peek(group -> {
                    Long id = NumberUtils.toLong(Objects.toString(group.get("id"), null));
                    List<Map> mapList = groupMap.get(id);
                    if (mapList != null && mapList.size() > 0) {
                        if (mapList.size() > 0)
                            group.put("products", mapList);
                    }
                })
                .collect(Collectors.toList());

        // 从全部产品列表products中过滤出热销产品列表hotProducts
        List<Map> hotProducts = products.stream().filter((hotMap) -> {
            Integer recommend = (Integer) hotMap.get("recommend");
            return recommend != null && recommend == 1;
        }).collect(Collectors.toList());

        // 将热销产品列表hotProducts放入“热销分组”，并将该分组放入总分组groupList
        if (0 < hotProducts.size()) {
            //Map hotGroup = hotOfGroup(NumberUtils.toLong("1")).toMap();
            Map hotGroup = hotOfGroupMap(storeId);
            hotGroup.put("products", hotProducts);
            groupList.add(0, hotGroup);
            // return Lists.asList(hotGroup, groupList.toArray());
        }

        return groupList;
    }

    /**
     * 酒店商户主页的产品信息、折扣信息及库存信息查询
     * 20191119:折扣活动问题
     * 当前时间为20191119，正在举行折扣活动（20191101-20191130，五折活动），此时购买20200120的房间，也是五折，
     * 这严重不符合该折扣活动的目的（活动目的应该是20191101-20191130期间的房价为五折，而不是期间后续可预订日期内房价为五折）
     * 20191119：@Deprecated掉该方法
     * 该类折扣活动的查询严重不适应预约类产品（如酒店住宿、医美预约），故针对酒店单独编写接口进行完善。
     *
     * @param request
     * @return
     * @see public Object mainDataListWithDiscount(HttpServletRequest request) {}
     */
    @Deprecated
    @RequestMapping(value = "/maindischotel", method = RequestMethod.GET)
    @ResponseBody
    public Object hotelMainDataListWithDiscount(HttpServletRequest request,
                                                @RequestParam(name = "startDate", defaultValue = "") String startDate,
                                                @RequestParam(name = "endDate", defaultValue = "") String endDate) {
        WxSession wxSession = (WxSession) request.getAttribute(AppConfigure.BASOFT_WX_SESSION_PROP);

        log.info("店铺列表查询参数>>>>>>startDate||endDate>>>" + startDate + "||" + endDate);
        if (startDate == null || "".equals(startDate.trim()) || endDate == null || "".equals(endDate.trim())) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 门店ID
        long storeId = wxSession.getStoreId();

        // 查询折扣活动的产品列表
        List<LinkedHashMap<String,Object>> discountDisplayList = null;
        String queryDiscount = "{DiscountsDisplay(storeId:" + storeId + "){discId, discName, discChannel, custId, storeId, discRate, startTime,endTime,discDetId,prodId,prodSkuId,discPrice,discPriceChn}}";
        Map disCountMap = (Map) getQueryHandler().handle(queryDiscount);
        if (disCountMap == null) {
            return null;
        } else {
            discountDisplayList = (List<LinkedHashMap<String,Object>>) disCountMap.get("DiscountsDisplay");
        }

        String query = "{ProductGroups(storeId:" + storeId + "){dataList{id, nameKor, nameChn, storeId, status, showIndex, created}}, ProductGroupMaps(storeId:" + storeId + ",status:\"" + 1 + "\"){id, storeId, categoryId, nameKor, nameChn,detailDesc,detailChnDesc,created,desChn,desKor, fileId, fileName, fileSysName, fileType, fileUrl, fileSize, fileOriginalName, productId, prdGroupId, showIndex, mainUrl, subImageUrl,recommend, psdList{id, nameKor, nameChn, productId, priceKor, priceChn, useDefault, created}}}";
        log.info(query);
        Map map = (Map) getQueryHandler().handle(query);
        if (map == null) {
            return null;
        }

        // 产品组列表
        List<Map> groups = (List<Map>) ((Map) map.get("ProductGroups")).get("dataList");
        // 产品列表
        List<Map> products = ((List<Map>) map.get("ProductGroupMaps"));

        // 查询产品库存信息
        List<LinkedHashMap<String,Object>> hotelInventoryList = null;
        StringBuilder queryHotelInventory = new StringBuilder("{InventoryHotelsByConditions(storeId:\"");
        queryHotelInventory.append(storeId).append("\",")
                .append("startDate").append(":\"").append(startDate).append("\",")
                .append("endDate").append(":\"").append(endDate).append("\")")
                .append("{invId,storeId,prodId,prodSkuId,invYear,invMonth,invDay,invDate,isOpening,invTotal,invUsed,invBalance}}");
        log.info("queryHotelInventory>>>" + queryHotelInventory.toString());
        Map hotelInventoryMap = (Map) getQueryHandler().handle(queryHotelInventory.toString());

        Map<Long, LinkedHashMap<String, Object>> hotelInventoryDisplayMap = new HashMap<Long, LinkedHashMap<String, Object>>();
        if (hotelInventoryMap == null) {
            // nothing to do
        } else {
            hotelInventoryList = (List<LinkedHashMap<String,Object>>) hotelInventoryMap.get("InventoryHotelsByConditions");
            hotelInventoryList.forEach(hotelInventoryDisplayLinkedHashMap -> {
                hotelInventoryDisplayMap.put((Long) hotelInventoryDisplayLinkedHashMap.get("prodSkuId"), hotelInventoryDisplayLinkedHashMap);
            });
        }

        // !!!将折扣活动产品信息和库存信息融入产品列表-start
        // 对折扣信息加工成Map<Long, LinkedHashMap<String, Object>>即skuid:sku Discount LinkedHashMap
        Map<Long, LinkedHashMap<String, Object>> discountDisplayMap = new HashMap<Long, LinkedHashMap<String, Object>>(discountDisplayList.size());
        discountDisplayList.forEach(discountDisplayLinkedHashMap -> {
            discountDisplayMap.put((Long) discountDisplayLinkedHashMap.get("prodSkuId"), discountDisplayLinkedHashMap);
        });
        // 产品循环
        products.stream().forEach(productMap -> {
            // 取出产品SKU列表
            List<Map> prodSkuList = (List<Map>) productMap.get("psdList");
            // 产品SKU列表循环
            prodSkuList.stream().forEach(psdMap -> {
                // 获取prod sku id
                Long prodSkuId = (Long) psdMap.get("id");
                // 根据prod sku id去折扣集合中获取折扣信息
                LinkedHashMap<String, Object> prodSkuDiscount = discountDisplayMap.get(prodSkuId);
                // 折扣信息存在，则将折扣信息注入产品的sku列表中。
                if (prodSkuDiscount != null && prodSkuDiscount.size() > 0) {
                    psdMap.put("discId", prodSkuDiscount.get("discId"));
                    psdMap.put("discPrice", prodSkuDiscount.get("discPrice"));
                    psdMap.put("discPriceChn", prodSkuDiscount.get("discPriceChn"));
                }

                // 根据prod sku id获取决定性库存信息
                LinkedHashMap<String, Object> inventorySkuDiscount = hotelInventoryDisplayMap.get(prodSkuId);
                if (inventorySkuDiscount != null && inventorySkuDiscount.size() > 0) {
                    psdMap.put("isOpening", inventorySkuDiscount.get("isOpening"));
                    psdMap.put("invTotal", inventorySkuDiscount.get("invTotal"));
                    psdMap.put("invUsed", inventorySkuDiscount.get("invUsed"));
                    psdMap.put("invBalance", inventorySkuDiscount.get("invBalance"));
                } else {
                    psdMap.put("isOpening", "1");
                    psdMap.put("invTotal", "10000");
                    psdMap.put("invUsed", "0");
                    psdMap.put("invBalance", "10000");
                }
            });
        });
        // !!!将折扣活动产品信息和库存信息融入产品列表-end

        // 产品列表按照产品组ID进行分组
        Map<Long, List<Map>> groupMap = products.stream().collect(Collectors.groupingBy(product ->
                NumberUtils.toLong(Objects.toString(product.get("prdGroupId"), null))
        ));

        // 将产品列表放入产品组中形成新的产品组groupList（其中每个分组包含自己组的产品列表）
        List<Map> groupList = groups
                .stream()
                .peek(group -> {
                    Long id = NumberUtils.toLong(Objects.toString(group.get("id"), null));
                    List<Map> mapList = groupMap.get(id);
                    if (mapList != null && mapList.size() > 0) {
                        if (mapList.size() > 0)
                            group.put("products", mapList);
                    }
                })
                .collect(Collectors.toList());

        // 从全部产品列表products中过滤出热销产品列表hotProducts
        List<Map> hotProducts = products.stream().filter((hotMap) -> {
            Integer recommend = (Integer) hotMap.get("recommend");
            return recommend != null && recommend == 1;
        }).collect(Collectors.toList());

        // 将热销产品列表hotProducts放入“热销分组”，并将该分组放入总分组groupList
        if (0 < hotProducts.size()) {
            //Map hotGroup = hotOfGroup(NumberUtils.toLong("1")).toMap();
            Map hotGroup = hotOfGroupMap(storeId);
            hotGroup.put("products", hotProducts);
            groupList.add(0, hotGroup);
            // return Lists.asList(hotGroup, groupList.toArray());
        }

        return groupList;
    }

    /**
     * 查询附近店铺列表
     * 同时复用为查询店铺的详情
     * 查询列表传参：
     * {
     *           id: "",空值
     *           far: "9999999999",
     *           page: 1,
     *           size: 20,
     *           longitude: this.longitude, 有值
     *           latitude: this.latitude,有值
     *           storeType: this.storeType,有值
     *           channel: "wechat"
     * }
     *
     * 查询详情传参：
     * {
     *           id: "99999999992344232",storeId
     *           far: "9999999999",
     *           page: 1,
     *           size: 20,
     *           longitude: "", 空值
     *           latitude: "",空值
     *           channel: "wechat"
     * }
     *
     * @param request
     * @param channel   业务终端 wechat-微信用户端
     * @param id        门店ID
     * @param longitude 当前所在位置的经度
     * @param latitude  当前所在位置的纬度
     * @param far       查询半径
     * @param storeType 门店的业务类型
     * @param page
     * @param size
     * @return java.lang.Object
     * @author Dong Xifu
     * @date 2019/3/5 上午11:25
     */
    @RequestMapping(value = "/stores", method = RequestMethod.GET)
    @ResponseBody
    public Object stores(HttpServletRequest request,
                         @RequestParam(name = "channel", defaultValue = "") String channel,
                         @RequestParam(name = "id", defaultValue = "") String id,
                         @RequestParam(name = "longitude", defaultValue = "") String longitude,
                         @RequestParam(name = "latitude", defaultValue = "") String latitude,
                         @RequestParam(name = "far", defaultValue = "", required = false) String far,
                         @RequestParam(name = "storeType", defaultValue = "") String storeType,
                         @RequestParam(name = "name", defaultValue = "") String name,
                         @RequestParam(name = "city", defaultValue = "") String city,
                         @RequestParam(name = "page", defaultValue = "1") Integer page,
                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
//        log.info("店铺列表查询参数>>>>>>longitude||latitude" + longitude + "||" + latitude);
//        log.info("店铺列表查询参数>>>>>>far" + far);
        // 列表查询则验证channel和storeType参数
        if (StringUtils.isBlank(id)) {
            if (channel == null || "".equals(channel.trim()) || storeType == null || "".equals(storeType.trim())) {
                throw new BizException(ErrorCode.PARAM_INVALID);
            }
        }
        String ban = "1";

        // String query = "";//20190814优化SQL拼接
        StringBuilder query = new StringBuilder(512);
        //20190814优化SQL拼接
        // query = "{ St ores(channel:\"" + channel + "\",id:\"" + id + "\",storeType:\"" + storeType + "\",size:" + size + ",page:" + page + ",ban:\"" + ban + "\",longitude:\"" + longitude + "\",latitude:\"" + latitude + "\",far:\"" + far + "\" ){total,dataList{id,name,city,areaNm,detailAddr,detailAddrChn,description,descriptionChn,shopHour,mobile,logoUrl,status,managerPhone,managerAccount,managerName,merchantId,longitude,latitude,disdance,categoryList{id,name,chnName,type,categoryType,manageType},isOpening,isSelfservice,isDelivery,selfserviceUseyn,deliveryUseyn,minPriceKor,minPriceChn,minDiscountPriceKor,minDiscountPriceChn}}}";
        query.append("{ Stores(channel:\"").append(channel).append("\",id:\"").append(id)
            .append("\",storeType:\"").append(storeType).append("\",city:\"").append(city).append("\",name:\"").append(name).append("\",size:").append(size)
            .append(",page:").append(page).append(",ban:\"").append(ban)
            .append("\",longitude:\"").append(longitude).append("\",latitude:\"").append(latitude);
        if (StringUtils.isNotBlank(far))
            query.append("\",far:\"").append(far);
        query.append("\"){total,dataList{id,name,city,areaNm,detailAddr,detailAddrChn,description,descriptionChn,shopHour,mobile,logoUrl,status,managerPhone,managerAccount,managerName,merchantId,longitude,latitude,disdance,categoryList{id,name,chnName,type,categoryType,manageType},isJoin,isOpening,isSelfservice,isDelivery,selfserviceUseyn,deliveryUseyn,minPriceKor,minPriceChn,minDiscountPriceKor,minDiscountPriceChn}}}");


        //20190814优化SQL拼接
        // Map map = (Map) getQueryHandler().handle(query);
        Map map = (Map) getQueryHandler().handle(query.toString());

        if (map == null) {
            return null;
        }

        // 查询门店详情
        if (StringUtils.isNotBlank(id)) {
            // 查询门店的Banners
            String bannerQuery = "{ Banners(storeId:" + id + ", size:" + size + ", page:" + page + "){id,name, imagePath, created, storeId,showIndex,status}}";
            Map bannerMap = (Map) getQueryHandler().handle(bannerQuery);
            if (bannerMap.size() > 0) {
                map.put("bannerList", (List<Map>) bannerMap.get("Banners"));
            }

            Map storesMap = (Map) map.get("Stores");
            List<Map> storeList = (List<Map>) storesMap.get("dataList");

            // 查询门店的推荐产品
            String proQuery = "{ProductGroupMaps(recommend:\"1\",storeId:" + id + ",status:\""+1+"\"){ nameChn,nameKor,mainUrl,psdList{nameKor,nameChn, priceKor, priceChn}}}";
            Map proMap = (Map) getQueryHandler().handle(proQuery);
            storeList.get(0).put("productList", proMap.get("ProductGroupMaps"));
        }
        // 查询门店列表
        else{
            // 如果是酒店业务则对商品韩币最低价转换为人民币
            if(CommonConstants.BIZ_HOTEL_STRING.equals(storeType)){
                Map storesMap = (Map) map.get("Stores");
                List<Map> storeList = (List<Map>) storesMap.get("dataList");

                // 首先进行价格设定。
                storeList.stream().forEach(storeDTOMap -> {
                    BigDecimal minPriceKor = (BigDecimal)storeDTOMap.get("minPriceKor");
                    BigDecimal minDiscountPriceKor = (BigDecimal)storeDTOMap.get("minDiscountPriceKor");
                    // minDiscountPriceKor折扣最低价为0或者相等或者最低价小于最低折扣价格
                    if(minDiscountPriceKor.compareTo(BigDecimal.ZERO)==0 ||
                            minDiscountPriceKor.compareTo(minPriceKor)==0 ||
                            minDiscountPriceKor.compareTo(minPriceKor) == 1){
                        // nothing to do
                    }
                    // 折扣最低价小于最低价，则将最低价设置为折扣最低价
                    else if(minDiscountPriceKor.compareTo(minPriceKor) == -1){
                        storeDTOMap.put("minPriceKor", minDiscountPriceKor);
                    }
                });

                // 然后将韩币价格转为人民币价格
                // productQuery.currencyConverter(storeList);// 20190814酒店最低价格显示韩币，不进行韩币转换了，提高性能，需要转换的时候去掉注释放开该行即可。
            }
        }
        return map;
    }

    /**
     * 我的订单
     *
     * @param customerId
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/myorder", method = RequestMethod.GET)
    @ResponseBody
    public Object myorder(@RequestParam String customerId,
                          @RequestParam(name = "storeType",defaultValue = "") String storeType,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "10") Integer size) {
        if (StringUtils.isBlank(customerId)) {
            logger.error("<<<<<<< error : customerId is null");
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        String query = "{Orders(customerId:\"" + customerId + "\",storeType:\""+storeType+"\",size:" + size + ",page:" + page + "){total, dataList{id, storeId, tableId, amount, paymentAmount,payAmtRmb, discountAmount, status, buyerMemo, customerId, created, payDts, updated, storeNm,storeType,revId, logoUrl, tableNum, tableTag, diningPlace, diningTime " +
                "itemList{id, orderId,prodNmKor,prodNmChn,prodUrl,qty, skuId, skuNmKor, skuNmChn, price, qty, created}}}}";

        Map map = (Map) getQueryHandler().handle(query);
        if (map == null) {
            logger.info("========== my order is empty =========");
            return null;
        }

        int total = NumberUtils.toInt(Objects.toString(((Map) map.get("Orders")).get("total")));
        if (total == 0) {
            logger.info("========== search my order result is 0 =========");
            return null;
        }

        List<Map> dataList = (List<Map>) ((Map) map.get("Orders")).get("dataList");
        if (dataList != null && dataList.size() > 0) {
            // 处理订单状态为1或2的订单（20200513标注：正式订单表不会存在状态为1或2的订单。不知道开发此接口时进行此步处理意图）
            dataList.stream().filter(p -> 1 == NumberUtils.toInt(Objects.toString(p.get("status"))) || 2 == NumberUtils.toInt(Objects.toString(p.get("status"))))
                    .forEach(m -> {
                        try {
                            Long storeId = NumberUtils.toLong(Objects.toString(m.get("storeId")));

                            Store store = storeRepository.getStore(storeId);
                            if (store != null) {
                                WxPayQueryResp queryResp = orderService.queryWechatOrder(store, m, getCertStream());
                                if ("SUCCESS".equals(queryResp.getReturn_code())) {
                                    if ("SUCCESS".equals(queryResp.getResult_code())) {
                                        if ("SUCCESS".equals(queryResp.getTrade_state())) {
                                            m.put("status", "4");
                                        } else if ("NOTPAY".equals(queryResp.getTrade_state())) {
                                            m.put("status", "0");
                                        } else if ("USERPAYING".equals(queryResp.getTrade_state())) {
                                            m.put("status", "2");
                                        } else if ("PAYERROR".equals(queryResp.getTrade_state())) {
                                            m.put("status", "3");
                                        }
                                    } else {
                                        if ("ORDERNOTEXIST".equals(queryResp.getErr_code())) {
                                            m.put("status", "3");
                                        }
                                    }
                                }
                            } else {
                                m.put("status", "3");
                            }

                        } catch (Exception e) {
                            logger.error("<<<<< query order pay is error >>>>>", e);
                        }
                    });
        } else {
            dataList = new LinkedList<>();
        }
        return map;
    }


    /**
     * 押金产品列表
     *
     * @Param storeId
     * @return java.lang.Object
     * @author Dong Xifu
     * @date 2019/8/27 下午5:49
     */
    @RequestMapping(value = "/depositProList", method = RequestMethod.GET)
    @ResponseBody
    public Object depositProList(@RequestParam(name = "storeId",defaultValue = "")String storeId,
    @RequestParam(name = "page", defaultValue = "1") int page,
    @RequestParam(name = "size", defaultValue = "10") int size) {
        String query = "{ Products(storeId:\""+storeId+"\",isDeposit:\""+1+"\",orderBy:\"created\" ,size:"+size+",page:"+page+") {total,dataList{id,nameKor,showIndex,mainUrl,subImageUrl,\n" +
            "nameChn,desKor,desChn,detailDesc,detailChnDesc,categoryId,categoryName,status,recommend,defaultPrice,created,storeId,storeName,areaNm,updateTime,psdList{id, nameKor, nameChn, productId, priceKor, priceChn, useDefault, created}      }}}";

        Map map = (Map) getQueryHandler().handle(query);
        Map productsMap = (Map) map.get("Products");
        List<Map> productList = (List<Map>) productsMap.get("dataList");
        productList = productList.stream().filter(p->(!((int)p.get("status") ==2))).collect(Collectors.toList());
        map.put("Products", productList);
        return map;
    }

    /**
     * @return java.lang.Object
     * @Title 个人中心
     * @Param
     * @author Dong Xifu
     * @date 2019/5/15 上午11:18
     */
    @RequestMapping(value = "/personal", method = RequestMethod.GET)
    @ResponseBody
    public Object personal(@RequestParam(name = "platformId") String platformId, HttpServletRequest request) {
        WxUserDTO dto = getWxUser(platformId, null);
        return dto;
    }


    /**
     * 用户同意授权后获取用户信息
     *
     * @param platformId
     * @param code
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/personalManual", method = RequestMethod.GET)
    @ResponseBody
    public Object personalManual(@RequestParam(name = "platformId") String platformId,
                           @RequestParam(name = "code") String code) throws Exception{
        String sysId = "1";
        WxAppInfoTable wt = wxAppInfoTableRepository.getWxAppInfoTable(sysId);

        StringBuilder redirectUrl = new StringBuilder();
        redirectUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token?")
            .append("appid="+wt.getAppId()+"&secret="+wt.getAppSecret()+"")
            .append("&code=")
            .append(code)
            .append("&grant_type=authorization_code");
        log.info("code="+code);

        JSONObject jsonObject = new JSONObject();
        //这里通过code换取的是一个特殊的网页授权access_token,与基础支持中的access_token(该access_token用于调用其他接口)不同
        String accessToken = HttpClientUtils.requestPost(redirectUrl.toString(), jsonObject);
        Token token = new Gson().fromJson(accessToken, Token.class);
        accessToken = token.getAccess_token();

        log.info("accessToken="+accessToken);

        StringBuilder getUserInfoUrl = new StringBuilder();
        getUserInfoUrl.append("https://api.weixin.qq.com/sns/userinfo?")
            .append("access_token=")
            .append(accessToken)
            .append("&openid=")
            .append(platformId)
            .append("&lang=en");

        String userInfo = HttpClientUtils.requestPost(getUserInfoUrl.toString(), jsonObject);
        log.info(userInfo);

        WXUser returns = new Gson().fromJson(userInfo, WXUser.class);

        return returns;
    }

    /**
     * @return int
     * @Title 新增评论
     * @Param
     * @author Dong Xifu
     * @date 2019/5/15 下午1:22
     */
    @RequestMapping(value = "/saveReview", method = RequestMethod.POST)
    @ResponseBody
    public int saveReview(@RequestBody SaveReview saveReview, HttpServletRequest request) {
        System.out.println("saveReview -------");
        Long revId = uidGenerator.generate(BusinessTypeEnum.REVIEW);

        WxUserDTO dto = getWxUser(saveReview.getPlatformId(),null);

        saveReview.setRevId(revId);
        saveReview.setNickname(dto.getNickname());
        saveReview.setChathead(dto.getHeadimgurl());
        saveReview.setModifiedUserId(saveReview.getPlatformId());

        String revContent = saveReview.getRevContent();

        if (StringUtil.hasEmoji(revContent)) {
            saveReview.setRevContent(StringUtil.replaceEmoji(revContent));
        }


        Review review = saveReview.build();

        Map<String, Object> param = new HashMap<>();
        param.put("openId", saveReview.getPlatformId());
        param.put("orderId", saveReview.getOrderId());
        int count = reviewQuery.getReviewCount(param);
        if (count > 1) {
            throw new BizException(ErrorCode.WECHAT_REVIEW_EXIT);
        }

        return reviewRepository.saveReview(review);
    }

    /**
     * @Title 翻译评论
     * @Param
     * @return int
     * @author Dong Xifu
     * @date 2019/5/28 下午6:20
     */
    @RequestMapping(value = "/translateReview", method = RequestMethod.POST)
    @ResponseBody
    public String translateReview(@RequestBody TransReview transReview){
        System.out.println("translateReview -------");
        Map<String, Object> param = new HashMap<>();
        param.put("revId",transReview.getRevId());
        Review review = reviewQuery.getReview(param);
        if (StringUtils.isNotBlank(transReview.getRevContent())) {
            if(StringUtils.isNotBlank(review.getRevContentCopie())){
                return review.getRevContentCopie();
            }else{
                String revCompie =  TranslateUtil.translateTest(transReview.getRevContent());
                transReview.setRevContentCopie(revCompie);
                transReview.setRevReplyContentCopie(review.getRevReplyContentCopie());
                reviewRepository.translateReview(transReview);
                return revCompie;
            }
        } else if (StringUtils.isNotBlank(transReview.getRevReplyContent())) {
            if (StringUtils.isNotBlank(review.getRevReplyContentCopie())) {
                return review.getRevReplyContentCopie();
            } else {
                String replyCompie =  TranslateUtil.translateTest(transReview.getRevReplyContent());
                transReview.setRevContentCopie(review.getRevContentCopie());
                transReview.setRevReplyContentCopie(replyCompie);
                reviewRepository.translateReview(transReview);
                return replyCompie;
            }
        }

        return TranslateUtil.translateTest(transReview.getQuery());
    }


    /**
     * @return java.lang.Object
     * @Title 评论列表
     * @Param
     * @author Dong Xifu
     * @date 2019/5/21 上午10:39
     */
    @RequestMapping(value = "/reviewList", method = RequestMethod.GET)
    @ResponseBody
    public Object reviewList(@RequestParam(name = "page", defaultValue = "1") int page,
                             @RequestParam(name = "size", defaultValue = "10") int size,
                             @RequestParam(name = "storeId", defaultValue = "") String storeId,
                             HttpServletRequest request) {
        String query = "{ reviews(storeId:\"" + storeId + "\",size:" + size + ", page:" + page + ")" +
            "{total,dataList{ revId,platformId,nickname,chathead,revTime,orderId,storeId,revClass,envClass,prodClass," +
            "serviceClass,revContent,isAnonymity,revReplyContent,revReplyTime,revStatus," +
            "imgList{revAttachId,contentUrl,displayOrder,isDisplay}   }  }}  ";
        Map reviewMap = (Map) getQueryHandler().handle(query);
        return reviewMap;
    }


    /**
     * @return com.basoft.eorder.domain.model.Review
     * @Title 查询评论详情
     * @Param
     * @author Dong Xifu
     * @date 2019/5/15 下午1:43
     */
    @RequestMapping(value = "/getReview", method = RequestMethod.GET)
    @ResponseBody
    public ReviewDTO getReview(@RequestParam(name = "revId", defaultValue = "") String revId,
                               @RequestParam(name = "orderId", defaultValue = "") String orderId,
                               @RequestParam(name = "platformId", defaultValue = "") String platformId) {
        logger.debug("Start getReview -------");

        Map<String, Object> param = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(revId)) {
            param.put("revId", revId);
        }
        if (StringUtils.isNotBlank(platformId)) {
            param.put("platformId", platformId);
        }
        if (StringUtils.isNotBlank(orderId)) {
            param.put("orderId", orderId);
        }
        return reviewQuery.getReviewDto(param);
    }


    /**
     * @return int
     * @Title 删除评论
     * @Param
     * @author Dong Xifu
     * @date 2019/5/15 下午1:47
     */
    @RequestMapping(value = "/delReview", method = RequestMethod.POST)
    @ResponseBody
    public int delReview(@RequestBody BatchUpReviewStatus updateReview, HttpServletRequest request) {
        if (updateReview.getRevIds().size() < 1) {
            new BizException(ErrorCode.PARAM_MISSING);
        }
        Map<String, Object> param = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(updateReview.getRevIds().get(0))) {
            param.put("revId", updateReview.getRevIds().get(0));
        }
        Review review = reviewQuery.getReview(param);
        if (!review.getPlatformId().equals(updateReview.getPlatformId())) {
            new BizException(ErrorCode.BIZ_EXCEPTION);
        }
        return reviewRepository.bachUpReviewStatus(updateReview);
    }


    /**
     * @return int
     * @Title 新增投诉与建议
     * @Param
     * @author Dong Xifu
     * @date 2019/5/21 上午10:45
     */
    @RequestMapping(value = "/saveAdvice", method = RequestMethod.POST)
    @ResponseBody
    public int saveAdvice(@RequestBody SaveAdvice saveAdvice, HttpServletRequest request) {
        System.out.println("saveAdvice -------");
        Long revId = uidGenerator.generate(BusinessTypeEnum.ADVICE);

        WxUserDTO dto = getWxUser(saveAdvice.getPlatformId(), null);

        saveAdvice.setAdviId(revId);
        saveAdvice.setNickname(dto.getNickname());
        saveAdvice.setChathead(dto.getHeadimgurl());
        saveAdvice.setAdviStatus(Advice.ADVICE_STATUS_START);
        saveAdvice.setModifiedUserId(dto.getNickname());

        Advice advice = saveAdvice.build();

        return adviceRepository.saveAdvice(advice);
    }



    /**
     * @return java.lang.Object
     * @Title 查询已消费过的店铺
     * @Param
     * @author Dong Xifu
     * @date 2019/5/21 上午11:21
     */
    @RequestMapping(value = "/getStoreListConsumed", method = RequestMethod.GET)
    @ResponseBody
    public Object getStoreListConsumed(@RequestParam(name = "platformId", defaultValue = "") String platformId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("platformId", platformId);

        return storeQuery.getStoreListConsumed(param);
    }

    /**
     * @return java.lang.Object
     * @Title 查询投诉列表
     * @Param
     * @author Dong Xifu
     * @date 2019/5/21 上午11:26
     */
    @RequestMapping(value = "/getAdviceList", method = RequestMethod.GET)
    @ResponseBody
    public Object getAdviceList(@RequestParam(name = "platformId", defaultValue = "") String platformId,
                                @RequestParam(name = "storeId", defaultValue = "") String storeId,
                                @RequestParam(name = "page", defaultValue = "1") int page,
                                @RequestParam(name = "size", defaultValue = "10") int size) {


        String query = "{advices(storeId:\"" + storeId + "\",platformId:\"" + platformId + "\",size:" + size + ", page:" + page + "){total,dataList{ adviId,adviType, platformId,nickname,storeId,storeName,adviContent,adviTime,linker,linkPhone,linkEmail,adviStatus,adviReplyContent,adviReplier,adviReplyTime,imgList{adviAttachId,contentUrl,displayOrder}    }  }}  ";
        Map reviewMap = (Map) getQueryHandler().handle(query);
        return reviewMap;
    }

    /**
     * @Title 删除投诉建议
     * @Param batchUpAdviceStatus
     * @author Dong Xifu
     * @date 2019/5/21 上午11:07
     */
    public int batchDelAdviceStatus(@RequestBody BatchUpAdviceStatus batchUpAdviceStatus, CommandHandlerContext context) {
        logger.debug("Start updateAdvice -------");
        batchUpAdviceStatus.setStatus(Advice.ADVICE_STATUS_DEL);

        return adviceRepository.bachUpAdviceStatus(batchUpAdviceStatus);
    }

    /**
     * 查询广告列表
     *
     * @Param
     * @return java.util.List<com.basoft.eorder.interfaces.query.AdvertDTO>
     * @author Dong Xifu
     * @date 2019/6/11 上午10:24
     */
    @RequestMapping(value = "/getAdvert", method = RequestMethod.GET)
    public AdvertDTO getAdvert(@RequestParam(defaultValue = "",name = "advType")String advType) {
        Map<String, Object> param = new HashMap<>();
        param.put("advStatus", Advertisement.UPSHELF);
        param.put("useYn",Advertisement.START);
        param.put("advType", advType);
        List<AdvertDTO> advertDTOList = advertQuery.getAdvertList(param);
        if (advertDTOList != null && advertDTOList.size() > 0) {
           return advertQuery.getAdvertList(param).get(0);
        }
        return new AdvertDTO();
    }


    @Override
    protected Object newQueryHandlerContext(HttpServletRequest request) {
        WxSession us = (WxSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
        Map<String, Object> context = new HashMap<>();
        context.put(AppConfigure.BASOFT_WX_SESSION_PROP, us);
        return context;
    }

    @Override
    protected Map<String, Object> newCommandHandlerContextMap(HttpServletRequest request) {
        WxSession session = (WxSession) request.getAttribute(AppConfigure.BASOFT_WX_SESSION_PROP);
        Map<String, Object> props = new HashMap<>();
        props.put("realIp", getRealIP(request));
        props.put(AppConfigure.BASOFT_WX_SESSION_PROP, session);
        return props;
    }

    private ProductGroupDTO hotOfGroup(Long storeId) {

        ProductGroupDTO groupDTO = new ProductGroupDTO();
        groupDTO.setId((long) -1);
        groupDTO.setCreated(java.util.Calendar.getInstance().getTime().toString());
        groupDTO.setNameChn("热销");
        groupDTO.setShowIndex(0);
        groupDTO.setStatus(0);
        groupDTO.setNameKor("추천");
        groupDTO.setStoreId(storeId);
        return groupDTO;
    }


    private Map hotOfGroupMap(Long storeId) {
        Map groupDTO = new LinkedHashMap();
        groupDTO.put("id", (long) -1);
        groupDTO.put("nameKor", "추천");
        groupDTO.put("nameChn", "热销");
        groupDTO.put("storeId", storeId);
        groupDTO.put("status", 1);
        groupDTO.put("showIndex", 0);
        groupDTO.put("created", java.util.Calendar.getInstance().getTime().toString());
        return groupDTO;
    }

    /**
     * 微信支付平台回调接口
     * 1.前端调用Order saveOrder接口提交订单,接口返回组合参数给前端
     * 2.前端将saveOrder接口返回的组合参数，传递给Wechat Pay JSSDK，拉起微信支付
     * 3.支付成功后，微信支付平台回调该接口
     *
     *（注：20200218新增到手价酒店saveHotelOrder）
     *
     *
     * 20200609新增注释（来自微信支付官方文档）：
     * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
     *
     * 支付完成后，微信会把相关支付结果及用户信息通过数据流的形式发送给商户，商户需要接收处理，并按文档规范返回应答。
     *
     * 注意：
     * 1、同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。
     * 2、后台通知交互时，如果微信收到商户的应答不符合规范或超时，微信会判定本次通知失败，重新发送通知，
     * 直到成功为止（在通知一直不成功的情况下，微信总共会发起多次通知，
     * 通知频率为15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h - 总计 24h4m），但微信不保证通知最终一定能成功。
     * 3、在订单状态不明或者没有收到微信支付结果通知的情况下，建议商户主动调用微信支付【查询订单API】确认订单状态。
     *
     * 特别提醒：
     * 1、商户系统对于支付结果通知的内容一定要做签名验证,并校验返回的订单金额是否与商户侧的订单金额一致，防止数据泄漏导致出现“假通知”，造成资金损失。
     * 2、当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，如果处理过直接返回结果成功。在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
     * 3、技术人员可登进微信商户后台扫描加入接口报警群，获取接口告警信息。
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/order_pay", method = RequestMethod.POST)
    @ResponseBody
    public String order_pay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 读取微信支付平台传递过来的XML数据流，解析为Map
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        BaseResult result = new BaseResult();
        String resultStr = "";
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        /*
         *返回XML样例
            <?xml version="1.0" encoding="utf-8"?>
            <xml>
              <appid><![CDATA[wx169f9463dac237ee]]></appid>
              <attach><![CDATA[635790301684634630]]></attach>
              <bank_type><![CDATA[CCB_DEBIT]]></bank_type>
              <cash_fee><![CDATA[301]]></cash_fee>
              <cash_fee_type><![CDATA[CNY]]></cash_fee_type>
              <device_info><![CDATA[707646140248298501]]></device_info>
              <fee_type><![CDATA[USD]]></fee_type>
              <is_subscribe><![CDATA[N]]></is_subscribe>
              <mch_id><![CDATA[1413386802]]></mch_id>
              <nonce_str><![CDATA[A1ldcfYnqVUYbZrmBsYbXpiMuUbDma1k]]></nonce_str>
              <openid><![CDATA[oJ_98wGAwuJznlOlQUlghmTq1E64]]></openid>
              <out_trade_no><![CDATA[BT1000000544]]></out_trade_no>
              <rate_value><![CDATA[717420000]]></rate_value>
              <result_code><![CDATA[SUCCESS]]></result_code>
              <return_code><![CDATA[SUCCESS]]></return_code>
              <sign><![CDATA[BA280202D7E47AAC02756FEC9F1C35FCEAE0ADD5BA301EB4A497578EA48667EC]]></sign>
              <sub_appid><![CDATA[wx7202c9b1660cc48d]]></sub_appid>
              <sub_is_subscribe><![CDATA[Y]]></sub_is_subscribe>
              <sub_mch_id><![CDATA[272399448]]></sub_mch_id>
              <sub_openid><![CDATA[o2dki5sQNSZ2_iJaFeNmJBu9_ai8]]></sub_openid>
              <time_end><![CDATA[20190904164643]]></time_end>
              <total_fee>42</total_fee>
              <trade_type><![CDATA[JSAPI]]></trade_type>
              <transaction_id><![CDATA[4200000349201909042562078986]]></transaction_id>
            </xml>
         */

        //解析xml成map
        Map<String, String> data = WXPayUtil.xmlToMap(sb.toString());
        logger.info("▶▶▶▶▶ order_pay Result sb.toString() ◀◀◀◀◀ \n" + sb.toString());


        Long storeId = NumberUtils.toLong(data.get("attach"));
        Store store = null;
        try {
            store = storeRepository.getStore(storeId);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        Order order = null;

        //判断签名是否正确
        WXPay wxPay = WechatPayAPI.getWXPay();
        if (!wxPay.isPayResultNotifySignatureValid(data, store.apiKey())) {
            logger.info("▶▶▶▶▶ order_pay Result isPayResult Notify Signature Valid Fail ◀◀◀◀◀");
            resultStr = WechatPayAPI.returnWXPayVerifyMsgError();
            return resultStr;
        } else {
            logger.info("▶▶▶▶▶ order_pay Result isPayResult Notify Signature Valid Success ◀◀◀◀◀");
            
            
            try {
				//Insert to DB：包括内容新增支付信息，根据临时订单生成正式订单信息等等
				order = orderService.receiveWechatOrder(data);
			} catch (Exception e) {
                // 回馈微信公众平台支付结果通知失败，需要微信公众平台重试再次通知
                resultStr = WechatPayAPI.returnWXPayVerifyMsgDealError();
                logger.error("▶▶▶▶▶ order_pay Result Data Write Failure:::: ◀◀◀◀◀ {}", e.getMessage());
                log.error("order_pay Result Data Write Failure::::", e);
                return resultStr;
			}
            
            
			// 返回成功给微信支付平台
            resultStr = WechatPayAPI.returnWXPayVerifyMsg();

            // 极光推送
            try {
                logger.info("▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶ 极光推送-START ◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
                Event event = new Event("order_pay", data);
                this.eventProducer.pushMsgSend(event, order.getStoreId().toString(), order.getTableId().toString(), String.valueOf(store.storeType()));
                logger.info("▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶ 极光推送-END ◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀");
            } catch (Throwable e) {
                logger.error("▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶ 极光推送-ERROR ◀◀◀◀◀◀◀◀◀◀◀◀◀◀◀" + e.getMessage());
                e.printStackTrace();
            }
        }
        
        if (order != null) {
            //Send msg
            Order msgOrder = orderRepository.getOrder(order.getTransId());
            sendTemplateMsgBySaveOrder(msgOrder, store, "orderPay");
        }
        
        
        return resultStr;
    }

    /**
     * @return java.lang.Object
     * @Param url
     * @describe 查询店铺列表
     * @author dikim
     * @date 2019/3/8
     */
    @RequestMapping(value = "/wxjssdk", method = RequestMethod.GET)
    @ResponseBody
    public Object getWxTicket(HttpServletRequest request, @RequestParam(name = "url", defaultValue = "") String url) {
        String ticket = "";
        try {
            ticket = Objects.toString(redisUtil.get("wx_ticket"), null);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        Long longTimestamp = WXPayUtil.getCurrentTimestamp();
        String timestamp = longTimestamp.toString();
        String nonce_str = WXPayUtil.generateNonceStr();
        String signature = "";

        String str = "jsapi_ticket=" + ticket
                + "&noncestr=" + nonce_str
                + "&timestamp=" + timestamp
                + "&url=" + url;

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(str.getBytes("UTF-8"));
            signature = WXPayUtil.byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("NoSuchAlgorithmException message:" + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("UnsupportedEncodingException message:" + e.getMessage());
        }

        Map<String, Object> jsSdk = new HashMap<>();
        jsSdk.put("signature", signature);
        jsSdk.put("timestamp", timestamp);
        jsSdk.put("nonce_str", nonce_str);
        jsSdk.put("ticket", ticket);

        return jsSdk;
    }

    /**
     * @return com.basoft.eorder.interfaces.query.WxUserDTO
     * @Title 获取用户信息
     * @Param
     * @author Dong Xifu
     * @date 2019/5/16 上午10:12
     */
    public WxUserDTO getWxUser(String openId, String shopId) {
        Map<String, Object> param = new HashMap<>();
        param.put("openId", openId);
        WxUserDTO wxUserDTO = wxUserQuery.getWxUser(param);
        // 出现该情况原因：一、关注时服务存在（网络或者服务停止）问题，没有同步到用户信息到本地
        if (wxUserDTO == null){
            logger.info("<><><><><><><>【用户信息重取】数据库无用户信息，开始从微信公众平台获取<><><><><>");

            // 获取access_token
            String access_token = Objects.toString(redisUtil.get("wx_token"), null);
            if(access_token == null){
                throw new BizException(ErrorCode.BIZ_EXCEPTION);
            }
            logger.info("<><><><><><><>【用户信息重取】access_token<><><><><>" + access_token);

            param.put("access_token",access_token);

            // shopId为null,数据库获取shopId
            if(shopId == null){
                String sysId = "1";
                WxAppInfoTable wt = wxAppInfoTableRepository.getWxAppInfoTable(sysId);
                shopId = String.valueOf(wt.getShopId());
            }
            logger.info("<><><><><><><>【用户信息重取】shopId<><><><><>" + shopId);
            param.put("shopId",shopId);

            // 生成微信用户CUST ID
            Long custId = uidGenerator.generate(BusinessTypeEnum.WX_CUST);
            logger.info("<><><><><><><>【用户信息重取】custId<><><><><>" + custId);
            param.put("custId",custId);

            wxUserDTO = wxUserQuery.reGetWxUser(param);

            // 如果用户信息重取为空，此时原因：1：用户取消关注；2：网络不通，获取错误。
            // 提示用户重新尝试关注
            // TODO
            if(wxUserDTO == null){
                throw new BizException(ErrorCode.BIZ_EXCEPTION);
            }
        }

        Base64.Decoder decoder = Base64.getDecoder();
        String nickNm = "";
        try {
           nickNm = new String(decoder.decode(wxUserDTO.getNickname().getBytes()), "UTF-8");
        } catch (Exception e) {
            nickNm = wxUserDTO.getNickname();
        }
        wxUserDTO.setNickname(nickNm);
        return wxUserDTO;
    }

    /**
     * 发送订单生成的模板消息
     *
     * @param order
     * @param store
     * @param msgFlag
     */
    private void sendTemplateMsgBySaveOrder(Order order, Store store, String msgFlag) {
        String orderId = order.getId()+"";
        String payAmtCny = order.getPayAmtCny() + "";
        String payAmtKrw = order.getAmount() + "";
        String templateId = "";

        try {
            //get TemplateMsg Config
            Map<String,Object> templateMsgConfig = (Map<String, Object>) appConfigure.getObject(AppConfigure.BASOFT_WX_TEMPLATE_CONFIG).get();
            
            //get token
            String token = Objects.toString(redisUtil.get("wx_token"), null);
            logger.info("Wechat Access Token::::::" + token);

            //get token
            String DEFAUT_COLOR = "#173177";
            Map<String, DataItem> data = new HashMap<String, DataItem>();

            if ("orderPay".equals(msgFlag)) {
                
            	if (store.storeType() == 2) {	//店铺类型(1:餐厅 2:医院 3:购物 4:酒店)
                    templateId = (String)templateMsgConfig.get("clinic_booking_confirm");

                    String reseveTime = "下午";
                    if (order.getReseveTime() == 1) {
                    	reseveTime = "上午";
                    }

                    String remark = "医院相关问题，请在聊天窗口中输入“3”. \n" + 
                    		"如客服不在线，您也可到”个人中心“->”投诉，建议“中留言，我们会尽快给您答复。\n" + 
                    		"如需电话沟通，请拨打热线：+82 64-748-0689.";
                    
                    data.put("first", new DataItem("亲爱的"+order.getCustNm()+" 您好，感谢您的预约。 我们会在在24小时内把预约结果通知您。", DEFAUT_COLOR));
                    data.put("storeName", new DataItem(store.name(), DEFAUT_COLOR));
                    data.put("bookTime", new DataItem(order.getReseveDtfrom() + " " + reseveTime, DEFAUT_COLOR));
                    data.put("orderId", new DataItem(orderId, DEFAUT_COLOR));
                    data.put("orderType", new DataItem(order.getProductNm(), DEFAUT_COLOR));
                    data.put("remark", new DataItem(remark, DEFAUT_COLOR));
            	}
            	else if (store.storeType() == 4) {	//店铺类型(1:餐厅 2:医院 3:购物 4:酒店)
            		
            		if (order.getOrderType() == 2) {
            			return;
            		}
            		
                    templateId = (String)templateMsgConfig.get("hotel_booking_confirm");

                    String remark = "如客服不在线，您也可到”个人中心“->”投诉，建议“中留言，我们会尽快给您答复。\n" +
                    		"如需电话沟通，请拨打热线：+82 64-748-0689.";
                    		
                    data.put("first", new DataItem("感谢您预约"+store.name()+"酒店。 我们会在24小时内把预约结果通知您。", DEFAUT_COLOR));
                    data.put("order", new DataItem(orderId, DEFAUT_COLOR));
                    data.put("Name", new DataItem(order.getCustNm(), DEFAUT_COLOR));
                    data.put("datein", new DataItem(order.getReseveDtfrom(), DEFAUT_COLOR));
                    data.put("dateout", new DataItem(order.getReseveDtto(), DEFAUT_COLOR));
                    data.put("number", new DataItem("1间", DEFAUT_COLOR));
                    data.put("room type", new DataItem(order.getProductNm(), DEFAUT_COLOR));
                    data.put("pay", new DataItem("￥ " + payAmtCny + "(￦" + payAmtKrw + ")", DEFAUT_COLOR));
                    data.put("remark", new DataItem(remark, DEFAUT_COLOR));
            	}
            	else {
            		return;
            	}
            	
            }
            else {
        		return;
            }
        	
            TemplateMessageReturn result = WechatAPI.sendTemplateMessage(token, order.getCustomerId(), templateId, "", DEFAUT_COLOR, data);
            logger.info("TemplateMessageReturn::::::" + result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成[min,max]之间的随机整数
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomNum(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    public static void main(String[] args) {
        System.out.println(randomNum(1000000,9999999));
    }
}