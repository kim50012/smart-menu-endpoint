package com.basoft.eorder.interfaces.controller.h5.retail;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.WxSession;
import com.basoft.eorder.application.framework.CommandHandleEngine;
import com.basoft.eorder.application.framework.QueryHandler;
import com.basoft.eorder.batch.job.model.retail.RetailToDoRecoverTempOrder;
import com.basoft.eorder.batch.job.threads.retail.RetailOrderRecoverThread;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.OrderRepository;
import com.basoft.eorder.domain.model.Order;
import com.basoft.eorder.domain.model.post.CustPostAddress;
import com.basoft.eorder.domain.model.retailOrderService.RetailOrderService;
import com.basoft.eorder.domain.order.retail.RetailOrderEventRepository;
import com.basoft.eorder.domain.post.CustPostAddressRepository;
import com.basoft.eorder.domain.retail.InventoryRetailRepository;
import com.basoft.eorder.domain.retail.RetailOrderRepository;
import com.basoft.eorder.interfaces.command.retail.SaveRetailOrderEvent;
import com.basoft.eorder.interfaces.controller.CQRSAbstractController;
import com.basoft.eorder.interfaces.query.OrderQuery;
import com.basoft.eorder.interfaces.query.order.retail.RetailOrderQuery;
import com.basoft.eorder.interfaces.query.post.CustPostAddressQuery;
import com.basoft.eorder.interfaces.query.retail.api.ProductAloneStandardInfoVO;
import com.basoft.eorder.interfaces.query.retail.api.RetailProductInfoVO;
import com.basoft.eorder.interfaces.query.retail.api.RetailProductQuery;
import com.basoft.eorder.interfaces.query.retail.api.RetailProductSkuInfoVO;
import com.basoft.eorder.util.ExchangeRateUtil;
import com.basoft.eorder.util.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Wechat/MiniProgram/H5 Retail API 公众号端零售商城类业务API
 *
 * @version 1.0
 * @Date 20200413
 **/
@Slf4j
@Controller
@RequestMapping("/wechat/api/v2/retail")
public class SpringRetailController extends CQRSAbstractController {
    @Autowired
    private RetailProductQuery retailProductQuery;

    @Autowired
    private OrderQuery orderQuery;

    @Autowired
    private RetailOrderQuery retailOrderQuery;

    @Autowired
    private InventoryRetailRepository inventoryRetailRepository;

    @Autowired
    private CustPostAddressRepository custPostAddressRepository;

    @Autowired
    private CustPostAddressQuery custPostAddressQuery;

    @Autowired
    private ExchangeRateUtil exchangeRateUtil;

    @Autowired
    private RetailOrderEventRepository retailOrderEventRepository;

    @Autowired
    private RetailOrderRepository retailOrderRepository;

    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    public SpringRetailController(
            QueryHandler queryHandler,
            CommandHandleEngine handleEngine) {
        super(queryHandler, handleEngine);
    }

    // TODO 接口I 零售业务首页

    /**
     * 接口II
     * Retail商户H5查询步骤：
     * 1、/maindisc接口查询产品列表，查询内容包括产品信息（产品表），默认sku的价格、活动价及库存信息，还有产品的列表图片及产品的图片集合
     * {
     *                     "id": "869112853785023497",
     *                     "storeId": "635791970942129161",
     *                     "categoryId": null,
     *                     "nameKor": "鞋",
     *                     "nameChn": "鞋",
     *                     "detailDesc": "<p>ererterter</p>",
     *                     "detailChnDesc": "",
     *                     "created": "2020-04-14 12:32:05.0",
     *                     "desChn": "鞋鞋鞋鞋鞋鞋鞋鞋鞋鞋鞋鞋鞋鞋鞋鞋鞋鞋",
     *                     "desKor": "鞋鞋鞋鞋鞋鞋鞋鞋鞋鞋鞋鞋鞋鞋",
     *                     "fileId": null,
     *                     "fileName": null,
     *                     "fileSysName": null,
     *                     "fileType": null,
     *                     "fileUrl": null,
     *                     "fileSize": 0,
     *                     "fileOriginalName": null,
     *                     "productId": "869112853785023497",
     *                     "prdGroupId": "645877466499716096",
     *                     "showIndex": 1,
     *                     "mainImageUrl": "https://wechatplace-stg-1259663537.cos.ap-seoul.myqcloud.com/jeju-images/f570f4bf-0ec9-49ac-b340-9fa7163f2431.png",
     *                     "subImageUrl": "https://wechatplace-stg-1259663537.cos.ap-seoul.myqcloud.com/jeju-images/29751be1-0fc2-4bb8-bb42-697469506a39.png,https://wechatplace-stg-1259663537.cos.ap-seoul.myqcloud.com/jeju-images/057c4d03-9a14-45a6-a71c-626c328d0dec.png",
     *                     "recommend": 0,
     *                     "defaultSkuId": "869112853810713610",
     *                     "defaultSkuPriceKor": 300,
     *                     "defaultSkuPriceChn": 1.80,
     *                     "defaultSkuDiscPriceKor": 210,
     *                     "defaultSkuDiscPriceChn": 1.26,
     *                     "defaultSkuIsInv": 1,
     *                     "defaultSkuInv": "5",
     *                     "skuCount": "6"
     *                 }
     *
     *
     * 2、查询指定产品的规格信息以及sku列表
     * 规格信息：
     *
     * sku信息：sku信息（product_sku表）；sku图片信息，字符串拼接，中间逗号隔开；活动价格及对应的活动id
     * {
     *                 "prodSkuId": "864172737010930691",
     *                 "nameKor": "240test3345333454345",
     *                 "nameChn": "240testxl",
     *                 "productId": "645876071758369795",
     *                 "priceKor": 1000,
     *                 "priceChn": 5.99,
     *                 "isInventory": 0,
     *                 "disOrder": 0,
     *                 "useDefault": true,
     *                 "prodSkuInv": "0",
     *                 "skuStdInfo": "",
     *                 "skuImageInfo": null,
     *                 "skuDiscId": "864229339984368649",
     *                 "skuDiscPriceKor": 900,
     *                 "skuDiscPriceChn": 5.39
     *             }
     */
    /**
     * 查询零售商户的产品列表(含产品默认SKU价格及其折扣)
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/maindisc", method = RequestMethod.GET)
    @ResponseBody
    public Object mainProducts(HttpServletRequest request) {
        // 1、获取门店ID
        WxSession wxSession = (WxSession) request.getAttribute(AppConfigure.BASOFT_WX_SESSION_PROP);
        long storeId = wxSession.getStoreId();

        // 2、查询该商户的产品列表(含产品默认SKU价格及其折扣)
        StringBuilder productQuery = new StringBuilder(512);
        productQuery
                .append("{");
        // ProductGroups
        productQuery
                .append("ProductGroups(storeId:").append(storeId).append(")")
                .append("{dataList{id, nameKor, nameChn, storeId, status, showIndex, created}}");

        productQuery
                .append(",");

        // RetailProductGroupMaps
        productQuery
                .append("RetailProductGroupMaps(storeId:").append(storeId).append(")")
                .append("{id, storeId, categoryId, nameKor, nameChn, weight, detailDesc, detailChnDesc, created, desChn, desKor, fileId, fileName, fileSysName, fileType, fileUrl, fileSize, fileOriginalName, productId, prdGroupId, showIndex, mainImageUrl, subImageUrl, recommend, defaultSkuId, defaultSkuPriceKor,defaultSkuPriceChn, defaultSkuDiscPriceKor, defaultSkuDiscPriceChn, defaultSkuIsInv, defaultSkuInv, skuCount}");
        productQuery
                .append("}");

        Map map = (Map) getQueryHandler().handle(productQuery.toString());
        if (map == null) {
            return null;
        }

        // 产品组列表
        List<Map> groups = (List<Map>) ((Map) map.get("ProductGroups")).get("dataList");
        // 产品列表
        List<Map> products = ((List<Map>) map.get("RetailProductGroupMaps"));

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
            Map hotGroup = hotOfGroupMap(storeId);
            hotGroup.put("products", hotProducts);
            groupList.add(0, hotGroup);
        }

        return groupList;
    }

    /**
     * 热销分组
     *
     * @param storeId
     * @return
     */
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
     * 接口III : 查询零售商户的指定产品的全部信息：规格定义，SKU列表及折扣信息，SKU库存信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    @ResponseBody
    public RetailProductInfoVO productInfo(HttpServletRequest request, @RequestBody Map<String, String> params) {
        // 1、获取门店ID，检验接口调用合法性
        WxSession wxSession = (WxSession) request.getAttribute(AppConfigure.BASOFT_WX_SESSION_PROP);
        Long storeId = wxSession.getStoreId();
        if (storeId == null) {
            // 抛出业务异常信息
            throw new BizException(ErrorCode.BIZ_EXCEPTION);
        }
        // 转换为Long，防止SQL注入
        Long productId = Long.parseLong(params.get("pid"));

        // 2、构造返回信息
        RetailProductInfoVO retailProductInfo = new RetailProductInfoVO();

        // 2-1 放入产品ID
        retailProductInfo.setId(productId);

        // 2-2查询产品规格信息
        List<ProductAloneStandardInfoVO> stdInfoList = retailProductQuery.getRetailProductAloneStandardInfoList(productId);
        retailProductInfo.setStdInfoList(stdInfoList);

        // 2-3查询产品sku信息
        List<RetailProductSkuInfoVO> retailProductSkuInfoList = retailProductQuery.getRetailProductInfoList(productId);
        retailProductInfo.setSkuInfoList(retailProductSkuInfoList);

        return retailProductInfo;
    }





    /**************************************************订单****************************************************/
    /**
     * 购物类订单详情（参考订单详情接口）
     *
     * @param oid
     * @return
     * @see
     * /eorder/wechat/api/v1/query?param=%7BOrders(id:%221000000881%22)%7Btotal,
     * +dataList%7BnmLastEn,nmFirstEn,custNo,diningPlace,diningTime,reseveDtfrom,
     * reseveConfirmtime,reseveDtto,reseveTime,numPersons,shippingType,shippingAddrNm,
     * custNm,shippingAddrNm,countryNo,mobile,cmt,shippingDt,payAmtRmb,revId,id,storeId,
     * storeType,created,amount,paymentAmount,discountAmount,payAmtCny,payAmtUsd,buyerMemo,
     * logoUrl,storeNm,tableId,tableNum,tableTag,openId,status,qty,
     * shippingMode,shippingModeNameChn,shippingModeNameKor,shippingModeNameEng,shippingAddrDetail,shippingAddrCountry,shippingWeight,shippingCost,shippingCostRule,itemList%7Bid,
     * orderId,prodNmKor,prodNmChn,skuId,skuNmKor,skuNmChn,price,priceCny,qty,created,
     * prodUrl,prodNmKor,prodNmChn%7D%7D%7D%7D
     *
     * {Orders(id:"1000000881"){total,+dataList{nmLastEn,nmFirstEn,custNo,diningPlace,
     * diningTime,reseveDtfrom,reseveConfirmtime,reseveDtto,reseveTime,numPersons,shippingType,
     * shippingAddrNm,custNm,shippingAddrNm,countryNo,mobile,cmt,shippingDt,payAmtRmb,revId,id,
     * storeId,storeType,created,amount,paymentAmount,discountAmount,payAmtCny,payAmtUsd,buyerMemo,
     * logoUrl,storeNm,tableId,tableNum,tableTag,openId,status,qty,
     * shippingMode,shippingModeNameChn,shippingModeNameKor,shippingModeNameEng,shippingAddrDetail,shippingAddrCountry,shippingWeight,shippingCost,shippingCostRule,
     * itemList{id,orderId,prodNmKor,prodNmChn,skuId,skuNmKor,skuNmChn,price,priceCny,
     * qty,created,prodUrl,prodNmKor,prodNmChn}}}}
     */
    @RequestMapping(value = "/roi/{oid}", method = RequestMethod.GET)
    @ResponseBody
    public Object retailOrderInfo(@PathVariable("oid") String oid) {
        if (StringUtils.isBlank(oid)) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        StringBuilder query = new StringBuilder(700);
        query.append("{Orders(id:\"").append(oid).append("\")")
        .append("{total,+dataList{nmLastEn,nmFirstEn,custNo,diningPlace,")
        .append("diningTime,reseveDtfrom,reseveConfirmtime,reseveDtto,reseveTime,numPersons,shippingType,")
        .append("shippingAddrNm,custNm,shippingAddrNm,countryNo,mobile,cmt,shippingDt,payAmtRmb,revId,id,")
        .append("storeId,storeType,created,amount,paymentAmount,discountAmount,payAmtCny,payAmtUsd,buyerMemo,")
        .append("logoUrl,storeNm,tableId,tableNum,tableTag,openId,status,changeStatus,qty,")
        .append("shippingMode,shippingModeNameChn,shippingModeNameKor,shippingModeNameEng,shippingAddrDetail,shippingAddrCountry,shippingWeight,shippingCost,shippingCostRule,")
        .append("itemList{id,orderId,prodNmKor,prodNmChn,skuId,skuNmKor,skuNmChn,price,priceCny,")
        .append("qty,created,prodUrl,prodNmKor,prodNmChn}}}}");

        Map map = (Map) getQueryHandler().handle(query.toString());
        if (map == null) {
            logger.info("========== my order is empty =========");
            return null;
        }

        int total = NumberUtils.toInt(Objects.toString(((Map) map.get("Orders")).get("total")));
        if (total == 0) {
            logger.info("========== search my order result is 0 =========");
            return null;
        }

        // 查询订单事件列表
        List<Map<String, Object>> retailOrderEventList = retailOrderQuery.queryRetailOrderEventList(Long.valueOf(oid));
        map.put("orderEvents", retailOrderEventList);

        // 查询订单事件列表
        List<Map<String, Object>> retailOrderServiceList = retailOrderQuery.queryRetailOrderServiceList(Long.valueOf(oid));
        map.put("orderServices", retailOrderServiceList);

        return map;
    }

    /**
     * 购物类订单用户退款-支付成功后商户接单前的退款申请
     *
     * @param saveRetailOrderEvent
     * @return
     */
    @RequestMapping(value = "/ror3", method = RequestMethod.POST)
    @ResponseBody
    public String retailOrderEventRefund3(HttpServletRequest request
            , @RequestBody SaveRetailOrderEvent saveRetailOrderEvent) {
        if (saveRetailOrderEvent.getOrderId() == null || saveRetailOrderEvent.getOrderId().longValue() == 0) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }
        saveRetailOrderEvent.setEventType(3);
        return retailOrderEventRepository.saveRetailOrderEventAndOrderStatus(saveRetailOrderEvent.build());
    }

    /**
     * 购物类订单用户退款-商户接单后发货前的用户申请退款8
     *
     * @param saveRetailOrderEvent
     * @return
     */
    @RequestMapping(value = "/ror8", method = RequestMethod.POST)
    @ResponseBody
    public String retailOrderEventRefund8(HttpServletRequest request
            , @RequestBody SaveRetailOrderEvent saveRetailOrderEvent) {
        if (saveRetailOrderEvent.getOrderId() == null || saveRetailOrderEvent.getOrderId().longValue() == 0) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }
        saveRetailOrderEvent.setEventType(8);
        return retailOrderEventRepository.saveRetailOrderEventAndOrderStatus(saveRetailOrderEvent.build());
    }

    /**
     * 购物类订单用户确认收货-商户接单后发货前的用户申请退款8
     *
     * @param saveRetailOrderEvent
     * @return
     */
    /*
        入参：
        {
            "orderId": 1000000881
        }
        返回值：1-正常 0-失败 异常-失败
     */
    @RequestMapping(value = "/roe9", method = RequestMethod.POST)
    @ResponseBody
    public int retailOrderEventEnd9(HttpServletRequest request
            , @RequestBody SaveRetailOrderEvent saveRetailOrderEvent) {
        if (saveRetailOrderEvent.getOrderId() == null || saveRetailOrderEvent.getOrderId().longValue() == 0) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }
        return retailOrderRepository.endRetailOrder(saveRetailOrderEvent);
    }

    /**
     * 购物类订单退换货/售后申请
     *
     * @param retailOrderService
     * @return
     */
    /*
        入参：
        {
            "orderId": 1000000881,
            "servType":1,
            "applyCount":1,
            "applyDesc":"申请说明",
            "applyImages":"申请图片。最多支持9张图片，图片URL拼接成英文逗号分隔的字符串保存在该字段"
        }
        返回值：1-正常 0-失败 异常-失败
     */
    @RequestMapping(value = "/ros", method = RequestMethod.POST)
    @ResponseBody
    public int retailOrderService(HttpServletRequest request
            , @RequestBody RetailOrderService retailOrderService) {
        Long orderId = retailOrderService.getOrderId();
        if (orderId == null || orderId == 0) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 查询订单信息
        Order order = orderRepository.getOrder(orderId);
        if (order == null ) {
            throw new BizException(ErrorCode.SYS_EMPTY);
        }

        // 更新订单，记录售后申请信息
        return retailOrderRepository.retailOrderService(retailOrderService, order);
    }
    /**************************************************订单****************************************************/





    /**************************************************其他接口****************************************************/
    /**
     * 接口其他I：retail pay fail or cancel，进行该作废订单所占库存的释放
     *
     * @param request
     * @param params  {
     *                "o":"1002423423566664"
     *                }
     * @return
     */
    @RequestMapping(value = "/rpf", method = RequestMethod.POST)
    @ResponseBody
    public String retailPayFail(HttpServletRequest request, @RequestBody Map<String, String> params) {
        // 下单未支付订单编号
        String tempOrderId = params.get("o");
        if (StringUtils.isBlank(tempOrderId)) {
            return "fail";
        }

        // 根据订单ID查询订购信息
        List<RetailToDoRecoverTempOrder> tempOrderList = orderQuery.queryRetailTempOrderByIdToRecover(tempOrderId, "auto");
        if (tempOrderList == null || tempOrderList.isEmpty()) {
            return "fail";
        } else {
            // 启动库存恢复线程
            try {
                Thread thread = new Thread(new RetailOrderRecoverThread(inventoryRetailRepository,
                        tempOrderList, CommonConstants.HOTEL_INVENTORY_RECOVER_AUTO));
                thread.start();
                return "success";
            } catch (Exception e) {
                log.error("【酒店预订下单未支付订单所占库存实时恢复】酒店库存恢复线程异常，异常信息为：" + e.getMessage(), e);
            }
        }
        return "fail";
    }

    /**
     * 接口其他II：获取汇率
     *
     * @return
     */
    @RequestMapping(value = "/exchangeRate", method = RequestMethod.GET)
    @ResponseBody
    public String getExchangeRate() {
        return exchangeRateUtil.getNowKrwCnyRate().toString();
    }
    /**************************************************其他接口****************************************************/





    /**************************************************用户配送地址管理****************************************************/
    /**
     * 配送地址列表
     *
     * @param queryType a-查询所有状态的配送地址 p-查询可用的配送地址 d-查询默认的配送地址
     * @param request
     * @return
     */
    @RequestMapping(value = "/postAddressList", method = RequestMethod.GET)
    @ResponseBody
    public List<CustPostAddress> postAddressList(@RequestParam(name = "type", defaultValue = "a") String queryType,
                                                 HttpServletRequest request) {
        // 获取openId
        WxSession wxSession = (WxSession) request.getAttribute(AppConfigure.BASOFT_WX_SESSION_PROP);
        String wxOpenId = wxSession.getOpenId();
        if (StringUtils.isBlank(wxOpenId)) {
            throw new BizException(ErrorCode.SYS_TIMEOUT);
        }
        List<CustPostAddress> postAddressList = custPostAddressQuery.getPostAddressList(wxOpenId, queryType);
        return postAddressList;
    }

    /**
     * 平台客户新增配送地址
     *
     * @param custPostAddress
     * @param request
     * @return
     */
    @RequestMapping(value = "/savePostAddress", method = RequestMethod.POST)
    @ResponseBody
    public int savePostAddress(@RequestBody CustPostAddress custPostAddress, HttpServletRequest request) {
        // 获取openId，检验接口调用合法性
        WxSession wxSession = (WxSession) request.getAttribute(AppConfigure.BASOFT_WX_SESSION_PROP);
        String openId = wxSession.getOpenId();
        if (!openId.equals(custPostAddress.getWxOpenId())) {
            // 验证token所属openid和传递的openid是否一致，不一致表示非法调用
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 判断传值是否为空
        if(!custPostAddress.checkInput()){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        return custPostAddressRepository.saveCustPostAddress(custPostAddress);
    }

    /**
     * 平台客户修改配送地址
     *
     * @param custPostAddress
     * @param request
     * @return
     */
    @RequestMapping(value = "/updatePostAddress", method = RequestMethod.POST)
    @ResponseBody
    public long updatePostAddress(@RequestBody CustPostAddress custPostAddress, HttpServletRequest request) {
        // 修改时检查AddrId
        if(custPostAddress.getAddrId() == null || custPostAddress.getAddrId().longValue() <= 0){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 获取openId，检验接口调用合法性
        WxSession wxSession = (WxSession) request.getAttribute(AppConfigure.BASOFT_WX_SESSION_PROP);
        String openId = wxSession.getOpenId();
        if (!openId.equals(custPostAddress.getWxOpenId())) {
            // 验证token所属openid和传递的openid是否一致，不一致表示非法调用
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 判断传值是否为空
        if(!custPostAddress.checkInput()){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        return custPostAddressRepository.updateCustPostAddress(custPostAddress);
    }

    /**
     * 平台客户修改配送地址
     *
     * @param custPostAddress
     * @param request
     * @return
     */
    @RequestMapping(value = "/deletePostAddress", method = RequestMethod.POST)
    @ResponseBody
    public long deletePostAddress(@RequestBody CustPostAddress custPostAddress, HttpServletRequest request) {
        // 修改时检查AddrId
        if(custPostAddress.getAddrId() == null || custPostAddress.getAddrId().longValue() <= 0){
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 获取openId，检验接口调用合法性
        WxSession wxSession = (WxSession) request.getAttribute(AppConfigure.BASOFT_WX_SESSION_PROP);
        String openId = wxSession.getOpenId();
        if (!openId.equals(custPostAddress.getWxOpenId())) {
            // 验证token所属openid和传递的openid是否一致，不一致表示非法调用
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        return custPostAddressRepository.deleteCustPostAddress(custPostAddress);
    }

    /**
     * 商户设置的配送信息列表
     *
     * @param page
     * @param size
     * @param storeId
     * @param name
     * @return
     */
    @RequestMapping(value = "/postStoreSetList", method = RequestMethod.GET)
    @ResponseBody
    public Object postStoreSetList(@RequestParam(name = "page", defaultValue = "1") int page,
                                   @RequestParam(name = "size", defaultValue = "10") int size,
                                   @RequestParam(name = "storeId", defaultValue = "") String storeId,
                                   @RequestParam(name = "targetCountryCode", defaultValue = "") String targetCountryCode,
                                   @RequestParam(name = "isFree", defaultValue = "") String isFree,
                                   @RequestParam(name = "name", defaultValue = "") String name) {
        String query = "{PostStoreSets(storeId:\"" + storeId + "\",targetCountryCode:\""+targetCountryCode+"\",isFree:\""+isFree+"\",status:\""+1+"\",name:\""+name+"\",size:" + size + ",page:" + page + ")\n"+
                " {total,dataList{pssId,setNameChn,setNameKor,targetCountryName,targetCountryCode,isFree,freeAmount,setRule,status,\n"+
                " detailList {detailNo,lowerLimit,upperLimit,chargeFee}  }}}";

        Map postSetMap = (Map) getQueryHandler().handle(query);

        return postSetMap;
    }
    /**************************************************用户配送地址管理****************************************************/
}