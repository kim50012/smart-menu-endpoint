package com.basoft.eorder.interfaces.controller.h5.common;

import ch.qos.logback.core.CoreConstants;
import com.basoft.eorder.application.AppConfigure;
import com.basoft.eorder.application.UserSession;
import com.basoft.eorder.common.CommonConstants;
import com.basoft.eorder.domain.excel.ExcelColumn;
import com.basoft.eorder.domain.model.agent.Agent;
import com.basoft.eorder.interfaces.controller.CQRSAbstractController;
import com.basoft.eorder.interfaces.query.*;
import com.basoft.eorder.interfaces.query.agent.*;
import com.basoft.eorder.interfaces.query.excel.AdminDtlSettleExcel;
import com.basoft.eorder.interfaces.query.excel.AdminSettleExcel;
import com.basoft.eorder.interfaces.query.retail.cms.RetailOrderExcel;
import com.basoft.eorder.interfaces.query.retail.cms.RetailQuery;
import com.basoft.eorder.interfaces.query.retail.cms.ShipRetailOrderExcel;
import com.basoft.eorder.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 10:06 上午 2019/11/4
 **/

@Slf4j
@Controller
@RequestMapping("/excel/api/v1")
@ResponseBody
@Component
public class ExportExcelController extends CQRSAbstractController {
    @Autowired
    private OrderQuery orderQuery;
    @Autowired
    private SettleQuery sq;
    @Autowired
    private AgentQuery aq;
    @Autowired
    private StoreQuery storeQuery;
    @Autowired
    private RetailQuery retailQuery;

    @Override
    protected Object newQueryHandlerContext(HttpServletRequest request) {
        UserSession us = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
        Map<String, Object> context = new HashMap<>();
        context.put(AppConfigure.BASOFT_USER_SESSION_PROP, us);
        return context;
    }

    @Override
    protected Map<String, Object> newCommandHandlerContextMap(HttpServletRequest request) {
        UserSession userSession = (UserSession) request.getAttribute(AppConfigure.BASOFT_USER_SESSION_PROP);
        Map<String, Object> props = new HashMap<>();
        props.put(AppConfigure.BASOFT_USER_SESSION_PROP, userSession);
        return props;
    }


    /**
     * 导出admin店铺结算excel
     *
     * @Param
     * @return void
     * @author Dong Xifu
     * @date 2019/8/26 下午6:20
     */
    @RequestMapping(value = "/adminSettles", method = RequestMethod.GET)
    public void adminSettles(HttpServletResponse response, HttpServletRequest request
        , @RequestParam(name="storeId",defaultValue = "0",required = false)Long storeId
        , @RequestParam(name="storeName",required = false)String storeName
        , @RequestParam(name="storeType",required = false)String storeType
        , @RequestParam(name="city",required = false)String city
        , @RequestParam(name="cloStatus",required = false)String cloStatus
        ,@RequestParam(name="startTime")String startTime
        ,@RequestParam(name="endTime")String endTime
        , @RequestParam(name="language")String language
        , @RequestParam(name="page",defaultValue = "1")int page
        , @RequestParam(name="size",defaultValue = "2000000")int size
    ) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Map<String, Object> param = SetParamUtil.setBaseParam(storeId,storeName,storeType,city, page,size,new HashMap<>());
        SetParamUtil.setTimeParam(startTime, endTime, true,param);
        if (StringUtils.isNotBlank(cloStatus)) {
            param.put("cloStatus", cloStatus);
        }
        param.put("currentTime",startTime);

        List<String> topList = getAdminSettleTopList(language,false, param);

        List<SettleDTO> settleList = sq.getAdminSettleList(param);

        List<AdminSettleExcel> list = new LinkedList<>();
        for (SettleDTO dto : settleList) {
            if ("chn".equals(language)) {
                if ("1".equals(dto.getSettleType())) {
                    dto.setSettleTypeStr("按营业额百分比(" + dto.getChargeRate() + "%)");
                } else if ("2".equals(dto.getSettleType())) {
                    dto.setSettleTypeStr("按营业额百分比(" + dto.getChargeRate() + "%)或最小服务费");
                } else if ("3".equals(dto.getSettleType())) {
                    dto.setSettleTypeStr("按月(" + dto.getServiceFee() + ")");
                }
            } else {
                if ("1".equals(dto.getSettleType())) {
                    dto.setSettleTypeStr("영업 매출(" + dto.getChargeRate() + "%)");
                } else if ("2".equals(dto.getSettleType())) {
                    dto.setSettleTypeStr("영업 매출(" + dto.getChargeRate() + "%)최소 이용료");
                } else if ("3".equals(dto.getSettleType())) {
                    dto.setSettleTypeStr("월정액(" + dto.getServiceFee() + ")");
                }
                if (dto.getSettleType().equals("3")) {
                    dto.setIsPay("미사용");
                } else {
                    dto.setIsPay("사용");
                }
            }
            dto.setPayDt(dto.getPayFrDt()+"~"+dto.getPayToDt());
            AdminSettleExcel as = new AdminSettleExcel();
            BeanUtils.copyProperties(as, dto);
            as.setServiceFee(StringUtil.decimalStr(Long.valueOf(dto.getServiceFee())));
            as.setAmount(StringUtil.decimalStr(dto.getAmount().longValue()));
            as.setPgFee(StringUtil.decimalStr(Long.valueOf(dto.getPgFee())));
            as.setFinalFee(StringUtil.decimalStr(Long.valueOf(dto.getFinalFee())));
            as.setPlMinFee(StringUtil.decimalStr(Long.valueOf(dto.getPlMinFee())));
            list.add(as);
        }

        String month = String.valueOf(param.get("month"));
        //2019-10 平台结算目录
        String fileName = ExcelUtils.getFileName(language, month+"_",ExcelUtils.STORESETTLES_TYPE);
        String title = ExcelUtils.getTitle(language,month,"",ExcelUtils.STORESETTLES_TYPE);
        ExcelUtils.writeExcel(response,title,topList, list, AdminSettleExcel.class,fileName,language);
    }


    /**
     * 导出admin店铺结算详情excel
     *
     * @Param
     * @author Dong Xifu
     * @date 2019/11/4 3:07 下午
     */
    @RequestMapping(value = "/adminDtlSettles", method = RequestMethod.GET)
    public void adminDtlSettles(HttpServletResponse response
        ,@RequestParam(name="storeId")Long storeId
        ,@RequestParam(name="startTime")String startTime
        ,@RequestParam(name="endTime")String endTime
        , @RequestParam(name="language")String language
        , @RequestParam(name="page",defaultValue = "1")int page
        , @RequestParam(name="size",defaultValue = "2000000")int size
    ) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        Map<String, Object> param = SetParamUtil.setBaseParam(Long.valueOf(storeId), page,size,new HashMap<>());
        SetParamUtil.setTimeParam(startTime, endTime, false,param);
        param.put("currentTime",startTime);

        List topList = getAdminSettleTopList(language,true,param);
        List<SettleDTO> settleDtoList = sq.getSettleDtlListByMap(param);
        List<AdminDtlSettleExcel> list = new LinkedList<>();
        for(SettleDTO dto:settleDtoList){
            AdminDtlSettleExcel ads = new AdminDtlSettleExcel();
            BeanUtils.copyProperties(ads, dto);
            list.add(ads);
        }
        StoreDTO storeDTO = storeQuery.getStoreById(storeId);
        String month = String.valueOf(param.get("month"));
        String fileName = ExcelUtils.getFileName(language, month+"_"+storeDTO.getName()+"_",ExcelUtils.STORESETTLES_TYPE);
        String title = ExcelUtils.getTitle(language,month,"("+storeDTO.getName()+")",ExcelUtils.STORESETTLES_TYPE);

        ExcelUtils.writeExcel(response,title,topList, list, AdminDtlSettleExcel.class,fileName,language);
    }

    @RequestMapping(value = "/baAdminSettles", method = RequestMethod.GET)
    private void baAdminSettles() {

    }

    /**
     * 店铺头部总结算
     *
     * @param param
     * @return
     * @throws NoSuchFieldException
     */
    public List<String> getAdminSettleTopList(String language,Boolean isManager,Map<String, Object> param) throws NoSuchFieldException {
        SettleDTO settleDto = new SettleDTO();
        if (isManager) {
            settleDto = sq.getStoreSettleSum(param);
        } else {
            settleDto = sq.getAdminSettle(param);
        }

        List<String> topList = new LinkedList<>();
        ExcelColumn amountStr = SettleDTO.class.getDeclaredField("amount").getAnnotation(ExcelColumn.class);
        if(language.equals("kor")){
            topList.add(amountStr.valueKor());
        }else{
            topList.add(amountStr.valueChn());
        }

        BigDecimal amount = new BigDecimal(0);
        if (settleDto.getAmount() != null) {
            amount = settleDto.getAmount();
        }
        topList.add(StringUtil.decimalStr(amount.longValue())+"/"+settleDto.getPayCnt());

        ExcelColumn pgFeestr = SettleDTO.class.getDeclaredField("pgFee").getAnnotation(ExcelColumn.class);
        if(language.equals("kor")){
            topList.add(pgFeestr.valueKor());
        }else{
            topList.add(pgFeestr.valueChn());
        }

        String pgFee = "0";
        if (settleDto.getAmount() != null) {
            pgFee = settleDto.getPgFee();
        }

        String pgAmount = "0";
        if (settleDto.getPgAmount() != null) {
            pgAmount = settleDto.getPgAmount();
        }
        topList.add( StringUtil.decimalStr(Long.valueOf(pgFee))+ "/" + StringUtil.decimalStr(Long.valueOf(pgAmount)));

        ExcelColumn serviceFeeStr = SettleDTO.class.getDeclaredField("serviceFee").getAnnotation(ExcelColumn.class);
        if(language.equals("kor")){
            topList.add(serviceFeeStr.valueKor());
        }else{
            topList.add(serviceFeeStr.valueChn());
        }

        String serviceFee = "0";
        if (settleDto.getAmount() != null) {
            serviceFee = settleDto.getServiceFee();
        }
        String servFeeFormat = StringUtil.decimalObj(Double.valueOf(serviceFee));
        topList.add(servFeeFormat);
        return topList;
    }




    /**
     * 代理商结算
     *
     * @param response
     * @param request
     * @param agtName
     * @param agtType
     * @param agtCode
     * @param startTime
     * @param endTime
     * @param language
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    @RequestMapping(value = "/agentSettles", method = RequestMethod.GET)
    public void agentSettles(HttpServletResponse response, HttpServletRequest request
        ,@RequestParam(name="agtName",defaultValue = "",required = false)String agtName
        ,@RequestParam(name="agtType",defaultValue = "",required = false)String agtType
        ,@RequestParam(name="agtCode",defaultValue = "",required = false)String agtCode
        ,@RequestParam(name="startTime")String startTime
        ,@RequestParam(name="endTime")String endTime
        ,@RequestParam(name="language")String language
    ) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Map<String, Object> param = SetParamUtil.setTimeParam(startTime,endTime,true,new HashMap<>());
        param.put("agtName", agtName);
        param.put("agtType", agtType);
        param.put("agtCode", agtCode);

        List<AgentSettleExcel> list = new LinkedList<>();
        List<AgentStoreDTO> agtSettles = aq.getAgentStoreSettleList(param);
        for (AgentStoreDTO dto : agtSettles) {
            AgentSettleExcel ase = new AgentSettleExcel();
            ase.setSumAmount(StringUtil.decimalStr(dto.getSumAmount().longValue()));
            ase.setAgtFee(StringUtil.decimalStr(dto.getAgtFee().longValue()));
            ase.setVatFee(StringUtil.decimalStr(dto.getVatFee().longValue()));
            BeanUtils.copyProperties(ase, dto);
            list.add(ase);
        }
        List<String> topList = getAgtTopSettle(language,param);

        //String title = String.valueOf(param.get("month"))+"代理商结算";
        String month = String.valueOf(param.get("month"));
        String fileName = ExcelUtils.getFileName(language, month+"_",ExcelUtils.AGT_SETTLES_TYPE);
        String title = ExcelUtils.getTitle(language,month,"",ExcelUtils.AGT_SETTLES_TYPE);
        ExcelUtils.writeExcel(response,title,topList,list,AgentSettleExcel.class,fileName,language);
    }


    /**
     * 代理商结算订单列表
     *
     */
    @RequestMapping(value = "/agentOrders", method = RequestMethod.GET)
    public void agentOrders(HttpServletResponse response,HttpServletRequest request
        ,@RequestParam(name="agtId")Long agtId
        ,@RequestParam(name="storeName",defaultValue = "",required = false)String storeName
        ,@RequestParam(name="storeType",defaultValue = "",required = false)String storeType
        ,@RequestParam(name="city",defaultValue = "",required = false)String city
        ,@RequestParam(name="startTime")String startTime
        ,@RequestParam(name="endTime")String endTime
        ,@RequestParam(name="language")String language
    ) throws IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        Map<String, Object> param = SetParamUtil.setBaseParam(0L, storeName, storeType, city, 0, 0, new HashMap<>());
        SetParamUtil.setTimeParam(startTime,endTime,true,param);
        param.put("agtId", agtId);
        List<AgentOrderDTO> agtOrders = aq.getAgentOrderList(param);
        AgentDTO agent =  aq.getAgentById(agtId);

        List<String> topList = getAgtTopSettle(language,param);

        List<AgentOrderExcel> saList = new LinkedList<>();
        List<CaAgtOrderExcel> caList = new LinkedList<>();
        for (AgentOrderDTO dto : agtOrders) {
            if (agent.getAgtType() == Agent.SA_TP) {
                AgentOrderExcel aoe = new AgentOrderExcel();
                BeanUtils.copyProperties(aoe, dto);
                saList.add(aoe);
            } else {
                CaAgtOrderExcel coe = new CaAgtOrderExcel();
                BeanUtils.copyProperties(coe, dto);
                caList.add(coe);
            }
        }

        String month = String.valueOf(param.get("month"));
        String fileName = ExcelUtils.getFileName(language, agent.getAgtName()+"_"+month+"_",ExcelUtils.AGT_SETTLES_TYPE);
        String title = ExcelUtils.getTitle(language,month,"("+agent.getAgtName()+")",ExcelUtils.AGT_SETTLES_TYPE);
        if (agent.getAgtType() == Agent.SA_TP) {
            ExcelUtils.writeExcel(response, title, topList, saList, AgentOrderExcel.class, fileName, language);
        } else {
            ExcelUtils.writeExcel(response, title, topList, caList, CaAgtOrderExcel.class, fileName, language);
        }
    }

    /**
     * 代理商头部结算
     *
     * @param language
     * @param param
     * @return
     * @throws NoSuchFieldException
     */
    private List<String> getAgtTopSettle(String language, Map<String, Object> param) throws NoSuchFieldException {
        List<String> topList = new LinkedList<>();

        Map<String,Object> resultMap = aq.getAgtStoreOrderSum(param);
        String sumAmount = String.valueOf(resultMap.get("sumAmount"));
        sumAmount = StringUtil.decimalStr(Long.valueOf(sumAmount));
        String qty = String.valueOf(resultMap.get("qty"));
        String agtFee = String.valueOf(resultMap.get("agtFee"));
        agtFee = StringUtil.decimalStr(Long.valueOf(agtFee));
        String vatFee = String.valueOf(resultMap.get("vatFee"));
        vatFee = StringUtil.decimalStr(Long.valueOf(vatFee));

        ExcelColumn sumAmountStr = AgentOrderDTO.class.getDeclaredField("sumAmount").getAnnotation(ExcelColumn.class);
        if(language.equals("kor")){
            topList.add(sumAmountStr.valueKor());
        }else{
            topList.add(sumAmountStr.valueChn());
        }
        topList.add(sumAmount + "/" + qty);

        ExcelColumn agtFeeStr = AgentOrderExcel.class.getDeclaredField("agtFee").getAnnotation(ExcelColumn.class);
        if(language.equals("kor")){
            topList.add(agtFeeStr.valueKor());
        }else{
            topList.add(agtFeeStr.valueChn());
        }
        topList.add(agtFee+"/"+vatFee);
        return topList;
    }


    /**
     * 导出retail订单
     *
     * @param response
     * @param request
     * @param storeId
     * @param ids
     * @param title
     * @param status
     * @param language
     * @throws IOException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/retailOrders", method = RequestMethod.GET)
    public void retailOrders(HttpServletResponse response,HttpServletRequest request
            ,@RequestParam(name="storeId")String storeId
            ,@RequestParam(name="ids",defaultValue = "",required = false)String ids
            ,@RequestParam(name="id",defaultValue = "",required = false)String id
            ,@RequestParam(name="shippingType",defaultValue = "4,5")String shippingType
            ,@RequestParam(name="custNm",defaultValue = "",required = false)String custNm
            ,@RequestParam(name="mobile",defaultValue = "",required = false)String mobile
            ,@RequestParam(name="shippingAddrCountry",defaultValue = "",required = false)String shippingAddrCountry
            ,@RequestParam(name="startTime",defaultValue = "",required = false)String startTime
            ,@RequestParam(name="endTime",defaultValue = "",required = false)String endTime
            ,@RequestParam(name="spStartTime",defaultValue = "",required = false)String spStartTime
            ,@RequestParam(name="spEndTime",defaultValue = "",required = false)String spEndTime
            ,@RequestParam(name="status",defaultValue = "",required = false)String status
            ,@RequestParam(name="title",defaultValue = "",required = true)String title
            ,@RequestParam(name="page",defaultValue = "1",required = false)int page
            ,@RequestParam(name="size",defaultValue = "900000",required = false)int size
            ,@RequestParam(name="language")String language
    ) throws IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {


        Map<String, Object> param = new HashMap<>();
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            param =  SetParamUtil.setTimeParam(startTime,endTime,false,param);
        }

        if(StringUtils.isNotBlank(spStartTime) && StringUtils.isNotBlank(spEndTime)&&CommonConstants.SHIPPINGTYPE_SELF.equals(shippingType)) {
            spStartTime = DateFormatUtils.format(Long.valueOf(spStartTime), CoreConstants.DAILY_DATE_PATTERN);
            spEndTime = DateFormatUtils.format(Long.valueOf(spEndTime), CoreConstants.DAILY_DATE_PATTERN);
            param.put("spStartTime", spStartTime);
            param.put("spEndTime", spEndTime);
        }
        param =  SetParamUtil.setBaseParam(Long.valueOf(storeId),page,size,param);
        param.put("ids", ids);
        param.put("id", id);
        param.put("shippingType", shippingType);
        param.put("custNm", custNm);
        param.put("mobile", mobile);
        param.put("shippingAddrCountry", shippingAddrCountry);

        param.put("status", status);
        List<OrderDTO> retailOrders = retailQuery.getRetailOrderList(param);


        List<RetailOrderExcel> excels = new LinkedList<>();
        if(!CommonConstants.SHIPPINGTYPE_SELF.equals(shippingType)) {
            int number = 1;
            for (OrderDTO dto : retailOrders) {
                List<OrderItemDTO> itemList = dto.getItemList();
                RetailOrderExcel ce = new RetailOrderExcel();
                BeanUtils.copyProperties(ce, dto);
                ce.setNumer(String.valueOf(number));
                number += 1;
                if (itemList != null && itemList.size() > 0) {
                    ce.setProductNm(itemList.get(0).getProdNmKor());
                    ce.setQty(itemList.get(0).getQty());
                    ce.setPrice(itemList.get(0).getPrice());
                }
                ce.setMobile("+"+ce.getCountryNo()+" "+ce.getMobile());
                if (itemList != null && itemList.size() == 1) {
                    ce.setIsEnd("endRow");
                }
                ce.setStatus(setStatus(ce.getStatus(),language));
                excels.add(ce);
                if (itemList != null && itemList.size() > 0) {
                    for (int i = 1; i < itemList.size(); i++) {
                        OrderItemDTO itm = itemList.get(i);
                        RetailOrderExcel pro = new RetailOrderExcel();
                        pro.setProductNm(itm.getProdNmKor());
                        pro.setQty(itm.getQty());
                        pro.setPrice(itm.getPrice());
                        if (i == itemList.size() - 1) {
                            pro.setIsEnd("endRow");
                        }
                        excels.add(pro);
                    }
                }
            }
        }

        String today = DateUtil.getToday();

        String fileName = RetailExcelUtils.getFileName(language, today+"_",RetailExcelUtils.RETAIL_ORDER_TYPE);

        if (CommonConstants.SHIPPINGTYPE_SELF.equals(shippingType)) {
            List<ShipRetailOrderExcel> shipExcels = getShipExcels(retailOrders,language);
            RetailExcelUtils.writeExcel(response, today + " " + title, null, shipExcels, ShipRetailOrderExcel.class, fileName, language);
        } else {
            RetailExcelUtils.writeExcel(response, today + " " + title, null, excels, RetailOrderExcel.class, fileName, language);
        }

    }

    /**
     * 预约订单列表
     *
     * @param retailOrders
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private List<ShipRetailOrderExcel> getShipExcels(List<OrderDTO> retailOrders,String language ) throws InvocationTargetException, IllegalAccessException {
        List<ShipRetailOrderExcel> excels = new LinkedList<>();

        int number = 1;
        for (OrderDTO dto : retailOrders) {
            List<OrderItemDTO> itemList =  dto.getItemList();
            ShipRetailOrderExcel ce = new ShipRetailOrderExcel();
            BeanUtils.copyProperties(ce, dto);
            ce.setNumer(String.valueOf(number));
            number+=1;
            if(itemList!=null&&itemList.size()>0) {
                ce.setProductNm(itemList.get(0).getProdNmKor());
                ce.setQty(itemList.get(0).getQty());
                ce.setPrice(itemList.get(0).getPrice());
            }
            ce.setMobile("+"+ce.getCountryNo()+" "+ce.getMobile());
            ce.setStatus(setStatus(ce.getStatus(),language));
            if (itemList != null && itemList.size() == 1) {
                ce.setIsEnd("endRow");
            }
            if (dto.getShippingTime() == 1) {
                if ("kor".equals(language)) {
                    ce.setShippingDt(ce.getShippingDt() + " 오전");
                } else {
                    ce.setShippingDt(ce.getShippingDt() + " 上午");
                }

            } else if (dto.getShippingTime() == 2) {
                if ("kor".equals(language)) {
                    ce.setShippingDt(ce.getShippingDt() + " 오후");
                } else {
                    ce.setShippingDt(ce.getShippingDt() + " 下午");

                }
            }
            excels.add(ce);
            if(itemList!=null&&itemList.size()>0) {
                for (int i = 1; i < itemList.size(); i++) {
                    OrderItemDTO itm = itemList.get(i);
                    ShipRetailOrderExcel pro = new ShipRetailOrderExcel();
                    pro.setProductNm(itm.getProdNmKor());
                    pro.setQty(itm.getQty());
                    pro.setPrice(itm.getPrice());
                    if (i == itemList.size() - 1) {
                        pro.setIsEnd("endRow");
                    }
                    excels.add(pro);
                }
            }
        }
        return excels;
    }

    private String setStatus(String status,String language) {
        switch(status)
        {
            case "4" :
                if ("kor".equals(language)) {
                    status = "신규주문";
                } else {
                    status = "支付成功";
                }
                break;
            case "5" :
                if ("kor".equals(language)) {
                    status = "상품준비중";
                } else {
                    status = "订单接收";
                }
                break;
            case "6" :
                if ("kor".equals(language)) {
                    status = "tuiing";
                } else {
                    status = "退款进行中";
                }
            case "7" :
                if ("kor".equals(language)) {
                    status = "환불성공";
                } else {
                    status = "退款成功";
                }
                break;
            case "8" :
                if ("kor".equals(language)) {
                    status = "tuifail";
                } else {
                    status = "退款失败";
                }
                break;
            case "9" :
                if ("kor".equals(language)) {
                    status = "완료";
                } else {
                    status = "订单完成";
                }
                break;
            case "10" :
                if ("kor".equals(language)) {
                    status = "배송완료";
                } else {
                    status = "商品准备完成";
                }
                break;
            case "11" :
                if ("kor".equals(language)) {
                    status = "sendSuccess";
                } else {
                    status = "商品配送完成";
                }
                break;
        }
        return status;
    }


}
