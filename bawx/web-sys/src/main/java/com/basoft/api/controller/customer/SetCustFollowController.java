package com.basoft.api.controller.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.basoft.api.controller.BaseController;
import com.basoft.api.vo.ApiJson;
import com.basoft.api.vo.customer.CustGradeVo;
import com.basoft.core.exception.BizException;
import com.basoft.core.exception.ErrorCode;
import com.basoft.core.web.vo.Echo;
import com.basoft.service.definition.customer.grade.CustGradeService;
import com.basoft.service.dto.customer.CustGradeDto;
import com.basoft.service.param.customer.CustGradeQueryParam;
import com.basoft.service.param.customer.CustShopParam;
import com.github.pagehelper.PageInfo;

/**
 * @Author:DongXifu
 * @Description: 设置关注
 * @Date Created in 下午2:59 2018/4/25
 **/
@RestController
public class SetCustFollowController extends BaseController{
    @Autowired
    private CustGradeService custGradeService;
    
    //@Autowired
    //private CustShopService custShopService;
    
    //@Autowired
    //private GradeMstService gradeMstService;

	/**
	 * 分页查询客户列表
	 * 
	 * @param page
	 * @param rows
	 * @param param
	 * @return
	 * @author Dong Xifu
     * @Date 2018/4/25 下午6:09
	 */
	@RequestMapping(value = "/custGradeFindAll", method = RequestMethod.GET)
	public ApiJson<List<CustGradeVo>> custGradeFindAll(@RequestParam(value = "page", defaultValue = "1") String page,
			@RequestParam(value = "rows", defaultValue = "20") String rows,
			@RequestParam(value = "param", defaultValue = "") String param) {
		ApiJson<List<CustGradeVo>> result = new ApiJson<>();
		
		// 封装查询条件
		CustGradeQueryParam queryParam = new CustGradeQueryParam();
		queryParam.setShopId(getShopId());
		queryParam.setPage(Integer.parseInt(page));
		queryParam.setRows(Integer.parseInt(rows));
		// 昵称或等级名称
		queryParam.setParam(param);
		
		try {
			// 根据查询条件进行查询
			PageInfo<CustGradeDto> pageInfo = custGradeService.findAllCustGrade(queryParam);
			
			// 处理查询结果
			/*for (CustGradeDto dto : pageInfo.getList()) {
				CustShop custShop = custShopService.getCustShop(getShopId(), dto.getCustSysId());
				if (custShop != null) {
					GradeMst gradeMst = gradeMstService.getGradeMst(getShopId(), custShop.getGradeId());
					if (gradeMst != null) {
						dto.setGradeNm(gradeMst.getGradeNm());
					}
					dto.setGradeId(custShop.getGradeId());
				}
			}*/
			if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
				result.setPage(pageInfo.getPageNum());
				result.setRecords((int) pageInfo.getTotal());
				result.setTotal(pageInfo.getPages());
				result.setRows(pageInfo.getList().stream().map(data -> new CustGradeVo(data)).collect(Collectors.toList()));
			} else {
				result.setPage(1);
				result.setRecords(0);
				result.setTotal(0);
				result.setRows(new ArrayList<CustGradeVo>());
			}
			result.setErrorCode(0);
			result.setErrorMsg("Success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 设置客户等级
	 * 
	 * @param custShopParam
	 * @return
     * @author Dong Xifu
     * @Date 2018/4/26 上午9:24
	 */
	@PostMapping(value = "/setGradeBycust", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Echo<?> setGradeBycust(@RequestBody CustShopParam custShopParam) {
		if (custShopParam.getGradeId() == null || custShopParam.getCustList().size() < 1) {
			throw new BizException(ErrorCode.PARAM_MISSING);
		}
		custShopParam.setShopId(getShopId());
		int result = custGradeService.setGradeBycust(custShopParam);
		return new Echo<Integer>(result);
	}
}
