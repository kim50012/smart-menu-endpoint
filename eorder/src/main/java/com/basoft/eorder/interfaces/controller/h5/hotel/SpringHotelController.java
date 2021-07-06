package com.basoft.eorder.interfaces.controller.h5.hotel;

import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.BizException;
import com.basoft.eorder.application.ErrorCode;
import com.basoft.eorder.application.WxSession;
import com.basoft.eorder.application.framework.CommandHandleEngine;
import com.basoft.eorder.application.framework.QueryHandler;
//import com.basoft.eorder.batch.job.threads.HotelOrderRecoverThread;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.InventoryHotelRepository;
import com.basoft.eorder.interfaces.controller.CQRSAbstractController;
import com.basoft.eorder.interfaces.query.OrderQuery;
import com.basoft.eorder.interfaces.query.PaginationDTO;
import com.basoft.eorder.interfaces.query.ProductQuery;
import com.basoft.eorder.interfaces.query.hotel.HotelProductSkuDatePriceDTO;
import com.basoft.eorder.interfaces.query.hotel.HotelStoreQuery;
import com.basoft.eorder.interfaces.query.hotel.HotelStoreQueryDTO;
import com.basoft.eorder.interfaces.query.hotel.HotelStoreVO;
import com.basoft.eorder.interfaces.query.inventory.hotel.InventoryHotelQuery;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Wechat/MiniProgram/H5 Hotel API 公众号端酒店类业务API
 *
 * @version 1.0
 * @Date 20191122
 **/
@Slf4j
@Controller
@RequestMapping("/wechat/api/v2/h")
public class SpringHotelController extends CQRSAbstractController {
    @Autowired
    private HotelStoreQuery hotelStoreQuery;

    @Autowired
    private InventoryHotelQuery inventoryHotelQuery;

    @Autowired
    private ProductQuery productQuery;

    @Autowired
    private OrderQuery orderQuery;

    @Autowired
    private InventoryHotelRepository inventoryHotelRepository;

    @Autowired
    private AppConfigure appConfigure;

    @Autowired
    public SpringHotelController(
            QueryHandler queryHandler,
            CommandHandleEngine handleEngine) {
        super(queryHandler, handleEngine);
    }

    /**
     * Wechat/MiniProgram/H5
     * 酒店首页查询接口/hotel main page api
     * <p>
     * 提醒：前端注意处理无可用状态的往后显示
     *
     * @param request
     * @param hotelStoreQueryDTO
     * @return
     */

    /* 传参示例一
    {
        "channel": "wechat",
        "city": "0017",
        "longitude": "116.53889",
        "latitude": "40.099156",
        "storeType": "4",
        "startDate": "2019-12-10",
        "endDate": "2019-12-15",
        "key": "乐天",
        "orderBy": "1",
        "orderSort": "1",
        "page": 1,
        "size": 20
    }
     */

    /* 传参示例二
    {
        "city": "0017",
        "longitude": "116.53889",
        "latitude": "40.099156",
        "startDate": "2019-12-10",
        "endDate": "2019-12-15",
        "key": "乐天",
        "orderBy": "1",
        "orderSort": "1"
    }
     */

    /* 传参示例三
        {
            "channel": "wechat",
            "city": "0017",
            "longitude": "116.53889",
            "latitude": "40.099156",
            "storeType": "4",
            "startDate": "2019-12-10",
            "endDate": "2019-12-15",
            "key": "乐天",
            "minDistance":"0",
            "maxDistance":"100000000",
            "minPrice":"0",
            "maxPrice":"100000",
            "comfortLevels":["HOTEL_COMFORT_LEVEL@5","HOTEL_COMFORT_LEVEL@4","HOTEL_COMFORT_LEVEL@3","HOTEL_COMFORT_LEVEL@2"],
            "storeExtends":["BREAKFAST@1","HOTEL_DINING_ROOM@1","HOTEL_PARK@1","HOTEL_TRANSFER@1"],
            "orderBy": "1",
            "orderSort": "1",
            "page": 1,
            "size": 20,
            "qmd":"c"
        }

        距离价格传值规范：1、最小距离和最大距离 2、最大距离；其他情况不允许。如0-150,150-300,300-600,600+
        星级和扩展属性传值规范：字段对应的编码@字段值编码，如HOTEL_COMFORT_LEVEL@5,HOTEL_BREAKFAST@1
     */
    @RequestMapping(value = "/hotelmain", method = RequestMethod.POST)
    @ResponseBody
    public PaginationDTO hotelMain(HttpServletRequest request, @RequestBody HotelStoreQueryDTO hotelStoreQueryDTO) {
        log.info("酒店主页商户列表查询接口接收到的参数HotelStoreQueryDTO>>>>>>{}", hotelStoreQueryDTO);

        // 核心参数验证
        String city = hotelStoreQueryDTO.getCity();
        String startDate = hotelStoreQueryDTO.getStartDate();
        String endDate = hotelStoreQueryDTO.getEndDate();
        if (city == null || "".equals(city.trim()) || startDate == null || "".equals(startDate.trim()) ||
                endDate == null || "".equals(endDate.trim())) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 处理默认值。channel/page/size
        // 转换order by和order sort
        hotelStoreQueryDTO.initValue();

        // 嵌套查询：对一级结果过滤查询
        int dataCount = 0;
        if ("c".equals(hotelStoreQueryDTO.getQmd())) {
            // 获取酒店数量
            dataCount = hotelStoreQuery.getHotelStoreComplexCount(hotelStoreQueryDTO);
            if (dataCount == 0) {
                return new PaginationDTO<>(0, new ArrayList<>());
            }

            // 查询酒店列表-默认按距离排序，如果没有授权地理位置，则按默认排序
            List<HotelStoreVO> hotelStoreList = hotelStoreQuery.getHotelStoreComplexList(hotelStoreQueryDTO);
            return new PaginationDTO<>(dataCount, hotelStoreList);
        }
        // 简洁查询
        else {
            // 获取酒店数量
            dataCount = hotelStoreQuery.getHotelStoreSimpleCount(hotelStoreQueryDTO);
            if (dataCount == 0) {
                return new PaginationDTO<>(0, new ArrayList<>());
            }

            // 查询酒店列表-默认按距离排序，如果没有授权地理位置，则按默认排序
            List<HotelStoreVO> hotelStoreList = hotelStoreQuery.getHotelStoreSimpleList(hotelStoreQueryDTO);
            return new PaginationDTO<>(dataCount, hotelStoreList);
        }
    }

    /**
     * 酒店商户主页的产品信息、折扣信息及库存信息查询
     *
     * @param request
     * @param startDate
     * @param endDate
     * @param priceType 1-最低价格 2-均价 3- 4-
     * @return
     */
    @RequestMapping(value = "/hotel", method = RequestMethod.GET)
    @ResponseBody
    public Object hotelMainDataListWithDiscount(HttpServletRequest request,
                                                @RequestParam(name = "startDate", defaultValue = "") String startDate,
                                                @RequestParam(name = "endDate", defaultValue = "") String endDate,
                                                @RequestParam(name = "pt", defaultValue = "1") String priceType) {
        log.info("店铺列表查询参数>>>>>>startDate||endDate>>>" + startDate + "||" + endDate);
        if (startDate == null || "".equals(startDate.trim()) || endDate == null || "".equals(endDate.trim())) {
            throw new BizException(ErrorCode.PARAM_INVALID);
        }

        // 1、门店ID
        WxSession wxSession = (WxSession) request.getAttribute(AppConfigure.BASOFT_WX_SESSION_PROP);
        long storeId = wxSession.getStoreId();

        // 2、查询酒店商户的房间信息（状态不为2或3的房间，即在线的房间）
        String query = "{ProductGroups(storeId:" + storeId + "){dataList{id, nameKor, nameChn, storeId, status, showIndex, created}}, ProductGroupMaps(storeId:" + storeId + ",status:\"" + 1 + "\"){id, storeId, categoryId, nameKor, nameChn,detailDesc,detailChnDesc,created,desChn,desKor, fileId, fileName, fileSysName, fileType, fileUrl, fileSize, fileOriginalName, productId, prdGroupId, showIndex, mainUrl, subImageUrl,recommend, psdList{id, nameKor, nameChn, productId, priceKor, priceChn, useDefault, created}}}";
        log.info(query);
        Map map = (Map) getQueryHandler().handle(query);
        if (map == null) {
            return null;
        }
        // 房间分组组列表
        List<Map> groups = (List<Map>) ((Map) map.get("ProductGroups")).get("dataList");
        // 房间（产品SKU）列表
        List<Map> products = ((List<Map>) map.get("ProductGroupMaps"));

        // 3、查询产品决定是否可预定的库存信息:从指定日期区间内取出各个sku库存最小的那条库存信息，
        // 如20191227-20191231，sku9099在1228这天的库存余量最小，则取该日期的库存记录
        List<LinkedHashMap<String,Object>> hotelInventoryList = null;
        StringBuilder queryHotelInventory = new StringBuilder("{InventoryHotelsByConditions(storeId:\"");
        queryHotelInventory.append(storeId).append("\",")
                .append("startDate").append(":\"").append(startDate).append("\",")
                .append("endDate").append(":\"").append(endDate).append("\")")
                .append("{invId,storeId,prodId,prodSkuId,price,disPrice,invYear,invMonth,invDay,invDate,isOpening,invTotal,invUsed,invBalance}}");
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

        // 4、4-1查询所有产品的所有sku的日期期间内的价格
        List<HotelProductSkuDatePriceDTO> productSkuDatePriceList = inventoryHotelQuery.getProductSkuDatePriceList(storeId,
                startDate,endDate);
        // 4-2按照prod sku分组
        Map<Long, List<HotelProductSkuDatePriceDTO>> productSkuDatePriceMap = productSkuDatePriceList.stream().collect(
                Collectors.groupingBy(hotelProductSkuDatePriceDTO ->
                        NumberUtils.toLong(Objects.toString(hotelProductSkuDatePriceDTO.getProdSkuId())))
        );

        // 5、将库存信息和价格信息融入产品列表-start
        // 产品循环
        products.stream().forEach(productMap -> {
            // 取出产品SKU列表
            List<Map> prodSkuList = (List<Map>) productMap.get("psdList");
            // 产品SKU列表循环
            prodSkuList.stream().forEach(psdMap -> {
                // 获取prod sku id
                Long prodSkuId = (Long) psdMap.get("id");

                // 根据prod sku id获取决定性库存信息。前端根据库存余量判断该房间是否可以预订，等于0不可预订，大于0可以预订。
                LinkedHashMap<String, Object> inventorySku = hotelInventoryDisplayMap.get(prodSkuId);
                if (inventorySku != null && inventorySku.size() > 0) {
                    //psdMap.put("isOpening", inventorySku.get("isOpening"));
                    //psdMap.put("invTotal", inventorySku.get("invTotal"));
                    //psdMap.put("invUsed", inventorySku.get("invUsed"));
                    psdMap.put("invBalance", inventorySku.get("invBalance"));
                } else {
                    //psdMap.put("isOpening", "1");
                    //psdMap.put("invTotal", "10000");
                    //psdMap.put("invUsed", "0");
                    psdMap.put("invBalance", "10000");
                }

                // 根据prod sku id获取日期期间每天的价格信息，放入sku信息主体中。
                // 即使该sku综合运算（查看各个天数isopening是否为1）得到的isopening是0，此处依然传值，以便前端计算最低价用于显示
                List<HotelProductSkuDatePriceDTO> hotelProductSkuDatePriceMap = productSkuDatePriceMap.get(prodSkuId);
                if (hotelProductSkuDatePriceMap != null && hotelProductSkuDatePriceMap.size() > 0) {
                    psdMap.put("prodSkuDatePriceList", hotelProductSkuDatePriceMap);
                }
            });
        });
        // 将库存信息和价格信息融入产品列表-end

        // 6、产品列表按照产品组ID进行分组
        Map<Long, List<Map>> groupMap = products.stream().collect(Collectors.groupingBy(product ->
                NumberUtils.toLong(Objects.toString(product.get("prdGroupId"), null))
        ));

        // 7、将产品列表放入产品组中形成新的产品组groupList（其中每个分组包含自己组的产品列表）
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

        // 8、8-1从全部产品列表products中过滤出热销产品列表hotProducts
        List<Map> hotProducts = products.stream().filter((hotMap) -> {
            Integer recommend = (Integer) hotMap.get("recommend");
            return recommend != null && recommend == 1;
        }).collect(Collectors.toList());

        // 8-2将热销产品列表hotProducts放入“热销分组”，并将该分组放入总分组groupList
        if (0 < hotProducts.size()) {
            //Map hotGroup = hotOfGroup(NumberUtils.toLong("1")).toMap();
            Map hotGroup = hotOfGroupMap(storeId);
            hotGroup.put("products", hotProducts);
            groupList.add(0, hotGroup);
            // return Lists.asList(hotGroup, groupList.toArray());
        }

        // 9、放入汇率
        Map hlMap = Maps.newHashMap();
        hlMap.put("exchangeRate", productQuery.getNowExchangeRate());
        groupList.add(hlMap);

        // 10、return
        return groupList;
    }

    private Map hotOfGroupMap(Long storeId) {
        Map groupDTO = new LinkedHashMap();
        groupDTO.put("id", (long) -1);
        groupDTO.put("nameKor", "추천");
        groupDTO.put("nameChn", "人气热销");
        groupDTO.put("storeId", storeId);
        groupDTO.put("status", 1);
        groupDTO.put("showIndex", 0);
        groupDTO.put("created", java.util.Calendar.getInstance().getTime().toString());
        return groupDTO;
    }

    /**
     * hotel pay fail or cancel，进行该作废订单所占库存的释放
     *
     * @param request
     * @param params
     * {
     *     "o":"1002423423566664"
     * }
     * @return
     */
    @RequestMapping(value = "/hpf", method = RequestMethod.POST)
    @ResponseBody
    public String hotelPayFail(HttpServletRequest request, @RequestBody Map<String, String> params) {
        // 下单未支付订单编号
        String tempOrderId = params.get("o");
        if (StringUtils.isBlank(tempOrderId)) {
            return "fail";
        }

        List<Map<String, Object>> tempOrderList = orderQuery.queryTempOrderByIdToRecover(tempOrderId);
        if (tempOrderList == null || tempOrderList.isEmpty()) {
            return "fail";
        } else {
            // 启动各个库存恢复线程
            for (Map<String, Object> tempOrder : tempOrderList) {
                try {
//                    Thread thread = new Thread(new HotelOrderRecoverThread(inventoryHotelRepository,
//                            tempOrder, CommonConstants.HOTEL_INVENTORY_RECOVER_AUTO, appConfigure));
//                    thread.start();
                    return "success";
                } catch (Exception e) {
                    log.error("【酒店预订下单未支付订单所占库存实时恢复】酒店库存恢复线程异常，异常信息为：" + e.getMessage(), e);
                }
            }
        }
        return "fail";
    }
}