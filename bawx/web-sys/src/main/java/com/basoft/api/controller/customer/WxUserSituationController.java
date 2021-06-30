package com.basoft.api.controller.customer;

import com.basoft.api.controller.BaseController;
import com.basoft.api.vo.ApiJson;
import com.basoft.api.vo.customer.LanguageVo;
import com.basoft.api.vo.customer.ProvinCityTotalVo;
import com.basoft.api.vo.customer.SexRadioVo;
import com.basoft.api.vo.customer.UserSituationVo;
import com.basoft.core.constants.CoreConstants;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.util.DateTimeUtil;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.customer.grade.CustGradeService;
import com.basoft.service.definition.customer.wxUser.WxUserService;
import com.basoft.service.dto.customer.*;
import com.basoft.service.param.customer.WxUserQueryParam;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * 客户分析
 * 
 * @Author:DongXifu
 * @Description:
 * @Date Created in 下午2:54 2018/4/27
 **/
@RestController
public class WxUserSituationController extends BaseController{
    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private CustGradeService custGradeService;

    /**
     * 关注现况统计
     * 
     * @param startTime
     * @param endTime
     * @param followType
     * @return
     * @throws ParseException
     * @author Dong Xifu
     * @Date 2018/4/27 下午5:55
     */
    @RequestMapping(value = "/selectFollowSituation",method = RequestMethod.GET)
    public UserSituationVo selectFollowSituation(@RequestParam(value = "startTime",required = false) String startTime,
                                                 @RequestParam (value = "endTime",required = false) String endTime,
                                                 @RequestParam (value = "followType",defaultValue = "1",required = true) String followType) throws ParseException {
		// 参数检验
		if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}
    	
		// 一、查询昨日关键指标---start
		// 昨日
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		// 明日
		Calendar calEnd = Calendar.getInstance();
		calEnd.add(Calendar.DATE, +1);
		String tomorrowDay = new SimpleDateFormat("yyyy-MM-dd").format(calEnd.getTime());
		
		// 设置查询条件
		WxUserQueryParam yesterdayQueryParam = new WxUserQueryParam();
		yesterdayQueryParam.setShopId(getShopId());
		yesterdayQueryParam.setStartTime(yesterday);
		yesterdayQueryParam.setEndTime(tomorrowDay);

		// 新增粉丝数
		int sumNewFans = wxUserService.sumNewFans(yesterdayQueryParam);
		
		// 取消关注数
		int sumOutFans = wxUserService.sumOutFans(yesterdayQueryParam);
		
		// 粉丝净增数
		int growFans = sumNewFans - sumOutFans;
		
		// 全部粉丝数
		int totalFans = wxUserService.totalFans(yesterdayQueryParam);
		// 查询昨日关键指标---end
        
		// 二、查询日期区间内的每日关注情况---start
		// （1）封装查询条件
		WxUserQueryParam queryParam = new WxUserQueryParam();
		queryParam.setShopId(getShopId());
		Long st = Long.valueOf(startTime);
		Long et = Long.valueOf(endTime);
		startTime = DateFormatUtils.format(st, "yyyy-MM-dd");
		endTime = DateFormatUtils.format(et, "yyyy-MM-dd");
		queryParam.setStartTime(startTime);
		queryParam.setEndTime(endTime);
		queryParam.setFollowType(followType);
		queryParam.setRows(200);
		// （2）查询日期区间内的每日关注情况（新增、取消、净增）
		PageInfo<WxUserSituationDto> wxUserPage = wxUserService.selectSituationDto(queryParam);
		// 构建所有日期
		List<String> dataAll = DateTimeUtil.findDataAll(startTime, endTime, 1);
		// 将没有数据的日期放入wxUserDtosList中
		if(wxUserPage.getList()==null||wxUserPage.getList().size()<1)
			edtWxUserDtosList(startTime, endTime, wxUserPage.getList(), dataAll);



		// 查询日期区间内的每日关注情况---end
		
		// 三、封装返回到的页面的数据---start
		UserSituationVo vo = new UserSituationVo();
		// （1）昨日关键指标
		vo.setSumNewFans(sumNewFans);
		vo.setSumOutFans(sumOutFans);
		vo.setSumGrowFans(growFans);
		vo.setSumTotalFans(totalFans);
		// （2）趋势图
		vo.setUserDtoSituatList(wxUserPage.getList());
		vo.setDate(dataAll);
		// （3）
		ApiJson<List<?>> pageUser = new ApiJson<>();
		vo.setPageUser(pageUser);

		// 封装返回到的页面的数据---end
        return vo;
    }

    /**
     * 获取用户统计 分页
     * 
     * @param startTime
     * @param endTime
     * @param followType
     * @param page
     * @param rows
     * @return
     * @author Dong Xifu
     * @date 2018/5/28 下午2:15
     */
    @RequestMapping(value = "/selectFollowSituationTable",method = RequestMethod.GET)
    public ApiJson<List<?>> selectFollowSituationTable(@RequestParam(value = "startTime",required = false) String startTime,
                                                 @RequestParam (value = "endTime",required = false)String endTime,
                                                 @RequestParam (value = "followType",defaultValue = "1",required = true)String followType,
                                                 @RequestParam(value = "page",defaultValue = "1" )int page,
                                                 @RequestParam(value = "rows",defaultValue = "20" )int rows ) throws ParseException {
		// 封装查询条件
    	WxUserQueryParam queryParam = new WxUserQueryParam();
		queryParam.setShopId(getShopId());
		startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
		endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
		queryParam.setStartTime(startTime);
		queryParam.setEndTime(endTime);
		queryParam.setFollowType(followType);
		queryParam.setPage(page);
		queryParam.setRows(rows);
		ApiJson<List<?>> pageUser = new ApiJson<>();
		try {
			// 查询日期区间内的每日关注情况
			PageInfo<WxUserSituationDto> wxUserPage = wxUserService.selectSituationDto(queryParam);
			// 构建所有日期
			//ArrayList<String> dataAll = DateTimeUtil.findDataAll(startTime, endTime, 1);
			// 将没有数据的日期放入wxUserDtosList中
			//edtWxUserDtosList(startTime, endTime, wxUserPage.getList(), dataAll);

			if (wxUserPage != null && CollectionUtils.isNotEmpty(wxUserPage.getList())) {
				pageUser.setPage(wxUserPage.getPageNum());
				pageUser.setRecords((int) wxUserPage.getTotal());
				pageUser.setTotal(wxUserPage.getPages());
				pageUser.setRows(wxUserPage.getList());
			} else {
				pageUser.setPage(1);
				pageUser.setRecords(0);
				pageUser.setTotal(0);
				pageUser.setRows(new ArrayList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		pageUser.setErrorCode(0);
		pageUser.setErrorMsg("Success");

		return pageUser;
	}
    
	/**
	 * 将没有查询统计数据的日期放入列表
	 * 
	 * @param startTime
	 * @param endTime
	 * @param wxUserDtosList
	 * @param dataAll
	 * @return
	 */
	private List<WxUserSituationDto> edtWxUserDtosList(String startTime, String endTime, List<WxUserSituationDto> wxUserDtosList, List<String> dataAll) {
		if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			for (String str : dataAll) {
				boolean flag = true;
				for (int i = 0; i < wxUserDtosList.size(); i++) {
					WxUserSituationDto dtoY = wxUserDtosList.get(i);
					if (str.equals(dtoY.getFollowDt())) {
						flag = false;
						break;
					}
				}
				if (flag) {
					WxUserSituationDto dto = new WxUserSituationDto();
					dto.setFollowDt(str);
					dto.setNewFans(0);
					dto.setGrowFans(0);
					dto.setOutFans(0);
					// TODO 可考虑优化算法设置未有数据日期的累计关注人数取最近过往日期的累计关注人数，减少数据库IO

					WxUserQueryParam totalParm = new WxUserQueryParam();
					totalParm.setShopId(getShopId());
					if (dto.getFollowDt() != null) {
						totalParm.setShopId(getShopId());
						totalParm.setEndTime(dto.getFollowDt());
						int dayTotalFans = wxUserService.totalFans(totalParm);
						dto.setTotalFans(dayTotalFans);
					}
					wxUserDtosList.add(dto);
				}
			}
			
			// 排序
			Collections.sort(wxUserDtosList, (s1, s2) -> {
				int i = s1.getFollowDt().compareTo(s2.getFollowDt());
				if (i > 0) {
					return -1;
				} else if (i == 0) {
					return 0;
				} else {
					return 1;
				}
			});
		}
		return wxUserDtosList;
	}

	/**
	 * 内存分页
	 * 
	 * @param pageNo 当前页码
	 * @param pageSize 每页条数
	 * @param list 所有集合
	 * @return
	 */
	private ApiJson<List<?>> pageWxUserList(int pageNo, int pageSize, List<WxUserSituationDto> list) {
		ApiJson<List<?>> resultPage = new ApiJson<>();
		List<WxUserSituationDto> result = new ArrayList<WxUserSituationDto>();
		if (list != null && list.size() > 0) {
			// 总条数
			int allCount = list.size();
			// 总页数
			int pageCount = (allCount + pageSize - 1) / pageSize;
			// 当前页码大于总页数，则当前页码设置为最后一页
			if (pageNo >= pageCount) {
				pageNo = pageCount;
			}
			// 起始索引
			int start = (pageNo - 1) * pageSize;
			// 结束索引
			int end = pageNo * pageSize;
			// 如果结束索引大于总条数，则结束索引设置为总条数
			if (end >= allCount) {
				end = allCount;
			}
			// 放入结果列表
			result.addAll(list.subList(start, end));
			
			// 分页结果集
			resultPage.setRows(result);
			// 总页数
			resultPage.setTotal(allCount);
			// 当前页码
			resultPage.setPage(pageNo);
			// 总记录数
			resultPage.setRecords(allCount);
			return resultPage;
		}
		return resultPage;
	}

	/**
	 * 查询性别比例统计
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
     * @author DONG XIFU
     * @Date 2018/4/28 下午6:25
	 */
	@RequestMapping(value = "/selectSexRadio", method = RequestMethod.GET)
	public Echo<?> selectSexRadio(@RequestParam(value = "startTime", required = false) String startTime, 
								@RequestParam(value = "endTime", required = false) String endTime) {
		// 封装查询条件
		WxUserQueryParam param = new WxUserQueryParam();
		param.setShopId(getShopId());
		if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
			endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.DATE_FORMAT_YYYY_MM_DD);
			param.setStartTime(startTime);
			param.setEndTime(endTime);
		}
		
		// CustSexRadioDto custSexRadioDto = custGradeService.selectSexRadio(param);
		List<WxUserTotalProvinDto> listSexRido = custGradeService.selectSexRadioList(param);
		return new Echo<>(new SexRadioVo(null, listSexRido));
	}

	/**
	 * 语种统计
	 * @return
     * @author DONG XIFU
     * @date 2018/5/15 下午1:34
	 */
	@RequestMapping(value = "/totalLanguigeType", method = RequestMethod.GET)
	public Echo<?> totalLanguigeType() {
		WxUserTotalLangDto dto = wxUserService.totalLanguigeType(getShopId());
		List<WxUserTotalProvinDto> list = wxUserService.selectLanguigeTypeList(getShopId());
		return new Echo<>(new LanguageVo(list, dto));
	}

    /**
     * 省市统计
     * 
     * @author DONG XIFU
     * @date 2018/5/16 下午4:49
     */
	@RequestMapping(value = "/totalProvinCity", method = RequestMethod.GET)
	public Echo<?> totalProvinCity() {
		List<WxUserTotalProvinDto> provinDtoList = wxUserService.totalProvince(getShopId());
		List<WxUserTotalCityDto> cityDtoList = wxUserService.totalCity(getShopId());
		List<WxUserProvinceAllDto> allProList = provinceAll();
		allProList = provinceDataAll(allProList, provinDtoList);
		return new Echo<>(new ProvinCityTotalVo(provinDtoList, cityDtoList, allProList));
	}

    /**
     * 初始化全量省份列表
     * 
     * @return
     */
    private List<WxUserProvinceAllDto> provinceAll(){
            String proArr [] = {"北京","天津","上海","重庆","河北","河南","云南","辽宁","黑龙江","湖南",
            "安徽","山东","新疆","江苏","浙江","江西","湖北","广西","甘肃","山西","内蒙古","陕西","吉林",
                "福建","贵州","广东","青海","西藏","四川","宁夏","海南","台湾","香港","澳门","未知地域"};
		List<WxUserProvinceAllDto> proAllList = new ArrayList<>();
		for (String pro : proArr) {
			WxUserProvinceAllDto dto = new WxUserProvinceAllDto();
			dto.setName(pro);
			dto.setValue(0);
			dto.setProportion(new BigDecimal(0));
			proAllList.add(dto);
		}
		return proAllList;
	}

	/**
	 * 查询数据放入全量省份列表
	 * 
	 * @param provinceAllList
	 * @param provinDataList
	 * @return
	 */
	private List<WxUserProvinceAllDto> provinceDataAll(List<WxUserProvinceAllDto> provinceAllList, List<WxUserTotalProvinDto> provinDataList) {
		for (int i = 0; i < provinceAllList.size(); i++) {
			WxUserProvinceAllDto proAllDto = provinceAllList.get(i);
			for (int j = 0; j < provinDataList.size(); j++) {
				WxUserTotalProvinDto dataPro = provinDataList.get(j);
				if (proAllDto.getName().equals(dataPro.getName())) {
					proAllDto.setValue(dataPro.getValue());
					proAllDto.setProportion(dataPro.getProportion());
					//provinceAllList.set(i, proAllDto);
					//i++;
					//continue;
					break;
				}
			}
		}
		return provinceAllList;
	}
}
