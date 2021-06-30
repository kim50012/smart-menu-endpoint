package com.basoft.eorder.interfaces.controller.h5.restaurant;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.WxSession;
import com.basoft.eorder.application.framework.CommandHandleEngine;
import com.basoft.eorder.application.framework.QueryHandler;
import com.basoft.eorder.domain.StoreRepository;
import com.basoft.eorder.domain.StoreTableRepository;
import com.basoft.eorder.domain.model.Store;
import com.basoft.eorder.domain.model.StoreTable;
import com.basoft.eorder.interfaces.controller.CQRSAbstractController;
import com.basoft.eorder.interfaces.controller.Echo;
import com.basoft.eorder.interfaces.controller.h5.common.CommonService;
import com.basoft.eorder.interfaces.query.PaginationDTO;
import com.basoft.eorder.interfaces.query.WxUserDTO;
import com.basoft.eorder.interfaces.query.restaurant.RestaurantStoreQuery;
import com.basoft.eorder.interfaces.query.restaurant.RestaurantStoreQueryDTO;
import com.basoft.eorder.interfaces.query.restaurant.RestaurantStoreVO;
import com.basoft.eorder.interfaces.query.topic.BaseTopicDTO;
import com.basoft.eorder.interfaces.query.topic.BaseTopicQuery;
import com.basoft.eorder.util.RedisUtil;
import com.basoft.eorder.util.StoreUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Wechat/MiniProgram/H5 Electronic Order API 公众号端电子点餐类业务API
 *
 * @version 1.0
 * @Date 20200114
 **/
@Slf4j
@Controller
@RequestMapping("/wechat/api/v2/r")
public class SpringRestaurantController extends CQRSAbstractController {
    @Autowired
    private RestaurantStoreQuery restaurantStoreQuery;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BaseTopicQuery baseTopicQuery;

    @Autowired
    private StoreTableRepository storeTableRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CommonService commonService;

    @Autowired
    public SpringRestaurantController(
            QueryHandler queryHandler,
            CommandHandleEngine handleEngine) {
        super(queryHandler, handleEngine);
    }

    /**
     * Wechat/MiniProgram/H5
     * 电子点餐首页查询接口/Electronic Order main page api
     * <p>
     * 提醒：前端注意处理无可用状态的往后显示
     *
     * @param request
     * @param restaurantStoreQueryDTO
     * @return
     */

    /* 传参示例一
    简洁查询
    {
        "channel": "wechat",
        "city": "0017",
        "longitude": "116.53889",
        "latitude": "40.099156",
        "storeType": "1",
        "key": "餐",
        "topicType": "1",
        "orderBy": "1",
        "orderSort": "1",
        "page": 1,
        "size": 20,
        "qmd":"s"
    }
     */

    /* 传参示例二
    {
        "channel": "wechat",
        "city": "0017",
        "longitude": "116.53889",
        "latitude": "40.099156",
        "storeType": "1",
        "key": "餐",
        "topicType": "1",
        "topic": ["1111","222","333"],
        "queryScore": "4.0",
        "isSelfService": "1",
        "minDistance":"",
        "maxDistance":"",
        "minPrice":"",
        "maxPrice":"",
        "storeExtends":["BREAKFAST@1","HOTEL_DINING_ROOM@1","HOTEL_PARK@1","HOTEL_TRANSFER@1"],
        "orderBy": "1",
        "orderSort": "1",
        "page": 1,
        "size": 20,
        "qmd":"c"
    }

        距离价格传值规范：1、最小距离和最大距离 2、最大距离；其他情况不允许。如0-150,150-300,300-600,600+
        扩展属性传值规范：字段对应的编码@字段值编码，如HOTEL_COMFORT_LEVEL@5,HOTEL_BREAKFAST@1
     */
    @RequestMapping(value = "/rmain", method = RequestMethod.POST)
    @ResponseBody
    public PaginationDTO restaurantMain(HttpServletRequest request,
                                        @RequestBody RestaurantStoreQueryDTO restaurantStoreQueryDTO) {
        log.info("餐厅主页商户列表查询接口接收到的参数RestaurantStoreQueryDTO>>>>>>{}", restaurantStoreQueryDTO);

        // 核心参数验证
        String city = restaurantStoreQueryDTO.getCity();
        if (city == null || "".equals(city.trim())) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 处理默认值。channel/page/size
        // 转换order by和order sort
        restaurantStoreQueryDTO.initValue();

        // 嵌套查询：对一级结果过滤查询
        int dataCount = 0;
        if ("c".equals(restaurantStoreQueryDTO.getQmd())) {
            // 获取餐厅数量
            dataCount = restaurantStoreQuery.getRestaurantStoreComplexCount(restaurantStoreQueryDTO);
            if (dataCount == 0) {
                return new PaginationDTO<>(0, new ArrayList<>());
            }

            // 查询酒店列表-默认按距离排序，如果没有授权地理位置，则按默认排序
            List<RestaurantStoreVO> restaurantStoreList = restaurantStoreQuery.getRestaurantStoreComplexList(restaurantStoreQueryDTO);
            return new PaginationDTO<>(dataCount, restaurantStoreList);
        }
        // 简洁查询
        else {
            // 获取餐厅数量
            dataCount = restaurantStoreQuery.getRestaurantStoreSimpleCount(restaurantStoreQueryDTO);
            if (dataCount == 0) {
                return new PaginationDTO<>(0, new ArrayList<>());
            }

            // 查询酒店列表-默认按距离排序，如果没有授权地理位置，则按默认排序
            List<RestaurantStoreVO> restaurantStoreList = restaurantStoreQuery.getRestaurantStoreSimpleList(restaurantStoreQueryDTO);
            return new PaginationDTO<>(dataCount, restaurantStoreList);
        }
    }

    /**
     * 查询餐厅首页的topic主题
     *
     * @return
     */
    @RequestMapping(value = "/rtopic", method = RequestMethod.GET)
    @ResponseBody
    public Object restaurantMainTopic() {
        // 缓存中获取主题信息
        List<Object> dataList = redisUtil.lGet("restaurantBaseTopicList", 0, -1);

        List<BaseTopicDTO> restaurantBaseTopicList = null;
        if(dataList == null || dataList.isEmpty()){
            // 缓存中没有数据则进行数据库查询
            restaurantBaseTopicList = baseTopicQuery.getRestaurantBaseTopicList();
        } else {
            restaurantBaseTopicList = (List<BaseTopicDTO>) dataList.get(0);
        }

        if (restaurantBaseTopicList == null || restaurantBaseTopicList.isEmpty()) {
            return new Echo(null,ErrorCode.SYS_EMPTY.getCode(), ErrorCode.SYS_EMPTY.getMsg());
        }
        log.debug("餐厅主题列表信息>>>>>>{}", restaurantBaseTopicList);

        // 过滤出-主显示主题（tpDisType-显示类型 1-主显示 2-筛选显示）
        List<BaseTopicDTO> restaurantMainTopicList = restaurantBaseTopicList.stream()
                .filter(baseTopicDTO -> (baseTopicDTO.getTpDisType() == 1))
                .collect(Collectors.toList());
        Map<String, List<BaseTopicDTO>> restaurantMainTopicMap =
                restaurantMainTopicList.stream().collect(Collectors.groupingBy(BaseTopicDTO::getTpFuncType));

        // 整理出-对筛选显示的主题（tpDisType-显示类型 1-主显示 2-筛选显示。）
        // 筛选/查询条件中显示的主题不仅仅是tpDisType类型为2的，tpDisType类型为1的同样显示
        Map<String, List<BaseTopicDTO>> restaurantFiltrateTopicMap =
                restaurantBaseTopicList.stream().collect(Collectors.groupingBy(BaseTopicDTO::getTpFuncType));

        Map<String, Object> topicInfo = Maps.newHashMap();
        topicInfo.put("restaurantMainTopicMap", restaurantMainTopicMap);
        topicInfo.put("restaurantFiltrateTopicMap", restaurantFiltrateTopicMap);
        // return new Echo(topicInfo);
        return topicInfo;
    }

    /**
     * 迁移自SpringWechatController类中的initBaSoftSelfService方法
     *
     * 目标：为了实现餐厅整体改造后，点击非店外点餐餐厅可以进入菜谱页面，新增该方法。实现非店外点餐餐厅的登陆。
     *
     * ::非自助点餐餐厅的登录
     *
     * 点击“进店”按钮，执行该逻辑，入参门店ID和openid，返回token、是否
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

        boolean canReserve = true;
        StoreTable st = storeTableRepository.getSilentStoreTable(storeId);
        logger.info("Get StoreTable:" + st);
        // 如果该门店没有静默桌号则抛出错误
        if(st == null || StringUtils.isBlank(st.getQrCodeId())){
            canReserve = false;
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

        WxUserDTO wxUserDTO = commonService.getWxUser(openId, null);

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
        returnObj.put("canReserve", "" + canReserve);
        return returnObj;
    }
}