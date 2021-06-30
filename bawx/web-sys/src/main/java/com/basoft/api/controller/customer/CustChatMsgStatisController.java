package com.basoft.api.controller.customer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.basoft.service.entity.wechat.wxMessage.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.basoft.api.controller.BaseController;
import com.basoft.api.vo.ApiJson;
import com.basoft.core.constants.CoreConstants;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.util.DateTimeUtil;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.customer.cust.custShop.CustService;
import com.basoft.service.param.wechat.wxMessage.WxMessageQueryParam;
import com.github.pagehelper.PageInfo;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午1:21 2018/5/18
 **/
@RestController
public class CustChatMsgStatisController extends BaseController{
    @Autowired
    private CustService custService;

    /**
     * 统计用户消息发送次数 (表格)
     * 
     * @param timeType  0-日报 1-小时 2-周报
     * @param startTime
     * @param endTime
     * @param page
     * @param rows
     * @return com.basoft.api.vo.ApiJson<java.util.List>
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/selectCustMsgStaticTable",method = RequestMethod.GET)
	public ApiJson<List> selectCustMsgStaticTable(@RequestParam(defaultValue = "0") Byte timeType,
												@RequestParam(value = "startTime") String startTime,
												@RequestParam(value = "endTime") String endTime,
												@RequestParam(value = "page", defaultValue = "1") String page,
												@RequestParam(value = "rows", defaultValue = "20") String rows) {
    	// 校验参数
        if(StringUtils.isBlank(endTime)||StringUtils.isBlank(startTime)){
            throw new BizException(ErrorCode.TIME_NULL);
        }
        
        // 封装查询条件
        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
            endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
        }
        ApiJson<List> result = new ApiJson<>();
        WxMessageQueryParam queryParam = new WxMessageQueryParam();
        queryParam.setShopId(getShopId());
        queryParam.setTimeType(timeType);
        queryParam.setStartTime(startTime);
        queryParam.setEndTime(endTime);
        queryParam.setPage(Integer.parseInt(page));
        queryParam.setRows(Integer.parseInt(rows));
        
        // 查询
		try {
			PageInfo<WxIfStreamMsgStatsData> dataPageInfo = custService.selectCustMsgStaticTable(queryParam);
			if (dataPageInfo != null && dataPageInfo.getTotal() > 0) {
				if (timeType == 1) {
					dataPageInfo.setList(setHourList(dataPageInfo.getList()));
				}
				result.setPage(dataPageInfo.getPageNum());
				result.setTotal(dataPageInfo.getPages());
				result.setRows(dataPageInfo.getList());
				result.setRecords((int) dataPageInfo.getTotal());
			} else {
				result.setPage(1);
				result.setRecords(0);
				result.setTotal(0);
				result.setRows(new ArrayList());
			}
			result.setErrorMsg("success");
			result.setErrorCode(0);
		} catch (Exception e) {
			result.setErrorMsg("fail");
			result.setErrorCode(1);
			e.printStackTrace();
		}
		return result;
    }

    /**
     * 统计用户消息发送次数chart图表
     * 
     * @param timeType 0-日报 1-小时 2-周报
     * @param startTime
     * @param endTime
     * @return com.basoft.api.vo.ApiJson<java.util.List>
     * @author DONG XIFU
     * @date 2018/5/18 下午4:25
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/selectCustMsgStatic",method = RequestMethod.GET)
    public ApiJson<List> selectCustMsgStatic(@RequestParam(defaultValue = "0")Byte timeType,
                                             @RequestParam(value = "startTime")String startTime,
                                             @RequestParam(value = "endTime")String endTime){
    	// 校验参数
		if (StringUtils.isBlank(endTime) || StringUtils.isBlank(startTime)) {
			throw new BizException(ErrorCode.TIME_NULL);
		}
		ApiJson<List> result = new ApiJson<>();

		// 封装查询条件
		if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
			endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
		}
		WxMessageQueryParam queryParam = new WxMessageQueryParam();
		queryParam.setShopId(getShopId());
		queryParam.setTimeType(timeType);
		queryParam.setStartTime(startTime);
		queryParam.setEndTime(endTime);
		try {
			List<WxIfStreamMsgStatsData> list = custService.selectCustMsgStatic(queryParam);
			boolean timeFlag = true;
			// 小时
			if (timeType == 1) {
				timeFlag = false;
			}
			ArrayList<String> dataAll = new ArrayList<>();
			if (timeFlag == true) {
				// 查询所有日期
				dataAll = DateTimeUtil.findDataAll(startTime, endTime, 1);
			} else {
				// 查询一天之间的所有小时（小时）
				dataAll = DateTimeUtil.findHourAll();
			}
			list = setMsgStatsDataList(dataAll, list, timeFlag);
			if (list != null && list.size() > 0) {
				result.setRecords(list.size());
				result.setRows(list);
			} else {
				result.setRecords(0);
				result.setRows(new ArrayList());
			}
			result.setErrorMsg("success");
			result.setErrorCode(0);
		} catch (Exception e) {
			result.setErrorMsg("fail");
			result.setErrorCode(1);
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * 将没有数据的日期放入list中
	 * 
	 * @param dataAll
	 * @param list
	 * @param timeFlag
	 * @return java.util.List<com.basoft.service.entity.wechat.wxMessage.WxIfStreamMsgStatsData>
	 */
	private List<WxIfStreamMsgStatsData> setMsgStatsDataList(ArrayList<String> dataAll, List<WxIfStreamMsgStatsData> list, Boolean timeFlag) {
		for (String str : dataAll) {
			WxIfStreamMsgStatsData entity = new WxIfStreamMsgStatsData();
			Boolean flag = true;
			for (WxIfStreamMsgStatsData data : list) {
				if (timeFlag) {
					if (str.equals(data.getRefDate())) {
						flag = false;
					}
				} else {
					if (str.equals(data.getRefHour())) {
						flag = false;
					}
				}
			}
			if (timeFlag) {
				if (flag) {
					entity.setRefDate(str);
					entity.setMsgUser(0);
					entity.setMsgCount(0);
					list.add(entity);
				}
			} else {
				if (flag) {
					entity.setRefHour(str);
					entity.setMsgUser(0);
					entity.setMsgCount(0);
					list.add(entity);
				}
			}
		}
		list = setHourList(list);// 转化小时
		Collections.sort(list, (s1, s2) -> {
			int i = 0;
			if (timeFlag) {
				if (s1 != null && s2 != null) {
					i = s1.getRefDate().compareTo(s2.getRefDate());
				}
			} else {
				if (s1 != null && s2 != null && s1.getRefHour() != null && s2.getRefHour() != null) {
					i = s1.getRefHour().compareTo(s2.getRefHour());
				}
			}
			if (i > 0) {
				return -1;
			} else if (i == 0) {
				return 0;
			} else {
				return 1;
			}
		});
		return list;
	}

	/**
	 * 设置小时
	 * 
	 * @param list
	 * @return java.util.List<com.basoft.service.entity.wechat.wxMessage.WxIfStreamMsgStatsData>
	 */
	private List<WxIfStreamMsgStatsData> setHourList(List<WxIfStreamMsgStatsData> list) {
		for (WxIfStreamMsgStatsData detail : list) {
			if ("000".equals(detail.getRefHour())) {
				detail.setRefHour("00:00");
			} else if ("100".equals(detail.getRefHour())) {
				detail.setRefHour("01:00");
			} else if ("200".equals(detail.getRefHour())) {
				detail.setRefHour("02:00");
			} else if ("300".equals(detail.getRefHour())) {
				detail.setRefHour("03:00");
			} else if ("400".equals(detail.getRefHour())) {
				detail.setRefHour("04:00");
			} else if ("500".equals(detail.getRefHour())) {
				detail.setRefHour("05:00");
			} else if ("600".equals(detail.getRefHour())) {
				detail.setRefHour("06:00");
			} else if ("700".equals(detail.getRefHour())) {
				detail.setRefHour("07:00");
			} else if ("800".equals(detail.getRefHour())) {
				detail.setRefHour("08:00");
			} else if ("900".equals(detail.getRefHour())) {
				detail.setRefHour("09:00");
			} else if ("1000".equals(detail.getRefHour())) {
				detail.setRefHour("10:00");
			} else if ("1100".equals(detail.getRefHour())) {
				detail.setRefHour("11:00");
			} else if ("1200".equals(detail.getRefHour())) {
				detail.setRefHour("12:00");
			} else if ("1300".equals(detail.getRefHour())) {
				detail.setRefHour("13:00");
			} else if ("1400".equals(detail.getRefHour())) {
				detail.setRefHour("14:00");
			} else if ("1500".equals(detail.getRefHour())) {
				detail.setRefHour("15:00");
			} else if ("1600".equals(detail.getRefHour())) {
				detail.setRefHour("16:00");
			} else if ("1700".equals(detail.getRefHour())) {
				detail.setRefHour("17:00");
			} else if ("1800".equals(detail.getRefHour())) {
				detail.setRefHour("18:00");
			} else if ("1900".equals(detail.getRefHour())) {
				detail.setRefHour("19:00");
			} else if ("2000".equals(detail.getRefHour())) {
				detail.setRefHour("20:00");
			} else if ("2100".equals(detail.getRefHour())) {
				detail.setRefHour("21:00");
			} else if ("2200".equals(detail.getRefHour())) {
				detail.setRefHour("22:00");
			} else if ("2300".equals(detail.getRefHour())) {
				detail.setRefHour("23:00");
			}
		}
		return list;
	}

	/**
	 * 统计人均发送条数和区间条数
	 * 
	 * @param timeType 0-日报 1-小时 2-周报
	 * @param startTime
	 * @param endTime
	 * @return com.basoft.api.vo.ApiJson<java.util.List>
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/selectCountIntervalPercent", method = RequestMethod.GET)
	public ApiJson<List> selectCountIntervalPercent(@RequestParam(value = "timeType", defaultValue = "0") Byte timeType,
			@RequestParam(value = "startTime") String startTime,
			@RequestParam(value = "endTime") String endTime) {
		// 校验参数
		if (StringUtils.isBlank(endTime) || StringUtils.isBlank(startTime)) {
			throw new BizException(ErrorCode.TIME_NULL);
		}
		
		// 转换参数
		if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
			endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
		}
		ApiJson<List> result = new ApiJson<>();
		try {
			List<WxIfMsgCountPercent> list = custService.selectCountIntervalPercent(getShopId(), timeType, startTime, endTime);
			if (list != null && list.size() > 0) {
				result.setRows(list);
				result.setRecords(list.size());
			} else {
				result.setRecords(0);
				result.setRows(new ArrayList());
			}
			result.setErrorCode(0);
			result.setErrorMsg("success");
		} catch (Exception e) {
			result.setErrorCode(1);
			result.setErrorMsg("系统异常:" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 统计昨日客户聊天总数
	 * 
	 * @param timeType
	 * @return com.basoft.service.entity.wechat.wxMessage.WxIfMsgSumYestoday
	 */
	@RequestMapping(value = "/selectMsgSumYestoday", method = RequestMethod.GET)
	public Echo<?> selectMsgSumYestoday(@RequestParam(defaultValue = "0") Byte timeType) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
		
		Calendar calEnd = Calendar.getInstance();
		calEnd.add(Calendar.DATE, 0);
		String tomorrowDay = new SimpleDateFormat("yyyy-MM-dd ").format(calEnd.getTime());
		
		WxIfMsgSumYestoday wxIfMsgSumYestoday = new WxIfMsgSumYestoday();
		wxIfMsgSumYestoday = custService.selectMsgSumYestoday(getShopId(), timeType, yesterday, tomorrowDay);
		return new Echo<>(wxIfMsgSumYestoday);
	}

    /**
     * @param  [timeType, startTime, endTime]
     * @return com.basoft.api.vo.ApiJson<java.util.List>
     * @describe 统计消息发送次数 周报
     * @author Dong Xifu
     * @date 2018/5/23 下午5:41
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/selectCustMsgStaticTableWeek",method = RequestMethod.GET)
    public ApiJson<List> selectCustMsgStaticTableWeek(@RequestParam(defaultValue = "0")Byte timeType,
                                                  @RequestParam(value = "startTime")String startTime,
                                                  @RequestParam(value = "endTime")String endTime){

        if(StringUtils.isBlank(endTime)||StringUtils.isBlank(startTime)){
            throw new BizException(ErrorCode.TIME_NULL);
        }
        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
            endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD)+1;
        }
        ApiJson<List> result = new ApiJson<>();
        try {
            List<WxIfStreamMsgStatsDataWeek> list = custService.selectCustMsgStaticWeek(getShopId(), timeType,startTime,endTime);
            if(list!=null&&list.size()>0){
                result.setRecords(list.size());
                result.setRows(list);
            }else{
                result.setRecords(0);
                result.setRows(new ArrayList());
            }
            result.setErrorMsg("success");
            result.setErrorCode(0);
        }catch (Exception e){
            result.setErrorMsg("fail");
            result.setErrorCode(1);
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @describe 统计用户消息发送次数chart图表 周报
     * @param  [timeType, startTime, endTime]
     * @return com.basoft.api.vo.ApiJson<java.util.List>
     * @author Dong Xifu
     * @date 2018/5/18 下午4:25
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/selectCustMsgStaticWeek",method = RequestMethod.GET)
    public ApiJson<List> selectCustMsgStaticWeek(@RequestParam(defaultValue = "0")Byte timeType,
                                             @RequestParam(value = "startTime")String startTime,
                                             @RequestParam(value = "endTime")String endTime){

        if(StringUtils.isBlank(endTime)||StringUtils.isBlank(startTime)){
            throw new BizException(ErrorCode.TIME_NULL);
        }
        if(StringUtils.isNotBlank(startTime)&&StringUtils.isNotBlank(endTime)){
            startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
            endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
        }
        ApiJson<List> result = new ApiJson<>();
        try {
            List<WxIfStreamMsgStatsDataWeek> list = custService.selectCustMsgStaticWeek(getShopId(), timeType,startTime,endTime);
            boolean timeFlag = false;
            if (timeType == 0) {
                timeFlag = true;
            } else {
                timeFlag = false;
            }
            ArrayList<String> dataAll = new ArrayList<>();
            dataAll = DateTimeUtil.findDataAll(startTime, endTime,7);//查询所有日期

            list = setMsgStatsDataListWeek(dataAll,list);

            if(list!=null&&list.size()>0){
                result.setRecords(list.size());
                result.setRows(list);
            }else{
                result.setRecords(0);
                result.setRows(new ArrayList());
            }
            result.setErrorMsg("success");
            result.setErrorCode(0);
        }catch (Exception e){
            result.setErrorMsg("fail");
            result.setErrorCode(1);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param  [dataAll, list]
     * @return java.util.List<com.basoft.service.entity.wechat.wxMessage.WxIfStreamMsgStatsData>
     * @describe 将没有数据的日期放入list中 周报
     * @author Dong Xifu
     * @date 2018/5/21 下午2:33
     */
    private List<WxIfStreamMsgStatsDataWeek> setMsgStatsDataListWeek(ArrayList<String> dataAll, List<WxIfStreamMsgStatsDataWeek> list) {
        for (String str : dataAll) {
            WxIfStreamMsgStatsDataWeek entity = new WxIfStreamMsgStatsDataWeek();
            Boolean flag = true;
            for (WxIfStreamMsgStatsDataWeek data : list) {
                if (str.equals(data.getRefDate())) {
                    flag = false;
                }
            }
            if (flag) {
                entity.setRefDate(str);
                entity.setMsgUser(0);
                entity.setMsgCount(0);
                list.add(entity);
            }
        }
        Collections.sort(list, (s1, s2) -> {
            int i = 0;
            i = s1.getRefDate().compareTo(s2.getRefDate());
            if (i > 0) {
                return -1;
            } else if (i == 0) {
                return 0;
            } else {
                return 1;
            }
        });
        return list;
    }

    /**
     * @describe 统计人均发送条数 和区间条数 周
     * @param  [timeType, startTime, endTime]
     * @return com.basoft.api.vo.ApiJson<java.util.List>
     * @author Dong Xifu
     * @date 2018/5/18 下午5:29
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/selectCountIntervalPercentWeek", method = RequestMethod.GET)
    public ApiJson<List> selectCountIntervalPercentWeek(@RequestParam(value = "timeType",defaultValue = "0") Byte timeType,
                                                    @RequestParam(value = "startTime") String startTime,
                                                    @RequestParam(value = "endTime") String endTime) {
        if (StringUtils.isBlank(endTime) || StringUtils.isBlank(startTime)) {
            throw new BizException(ErrorCode.TIME_NULL);
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
            endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
        }

        ApiJson<List> result = new ApiJson<>();

        try {
            List<WxIfMsgCountPercent> list = custService.selectCountIntervalPercentWeek(getShopId(), timeType, startTime, endTime);
            if (list != null && list.size() > 0) {
                result.setRows(list);
                result.setRecords(list.size());
            } else {
                result.setRecords(0);
                result.setRows(new ArrayList());
            }
            result.setErrorMsg("success");
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrorMsg("系统异常");
        }
        return result;
    }

	/**
	 * @Param
	 * @return com.basoft.api.vo.ApiJson<java.util.List>
	 * @describe 关键字统计
	 * @author Dong Xifu
	 * @date 2019/2/28 下午1:46
	 */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/findCustMsgKeyStatic",method = RequestMethod.GET)
    public ApiJson<List> findCustMsgKeyStatic(@RequestParam(defaultValue = "0") Byte keyType,
                                                  @RequestParam(value = "startTime") String startTime,
                                                  @RequestParam(value = "endTime") String endTime,
                                                  @RequestParam(value = "page", defaultValue = "1") String page,
                                                  @RequestParam(value = "rows", defaultValue = "20") String rows) {

        // 校验参数
        if (StringUtils.isBlank(endTime) || StringUtils.isBlank(startTime)) {
            throw new BizException(ErrorCode.TIME_NULL);
        }

        // 转换参数
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
            endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
        }
        ApiJson<List> result = new ApiJson<>();
		WxMessageQueryParam param = new WxMessageQueryParam();
		param.setShopId(getShopId());
		param.setKeyType(keyType);
		param.setStartTime(startTime);
		param.setEndTime(endTime);

		try {
			PageInfo<WxKeyMsgStats> dataPageInfo = custService.findCustMsgKeyStatic(param);
			result.setPage(dataPageInfo.getPageNum());
			result.setTotal(dataPageInfo.getPages());
			result.setRows(dataPageInfo.getList());
			result.setRecords((int) dataPageInfo.getTotal());
			result.setErrorMsg("success");
			result.setErrorCode(0);
		}catch (Exception e){
			result.setErrorMsg("fail");
			result.setErrorCode(1);
			e.printStackTrace();
		}

        return result;
    }
}